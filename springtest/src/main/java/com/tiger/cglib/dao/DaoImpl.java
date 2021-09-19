package com.tiger.cglib.dao;

import com.tiger.cglib.EverythingTest;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title DaoImpl
 * @date 2021/9/15 14:25
 * @description
 */
public class DaoImpl extends Dao {
    @Override
    public String update() {
        System.out.println("DaoImpl.update");
        return "DaoImpl.update";
    }
}
