// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

import "../_03_erc165/IERC165.sol";

/**
 * erc721, 它利用tokenId来表示特定的非同质化代币，授权或转账都要明确tokenId；
 * 而ERC20只需要明确转账的数额即可。
 *
 * erc721的接口说明: https://eips.ethereum.org/EIPS/eip-721
 */
interface IERC721 is IERC165 {
    // 转账时触发, 即使from == 0, to == 0
    // 在创建该合约时进行nft的transfer可以不用触发
    // 进行nft转移的时候, 任何该nft的授权地址都应该reset为0x0
    event Transfer(address indexed _from, address indexed _to, uint256 indexed _tokenId);

    // 授权时触发, 如果授权给0x0, 说明没有授权给任何人
    event Approval(address indexed _owner, address indexed _approved, uint256 indexed _tokenId);
    // 批量授权时触发, 表示将owner的所有nft授权/取消授权给operator
    event ApprovalForAll(address indexed _owner, address indexed _operator, bool _approved);

    // 查询owner的nft持有量
    // 对于零地址应该抛出异常
    function balanceOf(address owner) external view returns (uint256 balance);

    // 查询nft的持有者
    // 如果nft的持有者是零地址, 说明该nft不存在, 那么应该抛出异常
    function ownerOf(uint256 tokenId) external view returns (address owner);

    // 安全转账
    // 除非msg.sender是owner, 授权的operator, 授权的approved地址, 报错
    // 如果from不是owner, 报错
    // 如果to是零地址, 报错
    // 如果tokenId非法, 报错
    // 当转账完成的时候, 应该检测to是否为合约地址, 如果是, 应该调用他的onERC721Received方法
    // 如果返回值不是IERC721Receiver.onERC721Received.selector, 应该报错
    // data是传递给onERC721Received方法的
    function safeTransferFrom(address _from, address _to, uint256 _tokenId, bytes memory data) external payable;

    // 与重载参数相同
    function safeTransferFrom(address _from, address _to, uint256 _tokenId) external payable;

    // 从from处转移nft到to
    // 除非msg.sender是授权的operator或者授权的approved地址或者持有者, 否则报错
    // 如果token不存在, 报错
    // 通过_to是零地址, 报错
    // 如果from不是nft的持有者, 报错
    // 调用方负责_to能够接受nft, 否则nft可能永远无法取回
    function transferFrom(address _from, address _to, uint256 _tokenId) external payable;

    // 调用者授权/更改授权给_approved某个nft
    // 授权给零地址 因为在没有授权给任何人
    // 一个nft只能有一个_approved地址
    // 除非msg.sender是nft持有者, 或者持有者授权all 给 msg.sender, 否则应该抛出异常
    function approve(address _approved, uint256 _tokenId) external payable;

    // 调用者将他的所有ntf授权/取消授权给operator
    // 一个地址可以设置多个operator
    // 应该触发ApprovalForAll事件
    function setApprovalForAll(address _operator, bool _approved) external;

    // 查看nft被授权给了谁
    // 如果token不存在应该报错
    function getApproved(uint256 tokenId) external view returns (address operator);

    // 查看owner是否将nft全部授权给了operator
    function isApprovedForAll(address owner, address operator) external view returns (bool);
}
