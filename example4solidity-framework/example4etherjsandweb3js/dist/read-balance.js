var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import { ethers } from "ethers";
function main() {
    return __awaiter(this, void 0, void 0, function* () {
        // provider 提供对区块链及其状态的只读访问
        const provider = ethers.getDefaultProvider("https://eth-sepolia.g.alchemy.com/v2/H8zDcVhsxxmYq7YomnXe3YQ4gGmOiUgU");
        // ethers原生支持ens域名
        const balanceWei = yield provider.getBalance("vitalik.eth");
        // 从链上获取的余额单位都是wei, 转换为eth
        const balanceETH = ethers.formatEther(balanceWei);
        console.log(`eth balance of vitalik: ${balanceETH}`);
    });
}
main()
    .then(() => {
    return process.exit();
})
    .catch((err) => {
    console.log(err);
    process.exit(1);
});
