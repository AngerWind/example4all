package com.tiger.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 @Title SimplePlugin
 @author tiger.shen
 @date 2021/12/23 17:25
 @version v1.0
 @description
 */
class SimplePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.register("hello", {
            println("hello world!!!")
        })
    }
}
