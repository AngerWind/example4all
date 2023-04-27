// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

interface IERC777Recipient {
    /**
     * ERC777中, recipient的回调函数
     */
    function tokensReceived(
        address operator,
        address from,
        address to,
        uint amount,
        bytes calldata userData,
        bytes calldata operatorData
    ) external;
}
