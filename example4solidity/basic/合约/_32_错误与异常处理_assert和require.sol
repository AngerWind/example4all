// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract A {
    /**
        - 在EVM内部定义了两个内建的error, 分别是Panic(uint256)和Error(string)
            Panic这类error的含义与java中的RuntimeException类似, 即正常的函数代码永远不会产生 Panic , 
            甚至是基于一个无效的外部输入时。如果发生了，那就说明出现了一个需要你修复的 bug。
            !!!panic只能通过状态码来判断异常的原因, 不能自定义!!!

            Error这类error的含义与java中的非RuntimeException类似

        - EVM中提供了assert来产生Panic异常, require产生Error异常
     */
    function requireTest(uint256 a, uint256 b) public pure {
        require(a < b, "error message"); // 如果表达式成立, 会抛出一个Error异常
    }

    function div(uint256 a, uint256 b) public pure returns (uint256) {
        assert(b != 0); // 触发要求除数不能为0, 如果表达式成立, 会抛出一个Panic异常
        return a / b;
    }

    /**
        - 以下是一些产生Panic的操作
            0x00: 用于常规编译器插入的Panic。
            0x01: 如果你调用 assert 的参数（表达式）结果为 false 。
            0x11: 在 unchecked { ... } 外，如果算术运算结果向上或向下溢出。
            0x12; 如果你用零当除数做除法或模运算（例如 5 / 0 或 23 % 0 ）。
            0x21: 如果你将一个太大的数或负数值转换为一个枚举类型。
            0x22: 如果你访问一个没有正确编码的存储byte数组.
            0x31: 如果在空数组上 .pop() 。
            0x32: 如果你访问 bytesN 数组（或切片）的索引太大或为负数。(例如： x[i] 而 i >= x.length 或 i < 0).
            0x41: 如果你分配了太多的内内存或创建了太大的数组。
            0x51: 如果你调用了零初始化内部函数类型变量。
        - 以下是一些产生Error的情况
            如果你调用 require(x) ，而 x 结果为 false 
            如果你使用 revert() 或者 revert("description")
            如果你在不包含代码的合约上执行外部函数调用。
            如果你通过合约接收以太币，而又没有 payable 修饰符的公有函数（包括构造函数和 fallback 函数）。
            如果你的合约通过公有 getter 函数接收 Ether 
     */
}
