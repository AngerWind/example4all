
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\basic\类型\_12_数据类型_值类型_地址的方法(call, staticcall, delegatecall).sol
*/

 ////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0

pragma solidity ^0.8.17;  
   
    /**
        .call(bytes memory): 用给定的有效荷载(payload)发出低级`call`调用,并返回交易释放成功和返回数据
        .delegatecall(bytes memory):用给定的有效荷载(payload)发出低级`DELEGATECALL`调用,并返回交易释放成功和返回数据
        .staticcall(bytes memory):用给定的有效荷载(payload)发出低级`STATICCALL`调用,并返回交易释放成功和返回数据

        
        call: 调用后内置变量 msg 的值会修改为调用者，执行环境为被调用者的运行环境(storage, 变量, this都是被调用者的)。
        delegatecall: 调用后内置变量 msg 的值不会修改为调用者，但执行环境为调用者的运行环境((storage, 变量, this都是调用者的))。
        staticcall: 与call相同, 但是不能修改状态变量

        https://blog.csdn.net/powervip/article/details/104330170
        https://www.codercto.com/a/37136.html
        
    */
contract Caller {
    function callTest(address addr) public  returns (address,  address,  uint) {
        (bool success, bytes memory data) = addr.call(abi.encodeWithSignature("getAddressAndBalance(uint256,uint256)", 1, 2)); // !!!!方法签名不能有空格, uint和uint256不通用貌似!!!!
        // 获取返回结果
        // sender指向Caller合约, _this指向DataPool合约
        (address sender,  address _this,  uint _balance) = abi.decode(data, (address, address, uint));
       return ( sender,   _this,   _balance);
    }
    function delegatecallTest(address addr) public  returns (address,  address,  uint, bool){
        (bool success, bytes memory data) = addr.delegatecall(abi.encodeWithSignature("getAddressAndBalance(uint256,uint256)", 1, 2));
        // sender指向调用Caller的地址账户, _this指向Caller合约
        (address sender,  address _this,  uint _balance) = abi.decode(data, (address, address, uint));
        bool same = sender == msg.sender; // true, 在delegatecall的时候, msg.sender与调用者中的msg.sender相同
       return ( sender,   _this,   _balance, same);
    }
    function staticcallTest(address addr) public  returns (address,  address,  uint){
        (bool success, bytes memory data) = addr.staticcall(abi.encodeWithSignature("getAddressAndBalance(uint256,uint256)", 1, 2));
        // 与call相同
        (address sender,  address _this,  uint _balance) = abi.decode(data, (address, address, uint));
       return ( sender,   _this,   _balance);
    }

}

contract DataPool {
    function getAddressAndBalance(uint256 args1, uint256 args2) public view returns (address, address, uint) {
        return (msg.sender,  address(this), address(this).balance);
    }
}
