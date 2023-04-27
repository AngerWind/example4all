export let test = "hello"; // 单独导出
export function f() {
  return "xxx";
} // 单独导出
let a = 1;
let b = 2;
export { a, b }; // 一次性导出多个变量
let d1 = 1;
let d2 = 2;
export default { d1, d2 }; // 默认导出

export let num = 1
   export const addNumber = ()=>{
    num++
  }
