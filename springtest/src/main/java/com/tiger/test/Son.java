package com.tiger.test;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Son
 * @date 2020/12/18 15:43
 * @description
 */
public class Son extends Subject {

    public static void main(String[] args) {
        Class<Son> clazz = Son.class;
        boolean present = clazz.isAnnotationPresent(HasInherited.class);
        System.out.println(present);

        System.out.println(Subject.class.isAnnotationPresent(HasInherited.class));
    }
}
