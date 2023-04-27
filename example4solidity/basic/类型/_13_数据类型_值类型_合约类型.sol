// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    合约可以通过address()函数显式的转换为address类型
    只有当合约具有receive函数 或 payable fallback函数时，才能显式和 address payable 类型相互转换
 */
contract A {
    B b; // 合约的默认值为0x0000000000000000000
    B b1;

    constructor() {
        // 在区块链上创建一个新的合约
        b = new B();
        // 从一个已有的合约地址上创建一个合约, 这样我们就可以直接调用这个合约了
        b1 = B(0xdCad3a6d3569DF655070DEd06cb7A1b2Ccd1D3AF);
    }
}

contract B {}
