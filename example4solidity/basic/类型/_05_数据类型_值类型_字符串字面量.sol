// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;


contract HelloWorld {
    
    // 字符串字面量可以使用单引号或者双引号表示
    // 并且可以分为多个连续的部分,即"zhang" "san"与"zhangsan"是一样的, 至少增加了可读性
    // 字符串常量只能包含可打印的ascII字符, 意味着字符串常量不支持中文
    // 字符串支持\n, \t, \r, \", \', \\等转义字符
    
    // 下面是两个字符串常量
    // "zhang" "\n" "san";
    // "zhang\nsan";
    // "离谱"  这个不是字符串常量, 因为字符串常量不支持中文

    // 字符串常量可以隐式地转换成 bytes1, ..., bytes32
    // 如果合适的话，还可以转换成 bytes 以及 string

    
    // !!!!!!!!!!!!!!
    // 特别要区分字符串常量与字符串类型
    // string name = "zhangsan"表示的是将一个字符串常量赋值给字符串类型的一个变量
    // 这里要特别区分
    // 字符串常量是值类型, 而字符串是引用类型
}