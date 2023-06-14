import Web3 from "web3";

import { SignedTransaction } from "web3-core";

async function main() {
  const web3 = new Web3("http://localhost:8545");
  // 创建一个新的随机的账户
  const newAccount = web3.eth.accounts.create();
  console.log(newAccount.address);
  console.log(newAccount.privateKey);

  // 通过privateKey创建一个账户
  const account = web3.eth.accounts.privateKeyToAccount(
    "0xeff95774a39191afcda503ab5e1243f76747d886e9bfabf1b4434476641a9aac"
  );

  // 签名交易
  const signedTransaction: SignedTransaction =
    await web3.eth.accounts.signTransaction(
      {
        value: 1 * 10 ** 18,
        data: "",
        gas: 21000,
        gasPrice: 1000000000,
        chainId: 5777,
      },
      account.privateKey // 签名的私钥
    );

  // 签名数据
  // 将数据abi编码后进行keccak256哈希, 然后添加上\x19Ethereum Signed Message: \n, 最后进行签名
  const signedData = web3.eth.accounts.sign("hello world", account.privateKey);

  // 将账户添加到钱包中, 一个钱包中可以包含多个账户, 使用索引来获取账户
  const wallet = web3.eth.accounts.wallet;
  wallet.add(account); // 直接添加账户
  wallet.add(newAccount.privateKey); // 通过私钥添加账户

  const first = wallet[0]; // 通过索引获取账户
  wallet.remove(account.address); // 通过地址删除账户
  wallet.clear(); // 清空钱包

  // 创建一个新的空的钱包
  const newWallet = web3.eth.accounts.wallet.create(0);
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });
