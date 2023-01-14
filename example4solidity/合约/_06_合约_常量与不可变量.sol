// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;


// 常量可以写在合约外面
uint256 constant X = 32**22 + 8;

contract C {

    /**
        当前仅支持 字符串(仅常量)和值类型

        状态变量声明为 constant (常量)或者 immutable （不可变量），合约一旦部署之后，变量将不在修改。
        对于 constant 常量, 他的值在编译器确定，而对于 immutable, 它的值在部署时确定。
        编译器不会为这些变量预留存储位，它们的每次出现都会被替换为相应的常量表达式（它可能被优化器计算为实际的某个值）。

        常量:
            - 赋值给它的表达式将复制到所有访问该常量的位置，并且每次都会对其进行重新求值。
            - 只能使用那些在编译时有确定值的表达式来给它们赋值。 任何通过访问 storage，区块链数据（例如 block.timestamp, address(this).balance 或者 block.number）或执行数据（ msg.value 或 gasleft() ） 或对外部合约的调用来给它们赋值都是不允许的。
              内建（built-in）函数 keccak256 ， sha256 ， ripemd160 ， ecrecover ， addmod 和 mulmod 是允许的
        不可变量: 
            - 不可变变量在构造时进行一次求值，并将其值复制到代码中访问它们的所有位置。 对于这些值，将保留32个字节，即使它们适合较少的字节也是如此。
            - 可以在合约的构造函数中或声明时为不可变的变量分配任意值。 不可变量只能赋值一次，并且在赋值之后才可以读取。
            - 编译器生成的合约创建代码将在返回合约之前修改合约的运行时代码，方法是将对不可变量的所有引用替换为分配给它们的值。
    */
    string constant TEXT = "abc";
    bytes32 constant MY_HASH = keccak256("abc");
    uint256 immutable decimals;
    uint256 immutable maxBalance;
    address immutable owner = msg.sender;

    constructor(uint256 decimals_, address ref) {
        // 部署的时候确定不可变量
        decimals = decimals_;
        maxBalance = ref.balance;
    }

    function isBalanceTooHigh(address _other) public view returns (bool) {
        return _other.balance > maxBalance;
    }
}
