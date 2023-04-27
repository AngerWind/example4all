const { describe } = require("mocha");
const chai = require("chai");
const assert = chai.assert;
const expect = chai.expect;
const should = chai.should();

/**
 * 最好传入function定义的函数, 因为箭头函数的this是固定的, 无法改变
 * 所以会导致this.skip, this.retries等方法无法使用
 */
describe("test1 file", function () {
  /**
   * before: 所有测试用例执行之前执行一次
   * after: 所有测试用例执行之后执行一次
   * beforeEach: 每个测试用例执行之前执行一次
   * afterEach: 每个测试用例执行之后执行一次
   * describe: 分组, 上一级分组中的所有it执行wan后, 才会执行下一级分组中的it
   *      在当前案例中, 只有test2, test3执行完后, 才会执行test1中的it
   * it: 测试用例
   *
   * 可以通过mocha --grep "标签" 来执行指定的测试用例
   *
   *
   *
   */
  before(() => {
    console.log("before");
  });
  after(() => {
    console.log("after");
  });
  beforeEach(() => {
    console.log("beforeEach-------------------------");
  });
  afterEach(() => {
    console.log("afterEach--------------------------");
  });
  describe("test1", () => {
    it("test1.1", () => {
      console.log("test1.1");
    });
  });

  it("test2", () => {
    console.log("test2");
  });
  it("test3", () => {
    console.log("test3");
  });

  // it.only("test4", () => {
  //     console.log("test4")
  // }); // 添加only之后, 执行mocha就只会执行only标记的测试用例, 同时only也可以用在describe中, 不要把only提交到git上

  it.skip("test5", () => {
    console.log("test5");
  }); // skip会跳过这个测试用例, 并且将其标记为pending, 意思是这个测试用例还没有写完

  it("test6", function () {
    this.skip(); // this.skip()也可以跳过这个测试用例
    console.log("test6");
  });

  describe("retry", function () {
    /**
     * retry
     */
    this.retries(2); // 针对当前分组中的所有测试案例, 如果失败了重试两次, 即最多执行3次
    it("test7", function () {
      this.retries(1); // 重试次数, 会覆盖上层定义的重试次数, 在这个案例中, test7会执行2次
      console.log("test7");
      assert(false, "test retry");
    });
  });

  describe("timeout", function () {
    /**
     * timeout和slow
     *
     * slow: 超过多少毫秒就会标记为慢用例, 默认是75ms, 可以通过this.slow()来设置
     *      用例执行时间 < slow/2 , 被标记为快用例
     *      slow/2 < 用例执行时间 < slow, 被标记为中等用例
     *      slow < 用例执行时间 < timeout, 被标记为慢用例
     *
     * timeout: 超过多少毫秒就会标记为超时用例, 默认是2000ms, 可以通过this.timeout()来设置,
     *      如果使用this.timeout(0), 则表示不设置超时时间
     */
    this.slow(1000); // 针对当前分组中的所有测试案例, 如果超过1000ms就会标记为慢用例
    this.timeout(2000); // 针对当前分组中的所有测试案例, 如果超过1000ms就会标记为超时用例

      it("test8", async function () {
          this.slow(500) // 覆盖
          this.timeout(1000); // 覆盖

          console.log("test8");
          await new Promise((resolve, reject) => {
              setTimeout(resolve, 1500);
          });
    });
  });
});
