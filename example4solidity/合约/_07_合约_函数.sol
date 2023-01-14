// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

function sum(uint256[] memory arr) pure returns (uint256 s) {
    for (uint256 i = 0; i < arr.length; i++) s += arr[i];
}

contract C {
    /**
        - 可以在合约内部或者外部(0.7.0之后)定义函数
        - 合约之外的函数（也称为“自由函数”）始终具有隐式的 internal 可见性。 
          编译器会把自由函数的代码添加到所有调用他们的合约中
     */
    function f(uint256[] memory arr) public pure {
        // This calls the free function internally.
        // The compiler will add its code to the contract.
        uint256 s = sum(arr);
    }


    /**
        - 函数可以返回任意多个返回值
        - 可以给返回值一个名称, 然后就像本地变量一样使用他们
        - 函数的返回值不能是以下类型以及他们的组合
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

    /**
        函数的状态可变性:
            view(视图函数): 将函数声明为view类型, 需要保证不修改状态
                    以下语句被认为是修改状态: 修改状态变量。
                        1.产生事件。
                        2.创建其它合约。
                        3.使用 selfdestruct。
                        4.通过调用发送以太币。
                        5.调用任何没有标记为 view 或者 pure 的函数。
                        6.使用低级调用。
                        7.使用包含特定操作码的内联汇编。
            pure(纯函数): 将函数声明为pure类型, 需要保证不读取和修改状态
                        1.读取状态变量。(包括immutable)
                        2.访问 address(this).balance 或者 <address>.balance。
                        3.访问 block，tx， msg 中任意成员 （除 msg.sig 和 msg.data 之外）。
                        4.调用任何未标记为 pure 的函数。
                        5.使用包含某些操作码的内联汇编。
     */
    function viewTest(uint256 a, uint256 b) public view returns (uint256) {
        return a * (b + 42) + block.timestamp;
    }

    function pureTest(uint256 a, uint256 b) public pure returns (uint256) {
        return a * (b + 42);
    }

    /**
        函数重载
     */
    function f(uint256 value) public pure returns (uint256 out) {
        out = value;
    }

    function f(uint256 value, bool really) public pure returns (uint256 out) {
        if (really) out = value;
    }

    /**
        receive函数: 
            - 一个合约最多有一个 receive 函数, 声明函数为： receive() external payable { ... }
                不需要 function 关键字，也没有参数和返回值并且必须是　external　可见性和　payable 修饰．
            - 它可以是 virtual 的，可以被重载也可以有 修改器modifier
            - 在对合约没有任何附加数据调用（通常是对合约转账）是会执行 receive 函数．　
                例如通过 .send() or .transfer()
                如果 receive 函数不存在, 但是有payable的fallback回退函数 那么在进行纯以太转账时，fallback 函数会调用．
                如果两个函数都没有，这个合约就没法通过常规的转账交易接收以太（会抛出异常）
     */
}