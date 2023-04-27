// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract DeleteContract {
    constructor() payable {}

    receive() external payable {}

    function deleteContract() external {
        // 调用selfdestruct销毁合约，并把剩余的ETH转给msg.sender
        // 最好使用owner
        // 该功能会降低用户对合约的信心
        selfdestruct(payable(msg.sender));
    }

    function getBalance() external view returns (uint balance) {
        balance = address(this).balance;
    }
}
