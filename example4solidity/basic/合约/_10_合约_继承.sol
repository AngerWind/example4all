// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    - solidity支持多重继承, 即一个合约可以继承多个合约
    - 如果出现重写, 将调用子类的函数, 除非使用super关键字或者 父合约.方法
    - 当一个合约从多个合约继承时，在区块链上只有一个合约被创建，所有基类合约（或称为父合约）的代码被编译到创建的合约中。
        这意味着对基类合约函数的所有内部调用也只是使用内部函数调用（super.f（..）将使用JUMP跳转而不是消息调用）。
    - 子类不能覆盖父类的状态变量
*/
contract Owned {
    constructor() {
        owner = payable(msg.sender);
    }

    address payable owner;
}

// 使用 is 从另一个合约派生。派生合约可以访问所有非私有成员，包括内部（internal）函数和状态变量，
// 但无法通过 this 来外部访问。
contract Destructible is Owned {
    // 关键字`virtual`表示子类可以重写该函数, 没有virtual的函数子类不能重写
    function destroy() public virtual {
        if (msg.sender == owner) selfdestruct(owner);
    }
}

// 这些抽象合约仅用于给编译器提供接口。
// 注意函数没有函数体。
// 如果一个合约没有实现所有函数，则只能用作接口。
abstract contract Config {
    function lookup(uint256 id) public virtual returns (address adr);
}

abstract contract NameReg {
    function register(bytes32 name) public virtual;

    function unregister() public virtual;
}

// 可以多重继承。请注意，`Owned` 也是 Destructible 的基类，
// 但只有一个 `Owned` 实例（就像 C++ 中的虚拟继承）。
// (c++中的菱形继承问题: 即B,C继承自A, D继承自B,C, 这个时候A就会有两份A的成员变量和函数,
// 而如果BC虚拟继承A的话, 那么D继承BC的时候就只会有一份A的成员变量和函数)
contract Named is Owned, Destructible {
    constructor(bytes32 name) {
        Config config = Config(0xD5f9D8D94886E70b06E474c3fB14Fd43E2f23970);
        NameReg(config.lookup(1)).register(name);
    }

    // 函数可以被另一个具有相同名称和相同数量/类型输入的函数重写。
    // 如果重载函数有不同类型的输出参数，会导致错误。
    // 本地和基于消息的函数调用都会考虑这些重载。

    //如果要重写父类的函数，则需要使用 `override` 关键字。 如果您想子类能够再次覆盖此函数，则需要再次指定 `virtual` 关键字。
    function destroy() public virtual override {
        if (msg.sender == owner) {
            Config config = Config(0xD5f9D8D94886E70b06E474c3fB14Fd43E2f23970);
            NameReg(config.lookup(1)).unregister();
            // 虽然重写了父类的destory函数, 这里仍然可以通过Destructible.destroy调用了父类的destory函数
            Destructible.destroy();
        }
    }
}

// 如果构造函数接受参数，
// 则需要在声明时提供，
// 或在派生合约的构造函数位置以修改器调用风格提供（见下文）。
contract PriceFeed is Owned, Destructible, Named("asdsd") {
    uint256 info;

    // 或者下面这样
    // constructor(bytes32 _name) Named(_name) {}

    function updateInfo(uint256 newInfo) public {
        if (msg.sender == owner) info = newInfo;
    }

    // 这里只指定了override, 没有指定virtual
    // 意味着PriceFeed的子类不能再重写这个函数
    function destroy() public override(Destructible, Named) {
        // 调用父类Named的destory函数
        Named.destroy();
    }

    function get() public view returns (uint256 r) {
        return info;
    }
}
