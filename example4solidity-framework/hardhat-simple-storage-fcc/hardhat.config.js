require("@nomiclabs/hardhat-waffle");

// You need to export an object to set up your config
// Go to https://hardhat.org/config/ to learn more

/**
 * @type import('hardhat/config').HardhatUserConfig
 */
require("dotenv").config();
require("@nomiclabs/hardhat-etherscan"); // 启动插件hardhat-etherscan

require("./tasks/get-block-number"); // 添加自定义任务
require("./tasks/buildin-accounts"); // 添加自定义任务

require("hardhat-gas-reporter"); // gas消耗报告
require("solidity-coverage"); // 覆盖率测试报告
module.exports = {
  solidity: "0.8.7", // 指定编译时使用的solidity编译器版本, 需要与sol文件一致
  defaultNetwork: "hardhat", // hardhat内部自带一个虚拟的区块链网络, 名为hardhat, 该选项设置默认的网络
  networks: {
    sepolia: {
      // 新添加一个网络
      url: process.env.SEPOLIA_RPC_URL, // 指定网络的url
      accounts: [process.env.SEPOLIA_PRIVATE_KEY], // 指定网络的账户私钥
      chainId: 11155111, // 指定chainId
    },
    localhost: {
      // accounts: [] // 不用再填写账户私钥, hardhat自动知道账户私钥
      url: "http://127.0.0.1:8545/",
      chainId: 31337, // 默认使用这个id, 也是hardhat网络的id
    },
  },
  etherscan: {
    apiKey: process.env.ETHERSCAN_API_KEY, // 自动验证发布源码所需要的的api token
  },
  gasReporter: {
    enabled: true, // 如果不需要启动的话, 可以为false

    // gas消耗记录输出到文件, 如果不需要输出可以不写下面这两个
    // outputFile: "gas-report.txt", // 指定将gas消耗的记录输出到文件
    // noColors: true, // 输出的文本不需要颜色, 因为颜色可能导致格式错了(???没测试)

    // 下面两个配置用来输出消耗gas对应的usd
    currency: "USD", // 指定输出gas消耗对应的usd
    coinmarketcap: "5da11934-d9f6-4192-8ce4-0752cd244af4", // 为了获取不同网络的gas price, 需要连接https://pro.coinmarketcap.com/, 所以需要去他上面注册账号, 获取api token

    // 下面这个配置用来输出gas消耗是在哪个网络上的, 默认为eth网络
    token: "MATIC", // 自动生成的gas消耗是部署到polygon链上的
  },
};
