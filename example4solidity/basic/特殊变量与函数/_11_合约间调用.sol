// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract Tester {
    SimpleStorage s = new SimpleStorage();
    Caller caller = new Caller{value: 2 gwei}();

    constructor() payable {}

    function testCallWithContract() public {
        caller.callWithContract(s);
        require(s.x() == 1, "unexpected result");
    }

    function testCallWithAddress() public {
        caller.callWithAddress(address(s));
        require(s.x() == 5, "unexpected result");
    }

    function testCallWithCall() public {
        caller.callWithCall(address(s));
        require(s.x() == 10, "unexpected result");
    }
}

contract Caller {
    event Caller_Response(bool success, bytes data);

    constructor() payable {}

    function callWithContract(SimpleStorage sto) public returns (uint x) {
        x = sto.setX(1); //直接调用合约的方法
    }

    function callWithAddress(address stoAddress) public returns (uint x) {
        // 通过地址创建合约, 然后调用合约, 同时发送eth
        x = SimpleStorage(payable(stoAddress)).setX{value: 2000}(5);
    }

    function callWithCall(address stoAddress) public returns (uint x) {
        // 通过call来调用合约的方法, encodeWithSignature需要传入函数的名字和参数类型, 以及真实的参数
        // uint不能使用, 必须要使用uint256
        (bool success, bytes memory data) = stoAddress.call{value: 1000 wei, gas: 1 gwei}(
            abi.encodeWithSignature("setX(uint)", 10)
        );

        emit Caller_Response(success, data);
        require(success, "call failed");
        x = abi.decode(data, (uint)); // 解码data, 需要传入返回值类型的元组
    }

    function getBalance() public view returns (uint) {
        return address(this).balance;
    }

    receive() external payable {}
}

contract SimpleStorage {
    uint public x;

    function setX(uint256 _x) public payable returns (uint) {
        x = _x;
        return x;
    }

    receive() external payable {
        (success, ) = address(this).call(
            abi.encodePacked(
                bytes4(keccak256(abi.encodePacked(_method, "(bytes,bytes,uint64)"))),
                abi.encode(_bytes, _bytes1, _num)
            )
        );
    }
}
