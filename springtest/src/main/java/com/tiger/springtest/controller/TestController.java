package com.tiger.springtest.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.tiger.jackson.JsonViewTest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title TestController
 * @date 2021/7/7 11:28
 * @description
 */
@RestController("/hello")
public class TestController {

    @GetMapping("/say")
    @JsonView(JsonViewTest.Small.class)
    public JsonViewTest.Student2 say() {
        return new JsonViewTest.Student2(new JsonViewTest.Student("zhangsan", 999, 111, 555));
    }

    @GetMapping("/say2")
    @JsonView(JsonViewTest.Big.class)
    public JsonViewTest.Student say2() {
        return new JsonViewTest.Student("zhangsan", 999, 111, 555);
    }
}
