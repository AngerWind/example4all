// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.7;


import "@openzeppelin/contracts/token/ERC20/ERC20.sol";

contract MyToken is ERC20 {
    constructor() ERC20("MyToken", "MCO") {
        _mint(msg.sender, 1000000000000000000000000);
    }
}