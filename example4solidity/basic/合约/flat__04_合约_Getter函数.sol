
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\basic\合约\_04_合约_Getter函数.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0

pragma solidity ^0.8.17;

contract HelloWorld {
    /**
        - 编译器自动为所有 public 状态变量创建 getter 函数
        - getter 函数具有外部（external）可见性。
       -  如果在内部访问 getter（即没有 this. ），它被认为一个状态变量。 如果使用外部访问（即用 this. ），它被认作为一个函数。
    */

    uint256 public data;

    function x() public {
        data = 3; // 内部访问
        uint256 val = this.data(); // 外部访问
    }

    /**
        对于数组类型, 生成的getter函数需要一个元素下标, 返回数组的某个元素.
        这个机制以避免返回整个数组时的高成本gas

        如果需要返回整个数组可以自己写一个
     */
    uint256[] public myArray;

    // 指定生成的Getter 函数
    /*
        function myArray(uint i) public view returns (uint) {
            return myArray[i];
        }
    */
    // 返回整个数组
    function getArray() public view returns (uint256[] memory) {
        return myArray;
    }
}

/**
        针对复杂的结构, 生成的getter函数可能也会千奇百怪
     */
contract Complex {
    struct Data {
        uint256 a;
        bytes3 b;
        mapping(uint256 => uint256) map;
        uint256[3] c;
        uint256[] d;
        bytes e;
    }
    mapping(uint256 => mapping(bool => Data[])) public data;

    /** 
    生成的getter行类似这样, 结构体内的映射和数组（byte 数组除外）被省略了
    function data( uint256 arg1, bool arg2, uint256 arg3 ) public
        returns ( uint256 a, bytes3 b, bytes memory e ) {
        a = data[arg1][arg2][arg3].a;
        b = data[arg1][arg2][arg3].b;
        e = data[arg1][arg2][arg3].e;
    }
    */
}

