// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;


contract HelloWorld {
    
    // 数字之间可以使用_分割开, 增加可读性
    int public num = 123_456_789; // 123456789

    // 数值字面常量表达式本身支持任意精度，直到被转换成了非常量类型
    // 即: 2**1000 - 2**1000 + 1可以支持任意精度, 但是在将结果赋值给x之后, 就变成了有限精度了
    int public x = 2**1000 - 2**1000 + 1 ; // 1
}