// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract HelloWorld {
    /**
        - Solidity 中有 if，else，while，do，for，break，continue，return，? : 
            用于表示条件的括号 不可以 被省略，单语句体两边的花括号可以被省略。

        - if(1){}这种在solidity中不能使用

        - Solidity还支持 try/ catch 语句形式的异常处理，但仅用于外部函数调用和合约创建调用。 
            使用:ref:revert 语句 <revert-statement> 可以触发一个”错误”。
     */
     function ifTest(uint a) public pure returns (string memory) {
        if (a % 2 == 0){
            return unicode"偶数";
        } else {
            return unicode"奇数";
        }
     }

     function forTest() public pure returns (uint) {
        uint8[5] memory array = [1, 2, 3, 4, 5];
        uint sum = 0;
        for (uint8 i = 0; i < array.length; i++){
            sum += array[i];
        }
        return sum;
     }
}
