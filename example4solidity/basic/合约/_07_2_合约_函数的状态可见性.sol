// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract C {
    /**
        函数的状态可变性:
            view(视图函数): 将函数声明为view类型, 需要保证不修改状态
                    以下语句被认为是修改状态:
                        0.修改状态变量。
                        1.产生事件。
                        2.创建其它合约。
                        3.使用 selfdestruct。
                        4.通过调用发送以太币。
                        5.调用任何没有标记为 view 或者 pure 的函数。
                        6.使用低级调用。
                        7.使用包含特定操作码的内联汇编。
            pure(纯函数): 将函数声明为pure类型, 需要保证不读取和修改状态(可以读取函数参数)
                    以下语句被认为是读取状态:
                        1.读取状态变量。(包括immutable)
                        2.访问 address(this).balance 或者 <address>.balance。
                        3.访问 block，tx， msg 中任意成员 （除 msg.sig 和 msg.data 之外）。
                        4.调用任何未标记为 pure 的函数。
                        5.使用包含某些操作码的内联汇编。
        
        在Solidity v4.17之前，只有constant，
        后来有人嫌constant这个词本身代表变量中的常量，不适合用来修饰函数，所以将constant拆成了view和pure。
     */
    function viewTest(uint256 a, uint256 b) public view returns (uint256) {
        return a * (b + 42) + block.timestamp;
    }

    function pureTest(uint256 a, uint256 b) public pure returns (uint256) {
        return a * (b + 42);
    }
}
