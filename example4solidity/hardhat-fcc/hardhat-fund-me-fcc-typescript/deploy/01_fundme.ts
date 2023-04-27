import { HardhatRuntimeEnvironment } from 'hardhat/types';
import { DeployFunction } from 'hardhat-deploy/types';
import * as config from '../helper-hardhat-config';
import { network } from 'hardhat';

const func: DeployFunction = async function (hre: HardhatRuntimeEnvironment) {
  const { deployments, getNamedAccounts } = hre; // 这两个属性由hardhat-deploy提供
  const { deploy } = deployments;
  const chainId = hre.network.config.chainId as number;
  let priceFeedAddress;
  if (config.developmentChains.includes(hre.network.name)) {
    priceFeedAddress = (await deployments.get('MockV3Aggregator')).address;
  } else {
    priceFeedAddress = config.networkConfig[chainId].ethUsdPriceFeed;
  }
  const { deployer } = await getNamedAccounts(); // 获取在hardhat.config.ts中配置的namedAccounts
  await deploy('FundMe', {
    // 部署后这个合约的实例名, 之后就可以通过实例名获取已经不是的合约
    contract: 'FundMe', // 需要部署的合约
    from: deployer, // 执行部署所使用的的账户
    args: [priceFeedAddress], // 构造函数参数
    log: true, // 打印部署后的合约地址和消耗的gas
    // waitConfirmations: 6,
  });

  if (!config.developmentChains.includes(hre.network.name)) {
    await hre.run('verify:verify', {
      address: (await deployments.get('FundMe')).address,
      constructorArguments: [priceFeedAddress],
    });
  }
};
export default func;
func.tags = ['fundme']; // 定义该部署脚本的tag
