// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract HelloWorld {
    /**
        地址分为外部地址和合约地址, 每个地址都有一块持久化内存区, 称为存储
            - 一个地址20字节
            - 地址支持<=, <, ==, !=, >=, > (直接转成10进制比较)
            - 地址有两种类型: 
                address
                address payable(可支付地址): 与address相同，不过有成员函数transfer和send, 
                                            可以向可支付地址发送以太币, 而不能向一个普通地址发送以太币
            - 可支付地址可以隐式转换为普通地址, 普通地址必须通过payable()函数转换为可支付地址
            - address可以和uint160, 整型字面量, bytes20, 合约类型相互转换
     */
    address public addr1 = 0xdCad3a6d3569DF655070DEd06cb7A1b2Ccd1D3AF;
    address payable public addrPayable =
        payable(0xdCad3a6d3569DF655070DEd06cb7A1b2Ccd1D3AF); // 直接写的话就是普通地址, 需要通过payable()转换为可支付地址


    address addr2 = addrPayable; // 可支付地址隐式转换为普通地址
    
    bytes20 public b20 = bytes20(addr1); // address => bytes20
    address public addr3 = address(b20); // bytes20 => address
 
    uint160 public u160 = uint160(addr1); // address => uint160
    address public addr4 = address(u160); // uint160 => address

    // 需要注意的时候, 长度不等的数字和定长数组在转换address的时候, 需要一个中间类型
    // 详细情况查看   基本数据类型间的显示转换
    // bytes32要转成160位的address, 要么转换成bytes20( bytes32 => bytes20 ), 要么转换成uint160 ( bytes32 => uint256 => uint160 )
    bytes32 public b32 =  0x111122223333444455556666777788889999AAAABBBBCCCCDDDDEEEEFFFFCCCC;
    address public addr5 = address(bytes20(b32)); // 0x111122223333444455556666777788889999aAaa, 截断右边
    address public addr6 = address(uint160(uint256(b32))); // 0x777788889999AaAAbBbbCcccddDdeeeEfFFfCcCc, 截断左边

    /**
       address成员变量:
            - balance: 类型uint256, 表示当前账户余额,单位Wei
            - code: 类型bytes memory, 表示当前地址上的代码
            - codehash: 类型bytes32, 表示当前地址上的code的hash

        address方法: 
    */
    function getBalance() external view returns (uint256) {
        return address(this).balance;
    }

    receive() external payable {

    }

}
