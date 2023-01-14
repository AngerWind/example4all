// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    合约可以通过address()函数显式的转换为address类型
    只有当合约具有receive函数 或 payable fallback函数时，才能显式和 address payable 类型相互转换
 */