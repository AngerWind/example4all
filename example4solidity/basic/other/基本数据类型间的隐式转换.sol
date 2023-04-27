// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract HelloWorld {
    /**
        隐式转换发生在 赋值, 传参, 使用运算符
        通常情况下, 如果不会发生信息的丢失, 都可以进行隐式类型转换
            - uint之间可以从低精度转换为高精度
            - int之间可以从低精度转换为高精度
     */
     uint8 public a8 = 0xff; // 255
     uint16 public a16 = 0xff; // 255, 0x00ff, 高位补充0

     uint32 public a32 = a8 + a16; // 510, 将运算符应用于不同的类型，编译器将尝试将其中一个操作数隐式转换为另一个操作数的类型（赋值也是如此）
}
