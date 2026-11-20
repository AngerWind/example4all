### 共享配置（Sharing configurations）

> 原文链接：https://prettier.io/docs/sharing-configurations

如果你有许多不同的项目，拥有一个可以在所有项目中使用的**共享配置**会很有帮助，而不必为每个项目复制粘贴相同的配置。

本页介绍如何创建、发布和使用可共享配置。

#### 创建可共享配置（Creating a Shareable Config）

可共享配置就是导出单个 [Prettier 配置文件](https://prettier.io/docs/configuration)的 [npm 包](https://docs.npmjs.com/about-packages-and-modules#about-packages)。

在开始之前，请确保你有：

- 一个用于发布包的 [npmjs.com](https://www.npmjs.com/) 账号
- 对[如何创建 Node.js 模块](https://docs.npmjs.com/creating-node-js-modules)的基本了解

首先，创建一个新包。我们建议创建一个名为 `@username/prettier-config` 的[作用域包](https://docs.npmjs.com/cli/v10/using-npm/scope)。

一个最小的包至少应该有两个文件：用于包配置的 `package.json` 和存放共享 Prettier 配置对象的 `index.js`：

```text
prettier-config/
├── index.js
└── package.json
```

示例 `package.json`：

```json title="package.json"
{
  "name": "@username/prettier-config",
  "version": "1.0.0",
  "description": "My personal Prettier config",
  "type": "module",
  "exports": "./index.js",
  "license": "MIT",
  "publishConfig": {
    "access": "public"
  },
  "peerDependencies": {
    "prettier": ">=3.0.0"
  },
  "devDependencies": {
    "prettier": "3.6.2"
  }
}
```

`index.js` 是你放置共享配置的地方。这个文件只是用相同的语法和相同的选项导出一个[常规的 Prettier 配置](https://prettier.io/docs/configuration)：

```js title="index.js"
/**
 * @see https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
const config = {
  trailingComma: "es5",
  tabWidth: 4,
  singleQuote: true,
};

export default config;
```

一个共享配置仓库的示例可在[这里](https://github.com/azz/prettier-config)找到。

#### 发布可共享配置（Publishing a Shareable Config）

准备好后，你可以[将你的包发布到 npm](https://docs.npmjs.com/creating-and-publishing-scoped-public-packages#publishing-scoped-public-packages)：

```bash
npm publish
```

#### 使用可共享配置（Using a Shareable Config）

你首先需要安装已发布的配置，例如：

```bash
# npm
npm install --save-dev @username/prettier-config

# yarn
yarn add --dev @username/prettier-config

# pnpm
pnpm add --save-dev @username/prettier-config

# bun
bun add --dev @username/prettier-config

# deno
deno install --dev @username/prettier-config
```

然后，你可以在你的 `package.json` 中引用它：

```json title="package.json"
{
  "name": "my-cool-library",
  "version": "1.0.0",
  "prettier": "@username/prettier-config"
}
```

如果你不想使用 `package.json`，可以使用任何受支持的扩展名来导出一个字符串，例如 `.prettierrc`：

```json title=".prettierrc"
"@company/prettier-config"
```

##### 扩展可共享配置（Extending a Shareable Config）

要*扩展*配置以覆盖共享配置中的某些属性，在 `prettier.config.mjs` 文件中导入该文件并导出修改后的内容，例如：

```js title="prettier.config.mjs"
import usernamePrettierConfig from "@username/prettier-config";

/**
 * @see https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
const config = {
  ...usernamePrettierConfig,
  semi: false,
};

export default config;
```

#### 其他示例（Other examples）

##### 在可共享配置中包含插件（Include Plugins in Shareable Configurations）

如果你想在共享配置中使用[插件](https://prettier.io/docs/plugins)，需要在配置文件的 `plugin` 数组中声明这些插件，并在 `package.json` 中将它们声明为 `dependencies`：

```js title="index.js"
import * as prettierPluginOxc from "@prettier/plugin-oxc";

/**
 * @see https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
const config = {
  singleQuote: true,
  plugins: [prettierPluginOxc],
};

export default config;
```

```diff title="package.json"
{
  "name": "@username/prettier-config",
  "version": "1.0.0",
  "description": "My personal Prettier config",
  "type": "module",
  "exports": "./index.js",
  "license": "MIT",
  "publishConfig": {
    "access": "public"
  },
+  "dependencies": {
+    "@prettier/plugin-oxc": "0.1.2"
+  },
  "peerDependencies": {
    "prettier": ">=3.0.0"
  },
  "devDependencies": {
    "prettier": "3.6.2"
  }
}
```

一个示例仓库可在[这里](https://github.com/kachkaev/routine-npm-packages/tree/bc3e658f88c0b41beb118c7a1b9b91ec647f8478/packages/prettier-config)找到。

**注意：**你可以使用 [`peerDependencies`](https://docs.npmjs.com/cli/v10/configuring-npm/package-json#peerdependencies) 代替 [`dependencies`](https://docs.npmjs.com/cli/v10/configuring-npm/package-json#dependencies)。要了解它们的区别，可以阅读 [Domenic Denicola 关于 peer dependencies 的这篇博客文章](https://nodejs.org/en/blog/npm/peer-dependencies)。
