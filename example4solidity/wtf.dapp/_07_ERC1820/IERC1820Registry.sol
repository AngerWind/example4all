// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

/**
 * 在很多接口中都要求执行合约的相关地址要能够进行回调,  如erc777要求sender和recipient为合约的地址都要在erc1820上面进行注册
 * 当地址执行一定的操作的时候, 执行合约能够找到地址是否有实现了要求的接口的相关合约, 并且进行回调
 *
 * 执行合约是怎么找到地址是否有实现了要求的接口的相关合约, 并且进行回调的呢?
 * 实现的办法是: 在链上部署当前合约的一个实例, 该实例整个链唯一, 并且要求所有实现了要求的接口的合约都要在该实例上面进行注册
 * 该实例通过一个mapping(address => mapping(bytes32 => address))来保存什么地址实现了什么类型的接口, 并且实现的合约在那个地址
 *
 * 这样执行合约就可以通过查询该实例, 来找到地址是否有实现了要求的接口, 并且进行回调
 *
 * 该实例的地址是固定的, 0x1820a4B7618BdE71Dce8cdc73aAB6C95905faD24
 *
 * implementer可以由多个地址共享, 每个地址也可以注册多个接口的implementer
 * 用户地址必须将implementer设置为合约
 *
 * 该合约与erc165的区别就是: 用户地址也可以指定相关的合约进行回调, 而erc165只能是合约声明自己支持什么接口
 *
 * 只有address的manager可以设置address的implementer
 * 默认情况下address的manager是他自己
 * 如果address设置了manager, 那么他就不能再设置implementer了, 只有manager可以设置implementer
 */
interface IERC1820Registry {
    // 某个地址设置了某种接口的implementer
    event InterfaceImplementerSet(address indexed account, bytes32 indexed interfaceHash, address indexed implementer);

    event ManagerChanged(address indexed account, address indexed newManager);

    /**
     * 授权给某个manager, 该manager可以替自己设置implementer
     * manaeer可以是0x00, 代表取消授权
     * 每个地址只能有一个manager
     *
     * caller必须是account的manager
     *
     * 触发ManagerChanged事件
     *
     * 每个address都是自己的manager
     */
    function setManager(address account, address newManager) external;

    /**
     * 获取address的manager
     */
    function getManager(address account) external view returns (address);

    /**
     * 设置address的implementer合约实现了interfaceHash接口
     *
     * address如果为0x00, 那么address就是caller的地址
     * implementer如果为0x00, 那么就是删除address的implementer
     *
     * 触发InterfaceImplementerSet事件
     *
     * caller必须是account的manager
     * interfaceHash不能是IERC165的接口id
     * implementer必须实现IERC1820Implementer接口, 并且返回true, 除非implementer就是caller
     *
     */
    function setInterfaceImplementer(address account, bytes32 _interfaceHash, address implementer) external;

    /**
     * 获取address的实现了interfaceHash接口的implementer
     * 如果没有返回0x00
     *
     * @dev Returns the implementer of `interfaceHash` for `account`. If no such
     * implementer is registered, returns the zero address.
     *
     * If `interfaceHash` is an {IERC165} interface id (i.e. it ends with 28
     * zeroes), `account` will be queried for support of it.
     *
     * `account` being the zero address is an alias for the caller's address.
     */
    function getInterfaceImplementer(address account, bytes32 _interfaceHash) external view returns (address);

    /**
     * 根据interfaceName获取interfaceHash
     * https://eips.ethereum.org/EIPS/eip-1820#interface-name[section of the EIP].
     */
    function interfaceHash(string calldata interfaceName) external pure returns (bytes32);

    /**
     * @notice Updates the cache with whether the contract implements an ERC165 interface or not.
     * @param account Address of the contract for which to update the cache.
     * @param interfaceId ERC165 interface for which to update the cache.
     */
    function updateERC165Cache(address account, bytes4 interfaceId) external;

    /**
     * @notice Checks whether a contract implements an ERC165 interface or not.
     * If the result is not cached a direct lookup on the contract address is performed.
     * If the result is not cached or the cached value is out-of-date, the cache MUST be updated manually by calling
     * {updateERC165Cache} with the contract address.
     * @param account Address of the contract to check.
     * @param interfaceId ERC165 interface to check.
     * @return True if `account` implements `interfaceId`, false otherwise.
     */
    function implementsERC165Interface(address account, bytes4 interfaceId) external view returns (bool);

    /**
     * @notice Checks whether a contract implements an ERC165 interface or not without using or updating the cache.
     * @param account Address of the contract to check.
     * @param interfaceId ERC165 interface to check.
     * @return True if `account` implements `interfaceId`, false otherwise.
     */
    function implementsERC165InterfaceNoCache(address account, bytes4 interfaceId) external view returns (bool);
}
