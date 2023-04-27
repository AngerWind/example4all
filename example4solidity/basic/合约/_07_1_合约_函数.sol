// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract C {
    /**
        - 在函数中没有使用到的参数可以省略参数名
        - 函数可以返回任意多个返回值, 可以给返回值一个名称, 然后就像本地变量一样使用他们

        - 对于非内部函数, 函数的返回值不能是以下类型以及他们的组合
            mappings(映射), 内部函数类型, 指向存储 storage 的引用类型,
            多维数组 (仅适用于 ABI coder v1),
            结构体 (仅适用于 ABI coder v1).
    */
    function arrayInfo1(uint256[] memory arr)
        public
        pure
        returns (uint256 len, uint256 s)
    {
        len = arr.length;
        s = sum(arr);
    }

    function arrayInfo2(uint256[] memory arr)
        public
        pure
        returns (uint256, uint256)
    {
        uint256 len = arr.length;
        uint256 s = sum(arr);
        return (len, s);
    }

    function sum(uint256[] memory arr) public pure returns (uint256 s) {
        for (uint256 i = 0; i < arr.length; i++) s += arr[i];
    }
}
