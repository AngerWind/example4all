// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

// 我们定义了一个新的结构体数据类型，用于在调用合约中保存数据。
struct Data {
    mapping(uint256 => bool) flags;
}

library Set {
    /**

        库与合约类似，库的目的是只需要在特定的地址部署一次，而它们的代码可以通过 EVM 的 DELEGATECALL特性进行重用。
        这意味着如果库函数被调用，它的代码在调用合约的上下文中执行，即 this 指向调用合约，特别注意，他访问的是调用合约存储的状态。 
        因为我们假定库是无状态的，所以如果它们不修改状态（view或者pure函数），库函数仅能通过直接调用来使用（即不使用 DELEGATECALL 关键字）， 
        - 没有状态变量
        - 不能够继承或被继承
        - 不能接收以太币
        - 不可以被销毁
        - 特别是，任何库不可能被销毁。
    */

    // 注意第一个参数是“storage reference”类型，因此在调用中参数传递的只是它的存储地址而不是内容。
    // 这是库函数的一个特性。如果该函数可以被视为对象的方法，则习惯称第一个参数为 `self` 。
    function insert(Data storage self, uint256 value) public returns (bool) {
        if (self.flags[value]) return false; // 已经存在
        self.flags[value] = true;
        return true;
    }

    function remove(Data storage self, uint256 value) public returns (bool) {
        if (!self.flags[value]) return false; // 不存在
        self.flags[value] = false;
        return true;
    }

    function contains(Data storage self, uint256 value) public view returns (bool) {
        return self.flags[value];
    }

    function addr() public view returns (address) {
        return address(this);
    }
}

contract C {
    Data knownValues;

    function register(uint256 value) public {
        // 不需要库的特定实例就可以调用库函数，
        // 因为当前合约就是“instance”。
        require(Set.insert(knownValues, value));
    }
    // 如果我们愿意，我们也可以在这个合约中直接访问 knownValues.flags。

    function addr1() public view returns (address) {
        return Set.addr();
    }

    function addr2() public view returns (address) {
        return address(this);
    }


}
