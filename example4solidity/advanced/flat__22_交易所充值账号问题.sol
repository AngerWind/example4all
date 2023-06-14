
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\advanced\_22_交易所充值账号问题.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0

pragma solidity ^0.8.17;

/**

CREATE2 是以太坊在2019年2月28号的君士坦丁堡（Constantinople）硬分叉中引入 的一个新操作码。
根据EIP1014 CREATE2操作码引入，主要是用于状态通道，然而，我们也可以用于解决其他问题。

例如，交易所需要为每个用户提供一个以太坊地址，以便用户可以向其充值。我们称这些地址为“充值地址”。
当代币进入充值地址时，我们需要将其汇总到一个钱包（热钱包）。
下面我们分析一下在没有CREATE2操作码时，如何解决上述问题， 以及为什么这些方案不适用。
如果你只对最终结果感兴趣，可以直接跳到最后一节：最终方案。

淘汰方案：直接使用以太坊地址
    最简单的解决方案是为新用户生成以太坊账号地址作为用户充值地址。
    需要时则在后台用充值地址的私钥签名调用transfer()把用户钱包归集到交易所热钱包。
    此方法具有以下优点： 很简单
    将代币从用户充值地址转到热钱包的费用与调用transfer()的费用一样
    然而，我们决定放弃这个方案，因为它有一个重大的缺陷：总是需要在一些地方保存私钥，这不仅仅是私钥可能丢失的问题，
    还需要仔细管理私钥的访问权限。如果其中一个私钥被盗，那么这个用户的代币就无法归集到热钱包。

淘汰方案：为用户创建独立的智能合约
    每个用户创建一个单独的智能合约并用合约地址作为用户的充值地址，这避免了在服务器上保存地址的私钥， 交易通过调用智能合约进行代币归集。
    不过我们依旧没有选择这个方案，因为在部署合约之前用户没有办法显示充值地址。
    在交易所中，用户应该可以创建任意多的账号，这意味着需要在合约部署上浪费资金，并且还不能确认用户是否会使用这个账号。

改进：使用CREATE2 操作码预计算合约地址
    为了解决上一节没有办法显示充值地址的问题，我们决定使用 CREATE2 操作码，它允许我们提前计算出要部署的合约地址，地址计算公式如下：
        keccak256(0xff,sender,salt,keccak256(create_code))[12:]
    因此，可以保证提供给用户的合约地址中包含了期望的合约字节码。此外，合约可以在需要的时候才部署。例如，当用户决定使用钱包时。
    更进一步，可以随时计算出合约的地址而无需保存地址，因为公式中的：
    address：是个常量，它是部署钱包的工厂合约地址
    salt：使用user_id的哈希
    create_code：也是个常量，因为总是部署相同合约

继续改进

    上面的解决方案仍然有一个缺陷：交易所需要付费部署智能合约。但是，这是可以避免的。
    可以在合约构造函数中调用transfer（）函数，然后调用selfdestruct（）。这将退还部署智能合约部分的gas。
    与常见错误认识相反，其实你可以使用CREATE2操作码在同一地址多次部署智能合约。这是因为CREATE2检查目标地址的 nonce 是否为零
    （它会在构造函数的开头将其设置为1）。 在这种情况下，selfdestruct（）函数每次都会重置地址的 nonce。
    因此，如果再次使用相同的参数调用CREATE2创建合约， 对nonce的检查是可以通过的。

    这个解决方案类似于使用以太坊地址的方案，但是无需存储私钥。因为我们不支付智能合约部署费用，所以将钱从充值地址到热钱包的成本大约等于调用transfer（）函数的成本。
    下面是代码示例, 但不是生成代码

 */
contract Wallet {
    address internal hotWallet = 0xdCad3a6d3569DF655070DEd06cb7A1b2Ccd1D3AF; // 用于归集的热钱包

    constructor() {
        // 发送所有eth到热钱包
        payable(hotWallet).send(address(this).balance);
        // selfdestruct to receive gas refund and reset nonce to 0
        selfdestruct(payable(address(0x0)));
    }
}

contract Fabric {
    function createContract(uint256 salt) public {
        // 获取钱包的bytecode
        bytes memory bytecode = type(Wallet).creationCode;
        assembly {
            let codeSize := mload(bytecode) // get size of init_bytecode
            let newAddr := create2(
                0, // 0 wei
                add(bytecode, 32), // the bytecode itself starts at the second slot. The first slot contains array length
                codeSize, // size of init_code
                salt // salt from function arguments
            )
        }
    }
}

