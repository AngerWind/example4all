// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    目前数组切片，仅可使用于 calldata 数组.

    数组切片是数组连续部分的视图，用法如：x[start:end] ， start 和 end 是 uint256 类型（或结果为 uint256 的表达式）。 
    x[start:end] 的第一个元素是 x[start] ， 最后一个元素是 x[end - 1] 。
    如果 start 比 end 大或者 end 比数组长度还大，将会抛出异常。
    start 和 end 都可以是可选的： start 默认是 0， 而 end 默认是数组长度。

    数组切片没有任何成员。 它们可以隐式转换为其“背后”类型的数组，并支持索引访问。 
    索引访问也是相对于切片的开始位置。 
    !!! 数组切片没有类型名称，这意味着没有变量可以将数组切片作为类型，它们仅存在于中间表达式中。!!!


 */
contract Proxy {
    /// 被当前合约管理的 客户端合约地址
    address client;

    constructor(address client_) { client = client_; }

    /// 在进行参数验证之后，转发到由client实现的 "setOwner(address)"
    function forward(bytes calldata payload) external {
        // 由于截断行为，与执行 bytes4(payload) 是相同的
        // bytes4 sig = bytes4(payload);
        bytes4 sig = bytes4(payload[:4]);

        if (sig == bytes4(keccak256("setOwner(address)"))) {
            address owner = abi.decode(payload[4:], (address));
            require(owner != address(0), "Address of owner cannot be zero.");
        }
        (bool status, ) = client.delegatecall(payload);
        require(status, "Forwarded call failed.");
    }
}
