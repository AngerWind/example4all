## 共享配置（Share Configurations）

> 原文链接：https://eslint.org/docs/latest/extend/shareable-configs

要分享你的 ESLint 配置，请创建一个**可共享配置（shareable config）**。你可以将可共享配置发布到 [npm](https://www.npmjs.com/) 上，让其他人下载并在他们的 ESLint 项目中使用。

本页介绍如何创建和发布可共享配置。

::: tip
本页介绍如何使用 flat config 格式创建可共享配置。
:::

### 创建可共享配置（Create a Shareable Config）

可共享配置就是导出配置对象或数组的 npm 包。首先，像平常一样[创建一个 Node.js 模块](https://docs.npmjs.com/getting-started/creating-node-modules)。

虽然你可以随意命名这个包，但我们建议使用以下约定之一，以便你的包更容易被识别：

- 以 `eslint-config-` 开头，例如 `eslint-config-myconfig`。
- 对于 npm [作用域模块](https://docs.npmjs.com/misc/scope)，以 `@scope/eslint-config` 命名或作为前缀，例如 `@scope/eslint-config` 或 `@scope/eslint-config-myconfig`。

在你的模块中，从模块的 [`main`](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#main) 入口文件导出可共享配置。默认的 main 入口是 `index.js`。例如：

```js
// index.js
export default [
	{
		languageOptions: {
			globals: {
				MyGlobal: true,
			},
		},

		rules: {
			semi: [2, "always"],
		},
	},
];
```

因为 `index.js` 文件就是普通的 JavaScript，所以你可以从文件中读取这些设置，或者动态生成它们。

::: tip
大多数情况下，你会想从可共享配置中导出一个配置对象数组。不过，你也可以导出单个配置对象。请确保你的文档清楚地展示了如何在 `eslint.config.js` 文件中使用你的可共享配置的示例，以避免用户困惑。
:::

### 发布可共享配置（Publishing a Shareable Config）

当你的可共享配置准备好后，你可以[将其发布到 npm](https://docs.npmjs.com/getting-started/publishing-npm-packages) 与他人分享。我们建议在 `package.json` 文件中使用 `eslint` 和 `eslintconfig` [keywords](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#keywords)，以便其他人可以轻松找到你的模块。

你应该在 `package.json` 中使用 [peerDependencies](https://docs.npmjs.com/files/package.json#peerdependencies) 字段声明对 ESLint 的依赖。为了实现面向未来的兼容性，推荐的依赖声明方式是使用 ">=" 范围语法，并指定所需的最低 ESLint 版本。例如：

```json
{
	"peerDependencies": {
		"eslint": ">=10.0.0"
	}
}
```

如果你的可共享配置依赖于某个插件或自定义解析器，你应该在 `package.json` 中将这些包指定为 `dependencies`。

### 使用可共享配置（Use a Shareable Config）

要使用可共享配置，在 `eslint.config.js` 文件中导入该包，并使用 `extends` 将其添加到导出的数组中，如下所示：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";
import myconfig from "eslint-config-myconfig";

export default defineConfig([
	{
		files: ["**/*.js"],
		extends: [myconfig],
	},
]);
```

::: warning
可共享配置无法与 ESLint CLI 的 [`--config`](https://eslint.org/docs/latest/use/command-line-interface#-c---config) 标志一起使用。
:::

#### 覆盖可共享配置中的设置（Overriding Settings from Shareable Configs）

你可以在导入可共享配置之后，直接在 `eslint.config.js` 文件中添加设置来覆盖可共享配置中的设置。例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";
import myconfig from "eslint-config-myconfig";

export default defineConfig([
	{
		files: ["**/*.js"],
		extends: [myconfig],

		// 从这里开始的任何内容都会覆盖 myconfig
		rules: {
			"no-unused-vars": "warn",
		},
	},
]);
```

### 共享多个配置（Sharing Multiple Configs）

因为可共享配置就是 npm 包，所以你可以从同一个包中导出任意多个配置。除了在 `package.json` 中使用 `main` 入口指定默认配置外，你还可以通过在 npm 包中添加新文件，然后在 `eslint.config.js` 文件中引用它，来指定额外的可共享配置。

例如，你可以在 npm 包的根目录创建一个名为 `my-special-config.js` 的文件并导出一个配置，例如：

```js
// my-special-config.js
export default {
	rules: {
		quotes: [2, "double"],
	},
};
```

然后，假设你使用的包名是 `eslint-config-myconfig`，你可以通过以下方式访问这个额外的配置：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";
import myconfig from "eslint-config-myconfig";
import mySpecialConfig from "eslint-config-myconfig/my-special-config.js";

export default defineConfig([
	{
		files: ["**/*.js"],
		extends: [myconfig, mySpecialConfig],

		// 从这里开始的任何内容都会覆盖 myconfig
		rules: {
			"no-unused-vars": "warn",
		},
	},
]);
```

::: important
我们强烈建议始终为你的包提供一个默认导出，以避免混淆。
:::

### 延伸阅读（Further Reading）

- [npm 开发者指南](https://docs.npmjs.com/misc/developers)
