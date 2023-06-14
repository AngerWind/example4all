// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

// https://blog.csdn.net/watson2017/article/details/123131401

import "@openzeppelin/contracts/utils/cryptography/ECDSA.sol";
import "@openzeppelin/contracts/utils/cryptography/EIP712.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract Demo is EIP712, Ownable {
    // 签名人
    address private _signer;

    constructor(string memory name, string memory version) EIP712(name, version) {}

    // 设置签名人
    function setSigner(address signer) public onlyOwner {
        _signer = signer;
    }

    // 获取签名（V4）
    function recoverV4(address from, address to, uint256 value, bytes memory signature) public view returns (address) {
        bytes32 digest = _hashTypedDataV4(
            keccak256(abi.encode(keccak256("Mail(address from,address to,uint256 value)"), from, to, value))
        );
        return ECDSA.recover(digest, signature);
    }

    // 验证签名
    function verify(address from, address to, uint256 value, bytes memory signature) public view returns (bool) {
        address signer = recoverV4(from, to, value, signature);
        return signer == _signer;
    }
}
