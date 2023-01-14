// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract Receiver{

    mapping(address => uint) amounts; 

    function getBalance() view public returns(uint) {
        return address(this).balance;
    }

    receive() external payable {
        
    }
}

contract Sender{

    function getBalance() view public returns(uint) {
        return address(this).balance;
    }

    function send(address payable addr, uint amount) public {
        addr.transfer(amount);
    }

    receive() external payable {
        
    }
}

contract LedgerBalance{
    mapping (address => uint) public amounts;
    function updateBalance(uint newBalance) public returns (address) {
        amounts[msg.sender] = newBalance;
        return msg.sender;
    }
}

contract Updated{
    function updateBalance() public returns (address) {
        LedgerBalance ledgerBalance = new LedgerBalance();
        return ledgerBalance.updateBalance(20);
    }
}
