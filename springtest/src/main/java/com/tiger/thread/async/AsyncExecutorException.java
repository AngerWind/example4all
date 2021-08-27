package com.tiger.thread.async;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title AsyncExecutorException
 * @date 2021/8/25 13:57
 * @description
 */
public class AsyncExecutorException extends RuntimeException {

    private static final long serialVersionUID = 7697293652227367297L;

    public AsyncExecutorException(Throwable cause) {
        super(cause);
    }
}
