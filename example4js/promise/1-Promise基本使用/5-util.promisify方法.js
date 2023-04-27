/**
 * util.promisify 方法接收一个函数对象
 *      并且该函数对象的最后一个参数是(err, value) => ...  形式的
 *      列如fs.readFile
 */

// Promise风格转变!!!!

//引入 util 模块
const util = require("util");
//引入 fs 模块
const fs = require("fs");
const { rejects } = require("assert");
const { resolve } = require("path");
//返回一个新的函数
let mineReadFile = util.promisify(fs.readFile);

mineReadFile("./resource/content.txt").then((value) => {
  console.log(value.toString());
});

// mineReadFile类似这样
function mineReadFile(...args) {
  return new Promise((resolve, rejects) => {
    fs.readFile(...args, (err, value) => {
      if (err != null) {
        rejects(err);
      } else {
        resolve(value);
      }
    });
  });
}
