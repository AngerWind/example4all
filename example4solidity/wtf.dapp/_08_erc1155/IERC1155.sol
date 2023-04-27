// SPDX-License-Identifier: MIT
// OpenZeppelin Contracts (last updated v4.7.0) (token/ERC1155/IERC1155.sol)

pragma solidity ^0.8.0;

import "../_03_erc165/IERC165.sol";

/**
 * eip1155, 允许一个合约中包含多种代币, 每种代币都有自己的id
 * 如果代币总量是1, 则可以认为是一个非同质化代币
 * 如果代币总量大于1, 则可以认为是一个同质化代币
 *
 * https://eips.ethereum.org/EIPS/eip-1155[EIP].
 */
interface IERC1155 is IERC165 {
    /**
     * 单币种转移事件
     */
    event TransferSingle(address indexed operator, address indexed from, address indexed to, uint256 id, uint256 value);

    /**
     * 多币种转移事件.
     */
    event TransferBatch(
        address indexed operator,
        address indexed from,
        address indexed to,
        uint256[] ids,
        uint256[] values
    );

    /**
     * 授权事件
     */
    event ApprovalForAll(address indexed account, address indexed operator, bool approved);

    /**
     * 当id代币的URI发生变化时触发, value为新的uri
     * @dev Emitted when the URI for token type `id` changes to `value`, if it is a non-programmatic URI.
     *
     * If an {URI} event was emitted for `id`, the standard
     * https://eips.ethereum.org/EIPS/eip-1155#metadata-extensions[guarantees] that `value` will equal the value
     * returned by {IERC1155MetadataURI-uri}.
     */
    event URI(string value, uint256 indexed id);

    /**
     * 获取account地址的代币id的余额
     * account 不能是0地址
     */
    function balanceOf(address account, uint256 id) external view returns (uint256);

    /**
     * 获取一系列代币的余额
     */
    function balanceOfBatch(
        address[] calldata accounts,
        uint256[] calldata ids
    ) external view returns (uint256[] memory);

    /**
     * 授权或者取消授权operator操作account的代币
     * 触发ApprovalForAll事件
     * operator不能是caller
     */
    function setApprovalForAll(address operator, bool approved) external;

    /**
     *account是否授权给了operator操作代币
     */
    function isApprovedForAll(address account, address operator) external view returns (bool);

    /**
     * 从from转移id类型的amount个代币到to
     * 触发TransferSingle事件
     *
     * to不能是0地址
     * 如果caller不是from, 则必须通过setApprovalForAll授权
     * from必须有足够的代币
     * 如果to是一个合约, 则必须实现IERC1155Receiver接口并且返回正确的magic value
     */
    function safeTransferFrom(address from, address to, uint256 id, uint256 amount, bytes calldata data) external;

    /**
     * safeTransferFrom的批量操作
     * 触发TransferBatch事件
     *
     * ids和amounts的长度必须相等
     * 如果to是一个合约, 则必须实现IERC1155Receiver接口并且返回正确的magic value
     */
    function safeBatchTransferFrom(
        address from,
        address to,
        uint256[] calldata ids,
        uint256[] calldata amounts,
        bytes calldata data
    ) external;
}
