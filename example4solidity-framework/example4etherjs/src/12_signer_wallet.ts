/**
 * signer是以太坊对于账户的抽象, 可以用来签名消息, 发送交易, 交互合约
 * signer只是一个抽象类, 必须使用他的子类: wallet, VoidSigner(一个只读的signer, 只能进行只读操作, 不能进行交互, 发送交易)
 *
 * 下面专门讲讲wallet的相关方法
 */

import { Wallet, providers, ethers, utils } from "ethers";

async function main() {
  const provider = new ethers.providers.JsonRpcProvider(
    "https://eth-sepolia.g.alchemy.com/v2/H8zDcVhsxxmYq7YomnXe3YQ4gGmOiUgU"
  );
  var wallet = Wallet.createRandom().connect(provider); // 随机创建钱包, 并连接网络

  const wallet1 = new ethers.Wallet(
    "5961e0624c7c4862efce438e3b069153519a8c4d56299631692b3b3eaf09d9eb",
    provider
  ); // 直接创建钱包, 并连接网络

  /**
   * signer相关方法
   */
  const address = await wallet.getAddress(); // 获取钱包地址

  /**
   * blockchain相关方法
   */
  await wallet.getBalance(); // 获取余额
  await wallet.getChainId(); // 获取链id
  await wallet.getGasPrice(); // 获取gas价格
  await wallet.getTransactionCount(); // 获取交易数量
  await wallet.call({ from: "", to: "", data: "" }); // 调用合约
  await wallet.estimateGas({ from: "", to: "", data: "" }); // 估算gas
  await wallet.populateTransaction({ from: "", to: "", data: "" }); // 估算gas, 并返回交易对象
  await wallet.sendTransaction({ from: "", to: "", data: "" }); // 发送交易
  await wallet.resolveName("0x123"); // 解析域名

  /**
   * 签名消息
   */
  const account = "0x5B38Da6a701c568545dCfcB03FcB875f56beddC4";
  const tokenId = "0";
  // 等效于Solidity中的keccak256(abi.encodePacked(account, tokenId))
  const msgHash = utils.solidityKeccak256(
    ["address", "uint256"],
    [account, tokenId]
  );
  const messageHashBytes = ethers.utils.arrayify(msgHash);
  wallet.signMessage(messageHashBytes); // 按照EIP-191签名, 签名自动加上"\x19Ethereum Signed Message:\n32"

  /**
   * 签名交易
   */
  wallet.signTransaction({ from: "", to: "", data: "" }); // 签名交易

  /**
   * 签名结构化数据
   */
  // 定义domainSeparator
  const domainSeparator = {
    name: "Ether Mail",
    version: "1",
    chainId: 1,
    verifyingContract: "0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC",
  };
  // 定义结构化数据的类型
  const types = {
    Person: [
      { name: "name", type: "string" },
      { name: "wallet", type: "address" },
    ],
    Mail: [
      { name: "from", type: "Person" },
      { name: "to", type: "Person" },
      { name: "contents", type: "string" },
    ],
  };
  // 待签名的结构化数据
  const value = {
    from: {
      name: "Cow",
      wallet: "0xCD2a3d9F938E13CD947Ec05AbC7FE734Df8DD826",
    },
    to: {
      name: "Bob",
      wallet: "0xbBbBBBBbbBBBbbbBbbBbbbbBBbBbbbbBbBbbBBbB",
    },
    contents: "Hello, Bob!",
  };
  await wallet._signTypedData(domainSeparator, types, value); // 签名TypedData, 按照EIP-712签名
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
