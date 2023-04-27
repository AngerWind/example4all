// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;


contract HelloWorld {
    // 常规字符串文字只能包含ASCII，而Unicode文字（以关键字unicode为前缀）可以包含任何有效的UTF-8序列
    // 比如: "zhangsan"是一个非法的字符串常量, 而unicode"张三"是合法的
    // 所有如果需要在字符串中使用非ascII码的字符, 需要前面添加unicode
}