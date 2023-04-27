// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    assert(bool condition)
        如果不满足条件，则会导致Panic 错误，则撤销状态更改 - 用于检查内部错误。
    require(bool condition)
        如果条件不满足则撤销状态更改 - 用于检查由输入或者外部组件引起的错误。
    require(bool condition, string memory message)
        如果条件不满足则撤销状态更改 - 用于检查由输入或者外部组件引起的错误，可以同时提供一个错误消息。
    revert()
        终止运行并撤销状态更改。
    revert(string memory reason)
        终止运行并撤销状态更改，可以同时提供一个解释性的字符串。
 */