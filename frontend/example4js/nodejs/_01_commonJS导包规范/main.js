// commonJS规范是nodejs中的一套导包的语法

/**
 * 1. 导入使用require导入js文件, 文件名后缀可以省略
 * 2. 在导入js的时候, 会执行a.js中的代码, 所以可以在其中设置一些初始化的代码
 * 3. 使用require导入获得的变量就是js文件中使用导出语法导出的变量
 *
 * 如果在b.js中导入a.js, 然后再main.js中先后导入a.js, b.js, 使用node main.js运行程序
 * 程序碰到require a的时候会执行a.js, 碰到require b的时候执行b.js
 * 但是在执行b.js的时候碰到require a就不会执行a.js了
 *
 * 很奇怪的问题, 猜测是程序保存了a.js导出的对象, 碰到后续的require a的导包就直接返回了
 */

// 因为module.export后面是一个test()函数, 所以这里的a就等于test函数
var a = require("./a.js");
a(); // test-a.file

// 这里的b导出的是一个对象, 这个对象打包了b.js中导出的所有对象
var b = require("./b");
b.test();
b.upper("aaa");
