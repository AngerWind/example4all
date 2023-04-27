// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

interface IERC777Sender {
    /**
     * ERC777中, sender的回调函数
     */
    function tokensToSend(
        address operator,
        address from,
        address to,
        uint amount,
        bytes calldata userData,
        bytes calldata operatorData
    ) external;
}
