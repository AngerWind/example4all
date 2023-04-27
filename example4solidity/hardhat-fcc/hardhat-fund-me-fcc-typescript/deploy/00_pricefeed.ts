import {HardhatRuntimeEnvironment} from 'hardhat/types'; 
import { DeployFunction } from 'hardhat-deploy/types'; 
import * as config from "../helper-hardhat-config";

const func: DeployFunction = async function (hre: HardhatRuntimeEnvironment) { 
  const {deployments, getNamedAccounts} = hre; // 这两个属性由hardhat-deploy提供
  const {deploy} = deployments; 

  const {deployer} = await getNamedAccounts(); // 获取在hardhat.config.ts中配置的namedAccounts
  await deploy('MockV3Aggregator', {  // 部署后这个合约的实例名, 之后就可以通过实例名获取已经不是的合约
      contract: "MockV3Aggregator", // 需要部署的合约
    from: deployer, // 执行部署所使用的的账户
    args: [config.DECIMALS, config.INITIAL_ANSWER], // 构造函数参数
    log: true, // 打印部署后的合约地址和消耗的gas
  });
};
export default func;
func.tags = ['pricefeed']; // 定义该部署脚本的tag