import { ethers } from "ethers";

async function main() {
  // 创建随机的wallet对象
  const wallet1 = ethers.Wallet.createRandom();
  console.log(`address: ${await wallet1.getAddress()};
  private key: ${wallet1.privateKey};
  public key: ${wallet1.publicKey};
  phrase: ${wallet1.mnemonic?.phrase};`);

  // 利用私钥和provider创建wallet对象, 并连接上指定网络
  // 也可以在稍后使用connect来连接网络
  const privateKey =
    "0x227dbb8586117d55284e26620bc76534dfbd2394be34cf4a09cb775d593b6f2b";
  const providerEth = new ethers.providers.JsonRpcProvider("https://eth.llamarpc.com");
  const wallet2 = new ethers.Wallet(privateKey, providerEth);

  // 从助记词创建wallet对象
  const phrase =
    "walnut wait foot vapor flag squeeze garage bomb lyrics pigeon bulk veteran";
  const wallet3 = ethers.Wallet.fromMnemonic(phrase);

  // Wallet还有
  //     fromEncryptedJsonSync   从加密的json中创建钱包
  //     encryptSync    将一个钱包加密成json

  console.log(`address: ${wallet2.address}
private key: ${wallet2.privateKey}`);

  console.log(`transaction count: ${await wallet2.getTransactionCount()}`);

  console.log(`phrase: ${wallet3.mnemonic?.phrase}`);
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
