// callstatic 用来模拟与合约的交互, 并返回调用的结果

import { EventFilter, ethers } from "ethers";

import * as fs from "fs";
import * as path from "path";

async function main() {
  const abi = ["function supportsInterface(bytes4) public view returns(bool)"]

  const provider = new ethers.providers.JsonRpcProvider(
    "https://eth-mainnet.g.alchemy.com/v2/oKmOQKbneVkxgHZfibs-iFhIlIAl6HDN"
  );
  const contract = new ethers.Contract(
    "0xbc4ca0eda7647a8ab7c2061c2e118a18a936f13d", // bayc的合约地址
    abi,
    provider
    );
    const selectorERC721 = "0x80ac58cd" // ERC721接口的id
    const support = await contract.supportsInterface(selectorERC721)
    console.log(support) // true

}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });

