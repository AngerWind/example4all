package com.tiger.timewheel.netty;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2023/12/26
 * @description
 */

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.util.internal.PlatformDependent;

// https://zhuanlan.zhihu.com/p/32906730
public class HashedWheelTimer implements Timer {

    static final Logger logger =
            LoggerFactory.getLogger(HashedWheelTimer.class);

    // HashedWheelTimer实例的个数
    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger();
    // 是否限制HashedWheelTimer实例的个数
    private static final AtomicBoolean WARNED_TOO_MANY_INSTANCES = new AtomicBoolean();
    // 因为时间轮是整个jvm中的共享资源, 应该进行复用, 所以会限制创建的时间轮的个数
    private static final int INSTANCE_COUNT_LIMIT = 64;

    // 每个bucket能够表示的最小时间间隔, 如果用户设置的时间间隔小于1ms, 就会使用1ms作为每个bucket的时间间隔
    private static final long MILLISECOND_NANOS = TimeUnit.MILLISECONDS.toNanos(1);


    // 推动时间前进的任务
    private final HashedWheelTimer.Worker worker = new HashedWheelTimer.Worker();
    // 执行worker的线程
    private final Thread workerThread;

    // 时间轮状态标志位, 0初始化, 1正在运行, 2结束
    public static final int WORKER_STATE_INIT = 0;
    public static final int WORKER_STATE_STARTED = 1;
    public static final int WORKER_STATE_SHUTDOWN = 2;
    // 当前时间轮的状态, 初始状态为0
    private volatile int workerState = 0; // 0 - init, 1 - started, 2 - shut down
    // AtomicIntegerFieldUpdater是JUC里面的类，原理是利用安全的反射进行原子操作，来获取实例的本身的属性。
    // 有比AtomicInteger更好的性能和更低得内存占用
    private static final AtomicIntegerFieldUpdater<HashedWheelTimer> WORKER_STATE_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimer.class, "workerState");

    // 轮上每个bucket代表的时间间隔, 纳秒
    private final long tickDuration;
    // 轮上的每个槽位
    private final HashedWheelTimer.HashedWheelBucket[] wheel;
    private final int mask;

    // Mpsc表示Multi Producer Single Consumer
    // Queue不是普通java自带的Queue的实现，而是使用JCTool–一个高性能的的并发Queue实现包。

    // 所有添加到时间轮中的task, 需要再下一个tick时才会被挂载到对应的bucket上, 减少并发消耗
    private final Queue<HashedWheelTimeout> timeouts = PlatformDependent.newMpscQueue();
    // 所有取消的任务放到下一个tick时, 再进行处理, 可以防止并发, 减少消耗
    private final Queue<HashedWheelTimeout> cancelledTimeouts = PlatformDependent.newMpscQueue();


    private final AtomicLong pendingTimeouts = new AtomicLong(0);
    // 时间轮中能够添加的最大任务个数, 如果为0或者负数表示不限制
    private final long maxPendingTimeouts;

    // 执行任务的线程池
    // 在时间轮结束后应该手动关闭线程池!!!!!!!!!!!!!!!!!!
    private final Executor taskExecutor;

    // 时间轮的启动时间(纳秒), 当时间轮没有start的时候, startTime=0
    private volatile long startTime;
    // 主线程调用start后, 必须等startTime初始化完毕后才退出
    private final CountDownLatch startTimeInitialized = new CountDownLatch(1);

    /**
     * Creates a new timer with the default thread factory
     * ({@link Executors#defaultThreadFactory()}), default tick duration, and
     * default number of ticks per wheel.
     */
    public HashedWheelTimer() {
        this(Executors.defaultThreadFactory());
    }

    /**
     * Creates a new timer with the default thread factory
     * ({@link Executors#defaultThreadFactory()}) and default number of ticks
     * per wheel.
     *
     * @param tickDuration the duration between tick
     * @param unit         the time unit of the {@code tickDuration}
     * @throws NullPointerException     if {@code unit} is {@code null}
     * @throws IllegalArgumentException if {@code tickDuration} is &lt;= 0
     */
    public HashedWheelTimer(long tickDuration, TimeUnit unit) {
        this(Executors.defaultThreadFactory(), tickDuration, unit);
    }

    /**
     * Creates a new timer with the default thread factory
     * ({@link Executors#defaultThreadFactory()}).
     *
     * @param tickDuration  the duration between tick
     * @param unit          the time unit of the {@code tickDuration}
     * @param ticksPerWheel the size of the wheel
     * @throws NullPointerException     if {@code unit} is {@code null}
     * @throws IllegalArgumentException if either of {@code tickDuration} and {@code ticksPerWheel} is &lt;= 0
     */
    public HashedWheelTimer(long tickDuration, TimeUnit unit, int ticksPerWheel) {
        this(Executors.defaultThreadFactory(), tickDuration, unit, ticksPerWheel);
    }

    /**
     * Creates a new timer with the default tick duration and default number of
     * ticks per wheel.
     *
     * @param threadFactory a {@link ThreadFactory} that creates a
     *                      background {@link Thread} which is dedicated to
     *                      {@link TimerTask} execution.
     * @throws NullPointerException if {@code threadFactory} is {@code null}
     */
    public HashedWheelTimer(ThreadFactory threadFactory) {
        this(threadFactory, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * Creates a new timer with the default number of ticks per wheel.
     *
     * @param threadFactory a {@link ThreadFactory} that creates a
     *                      background {@link Thread} which is dedicated to
     *                      {@link TimerTask} execution.
     * @param tickDuration  the duration between tick
     * @param unit          the time unit of the {@code tickDuration}
     * @throws NullPointerException     if either of {@code threadFactory} and {@code unit} is {@code null}
     * @throws IllegalArgumentException if {@code tickDuration} is &lt;= 0
     */
    public HashedWheelTimer(
            ThreadFactory threadFactory, long tickDuration, TimeUnit unit) {
        this(threadFactory, tickDuration, unit, 512);
    }

    /**
     * Creates a new timer.
     *
     * @param threadFactory a {@link ThreadFactory} that creates a
     *                      background {@link Thread} which is dedicated to
     *                      {@link TimerTask} execution.
     * @param tickDuration  the duration between tick
     * @param unit          the time unit of the {@code tickDuration}
     * @param ticksPerWheel the size of the wheel
     * @throws NullPointerException     if either of {@code threadFactory} and {@code unit} is {@code null}
     * @throws IllegalArgumentException if either of {@code tickDuration} and {@code ticksPerWheel} is &lt;= 0
     */
    public HashedWheelTimer(
            ThreadFactory threadFactory,
            long tickDuration, TimeUnit unit, int ticksPerWheel) {
        this(threadFactory, tickDuration, unit, ticksPerWheel, true);
    }

    /**
     * Creates a new timer.
     *
     * @param threadFactory a {@link ThreadFactory} that creates a
     *                      background {@link Thread} which is dedicated to
     *                      {@link TimerTask} execution.
     * @param tickDuration  the duration between tick
     * @param unit          the time unit of the {@code tickDuration}
     * @param ticksPerWheel the size of the wheel
     * @param leakDetection {@code true} if leak detection should be enabled always,
     *                      if false it will only be enabled if the worker thread is not
     *                      a daemon thread.
     * @throws NullPointerException     if either of {@code threadFactory} and {@code unit} is {@code null}
     * @throws IllegalArgumentException if either of {@code tickDuration} and {@code ticksPerWheel} is &lt;= 0
     */
    public HashedWheelTimer(
            ThreadFactory threadFactory,
            long tickDuration, TimeUnit unit, int ticksPerWheel, boolean leakDetection) {
        this(threadFactory, tickDuration, unit, ticksPerWheel,  -1);
    }

    /**
     * Creates a new timer.
     *
     * @param threadFactory        a {@link ThreadFactory} that creates a
     *                             background {@link Thread} which is dedicated to
     *                             {@link TimerTask} execution.
     * @param tickDuration         the duration between tick
     * @param unit                 the time unit of the {@code tickDuration}
     * @param ticksPerWheel        the size of the wheel
     *                             if false it will only be enabled if the worker thread is not
     *                             a daemon thread.
     * @param  maxPendingTimeouts  The maximum number of pending timeouts after which call to
     *                             {@code newTimeout} will result in
     *                             {@link java.util.concurrent.RejectedExecutionException}
     *                             being thrown. No maximum pending timeouts limit is assumed if
     *                             this value is 0 or negative.
     * @throws NullPointerException     if either of {@code threadFactory} and {@code unit} is {@code null}
     * @throws IllegalArgumentException if either of {@code tickDuration} and {@code ticksPerWheel} is &lt;= 0
     */
    public HashedWheelTimer(
            ThreadFactory threadFactory,
            long tickDuration, TimeUnit unit, int ticksPerWheel,
            long maxPendingTimeouts) {
        this(threadFactory, tickDuration, unit, ticksPerWheel,
                maxPendingTimeouts, Executors.newSingleThreadExecutor());
    }

    public HashedWheelTimer(
            ThreadFactory threadFactory,
            long tickDuration, TimeUnit unit, int ticksPerWheel,
            long maxPendingTimeouts, Executor taskExecutor) {

        Objects.requireNonNull(threadFactory, "threadFactory");
        Objects.requireNonNull(unit, "unit");
        checkPositive(tickDuration, "tickDuration");

        checkPositive(ticksPerWheel, "ticksPerWheel");
        this.taskExecutor = Objects.requireNonNull(taskExecutor, "taskExecutor");


        // 计算bucket的个数, 只能是2的倍数, 并且初始化时间轮
        // 如果是7, 那么时间轮个数为8
        wheel = createWheel(ticksPerWheel);
        mask = wheel.length - 1;

        // 将tick根据单位转换为纳秒
        long duration = unit.toNanos(tickDuration);

        // 时间间隔不能太长而导致tickDuration*wheel.length>Long.MAX_VALUE
        if (duration >= Long.MAX_VALUE / wheel.length) {
            throw new IllegalArgumentException(String.format(
                    "tickDuration: %d (expected: 0 < tickDuration in nanos < %d",
                    tickDuration, Long.MAX_VALUE / wheel.length));
        }

        // buckets表示的时间间隔小于1ms, 就使用1ms
        if (duration < MILLISECOND_NANOS) {
            logger.warn("Configured tickDuration {} smaller then {}, using 1ms.",
                    tickDuration, MILLISECOND_NANOS);
            this.tickDuration = MILLISECOND_NANOS;
        } else {
            this.tickDuration = duration;
        }

        // worker是推动时间前进的任务, workerThread是执行该任务的线程
        workerThread = threadFactory.newThread(worker);

        // 最大的任务个数
        this.maxPendingTimeouts = maxPendingTimeouts;

        // 一个jvm最多只能有64个时间轮对象实例，
        // 因为时间轮是一个非常耗费资源的结构所以实例数目不能太高
        if (INSTANCE_COUNTER.incrementAndGet() > INSTANCE_COUNT_LIMIT &&
                WARNED_TOO_MANY_INSTANCES.compareAndSet(false, true)) {
            if (logger.isErrorEnabled()) {
                String resourceType = HashedWheelTimer.class.getSimpleName();
                logger.error("You are creating too many " + resourceType + " instances. " +
                        resourceType + " is a shared resource that must be reused across the JVM, " +
                        "so that only a few instances are created.");
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            // This object is going to be GCed and it is assumed the ship has sailed to do a proper shutdown. If
            // we have not yet shutdown then we want to make sure we decrement the active instance count.
            if (WORKER_STATE_UPDATER.getAndSet(this, WORKER_STATE_SHUTDOWN) != WORKER_STATE_SHUTDOWN) {
                INSTANCE_COUNTER.decrementAndGet();
            }
        }
    }

    private static HashedWheelBucket[] createWheel(int ticksPerWheel) {
        //ticksPerWheel may not be greater than 2^30
        checkInRange(ticksPerWheel, 1, 1073741824, "ticksPerWheel");

        ticksPerWheel = normalizeTicksPerWheel(ticksPerWheel);
        HashedWheelBucket[] wheel = new HashedWheelBucket[ticksPerWheel];
        for (int i = 0; i < wheel.length; i ++) {
            wheel[i] = new HashedWheelBucket();
        }
        return wheel;
    }

    // 时间轮中bucket只能是2的次方倍
    // 如果传入的是7, 那么bucket的个数为8
    private static int normalizeTicksPerWheel(int ticksPerWheel) {
        int normalizedTicksPerWheel = 1;
        while (normalizedTicksPerWheel < ticksPerWheel) {
            normalizedTicksPerWheel <<= 1;
        }
        return normalizedTicksPerWheel;
    }

    // 启动时间轮
    // 允许并发!!!!!!!!!!
    public void start() {
        switch (WORKER_STATE_UPDATER.get(this)) {
            case WORKER_STATE_INIT:
                // 更新时间轮状态
                if (WORKER_STATE_UPDATER.compareAndSet(this, WORKER_STATE_INIT, WORKER_STATE_STARTED)) {
                    // 启动工作线程, 来推动时间前进
                    workerThread.start();
                }
                break;
            case WORKER_STATE_STARTED:
                break;
            case WORKER_STATE_SHUTDOWN:
                throw new IllegalStateException("cannot be started once stopped");
            default:
                throw new Error("Invalid WorkerState");
        }

        // Wait until the startTime is initialized by the worker.
        while (startTime == 0) {
            try {
                startTimeInitialized.await();
            } catch (InterruptedException ignore) {
                // Ignore - it will be ready very soon.
            }
        }
    }

    @Override
    public Set<Timeout> stop() {
        // worker线程不能停止时间轮，也就是加入的定时任务，不能调用这个方法。
        // 不然会有恶意的定时任务调用这个方法而造成大量定时任务失效
        if (Thread.currentThread() == workerThread) {
            throw new IllegalStateException(
                    HashedWheelTimer.class.getSimpleName() +
                            ".stop() cannot be called from " +
                            TimerTask.class.getSimpleName());
        }

        // 如果将状态从started设置为shutdown失败, 说明状态可能是init或者shutdown
        if (!WORKER_STATE_UPDATER.compareAndSet(this, WORKER_STATE_STARTED, WORKER_STATE_SHUTDOWN)) {
            // 状态可能为init或者shutdown, 直接设置为shutdown
            // 并且如果是从init转为shutdown, 会进入if里面
            if (WORKER_STATE_UPDATER.getAndSet(this, WORKER_STATE_SHUTDOWN) != WORKER_STATE_SHUTDOWN) {
                // HashedWheelTimer实例数减一
                INSTANCE_COUNTER.decrementAndGet();
            }
            // 因为状态是从init或者shutdown转为shutdown, 所以返回空集
            return Collections.emptySet();
        }

        try {
            boolean interrupted = false;
            while (workerThread.isAlive()) {
                // 中断worker线程
                workerThread.interrupt();
                try {
                    // 等待workerThread线程处理stop后的逻辑
                    workerThread.join(100);
                } catch (InterruptedException ignored) {
                    interrupted = true;
                }
            }

            if (interrupted) {
                // join方法抛出InterruptedException会清除标志位, 所以要重新设置
                Thread.currentThread().interrupt();
            }
        } finally {
            // HashedWheelTimer实例数减一
            INSTANCE_COUNTER.decrementAndGet();
        }
        // 返回未处理的任务
        return worker.unprocessedTimeouts();
    }

    @Override
    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
        Objects.requireNonNull(task, "task");
        Objects.requireNonNull(unit, "unit");

        long pendingTimeoutsCount = pendingTimeouts.incrementAndGet();

        if (maxPendingTimeouts > 0 && pendingTimeoutsCount > maxPendingTimeouts) {
            pendingTimeouts.decrementAndGet();
            throw new RejectedExecutionException("Number of pending timeouts ("
                    + pendingTimeoutsCount + ") is greater than or equal to maximum allowed pending "
                    + "timeouts (" + maxPendingTimeouts + ")");
        }

        // 自动调用start, 因为如果时间轮中没有任务, start也是浪费资源
        start();

        // Add the timeout to the timeout queue which will be processed on the next tick.
        // During processing all the queued HashedWheelTimeouts will be added to the correct HashedWheelBucket.
        long deadline = System.nanoTime() + unit.toNanos(delay) - startTime;

        // Guard against overflow.
        if (delay > 0 && deadline < 0) {
            deadline = Long.MAX_VALUE;
        }
        HashedWheelTimeout timeout = new HashedWheelTimeout(this, task, deadline);
        // 这里不直接把任务添加到时间轮中, 而是先把任务添加到队列中, 然后在下一个tick的时候
        // 从队列中把任务取出来挂载到bucket中
        // 这样可以减少加锁
        timeouts.add(timeout);
        return timeout;
    }

    /**
     * Returns the number of pending timeouts of this {@link Timer}.
     */
    public long pendingTimeouts() {
        return pendingTimeouts.get();
    }

    private final class Worker implements Runnable {
        private final Set<Timeout> unprocessedTimeouts = new HashSet<Timeout>();

        private long tick;

        @Override
        public void run() {
            // Initialize the startTime.
            startTime = System.nanoTime();
            if (startTime == 0) {
                // We use 0 as an indicator for the uninitialized value here, so make sure it's not 0 when initialized.
                startTime = 1;
            }

            // 唤醒调用start的线程
            startTimeInitialized.countDown();

            do {
                // waitForNextTick方法主要是计算下次tick的时间, 然后sleep到下次tick
                // 返回值就是System.nanoTime() - startTime, 也就是Timer启动后到这次tick, 所过去的时间
                final long deadline = waitForNextTick();
                // 如果deadline>=0, 正常执行
                // 如果deadline<0, 说明时间轮应该退出了
                if (deadline > 0) {
                    // 计算当前tick所使用的bucket的索引
                    // 原理: tick&(size−1) = tick%size, 通常使用在循环队列中
                    int idx = (int) (tick & mask);
                    // 将所有被取消的任务冲bucket中移除
                    processCancelledTasks();
                    HashedWheelBucket bucket =
                            wheel[idx];
                    // 将所有timeouts中的任务挂载到bucket上
                    transferTimeoutsToBuckets();
                    // 遍历bucket中的链表, 执行到期任务
                    bucket.expireTimeouts(deadline);
                    tick++;
                }
            } while (WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == WORKER_STATE_STARTED);

            // 时间轮已经stop

            for (HashedWheelBucket bucket: wheel) {
                // 遍历链表, 移除所有任务, 并将未执行的任务放到unprocessedTimeouts
                bucket.clearTimeouts(unprocessedTimeouts);
            }
            for (;;) {
                // 从timeouts中取出所有还没来得及挂载的任务
                HashedWheelTimeout timeout = timeouts.poll();
                if (timeout == null) {
                    break;
                }

                if (!timeout.isCancelled()) {
                    unprocessedTimeouts.add(timeout);
                }
            }
            // 从cancelledTimeouts中取出所有被取消的任务, 将所有被取消的任务冲bucket中取出来
            processCancelledTasks();
        }

        private void transferTimeoutsToBuckets() {
            // transfer only max. 100000 timeouts per tick to prevent a thread to stale the workerThread when it just
            // adds new timeouts in a loop.
            for (int i = 0; i < 100000; i++) {
                HashedWheelTimeout timeout = timeouts.poll();
                if (timeout == null) {
                    // all processed
                    break;
                }
                if (timeout.state() == HashedWheelTimeout.ST_CANCELLED) {
                    // Was cancelled in the meantime.
                    continue;
                }

                // 计算任务的tick
                long calculated = timeout.deadline / tickDuration;
                // 计算任务的轮数
                timeout.remainingRounds = (calculated - tick) / wheel.length;

                // 取任务的tick和当前tick, 防止任务还没有加入到时间轮中就过期了
                // 这种情况出现在tickDuration比较大的时候
                // 比如tickDuration为10秒, delay为5秒, 那么要下个tick才会把任务挂载到时间轮上
                // 这个时候任务早就过期了, 所以就把他加入到当前的bucket中, 立即执行
                final long ticks = Math.max(calculated, tick); // Ensure we don't schedule for past.
                // 计算任务的bucket
                int stopIndex = (int) (ticks & mask);

                HashedWheelBucket bucket = wheel[stopIndex];
                // 添加到bucket中
                bucket.addTimeout(timeout);
            }
        }

        private void processCancelledTasks() {
            for (;;) {
                HashedWheelTimeout timeout = cancelledTimeouts.poll();
                if (timeout == null) {
                    // all processed
                    break;
                }
                try {
                    timeout.remove();
                } catch (Throwable t) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("An exception was thrown while process a cancellation task", t);
                    }
                }
            }
        }

        /**
         * calculate goal nanoTime from startTime and current tick number,
         * then wait until that goal has been reached.
         * @return Long.MIN_VALUE if received a shutdown request,
         * current time otherwise (with Long.MIN_VALUE changed by +1)
         */
        // sleep, 直到下次tick到来, 然后返回该次tick和启动时间之间的时长
        private long waitForNextTick() {
            // 下次tick的时间点, 用于计算需要sleep的时间
            long deadline = tickDuration * (tick + 1);

            // sleep并不保证能sleep足够的时间, 所以需要再while循环中不断sleep, 以达到下个tick的时间点
            for (;;) {
                // 当前到开始过去的纳秒数
                final long currentTime = System.nanoTime() - startTime;
                // 到下一个tick需要sleep的毫秒数
                // 至少需要sleep 1ms
                long sleepTimeMs = (deadline - currentTime + 999999) / 1000000;

                // 如果是负数, 已经到了下一个tick
                // 如果workThread处理任务太久了, 以至于过了好几个tick时间才再次到这里, 那么他不会sleep, 而是直接进入下一个tick
                // 这样workThread就会快速处理任务, 从而追上实际上的tick
                if (sleepTimeMs <= 0) {
                    // 这里的意思应该是从时间轮启动到现在经过太长的时间(跨度大于292年...)，以至于让long装不下，都溢出了...对于netty的严谨，我服！
                    if (currentTime == Long.MIN_VALUE) {
                        return -Long.MAX_VALUE;
                    } else {
                        return currentTime;
                    }
                }

                // Check if we run on windows, as if thats the case we will need
                // to round the sleepTime as workaround for a bug that only affect
                // the JVM if it runs on windows.
                //
                // See https://github.com/netty/netty/issues/356
                // 这里是因为windows平台的定时调度最小单位为10ms，如果不是10ms的倍数，可能会引起sleep时间不准确
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    // 只sleep 10的倍数毫秒
                    sleepTimeMs = sleepTimeMs / 10 * 10;
                    if (sleepTimeMs == 0) {
                        sleepTimeMs = 1;
                    }
                }

                try {
                    // 因为sleep无法准确的保证就是sleep这么久, 有可能提前, 也有可能推后才醒来
                    // 如果这里sleep太久了, 以至于真实的时间已经过了好几个tick了
                    // 这样workThread就会快速处理任务, 从而追上实际上的tick
                    Thread.sleep(sleepTimeMs);
                } catch (InterruptedException ignored) {
                    if (WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == WORKER_STATE_SHUTDOWN) {
                        // 退出
                        return Long.MIN_VALUE;
                    }
                }
            }
        }

        public Set<Timeout> unprocessedTimeouts() {
            return Collections.unmodifiableSet(unprocessedTimeouts);
        }
    }

    // 时间轮中对Task的包装
    private static final class HashedWheelTimeout implements Timeout, Runnable {


        private static final AtomicIntegerFieldUpdater<HashedWheelTimeout> STATE_UPDATER =
                AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimeout.class, "state");

        private final HashedWheelTimer timer;

        // 具体的任务
        private final TimerTask task;


        // 在时间轮中, 任务执行的时间
        // System.nanoTime() + unit.toNanos(delay) - startTime;
        private final long deadline;

        // 当前任务的状态
        private volatile int state = ST_INIT;
        // 任务的状态, init表示待执行, cancelled表示已经取消, expired表示已经执行完毕
        private static final int ST_INIT = 0;
        private static final int ST_CANCELLED = 1;
        private static final int ST_EXPIRED = 2;

        // 在Timeout放入到bucket中的时候, 会设置remainingRounds
        // 时间轮没转动一轮就会将其减一, 当为0的时候, 说明该执行这个任务了
        long remainingRounds;

        // 组成双向链表,
        // 只有workerThread会操作这两个变量, 所以无需添加 volatile, 对他们的操作也无需添加synchronized
        HashedWheelTimeout next;
        HashedWheelTimeout prev;

        // 当前任务被添加到的bucket
        HashedWheelBucket bucket;

        HashedWheelTimeout(HashedWheelTimer timer, TimerTask task, long deadline) {
            this.timer = timer;
            this.task = task;
            this.deadline = deadline;
        }

        @Override
        public Timer timer() {
            return timer;
        }

        @Override
        public TimerTask task() {
            return task;
        }

        @Override
        public boolean cancel() {
            // 只修改状态
            // 所有被取消的任务, 会被放进队列中, 然后在下一个tick的时候才会poll出来, 然后从bucket中移除
            // 这样就避免了并发, 因为如果一边remove任务, 一边又添加任务, 必须要添加锁
            // 而统一到下一个tick处理就不需要使用锁了
            if (!compareAndSetState(ST_INIT, ST_CANCELLED)) {
                return false;
            }
            timer.cancelledTimeouts.add(this);
            return true;
        }

        // 只有workerThread会操作这个方法, 所以不必上锁
        void remove() {
            HashedWheelBucket bucket = this.bucket;
            if (bucket != null) {
                // 将Timeout从bucket中移除
                bucket.remove(this);
            } else {
                timer.pendingTimeouts.decrementAndGet();
            }
        }

        public boolean compareAndSetState(int expected, int state) {
            return STATE_UPDATER.compareAndSet(this, expected, state);
        }

        public int state() {
            return state;
        }

        @Override
        public boolean isCancelled() {
            return state() == ST_CANCELLED;
        }

        @Override
        public boolean isExpired() {
            return state() == ST_EXPIRED;
        }

        // 将当前任务设置为过期, 并且执行当前任务
        public void expire() {
            if (!compareAndSetState(ST_INIT, ST_EXPIRED)) {
                return;
            }
            try {
                timer.taskExecutor.execute(this);
            } catch (Throwable t) {
                if (logger.isWarnEnabled()) {
                    logger.warn("An exception was thrown while submit " + TimerTask.class.getSimpleName()
                            + " for execution.", t);
                }
            }
        }

        @Override
        public void run() {
            try {
                task.run(this);
            } catch (Throwable t) {
                if (logger.isWarnEnabled()) {
                    logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', t);
                }
            }
        }

        @Override
        public String toString() {
            final long currentTime = System.nanoTime();
            long remaining = deadline - currentTime + timer.startTime;

            StringBuilder buf = new StringBuilder(192)
                    .append(this.getClass().getSimpleName())
                    .append('(')
                    .append("deadline: ");
            if (remaining > 0) {
                buf.append(remaining)
                        .append(" ns later");
            } else if (remaining < 0) {
                buf.append(-remaining)
                        .append(" ns ago");
            } else {
                buf.append("now");
            }

            if (isCancelled()) {
                buf.append(", cancelled");
            }

            return buf.append(", task: ")
                    .append(task())
                    .append(')')
                    .toString();
        }
    }

    /**
     * Bucket that stores HashedWheelTimeouts. These are stored in a linked-list like datastructure to allow easy
     * removal of HashedWheelTimeouts in the middle. Also the HashedWheelTimeout act as nodes themself and so no
     * extra object creation is needed.
     */
    private static final class HashedWheelBucket {
        // 双向链表的头尾
        private HashedWheelTimeout head;
        private HashedWheelTimeout tail;

        // 添加任务到链表尾部
        public void addTimeout(HashedWheelTimeout timeout) {
            assert timeout.bucket == null;
            timeout.bucket = this;
            if (head == null) {
                head = tail = timeout;
            } else {
                tail.next = timeout;
                timeout.prev = tail;
                tail = timeout;
            }
        }

        // 执行当前bucket中所有已经到deadline的任务
        public void expireTimeouts(long deadline) {
            HashedWheelTimeout timeout = head;

            // 遍历链表
            while (timeout != null) {
                HashedWheelTimeout next = timeout.next;
                if (timeout.remainingRounds <= 0) {
                    // 从链表中移除任务, 并返回任务的下一个任务
                    next = remove(timeout);
                    if (timeout.deadline <= deadline) {
                        // 在线程池中执行
                        timeout.expire();
                    } else {
                        // The timeout was placed into a wrong slot. This should never happen.
                        throw new IllegalStateException(String.format(
                                "timeout.deadline (%d) > deadline (%d)", timeout.deadline, deadline));
                    }
                } else if (timeout.isCancelled()) {
                    next = remove(timeout);
                } else {
                    // 将任务的轮数减一
                    timeout.remainingRounds --;
                }
                timeout = next;
            }
        }

        // 从链表中移除timeout, 简单的链表操作
        public HashedWheelTimeout remove(HashedWheelTimeout timeout) {
            HashedWheelTimeout next = timeout.next;
            // remove timeout that was either processed or cancelled by updating the linked-list
            if (timeout.prev != null) {
                timeout.prev.next = next;
            }
            if (timeout.next != null) {
                timeout.next.prev = timeout.prev;
            }

            if (timeout == head) {
                // if timeout is also the tail we need to adjust the entry too
                if (timeout == tail) {
                    tail = null;
                    head = null;
                } else {
                    head = next;
                }
            } else if (timeout == tail) {
                // if the timeout is the tail modify the tail to be the prev node.
                tail = timeout.prev;
            }
            // null out prev, next and bucket to allow for GC.
            timeout.prev = null;
            timeout.next = null;
            timeout.bucket = null;
            timeout.timer.pendingTimeouts.decrementAndGet();
            return next;
        }

        /**
         * Clear this bucket and return all not expired / cancelled {@link Timeout}s.
         */
        public void clearTimeouts(Set<Timeout> set) {
            for (;;) {
                // 移除bucket中的首个任务并返回
                HashedWheelTimeout timeout = pollTimeout();
                if (timeout == null) {
                    return;
                }
                if (timeout.isExpired() || timeout.isCancelled()) {
                    continue;
                }
                // 将未执行的任务添加到set中
                set.add(timeout);
            }
        }

        // 移除bucket中的首个任务并返回
        private HashedWheelTimeout pollTimeout() {
            HashedWheelTimeout head = this.head;
            if (head == null) {
                return null;
            }
            HashedWheelTimeout next = head.next;
            if (next == null) {
                tail = this.head =  null;
            } else {
                this.head = next;
                next.prev = null;
            }

            // null out prev and next to allow for GC.
            head.next = null;
            head.prev = null;
            head.bucket = null;
            return head;
        }
    }

    public static long checkPositive(long l, String name) {
        if (l <= 0) {
            throw new IllegalArgumentException(name + " : " + l + " (expected: > 0)");
        }
        return l;
    }

    public static int checkInRange(int i, int start, int end, String name) {
        if (i < start || i > end) {
            throw new IllegalArgumentException(name + ": " + i + " (expected: " + start + "-" + end + ")");
        }
        return i;
    }
}

