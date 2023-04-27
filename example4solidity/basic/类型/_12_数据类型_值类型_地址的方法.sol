// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract Caller {
    /**
        address(): 将十六进制转换为普通地址, 或者获取合约的地址
        payable(): 将普通地址转换为可支付地址
    */
    function testAddress() public view {
        address current = address(this); // 获取当前合约地址
        address payable currentPay = payable(current); // 将地址转换为payable地址
    }
    /**
        .transfer(uint256 amount): 向一个可支付地址发送eth, 如果当前合约的余额不够多，或者如果以太转移被接收帐户拒绝， transfer 函数会失败而进行回退。
        .send(uint256 amount) returns(bool): 同transfer, 但是执行失败，当前的合约不会因为异常而终止，而是返回false
    */

}