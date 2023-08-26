// import * as m from "./test.js";

// import def from "./test.js";
// console.log(m.f(), m.a, m.b, m.test); // import * 会将所有export的变量都被打包在一个对象中
// console.log(m.default.d1, m.default.d2); // import * 会将export default的变量被打包在default属性中

// console.log(def.d1, def.d2); // 直接使用import会将所有export default的变量打包

// export { f } from "./test.js";
// f();

import {  num , addNumber } from './test.js'
//   num = 2 // 该语句报错, num is read-only, 可以理解为被const修饰了
  addNumber() // 但是可以通过函数修改
console.log(num)