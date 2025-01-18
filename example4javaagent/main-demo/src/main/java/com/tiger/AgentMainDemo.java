package com.tiger;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/1/4
 * @description
 */
public class AgentMainDemo {

    public static void main(String[] args) {
        Student student = new Student();
        for (; ; ) {
            student.say();
            // new Student().say();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
