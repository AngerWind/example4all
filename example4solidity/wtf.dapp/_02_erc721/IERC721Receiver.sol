// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

/**
 * 如果一个合约没有实现ERC721的相关函数，转入的NFT就进了黑洞，永远转不出来了。
 * 为了防止误转账，ERC721实现了safeTransferFrom()安全转账函数，
 * 目标合约必须实现了IERC721Receiver接口才能接收ERC721代币，不然会revert。
 * IERC721Receiver接口只包含一个onERC721Received()函数。
 */
interface IERC721Receiver {
    /// @dev The ERC721 smart contract calls this function on the recipient
    ///  after a `transfer`. This function MAY throw to revert and reject the
    ///  transfer. Return of other than the magic value MUST result in the
    ///  transaction being reverted.
    ///  Note: the contract address is always the message sender.
    /// @return `bytes4(keccak256("onERC721Received(address,address,uint256,bytes)"))`
    ///  unless throwing
    function onERC721Received(
        address operator,
        address from,
        uint tokenId,
        bytes calldata data
    ) external returns (bytes4);
}
