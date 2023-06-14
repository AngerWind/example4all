require("@nomicfoundation/hardhat-toolbox");
require("hardhat-deploy");
require("@nomiclabs/hardhat-ethers");
require("dotenv").config();

module.exports = {
  solidity: {
    compilers: [
      {
        version: "0.8.8",
      },
      {
        version: "0.6.6",
      },
    ],
  },
  defaultNetwork: "hardhat", // hardhat自带一个虚拟的区块链网络, 名为hardhat, 也可以通过该属性来自定义
  networks: {
    sepolia: {
      // 新添加一个网络
      url: process.env.SEPOLIA_RPC_URL, // 指定网络的url
      accounts: [process.env.SEPOLIA_PRIVATE_KEY], // 指定网络的账户私钥
      chainId: 11155111, // 指定chainId
      blockConfirmations: 6, // 指定等待的区块
    },
    localhost: {
      // accounts: [] // 不用再填写账户私钥, hardhat自动知道账户私钥
      url: "http://127.0.0.1:8545/",
      chainId: 31337, // 默认使用这个id
    },
  },
  // 这个属性是hardhat-deploy扩展的属性
  namedAccounts: {
    deployer: {
      // 定义一个账户叫deployer, 该账户在不同网络中会使用不同的私钥
      default: 0, // 默认情况下使用networks中的accounts的索引位置0的账户
      sepolia: 0, // 在sepolia网络下, 使用accounts的索引位置0的账户, 即process.env.SEPOLIA_PRIVATE_KEY, key必须在networks中配置过才行
      localhost: 1, // 在localhost网络下, 使用accounts的索引位置1的账户
    },
    deployer2: {
      default: 0,
      sepolia: 0,
      localhost: 1,
    },
  },
};
