const { describe } = require("mocha");

describe("test2 file", () => { 

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
        console.log("before")
    });
    after(() => {
        console.log("after")
    });
    beforeEach(() => {
        console.log("beforeEach")
    });
    afterEach(() => {
        console.log("afterEach")
    });
    describe("test1", () => {
        it("test1.1", () => {
            console.log("test1.1")
        });
    });

    it("test2", () => {
        console.log("test2")
    });
    it("test3", () => {
        console.log("test3")
    });
})