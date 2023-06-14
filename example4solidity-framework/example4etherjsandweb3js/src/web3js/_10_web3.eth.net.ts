import { Transaction } from "web3-core/types";
import Web3 from "web3";

async function main() {
  const web3 = new Web3("http://localhost:8545");
  // 获取网络id
  const networkId = await web3.eth.net.getId();

  // 获取正在连接的节点的peer数量
  const peerCount = await web3.eth.net.getPeerCount();

  // 查看当前节点释放正在连接其他对等节点
  const isListening = await web3.eth.net.isListening();
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
