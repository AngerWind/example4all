import { ethers } from './ethers-5.2.esm.min.js';

const installedMetaMask = window.ethereum ? true : false;

const connectBtn = document.getElementById('connect');
const fundBtn = document.getElementById('fundButton');
const withdrawBtn = document.getElementById('withdraw');
const balance = document.getElementById('balance');

function init() {
  if (!installedMetaMask) {
    return;
  }
    connectBtn.addEventListener('click', connect);
    fundBtn.addEventListener('click', fund);
    balance.addEventListener('click', getBalance);
    withdrawBtn.addEventListener('click', withdraw);
}
init();
async function connect() {
    try {
        const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' });
        console.log(accounts);

        const [account] = await window.ethereum.request({ method: 'eth_accounts' })
        connectBtn.innerText = account;
    } catch (err) {
        console.error(err);
    }
}
async function fund() {
    try {
        const provider = new ethers.providers.Web3Provider(window.ethereum);
        const signer = provider.getSigner();
        const contract = new ethers.Contract('0xe7f1725e7734ce288f8367e1bb143e90bb3f0512', ['function fund() public payable'], signer);
        const transactionResponse = await contract.fund({ value: ethers.utils.parseEther(document.getElementById("fundInput").value) });
        await new Promise(function (resolve, reject) {
            provider.on(transactionResponse.hash, (transactionReceipt) => {
                console.log(`completed: ${transactionReceipt.confirmations} confirmations`);
                if (transactionReceipt.confirmations === 6) {
                    // cancel the listener
                    provider.removeAllListeners(transactionResponse.hash);
                    resolve();
                }
            });
        })
    } catch (err) {
        console.error(err);
    }
}
async function getBalance() {
    try {
        const provider = new ethers.providers.Web3Provider(window.ethereum);
        const balance = await provider.getBalance("0xe7f1725e7734ce288f8367e1bb143e90bb3f0512")
        alert(ethers.utils.formatEther(balance))
    } catch (err) {
        console.error(err);
    }
}
async function withdraw() {
    try {
        const provider = new ethers.providers.Web3Provider(window.ethereum);
        const signer = provider.getSigner();
        const contract = new ethers.Contract('0xe7f1725e7734ce288f8367e1bb143e90bb3f0512', ['function withdraw()'], signer);
        const transactionResponse = await contract.withdraw();
        await new Promise(function (resolve, reject) {
            provider.on(transactionResponse.hash, (transactionReceipt) => {
                console.log(`completed: ${transactionReceipt.confirmations} confirmations`);
                if (transactionReceipt.confirmations === 6) {
                    // cancel the listener
                    provider.removeAllListeners(transactionResponse.hash);
                    resolve();
                }
            });
        })
    } catch (err) {
        console.error(err);
    }
}
