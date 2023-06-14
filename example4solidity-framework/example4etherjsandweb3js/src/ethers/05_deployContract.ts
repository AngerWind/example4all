import { log } from "console";
import { ethers } from "ethers";

import * as fs from "fs";
import * as path from "path";

async function main() {
  const provider = new ethers.providers.JsonRpcProvider(
    // "https://eth-sepolia.g.alchemy.com/v2/H8zDcVhsxxmYq7YomnXe3YQ4gGmOiUgU"
    "http://127.0.0.1:7545"
  );
  const wallet1 = new ethers.Wallet(
    // "5961e0624c7c4862efce438e3b069153519a8c4d56299631692b3b3eaf09d9eb",
    "0x107cdc069083265fe2826289cc7e16c43f729ec2fca2d1449be48f03c35b66e8",
    provider
  );

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
  const contractFactory = new ethers.ContractFactory(abi, bin, wallet1); // 创建工厂

  const contract = await contractFactory.deploy(); // 部署
  console.log(`address: ${contract.address}`); // 输出地址

  const resp = contract.deployTransaction; // 获取部署的response
  console.log(resp);

  const receipt = await resp?.wait(3); // 等待确认
  console.log(receipt);
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
