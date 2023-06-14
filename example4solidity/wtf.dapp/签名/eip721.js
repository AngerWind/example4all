const { ethers } = require('hardhat');

async function signTypedData(signer) {
  const domain = {
    name: 'Uniswap V2',
    version: '1',
    chainId: 1,
    verifyingContract: '0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC',
  };

  // The named list of all type definitions
  const types = {
    Permit: [
      { name: 'owner', type: 'address' },
      { name: 'spender', type: 'address' },
      { name: 'value', type: 'uint256' },
      { name: 'nonce', type: 'uint256' },
      { name: 'deadline', type: 'uint256' },
    ],
  };

  // The data to sign
  const value = {
    owner: xx,
    spender: xx,
    value: xx,
    nonce: xx,
    deadline: xx,
  };
  // 签名
  const signature = await signer._signTypedData(domain, types, value);
  return signature;
}

async function main() {
  signers = await ethers.getSigners();
  signature = await signTypedData(signers[0]);
  console.log(signature);
}

main()
  .then(() => process.exit(0))
  .catch(error => {
    console.error(error);
    process.exit(1);
  });
