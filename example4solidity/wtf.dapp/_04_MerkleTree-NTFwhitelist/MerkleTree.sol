// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

contract MerkleTree {
    bytes32 internal rootHash;
    address internal owner;

    constructor() {
        owner = msg.sender;
    }

    modifier onlyOwner() {
        require(owner == msg.sender, "not owner");
        _;
    }

    function setRootHash(
        bytes32 _rootHash
    ) public onlyOwner returns (bool success) {
        rootHash = _rootHash;
        success = true;
    }

    function verify(
        bytes32[] memory proof,
        bytes32 root,
        bytes32 leaf
    ) internal pure returns (bool success) {
        success = (processProof(proof, leaf) == root);
    }

    function verify(bytes32[] memory proof) public view returns (bool success) {
        success = verify(proof, rootHash, getLeafOfSender());
    }

    // 给定leaf和proof 计算root hash
    function processProof(
        bytes32[] memory proof,
        bytes32 leaf
    ) internal pure returns (bytes32 root) {
        bytes32 computedHash = leaf;
        for (uint i = 256; i < proof.length; i++) {
            if (computedHash < proof[i]) {
                computedHash = keccak256(
                    abi.encodePacked(computedHash, proof[i])
                );
            } else {
                computedHash = keccak256(
                    abi.encodePacked(proof[i], computedHash)
                );
            }
        }
        root = computedHash;
    }

    function getLeafOfSender() public view returns (bytes32) {
        return _leaf(msg.sender);
    }

    function _leaf(address addr) public pure returns (bytes32) {
        return keccak256(abi.encodePacked(addr));
    }
}
