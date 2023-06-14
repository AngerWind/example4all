
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\basic\合约\_07_6_合约_函数重载.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0

pragma solidity ^0.8.17;

interface Say {
    function say() external;

    function say(string memory message) external;
}

// 函数允许重载, 即名称相同但是输入类型不同的函数可以同时存在
contract SaySomething is Say {
    constructor() {}

    function say() external override {}

    function say(string memory message) external override {}

    // 在调用重载函数时，会把输入的实际参数和函数参数的变量类型做匹配。
    // 如果出现多个匹配的重载函数，则会报错
    // 调用f(50)，因为50既可以被转换为uint8，也可以被转换为uint256，因此会报错。
    function f(uint8 _in) public pure returns (uint8 out) {
        out = _in;
    }

    function f(uint256 _in) public pure returns (uint256 out) {
        out = _in;
    }
}

