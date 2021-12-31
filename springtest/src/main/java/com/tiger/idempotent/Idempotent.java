package com.tiger.idempotent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Target({ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Idempotent {

    /**
     * 用于生成唯一key
     * @return
     */
    String[] value() default {};

    /**
     * 失效时间(单位毫秒)
     *
     * @return
     */
    int expired() default 60;
    
    /**
     * 
     * expired时间内可调用次数
     *
     * @return
     */
    int times() default 1;

}
