// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    合约的修改器类似于java中的切面, 可以包裹函数, 改变函数的行为.
    修改器modifier 是合约的可继承属性，并可能被派生合约覆盖 , 但前提是它们被标记为 virtual

    感觉没啥屌用, 就是放在函数前面用于条件判断
*/
contract A {
    uint256 a = 0;

    modifier checkA() {
        if (a > 10) {
             _; // 修改器所修饰的函数体会被插入到特殊符号 _ 的位置。
        } else {
            return; // 显式的 return 语句会跳出当前修改器, 返回到上一个修改器, 并执行上一个修改器_之后的内容
        }
    }
}

contract B is A {
    // 修改器可以定义参数
    modifier checkB(uint256 num) {
        if (num > 10){
            _;
        }
    }
    // 函数add使用了checkA和checkB两个修改器
    // 给checkB传参可以是当前合约的状态变量, 也可以是函数的参数
    // 修改器可以选择完全不执行函数体，在这种情况下，返回的变量被设置为默认值
    function add(uint256 a, uint256 b) public view checkA checkB(b) returns (uint256) {
        return a + b;
    }
}

