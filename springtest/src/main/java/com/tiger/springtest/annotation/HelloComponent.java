package com.tiger.springtest.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title HelloComponent
 * @date 2020/12/8 18:55
 * @description
 */
@Target({ElementType.TYPE})
@Component
@Retention(RetentionPolicy.RUNTIME)
@RestController
public @interface HelloComponent {

    // @AliasFor(annotation = Controller.class, attribute = "value")
    String value() default "";
}
