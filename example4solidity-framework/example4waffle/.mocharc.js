// mocha的配置文件
module.exports = {
  require: ["ts-node/register"], // 在执行测试之前, 先加载这些模块, 该模块用于ts执行mocha测试
  spec: "test/**/*.test.{ts, js, cjs, mjs}", // 指定测试文件的路径, 默认为./test/*.{js,cjs,mjs}, 不递归
};
