import { log } from "console";
import { BigNumber, BigNumberish, ethers } from "ethers";

async function main() {
  const ethBalance = "2";
  const weiBalance = ethers.utils.parseEther(ethBalance); // 将ether转换为wei
  const ethBal = ethers.utils.formatEther(weiBalance); // 将wei转换为ether
  console.log(`${weiBalance}, ${ethBal}`);

  console.log(ethers.utils.formatUnits(BigInt(3.12 * 10 ** 18), "gwei")); // 将wei转换为指定单位
  console.log(ethers.utils.parseUnits("3000000000", "gwei")); // 将指定单位数额转换为wei

  console.log("--------------------------------------------");
  console.log(fromEthToWei("0.01") + "#####");
  console.log(fromGweiToWei("2000000000"));
  console.log(fromWeiToEth(BigInt(100000000000000000)));
  console.log(fromWeiToGwei(BigInt(3e18)));
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });

export function fromEthToWei(eth: string): BigNumber {
  return ethers.utils.parseUnits(eth, "ether"); // 将指定单位解析为wei
}

export function fromGweiToWei(gwei: string): BigNumber {
  return ethers.utils.parseUnits(gwei, "gwei"); //// 将指定单位解析为wei
}

export function fromWeiToEth(wei: BigNumberish): string {
  return ethers.utils.formatUnits(wei, "ether"); // 将wei解析为指定单位
}

export function fromWeiToGwei(wei: BigNumberish): string {
  return ethers.utils.formatUnits(wei, "gwei"); // 将wei解析为指定单位
}
