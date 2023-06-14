
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\basic\特殊变量与函数\_20_new创建合约.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0

pragma solidity ^0.8.17;

contract Factory {
    address[] public storageAddress;

    function create() public returns (uint idx) {
        SimpleStorage sto = new SimpleStorage(); // 通过new创建一个新的合约
        storageAddress.push(address(sto));
        idx = storageAddress.length - 1;
    }

    function set(uint idx, uint number) public {
        require(idx < storageAddress.length, "Invalid address index");
        SimpleStorage sto = SimpleStorage(storageAddress[idx]); // 通过地址创建已有的合约
        sto.set(number);
    }
}

contract SimpleStorage {
    uint public number;

    constructor() payable {}

    function set(uint _number) public {
        number = _number;
    }
}

