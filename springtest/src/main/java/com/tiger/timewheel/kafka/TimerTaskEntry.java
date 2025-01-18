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

public class TimerTaskEntry {

    // 包含一个任务
    public final TimerTask timerTask;
    // 任务需要执行的时间, 延迟间隔+系统当前时间(毫秒)
    // 如果小于当前时间, 说明任务已经过期
    public final long expirationMs;


    // 当前任务属于哪一个列表
    volatile TimerTaskList list;
    // 当前任务的上一个任务，用双向列表连接
    TimerTaskEntry next;
    TimerTaskEntry prev;

    @SuppressWarnings("this-escape")
    public TimerTaskEntry(TimerTask timerTask, long expirationMs) {
        this.timerTask = timerTask;
        this.expirationMs = expirationMs;

        // if this timerTask is already held by an existing timer task entry,
        // setTimerTaskEntry will remove it.
        // 传递进来任务TimerTask，并设置TimerTask的包装类
        if (timerTask != null) {
            timerTask.setTimerTaskEntry(this);
        }
    }

    // 任务的取消，就是判断任务TimerTask的Entry是否是当前任务
    public boolean cancelled() {
        return timerTask.getTimerTaskEntry() != this;
    }

    // 任务的移出
    public void remove() {
        TimerTaskList currentList = list;
        // If remove is called when another thread is moving the entry from a task entry list to another,
        // this may fail to remove the entry due to the change of value of list. Thus, we retry until the list becomes
        // null.
        // In a rare case, this thread sees null and exits the loop, but the other thread insert the entry to another
        // list later.
        while (currentList != null) {
            // 从TimerTaskList中将当前entry移除出去
            currentList.remove(this);
            currentList = list;
        }
    }
}
