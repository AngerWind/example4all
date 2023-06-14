import { ethers, providers } from "ethers";

async function main() {
  /**
   * provider是以太坊网络的抽象, provider对以太坊数据是只读的, 不能修改数据
   * 如果需要与网络交互, 请使用他的子类
   */
  // 可以传入网络名, chainid, 或者url
  // 他会连接到尽可能多的后端服务, 当发出一个请求时，它会同时发送到多个后端。当从每个后端返回响应时，会检查它们是否同意。
  // 一旦达到规定数量(即足够多的后端同意)，你的应用程序就会得到相应。
  const defaultProvider: ethers.providers.BaseProvider =
    ethers.getDefaultProvider("sepolia");

  /**
   * 账户相关
   */
  const address = "0x966011527fA80D7CeF564d3B1B6ACF398F59ed27";
  const balance = await defaultProvider.getBalance(address, "latest"); // 获取指定区块高度下的余额, 如果不传入区块高度, 则默认为最新区块

  await defaultProvider.getCode(address, "latest"); // 获取指定区块高度下的合约代码, 如果不传入区块高度, 则默认为最新区块

  await defaultProvider.getTransactionCount(address, "latest"); // 获取指定区块高度下的交易数量, 如果不传入区块高度, 则默认为最新区块

  /**
   * 区块信息相关
   */
  await defaultProvider.getBlockNumber(); // 获取最新区块高度

  await defaultProvider.getBlock("latest"); // 获取最新区块, result.transactions 是交易的hash的数组

  const blockWithTransactions = await defaultProvider.getBlockWithTransactions(
    "latest"
  ); // 获取最新区块, result.transactions 是 一个TransactionResponse 对象的数组集合
  blockWithTransactions.transactions.forEach((tx) => {
    console.log(tx);
  });

  /**
   * ens相关
   */

  const resolver = await defaultProvider.getResolver("sepolia.eth"); // 获取指定域名的解析器
  const resolverAddr = await resolver?.getAddress(); // 获取指定域名的地址
  const vitalikAddr = await defaultProvider.resolveName("vitalik.eth");
  const ensName = vitalikAddr
    ? await defaultProvider.lookupAddress(vitalikAddr)
    : undefined; // 获取指定地址的域名

  /**
   * solidity event相关
   */
  const enentFilter = {
    address: "0x53844F9577C2334e541Aec7Df7174ECe5dF1fCf0",
    topics: [null, null, null],
    fromBlock: 0,
    toBlock: "latest",
    // blockHash: "0000000000000000"
  };
  const logs: ethers.providers.Log[] = await defaultProvider.getLogs(
    enentFilter
  ); // 获取指定event的logs

  /**
   * 网络状态相关
   */
  await defaultProvider.getNetwork(); // 获取当前网络信息

  await defaultProvider.getGasPrice(); // 获取当前gas价格, 适用于eip1559之前

  // 返回在一笔交易中当前的recommended FeeData。
  // 对于 EIP-1559 的交易, 应该使用其中的maxFeePerGas 和 maxPriorityFeePerGas。
  // 对于不支持 EIP-1559 中被遗留的交易和网络，应该使用其中的gasPrice。
  const feeData: ethers.providers.FeeData = await defaultProvider.getFeeData();

  /**
   * 交易相关
   */
  await defaultProvider.call({ from: "", to: "", data: "" }); // 只能调用view和pure等不需要gas的方法
  await defaultProvider.estimateGas({ from: "", to: "", data: "" }); // 估算gas
  await defaultProvider.getTransaction("0x"); // 获取指定hash的交易
  await defaultProvider.getTransactionReceipt("0x"); // 获取指定hash的交易回执
  defaultProvider.sendTransaction("signed transaction"); // 发送交易, 该交易必须经过了签名

  /**
   * 监听事件
   * event可以是string, 有固定的几个值:
   * - block 当一个区块被挖出时触发
   * - pending 当一个交易被挖出时触发
   * - error 当一个错误发生时触发
   * - willPoll 在一个polling loop开始之前触发;(大多数开发者很少使用)
   * - poll 在每个poll cycle中，`blockNumber`更新之后(如果改变了)，以及与在poll loop中任何其他的事件(如果有)之前触发; (大多数开发者很少使用)
   * - didPoll 在polling loop中的所有事件被触发后触发;(大多数开发者很少使用)
   * - debug 每个Provider可以使用它来发出有用的调试信息，格式由开发者决定;(大多数开发者很少使用) (very rarely used by most developers)
   */
  defaultProvider.on("block", (blockNumber) => {
    console.log("blockNumber", blockNumber);
  });
  defaultProvider.on("pending", (pedingTransaction) => {
    console.log("pedingTransaction", pedingTransaction);
  });
  defaultProvider.on("error", (error) => {
    console.log("error", error);
  });
  defaultProvider.on("willPoll", (pollld) => {
    console.log("pollld", pollld);
  });
  defaultProvider.on("poll", (pollld, blockNumber) => {
    console.log("pollld", pollld);
    console.log("blockNumber", blockNumber);
  });
  defaultProvider.on("didPoll", (pollld) => {
    console.log("pollld", pollld);
  });
  defaultProvider.on("debug", (...args) => {});

  // event还可以是一个transaction的hash, 该hash对应的交易被挖出时触发
  defaultProvider.once("0x0", (transaction) => {});

  // event还可以是一个filter, 该filter对应的event被触发时触发
  const filter1 = {
    address: "dai.tokens.ethers.eth",
    topics: [
      ethers.utils.id("Transfer(address,address,uint256)"), // 指定事件的签名
      ethers.utils.hexZeroPad("0x000", 32), // 指定第一个参数from的值
      null, // 第二个参数to的值, 匹配任何地址
    ],
  };
  const filter2 = {
    address: "dai.tokens.ethers.eth",
    topics: [
      ethers.utils.id("Transfer(address,address,uint256)"), // 指定事件的签名
      [
        ethers.utils.hexZeroPad("0x000", 32),
        ethers.utils.hexZeroPad("0x000", 32),
      ], // 指定第一个参数from的值, 可以是一个数组, 匹配数组中的任意一个值
      null, // 第二个参数to的值, 匹配任何地址
    ],
  };
  defaultProvider.on(filter1, (log, event) => {
    // 当 DAI token 发生转账时触发
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
