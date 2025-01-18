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

import org.apache.kafka.common.utils.Time;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

class TimerTaskList implements Delayed {
    private final Time time;
    // 当前列表中包含的任务数
    private final AtomicInteger taskCounter;
    // 任务需要执行的时间点
    private final AtomicLong expiration;

    // TimerTaskList forms a doubly linked cyclic list using a dummy root entry
    // root.next points to the head
    // root.prev points to the tail
    // 列表的头结点
    private final TimerTaskEntry root;

    TimerTaskList(AtomicInteger taskCounter) {
        this(taskCounter, Time.SYSTEM);
    }

    TimerTaskList(AtomicInteger taskCounter, Time time) {
        this.time = time;
        this.taskCounter = taskCounter;
        this.expiration = new AtomicLong(-1L);
        this.root = new TimerTaskEntry(null, -1L);
        this.root.next = root;
        this.root.prev = root;
    }

    // 给当前槽设置过期时间
    public boolean setExpiration(long expirationMs) {
        return expiration.getAndSet(expirationMs) != expirationMs;
    }

    public long getExpiration() {
        return expiration.get();
    }

    // 用于遍历当前列表中的任务
    public synchronized void foreach(Consumer<TimerTask> f) {
        TimerTaskEntry entry = root.next;
        while (entry != root) {
            TimerTaskEntry nextEntry = entry.next;
            if (!entry.cancelled())
                f.accept(entry.timerTask);
            entry = nextEntry;
        }
    }

    // todo
    public void add(TimerTaskEntry timerTaskEntry) {
        boolean done = false;
        while (!done) {
            // Remove the timer task entry if it is already in any other list
            // We do this outside of the sync block below to avoid deadlocking.
            // We may retry until timerTaskEntry.list becomes null.
            timerTaskEntry.remove();

            synchronized (this) {
                synchronized (timerTaskEntry) {
                    if (timerTaskEntry.list == null) {
                        // put the timer task entry to the end of the list. (root.prev points to the tail entry)
                        TimerTaskEntry tail = root.prev;
                        timerTaskEntry.next = root;
                        timerTaskEntry.prev = tail;
                        timerTaskEntry.list = this;
                        tail.next = timerTaskEntry;
                        root.prev = timerTaskEntry;
                        taskCounter.incrementAndGet();
                        done = true;
                    }
                }
            }
        }
    }

    // 移出任务
    public synchronized void remove(TimerTaskEntry timerTaskEntry) {
        synchronized (timerTaskEntry) {
            if (timerTaskEntry.list == this) {
                timerTaskEntry.next.prev = timerTaskEntry.prev;
                timerTaskEntry.prev.next = timerTaskEntry.next;
                timerTaskEntry.next = null;
                timerTaskEntry.prev = null;
                timerTaskEntry.list = null;
                taskCounter.decrementAndGet();
            }
        }
    }

    public synchronized void flush(Consumer<TimerTaskEntry> f) {
        TimerTaskEntry head = root.next;
        while (head != root) {
            remove(head);
            f.accept(head);
            head = root.next;
        }
        expiration.set(-1L);
    }

    // 获取当前bucket的剩余时间
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(Math.max(getExpiration() - time.hiResClockMs(), 0), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        TimerTaskList other = (TimerTaskList)o;
        return Long.compare(getExpiration(), other.getExpiration());
    }
}
