
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\wtf.dapp\_03_erc165\Example.sol
*/
            
////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0
pragma solidity ^0.8.17;

/**
 * ERC165, 声明他支持的接口, 供其他合约检查
 *
 * 接口的interfaceId等于所有函数的selector进行按位与(^), 可以通过type(Interface).interfaceId获得
 */
interface IERC165 {
    /**
     * 只有一个方法supportsInterface, 给定interfaceId, 返回他是否支持这个接口, 对于0xffffffff, 应该返回false
     * supportsInterface使用的gas应该小于30,000
     */
    function supportsInterface(bytes4 interfaceId) external view returns (bool);
}






/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\wtf.dapp\_03_erc165\Example.sol
*/
            
////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0
pragma solidity ^0.8.17;

////import "./IERC165.sol";

abstract contract ERC165 is IERC165 {
    function supportsInterface(bytes4 interfaceId) public view virtual override returns (bool) {
        return interfaceId == type(IERC165).interfaceId;
    }
}


/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\wtf.dapp\_03_erc165\Example.sol
*/


////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0
pragma solidity ^0.8.17;

////import "./IERC165.sol";
////import "./ERC165.sol";
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
