// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    - 构造函数是使用 constructor 关键字声明的一个可选函数, 它在创建合约时执行, 可以在其中运行合约初始化代码。
    - 构造函数运行后, 将合约的最终代码部署到区块链。代码的部署需要 gas 与代码的长度线性相关。 
        此代码包括所有函数部分是公有接口以及可以通过函数调用访问的所有函数。
        它不包括构造函数代码或仅从构造函数调用的内部函数。
    - 如果没有构造函数, 合约将假定采用默认构造函数, 它等效于 constructor() {} 。
    - 构造函数的可见性默认为public
    - 抽象合约的构造函数可见性默认为internal
 */
contract A {
    uint public a;
    // 默认为public 
    constructor(uint a_) { a = a_; }
}

abstract contract B {
    uint public a;
    // 默认为internal 
    constructor(uint a_)  { a = a_; }
}