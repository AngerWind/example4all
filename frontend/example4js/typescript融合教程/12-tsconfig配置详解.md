# tsconfig 配置详解

## 1. tsconfig.json 的作用

`tsconfig.json` 用来告诉 TypeScript：

- 哪些文件需要被纳入项目。
- 使用哪些类型检查规则。
- 编译成什么版本和模块格式的 JavaScript。
- 如何解析模块、JSX、声明文件、路径别名等。

项目根目录有 `tsconfig.json` 后，执行：

```bash
npx tsc
```

TypeScript 会按该配置检查整个项目。

## 2. 顶层配置

`include`：指定要包含的文件：

```json
{
  "include": ["src/**/*", "tests/**/*"]
}
```

`exclude`：排除文件：

```json
{
  "exclude": ["node_modules", "dist"]
}
```

`files`：只包含明确列出的文件，适合很小的项目：

```json
{
  "files": ["src/main.ts"]
}
```

`extends`：继承其他配置：

```json
{
  "extends": "./tsconfig.base.json",
  "include": ["src"]
}
```

## 3. target 和 lib

`target` 决定输出 JavaScript 的语法版本：

```json
{
  "compilerOptions": {
    "target": "ES2020"
  }
}
```

`lib` 决定类型系统能看到哪些内置 API：

```json
{
  "compilerOptions": {
    "lib": ["DOM", "DOM.Iterable", "ES2022"]
  }
}
```

例如浏览器项目需要 `DOM`，Node 项目通常不需要 DOM，但需要 `@types/node`。

## 4. module 和 moduleResolution

`module` 决定输出模块格式：

```json
{
  "compilerOptions": {
    "module": "ESNext"
  }
}
```

常见选择：

- 浏览器打包项目：`ESNext`。
- Node CommonJS 项目：`CommonJS`。
- Node ESM 项目：`NodeNext`。

`moduleResolution` 决定模块查找策略：

```json
{
  "compilerOptions": {
    "moduleResolution": "bundler"
  }
}
```

常见选择：

- Vite/Rollup/Webpack：`bundler` 或模板默认。
- Node ESM/CJS：`node16` 或 `nodenext`。
- 老项目：`node`。

## 5. 输出相关配置

`outDir`：输出目录：

```json
{
  "compilerOptions": {
    "outDir": "dist"
  }
}
```

`rootDir`：源码根目录：

```json
{
  "compilerOptions": {
    "rootDir": "src",
    "outDir": "dist"
  }
}
```

`sourceMap`：生成 sourcemap：

```json
{
  "compilerOptions": {
    "sourceMap": true
  }
}
```

`declaration`：生成 `.d.ts`：

```json
{
  "compilerOptions": {
    "declaration": true
  }
}
```

`noEmit`：只检查，不输出：

```json
{
  "compilerOptions": {
    "noEmit": true
  }
}
```

`noEmitOnError`：类型检查未通过时不输出：

```json
{
  "compilerOptions": {
    "noEmitOnError": true
  }
}
```

## 6. strict

`strict` 是严格类型检查总开关：

```json
{
  "compilerOptions": {
    "strict": true
  }
}
```

它会启用一组严格规则，其中最重要的是：

- `noImplicitAny`
- `strictNullChecks`
- `strictFunctionTypes`
- `strictBindCallApply`
- `strictPropertyInitialization`
- `noImplicitThis`
- `alwaysStrict`
- `useUnknownInCatchVariables`

现代 TypeScript 项目建议开启 `strict`。

## 7. noImplicitAny

禁止隐式 `any`：

```ts
function fn(s) {
  console.log(s.subtr(3));
}
```

开启后，参数 `s` 需要明确类型：

```ts
function fn(s: string) {
  console.log(s.substring(3));
}
```

## 8. strictNullChecks

开启后，`null` 和 `undefined` 是独立类型：

```ts
function findUser(): { name: string } | undefined {
  return undefined;
}

const user = findUser();
// user.name; // 报错

if (user) {
  console.log(user.name);
}
```

开启 `strictNullChecks` 后，函数和变量都要明确处理 `null` 与 `undefined`。

## 9. strictPropertyInitialization

检查类属性是否初始化：

```ts
class User {
  name: string;

  constructor(name: string) {
    this.name = name;
  }
}
```

如果属性可能不存在，应显式写成联合类型：

```ts
class User {
  email: string | undefined;
}
```

## 10. 额外质量检查

这些不都属于 `strict`，但很实用：

```json
{
  "compilerOptions": {
    "noFallthroughCasesInSwitch": true,
    "noImplicitReturns": true,
    "noUnusedLocals": true,
    "noUnusedParameters": true,
    "noImplicitOverride": true,
    "noUncheckedIndexedAccess": true,
    "exactOptionalPropertyTypes": true
  }
}
```

说明：

- `noFallthroughCasesInSwitch`：防止 `switch` 分支漏写 `break`。
- `noImplicitReturns`：要求函数的返回路径保持明确。
- `noUnusedLocals` / `noUnusedParameters`：检查未使用代码。
- `noImplicitOverride`：重写父类方法时要求写 `override`。
- `noUncheckedIndexedAccess`：索引访问结果自动包含 `undefined`，更安全。
- `exactOptionalPropertyTypes`：让可选属性语义更精确。

## 11. JS 项目相关

```json
{
  "compilerOptions": {
    "allowJs": true,
    "checkJs": true
  }
}
```

- `allowJs`：允许 JS 文件进入项目。
- `checkJs`：检查 JS 文件类型。

如果只是迁移项目，常配合：

```json
{
  "compilerOptions": {
    "allowJs": true,
    "checkJs": false,
    "noEmit": true
  }
}
```

## 12. JSX 配置

React 新 JSX 转换常用：

```json
{
  "compilerOptions": {
    "jsx": "react-jsx"
  }
}
```

前端项目使用 React JSX 时通常会配置这个选项。

## 13. 类型包控制

`types` 指定自动包含哪些全局类型包：

```json
{
  "compilerOptions": {
    "types": ["node"]
  }
}
```

`typeRoots` 指定类型包查找目录：

```json
{
  "compilerOptions": {
    "typeRoots": ["./types", "./node_modules/@types"]
  }
}
```

一般不需要手动配置 `typeRoots`，除非项目有特殊类型目录结构。

## 14. 推荐配置示例

浏览器打包项目：

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "ESNext",
    "moduleResolution": "bundler",
    "strict": true,
    "noEmit": true,
    "skipLibCheck": true,
    "jsx": "react-jsx"
  },
  "include": ["src"]
}
```

Node ESM 项目：

```json
{
  "compilerOptions": {
    "target": "ES2022",
    "module": "NodeNext",
    "moduleResolution": "NodeNext",
    "strict": true,
    "outDir": "dist",
    "declaration": true,
    "skipLibCheck": true
  },
  "include": ["src"]
}
```

库项目：

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "ESNext",
    "moduleResolution": "bundler",
    "strict": true,
    "declaration": true,
    "emitDeclarationOnly": false,
    "outDir": "dist",
    "skipLibCheck": true
  },
  "include": ["src"]
}
```
