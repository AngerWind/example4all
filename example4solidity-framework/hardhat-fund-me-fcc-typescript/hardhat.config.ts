import { HardhatUserConfig } from "hardhat/config";
import "@nomicfoundation/hardhat-toolbox";

import 'hardhat-deploy'; // hardhat deploy plugin
import 'hardhat-deploy-ethers';

import '@typechain/hardhat' // typechain plugin

import * as dotenv from "dotenv"; // 读取.env文件中的环境变量
dotenv.config();

import "@nomiclabs/hardhat-etherscan" // hardhat etherscan plugin

const config: HardhatUserConfig = {
  solidity: "0.8.8", // 指定编译时使用的solidity编译器版本, 需要与sol文件一致
  defaultNetwork: "hardhat", // hardhat内部自带一个虚拟的区块链网络, 名为hardhat, 该选项设置默认的网络
  paths: {
    sources: "./contracts", // 指定合约文件的位置
  },
  networks: {
    sepolia: {
      // 新添加一个网络
      url: process.env.SEPOLIA_RPC_URL as string, // 指定网络的url
      accounts: [process.env.SEPOLIA_PRIVATE_KEY as string], // 指定网络的账户私钥
      chainId: 11155111, // 指定chainId
      // blockConfirmations: 6, // 指定确认区块数
    },
    localhost: {
      // accounts: [] // 不用再填写账户私钥, hardhat自动知道账户私钥
      url: "http://127.0.0.1:8545/",
      chainId: 31337, // 默认使用这个id, 也是hardhat网络的id
    },
  },
  typechain: {
    outDir: "typechain-types", // 指定生成的ts类型文件的输出目录
    target: "ethers-v5" // 指定生成的ts类型文件的使用ethers的5.x版本, 可选的还有truffle-v5, web3-v1
  },
  namedAccounts: {
    deployer: 0
  },
  etherscan: {
    apiKey: process.env.ETHERSCAN_API_KEY,
  },
  
};

export default config;
