package com.tiger.dynamic_proxy.cglib.dao;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title DaoImpl
 * @date 2021/9/15 14:25
 * @description
 */
public class BaseDaoImpl extends BaseDao implements StudentMapper{
    @Override
    public String update() {
        System.out.println("DaoImpl.update");
        return "DaoImpl.update";
    }

    @Override
    public void selectStudentById(Long id) { }
}
