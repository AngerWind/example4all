// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

/**
 * ERC165, 声明他支持的接口, 供其他合约检查
 *
 * 接口的interfaceId等于所有函数的selector进行按位与(^), 可以通过type(Interface).interfaceId获得
 */
interface IERC165 {
    /**
     * 只有一个方法supportsInterface, 给定interfaceId, 返回他是否支持这个接口, 对于0xffffffff, 应该返回false
     * supportsInterface使用的gas应该小于30,000
     */
    function supportsInterface(bytes4 interfaceId) external view returns (bool);
}


