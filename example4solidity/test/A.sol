// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

// 我们部署它，会得到一个合约地址，假设它是“0xxxxx”
contract A {
    uint256 public a = 1;

    function setA(uint256 _a) public returns (uint256) {
        a = _a;
        return a;
    }
}