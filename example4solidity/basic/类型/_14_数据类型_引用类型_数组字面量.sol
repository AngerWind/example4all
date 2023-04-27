// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract HelloWorld {
    // 数组字面量
    function test2() public pure returns (uint8[3] memory, bytes1[3] memory) {
        /**
            - 数组字面量是在方括号中（ [...] ） 包含一个或多个逗号分隔的表达式.
                比如: [1, 2, 3]
            - 他的长度固定, 不能修改, 但是内容可以修改
            - ***数组字面量的基本类型由数组的一个元素确定****
                [1, 2, 3]是一个uint[3]类型的数组字面量
                [1, -1]是无效的, 因为第一个元素是uint8, 所以整个数组的基本类型是uint8, 而第二个元素是int, 不能隐式转换
                如果希望表示一个int8类型的数组, 使用[int8(1), -1]
        */

        // 定义一个uint8[3]的数组字面量并赋值
        uint8[3] memory array = [1, 2, 3];

        // 定义一个bytes1[3]的数组字面量并赋值
        // bytes1[3] array2 = [0xff, 0xaa, 0xbb]; // 因为第一个元素0xff默认是uint8的, 所以整个字面量的类型为uint[3], 无法转换为bytes1[3]
        bytes1[3] memory array2 = [bytes1(0xff), 0xaa, 0xbb];

        return (array, array2);
    }
}
