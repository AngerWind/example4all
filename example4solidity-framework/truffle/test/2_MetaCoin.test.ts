/**
 * 测试代码的结构与mocha一样
 * 只是使用了contract()方法来代替describe()方法
 * contract相对于describe的区别就是:
 *      在每次执行contract()方法之前都会重新执行部署脚本, 以便环境是赶紧的
 *      (针对ganache和truffle develop环境, contract()方法为了加快测试不会重新部署合约, 而是使用快照)
 *
 *      而decribe()方法不会
 *
 */
// describe("MetaCoin", function (accounts) {
//   it("should assert true", async function () {
//     await Migrations.deployed();
//     return assert.isTrue(true);
//   });
// });

import { MetaCoinInstance } from "../types/truffle-contracts";

const MetaCoin = artifacts.require("MetaCoin");

contract("MetaCoin", (accounts) => {
  it("should put 10000 MetaCoin in the first account", async () => {
    const instance = await MetaCoin.deployed();
    const balance = await instance.getBalance(accounts[0]);
    assert.equal(balance.valueOf(), 10000, "10000 wasn't in the first account");
  });

  it("should call a function that depends on a linked library", async () => {
    const instance = await MetaCoin.deployed();
    const balance = await instance.getBalance(accounts[0]);
    const balanceInEth = await instance.getBalanceInEth(accounts[0]);
    assert.equal(balanceInEth.toNumber(), 2 * balance.toNumber());
  });

  it("should send coin correctly", async () => {
    const amount = 10;

    const instance = await MetaCoin.deployed();

    const starting_balance0 = (
      await instance.getBalance(accounts[0])
    ).toNumber();
    const starting_balance1 = (
      await instance.getBalance(accounts[1])
    ).toNumber();
    await instance.sendCoin(accounts[0], amount, { from: accounts[1] });
    const ending_balance0 = (await instance.getBalance(accounts[0])).toNumber();
    const ending_balance1 = (await instance.getBalance(accounts[1])).toNumber();

    assert.equal(
      ending_balance0,
      starting_balance0 + amount,
      "Amount wasn't correctly taken from the sender"
    );
    assert.equal(
      ending_balance1,
      starting_balance1 - amount,
      "Amount wasn't correctly sent to the receiver"
    );
  });
});
