// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.8.1;

/***


    结论: 如果一个合约没有receive函数, 同时也没有payable的fallback函数, 那么就不能将其转换为payable类型
    (一个地址能否将其转换为payable, 只取决于他有没有receive函数, 或者payable的fallback函数)
    也就不能调用其send, transfer, 无负载的call向其转账
    但是依旧可以通过带有payable的其他函数向其转账
 */
contract Receiver {
    uint256 public x;

    // 用于接收eth
    function fund(uint256 _x) public payable {
        x = _x;
    }

    function getBalance() public view returns (uint256) {
        return address(this).balance;
    }
}

contract Sender {
    // 在勾账函数上添加payable, 表示在部署的时候就可以转入一定的eth
    constructor() payable {}

    // 接收eth需要
    receive() external payable {}

    function getBalance() public view returns (uint256) {
        return address(this).balance;
    }

    // 向指定的地址发送自动的金额
    function pay(uint256 amount, Receiver receiver) public {
        // Receiver没有receive或者payable的fallback函数, 所以不能转换为payable
        payable(address(receiver)).transfer(amount);
    }

    function payToReceiver1(
        uint256 amount,
        Receiver receiver,
        uint256 args
    ) public {
        // 虽然Receiver没有receive函数, 但是还是可以通过带有payable的fund向其转账
        receiver.fund{value: amount}(args);
    }
}
