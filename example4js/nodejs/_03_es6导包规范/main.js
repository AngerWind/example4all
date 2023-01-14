/**
 * nodejs自诞生开始就支持commonjs导包规范, 在13.2之后才支持es6导包规范
 * 所以es6导包规范一直是一个试验性的功能
 * 如果需要开启es6导包规范, 需要在package.json中添加"type":"module"
 */

// commonjs规范需要添加.js后缀, 但是es6规范需要
//引入 m1.js 模块内容, 并作为m1变量的属性
import * as m1 from "./src/js/m1.js.js";
console(m1.school)
m1.teach()

// 引入 m2.js 模块内容
import * as m2 from "./src/js/m2.js.js";
console(m2.school)
m2.teach()

// 引入 m3.js, 因为m3.js使用的是默认暴露, 所以需要添加一个default属性来调用
import * as m3 from "./src/js/m3.js.js";
console(m3.default.school)
m3.default.teach()

//2. 解构赋值形式
// import {school, teach} from "./src/js/m1.js";
// console(school)
// teach.teach()

// import {school as guigu, findJob} from "./src/js/m2.js";
// console(guigu)
// teach.teach()

// import {default as m3} from "./src/js/m3.js";
// console.log(m3.school)
// m3.findJob

//3. 简便形式  针对默认暴露
// import m3 from "./src/js/m3.js";
// console.log(m3.school)
// m3.findJob