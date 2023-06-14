
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\basic\other\节省gas的方法.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0

pragma solidity ^0.8.17;

/**
 * 优化gas消耗的方法
 * 1. 对于不可变量添加constant和immutable关键字
 * 2. 添加constant和immutable的变量不会再占用storage slot, 而是直接被编译进字节码
 * 3. 使用error代替require函数, 因为require需要一个字符串消息, 而使用error比字符串更省gas
 * 5. 从合约外调用view/pure函数不需要花费gas, 但是从合约内的非view/pure函数调用view/pure函数需要花费gas
 * 
 * 6. 将public变量改为private或者internal变量, 并且添加getter函数, 这样可以减少gas消耗
 * 7. 如果要遍历数组, 可以先将数组一次性拷贝到内存中, 然后再遍历内存中的数组, 这样可以减少gas消耗, 否则每次读取length和数组元素都需要消耗大量gas
 *      uint[] memory arr = storageArray;
 *      for (uint i = 0; i < arr.length; i++) {}
 */

contract Test {
    // 使用不可变量
    uint256 public constant VERSION1 = 1;
    uint256 public constant VERSION2 = 1;

    // 使用不可变量
    address public immutable owner1;
    address public immutable owner2;

    error LessError();

    constructor() {
        owner1 = msg.sender;
        owner2 = msg.sender;
    }

    modifier lessThen(uint256 a, uint256 b) {
        // require(a < b, "msg");
        if (a < b) {
            revert LessError();
        }
        _;
    }
}

// 从合约外调用view/pure函数不需要花费gas, 但是从合约内的非view/pure函数调用view/pure函数需要花费gas
contract Test1 {
    uint a = 1;

    function get() public view returns (uint) {
        return a;
    }

    function get1() public view returns (uint) {
        return get();
    }

    function set1(uint _a) public {
        a = _a;
    }

    function set2(uint _a) public {
        a = _a;
        get();
    }

    function set3(uint _a) public {
        a = _a;
        get();
        get();
    }
}

