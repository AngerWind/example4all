// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;


contract HelloWorld {
    /**
        定长字节数组: bytes1， bytes2， bytes3， …， bytes32
            支持的运算符:
                比较运算符： <=， <， ==， !=， >=， > (返回布尔型,)
                位运算符： &， |， ^ （按位异或）， ~ （按位取反）
                移位运算符： << （左移位）， >> （右移位）
            索引访问: 可以通过索引v[i]的形式来访问变量v的第i个字节, 只读
            成员变量: length, uint8类型, 表示字节数组的长度, 只读
     */
     bytes3 public b = 0xab_cd_ef; // 0xabcdef, 十六进制字面量隐式转换为bytes3
     bytes1 public b1 = b[2]; // 0xef
     uint8 public length = b.length; // 3

     // 0x61626300, 字符串字面量转bytes4, "abc"在被赋值给byte4的时候, 比解释为字节原始值
     // 即a的ascII码为97, 而97的十六进制为0x61, 所以"abc"在转bytes4的时候变成0x61626300, 低位不足补0
     bytes4 public s = "abc"; 
}