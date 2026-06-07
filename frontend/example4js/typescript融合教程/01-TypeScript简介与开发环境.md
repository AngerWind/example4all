# TypeScript 简介与开发环境

## 1. TypeScript 是什么

TypeScript 是 JavaScript 的超集。能在 JavaScript 中运行的大部分语法，也能写在 TypeScript 中；TypeScript 额外提供了静态类型系统、类型推断、接口、泛型、类型运算、声明文件等能力。

TypeScript 本身不会直接在浏览器或 Node.js 中执行。实际运行前通常要经过编译：

```text
TypeScript 源码 -> tsc / bundler 编译 -> JavaScript -> 浏览器或 Node.js 执行
```

学习 TypeScript 时要把两件事分清：

- 类型检查发生在开发期和编译期。
- 运行时执行的仍然是 JavaScript。

例如类型断言、接口、泛型约束大多不会生成运行时代码，它们主要帮助编辑器和编译器提前发现错误。

## 2. 为什么要使用 TypeScript

TypeScript 的核心价值不是“让代码更复杂”，而是让大型 JavaScript 项目更容易维护：

- 在编码阶段发现参数、属性、返回值等类型错误。
- 给编辑器提供更准确的补全、跳转、重构能力。
- 为函数、对象、模块和第三方库建立清晰契约。
- 让多人协作时减少“这个值到底是什么结构”的猜测。

一个典型例子：

```ts
function add(a: number, b: number): number {
  return a + b;
}

add(1, 2);
// add(1, "2"); // 报错：string 不能传给 number 参数
```

如果这段代码写成 JavaScript，`add(1, "2")` 会得到 `"12"`，这种行为可能不是业务想要的。

## 3. 安装开发环境

先安装 Node.js，然后安装 TypeScript：

```bash
npm install -g typescript
tsc --version
```

也可以不全局安装，在项目中安装：

```bash
npm install -D typescript
npx tsc --version
```

项目内安装更适合真实项目，因为不同项目可以锁定不同的 TypeScript 版本。

## 4. 编译第一个 TS 文件

创建 `hello.ts`：

```ts
const message: string = "Hello TypeScript";
console.log(message);
```

编译：

```bash
tsc hello.ts
node hello.js
```

如果希望监听单个文件：

```bash
tsc hello.ts --watch
```

如果希望监听整个项目，应创建 `tsconfig.json`，然后执行：

```bash
tsc --watch
```

## 5. 使用 ts-node 或 tsx

教程中提到 `ts-node`，它可以让 Node.js 项目直接运行 TypeScript 文件：

```bash
npm install -D ts-node typescript
npx ts-node src/index.ts
```

现代项目里也常用 `tsx`：

```bash
npm install -D tsx typescript
npx tsx src/index.ts
```

二者本质上都不是让 TypeScript 直接成为运行时语言，而是在运行前或运行过程中处理 TypeScript 到 JavaScript 的转换。

## 6. 创建 tsconfig.json

初始化配置：

```bash
npx tsc --init
```

一个适合学习阶段的最小配置：

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "ESNext",
    "strict": true,
    "noEmitOnError": true,
    "skipLibCheck": true
  },
  "include": ["src"]
}
```

常用命令：

```bash
npx tsc
npx tsc --noEmit
npx tsc --watch
```

`--noEmit` 表示只检查类型，不输出 JavaScript 文件，适合在已有构建工具的项目中使用。

## 7. 本章融合来源

本章融合了：

- `typescript_guigu/js-ts.md` 中的 TypeScript 简介、环境搭建、`ts-node` 使用。
- `typescript黑马/day-01/01-hello.ts`、`02-hello.ts`、`03-ts代码意外行为演示.ts` 中的入门示例。
- TypeScript 官方 Handbook 中关于基础类型检查和 `strict` 的说明。
