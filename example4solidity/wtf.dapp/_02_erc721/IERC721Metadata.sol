// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

import "./IERC721.sol";

/**
 * IERC721Metadata是ERC721的扩展接口, 实现了三个查询metadata元数据的常用函数
 */
interface IERC721Metadata {
    function name() external view returns (string memory); // 返回代币名称

    function symbol() external view returns (string memory); // 代币代号

    // tokenid 非法报错, 返回一个url, 指向一个json文件, json内容如下
    function tokenURI(uint256 tokenId) external view returns (string memory);
    /**
    {
    "title": "Asset Metadata",
    "type": "object",
    "properties": {
        "name": {
            "type": "string",
            "description": "Identifies the asset to which this NFT represents"
        },
        "description": {
            "type": "string",
            "description": "Describes the asset to which this NFT represents"
        },
        "image": {
            "type": "string",
            "description": "A URI pointing to a resource with mime type image/* representing the asset to which this NFT represents. Consider making any images at a width between 320 and 1080 pixels and aspect ratio between 1.91:1 and 4:5 inclusive."
        }
    }
}
     */
}
