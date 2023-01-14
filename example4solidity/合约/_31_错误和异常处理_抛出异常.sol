// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

error InsufficientBalance(uint256 available, uint256 required);

contract TestToken {
    /** 
        - Solidity 中的错误（关键字error）提供了一种方便且省gas的方式来向用户解释为什么一个操作会失败。
            它们可以被定义在合约（包括接口和库）内部和外部。

        - 使用revert语句来抛出一个异常, 它会还原当前调用中的发生的所有变化，并将错误数据传回给调用者。
            revert语句接受一个自定义错误作为不带括号的直接参数: revert CustomError(arg1, arg2);
            出于向后兼容的原因，还有revert()函数，它使用圆括号并接受一个字符串:revert(); revert(“description”);

        - 在EVM内部定义了两个内建的error, 分别是Panic(uint256)和Error(string)
            Panic这类error的含义与java中的RuntimeException类似, 即正常的函数代码永远不会产生 Panic , 
            甚至是基于一个无效的外部输入时。如果发生了，那就说明出现了一个需要你修复的 bug。
            Error这类error的含义与java中的非RuntimeException类似

        - 使用自定义错误实例通常比使用字符串描述便宜得多，因为你可以使用错误名称来描述它，该名称仅用4个字节编码。

        - 错误不能被重写或覆盖，但是可以继承。
        - 在内部， Solidity 对异常执行回退操作（指令 0xfd ），从而让 EVM 回退对状态所做的所有更改。
    */
    function check(uint256 available, uint256 required) public pure {
        // 检查可用余额是否小于要求的余额
        if (available < required) {
            // 抛出自定义的异常
            revert InsufficientBalance({
                available: available,
                required: required
            });
        } else if (available == required) {
            // 抛出内建的Error异常
            revert(unicode"余额不足啦....");
        } else {
            // do something...
        }
    }
}
