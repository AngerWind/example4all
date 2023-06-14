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

  // Transfer有3个topic, 分别是event名称的hash, from, to
  const filter: EventFilter = contract.filters.Transfer(); // 通过filter创建过滤器,Transfer表示要查找的event名称
  const filter1 = contract.filters.Transfer(null, null, null); // 根据topic的位置来指定过滤条件, null表示不过滤
  
  const eventArray: ethers.Event[] = await contract.queryFilter(
    filter,
    blockNumber - 10, // 指定要过滤的区块范围
    blockNumber
    );
    
    console.log(eventArray[0]);
    console.log(eventArray[0].args?.from); // 从event中可以获取event发生的block, blockhash, 发生event的address, event的data, event的topic等信息
    
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
