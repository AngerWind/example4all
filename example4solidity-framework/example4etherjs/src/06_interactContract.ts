import { log } from "console";
import {  ethers } from "ethers";

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
  // 或者通过下面的方法, 将只读合约转换为可写合约
  // const contract2 = contract.connect(wallet)

  const num = await contract.retrieve();
  console.log(num);

  const storeResp = await contract.store(7);
  const storeReceipt = storeResp.wait(1);
  console.log(storeResp);
  console.log(storeReceipt);
  console.log(typeof(storeReceipt), typeof(storeResp))

  console.log(contract.retrieve());
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
