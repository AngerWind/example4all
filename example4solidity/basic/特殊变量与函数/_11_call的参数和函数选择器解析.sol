// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

contract Test {
    /**
     * 函数选择器的编码:
     */
    function getSelector() public {
        bytes4 selector = bytes4(keccak256(abi.encodePacked("setX", "(uint)")));
    }

    function test(address addr) public {
        addr.call(abi.encodePacked(bytes4(keccak256(abi.encodePacked("setX", "(uint)"))), abi.encode(10)));
    }
}
