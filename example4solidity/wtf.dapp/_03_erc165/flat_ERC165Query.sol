
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\wtf.dapp\_03_erc165\ERC165Query.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0
pragma solidity ^0.8.17;

/**
 * supportsInterface的selector为01ffc9a7
 * IERC165的interfaceID为01ffc9a7
 *
 * 判断一个合约是否支持erc165, 应该按照如下步骤:
 *      1. 对合约进行staticcall, 并且calldata为0x01ffc9a701ffc9a700000000000000000000000000000000000000000000000000000000
 *          (01ffc9a7是需要调用的函数selector, 就是supportsInterface的selector, 后面部分
 *          表示的是interfaceID的abi编码, 这个interfaceID就是IERC165的interfaceID的abi编码
 *          所以这里的staticcall的意思是调用supportsInterface, 并传入IERC165.interfaceId)
 *          如果调用失败, 或者返回false, 不支持erc165
 *      2. 对合约进行staticcall, 并且calldata为0x01ffc9a7ffffffff00000000000000000000000000000000000000000000000000000000
 *          如果调用失败, 或者返回true, 不支持erc165
 *      3. 否则支持erc165
 *
 * 判断一个合约是否支持某个接口
 *  1. 是否支持erc165, 如果支持, 直接调用supportsInterface
 *  2. 否则通过其他途径
 *
 * 规则详见：https://eips.ethereum.org/EIPS/eip-165
 */
contract ERC165Query {
    bytes4 constant InvalidID = 0xffffffff;
    bytes4 constant ERC165ID = 0x01ffc9a7;

    function doesContractImplementInterface(
        address _contract,
        bytes4 _interfaceId
    ) external view returns (bool) {
        uint256 success;
        uint256 result;

        (success, result) = noThrowCall(_contract, ERC165ID);
        if ((success == 0) || (result == 0)) {
            return false;
        }

        (success, result) = noThrowCall(_contract, InvalidID);
        if ((success == 0) || (result != 0)) {
            return false;
        }

        (success, result) = noThrowCall(_contract, _interfaceId);
        if ((success == 1) && (result == 1)) {
            return true;
        }
        return false;
    }

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
                0x24, // Inputs are 36 bytes long
                x, // Store output over input (saves space)
                0x20
            ) // Outputs are 32 bytes long

            result := mload(x) // Load the result
        }
    }
}

