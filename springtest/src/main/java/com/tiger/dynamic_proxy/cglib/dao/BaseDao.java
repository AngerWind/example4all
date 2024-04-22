package com.tiger.dynamic_proxy.cglib.dao;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Dao
 * @date 2021/9/15 14:25
 * @description
 */
public class Dao implements StudentMapper{

    public String update() {
        System.out.println("Dao.update()");
        return "Dao.update()";
    }

    public Dao() {}

    public Dao(int arg1, int arg2) {}

    private Dao(int arg1, String arg2) {}

    public final void finalMethod() {}

    public static void staticMethod() {}

    protected void defaultMethod(){}

    private void privateMethod(){}

    @Override
    public void selectStudentById(Long id) { }
}
