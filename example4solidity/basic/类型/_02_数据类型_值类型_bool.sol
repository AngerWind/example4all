// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;


/**
 * !!!!与go类似, 任何的变量都有零值!!!!
 */
contract TinyStoryge {
    /**
       bool: 
            - 只有两个值: true, false
            - 零值: false
            - 支持的运算符: !, &&, ||, ==, !=
                     运算符 || 和 && 都遵循短路规则
     */
     bool flag;

     function getFlag() public view returns(bool) {
        return flag;
    }
     
}
