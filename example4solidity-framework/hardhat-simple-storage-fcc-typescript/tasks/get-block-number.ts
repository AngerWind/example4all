import { task } from "hardhat/config";

// 命令的名称与描述
export default task("block-number", "Prints the current block number").setAction(
  // 命令传入的参数, hardhat runtime environment
  async (taskArgs, hre) => {
    const blockNumber = await hre.ethers.provider.getBlockNumber();
    console.log(`Current block number: ${blockNumber}`);
  }
);
