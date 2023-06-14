
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\basic\合约\_03_合约_函数可见性.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0

pragma solidity ^0.8.17;
/**
        函数可见性: 
        由于 Solidity 有两种函数调用：外部调用则会产生一个EVM调用，而内部调用不会，这里有 4 种可见性：
            public
                任何用户或者合约都可以通过外部调用调用, 其他函数也可以通过内部调用进行调用
            external
                external 与public 类似，只不过这些函数只能通过外部调用被调用 - 它们不能被合约内的其他函数调用。
                一个外部函数 f 不能从内部调用（即 f 不起作用，但 this.f() 可以）。
            
            internal
                内部可见性函数访问可以在当前合约或派生的合约访问，不可以外部访问。 
                由于它们没有通过合约的ABI向外部公开，它们可以接受内部可见性类型的参数：比如映射或存储引用。
            private
                private 函数和状态变量仅在当前定义它们的合约中使用，并且不能被派生合约使用。
*/

contract C {
    uint256 private data;

    function f(uint256 a) private pure returns (uint256 b) {
        return a + 1;
    }

    function setData(uint256 a) public {
        data = a;
    }

    function getData() public view returns (uint256) {
        return data;
    }

    function compute(uint256 a, uint256 b) internal pure returns (uint256) {
        return a + b;
    }
}

// 下面代码编译错误
contract D {
    function readData() public {
        C c = new C();
        // uint256 local = c.f(7); // 错误：成员 `f` 不可见
        c.setData(3);
        // local = c.getData();
        // local = c.compute(3, 5); // 错误：成员 `compute` 不可见
    }
}

contract E is C {
    function g() public {
        C c = new C();
        uint256 val = compute(3, 5); // 访问内部成员（从继承合约访问父合约成员）
    }
}

