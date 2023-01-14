// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;


contract HelloWorld {
    // 长度在39-41之间的16进制会被认为是一个地址, 所以会对他进行地址校验和测试
    // 如果通过了校验就可以作为一个地址类型使用, 比如0xdCad3a6d3569DF655070DEd06cb7A1b2Ccd1D3AF;
    // 如果没有通过校验, 就会报错
    address public addr = 0xdCad3a6d3569DF655070DEd06cb7A1b2Ccd1D3AF;

    // 下面这个十六进制被认为是一个地址, 但是他没有通过校验, 所以报错了, 目前不知道应该怎么解决;
    // bytes20 public b = 0xdCad3a6d3569DF655070DEd06cb7A1b2Ccd1D3Ac;
}