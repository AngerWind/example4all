// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;


contract HelloWorld {
    string name = "zhangsan";

    function getName() public view returns(string memory) {
        return name;
    }
}