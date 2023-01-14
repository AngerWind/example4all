// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    receive接收以太:
        一个合约最多有一个 receive 函数, 声明函数为： receive() external payable { ... }
        可以是virtual, 可以被重载, 也可以有修改器
        在对合约没有任何附加数据调用（通常是对合约转账）是会执行 receive 函数．　例如　通过 .send() or .transfer() or address{value: 100}.call("")
        如果 receive 函数不存在，就会调用payable的fallback回退函数
        如果两个函数都没有，这个合约就没法通过常规的转账交易接收以太（会抛出异常）

    fallback回退函数:
        合约可以最多有一个回退函数。函数声明为： 
            fallback () external [payable]
            fallback (bytes calldata input) external [payable] returns (bytes memory output)
        可以是virtual的, 可以被重载, 可以有修改器
        如果其他合约对当前合约的调用中, 没有函数与给定的函数标识符匹配, fallback会被调用
        或者当其他合约向当前合约发送以太, 而当前合约没有receive函数时, 会调用payable的fallback函数 
 */
// ------------------------- 例子1 --------------------------------
contract Receiver {
    mapping(address => uint256) public amounts;

    event Received(address, uint);

    function getBalance() public view returns (uint256) {
        return address(this).balance;
    }

    receive() external payable {
        // 获取发送者, 以及金额
        emit Received(msg.sender, msg.value);
    }

    // 其他合约对当前合约的调用中, 没有函数与给定的函数标识符匹配
    // 当其他合约向当前合约发送以太, 而当前合约没有receive函数时
    // fallback() external payable {
    //     // 获取发送者, 以及金额
    //     emit Received(msg.sender, msg.value);
    // }
}

contract Sender {
    function getBalance() public view returns (uint256) {
        return address(this).balance;
    }

    function send(address payable addr, uint256 amount) public{
        addr.transfer(amount); // 向指定地址发送以太
    }

    receive() external payable {}

    fallback() external payable {}
}

// --------------------------- 例子2 ------------------------------
contract Test {
    // 发送到这个合约的所有消息都会调用此函数（因为该合约没有其它函数）。
    // 向这个合约发送以太币会导致异常，因为 fallback 函数没有 `payable` 修饰符
    fallback() external { x = 1; }
    uint x;
}


// 这个合约会保留所有发送给它的以太币，没有办法返还。
contract TestPayable {
    uint x;
    uint y;

    // 除了纯转账外，所有的调用都会调用这个函数．
    // (因为除了 receive 函数外，没有其他的函数).
    // 任何对合约非空calldata 调用会执行回退函数(即使是调用函数附加以太).
    fallback() external payable { x = 1; y = msg.value; }

    // 纯转账调用这个函数，例如对每个空empty calldata的调用
    receive() external payable { x = 2; y = msg.value; }
}

contract Caller {
    function callTest(Test test) public  {
        //  test.x 结果变成 == 1。
        (bool success,) = address(test).call(abi.encodeWithSignature("nonExistingFunction()"));
        require(success);
        
        // address(test) 不允许直接调用 ``send`` ,  因为 ``test`` 没有 payable 回退函数
        //  转化为 ``address payable`` 类型 , 然后才可以调用 ``send``
        address payable testPayable = payable(address(test));

        // 以下将不会编译，但如果有人向该合约发送以太币，交易将失败并拒绝以太币。
        // test.send(2 ether）;
    }

    function callTestPayable(TestPayable test) public returns (bool) {
        // 结果 test.x 为 1  test.y 为 0.
        (bool success,) = address(test).call(abi.encodeWithSignature("nonExistingFunction()"));
        require(success);
        
        // 结果test.x 为1 而 test.y 为 1.
        (success,) = address(test).call{value: 1}(abi.encodeWithSignature("nonExistingFunction()"));
        require(success);
        

        // 发送以太币, TestPayable 的 receive　函数被调用．
        // 因为函数有存储写入, 会比简单的使用 ``send`` or ``transfer``消耗更多的 gas。
        // 因此使用底层的call调用
        // 结果 test.x 为 2 而 test.y 为 2 ether.
        (success,) = address(test).call{value: 2 ether}("");
        require(success);

        return true;
    }

}