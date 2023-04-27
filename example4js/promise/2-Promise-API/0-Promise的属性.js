/**
 * PromiseState
 *      Promise 的状态实例对象中的一个属性
 *      pending  未决定的
 *      resolved / fullfilled  成功
 *      rejected  失败
 * PromiseResult:
 *      Promise实例对象中的另一个属性
 *      保存着异步任务『成功/失败』的结果
 *
 *      resolve函数和reject函数可以PromiseResult进行修改
 *      在后续的then方法中可以根据PromiseResult调用不同的回调函数
 */

console.log(
  new Promise((resolve, reject) => {
    reject("erro");
  }).catch((reason) => {
    console.log(reason);
  })
);
