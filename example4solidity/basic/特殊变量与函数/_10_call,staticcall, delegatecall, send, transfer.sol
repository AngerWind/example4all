// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    call和delegatecall, staticcall都用于调用地址的指定方法, 区别在于:
        1. 三者都提供了修改gas选项, 但是只有call可以通过value选项来发送eth
        2. call会修改环境变量(storage, 变量, this都是被调用者的), staticcall和delegatecall不会修改(storage, 变量, this都是调用者的)
        3. staticcall和delegatecall的区别在于staticcall不能修改状态变量

    send和transfer和call都可以用来发送eth, 区别在于
        1. send和transfer消耗固定 2300 gas, 如果gas不足会转账失败, 但是call可以通过gas选项来修改gas费
        2. transfer调用失败直接报错, send返回一个bool表示是否调用成功, call返回两个值, 第一个bool是否调用成功, 第二个是调用结果
 */
