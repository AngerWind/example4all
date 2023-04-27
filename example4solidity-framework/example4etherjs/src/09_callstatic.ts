// callstatic 用来模拟与合约的交互, 并返回调用的结果

import { EventFilter, ethers } from "ethers";

import * as fs from "fs";
import * as path from "path";

async function main() {
  const abi = fs.readFileSync(
    path.resolve(
      __dirname,
      "contract/_src_contract_SimpleStorage_sol_SimpleStorage.abi"
    ),
    "utf8"
  );

  const provider = new ethers.providers.JsonRpcProvider(
    "https://eth-sepolia.g.alchemy.com/v2/H8zDcVhsxxmYq7YomnXe3YQ4gGmOiUgU"
  );
  const wallet = new ethers.Wallet(
    "5961e0624c7c4862efce438e3b069153519a8c4d56299631692b3b3eaf09d9eb",
    provider
  );
  const contract = new ethers.Contract(
    "0x966011527fA80D7CeF564d3B1B6ACF398F59ed27",
    abi,
    wallet // 这里如果传入provider, 则只能调用只读方法, 传入wallet, 则可以调用所有方法
  );

    const storeResp = await contract.callStatic.store(7);
    console.log(storeResp); // 返回的是store函数的返回, 而不是交易的回执, 因为store函数没有返回值, 所以返回的是[]

}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });

