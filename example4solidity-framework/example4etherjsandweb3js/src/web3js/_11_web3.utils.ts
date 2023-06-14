import { Transaction } from "web3-core/types";
import Web3 from "web3";

async function main() {
  const web3 = new Web3("http://localhost:8545");

  /**
   * 单位转换
   */
  // 单位转换为wei
  const ether = web3.utils.toWei("1", "ether"); // 将ether转换为wei
  const gwei = web3.utils.toWei("1", "gwei"); // 将gwei转换为wei

  // 将wei转换为其他单位
  const num = web3.utils.fromWei("1", "ether"); // 将wei转换为ether
  const num2 = web3.utils.fromWei("1", "gwei"); // 将wei转换为gwei

  // 判断是否为一个有效地址
  const isAddress = web3.utils.isAddress(
    "0xc1912fEE45d61C87Cc5EA59DaE31190FFFFf232d"
  );

  /**
   * 填充
   */
  // 填充字符串左边, 使其长度达到指定字符数量
  const str = web3.utils.padLeft("0x3456ff", 20);

  // 填充字符串右边, 使其长度达到指定字符数量
  const str2 = web3.utils.padRight("0x3456ff", 20);

  /**
   * keccak256
   */
  // 对数据直接进行keccak256哈希
  const hash = web3.utils.keccak256("I am Satoshi Nakamoto");

  // 对数据先进行类似abi.encodePacked的处理, 再进行keccak256哈希, 如果传入空字符串, 将会返回null
  const hash2 = web3.utils.soliditySha3("I am Satoshi Nakamoto");
  // 与上面不同的是, 如果传入空字符串, 将会返回字符串的哈希值,而不是null
  const hash3 = web3.utils.soliditySha3Raw("I am Satoshi Nakamoto");

  /**
   * 其他转十六进制
   */
  // 将数字和字符串转换为十六进制, 纯数字的字符串会被当做数字处理, 否则按照utf8编码转换为十六进制
  const str5 = web3.utils.toHex(123);
  const str6 = web3.utils.toHex("123");
  const str3 = web3.utils.numberToHex(123); // 十进制转十六进制, 返回字符串
  const str7 = web3.utils.utf8ToHex("I am a string"); // utf8字符串转十六进制
  const str8 = web3.utils.asciiToHex("I am a string"); // ascii字符串转十六进制
  const str9 = web3.utils.bytesToHex([0x49, 0x20, 0x61, 0x6d, 0x20, 0x61]); // 字节转十六进制
  /**
   * 十六进制转其他
   */
  const num3 = web3.utils.hexToNumberString("0x12"); // 将十六进制转换为十进制, 并返回字符串
  const num4 = web3.utils.hexToNumber("0x12"); // 将十六进制转换为十进制, 返回数字
  const str4 = web3.utils.hexToUtf8("0x4920616d2043616e6e6f742042656172"); // 十六进制转utf8字符串
  const bytes = web3.utils.hexToBytes("0x4920616d2043616e6e6f742042656172"); // 十六进制转字节

  // 将任何类型的数据转换为BigNumber
  const num5 = web3.utils.toBN("0x12");
  const num6 = web3.utils.toBN(123);
  const num7 = web3.utils.toBN("123");
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
