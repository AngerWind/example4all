import { Transaction } from "web3-core/types";
import Web3 from "web3";

async function main() {
  const web3 = new Web3("http://localhost:8545");
  /**
   * web3.eth.personal可以让你与当前连接的节点所管理的账户进行交互
   * 比如管理账户、处理身份验证和执行与账户相关的操作
   *
   * 没什么用感觉
   *
   * https://learnblockchain.cn/docs/web3.js/web3-eth-personal.html
   */
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
