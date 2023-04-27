const { ethers } = require("ethers");
const fs = require("fs");


async function main() {
  const abi = fs.readFileSync(
    "./src/contract/_src_contract_SimpleStorage_sol_SimpleStorage.abi",
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
    wallet
  );

  const currentFavoriteNumber = await contract.retrieve();
  console.log(currentFavoriteNumber); // currentFavoriteNumber是etherjs中的BigNumber类型, 因为solidity中无法使用float, 同时solidity中动辄二三十个的零对js来说也太大了
  console.log(currentFavoriteNumber.toString()); // 将BigNumber转换为Number
  console.log("the favorite number is " + currentFavoriteNumber.toString());

  console.log("start update");

  // 调用store方法, 即使solidity中的函数参数是uint, 最好传字符串
  const response = await contract.store("7");
  const storeReceipt = await response.wait(1);

  console.log("update success");

  console.log(response); // 返回的是交易发起的相关信息, 例如data, value, gasPrice, gasLimit, maxFeePerGas
  console.log(storeReceipt); // 返回的是交易完成的相关信息, 例如gasUsed, blockNumber, confirmations

  // 重新调用retrieve
  const currentFavoriteNumber1 = await contract.retrieve();
  console.log(`after update, the favorite number is ${currentFavoriteNumber1}`);
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
