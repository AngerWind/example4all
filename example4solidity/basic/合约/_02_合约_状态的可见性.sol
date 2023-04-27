// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract A {
    /**
        状态变量可见性
        状态变量有 3 种可见性：
            public:
                对于public状态变量会自动生成一个getter函数。 以便其他的合约读取他们的值。 
                当在同一个合约里使用时，外部方式访问 (如: this.x) 会调用getter函数，而内部方式访问 (如: x) 会直接从存储中获取值。 
                Setter函数则不会被生成，所以其他合约不能直接修改其值。
            internal(默认):
                内部可见性状态变量只能在它们所定义的合约和派生合同中访问。 
                它们不能被外部访问。 
                类似java中的protect
            private:
                私有状态变量就像内部变量一样，但它们在派生合约中是不可见的。

        Note:
            设置为 private``或 ``internal，只能防止其他合约读取或修改信息，
            但它仍然可以在链外查看到。
      */

    uint public a = 10;
    uint internal b = 10;
    uint private c = 10;
}

contract B is A {
    function test1() public { 
        a = 20;
        b = 20; // internal, 可以访问
        // c = 20; // private, 无法访问
    }
}

contract C{
    function test1() public { 
        B ctct = new B();
        uint a = ctct.a(); // public, 可以通过自动生成的getter函数读取
        // B.a = 20; // public, 无法修改
        // uint b = B.b; // 无法访问
        // uint c = B.c; // 无法访问
    }
}


