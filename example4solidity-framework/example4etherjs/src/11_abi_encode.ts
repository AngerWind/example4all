import { ethers } from "ethers";
import { FormatTypes, Interface } from "ethers/lib/utils";

/**
 * abicoder是一个存储的abi编码和解码的工具
 */

var abiCoder = ethers.utils.defaultAbiCoder;
// 对简单的类型进行编码
const bin = abiCoder.encode(["uint", "string"], [1234, "Hello World"]);

// 对数组的类型进行编码
abiCoder.encode(["uint[]", "string"], [[1234, 5678], "Hello World"]);

// 对复杂的结构体进行编码
abiCoder.encode(
  ["uint", "tuple(uint256, string)"],
  [1234, [5678, "Hello World"]]
);

const data1 =
  "0x00000000000000000000000000000000000000000000000000000000000004d20000000000000000000000000000000000000000000000000000000000000040000000000000000000000000000000000000000000000000000000000000000b48656c6c6f20576f726c64000000000000000000000000000000000000000000";
abiCoder.decode(["uint", "string"], data1);
// [
//   { BigNumber: "1234" },
//   'Hello World'
// ]

// 对数组的类型进行解码
const data2 =
  "0x000000000000000000000000000000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000000000000a0000000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000004d2000000000000000000000000000000000000000000000000000000000000162e000000000000000000000000000000000000000000000000000000000000000b48656c6c6f20576f726c64000000000000000000000000000000000000000000";
abiCoder.decode(["uint[]", "string"], data2);
// [
//   [
//     { BigNumber: "1234" },
//     { BigNumber: "5678" }
//   ],
//   'Hello World'
// ]

// 对复杂的结构体进行解码; 参数如果未命名，只允许按照位置（positional）方式去访问值（values）
const data3 =
  "0x00000000000000000000000000000000000000000000000000000000000004d20000000000000000000000000000000000000000000000000000000000000040000000000000000000000000000000000000000000000000000000000000162e0000000000000000000000000000000000000000000000000000000000000040000000000000000000000000000000000000000000000000000000000000000b48656c6c6f20576f726c64000000000000000000000000000000000000000000";
abiCoder.decode(["uint", "tuple(uint256, string)"], data3);
// [
//   { BigNumber: "1234" },
//   [
//     { BigNumber: "5678" },
//     'Hello World'
//   ]
// ]

/**
 * interface是etherjs中对于合约abi的封装, 因为已经封装了abi, 所以编码和解码的时候, 就不需要传入abi了
 *
 * abi有两种, 一种是在remix中生成的json格式, 一直就是这样的数组格式, 被称为Human-Readable ABI
 */
const abi = [
    // 构造函数
    "constructor(string symbol, string name)",
    // 会改变状态的方法
    "function transferFrom(address from, address to, uint amount)",
    // 会改变状态的方法，是 payable 的(可接收以太币)
    "function mint(uint amount) payable",
    // 常量方法 (如 "view" 或 "pure")
    "function balanceOf(address owner) view returns (uint)",
    // 事件
    "event Transfer(address indexed from, address indexed to, uint256 amount)",
    // 自定义的 Solidity Error
    "error AccountLocked(address owner, uint256 balance)",
    // 带有结构体类型的例子
    "function addUser(tuple(string name, address addr) user) returns (uint id)",
    "function addUsers(tuple(string name, address addr)[] user) returns (uint[] id)",
    "function getUser(uint id) view returns (tuple(string name, address addr) user)",
];
// 直接从合约中获取
const provider = new ethers.providers.JsonRpcProvider(
    "https://eth-mainnet.g.alchemy.com/v2/oKmOQKbneVkxgHZfibs-iFhIlIAl6HDN"
  );
  const contract = new ethers.Contract(
    "0x0000000000000000000000000000000", // 合约地址
    abi,
    provider
);
const iface1 = contract.interface;    



// 从abi中创建interface
const iface = new Interface(abi);

const jsonAbi = iface.format(FormatTypes.json); // 转换为json格式的abi
const readableAbi = iface.format(FormatTypes.full); // 转换为Human-Readable ABI格式的abi

// 编码构造函数的参数
iface.encodeDeploy(["SYM", "Some Name"]);

// 对error进行编码和解码
const errorData = iface.encodeErrorResult("AccountLocked", [
  "0x8ba1f109551bD432803012645Ac136ddd64DBA72",
  ethers.utils.parseEther("1.0"),
]);
iface.decodeErrorResult("AccountLocked", errorData);

// 编码event
iface.encodeFilterTopics(iface.getEvent("Transfer"), [
  "0x8ba1f109551bD432803012645Ac136ddd64DBA72",
]);

// 对函数参数进行abi编码和解码, 可直接用于call
const funcData = iface.encodeFunctionData("transferFrom", [
  "0x8ba1f109551bD432803012645Ac136ddd64DBA72",
  "0xaB7C8803962c0f2F5BBBe3FA8bf41cd82AA1923C",
  ethers.utils.parseEther("1.0"),
]);
iface.decodeFunctionData("transferFrom", funcData);

// 对函数返回值进行abi编码和解码
const funcResult = iface.encodeFunctionResult("balanceOf", [
  "0x8ba1f109551bD432803012645Ac136ddd64DBA72",
]);
iface.decodeFunctionResult("balanceOf", funcResult);
