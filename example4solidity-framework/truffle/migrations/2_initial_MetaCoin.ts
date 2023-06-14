import { AllEvents } from "../types/truffle-contracts/MetaCoin";

// artifacts这个变量是在通过typechain生成的类型声明文件中定义的, 可以直接使用
let MetaCoin = artifacts.require("MetaCoin");
let ConvertLib = artifacts.require("ConvertLib");

// web3这个变量也是在通过typechain生成的类型声明文件中定义的, 可以直接使用
let Web3 = web3;

// 通过合约的名字获取合约的抽象对象
// 导出的函数第一个对象必须是 deployer
// 第二个参数是网络相关
// 第三个是连接的节点所提供的账户, 类似于web3.js中的web3.eth.getAccounts()
module.exports = async (
  deployer: Truffle.Deployer,
  network: string,
  accounts: string[]
) => {
  console.log("network: ", network);
  console.log("start deploy...");

  deployer.deploy(ConvertLib);
  deployer.link(ConvertLib, MetaCoin); // 是在是搞不懂这个link是干啥的

  // 通过deployer部署合约
  // const constructorArgs: any[] = [];
  // const options = {
  //   from: accounts[0], // 部署合约的账户
  //   gas: 5000000,
  //   overwrite: false, // 如果之前部署过了该类型的合约, 就不会重复部署了, 对于某些只需要部署一遍的合约很有用
  // };
  deployer.deploy(MetaCoin);

  const instance = await MetaCoin.deployed(); // 获取部署后的合约实例

  // 通过new()方法部署合约
  const instance2 = await MetaCoin.new();
  // 通过at()方法获取指定地址的合约实例
  const instance3 = await MetaCoin.at(instance2.address);

  /**
   * 与合约交互
   *    可以直接通过合约实例调用合约的方法, 比如instance.myMethod()
   *    也可以通过myMethod.estimateGas()获取调用该方法所需要的gas
   *    也可以通过myMethod.call()调用合约的view,pure方法, 该方法不会发送交易, 也不会修改合约的状态
   *    也可以通过myMethod.sendTransaction()调用合约的非view,pure方法, 该方法会发送交易, 也会修改合约的状态
   *    也可以通过myMethod.request()来获取一个已经编码的交易对象, 之后可以通过web3.eth.sendTransaction()发送交易
   *
   */
  // 与合约进行交互, 多个参数使用逗号隔开
  const result: Truffle.TransactionResponse<AllEvents> =
    await instance.sendCoin(accounts[1], 100, { from: accounts[0] });
  // 通过result.logs获取事件日志
  const logs = result.logs;
  // 通过result.receipt获取交易回执
  const receipt = result.receipt;
  // 通过result.tx获取交易hash
  const txHash = result.tx;

  // 预估gas
  const gas = await instance.sendCoin.estimateGas(accounts[1], 100, {
    from: accounts[0],
  });
  console.log("gas: ", gas);
};
