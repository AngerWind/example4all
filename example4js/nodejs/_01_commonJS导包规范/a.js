function test() {
    console.log("test-a.file")
}

console.log("running in a.file") // require当前文件的时候会执行这段代码
// 导出当前文件中的test函数
module.exports = test