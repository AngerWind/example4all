import { HardhatUserConfig } from "hardhat/config";
import "@nomicfoundation/hardhat-toolbox";
import "dotenv/config";
import "hardhat-deploy";
import "hardhat-deploy-ethers";

const config: HardhatUserConfig = {
  solidity: "0.8.18",
  defaultNetwork: "hardhat",
  networks: {
    sepolia: {
      // 新添加一个网络
      url: process.env.SEPOLIA_RPC_URL, // 指定网络的url
      accounts: [process.env.SEPOLIA_PRIVATE_KEY as string], // 指定网络的账户的私钥, 可以是多个
      chainId: 11155111, // 指定chainId
    },
    localhost: {
      // accounts: [] // 不用再填写账户私钥, hardhat自动知道账户私钥
      url: "http://127.0.0.1:8545/",
      chainId: 31337, // 默认使用这个id
    },
  },
  namedAccounts: {
    deployer: {
      default: 0, // 默认使用第一个账户
      1: "0x1234567890123456789012345678901234567890", // 主网
      4: "0x1234567890123456789012345678901234567890", // 测试网
      5: "0x1234567890123456789012345678901234567890", // goerli测试网
      42: "0x1234567890123456789012345678901234567890", // kovan测试网
      31337: "0x1234567890123456789012345678901234567890", // hardhat本地测试
      11155111: "0x1234567890123456789012345678901234567890", // sepolia测试网
    },
  },
};

export default config;
