import { Transaction } from "web3-core/types";
import Web3 from "web3";

async function main() {
  const web3 = new Web3("http://localhost:8545");
  /**
   *
   */

  // 返回当前网络中的ENS注册器
  const registry = await web3.eth.ens.registry;
  // 获取解析器的合约对象
  const contract = registry.contract;
  // 获取注册表的地址
  const address = contract?.options.address;

  // 获取指定域名的解析器的合约对象
  const resolver = await web3.eth.ens.getResolver("domain.eth");
  // 获取解析器地址
  const resolverAddress = resolver?.options.address;

  // 将ens域名解析为地址
  const address1 = await web3.eth.ens.getAddress("domain.eth");
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
