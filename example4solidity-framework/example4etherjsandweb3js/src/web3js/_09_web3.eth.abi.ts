import { Transaction } from "web3-core/types";
import Web3 from "web3";

async function main() {
  const web3 = new Web3("http://localhost:8545");
  // 获取函数的abi签名
  const signature = web3.eth.abi.encodeFunctionSignature(
    "myMethod(uint256,string)"
  );

  // 获取事件的abi签名
  const signature1 = web3.eth.abi.encodeEventSignature(
    "myEvent(uint256,string)"
  );

  // 根据类型对数据进行编码
  const data = web3.eth.abi.encodeParameter("uint256", "2345675643");

  // 根据类型对多个数据进行编码
  const data1 = web3.eth.abi.encodeParameters(
    ["uint256", "string"],
    ["2345675643", "Hello!%"]
  );

  // 编码一个calldata, 太几把麻烦了
  web3.eth.abi.encodeFunctionCall(
    {
      name: "myMethod",
      type: "function",
      inputs: [
        {
          type: "uint256",
          name: "myNumber",
        },
        {
          type: "string",
          name: "myString",
        },
      ],
    },
    ["2345675643", "Hello!%"]
  );

  // 解码参数
  web3.eth.abi.decodeParameter("uint256", data);

  // 解码多个参数
  web3.eth.abi.decodeParameters(["uint256", "string"], data1);

  // 解码abi编码的日志数据
  web3.eth.abi.decodeLog(
    [
      {
        type: "string",
        name: "myString",
      },
      {
        type: "uint256",
        name: "myNumber",
        indexed: true,
      },
      {
        type: "uint8",
        name: "mySmallNumber",
        indexed: true,
      },
    ],
    "0x0000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000000748656c6c6f252100000000000000000000000000000000000000000000000000",
    [
      "0x000000000000000000000000000000000000000000000000000000000000f310",
      "0x0000000000000000000000000000000000000000000000000000000000000010",
    ]
  );
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
