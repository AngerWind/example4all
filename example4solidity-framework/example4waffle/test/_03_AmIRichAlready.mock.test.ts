import { use, expect } from "chai";
import { ContractFactory, utils } from "ethers";
import { MockProvider } from "@ethereum-waffle/provider";
import { waffleChai } from "@ethereum-waffle/chai";
import { deployMockContract } from "@ethereum-waffle/mock-contract";

import IERC20 from "../build/IERC20.json";
import AmIRichAlready from "../build/AmIRichAlready.json";

use(waffleChai);

/**
 * waffle中的mock合约, 基本上就是创建一个mock合约, 然后这个可以自己控制返回值, 抛出异常
 * 我们需要测试的合约可以与他进行互动
 *
 * 通过一下方法来控制合约的行为
 * await mockContract.mock.<nameOfMethod>.returns(<value>)
 * await mockContract.mock.<nameOfMethod>.withArgs(<arguments>).returns(<value>)
 * await mockContract.mock.<nameOfMethod>.reverts()
 * await mockContract.mock.<nameOfMethod>.revertsWithReason(<reason>)
 * await mockContract.mock.<nameOfMethod>.withArgs(<arguments>).reverts()
 * await mockContract.mock.<nameOfMethod>.withArgs(<arguments>).revertsWithReason(<reason>)
 *
 * 很垃圾的功能, 没什么用, 很离谱, 我们的合约与其他合约交互的过程直接mock掉, 这样测试有什么意义
 */
describe("Am I Rich Already", () => {
  async function setup() {
    const [sender, receiver] = new MockProvider().getWallets();
    // 通过abi创建一个mock合约
    const mockERC20 = await deployMockContract(sender, IERC20.abi);
    const contractFactory = new ContractFactory(
      AmIRichAlready.abi,
      AmIRichAlready.bytecode,
      sender
    );
    const contract = await contractFactory.deploy(mockERC20.address);
    return { sender, receiver, contract, mockERC20 };
  }

  it("returns false if the wallet has less then 1000000 coins", async () => {
    const { contract, mockERC20 } = await setup();
    // 设置mock合约的balanceOf方法返回值
    await mockERC20.mock.balanceOf.returns(utils.parseEther("999999"));
    expect(await contract.check()).to.be.equal(false);
  });

  it("returns true if the wallet has more than 1000000 coins", async () => {
    const { contract, mockERC20 } = await setup();
    // 设置mock合约的balanceOf方法返回值
    await mockERC20.mock.balanceOf.returns(utils.parseEther("1000001"));
    expect(await contract.check()).to.equal(true);
  });

  it("reverts if the ERC20 reverts", async () => {
    const { contract, mockERC20 } = await setup();
    //  设置mock合约的balanceOf方法抛出异常
    await mockERC20.mock.balanceOf.reverts();
    await expect(contract.check()).to.be.revertedWith("Mock revert");
  });

  it("returns 1000001 coins for my address and 0 otherwise", async () => {
    const { contract, mockERC20, sender, receiver } = await setup();
    // 设置mock合约的balanceOf方法返回值
    await mockERC20.mock.balanceOf.returns("0");
    // 设置mock合约对于不同的参数返回不同的值
    await mockERC20.mock.balanceOf
      .withArgs(sender.address)
      .returns(utils.parseEther("1000001"));

    expect(await contract.check()).to.equal(true);
    expect(await contract.connect(receiver.address).check()).to.equal(false);
  });
});
