import { AllEvents } from "../types/truffle-contracts/Migrations";
import { ConvertLibContract } from "../types/truffle-contracts/ConvertLib";

// artifacts这个变量是在通过typechain生成的类型声明文件中定义的, 可以直接使用
let Migrations = artifacts.require("Migrations");

// web3这个变量也是在通过typechain生成的类型声明文件中定义的, 可以直接使用
let Web3 = web3;

module.exports = async (
  deployer: Truffle.Deployer,
  network: string,
  accounts: string[]
) => {
  console.log("start deploy migrations...");
  deployer.deploy(Migrations);
};
