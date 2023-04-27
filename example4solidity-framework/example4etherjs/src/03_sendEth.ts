import { ethers} from 'ethers';

async function main() {
  
  const provider = new ethers.providers.JsonRpcProvider("https://eth-sepolia.g.alchemy.com/v2/H8zDcVhsxxmYq7YomnXe3YQ4gGmOiUgU");
  const wallet1 = new ethers.Wallet(
    "5961e0624c7c4862efce438e3b069153519a8c4d56299631692b3b3eaf09d9eb",
    provider
  );
  const wallet2 = new ethers.Wallet(
    "fbb597156d8516a4a656894b9f4f9c135e277d5956d53603f5dc199f62c9fc10",
    provider
  );
  console.log(
    `wallet1 balance: ${ethers.utils.formatEther(
      await provider.getBalance(wallet1.address)
    )}`
  );
  console.log(
    `wallet2 balance: ${ethers.utils.formatEther(
      await provider.getBalance(wallet2.address)
    )}`
  );

  const transResq: ethers.providers.TransactionRequest = {
    to: wallet2.address,
    value: ethers.utils.parseEther("0.02"),
  };
  console.log("sending transaction...");
  const response: ethers.providers.TransactionResponse = await wallet1.sendTransaction(
    transResq
  )
  console.log("send transaction success, waitting confirm");
  await response.wait(3);

  console.log(response);

  console.log(
    `wallet1 balance: ${ethers.utils.parseEther(
      (await provider.getBalance(wallet1.address)).toString()
    )}`
  );
  console.log(
    `wallet2 balance: ${ethers.utils.parseEther(
      (await provider.getBalance(wallet2.address)).toString()
    )}`
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
