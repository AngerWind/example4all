// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

import "./IERC1155.sol";

/**
 * @dev Interface of the optional ERC1155MetadataExtension interface, as defined
 * in the https://eips.ethereum.org/EIPS/eip-1155#metadata-extensions[EIP].
 *
 * _Available since v3.1._
 */
interface IERC1155MetadataURI is IERC1155 {
    /**
     * 返回代币id的URI
     */
    function uri(uint256 id) external view returns (string memory);
}
