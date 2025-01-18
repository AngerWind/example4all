/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.tiger.timewheel.kafka;

import org.apache.kafka.common.utils.KafkaThread;
import org.apache.kafka.common.utils.Time;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SystemTimer implements Timer {
    // 用来执行TimerTask任务
    private final ExecutorService taskExecutor;
    private final DelayQueue<TimerTaskList> delayQueue;
    private final AtomicInteger taskCounter;
    private final TimingWheel timingWheel;

    // Locks used to protect data structures while ticking
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    public SystemTimer(String executorName) {
        this(executorName, 1, 20, Time.SYSTEM.hiResClockMs());
    }

    public SystemTimer(String executorName, long tickMs, int wheelSize, long startMs) {
        this.taskExecutor =
            Executors.newFixedThreadPool(1, runnable -> KafkaThread.nonDaemon("executor-" + executorName, runnable));
        this.delayQueue = new DelayQueue<>();
        this.taskCounter = new AtomicInteger(0);
        this.timingWheel = new TimingWheel(tickMs, wheelSize, startMs, taskCounter, delayQueue);
    }

    // 可能会多个线程操作，所以需要加锁
    public void add(TimerTask timerTask) {
        readLock.lock();
        try {
            // 返回false并且任务未取消，则提交当前任务立即执行。
            addTimerTaskEntry(new TimerTaskEntry(timerTask, timerTask.delayMs + Time.SYSTEM.hiResClockMs()));
        } finally {
            readLock.unlock();
        }
    }

    private void addTimerTaskEntry(TimerTaskEntry timerTaskEntry) {
        // 添加entry失败, 有可能是任务已经过期了, 需要立即执行
        // 也有可能是取消了
        if (!timingWheel.add(timerTaskEntry)) {
            if (!timerTaskEntry.cancelled()) {
                // 过期任务立即执行
                taskExecutor.submit(timerTaskEntry.timerTask);
            }
        }
    }

    /**
     * Advances the clock if there is an expired bucket. If there isn't any expired bucket when called, waits up to
     * timeoutMs before giving up.
     */
    // 向前驱动时间轮
    public boolean advanceClock(long timeoutMs) throws InterruptedException {
        // 使用阻塞队列获取任务
        TimerTaskList bucket = delayQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
        if (bucket != null) {
            writeLock.lock();
            try {
                while (bucket != null) {
                    timingWheel.advanceClock(bucket.getExpiration());
                    // 驱动时间后，需要移动TimerTaskList到上一个槽或者从上一层移动到本层
                    bucket.flush(this::addTimerTaskEntry);
                    bucket = delayQueue.poll();
                }
            } finally {
                writeLock.unlock();
            }
            return true;
        } else {
            return false;
        }
    }

    public int size() {
        return taskCounter.get();
    }

    @Override
    public void close() {
        taskExecutor.shutdown();
    }
}
