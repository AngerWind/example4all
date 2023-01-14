// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract HelloWorld {
    // uint显式转换, 高转低舍弃高位, 低转高高位补0
    uint32 public a32 = 0xaabbccdd;
    uint16 public a16 = uint16(a32); // 0xccdd, 舍弃高位
    uint16 public b16 = 0xaabb;
    uint32 public b32 = b16; // 0x0000aabb, 直接隐式转换, 填充高位

    // 定长字节转换, 高转低舍弃低位, 低转高低位补0
    bytes4 public bs4 = 0xaabbccdd;
    bytes2 public bs2 = bytes2(bs4); // 0xaabb, 舍弃低位
    bytes2 public bz2 = 0xaabb;
    bytes4 public bz4 = bz2; // 0xaabb0000, 直接隐式转换, 填充低位


    // 因为整数和定长字节数组在截断（或填充）时行为是不同的， 如果整数和定长字节数组有相同的大小，则允许他们之间进行显式转换
    // 如果要在不同的大小的整数和定长字节数组之间进行转换 ，必须使用一个中间类型来明确进行所需截断和填充的规则
    // 即如果bytes2=>uint32, 有两种办法
    //      1. bytes2 => uint6 => uint32
    //      2. bytes2 => bytes4 => uint32
    // 按照中间类型的不同, 截断和填充的方式也会不同
    bytes2 a = 0x1234;
    uint32 b = uint16(a); // 0x00001234, bytes2 => uint16 => uint32, 填充/截断左边
    uint32 c = uint32(bytes4(a)); // 0x12340000, bytes2 => bytes4 => uint32, 填充/截断右边
    uint8 d = uint8(uint16(a)); // 0x34, 填充/截断左边
    uint8 e = uint8(bytes1(a)); // 0x12, 填充/截断右边
}
