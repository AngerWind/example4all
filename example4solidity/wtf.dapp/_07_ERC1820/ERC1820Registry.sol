// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

// https://github.com/0xjac/ERC1820/blob/master/contracts/ERC1820Registry.sol

import "./IERC1820Implementer.sol";

contract ERC1820Registry {
    /// @notice ERC165 Invalid ID.
    bytes4 internal constant INVALID_ID = 0xffffffff;
    /// @notice Method ID for the ERC165 supportsInterface method (= `bytes4(keccak256('supportsInterface(bytes4)'))`).
    bytes4 internal constant ERC165ID = 0x01ffc9a7;
    /// @notice Magic value which is returned if a contract implements an interface on behalf of some other address.
    bytes32 internal constant ERC1820_ACCEPT_MAGIC = keccak256(abi.encodePacked("ERC1820_ACCEPT_MAGIC"));

    // 正常情况下: 记录address => keccak256(interfaceName) => implementer
    // 但是也可以通过updateERC165Cachesd方法, 记录contract => interfaceId => contract(某个合约是否实现了interfaceId,
    // 如果实现了implementer是他自己, 如果没有还实现, implementer是0)
    mapping(address => mapping(bytes32 => address)) internal interfaces;
    mapping(address => address) internal managers; // 记录address的manager
    // contract => interfaceId => contract是否记录在了interfaces中
    mapping(address => mapping(bytes4 => bool)) internal erc165Cached;

    // addr设置implementer的事件
    event InterfaceImplementerSet(address indexed addr, bytes32 indexed interfaceHash, address indexed implementer);
    // addr设置/修改manager的事件
    event ManagerChanged(address indexed addr, address indexed newManager);

    function getInterfaceImplementer(address _addr, bytes32 _interfaceHash) external view returns (address) {
        address addr = (_addr == address(0) ? msg.sender : _addr); // 如果addr为0, 这使用msg.sender
        // 判断传入的interfaceHash是否是interfaceId
        if (isERC165Interface(_interfaceHash)) {
            bytes4 erc165InterfaceHash = bytes4(_interfaceHash);
            // 传入的是interfaceId
            return implementsERC165Interface(addr, erc165InterfaceHash) ? addr : address(0);
        }
        // 传入的是keccak256(interfaceName), 直接返回
        return interfaces[addr][_interfaceHash];
    }

    // 该方法向interfaces中添加address => keccak256(interfaceName) => implementer的记录
    // _interfaceHash不应该传入type(interface).id，而应该传入keccak256(interfaceName)
    function setInterfaceImplementer(address _addr, bytes32 _interfaceHash, address _implementer) external {
        address addr = _addr == address(0) ? msg.sender : _addr; // 如果address是0，则使用msg.sender
        require(getManager(addr) == msg.sender, "Not the manager");

        // hash应该是keccak256(interfaceName), 不能是type(interface).id
        require(!isERC165Interface(_interfaceHash), "Must not be an ERC165 hash");
        // 如果设置的implementer不是自身，那么必须实现IERC1820Implementer接口
        if (_implementer != address(0) && _implementer != msg.sender) {
            require(
                // 对implementer调用canImplementInterfaceForAddress函数，如果返回的是ERC1820_ACCEPT_MAGIC，则表示实现了接口
                IERC1820Implementer(_implementer).canImplementInterfaceForAddress(_interfaceHash, addr) ==
                    ERC1820_ACCEPT_MAGIC,
                "Does not implement the interface"
            );
        }
        interfaces[addr][_interfaceHash] = _implementer;
        emit InterfaceImplementerSet(addr, _interfaceHash, _implementer);
    }

    // 如果设置的manager是自身，则表示取消manager
    // 只有manager才能调用这个函数, 如果设置了manager, 那么自己就不能再调用这个函数了!!!!!!!!
    function setManager(address _addr, address _newManager) external {
        require(getManager(_addr) == msg.sender, "Not the manager");
        managers[_addr] = (_newManager == _addr ? address(0) : _newManager);
        emit ManagerChanged(_addr, _newManager);
    }

    // 如果指定了manager，则返回manager，否则返回自身
    function getManager(address _addr) public view returns (address) {
        // By default the manager of an address is the same address
        if (managers[_addr] == address(0)) {
            return _addr;
        } else {
            return managers[_addr];
        }
    }

    // 通过interface name获取interface hash
    function interfaceHash(string calldata _interfaceName) external pure returns (bytes32) {
        return keccak256(abi.encodePacked(_interfaceName));
    }

    // 向interfaces中添加contract => interfaceId => contract的记录
    // 并且通过erc165Cached记录contract => interfaceId => true
    function updateERC165Cache(address _contract, bytes4 _interfaceId) external {
        // 判断contruct是否实现了interfaceId
        interfaces[_contract][_interfaceId] = implementsERC165InterfaceNoCache(_contract, _interfaceId)
            ? _contract
            : address(0);
        erc165Cached[_contract][_interfaceId] = true;
    }

    // 判断contract是否实现了interfaceId
    function implementsERC165Interface(address _contract, bytes4 _interfaceId) public view returns (bool) {
        // 先判断是否已经记录在interfaces中, 如果没有, 直接调用contract的supportsInterface方法
        if (!erc165Cached[_contract][_interfaceId]) {
            return implementsERC165InterfaceNoCache(_contract, _interfaceId);
        }
        // 如果已经记录在interfaces中, 判断contract的implementer是否是contract自身, 还是address(0)
        return interfaces[_contract][_interfaceId] == _contract;
    }

    // 通过调用合约的supportsInterface方法来判断是否支持interfaceid
    function implementsERC165InterfaceNoCache(address _contract, bytes4 _interfaceId) public view returns (bool) {
        uint256 success;
        uint256 result;
        // staticcall 合约的supportsInterface方法, 并传入erc165的接口id
        // 即判断合约是否实现了erc165接口
        (success, result) = noThrowCall(_contract, ERC165ID);
        if (success == 0 || result == 0) {
            return false;
        }
        // staticcall 合约的supportsInterface方法, 并传入INVALID_ID
        // 他应该返回false, 如果返回true, 则说明合约实现了erc165接口, 但是没有按要求supportsInterface方法
        (success, result) = noThrowCall(_contract, INVALID_ID);
        if (success == 0 || result != 0) {
            return false;
        }
        // staticcall 合约的supportsInterface方法, 并传入_interfaceId
        (success, result) = noThrowCall(_contract, _interfaceId);
        if (success == 1 && result == 1) {
            return true;
        }
        return false;
    }

    // 判断传入的hash是否是支持erc165的接口(type(interface).id)(后面28个字节都是0)
    function isERC165Interface(bytes32 _interfaceHash) internal pure returns (bool) {
        return _interfaceHash & 0x00000000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF == 0;
    }

    /// @dev Make a call on a contract without throwing if the function does not exist.
    function noThrowCall(
        address _contract,
        bytes4 _interfaceId
    ) internal view returns (uint256 success, uint256 result) {
        bytes4 erc165ID = ERC165ID;

        assembly {
            let x := mload(0x40) // Find empty storage location using "free memory pointer"
            mstore(x, erc165ID) // Place signature at beginning of empty storage
            mstore(add(x, 0x04), _interfaceId) // Place first argument directly next to signature

            success := staticcall(
                30000, // 30k gas
                _contract, // To addr
                x, // Inputs are stored at location x
                0x24, // Inputs are 36 (4 + 32) bytes long
                x, // Store output over input (saves space)
                0x20 // Outputs are 32 bytes long
            )

            result := mload(x) // Load the result
        }
    }
}
