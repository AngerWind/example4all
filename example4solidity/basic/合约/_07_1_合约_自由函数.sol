// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    - 可以在合约内部或者外部(0.7.0之后)定义函数
    - 合约之外的函数（也称为“自由函数”), 不能设置可见性关键字, 始终具有隐式的 internal 可见性。 
    - 编译器会把自由函数的代码添加到所有调用他们的合约中
*/
function sum(uint256[] memory arr) pure returns (uint256 s) {
    for (uint256 i = 0; i < arr.length; i++) s += arr[i];
}

contract C {
    function f(uint256[] memory arr) public pure {
        // This calls the free function internally.
        // The compiler will add its code to the contract.
        uint256 s = sum(arr);
    }
}
