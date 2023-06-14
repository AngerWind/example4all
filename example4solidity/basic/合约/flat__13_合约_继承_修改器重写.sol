
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\basic\合约\_13_合约_继承_修改器重写.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0

pragma solidity ^0.8.17;

// 修改器重写也可以被重写，工作方式和 函数重写 类似。 
// 需要被重写的修改器也需要使用 virtual 修饰， override 则同样修饰重载，例如：
// 如果是多重继承，所有直接父合约必须显示指定override， 例如：

contract Base1{
    modifier foo() virtual {_;}
}
contract Base2{
    modifier foo() virtual {_;}
}
contract Inherited is Base1, Base2{
    modifier foo() override(Base1, Base2) {_;}
}
