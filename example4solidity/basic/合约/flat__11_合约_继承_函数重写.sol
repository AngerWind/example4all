
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\basic\合约\_11_合约_继承_函数重写.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0

pragma solidity ^0.8.17;

/**
    - 父合约标记为 virtual 函数可以在继承合约里重写(overridden)以更改他们的行为。
        重写的函数需要使用关键字 override 修饰。

    - 重写函数只能将函数的可见性从 external 更改为 public 。

    - 重写函数只能将函数的可变性按照以下顺序更改为更严格的一种： 
        nonpayable 可以被 view 和 pure 覆盖。 
        view 可以被 pure 覆盖。 
        payable 是一个例外，不能更改为任何其他可变性。

    - 如果函数没有标记为 virtual ， 那么派生合约将不能重写该方法
    - 已重写的方法仍然可以使用(super. 或者 父合约.) 来调用
    - private 的函数是不可以标记为 virtual 的。
    - 除接口之外（因为接口会自动作为 virtual ），没有实现的函数必须标记为 virtual
 */
contract Base{
    function foo() virtual external view {}
    function boo() virtual public view {}
}
contract Middle is Base {
    function foo() override public pure {}
    function boo() virtual public override view {
        super.boo(); // 通过super调用父类的方法
        Base.boo(); // 通过父类名调用父类的方法
    }
}

// ----------------------------------------
contract Base1{
    function foo() virtual public {}
}
contract Base2{
    function foo() virtual public {}
}
contract Inherited is Base1, Base2{
    // 继承自两个基类合约定义的foo(), 必须显示的指定 override以及所有父合约名
    function foo() public override(Base1, Base2) {}
}

// ------------------------------------

// 如果getter 函数的参数和返回值都和外部函数一致时，外部（external）函数是可以被 public 的状态变量的getter函数重写的，例如
contract A{
    function f() external view virtual returns(uint) { return 5; }
}
contract B is A{
    uint public override f;
}
