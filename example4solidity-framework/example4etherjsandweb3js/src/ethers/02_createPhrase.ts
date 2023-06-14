import { ethers, utils } from "ethers";

/**
 * https://zhuanlan.zhihu.com/p/149165331
 *
 * 比特币社区十分倡导Principle Of Avoiding Reuse，即用过的比特币地址就不要再用了，
 * 这样有助于交易隐私的保护和地址的安全性, 但是如果仅仅使用老旧的私钥管理技术，
 * 就意味着使用者必须在钱包中预设非常多的比特币地址，这些地址都是各自随机生成的，
 * 使用者在一个地址转账时，为了保证Principle Of Avoiding Reuse，在每一次转账后，
 * 都将交易输出指向另一个地址。这种操作手法有一个最大的麻烦，那就是私钥的管理和保存
 *
 * BIP32中提出了提出了一套确定性钱包的解决方案协议，即HD钱包协议。
 * HD钱包的全称是Hierarchical Deterministic Wallet，译为树状确定性钱包
 * 顾名思义，在HD钱包中管理的地址之间是有关系的，地址的生成如同树种-树干-树枝的关系。
 * 在BIP32中，规定了一个种子是如何形成的，以及这个种子如何生成一系列的私钥。
 * 只要掌握了这串种子，就等于控制了根据这个种子生成的私钥。
 *
 * 掌握种子显然比掌握成百上千的私钥要方便的多，但显然128位的字符并不好记。
 * 于是比特币开发社区在BIP39提出了助记词方案。
 * BIP39中所提出的解决方案是，随机生成数位的助记词，通过这些单词和单词的顺序就能得到种子。
 *
 * BIP39具体的解决方案是，定义一个包含2048个单词的单词表（
 * 参考链接：https://github.com/bitcoin/bips/blob/master/bip-0039/english.txt），
 * 生成一串随机序列后，对这串随机序列做一定的算法演变，得到一个新的序列，
 * 将序列分割成好几个部分后，将每个部分与单词表上的一个单词做对应，就得到了助记词。
 * 而助记词再通过密钥生成函数PBKDF2就能生成种子。
 * 至此，助记词——种子——私钥的生成过程就完成了
 */

// 自动生成助记词, 种子, 私钥, 地址, 公钥

async function main() {
  const mnemonic = utils.entropyToMnemonic(utils.randomBytes(32)); // 通过随机的种子生成短语
  // 创建HD钱包
  const hdNode = ethers.utils.HDNode.fromMnemonic(mnemonic);
  const numWallet = 20;
  // 派生路径：m / purpose' / coin_type' / account' / change / address_index
  // 我们只需要切换最后一位address_index，就可以从hdNode派生出新钱包
  let basePath = "m/44'/60'/0'/0";
  let wallets = [];
  for (let i = 0; i < numWallet; i++) {
    let hdNodeNew = hdNode.derivePath(basePath + "/" + i);
    let walletNew = new ethers.Wallet(hdNodeNew.privateKey);
    console.log(`第${i + 1}个钱包地址： ${walletNew.address}`);
    console.log(`第${i + 1}个钱包公钥： ${walletNew.publicKey}`);
    console.log(`第${i + 1}个钱包私钥： ${walletNew.privateKey}`);
    wallets.push(walletNew);
  }
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
