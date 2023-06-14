// 从hardhat中导入ethers, 这个ethers与ethersjs不同的区别在于, 这个ethers是hardhat自带的,
// 同时因为项目都按照hardhat的项目框架结构写的, 编译的
// 同时内部区块链网络也是hardhat自带的
// 所以在部署的时候, 不用指定网络结构, 也不用指定abi和binary的位置, 也不用指定私钥, 全部使用自带的就可以了
// 同时直接使用合约名就可以部署了
import {  network, run } from "hardhat";
import { ethers } from "hardhat" ;
async function main() {
  /**
   * 部署合约, 如果没有实现编译sol文件的话会自动编译
   */
  // hardhat能够自动检测合约的abi和binary
  const simpleStorageFactory = await ethers.getContractFactory("SimpleStorage");

  // 使用hardhat自带的"hardhat" 网络和账户私钥
  // 如果在运行脚本的时候, 通过--network指定了网络, 就使用对应网络的url和private key
  console.log("deploy contract...");
  const simpleStorage = await simpleStorageFactory.deploy();
  console.log(`address is ${simpleStorage.address}`);
  await simpleStorage.deployed(); // 等待确认

  /**
   * 根据网络判断是否对发布的合约进行验证
   */
  // 通过network可以检测出当前部署的网络, 如果是sepolia同时etherscan api token存在
  if (network.config.chainId === 11155111 && process.env.ETHERSCAN_API_KEY) {
    console.log("Waiting for block confirmations...");
    await simpleStorage.deployTransaction.wait(6);
    console.log("block confired");
    await verify(simpleStorage.address, []); // 提交源码进行验证
  }

  /**
   * 对合约进行交互
   */
  const currentValue = await simpleStorage.retrieve();
  console.log(`Current Value is: ${currentValue}`);

  // Update the current value
  const transactionResponse = await simpleStorage.store(7);
  await transactionResponse.wait(1);

  const updatedValue = await simpleStorage.retrieve();
  console.log(`Updated Value is: ${updatedValue}`);
}

// 传入合约地址, 合约的构造函数参数, hardhat可以通过地址知道对应的合约文件在哪里
const verify = async (contractAddress: string, args: any[]) => {
  console.log("Verifying contract...");
  try {
    // run这个变量可以用来执行hardhat的一些子任务
    // 等同于 npx hardhat cmd .....
    await run("verify:verify", {
      address: contractAddress,
      constructorArguments: args,
    });
  } catch (e: any) {
    if (e.message.toLowerCase().includes("already verified")) {
      console.log("Already Verified!");
    } else {
      console.log(e);
    }
  }
};
main();
