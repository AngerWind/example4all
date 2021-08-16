package com.tiger.springtest.annotation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title HotMapping
 * @date 2020/12/9 19:34
 * @description
 */

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@CoolMapping
public @interface HotMapping {

    String[] path() default {};
}
