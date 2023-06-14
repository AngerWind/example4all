
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\example\FundMeFcc\explainerContracts\SafeMathTesterV8.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING: MIT
pragma solidity ^0.8.0;

contract SafeMathTester{
    uint8 public bigNumber = 255; // checked

    function add() public {
        unchecked {bigNumber = bigNumber + 1;}
    }
}
