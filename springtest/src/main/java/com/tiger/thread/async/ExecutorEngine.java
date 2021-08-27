package com.tiger.thread.async;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Slf4j
public final class ExecutorEngine {

    private final ListeningExecutorService executorService;
    private final ThreadPoolExecutor threadPool;

    public ExecutorEngine(final int maxWorkerNumber) {
        this(maxWorkerNumber,"ExecutorEngine-%d");
    }

    public ExecutorEngine(final int maxWorkerNumber,String threadPoolName) {
        this.threadPool = new ThreadPoolExecutor(maxWorkerNumber, maxWorkerNumber, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactoryBuilder().setDaemon(true).setNameFormat(threadPoolName).build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
        this.executorService = MoreExecutors.listeningDecorator(this.threadPool);
    }

    public ListeningExecutorService getListeningExecutorService() {
        return this.executorService;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return this.threadPool;
    }

    /**
     * 多线程执行任务.
     *
     * @param inputs
     *            输入参数
     * @param executeUnit
     *            执行单元
     * @param <I>
     *            入参类型
     * @param <O>
     *            出参类型
     * @return 执行结果
     */
    public <I, O> List<O> execute(final Collection<I> inputs, final ExecuteUnit<I, O> executeUnit) {
        if (inputs.isEmpty()) {
            return Collections.emptyList();
        }
        ListenableFuture<List<O>> futures = submit(inputs, executeUnit);
        addCallback(futures);
        return getFutureResults(futures, executeUnit.getTimeOut(), executeUnit.getTimeUnit());
    }

    /**
     * 多线程执行任务.
     *
     * @param futures
     *            输入参数
     * @param timeOut
     * @param unit
     * @return
     */
    public <O> List<O> execute(final Collection<ListenableFuture<O>> futures, final long timeOut, final TimeUnit unit) {
        if (futures.isEmpty()) {
            return Collections.emptyList();
        }
        Set<ListenableFuture<O>> result = Sets.newHashSet();
        for (final ListenableFuture<O> each : futures) {
            if (each != null) {
                result.add(each);
            }
        }
        ListenableFuture<List<O>> list = Futures.allAsList(result);
        addCallback(list);
        return getFutureResults(list, timeOut, unit);
    }

    /**
     * 多线程执行任务.
     *
     * @param inputs
     *            输入参数
     * @param executeUnit
     *            执行单元
     * @param <I>
     *            入参类型
     * @param <O>
     *            出参类型
     * @return 执行结果
     */
    public <I, O> List<O> execute(final Iterable<I> inputs, final ExecuteUnit<I, O> executeUnit) {
        ListenableFuture<List<O>> futures = submit(inputs, executeUnit);
        addCallback(futures);
        return getFutureResults(futures, executeUnit.getTimeOut(), executeUnit.getTimeUnit());
    }

    /**
     * 多线程执行任务.
     *
     * @param inputs
     *            输入参数
     * @param executeUnit
     *            执行单元
     * @param <I>
     *            入参类型
     * @param <O>
     *            出参类型
     * @return 执行结果
     */
    public <I, O> List<O> execute(final Iterable<I> inputs, final ExecuteUnit<I, O> executeUnit,
                                  final ExecuteCallback executeCallback) {
        ListenableFuture<List<O>> futures = submit(inputs, executeUnit);
        addCallback(futures, executeCallback);
        return getFutureResults(futures, executeUnit.getTimeOut(), executeUnit.getTimeUnit());
    }

    /**
     * 多线程执行任务并归并结果.
     *
     * @param inputs
     *            执行入参
     * @param executeUnit
     *            执行单元
     * @param mergeUnit
     *            合并结果单元
     * @param <I>
     *            入参类型
     * @param <M>
     *            中间结果类型
     * @param <O>
     *            最终结果类型
     * @return 执行结果
     */
    public <I, M, O> O execute(final Collection<I> inputs, final ExecuteUnit<I, M> executeUnit,
                               final MergeUnit<M, O> mergeUnit) {
        return mergeUnit.merge(execute(inputs, executeUnit));
    }

    /**
     * 多线程执行任务并归并结果.
     *
     * @param inputs
     *            执行入参
     * @param executeUnit
     *            执行单元
     * @param mergeUnit
     *            合并结果单元
     * @param <I>
     *            入参类型
     * @param <M>
     *            中间结果类型
     * @param <O>
     *            最终结果类型
     * @return 执行结果
     */
    public <I, M, O> O execute(final Iterable<I> inputs, final ExecuteUnit<I, M> executeUnit,
                               final MergeUnit<M, O> mergeUnit) {
        return mergeUnit.merge(execute(inputs, executeUnit));
    }

    /**
     * @param inputs
     *            输入的参数
     * @param executeUnit
     *            执行参数与结果
     * @return
     */
    public <I, O> ListenableFuture<List<O>> submit(final Iterable<I> inputs, final ExecuteUnit<I, O> executeUnit) {
        Set<ListenableFuture<O>> result = Sets.newHashSet();
        for (final I each : inputs) {
            if (each != null) {
                result.add(executorService.submit(() -> executeUnit.execute(each)));
            }
        }
        return Futures.allAsList(result);
    }

    /**
     * @param inputs
     *            输入的参数
     * @param executeUnit
     *            执行参数与结果
     * @return
     */
    public <I, O> ListenableFuture<List<O>> submit(final Collection<I> inputs, final ExecuteUnit<I, O> executeUnit) {
        Set<ListenableFuture<O>> result = Sets.newHashSet();
        for (final I each : inputs) {
            result.add(executorService.submit(() -> executeUnit.execute(each)));
        }
        return Futures.allAsList(result);
    }

    public ListenableFuture<?> submit(Runnable task) {
        return executorService.submit(task);
    }

    public <T> ListenableFuture<T> submit(Callable<T> task) {
        return executorService.submit(task);
    }

    private <T> void addCallback(final ListenableFuture<T> allFutures) {
        Futures.addCallback(allFutures, new FutureCallback<T>() {
            @Override
            public void onSuccess(final T result) {
                if (log.isDebugEnabled()) {
                    log.debug("Concurrent onSuccess result {}.", JSONUtil.toJsonStr(result));
                }
            }

            @Override
            public void onFailure(final Throwable t) {
                throw new AsyncExecutorException(t);
            }
        });
    }

    public <T> void addCallback(final ListenableFuture<T> allFutures, ExecuteCallback callback) {
        Futures.addCallback(allFutures, new FutureCallback<T>() {
            @Override
            public void onSuccess(T t) {
                callback.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(allFutures, throwable);
            }
        });
    }

    public <O> O getFutureResults(final ListenableFuture<O> futures, long timeOut, TimeUnit unit) {
        try {
            if (timeOut > 0) {
                return futures.get(timeOut, unit);
            } else {
                return futures.get();
            }
        } catch (final Exception ex) {
            throw new AsyncExecutorException(ex);
        }
    }
}