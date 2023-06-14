import Web3 from "web3";

async function main() {
  const web3 = new Web3("http://localhost:8545");
  // 订阅pending的交易
  web3.eth.subscribe("pendingTransactions", function (error, txHash) {
    console.log(txHash);
  });
  // 当有新的块产生时，订阅块
  web3.eth.subscribe("newBlockHeaders", function (error, blockHeader) {
    console.log(blockHeader);
  });
  // 订阅指定地址的事件
  web3.eth
    .subscribe("logs", {
      address: "0x1234567890123456789012345678901234567890",
    })
    .on("data", (log) => {
      console.log("收到日志事件：", log);
      // 在这里可以处理收到的日志事件
    });

  // 清除订阅
  web3.eth.clearSubscriptions((error, result) => {});
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
