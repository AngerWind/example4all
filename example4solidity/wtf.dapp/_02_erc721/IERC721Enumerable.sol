// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

interface ERC721Enumerable {
    // 返回总的供应量, 他们的持有者地址不能是0x00
    function totalSupply() external view returns (uint256);

    /// @dev Throws if `_index` >= `totalSupply()`.
    // 根据index返回nft id
    function tokenByIndex(uint256 _index) external view returns (uint256);

    // 返回owner持有的第index个nft
    /// @dev Throws if `_index` >= `balanceOf(_owner)` or if
    ///  `_owner` is the zero address, representing invalid NFTs.
    /// @param _index 必须小于`balanceOf(_owner)`
    function tokenOfOwnerByIndex(
        address _owner,
        uint256 _index
    ) external view returns (uint256);
}
