import { Transaction } from "web3-core/types";
import Web3 from "web3";

async function main() {
  const web3 = new Web3("http://localhost:8545");
  // 获取gas price
  const gasPrice = await web3.eth.getGasPrice();

  //获取节点控制的账户列表
  const accounts = await web3.eth.getAccounts();

  //获取当前区块高度
  const blockNumber = await web3.eth.getBlockNumber();

  // 获取地址的余额
  const balance = await web3.eth.getBalance(accounts[0]);

  //获取地址在某个特定slot上的值
  const storageAt = await web3.eth.getStorageAt(accounts[0], 0);

  // 获取地址的bytecode
  const code = await web3.eth.getCode(accounts[0]);

  // 返回某个区块中的交易数量
  const transactionCount = await web3.eth.getBlockTransactionCount(blockNumber);

  // 获取指定hash的交易对象
  const transaction = await web3.eth.getTransaction("0x0");

  // 获取pending的交易列表
  const pendingTransactions: Transaction[] =
    await web3.eth.getPendingTransactions();

  //根据交易hash返回交易的收据
  const transactionReceipt = await web3.eth.getTransactionReceipt("0x0");

  // 发送交易
  const sendTransaction = await web3.eth.sendTransaction({
    from: accounts[0],
    to: accounts[1],
    value: 1 * 10 ** 18,
    gas: 21000,
    gasPrice: 1000000000,
    data: "",
  });

  // 使用指定账户对数据进行签名
  const sign = await web3.eth.sign("hello world", accounts[0]);

  // 使用指定账户对交易进行签名
  const signTransaction = await web3.eth.signTransaction({
    from: accounts[0],
    to: accounts[1],
    value: 1 * 10 ** 18,
    gas: 21000,
    gasPrice: 1000000000,
    data: "",
  });

  //发送已经签名的交易, 交易的签名可以通过web3.eth.accounts.signTransaction()方法进行签名
  const sendSignedTransaction = await web3.eth.sendSignedTransaction(
    "0xf8aa8202b28"
  );

  // 执行一个call, 类似于ethers中的callstatic
  const call = await web3.eth.call({
    from: accounts[0],
    to: accounts[1],
    gasPrice: 1000000000,
    data: "",
  });

  // 执行一个estimateGas, 类似于ethers中的estimateGas
  const estimateGas = await web3.eth.estimateGas({
    from: accounts[0],
    to: accounts[1],
    gasPrice: 1000000000,
    data: "",
  });

  // 获取当前节点的coinbase地址
  const coinbase = await web3.eth.getCoinbase();

  // 获取当前节点的hashrate
  const hashrate = await web3.eth.getHashrate();

  // 获取当前节点的挖矿是否开启
  const mining = await web3.eth.isMining();

  // 获取当前节点的同步状态
  const syncing = await web3.eth.isSyncing();

  // 获取当前节点挖矿需要满足的难度, 类似于ethers中的getDifficulty
  // 返回一个array, 0:当前区块hash, 1: 种子哈希, 2: 目标难度
  const getWork = await web3.eth.getWork();
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
