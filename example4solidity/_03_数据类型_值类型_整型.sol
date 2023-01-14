// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
 * !!!!与go类似, 任何的变量都有零值!!!!
 */
contract HelloWorld {
    /**
       整型:
              - 支持关键字uint8到uint256以及int8到int256，以8位为步长递增。 
              - uint 和 int 分别是 uint256 和 int256 的别名。
              - 零值: 0
              - 支持的运算符: 
                    比较运算符： <= ， < ， == ， != ， >= ， > （返回布尔值）
                    位运算符： & ， | ， ^ （异或）， ~ （位取反）
                    移位运算符： <<， >>
                    算数运算符： +, -, -(负号, 仅针对有符号整型），*，/， %, **(幂）
     */
    uint256 public u1 = 123;


    uint8 public u8Max = type(uint8).max; // 获取uint8的最大值, 2**8-1
    uint8 public u8Min = type(uint8).min; // 获取uint8的最小值, 0

    uint256 public u256Max = type(uint256).max; // 获取uint256的最大值, 2**256-1
    uint256 public u258Min = type(uint256).min; // 获取uint256的最小值, 0

    int256 public max256 = type(int256).max; // 获取int256的最大值, 2**255-1
    int256 public min256 = type(int256).min; // 获取int256的最大值, -(2**255)

    // 0.8.0之后, 加减乘除(+,-,*,/,++,--,+=,-=,**,)运算会进行溢出检测(上溢, 下溢)
    // 如果不需要安全检查, 可以使用unchecked{}包裹代码, 系统不会进行检测, 溢出之后会自动截断
    // 位移运算不会进行溢出检测
    function add() public pure returns (uint256) {
        uint256 max = type(uint256).max;
        unchecked {
            return max + 1; // 这里自动截断变成0了
        }
    }

    function add1() public pure returns (uint256) {
        uint256 max = type(uint256).max;
        return max + 1; // 这里会报错, uint256上溢
    }
}
