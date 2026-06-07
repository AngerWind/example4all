# TypeScript 与构建工具

## 1. tsc 和构建工具的分工

TypeScript 项目通常有两类任务：

- 类型检查：确认类型是否正确。
- 构建输出：把源码转换成浏览器或 Node.js 可运行的代码。

`tsc` 可以同时做这两件事，但现代前端项目通常让构建工具负责输出，让 `tsc --noEmit` 负责类型检查。

```bash
npx tsc --noEmit
```

## 2. 只用 tsc 构建

适合 Node.js 工具、简单库、小型项目。

`tsconfig.json`：

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "CommonJS",
    "rootDir": "src",
    "outDir": "dist",
    "strict": true,
    "declaration": true
  },
  "include": ["src"]
}
```

命令：

```bash
npx tsc
```

输出：

```text
src/index.ts -> dist/index.js
src/index.ts -> dist/index.d.ts
```

## 3. Webpack + TypeScript

硅谷教程中给出了 Webpack + `ts-loader` 的方案。现代 Webpack 写法大致如下：

```bash
npm install -D webpack webpack-cli webpack-dev-server typescript ts-loader html-webpack-plugin clean-webpack-plugin
```

`webpack.config.js`：

```js
const path = require("path");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");

module.exports = {
  mode: "development",
  entry: "./src/index.ts",
  devtool: "inline-source-map",
  output: {
    path: path.resolve(__dirname, "dist"),
    filename: "bundle.js",
    clean: true
  },
  resolve: {
    extensions: [".ts", ".tsx", ".js"]
  },
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        use: "ts-loader",
        exclude: /node_modules/
      }
    ]
  },
  plugins: [
    new CleanWebpackPlugin(),
    new HtmlWebpackPlugin({
      template: "./src/index.html"
    })
  ],
  devServer: {
    static: "./dist"
  }
};
```

教程旧配置里的 `devServer.contentBase` 是 Webpack 4 时代写法，Webpack 5 中应使用 `devServer.static`。

## 4. ts-loader 和 Babel

`ts-loader` 会调用 TypeScript 编译器，能做类型检查和转译。也可以开启 `transpileOnly` 提升速度，再用独立命令做类型检查：

```js
{
  loader: "ts-loader",
  options: {
    transpileOnly: true
  }
}
```

Babel 也能通过 `@babel/preset-typescript` 转译 TS：

```bash
npm install -D @babel/core @babel/preset-env @babel/preset-typescript babel-loader core-js
```

但 Babel 只擦除类型并转译语法，不负责 TypeScript 类型检查。因此仍需要：

```bash
npx tsc --noEmit
```

## 5. Webpack + Babel + TS

常见 loader：

```js
module.exports = {
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        use: {
          loader: "babel-loader",
          options: {
            presets: [
              [
                "@babel/preset-env",
                {
                  targets: {
                    chrome: "90"
                  },
                  useBuiltIns: "usage",
                  corejs: "3"
                }
              ],
              "@babel/preset-typescript"
            ]
          }
        },
        exclude: /node_modules/
      }
    ]
  }
};
```

如果使用 `babel-loader` 处理 TS，要明确团队用什么方式做类型检查。

## 6. Vite + TypeScript

现代前端项目更常用 Vite。创建项目：

```bash
npm create vite@latest my-app
```

Vite 使用 esbuild 快速转译 TypeScript，但默认不做完整类型检查。生产检查仍应加：

```json
{
  "scripts": {
    "typecheck": "tsc --noEmit",
    "build": "npm run typecheck && vite build"
  }
}
```

Vite 项目中 `moduleResolution` 常见为 `bundler`，并搭配 `module: "ESNext"`。

## 7. esbuild、swc 和 tsup

很多工具使用 esbuild 或 swc 快速处理 TS：

- Vite：开发阶段大量使用 esbuild。
- tsup：基于 esbuild 的库打包工具。
- swc：Rust 实现的快速转译器。

它们通常只负责转译，不等价于完整类型检查。项目仍应保留：

```bash
npx tsc --noEmit
```

库项目常见 `tsup` 配置：

```bash
npm install -D tsup typescript
```

```json
{
  "scripts": {
    "build": "tsup src/index.ts --format esm,cjs --dts",
    "typecheck": "tsc --noEmit"
  }
}
```

## 8. sourceMap

开发环境建议开启 sourcemap：

```json
{
  "compilerOptions": {
    "sourceMap": true
  }
}
```

构建工具里也可配置：

```js
module.exports = {
  devtool: "source-map"
};
```

这样浏览器调试时能定位回 `.ts` 源码。

## 9. polyfill 和语法降级

TypeScript 的 `target` 主要控制语法降级，不会自动补齐运行时 API。

例如你把 `target` 设置为 `ES5`，不代表旧浏览器就自动支持 `Promise`、`Array.prototype.includes`。

如果需要兼容旧环境，要用 Babel + `core-js` 或框架推荐的 polyfill 方案。

硅谷教程中 Babel 配置里的：

```js
{
  useBuiltIns: "usage",
  corejs: "3"
}
```

就是为此服务。

## 10. 推荐构建流程

应用项目：

```json
{
  "scripts": {
    "dev": "vite",
    "typecheck": "tsc --noEmit",
    "build": "npm run typecheck && vite build"
  }
}
```

库项目：

```json
{
  "scripts": {
    "typecheck": "tsc --noEmit",
    "build": "npm run typecheck && tsup src/index.ts --format esm,cjs --dts"
  }
}
```

Node 项目：

```json
{
  "scripts": {
    "dev": "tsx src/index.ts",
    "typecheck": "tsc --noEmit",
    "build": "tsc",
    "start": "node dist/index.js"
  }
}
```

## 11. 本章融合来源

本章融合了：

- `typescript_guigu/js-ts.md` 中的 Webpack、Babel、包说明。
- `typescript_guigu/chapter01/part3`、`chapter02/part2` 中的 Webpack 项目结构。
- `typescript黑马/day-04` 中的第三方库、声明文件、CRA `tsconfig` 配置。
- 现代 TypeScript 项目中 `tsc --noEmit` 与构建工具分工的实践。
