// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
 * 当我们调用智能合约时，本质上是向目标合约发送了一段calldata，在remix中发送一次交易后，
 * 可以在详细信息中看见input即为此次交易的calldata
 * 发送的calldata中前4个字节是selector（函数选择器）
 * msg.data是solidity中的一个全局变量，值为完整的calldata
 */
contract Tester {
    // event 返回msg.data
    event Log(bytes data);

    function mint(address to) external {
        // 当参数为0x2c44b726ADF1963cA47Af88B284C06f30380fC78时，输出的calldata为
        // 0x6a6278420000000000000000000000002c44b726adf1963ca47af88b284c06f30380fc78
        // 其中前四字节为函数选择器, 后面的为address的abi编码
        emit Log(msg.data);
    }

    // 获取mint函数的函数选择器
    function mintSelector() external pure returns (bytes4 mSelector) {
        return bytes4(keccak256("mint(address)"));
    }

    // 通过函数选择器调用函数
    function callWithSelector() external returns (bool, bytes memory) {
        (bool success, bytes memory data) = address(this).call(
            abi.encodeWithSelector(
                0x6a627842,
                "0x2c44b726ADF1963cA47Af88B284C06f30380fC78"
            )
        );
        return (success, data);
    }
}
