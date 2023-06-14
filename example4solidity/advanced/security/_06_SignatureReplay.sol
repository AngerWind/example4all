// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

// https://github.com/WTFAcademy/WTF-Solidity/blob/main/S06_SignatureReplay/readme.md

/**
 * 签名的原理:
 *  ECDSA(消息 + 私钥 + 随机数) => 签名
 *  ECDSA_反向算法(签名 + 消息) => 公钥
 *
 * 签名的作用常常用在通行证上, 即我们给指定的消息进行签名, 用户拿着这个签名去调用合约
 * 在合约中通过ECDSA反向算法计算出公钥的地址, 如果地址与signer的地址相同, 说明是我们给出的签名, 可以通过
 *
 * 但是签名中应该包含chainId和一个递增的数nonce这两个属性, 第一个属性是防止跨链攻击, 第二个属性是防止重放攻击
 * 即每次签名的nonce都应该递增, 然后将nonce与合约的nonce进行比较, 确认是否重放攻击
 * 验证通过后nonce要递增
 */
