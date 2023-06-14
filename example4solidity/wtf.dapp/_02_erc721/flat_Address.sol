
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\wtf.dapp\_02_erc721\Address.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0
pragma solidity ^0.8.17;

library Address {
    // 利用extcodesize判断一个地址是否为合约地址
    function isContract(address account) internal view returns (bool) {
        uint size;
        assembly {
            size := extcodesize(account)
        }
        return size > 0;
    }
}

