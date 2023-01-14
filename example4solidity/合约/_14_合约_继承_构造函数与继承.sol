// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;


// 如果基构造函数有参数, 派生合约需要指定所有参数。这可以通过两种方式来实现：

contract Base {
    uint x;
    constructor(uint x) { x = x; }
}

// 直接在继承列表中指定参数
contract Derived1 is Base(7) {
    constructor() {}
}

// 或通过派生的构造函数中用 修饰符 "modifier"
contract Derived2 is Base {
    constructor(uint y) Base(y * y) {}
}

// or declare abstract...
abstract contract Derived3 is Base {
}

// and have the next concrete derived contract initialize it.
contract DerivedFromDerived is Derived3 {
    constructor() Base(10 + 10) {}
}
// 一种方法直接在继承列表中调用基类构造函数（ is Base(7) ）。 
// 另一种方法是像 修改器modifier 使用方法一样， 作为派生合约构造函数定义头的一部分，（ Base(y * y) )。 
// 如果构造函数参数是常量并且定义或描述了合约的行为，使用第一种方法比较方便。 如果基类构造函数的参数依赖于派生合约，那么必须使用第二种方法。
// 参数必须在两种方式中（继承列表或派生构造函数修饰符样式）使用一种 。 在这两个位置都指定参数则会发生错误。
// 如果派生合约没有给所有基类合约指定参数，则这个合约必须声明为抽象合约。 在这种情况下，当另一个合约从它派生出来时，
// 另一个合约的继承列表或构造函数必须为所有还没有指定参数的基类提供必要的参数（否则，其他合约也必须被声明为抽象的）。 
// 例如，在上面的代码片段中，可以看到的 Derived3 和 DerivedFromDerived 。