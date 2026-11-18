# ESLint 入门（Getting Started with ESLint）

> 原文链接：https://eslint.org/docs/latest/use/getting-started

ESLint 是一个用于识别和报告 ECMAScript/JavaScript 代码中模式的工具，其目标是让代码更加一致并避免 bug。

ESLint 是完全可插拔的。每一条规则都是一个插件，你可以在运行时添加更多规则。你也可以添加社区插件、配置和解析器来扩展 ESLint 的功能。

## 前提条件（Prerequisites）

要使用 ESLint，你必须安装 [Node.js](https://nodejs.org/en/)（`^20.19.0`、`^22.13.0` 或 `>=24`），并且构建时需要包含 SSL 和 ICU 支持。（如果你使用的是官方 Node.js 发行版，SSL 和 ICU 总是内置的。）

如果你使用 ESLint 的 TypeScript 类型定义，则需要 TypeScript 5.3 或更高版本。

## 快速开始（Quick start）

你可以使用以下命令安装并配置 ESLint：

```shell
npm init @eslint/config@latest
```

（yarn 用户：`yarn create @eslint/config`；pnpm 用户：`pnpm create @eslint/config@latest`）

如果你想使用托管在 npm 上的特定可共享配置，可以使用 `--config` 选项并指定包名：

```shell
# 使用 `eslint-config-xo` 共享配置 - 需要 npm 7+
npm init @eslint/config@latest -- --config eslint-config-xo
```

**注意：** `npm init @eslint/config` 假定你已经有一个 `package.json` 文件。如果没有，请确保事先运行 `npm init` 或 `yarn init`。

之后，你可以对任何文件或目录运行 ESLint，如下所示：

```shell
npx eslint yourfile.js
```

## 配置（Configuration）

**注意：** 如果你是从 9.0.0 之前的版本迁移过来的，请查看[迁移指南](https://eslint.org/docs/latest/use/configure/migration-guide)。

当你运行 `npm init @eslint/config` 时，会被询问一系列问题，以确定你如何使用 ESLint 以及应包含哪些选项。回答完这些问题后，你的目录中会创建一个 `eslint.config.js`（或 `eslint.config.mjs`）文件。

例如，其中一个问题是"你的代码运行在哪里？"如果你选择"Browser"，那么你的配置文件将包含 Web 浏览器中全局变量的定义。下面是一个示例：

```js
import { defineConfig } from "eslint/config";
import globals from "globals";
import js from "@eslint/js";

export default defineConfig([
	{ files: ["**/*.js"], languageOptions: { globals: globals.browser } },
	{ files: ["**/*.js"], plugins: { js }, extends: ["js/recommended"] },
]);
```

`"js/recommended"` 配置会确保[规则页面](https://eslint.org/docs/latest/rules)上所有标记为推荐（recommended）的规则都被启用。或者，你也可以在 [npmjs.com](https://www.npmjs.com/search?q=eslint-config) 上搜索 "eslint-config" 来使用其他人创建的配置。除非你继承了一个共享配置或在配置中显式启用规则，否则 ESLint 不会检查你的代码。

你可以通过定义一个带有 `rules` 键的新对象来单独配置规则，如本例所示：

```js
import { defineConfig } from "eslint/config";
import js from "@eslint/js";

export default defineConfig([
	{ files: ["**/*.js"], plugins: { js }, extends: ["js/recommended"] },

	{
		rules: {
			"no-unused-vars": "warn",
			"no-undef": "warn",
		},
	},
]);
```

`"no-unused-vars"` 和 `"no-undef"` 是 ESLint 中[规则](https://eslint.org/docs/latest/rules)的名称。第一个值是规则的错误级别，可以是以下值之一：

- `"off"` 或 `0` - 关闭该规则
- `"warn"` 或 `1` - 将该规则作为警告启用（不影响退出码）
- `"error"` 或 `2` - 将该规则作为错误启用（退出码为 1）

这三个错误级别允许你细粒度地控制 ESLint 应用规则的方式（更多配置选项和细节，请参阅配置文档）。

## 全局安装（Global Install）

也可以使用 `npm install eslint --global` 全局安装 ESLint，而不是本地安装。但是，不推荐这样做，而且即使你全局安装了 ESLint，你使用的任何插件或可共享配置仍必须本地安装。

## 手动设置（Manual Set Up）

你也可以在项目中手动设置 ESLint。

::: important
如果你使用的是 [pnpm](https://pnpm.io)，请务必创建一个 `.npmrc` 文件，至少包含以下设置：

```text
auto-install-peers=true
node-linker=hoisted
```

这可以确保 pnpm 以与 npm 更兼容的方式安装依赖，从而减少出错的可能性。
:::

在开始之前，你必须已经有一个 `package.json` 文件。如果没有，请确保事先运行 `npm init` 或 `yarn init` 来创建该文件。

1. 在项目中安装 ESLint 包：

```shell
npm install --save-dev eslint@latest @eslint/js@latest
```

2. 添加一个 `eslint.config.js` 文件：

    ```shell
    # 创建 JavaScript 配置文件
    touch eslint.config.js
    ```

3. 向 `eslint.config.js` 文件添加配置。参阅[配置 ESLint 文档](https://eslint.org/docs/latest/use/configure/)了解如何添加规则、自定义配置、插件等。

    ```js
    import { defineConfig } from "eslint/config";
    import js from "@eslint/js";

    export default defineConfig([
    	{
    		files: ["**/*.js"],
    		plugins: {
    			js,
    		},
    		extends: ["js/recommended"],
    		rules: {
    			"no-unused-vars": "warn",
    			"no-undef": "warn",
    		},
    	},
    ]);
    ```

4. 使用 ESLint CLI 检查代码：

```shell
npx eslint project-dir/ file.js
```

有关可用 CLI 选项的更多信息，请参阅[命令行接口](https://eslint.org/docs/latest/use/command-line-interface)。

---

## 后续步骤（Next Steps）

- 了解 ESLint 的[高级配置](https://eslint.org/docs/latest/use/configure/)。
- 熟悉[命令行选项](https://eslint.org/docs/latest/use/command-line-interface)。
- 探索 ESLint 与编辑器、构建系统等其他工具的[集成](https://eslint.org/docs/latest/use/integrations)。
- 找不到合适的规则？制作你自己的[自定义规则](https://eslint.org/docs/latest/extend/custom-rules)。
- 通过[贡献](https://eslint.org/docs/latest/contribute/)让 ESLint 变得更好。
