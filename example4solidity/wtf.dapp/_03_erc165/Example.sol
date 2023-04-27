
// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

import "./IERC165.sol";
import "./ERC165.sol";
/**
 * 下面是一个简单的IERC165实现案例
 */
interface OtherInterface {
    function do1() external;
    function do2() external;
}

interface OtherInterface1 {
    function todo1() external;
    function todo2() external;
}

contract Homer is ERC165, OtherInterface, OtherInterface1 {
    function supportsInterface(
        bytes4 interfaceID
    ) public view override returns (bool) {
        return
            super.supportsInterface(interfaceID) || // 调用父合约的supportsInterface
            interfaceID == type(OtherInterface1).interfaceId || // 通过type获取id
            interfaceID == this.do1.selector ^ this.do2.selector; // 另外一种获取id的方式
    }

    function do1() external override( OtherInterface) {}
    function do2() external override( OtherInterface) {}
    function todo1() external override( OtherInterface1) {}
    function todo2() external override( OtherInterface1) {}
}