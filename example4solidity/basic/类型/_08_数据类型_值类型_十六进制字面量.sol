// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;


contract HelloWorld {
    // 十六进制以0x开头, 或者以关键字 hex 打头，后面紧跟着用单引号或双引号引起来的字符串
    // 比如: hex"001122FF",  0xabcded

    // 可以选择使用单个下划线作为字节边界分隔符
    //      如: hex"ab_ef_cdxy"与hex"abefcdxy"是一样的
    //          0xab_cd_ef与0xabcdef是一样的
    
    // 用空格分隔的多个十六进制字面常量被合并为一个字面常量： hex"00112233" hex"44556677" 等同于 hex"0011223344556677"

    // 十六进制字面常量跟字符串字面常量很类似, 可以隐式地转换成 bytes1，……， bytes32
    // 如果合适的话，还可以转换成 bytes 以及 string。
}