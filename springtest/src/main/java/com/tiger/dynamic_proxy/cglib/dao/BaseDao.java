package com.tiger.dynamic_proxy.cglib.dao;

import com.tiger.dynamic_proxy.cglib.Dao;

import java.rmi.AccessException;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Dao
 * @date 2021/9/15 14:25
 * @description
 */
public class BaseDao extends Dao {

    public String update() {
        System.out.println("Dao.update()");
        return "Dao.update()";
    }

    protected void select(){};

    public BaseDao() {}

    public BaseDao(int arg1, int arg2) {}

    private BaseDao(int arg1, String arg2) {}

    public final void finalMethod() {}

    public static void staticMethod() {}

    protected void protectedMethod(){}

    void defaultMethod(){}

    private void privateMethod(){}

    public void  exceptionMethod() throws AccessException {};


}
