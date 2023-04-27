import { run } from "hardhat";
// 传入合约地址, 合约的构造函数参数, hardhat可以通过地址知道对应的合约文件在哪里
const verify = async (contractAddress: string, args: any[]) => {
    console.log("Verifying contract...");
    try {
      // run这个变量可以用来执行hardhat的一些子任务
      // 等同于 npx hardhat cmd .....
      await run("verify:verify", {
        address: contractAddress,
        constructorArguments: args,
      });
    } catch (e: any) {
      if (e.message.toLowerCase().includes("already verified")) {
        console.log("Already Verified!");
      } else {
        console.log(e);
      }
    }
};
  
export { verify };