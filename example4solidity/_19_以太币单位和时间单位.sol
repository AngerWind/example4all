// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract A {
    // 以太币Ether 单位之间的换算就是在数字后边加上 wei ， gwei 或 ether 来实现的
    // 如果后面没有单位，缺省为 wei。
    function test() public pure {
        assert(1 wei == 1);
        assert(1 gwei == 1e9);
        assert(1 ether == 1e18);
    }

    // 秒是缺省时间单位，在时间单位之间，数字后面带有 seconds、minutes、hours、days 和weeks的可以进行换算，基本换算关系如下：
    // 1 == 1 seconds
    // 1 minutes == 60 seconds
    // 1 hours == 60 minutes
    // 1 days == 24 hours
    // 1 weeks == 7 days
    // 由于闰秒造成的每年不都是 365 天、每天不都是 24 小时 leap seconds，所以如果你要使用这些单位计算日期和时间，请注意这个问题。因为闰秒是无法预测的，所以需要借助外部的预言机（oracle，是一种链外数据服务，译者注）来对一个确定的日期代码库进行时间矫正。
    // 这些后缀不能直接用在变量后边。如果想用时间单位（例如 days）来将输入变量换算为时间，你可以用如下方式来完成:
    function f(uint256 start, uint256 daysAfter) public view {
        if (block.timestamp >= start + daysAfter * 1 days) {
            // ...
        }
    }
}
