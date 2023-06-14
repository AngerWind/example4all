import { expect } from "chai";
import { loadFixture, deployContract, Fixture } from "ethereum-waffle";
import BasicToken from "../build/BasicToken.json";
import { MockProvider } from "ethereum-waffle";
import { Contract, Wallet } from "ethers";

/**
 * 制作区块链快照, 可以加快测试速度
 */
describe("Fixtures", () => {
  //   const fixture: Fixture<{ token: Contract; wallet: Wallet; other: Wallet }> =
  //     async function ([wallet, other]: Wallet[], provider: MockProvider) {
  //       const token = await deployContract(wallet, BasicToken, [
  //         wallet.address,
  //         1000,
  //       ]);
  //       return { token, wallet, other };
  //     };

  // 设置区块链快照
  async function fixture([wallet, other]: Wallet[], provider: MockProvider) {
    const token = await deployContract(wallet, BasicToken, [
      wallet.address,
      1000,
    ]);
    return { token, wallet, other };
  }

  it("Assigns initial balance", async () => {
    // 加载区块链快照
    const { token, wallet } = await loadFixture(fixture);
    expect(await token.balanceOf(wallet.address)).to.equal(1000);
  });

  it("Transfer adds amount to destination account", async () => {
    const { token, other } = await loadFixture(fixture);
    await token.transfer(other.address, 7);
    expect(await token.balanceOf(other.address)).to.equal(7);
  });
});
