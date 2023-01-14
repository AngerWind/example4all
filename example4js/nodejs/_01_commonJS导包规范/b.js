function test() {
    console.log("test-b.file")
}

function upper(str) {
    console.log(str.toUpperCase());
}

console.log("running in b.file") // require当前文件的时候会执行这段代码

var a = require("./a") // 如果之前require过了a.js, 这次就不会在执行a.js了, 会直接获得导出的对象
a()


// module.export不能出现多次, 因为会覆盖
// 如果需要一次性导出多个对象, 可以将这些对象打包为一个对象
// module.exports = {
//     test: test,
//     upper: upper
// }

// 上面的方法可以使用es6的对象简写形式
// module.exports = {
//     test,
//     upper
// }

// 还可以使用下面的方法分别导出对象, 但是形参的效果是一样的, 多个对象最终会打包在一起
exports.test = test;
exports.upper = upper;