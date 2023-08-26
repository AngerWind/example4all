/**
 * nodejs自诞生开始就支持commonjs导包规范, 在13.2之后才支持es6导包规范
 * 所以es6导包规范一直是一个试验性的功能
 * 如果需要开启es6导包规范, 需要在package.json中添加"type":"module"
 */

// commonjs规范需要添加.js后缀, 但是es6规范需要
//引入 m1.js 模块内容, 并作为m1变量的属性
// import * as m1 from "./m1.js";
// console.log(m1.school);
// m1.teach();

// // 引入 m2.js 模块内容
import * as m2 from "./m2.js";
console.log(m2.school);
m2.findJob();
console.log(m2.h)

// // 引入 m3.js, 因为m3.js使用的是默认暴露, 所以需要添加一个default属性来调用
// import * as m3 from "./m3.js";
// m3.default.change();
// console.log(m3.h);
// console.log(m3.default.school);

//2. 解构赋值形式
// import { school, teach } from "./m1.js";
// console.log(school);
// teach();

// import { school as guigu, findJob } from "./m2.js";
// console.log(guigu);
// findJob();

// import { default as m3, h } from "./m3.js";
// console.log(m3.school);
// m3.change;
// console.log(h);

//3. 默认暴露
// import m3 from "./m3.js"; // 与 import {default as m3} from "./m3.js"同效
// console.log(m3.school);
// m3.change();

// 默认暴露和非默认暴露一起导出
// import m3, {h} from "./m3.js";

// 导出重定向
// export * from "./m3.js"; // 从当前文件导出所有的非默认暴露的内容, 不包括module.exports.default
export { h } from "./m3.js"; // 从当前文件导出m3中的h属性
// console.log(h) // 该语句报错, 导出重定向的变量无法在当前文件中使用
