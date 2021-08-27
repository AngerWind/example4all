package com.tiger.thread.async;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title MergeUnit
 * @date 2021/8/25 13:55
 * @description
 */

import java.util.Collection;

/**
 * 合并执行单元.
 *
 * @param <I>
 *            入参类型
 * @param <O>
 *            出参类型
 */
public interface MergeUnit<I, O> {

    /**
     * 合并执行结果.
     *
     * @param results
     *            合并前数据
     * @return 合并后结果
     */
    O merge(final Collection<I> results);
}
