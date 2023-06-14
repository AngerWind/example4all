import Web3 from "web3";
import * as fs from "fs";
import * as path from "path";
import { EventEmitter } from "stream";

async function main() {
  // const web3 = new Web3(new Web3.providers.HttpProvider("http://localhost:8545"));
  const web3 = new Web3("http://127.0.0.1:7545"); // ganache
  const abi = fs.readFileSync(
    path.resolve(
      __dirname,
      "../contract/_src_contract_SimpleStorage_sol_SimpleStorage.abi"
    ),
    "utf-8" // 需要指定格式
  );
  const bin = fs.readFileSync(
    path.resolve(
      __dirname,
      "../contract/_src_contract_SimpleStorage_sol_SimpleStorage.bin"
    ),
    "utf-8"
  );

  const account = web3.eth.accounts.privateKeyToAccount(
    "0xeff95774a39191afcda503ab5e1243f76747d886e9bfabf1b4434476641a9aac"
  );
  console.log(await web3.eth.getBalance(account.address));

  /**
   * Contract上面有两个重要的属性: options.address和defaultAccount
   * options.address表示该合约关联到的地址, 如果没有设置, 那么在发送交易的时候需要指定to属性, 设置了之后可以省略, 会默认使用该地址
   * defaultAccount表示该合约关联到的账户的**地址**, 如果没有设置, 那么在发送交易的时候需要指定from属性, 设置了之后可以省略, 会默认使用该账户
   *
   * web3.js如何将defaultAccount地址关联到私钥上面的:
   * 1. 通过web3.eth.accounts.privateKeyToAccount(privateKey)将私钥转换为账户, 之后就可以自动关联到私钥上面了
   * 2. 针对ganache等测试环境, 可以从provider中获取一些可用的账户, 使用这些地址会自动关联到provider中的私钥上面
   */
  const simpleStorage = new web3.eth.Contract(JSON.parse(abi));

  // 将合约关联到具体的地址上
  simpleStorage.options.address = "0x5FbDB2315678afecb367f032d93F642f64180aa3";
  // 将合约关联到具体的账户上
  simpleStorage.options.from = account.address;

  // 通过合约地址获取合约实例
  const deployedSimpleStorage = new web3.eth.Contract(
    JSON.parse(abi),
    "0x5FbDB2315678afecb367f032d93F642f64180aa3"
  );

  /**
   * 与合约交互
   * web3.js中与合约交互, 包括部署合约都通过两个步骤来实现:
   * 1. 通过contract.deploy()或者contract.methods.myMethod(args)来填充交易数据, 获得一个对象
   * 2. 调用该对象的send方法来执行修改区块链状态的方法或者部署合约
   *    或者调用call来模拟调用并立即返回结果, 但是不能修改区块链状态, 类似于ethers.js中的callStatic
   *    或者调用estimateGas来估算gas,
   *    或者调用encodeABI来获取交易数据
   */

  // 填充构造函数参数
  const contractSendMethod = simpleStorage.deploy({
    data: bin,
    // arguments: [],
  });
  // 预估gas
  const gas = await contractSendMethod.estimateGas({
    from: account.address,
  });
  // 发送交易, 获得一个新的合约实例, 该实例的options.address属性会被设置为新部署的合约地址
  const instance = await contractSendMethod.send({
    from: account.address, // 指定发送交易的地址, 自动关联私钥
    gas, // 他妈的狗屎, gas不能乱设置, 不然会乱报错
  });
  // abi编码, 返回calldata
  const encodeABI = contractSendMethod.encodeABI();

  const receipt = await instance.methods
    .store(100)
    .send({ from: account.address }); // 填充数据并发送交易
  const result = instance.methods.retrieve().call(); // 填充数据并调用方法, 如果结果只有一个,那么原样返回, 如果结果有多个, 返回一个带属性和索引的对象
  const gas1 = (await instance.methods.store(100).estimateGas()) as number; // 填充数据并预估gas
  instance.methods.store(100).encodeABI(); // 填充数据并abi编码

  /**
   * 监听事件
   */
  // 只监听一次, 回调有两个参数, 第一个是监听失败的错误, 第二个是事件对象
  // 使用"allEvents"监听所有事件
  // 第二个参数是过滤器, 可以指定fromBlock, toBlock, address, topics, filter
  instance.once(
    "StoreEvent",
    { fromBlock: "latest", topics: [account.address, "1"] },
    (err, event) => {
      if (err) {
        console.log(err);
      } else {
        console.log(event);
      }
    }
  );

  // 或者使用myContract.events.myEvent([options][, callback])的方式来监听事件, 该方法返回一个EventEmitter对象
  // 使用该对象来监听事件
  let eventEmit: EventEmitter = instance.events.StoreEvent([
    account.address,
    "1",
  ]);
  // 接收到新传入的事件时触发
  eventEmit.on("data", (event) => {
    console.log(event);
  });
  // 监听出现错误时触发
  eventEmit.on("error", (err) => {
    console.log(err);
  });
  // 当订阅成功时触发一次, 返回订阅id
  eventEmit.on("connected", (subscriptionId) => {
    console.log(subscriptionId);
  });
  // 当订阅状态发生改变时触发, 如网络断开, 节点重启, 取消订阅, 可以用来是否资源, 更新界面
  eventEmit.on("changed", (subscriptionId) => {
    console.log(subscriptionId);
  });

  /**
   * 获取以往的事件
   */
  const pastEvents = await instance.getPastEvents("StoreEvent", {
    fromBlock: 0,
    toBlock: "latest",
    filter: {},
  });
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
