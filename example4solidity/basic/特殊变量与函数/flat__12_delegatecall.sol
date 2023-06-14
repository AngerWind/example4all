
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\basic\特殊变量与函数\_12_delegatecall.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0

// delegatecall, call, staticcall 都可以调用合约
// 都有gas选项, 只有call有value选项
// delegatecall和staticcall不会修改环境变量(msg.value, msg.sender, storage, this)
// staticcall不能修改状态变量

/**
目前delegatecall主要有两个应用场景：

代理合约（Proxy Contract）：
    将智能合约的存储合约和逻辑合约分开：
    代理合约（Proxy Contract）存储所有相关的变量，并且保存逻辑合约的地址；
    所有函数存在逻辑合约（Logic Contract）里，通过delegatecall执行。
    当升级时，只需要将代理合约指向新的逻辑合约即可。

EIP-2535 Diamonds（钻石）：
    钻石是一个支持构建可在生产中扩展的模块化智能合约系统的标准。
    钻石是具有多个实施合同的代理合同。 更多信息请查看：https://www.wtf.academy/solidity-advanced/Delegatecall/
*/
pragma solidity ^0.8.17;

contract Tester {
    constructor() payable {}

    // 测试call函数
    function test() public {
        C c = new C();
        B b = new B(address(c));
        b.set(10);
        require(
            b.number() == 0 &&
                b.sender() == address(0) &&
                c.number() == 10 &&
                c.sender() == address(b)
        );
    }
    // 测试delegatecall函数
    function test1() public {
        C c = new C();
        B b = new B(address(c));
        b.set1(10); // b delegatecall c, 修改的都是b的变量
        require(
            b.number() == 10 &&
                b.sender() == address(this) &&
                c.number() == 0 &&
                c.sender() == address(0)
        );
    }
}

contract B {
    uint256 public number;
    address public sender;
    address public proxy;

    constructor(address _proxy) {
        proxy = _proxy;
    }

    function set(uint256 num) public payable {
        (bool success, ) = proxy.call(
            abi.encodeWithSignature("set(uint256)", num)
        );
        require(success, "failed");
    }

    function set1(uint256 num) public payable {
        (bool success, ) = proxy.delegatecall(
            abi.encodeWithSignature("set(uint256)", num)
        );
        require(success, "failed");
    }
}
contract C {
    uint256 public number;
    address public sender;

    function set(uint256 _num) public payable {
        sender = msg.sender;
        number = _num;
    }
}


/** -------------------delegatecall设计代理合约,  逻辑可以升级 ---------------------------*/ 


contract Tester1 {
    function test() public {
        SimpleStorageLogic logic = new SimpleStorageLogic();
        SimpleStorageLogic1 logic1 = new SimpleStorageLogic1();
        SimpleStorageProxy proxy = new SimpleStorageProxy(address(logic));
        proxy.doSomething();
        require(proxy.num() == 1);
        proxy.update(address(logic1));
        proxy.doSomething();
        require(proxy.num() == 10);
    }
}
// 作为代理和被代理的合约的父类, 定义了存储结构, 对外的方法
abstract contract SimpleStorage {
    uint public num;
    function doSomething() public virtual;
}
// 代理合约的通用接口
abstract contract Proxy {
    address public logicContract;
    constructor (address _logicContract){
        logicContract = _logicContract;
    }
    function update(address _logicContract) public{
        logicContract = _logicContract;
    }
}
// 代理合约
contract SimpleStorageProxy is SimpleStorage, Proxy{
    constructor(address _logicContract) Proxy(_logicContract){}
    function doSomething() public override {
        (bool success, ) = logicContract
        .delegatecall(abi.encodeWithSignature("doSomething()"));
        require(success, "delegatecall failed");
    }
}
// 逻辑合约
contract SimpleStorageLogic is SimpleStorage{
    function doSomething() public override {
        num += 1;
    }
}
// 逻辑合约
contract SimpleStorageLogic1 is SimpleStorage{
    function doSomething() public override {
        num = 10;
    }
}

