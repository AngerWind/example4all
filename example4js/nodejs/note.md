#### node如何解析import和require中模块的路径
- require相对导入, 例如`require("./moduleB"); `

  1. 先按照相对路径去查找对应的js文件, 有就导入
  2. 如果发现对应路径是目录
     1. 如果目录包含package.json且其中指定了main属性, 就导入main属性指定的js文件
     2. 如果没有main属性或者没有package.json, 那么默认查找index.js
     3. 如果还没有报错

- require绝对导入, 例如`require("react")`

  1. 直接去根目录下的`node_module/react`, 按照相对导入的条件2去查找

- import相对导入

  与require相对导入类似,只是条件2会去找module属性指定的js文件,而不是main

- import绝对导入

  同样与require绝对导入只有main和module的区别


#### es6和commonjs的导入导出语法
https://juejin.cn/post/6907428111332147208
1. es6中的导入导出
   导出
   ~~~js
        export let test = "hello" // 单独导出
        export function f(){return "xxx"} // 单独导出
        let a = 1;
        let b = 2;
        export {a, b} // 一次性导出多个变量
        let d1 = 1;
        let d2 = 2;
        export default {d1, d2} // 默认导出
   ~~~
   导入
   ~~~js
        import * as m from "./m.js"

        import def from "./m.js" 
        console.log(m.f(), m.a, m.b, m.test) // import * 会将所有export的变量都被打包在一个对象中
        console.log(m.default.d1, m.default.d2) // import * 会将export default的变量被打包在default属性中

        
        console.log(def.d1, def.d2) // 直接使用import会将所有export default的变量打包

        export * from "./m.js" // 导出重定向, 从当前文件导出所有非默认导出内容
        // 导出重定向不会将变量导入到当前文件内容
   ~~~  
2. commonjs中的导入导出
   导出
   ~~~js
        // 导入一个js模块, 就是导入了他的module.export对象
        // 所以要导出变量, 只需要将他附加到module.export对象上面就好了
        // 同时export是module.export的一个引用
        // 所以将变量附加到export上也是可以的
        let a = 1;
        let b = 2;
        module.export.a = a // 使用module.export
        export.b = b // 使用export
        // export = {a, b} // 千万不能使用这种方式, 因为export只是module.export的一个引用, 修改export引用并不能修改module.export

        module.export = {a, b} // 这种属于一次性导出, 修改了module.export将会导致上面的导出无效
   ~~~
   导入
   ~~~js
    const m = require("./m.js") 
    // 将会获得在m.js中的module.export对象
    // 类似于m = module.export
   ~~~
3. 在commonjs中引入es module(一般很少这种情况)
   由于es module在加载, 解析, 执行都是异步的, 而require的过程是同步的, 所以不能通过require()来引用一个es6模块
   es6提议使用import()函数来导入一个模块, import函数会返回一个Promis, 他在es module 加载后标记完成
   ~~~js
   // 使用 then() 来进行模块导入后的操作
   import(“es6-modules.mjs”).then((module)=>{/*…*/}).catch((err)=>{/**…*/})
   // 或者使用 async 函数
   (async () => {
   await import('./es6-modules.mjs');
   })();
   ~~~
4. 在es module文件中导入commonjs模块
   在es6模块里可以很方便的使用import来引入一个CommonJS模块, 因为在es6模块里异步加载并非是必须的
   ~~~js
   import { default as cjs } from 'cjs';

   // The following import statement is "syntax sugar" (equivalent but sweeter)
   // for `{ default as cjsSugar }` in the above import statement:
   import cjsSugar from 'cjs';

   console.log(cjs);
   console.log(cjs === cjsSugar);
   ~~~

#### 两者在执行上的不同
https://juejin.cn/post/6994224541312483336


1. require就像是执行代码, 所以可以在任何地方使用, 而且是运行时加载, 
   在第一次加载该文件的时候会执行该文件, 并生成他的module.export对象, 下次任何文件再导入他的时候, 都会直接获得module.export对象
   并且require对于暴露的module.export对象进行的是**值拷贝**, 所以对于基础类型, 无法获取后续的变化
2. es6中的导入导出是静态的, import会自动提升到代码的顶层, import和export不能放在块级作用域或者条件语句中
   因为导入导出是在编译过程中就确定的, 所以更方便查找依赖, 可以使用lint工具对模块依赖进行检查, 可以对导入导出加上类型信息进行静态的类型检查

3. es6中使用import导入的变量是只读的, 不能进行修改, 可以理解为被const修饰了
   并且import导入的变量无论是否是基础变量, 他都将是引用, 否则下面的num输出将不会是2
   ~~~js
   // a.js
   export let num = 1
   export const addNumber = ()=>{
    num++
   }
   // main.js
   import {  num , addNumber } from './a'
   // num = 2 // 该语句报错, num is read-only, 可以理解 为被const修饰了
   // 但是可以通过函数修改
   addNumber() 
   console.log(num) // 2, 
   ~~~


   #### package.json中的属性
   - main:
      在使用commonjs导包规范时使用的模块入口文件
   - type:
      定义当前项目使用的是什么导包规范, 默认情况下认为js使用的是commonjs的规范, 
      如果要在js中使用es6的导包规范, 需要将type设置为module, 或者将文件的后缀名该为mjs, 则会强制使用es6规范
   - module:
      配置在使用es6规范时, 模块的入口文件
   - types:
      该属性是typescript使用的, 用于配置模块入口文件
   - browser:
      与main, type, types是一样的, 但是是在web端使用的
   - exports
      用来定义各种环境下模块的入口文件, 是main, module, browser的统一配置项, 并且当他存在时, 他的**优先级是最高的**
      ~~~json
      "exports": {
         "require": "./index.js", // commonjs
         "import": "./index.mjs", // es6
         "browser": "./index.mjs", // web
         "node": "./index.js" // node(c++扩展二进制模块)
      }
      ~~~
      上方的写法其实等同于：
      ~~~json
      "exports": {
         ".": {
            "require": "./index.js",
            "import": "./index.mjs"
         }
      }
      ~~~
      为什么要加一个层级，把 require 和 import 放在 "." 下面呢？
      因为 exports 除了支持配置包的默认导出，还支持配置包的子路径。
      比如一些第三方 UI 包需要引入对应的样式文件才能正常使用。
      ```js
      import `packageA/dist/css/index.css`;
      ```
      我们可以使用 exports 来封装文件路径：
      ```js
      "exports": {
        "./style": "./dist/css/index.css'
      },
      ```
      用户引入时只需：
      ```js
      import `packageA/style`;
      ```
      除了对导出的文件路径进行封装，**exports 还限制了使用者不可以访问未在"exports" 中定义的任何其他路径。**

      比如发布的 dist 文件里有一些内部模块 `dist/internal/module` ，被用户单独引入使用的话可能会导致主模块不可用。为了限制外部的使用，我们可以不在exports 定义这些模块的路径，这样外部引入 `packageA/dist/internal/module`模块的话就会报错。
- scripts
   指定项目的一些内置脚本命令, 这些命令可以同归npm run来执行
   ```json
   "scripts": {
      "build": "webpack"
   }
   ```
- config
   用于指定项目的一些配置, 比如设置port
   ```json
   "config": { 
      "port": "3001"
    }
   ```
   在js中可以通过process.env.npm_package_config_xxx来访问
   ~~~js
   console.log(process.env.npm_package_config_port); // 3001
   ~~~



   