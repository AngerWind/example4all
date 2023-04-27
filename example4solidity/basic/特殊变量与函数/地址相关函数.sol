// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    <address>.balance (uint256)
        以 Wei 为单位的 地址类型 Address 的余额。
    <address>.code (bytes memory)
        在 地址类型 Address 上的代码(可以为空)
    <address>.codehash (bytes32)
        地址类型 Address 的codehash
    <address payable>.transfer(uint256 amount)
        向 地址类型 Address 发送数量为 amount 的 Wei，失败时抛出异常，使用固定（不可调节）的 2300 gas 的矿工费。
    <address payable>.send(uint256 amount) returns (bool)
        向 地址类型 Address 发送数量为 amount 的 Wei，失败时返回 false，发送 2300 gas 的矿工费用，不可调节。
    <address>.call(bytes memory) returns (bool, bytes memory)
        用给定的有效载荷（payload）发出低级 CALL 调用，返回成功状态及返回数据，发送所有可用 gas，也可以调节 gas。
    <address>.delegatecall(bytes memory) returns (bool, bytes memory)
        用给定的有效载荷 发出低级 DELEGATECALL 调用 ，返回成功状态并返回数据，发送所有可用 gas，也可以调节 gas。 
    <address>.staticcall(bytes memory) returns (bool, bytes memory)
        用给定的有效载荷 发出低级 STATICCALL 调用 ，返回成功状态并返回数据，发送所有可用 gas，也可以调节 gas。
 */