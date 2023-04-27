// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract HelloWorld {
    /**
        数组可以是定长的, 也可以是变长的, 并且可以是memory数组,也可以是storage数组
            - 定长storage数组: 长度不可变, 内容可变
            - 变长storage数组: 长度可以通过push, pop改变, 内容可变
            - 变长memory数组: 通过new创建, 在创建的时候需要指定长度, 他没有push, pop方法, 所以创建之后长度不可变, 内容可变
            - 定长memory数组: 长度不可变, 内容可变
        所有类型的数组都可以通过下边进行访问和修改, 通过.length获取数组长度
        变长的storage数组有push, pop方法, 可以添加和删除元素, 其他类型的没有
     */
    // 定长storage数组并赋值
    uint256[3] public a = [1, 2, 3];

    // 变长storage数组并赋值
    uint256[] public b = [4, 5, 6];
    // 变长storage数组, 默认[0, 0, 0]
    uint256[] public c = new uint256[](3);

    constructor() {
        c[0] = 1; // 修改数组
        c[1] = 2;
        c[2] = 3;

        uint256 len = c.length; // 通过length获取长度
    }

    // memory数组定义与赋值
    function test() public pure returns (uint256[3] memory) {
        // 通过new创建, 在创建的时候需要指定长度, 之后长度不可变, 内容可变, 初始为[0x00, 0x00, 0x00]
        bytes1[] memory array1 = new bytes1[](3);
        // 赋值
        array1[0] = 0xff;
        array1[1] = 0xff;
        array1[2] = 0xff;

        // 创建一个定长并赋值
        uint256[3] memory array2 = [uint256(256), 2, 3];

        return array2;
    }

    /**
    push():
        动态的存储storage数组以及bytes类型都有一个 push() 的成员函数，
        它用来添加新的零初始化元素到数组末尾，并返回元素引用． 
        因此可以这样：　 x.push().t = 2 或 x.push() = b.
    push(x):
        动态的存储storage数组以及bytes类型都有一个 push(ｘ) 的成员函数，
        用来在数组末尾添加一个给定的元素，这个函数没有返回值．
    pop():
        变长的存储storage数组以及bytes类型都有一个 pop() 的成员函数， 
        它用来从数组末尾删除元素。 同样的会在移除的元素上隐含调用 delete ，
        这个函数没有返回值。
    */
    function test2() public returns (uint) {
        uint p = c.push(); // 返回元素的引用
        c.push(3); // 没有返回值
        c.pop(); // 返回pop出来的值
        return c.length;
    }

    // 二维定长数组
    function test3() public pure returns (bytes1[2][3] memory) {
        // 定义了一个3,2类型的数组, 这里需要注意的是和其他语言是反的
        // 在java中定义一个3,2类型的是在是 int[3][2]
        bytes1[2][3] memory array;
        // 这里赋值和其他语言又是正的
        array[0][0] = 0xaa;
        array[0][1] = 0xbb;
        array[1][0] = 0xcc;
        array[1][1] = 0xdd;
        array[2][0] = 0xee;
        array[2][1] = 0xff;
        return array;
    }
}
