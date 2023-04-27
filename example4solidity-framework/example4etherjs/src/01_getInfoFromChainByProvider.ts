import { AbstractProvider, ethers, Network } from "ethers";

async function main() {
  await providerInfo();
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });

async function providerInfo() {
  // provider 提供对区块链及其状态的只读访问, 不需要接触私钥
  // 公网rpc接口速度有限, 可以从alchemy网站创建个人的rpc接口
  const provider: AbstractProvider = new ethers.JsonRpcProvider(
    "https://eth.llamarpc.com"
  );
  // ethers原生支持ens域名
  const balanceWei = await provider.getBalance("vitalik.eth");
  new ethers.JsonRpcProvider("https://eth.llamarpc.com");
  // 从链上获取的余额单位都是wei, 转换为eth
  const balanceETH = ethers.formatEther(balanceWei);
  console.log(`eth balance of vitalik: ${balanceETH}`);

  // 获取当前网络信息, homestead表示主网
  const network: Network = await provider.getNetwork();
  console.log(`id: ${network.chainId}, name: ${network.name}`);

  // 获取区块高度
  const blockNumber = await provider.getBlockNumber();
  console.log(blockNumber);

  // 获取当前建议的gas设置
  const feeData = await provider.getFeeData();
  console.log(feeData);

  // 查询区块信息
  const block = await provider.getBlock(0);

  // 根据地址获取合约的bytecode
  const byteCode = await provider.getCode(
    "0xc778417e063141139fce010982780140aa0cd5ab"
  );
  console.log(byteCode);

  // 根据hash获取交易
  const transaction = await provider.getTransaction(
    "0x71d5c1ad1e23339ed4521160eab0baa378f3c27ab556604ab90ea86add709214"
  );
  console.log(transaction);
}
