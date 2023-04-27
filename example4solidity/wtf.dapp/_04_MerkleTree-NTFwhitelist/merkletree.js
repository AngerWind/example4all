let { MerkleTree } = require("merkletreejs");
let keccak256 = require("keccak256");

let whitelistAddresses = [
  "0x5B38Da6a781c568545dCfcB03FcB875f56beddC4",
  "0x78D6CF3DEF45AF458442E787FE6E64A03750AB94",
  "0xA7BBE4CF55AF988F6E88D619AE1E00965044BC4E",
  "0x38C4278F42A26A588176017F5E4F6ED831835EA2",
  "0x9F9AOAA0507FE79CA1FF8F26A8D84BD8BBOEC9DF",
  "0xDABF258F5619942D19AD87C3472FABE893B26D7D",
  "0XD30ED9C457C7D32F3F7B949964191E4084C53F66",
];

// 生成叶子节点
const leafNodes = whitelistAddresses.map((addr) => keccak256(addr));
// 生成merkleTree, 并且自动hash算法, sortPairs表示在hash两个数据时, 小的表示第一个参数, 大的作为第二个参数
const merkleTree = new MerkleTree(leafNodes, keccak256, { sortPairs: true });

console.log(merkleTree.getHexRoot() + "\n-------------");
console.log(merkleTree.toString());

console.log(getProofOfAddress(whitelistAddresses[0]));

// 获取对应地址的proof
function getProofOfAddress(address) {
  let flag = whitelistAddresses.includes(address);
  if (flag === false) {
    return [""]; // 不存在该地址
  } else {
    let hexProof = merkleTree.getHexProof(keccak256(address));
    return hexProof;
  }
}
