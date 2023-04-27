const { network } = require("hardhat");
const { developmentChains } = require("../helper-hardhat-config.js");
module.exports = async ({ getNamedAccounts, deployments }) => {
  const { deploy, log } = deployments;
  const { deployer } = await getNamedAccounts();
  const chainId = network.config.chainId;
  // 如果当前网络是测试网络, 那么就部署
  if (developmentChains.includes(network.name)) {
    log("Local network detected! Deploying mocks...");
    await deploy("Mock", {
      // 指定部署后合约实例的名字, 可以通过deployments.get()来获得
      contract: "MockV3Aggregator", // 指定需要部署的合约名, 如果不指定的话, 那么部署的合约与第一个参数重名
      from: deployer,
      log: true,
      args: ["8", "200000000000"],
    });
    log("Mocks Deployed!");
  }
};
// 定义当前脚本的tag, 在之后的npx hardhat deploy时, 可以通过--tags指定需要运行的js脚本
// npx hardhat deploy --tags mocks 执行当前脚本
// 不指定--tags的时候会执行包含all 的脚本
module.exports.tags = ["all", "mocks"];
