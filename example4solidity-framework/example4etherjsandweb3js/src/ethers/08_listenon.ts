import { EventFilter, ethers } from "ethers";

async function main() {
  const provider = new ethers.providers.JsonRpcProvider(
    "https://eth-sepolia.g.alchemy.com/v2/H8zDcVhsxxmYq7YomnXe3YQ4gGmOiUgU"
  );

  const abi = [
    "event Transfer(address indexed from, address indexed to, uint256 value)",
  ];

  const address = "0x53844F9577C2334e541Aec7Df7174ECe5dF1fCf0"; // 随便一个erc20的合约地址

  const contract = new ethers.Contract(address, abi, provider);
  const blockNumber = await provider.getBlockNumber();

  // Transfer有3个topic, 分别是event的hash, from, to
  const filter: EventFilter = contract.filters.Transfer(); // 通过filter创建过滤器,Transfer表示要查找的event名称
  const filter1 = contract.filters.Transfer(null, null, null); // 根据topic的位置来指定过滤条件, null表示不过滤

  contract.on(filter, (from, to, value, event) => {
    console.log(from, to, value, event); // 监听event
  }); // 一直监听

  contract.once(filter, (from, to, value, event) => {
    console.log(from, to, value, event); // 监听event
  }); // 只监听一次
    
  console.log("start listen");
    await new Promise((resolve, reject) => {
        setTimeout(() => {}, 200000)
  }); // 20s后停止监听
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
