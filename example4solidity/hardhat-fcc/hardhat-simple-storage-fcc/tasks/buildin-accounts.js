const { task } = require("hardhat/config");

// This is a sample Hardhat task. To learn how to create your own go to
// https://hardhat.org/guides/create-task.html


// 命令的名称与描述
task("accounts", "prints the list of accounts and balances").setAction(
  // 命令传入的参数, hardhat runtime environment
  async (taskArgs, hre) => {
    const signers = await hre.ethers.getSigners()
    signers.forEach(async (signer) => {
      const balance = await signer.getBalance()
      console.log(`Account: ${signer.address} Balance: ${balance.toString()}`)
    })
  }
);

module.exports = {};