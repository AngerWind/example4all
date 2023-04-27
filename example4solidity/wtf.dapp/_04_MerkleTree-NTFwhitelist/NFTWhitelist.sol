// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

import "./MerkleTree.sol";
import "../_02_erc721/ERC721.sol";

contract NFTWhitelist is MerkleTree, ERC721 {
    constructor(
        string memory name,
        string memory symbol
    ) ERC721(name, symbol) {}

    function mint(address account, uint256, bytes32[] calldata proof) public {}
}
