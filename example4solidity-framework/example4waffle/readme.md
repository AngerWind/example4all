#### waffle的主要功能
1. 通过yarn waffle 对合约进行编译
2. 通过yarn waffle flatten对合约进行扁平化
3. 集成了mocha, chai, 并且添加了针对合约的chai插件
4. 添加了fixture
5. 可以自己创建mock合约, 然后指定合约的返回值, 我们的合约再与mock合约进行交互 (没什么用)

#### waffle教程
> waffle的项目框架构建过程(TS)
1. 安装依赖包
   ~~~json
   "devDependencies": {
    "@openzeppelin/contracts": "^4.8.3",
    "@types/chai": "^4.3.5",
    "@types/mocha": "^10.0.1",
    "chai": "^4.3.7",
    "ethereum-waffle": "^4.0.10",
    "mocha": "^10.2.0"
   },
   "dependencies": {
    "ethers": "^5.7.2"
   }
   ~~~
2. `tsc --init`初始化ts项目, 并且设置`esModuleInterop`和`resolveJsonModule`为`true`
   resolveJsonModule是为了能够在ts中导入json
   esModuleInterop是因为ems和cjs中的default概念不同而导入的, 不需要管

3. 在项目根目录下创建waffle的配置文件`waffle.json`, 内容如下
   ~~~json
   {
   "compilerType": "solcjs",
   "compilerVersion": "0.8.13", // 编译版本
   "sourceDirectory": "./contracts", // 合约源码文件的位置
   "outputDirectory": "./build", // 编译后的输出位置
   "flattenOutputDirectory": "./custom_flatten" // flatten后的输出位置
   }
   ~~~
4. 在项目根目录下创建mocha的配置文件`.mocharc.js`, 内容如下:
   ~~~js
   // mocha的配置文件
    module.exports = {
        require: ["ts-node/register"], // 在执行测试之前, 先加载这些模块, 该模块用于ts执行mocha测试
        spec: "test/**/*.test.{ts, js, cjs, mjs}", // 指定测试文件的路径, 默认为./test/*.{js,cjs,mjs}, 不递归
    };
   ~~~

> waffle编译合约
1. 在项目根目录下创建`contracts/BasicToken.sol`, 内容如下: 
   ```sol
   // SPDX-License-Identifier: MIT
   pragma solidity ^0.8.0;

   import "@openzeppelin/contracts/token/ERC20/ERC20.sol";

   // Example class - a mock class using delivering from ERC20
   contract BasicToken is ERC20 {
        constructor(uint256 initialBalance) ERC20("Basic", "BSC") {
      _mint(msg.sender, initialBalance);
    }
   }
   ```
2. 使用`yarn waffle`编译合约
   或者将其添加到package.json中, 在waffle.json中配置了合约源文件的位置
   ~~~json
   {
    "scripts": {
        "build": "waffle"
    }
   }
   ~~~
3. 使用`yarn waffle flatten`将所有合约打包到一个文件中, 在waffle.json中配置了输出路径

> waffle执行测试
1. 在`./test`目录下创建`_01_BasicToken.test.ts`测试文件
2. 因为mocha默认不会执行指定目录下的ts测试文件, 所以需要配置mocha, 配置文件如第一部分
3. 在`package.json`中添加script
   ```json
   {
    "scripts": {
        "test": "NODE_ENV=test mocha", // 先将环境设置为test, 然后执行mocha来执行测试文件
    }
   }
   ```
   之后执行`yarn test`来执行测试