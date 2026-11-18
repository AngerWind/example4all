# ESLint 中文文档：入门（Getting Started）、核心概念（Core Concepts）与配置（Configure ESLint）

> 翻译自 ESLint 官方文档 https://eslint.org/docs/latest/ ，章节层级对应官网结构，每个章节标题下标注了原文链接，可与 en/ 目录下的英文原文对照。

# ESLint 入门（Getting Started with ESLint）

> 原文链接：https://eslint.org/docs/latest/use/getting-started

ESLint 是一个用于识别和报告 ECMAScript/JavaScript 代码中模式的工具，其目标是让代码更加一致并避免 bug。

ESLint 是完全可插拔的。每一条规则都是一个插件，你可以在运行时添加更多规则。你也可以添加社区插件、配置和解析器来扩展 ESLint 的功能。

## 前提条件（Prerequisites）

要使用 ESLint，你必须安装 [Node.js](https://nodejs.org/en/)（`^20.19.0`、`^22.13.0` 或 `>=24`）

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


---

# 核心概念（Core Concepts）

> 原文链接：https://eslint.org/docs/latest/use/core-concepts/

本页提供 ESLint 一些核心概念的高层概览。

## 什么是 ESLint？（What is ESLint?）

ESLint 是一个可配置的 JavaScript linter。它帮助你发现并修复 JavaScript 代码中的问题。问题可以是任何东西，从潜在的运行时 bug，到未遵循最佳实践，再到风格问题。

## 规则（Rules）

规则是 ESLint 的核心构建块。规则验证你的代码是否满足某个预期，以及当不满足预期时该做什么。规则还可以包含特定于该规则的额外配置选项。

例如，[`semi`](https://eslint.org/docs/latest/rules/semi) 规则允许你指定 JavaScript 语句是否应以分号（`;`）结尾。你可以将该规则设置为始终要求分号，或要求语句永远不以分号结尾。

ESLint 包含数百条你可以使用的内置规则。你也可以创建自定义规则，或通过[插件](#插件)使用其他人创建的规则。

更多信息请参阅[规则](https://eslint.org/docs/latest/rules/)。

### 规则修复（Rule Fixes）

有一些规则可以对当前规则发现的问题进行自动修复, 而不改变应用程序逻辑。

比如`semi`这个规则, 如果设置为不能分号结尾, 那么他可以帮助你自动删除行尾的分号

修复可以通过 [`--fix` 命令行选项](https://eslint.org/docs/latest/use/command-line-interface#--fix)和编辑器扩展自动应用。

提供修复的规则在[规则](https://eslint.org/docs/latest/rules/)页面中有 🔧的标记。

### 规则建议（Rule Suggestions）

有一些规则可以提供修复建议,  但是不提供自动修复。建议与修复有两个方面的不同：

1. 建议可能改变应用程序逻辑，因此不能自动应用。
1. 建议不能通过 ESLint CLI 应用，只能通过编辑器集成使用。

提供建议的规则在[规则](https://eslint.org/docs/latest/rules/)页面中有 💡的标记。

## 配置文件（Configuration Files）

ESLint 配置文件是你在项目中放置 ESLint 配置的地方。配置中可以包含内置规则及其执行方式、带自定义规则的插件、可共享配置、希望规则应用于哪些文件等等。

更多信息请参阅[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files)。

## 可共享配置（Shareable Configurations）

可共享配置是别人打包好, 发布到npm上的一套现成的ESLint配置,  你安装后直接引用, 不需要自己从零开始写配置

可共享配置的包名一般都是`eslint-config-xxx`, 例如，可共享配置 [eslint-config-airbnb-base](https://www.npmjs.com/package/eslint-config-airbnb-base) 实现了流行的 Airbnb JavaScript 风格指南。

更多信息请参阅[使用可共享配置包](https://eslint.org/docs/latest/use/configure/configuration-files#using-a-shareable-configuration-package)。

## 插件（Plugins）

ESLint 插件是一个 npm 模块，可以包含一组 ESLint 规则、配置、处理器和语言。

插件通常包含自定义规则。插件可用于强制执行风格指南，并支持 JavaScript 扩展（如 [TypeScript](https://www.typescriptlang.org)）、库（如 [React](https://react.dev)）和框架（如 [Angular](https://angular.dev)）。

插件的包名一般都是`eslint-config-xxx`

插件的一个流行用例是强制执行某个框架的最佳实践。例如，[@angular-eslint/eslint-plugin](https://www.npmjs.com/package/@angular-eslint/eslint-plugin) 包含使用 Angular 框架的最佳实践。

更多信息请参阅[配置插件](https://eslint.org/docs/latest/use/configure/plugins)。

## 解析器（Parsers）

ESLint 解析器将代码转换为 ESLint 可以求值的抽象语法树（AST）。默认情况下，ESLint 使用内置的 [Espree](https://github.com/eslint/js/tree/main/packages/espree) 解析器，它与标准的 JavaScript 运行时和版本兼容。

自定义解析器让 ESLint 能够解析非标准的 JavaScript 语法。自定义解析器通常作为可共享配置或插件的一部分被包含，因此你不必直接使用它们。

例如，[@typescript-eslint/parser](https://www.npmjs.com/package/@typescript-eslint/parser) 是 [typescript-eslint](https://github.com/typescript-eslint/typescript-eslint) 项目中包含的自定义解析器，它让 ESLint 能够解析 TypeScript 代码。

## 自定义处理器（Custom Processors）

ESLint 处理器从其他类型的文件中提取 JavaScript 代码，然后让 ESLint 检查这些 JavaScript 代码。或者，你可以使用处理器在用 ESLint 解析 JavaScript 代码之前对其进行处理。

例如，[@eslint/markdown](https://github.com/eslint/markdown) 包含一个自定义处理器，让你可以检查 Markdown 代码块中的 JavaScript 代码。

## 格式化器（Formatters）

ESLint 格式化器控制 CLI 中 lint 结果的展示外观。

更多信息请参阅[格式化器](https://eslint.org/docs/latest/use/formatters/)。

## 集成（Integrations）

让 ESLint 成为如此有用的工具的原因之一是围绕它的集成生态系统。例如，许多代码编辑器都有 ESLint 扩展，可以在你工作时直接在文件中显示代码的 ESLint 结果，这样你就不需要使用 ESLint CLI 来查看 lint 结果。

更多信息请参阅[集成](https://eslint.org/docs/latest/use/integrations)。

## CLI 与 Node.js API（CLI & Node.js API）

ESLint CLI 是一个命令行接口，让你可以从终端执行 lint。CLI 有多种选项可以传递给它的命令。

ESLint Node.js API 让你可以从 Node.js 代码中以编程方式使用 ESLint。该 API 在开发插件、集成和其他与 ESLint 相关的工具时很有用。

除非你以某种方式扩展 ESLint，否则应该使用 CLI。

更多信息请参阅[命令行接口](https://eslint.org/docs/latest/use/command-line-interface)和 [Node.js API](https://eslint.org/docs/latest/integrate/nodejs-api)。


---

# 配置 ESLint（Configure ESLint）

> 原文链接：https://eslint.org/docs/latest/use/configure/

ESLint 被设计为可以灵活配置以适应你的使用场景。你可以关闭所有规则，只运行基本的语法检查；也可以混合搭配内置规则和你的自定义规则，以满足项目的需求。配置 ESLint 有两种主要方式：

1. **配置注释（Configuration Comments）** - 使用 JavaScript 注释将配置信息直接嵌入到文件中。
2. **配置文件（Configuration Files）** - 使用 JavaScript 文件为整个目录及其所有子目录指定配置信息。这可以是一个 [`eslint.config.js`](https://eslint.org/docs/latest/use/configure/configuration-files) 文件（ESLint 会自动查找并读取它），或者你也可以在[命令行](https://eslint.org/docs/latest/use/command-line-interface)上指定配置文件。

以下是你可以在 ESLint 中配置的一些选项：

- [**全局变量（Globals）**](https://eslint.org/docs/latest/use/configure/language-options#specifying-globals) - 你的脚本在执行期间访问的额外全局变量。
- [**规则（Rules）**](https://eslint.org/docs/latest/use/configure/rules) - 启用哪些规则以及使用什么错误级别。
- [**插件（Plugins）**](https://eslint.org/docs/latest/use/configure/plugins) - 哪些第三方插件为 ESLint 定义了额外的规则、语言、配置等。

所有这些选项让你能够细粒度地控制 ESLint 如何处理你的代码。

## 目录（Table of Contents）

[**配置文件（Configuration Files）**](https://eslint.org/docs/latest/use/configure/configuration-files)

- [配置文件格式](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file)
- [配置对象](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-objects)
- [配置共享设置](https://eslint.org/docs/latest/use/configure/configuration-files#configuring-shared-settings)
- [配置文件解析](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file-resolution)

[**配置语言选项（Configure Language Options）**](https://eslint.org/docs/latest/use/configure/language-options)

- [指定 JavaScript 选项](https://eslint.org/docs/latest/use/configure/language-options#specifying-javascript-options)
- [指定全局变量](https://eslint.org/docs/latest/use/configure/language-options#specifying-globals)

[**配置规则（Configure Rules）**](https://eslint.org/docs/latest/use/configure/rules)

- [配置规则](https://eslint.org/docs/latest/use/configure/rules)
- [禁用规则](https://eslint.org/docs/latest/use/configure/rules#disabling-rules)

[**配置插件（Configure Plugins）**](https://eslint.org/docs/latest/use/configure/plugins)

- [配置插件](https://eslint.org/docs/latest/use/configure/plugins#configuring-plugins)
- [指定处理器](https://eslint.org/docs/latest/use/configure/plugins#specifying-a-processor)

[**配置解析器（Configure a Parser）**](https://eslint.org/docs/latest/use/configure/parser)

- [配置自定义解析器](https://eslint.org/docs/latest/use/configure/parser#configuring-a-custom-parser)

[**忽略文件（Ignore Files）**](https://eslint.org/docs/latest/use/configure/ignore)

- [忽略文件](https://eslint.org/docs/latest/use/configure/ignore#ignoring-files)
- [忽略目录](https://eslint.org/docs/latest/use/configure/ignore#ignoring-directories)
- [取消忽略文件和目录](https://eslint.org/docs/latest/use/configure/ignore#unignoring-files-and-directories)
- [已忽略文件警告](https://eslint.org/docs/latest/use/configure/ignore#ignored-file-warnings)


---

## 配置文件（Configuration Files）

> 原文链接：https://eslint.org/docs/latest/use/configure/configuration-files

::: tip
本页介绍如何使用 flat config 配置文件。
:::

你可以将 ESLint 项目配置放在配置文件中。配置中可以包含内置规则及其执行方式、带自定义规则的插件、可共享配置、希望规则应用于哪些文件等等。

### 配置文件（Configuration File）

ESLint 配置文件可以命名为以下任意一种：

- `eslint.config.js`
- `eslint.config.mjs`
- `eslint.config.cjs`
- `eslint.config.ts`（需要[额外设置](#typescript-配置文件)）
- `eslint.config.mts`（需要[额外设置](#typescript-配置文件)）
- `eslint.config.cts`（需要[额外设置](#typescript-配置文件)）

它应该放在项目的根目录中，并导出一个[配置对象](#配置对象)数组。下面是一个示例：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		rules: {
			semi: "error",
			"prefer-const": "error",
		},
	},
]);
```

在此示例中，`defineConfig()` 辅助函数用于定义只包含一个配置对象的配置数组。该配置对象启用了两条规则：`semi` 和 `prefer-const`。这些规则应用于 ESLint 使用此配置文件处理的所有文件。

如果你的项目在其 `package.json` 文件中指定了 `"type": "commonjs"`，那么 `eslint.config.js` 必须使用 CommonJS 格式，例如：

```js
// eslint.config.js
const { defineConfig } = require("eslint/config");

module.exports = defineConfig([
	{
		rules: {
			semi: "error",
			"prefer-const": "error",
		},
	},
]);
```

### 配置对象（Configuration Objects）

每个配置对象包含 ESLint 在一组文件上执行所需的全部信息。每个配置对象由以下属性组成：

- `name` - 配置对象的名称。用于错误消息和 [config inspector](https://github.com/eslint/config-inspector) 中，帮助识别正在使用哪个配置对象。（[命名约定](#配置命名约定)）
- `basePath` - 一个字符串，指定配置对象应应用到的子目录路径。可以是相对路径或绝对路径。
- `files` - 一个 glob 模式数组，指示配置对象应应用于哪些文件。如果未指定，配置对象应用于被任何其他配置对象匹配的所有文件。
- `ignores` - 一个 glob 模式数组，指示配置对象不应应用于哪些文件。如果未指定，配置对象应用于 `files` 匹配的所有文件。如果 `ignores` 在配置对象中没有与其他任何键一起使用，那么这些模式将作为[全局忽略](#使用-ignores-全局忽略文件)，应用于每个配置对象。
- `extends` - 一个由字符串、配置对象或配置数组组成的数组，包含要应用的额外配置。
- `language` - 一个字符串，指定用于 lint 的[语言](https://eslint.org/docs/latest/extend/languages)，格式为 `"pluginName/languageName"`，例如 `"markdown/commonmark"`。（默认值：`"js/js"`，即 JavaScript）
- `languageOptions` - 一个对象，包含与如何配置指定语言进行 lint 相关的设置。对于 JavaScript，设置项包括：
    - `ecmaVersion` - 要支持的 ECMAScript 版本。可以是任意年份（如 `2022`）或版本号（如 `5`）。设置为 `"latest"` 表示最新支持的版本。（默认值：`"latest"`）
    - `sourceType` - JavaScript 源代码的类型。可能的值为 `"script"`（传统脚本文件）、`"module"`（ECMAScript 模块，ESM）和 `"commonjs"`（CommonJS 文件）。（默认值：`.js` 和 `.mjs` 文件为 `"module"`；`.cjs` 文件为 `"commonjs"`）
    - `globals` - 一个对象，指定在 lint 期间应添加到全局作用域的额外对象。
    - `parser` - 一个包含 `parse()` 方法或 `parseForESLint()` 方法的对象。（默认值：[`espree`](https://github.com/eslint/js/tree/main/packages/espree)）
    - `parserOptions` - 一个对象，指定直接传递给解析器的 `parse()` 或 `parseForESLint()` 方法的额外选项。可用选项取决于解析器。
- `linterOptions` - 一个对象，包含与 lint 过程相关的设置。
    - `noInlineConfig` - 一个布尔值，指示是否允许内联配置。
    - `reportUnusedDisableDirectives` - 一个严重级别字符串，指示是否以及如何跟踪和报告未使用的禁用和启用指令。为了兼容旧版本，`true` 等同于 `"warn"`，`false` 等同于 `"off"`。（默认值：`"warn"`）
    - `reportUnusedInlineConfigs` - 一个严重级别字符串，指示是否以及如何跟踪和报告未使用的内联配置。（默认值：`"off"`）
- `processor` - 一个包含 `preprocess()` 和 `postprocess()` 方法的对象，或者一个指示插件内处理器名称的字符串（如 `"pluginName/processorName"`）。
- `plugins` - 一个对象，包含插件名称到插件对象的名称-值映射。当指定了 `files` 时，这些插件仅对匹配的文件可用。
- `rules` - 一个对象，包含已配置的规则。当指定了 `files` 或 `ignores` 时，这些规则配置仅对匹配的文件可用。
- `settings` - 一个对象，包含应对所有规则可用的信息名称-值对。

#### 指定 `files` 和 `ignores`（Specify `files` and `ignores`）

::: tip
`files` 和 `ignores` 中指定的模式使用 [`minimatch`](https://www.npmjs.com/package/minimatch) 语法，并相对于 `eslint.config.js` 文件的位置求值。如果通过 `--config` 命令行选项使用替代配置文件，则所有模式相对于当前工作目录求值。如果配置对象带有相对路径的 `basePath` 属性，其指定的子目录相对于 `eslint.config.js` 文件的位置求值（或者，如果通过 `--config` 命令行选项使用替代配置文件，则相对于当前工作目录）。在带有 `basePath` 属性的配置对象中，`files` 和 `ignores` 中指定的模式相对于 `basePath` 所表示的子目录求值。
:::

你可以结合使用 `files` 和 `ignores` 来确定配置对象应应用于哪些文件、不应用于哪些文件。下面是一个示例：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	// 匹配所有以 .js 结尾的文件
	{
		files: ["**/*.js"],
		rules: {
			semi: "error",
		},
	},

	// 匹配所有以 .js 结尾的文件，但 __tests 中的除外
	{
		files: ["**/*.js"],
		ignores: ["__tests/**"],
		rules: {
			"no-console": "error",
		},
	},
]);
```

不带 `files` 或 `ignores` 的配置对象会自动应用于被任何其他配置对象匹配的任何文件。例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	// 匹配所有文件，因为它没有指定 `files` 或 `ignores` 键
	{
		rules: {
			semi: "error",
		},
	},
]);
```

使用此配置时，`semi` 规则对匹配 ESLint 默认文件的所有文件启用。因此，如果你将 `example.js` 传给 ESLint，`semi` 规则会被应用。如果你传入一个非 JavaScript 文件，比如 `example.txt`，`semi` 规则不会被应用，因为没有其他配置对象匹配该文件名。（ESLint 会输出一条错误消息，告知你该文件因缺少配置而被忽略。）

::: important
默认情况下，ESLint 检查匹配 `**/*.js`、`**/*.cjs` 和 `**/*.mjs` 模式的文件。除非你使用[全局忽略](#使用-ignores-全局忽略文件)显式排除它们，否则这些文件总是被匹配的。
如果你的配置对象包含其他模式，不带 `files` 键的配置对象中的规则也会应用于这些模式。

因此，当同时将 ESLint 用于非 JS 文件时，更合适的做法是创建一个包含 `files: ["**/*.js", "**/*.cjs", "**/*.mjs"]` 的配置对象，并将相关规则放在其中。
:::

##### 指定任意扩展名的文件（Specify files with arbitrary extensions）

要检查默认 `.js`、`.cjs` 和 `.mjs` 之外扩展名的文件，请在 `files` 中以 `"**/*.extension"` 格式的模式包含它们。任何模式都可以，除非它是 `*` 或以 `/*` 或 `/**` 结尾。
例如，要检查扩展名为 `.ts`、`.cts` 和 `.mts` 的 TypeScript 文件，你可以这样指定配置对象：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.ts", "**/*.cts", "**/*.mts"],
	},
	// ...其他配置
]);
```

##### 指定无扩展名的文件（Specify files without extension）

没有扩展名的文件可以使用模式 `!(*.*)` 进行匹配。例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/!(*.*)"],
	},
	// ...其他配置
]);
```

上述配置会检查所有目录中没有扩展名的文件，以及默认的 `.js`、`.cjs` 和 `.mjs` 扩展名文件。

::: tip
以点开头的文件名（如 `.gitignore`）被视为只有扩展名而没有基本名称。以 `.gitignore` 为例，其扩展名是 `gitignore`，因此该文件匹配模式 `"**/.gitignore"`，但不匹配 `"**/*.gitignore"`。
:::

##### 使用 AND 运算指定文件（Specify files with an AND operation）

通过在 `files` 数组内使用字符串数组，可以让多个模式同时匹配同一个文件。例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: [["src/*", "**/*.js"]],
	},
	// ...其他配置
]);
```

模式 `["src/*", "**/*.js"]` 在文件既位于 `src` 目录内又以 `.js` 结尾时匹配。当你动态计算 `files` 数组的值，并希望避免将多个 glob 模式组合为单个字符串可能带来的错误时，这种方法很有帮助。

##### 使用 `ignores` 排除文件（Exclude files with `ignores`）

你可以通过指定 `files` 和 `ignores` 模式的组合来限制配置对象应用于哪些文件。例如，你可能希望某些规则仅应用于 `src` 目录中的文件：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["src/**/*.js"],
		rules: {
			semi: "error",
		},
	},
]);
```

这里，只有 `src` 目录中的 JavaScript 文件应用了 `semi` 规则。如果你对另一个目录中的文件运行 ESLint，此配置对象会被跳过。通过添加 `ignores`，你还可以将 `src` 中的一些文件从此配置对象中排除：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["src/**/*.js"],
		ignores: ["**/*.config.js"],
		rules: {
			semi: "error",
		},
	},
]);
```

此配置对象匹配 `src` 目录中所有 JavaScript 文件，但以 `.config.js` 结尾的除外。你也可以在 `ignores` 中使用否定模式来将文件从忽略模式中排除，例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["src/**/*.js"],
		ignores: ["**/*.config.js", "!**/eslint.config.js"],
		rules: {
			semi: "error",
		},
	},
]);
```

这里，配置对象排除了以 `.config.js` 结尾的文件，但 `eslint.config.js` 除外。该文件仍会应用 `semi`。

非全局 `ignores` 模式只能匹配文件名。像 `"dir-to-exclude/"` 这样的模式不会忽略任何内容。要忽略特定目录中的所有内容，应使用 `"dir-to-exclude/**"` 这样的模式。

如果 `ignores` 在没有 `files` 且有其他键（如 `rules`）的情况下使用，那么配置对象应用于除被 `ignores` 排除的文件之外的所有被检查文件，例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		ignores: ["**/*.config.js"],
		rules: {
			semi: "error",
		},
	},
]);
```

此配置对象应用于除以 `.config.js` 结尾的文件之外的所有 JavaScript 文件。实际上，这相当于将 `files` 设置为 `**/*`。一般来说，如果你要指定 `ignores`，最好总是同时包含 `files`。

请注意，当未指定 `files` 时，否定的 `ignores` 模式不会导致任何匹配的文件被自动检查。
ESLint 只检查被默认模式或被 `files` 模式（不是 `*` 且不以 `/*` 或 `/**` 结尾）匹配的文件。

::: tip
使用 [config inspector](https://github.com/eslint/config-inspector)（CLI 中的 `--inspect-config`）来测试哪些配置对象适用于特定文件。
:::

##### 使用 `ignores` 全局忽略文件（Globally ignore files with `ignores`）

根据 `ignores` 属性的使用方式，它可以表现为非全局 `ignores` 或全局 `ignores`。

- 当 `ignores` 在配置对象中没有与任何其他键（除 `name` 外）一起使用时，这些模式作为全局忽略。这意味着它们应用于每个配置对象（不仅仅是定义它的配置对象）。全局 `ignores` 让你不必在多个配置对象中复制并保持 `ignores` 属性同步。
- 如果 `ignores` 与同一配置对象中的其他属性一起使用，那么这些模式作为非全局忽略。这样 `ignores` 只应用于定义它的配置对象。

全局和非全局 `ignores` 在使用上有一些区别：

- 非全局 `ignores` 中的模式只匹配文件（`dir/filename.js`）或目录内的文件（`dir/**`）
- 全局 `ignores` 中的模式除了支持非全局忽略支持的模式外，还可以匹配目录（`dir/`）

对于 `ignores` 的所有用法：

- 你定义的模式会添加在 ESLint 默认模式之后，默认模式是 `["**/node_modules/", ".git/"]`。
- 模式总是匹配以点开头的文件和目录，例如 `.foo.js` 或 `.fixtures`，除非这些文件被显式忽略。默认唯一被忽略的点目录是 `.git`。

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

// 全局忽略示例
export default defineConfig([
    {
      ignores: [".config/", "dist/", "tsconfig.json"] // 由于没有其他属性，作为全局忽略
    },
    { ... }, // ... 其他配置对象，继承全局忽略
    { ... }, // ... 其他配置对象，继承全局忽略
]);

// 非全局忽略示例
export default defineConfig([
    {
      ignores: [".config/**", "dir1/script1.js"],
      rules: { ... } // 此属性的存在决定了这是非全局忽略
    },
    {
      ignores: ["other-dir/**", "dist/script2.js"],
      rules: { ... } // 此属性的存在决定了这是非全局忽略
    },
]);
```

为了避免混淆，使用 `globalIgnores()` 辅助函数来清楚地表明哪些忽略是全局的。下面是用 `globalIgnores()` 重写的前一个示例：

```js
// eslint.config.js
import { defineConfig, globalIgnores } from "eslint/config";

// 全局忽略示例
export default defineConfig([
    globalIgnores([".config/", "dist/", "tsconfig.json"]),
    { ... }, // ... 其他配置对象，继承全局忽略
    { ... }, // ... 其他配置对象，继承全局忽略
]);

// 非全局忽略示例
export default defineConfig([
    {
      ignores: [".config/**", "dir1/script1.js"],
      rules: { ... } // 此属性的存在决定了这是非全局忽略
    },
    {
      ignores: ["other-dir/**", "dist/script2.js"],
      rules: { ... } // 此属性的存在决定了这是非全局忽略
    },
]);
```

有关配置 `ignores` 规则的更多信息和示例，请参见[忽略文件](https://eslint.org/docs/latest/use/configure/ignore)。

##### 指定基准路径（Specify base path）

你可以选择性地指定 `basePath`，将配置对象应用于特定子目录（包括其子目录）。

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	// 匹配 tests 及其子目录中的所有文件
	{
		basePath: "tests",
		rules: {
			"no-undef": "error",
		},
	},

	// 匹配 tests 及其子目录中所有以 spec.js 结尾的文件
	{
		basePath: "tests",
		files: ["**/*.spec.js"],
		languageOptions: {
			globals: {
				it: "readonly",
				describe: "readonly",
			},
		},
	},

	// 全局忽略 tests/fixtures 目录
	{
		basePath: "tests",
		ignores: ["fixtures/"],
	},
]);
```

结合 [`extends`](#扩展配置)，只需指定一次 `basePath`，就可以将多个配置对象应用于同一个子目录，如下所示：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		basePath: "tests",
		extends: [
			// 匹配 tests 及其子目录中的所有文件
			{
				rules: {
					"no-undef": "error",
				},
			},

			// 匹配 tests 及其子目录中所有以 spec.js 结尾的文件
			{
				files: ["**/*.spec.js"],
				languageOptions: {
					globals: {
						it: "readonly",
						describe: "readonly",
					},
				},
			},

			// 全局忽略 tests/fixtures 目录
			{
				ignores: ["fixtures/"],
			},
		],
	},
]);
```

##### 层叠配置对象（Cascading Configuration Objects）

当多个配置对象匹配给定文件名时，配置对象会被合并，发生冲突时后面的对象覆盖前面的对象。例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.js"],
		languageOptions: {
			globals: {
				MY_CUSTOM_GLOBAL: "readonly",
			},
		},
	},
	{
		files: ["tests/**/*.js"],
		languageOptions: {
			globals: {
				it: "readonly",
				describe: "readonly",
			},
		},
	},
]);
```

使用此配置时，所有 JavaScript 文件都定义了一个名为 `MY_CUSTOM_GLOBAL` 的自定义全局对象，而 `tests` 目录中的 JavaScript 文件除了 `MY_CUSTOM_GLOBAL` 之外，还定义了 `it` 和 `describe` 作为全局对象。对于 `tests` 目录中的任何 JavaScript 文件，两个配置对象都会被应用，因此 `languageOptions.globals` 会被合并以产生最终结果。

#### 配置 Linter 选项（Configure Linter Options）

特定于 lint 过程的选项可以使用 `linterOptions` 对象进行配置。这些选项影响 lint 的进行方式，不影响文件源代码的解释方式。

##### 禁用内联配置（Disable Inline Configuration）

内联配置使用 `/*eslint*/` 注释实现，例如 `/*eslint semi: error*/`。你可以通过将 `noInlineConfig` 设置为 `true` 来禁止内联配置。启用后，所有内联配置都会被忽略。下面是一个示例：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.js"],
		linterOptions: {
			noInlineConfig: true,
		},
	},
]);
```

##### 报告未使用的禁用指令（Report Unused Disable Directives）

诸如 `/*eslint-disable*/`、`/*eslint-enable*/` 和 `/*eslint-disable-next-line*/` 之类的禁用和启用指令用于禁用代码某些部分周围的 ESLint 规则。随着代码的变化，这些指令可能不再需要，因为代码已经改变，规则不再被触发。你可以通过将 `reportUnusedDisableDirectives` 选项设置为严重级别字符串来启用对这些未使用的禁用指令的报告，如本例所示：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.js"],
		linterOptions: {
			reportUnusedDisableDirectives: "error",
		},
	},
]);
```

此设置默认为 `"warn"`。

你可以使用 [`--report-unused-disable-directives`](https://eslint.org/docs/latest/use/command-line-interface#--report-unused-disable-directives) 或 [`--report-unused-disable-directives-severity`](https://eslint.org/docs/latest/use/command-line-interface#--report-unused-disable-directives-severity) 命令行选项覆盖此设置。

为了兼容旧版本，`true` 等同于 `"warn"`，`false` 等同于 `"off"`。

##### 报告未使用的内联配置（Report Unused Inline Configs）

诸如 `/* eslint rule-name: "error" */` 之类的内联配置注释用于更改代码某些部分周围的 ESLint 规则严重级别和/或选项。
随着项目 ESLint 配置文件的变化，这些指令可能与已设置的内容不再有任何区别。
你可以通过将 `reportUnusedInlineConfigs` 选项设置为严重级别字符串来启用对这些未使用的内联配置注释的报告，如本例所示：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.js"],
		linterOptions: {
			reportUnusedInlineConfigs: "error",
		},
	},
]);
```

你可以使用 [`--report-unused-inline-configs`](https://eslint.org/docs/latest/use/command-line-interface#--report-unused-inline-configs) 命令行选项覆盖此设置。

#### 配置规则（Configure Rules）

你可以在配置对象中配置任意数量的规则，只需添加一个 `rules` 属性，其值是包含你的规则配置的对象。此对象中的名称是规则的名称，值是每条规则的配置。下面是一个示例：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		rules: {
			semi: "error",
		},
	},
]);
```

此配置对象指定 [`semi`](https://eslint.org/docs/latest/rules/semi) 规则应以 `"error"` 严重级别启用。你也可以通过指定一个数组来为规则提供选项，数组的第一项是严重级别，之后的每一项都是规则的选项。例如，你可以通过传递 `"never"` 选项将 `semi` 规则切换为不允许分号：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		rules: {
			semi: ["error", "never"],
		},
	},
]);
```

每条规则指定自己的选项，选项可以是任何有效的 JSON 数据类型。有关其可用选项的更多信息，请查看你要配置的规则的文档。

有关配置规则的更多信息，请参见[配置规则](https://eslint.org/docs/latest/use/configure/rules)。

#### 配置共享设置（Configure Shared Settings）

ESLint 支持在配置文件中添加共享设置。当你向配置对象添加 `settings` 对象时，它会被提供给每条规则。按照约定，插件会为它们感兴趣的设置加上命名空间，以避免与其他插件冲突。插件可以使用 `settings` 来指定应在它们所有规则之间共享的信息。如果你要添加自定义规则并希望它们能访问相同的信息，这可能会很有用。下面是一个示例：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		settings: {
			sharedData: "Hello",
		},
		plugins: {
			customPlugin: {
				rules: {
					"my-rule": {
						meta: {
							// 自定义规则的 meta 信息
						},
						create(context) {
							const sharedData = context.settings.sharedData;
							return {
								// 代码
							};
						},
					},
				},
			},
		},
		rules: {
			"customPlugin/my-rule": "error",
		},
	},
]);
```

#### 扩展配置（Extending Configurations）

配置对象使用 `extends` 来继承另一个配置对象或数组的所有特性（包括规则、插件和语言选项），然后可以修改所有选项。`extends` 键是一个值数组，指示要扩展哪些配置。`extends` 数组的元素可以是以下三种值之一：

- 指定插件中配置名称的字符串
- 一个配置对象
- 一个配置数组

##### 使用来自插件的配置（Use Configurations from Plugins）

ESLint 插件可以导出预定义配置。这些配置使用字符串引用，遵循 `pluginName/configName` 模式。插件必须先在 `plugins` 键中指定。下面是一个示例：

```js
// eslint.config.js
import examplePlugin from "eslint-plugin-example";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.js"],
		plugins: {
			example: examplePlugin,
		},
		extends: ["example/recommended"],
	},
]);
```

在此示例中，来自 `eslint-plugin-example` 的名为 `recommended` 的配置被加载。插件配置也可以在配置数组内按名称引用。

你也可以将插件配置直接插入 `extends` 数组。例如：

```js
// eslint.config.js
import pluginExample from "eslint-plugin-example";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.js"],
		plugins: {
			example: pluginExample,
		},
		extends: [pluginExample.configs.recommended],
	},
]);
```

在这种情况下，来自 `eslint-plugin-example` 的名为 `recommended` 的配置通过插件对象的 `configs` 属性直接访问。

::: important
建议在使用 `extends` 键时始终使用 `files` 键，以确保你的配置应用于正确的文件。如果省略 `files` 键，扩展的配置可能最终应用于所有文件。
:::

##### 使用预定义配置（Use Predefined Configurations）

ESLint 有两个针对 JavaScript 的预定义配置：

- `js/recommended` - 启用 ESLint 推荐所有人使用以避免潜在错误的规则。
- `js/all` - 启用 ESLint 附带的所有规则。**不推荐**在生产环境使用此配置，因为它会随 ESLint 的每个次版本和主版本而变化。使用风险自负。

要包含这些预定义配置，请安装 `@eslint/js` 包，然后在后续配置对象中对其他属性进行任何修改：

```js
// eslint.config.js
import js from "@eslint/js";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.js"],
		plugins: {
			js,
		},
		extends: ["js/recommended"],
		rules: {
			"no-unused-vars": "warn",
		},
	},
]);
```

这里，首先应用 `js/recommended` 预定义配置，然后另一个配置对象添加 [`no-unused-vars`](https://eslint.org/docs/latest/rules/no-unused-vars) 所需的配置。

有关如何将预定义配置与你的偏好相结合的更多信息，请参见[合并配置](https://eslint.org/docs/latest/use/configure/combine-configs)。

##### 使用可共享配置包（Use a Shareable Configuration Package）

可共享配置是一个导出配置对象或数组的 npm 包。此包应作为依赖项安装在你的项目中，然后在你的 `eslint.config.js` 文件内引用。例如，要使用名为 `eslint-config-example` 的可共享配置，你的配置文件将如下所示：

```js
// eslint.config.js
import exampleConfig from "eslint-config-example";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.js"],
		extends: [exampleConfig],
		rules: {
			"no-unused-vars": "warn",
		},
	},
]);
```

在此示例中，`exampleConfig` 可以是对象或数组，无论哪种方式都可以直接插入 `extends` 数组。

有关如何将可共享配置与你的偏好相结合的更多信息，请参见[合并配置](https://eslint.org/docs/latest/use/configure/combine-configs)。

#### 何时使用 Extends 与 Cascading 层叠（When to Use Extends vs Cascading）

何时使用 `Extends`：

- **复用配置** - 当你想继承并在插件、可共享包或预定义配置的现有配置基础上构建时。
- **插件配置** - 当应用来自 ESLint 插件的推荐或特定配置时（例如 `example/recommended`）。
- **可共享配置** - 当使用导出配置对象的 npm 包时（例如 `eslint-config-example`）。
- **预定义配置** - 当使用 ESLint 的内置配置（如 `js/recommended` 或 `js/all`）时。
- **模块化配置** - 当你想将多个配置来源组合到单个配置对象中时。
- **保持一致性** - 当你想确保多个配置对象之间的基础规则一致时。
- **插件集成** - 当你需要应用插件自带的配置时。

何时使用 `Cascading`（层叠）：

- **针对特定文件的规则** - 当你需要为不同的文件模式或目录使用不同的规则时。
- **渐进式配置** - 当你想对所有文件应用基础规则，然后为特定子集添加/覆盖规则时。
- **特定于环境的设置** - 当测试文件、源文件和配置文件需要不同的规则集时。
- **基于目录的配置** - 当不同的项目目录需要不同的 lint 方式时。
- **规则严重级别调整** - 当你想为特定文件模式更改规则严重级别（`error`/`warn`/`off`）时。
- **语言选项变化** - 当不同的文件需要不同的 ECMAScript 版本或解析器选项时。

#### 配置命名约定（Configuration Naming Conventions）

`name` 属性是可选的，但建议为每个配置对象提供一个名称，尤其是在创建共享配置时。该名称用于错误消息和 config inspector 中，帮助识别正在使用哪个配置对象。

名称应能描述配置对象的用途，并使用 `/` 作为分隔符以配置名称或插件名称作为作用域前缀。ESLint 不会在运行时强制名称唯一，但建议设置唯一的名称以避免混淆。

例如，如果你正在为名为 `eslint-plugin-example` 的插件创建配置对象，你可以在配置对象中添加带有 `example/` 前缀的 `name`：

```js
export default {
	configs: {
		recommended: {
			name: "example/recommended",
			rules: {
				"no-unused-vars": "warn",
			},
		},
		strict: {
			name: "example/strict",
			rules: {
				"no-unused-vars": "error",
			},
		},
	},
};
```

当公开配置对象数组时，`name` 可以有额外的作用域层级，以帮助识别配置对象。例如：

```js
export default {
	configs: {
		strict: [
			{
				name: "example/strict/language-setup",
				languageOptions: {
					ecmaVersion: 2024,
				},
			},
			{
				name: "example/strict/sub-config",
				files: ["src/**/*.js"],
				rules: {
					"no-unused-vars": "error",
				},
			},
		],
	},
};
```

### 配置文件解析（Configuration File Resolution）

当 ESLint 在命令行上运行时，它通过首先查找包含目标文件的目录，然后向上搜索祖先目录，直到找到 `eslint.config.*` 文件，来确定每个目标文件的配置。此行为改进了对 monorepo 的支持，monorepo 中的子目录可以有自己的配置文件。

你可以通过在命令行上使用 `-c` 或 `--config` 选项指定替代配置文件来阻止此搜索，例如：

```shell
npx eslint --config some-other-file.js "**/*.js"
```

在这种情况下，ESLint 不会搜索配置文件，而是使用 `some-other-file.js`。

### TypeScript 配置文件（TypeScript Configuration Files）

对于 Deno 和 Bun，TypeScript 配置文件受到原生支持；对于 Node.js，你必须在项目中安装 2.2.0 或更高版本的可选 dev 依赖 [`jiti`](https://github.com/unjs/jiti)（ESLint 不会自动安装此依赖）：

```shell
npm install --save-dev jiti
```

然后你可以创建扩展名为 `.ts`、`.mts` 或 `.cts` 的配置文件，并导出一个[配置对象](#配置对象)数组。

::: important
ESLint 不会对你的配置文件进行类型检查，也不会应用 `tsconfig.json` 中的任何设置。
:::

#### 原生 TypeScript 支持（Native TypeScript Support）

如果你使用的是 **Node.js >= 22.13.0**，你可以原生加载 TypeScript 配置文件，而无需 [`jiti`](https://github.com/unjs/jiti)。这得益于 [**`--experimental-strip-types`**](https://nodejs.org/docs/latest-v22.x/api/cli.html#--experimental-strip-types) 标志。

由于此功能仍处于实验阶段，你还必须启用 `unstable_native_nodejs_ts_config` 标志。

```bash
npx --node-options='--experimental-strip-types' eslint --flag unstable_native_nodejs_ts_config
```

#### 配置文件优先级（Configuration File Precedence）

如果你有多个 ESLint 配置文件，ESLint 会优先使用 JavaScript 文件而不是 TypeScript 文件。优先级顺序如下：

1. `eslint.config.js`
2. `eslint.config.mjs`
3. `eslint.config.cjs`
4. `eslint.config.ts`
5. `eslint.config.mts`
6. `eslint.config.cts`

要覆盖此行为，使用 `--config` 或 `-c` 命令行选项指定不同的配置文件：

```shell
npx eslint --config eslint.config.ts
```







## 配置语言选项（Configure Language Options）

> 原文链接：https://eslint.org/docs/latest/use/configure/language-options



JavaScript 生态系统包含各种各样的运行时、版本、扩展和框架。它们各自可能支持不同的语法和全局变量。ESLint 允许你针对项目中使用的 JavaScript 配置语言选项，例如自定义全局变量。你也可以使用插件来扩展 ESLint，使其支持项目的语言选项。

### 指定 JavaScript 选项（Specify JavaScript Options）

ESLint 允许你指定想要支持的 JavaScript 语言选项。默认情况下，ESLint 假定使用最新的 stage 4 ECMAScript 语法和 ECMAScript 模块（ESM）模式。你可以通过 `languageOptions` 键覆盖这些设置，并指定以下一个或多个属性：

- `ecmaVersion`（默认值：`"latest"`）- 指示被检查代码的 ECMAScript 版本，同时决定可用的语法和全局变量。分别设置为 `3` 或 `5` 表示 ECMAScript 3 和 5。否则，你可以使用 `2015` 至今的任何年份。在大多数情况下，我们建议使用默认值 `"latest"`，以确保始终使用最新的 ECMAScript 版本。
- `sourceType`（默认值：`"module"`）- 指示所使用的 JavaScript 文件的模式。可能的值为：
    - `module` - ESM 模块（当 `ecmaVersion` 为 `3` 或 `5` 时无效）。你的代码具有模块作用域，并在严格模式下运行。
    - `commonjs` - CommonJS 模块（如果你的代码使用 `require()`，这个选项很有用）。你的代码具有顶层函数作用域，并在非严格模式下运行。
    - `script` - 非模块。你的代码具有共享的全局作用域，并在非严格模式下运行。

下面是一个在检查 ECMAScript 5 代码时可能使用的[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file)示例：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		languageOptions: {
			ecmaVersion: 5,
			sourceType: "script",
		},
	},
]);
```





### 指定解析器选项（Specify Parser Options）

如果你使用的是 ESLint 内置解析器，还可以通过指定 `languageOptions.parserOptions` 键来改变 ESLint 解释代码的方式。所有选项默认为 `false`：

- `allowReserved` - 允许使用保留字作为标识符（当 `ecmaVersion` 为 `3` 时）。
- `ecmaFeatures` - 一个对象，指示你想使用哪些额外的语言特性：
    - `globalReturn` - 允许在全局作用域中使用 `return` 语句。此选项仅在 `languageOptions.sourceType` 设置为 `"script"` 时生效。当 `languageOptions.sourceType` 设置为 `"commonjs"` 时，无论此选项如何，顶层 `return` 语句都是被允许的。当 `languageOptions.sourceType` 设置为 `"module"` 时，即使此选项设置为 `true`，顶层 `return` 语句也是不允许的。
    - `impliedStrict` - 启用全局[严格模式](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Strict_mode)（当 `ecmaVersion` 为 `5` 或更高时）。
    - `jsx` - 启用 [JSX](https://react.github.io/jsx/)。

下面是一个在默认解析器中启用 JSX 解析的[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file)示例：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		languageOptions: {
			parserOptions: {
				ecmaFeatures: {
					jsx: true,
				},
			},
		},
	},
]);
```

::: tip
虽然可以在 `languageOptions.parserOptions` 中传递 `ecmaVersion` 和 `sourceType`，但对于默认解析器我们不建议这样做。相反，请直接在 `languageOptions` 中配置这些选项。如果两者都被指定，对于解析器而言，`languageOptions.parserOptions` 中的选项优先于 `languageOptions` 中的选项。
:::

::: important
请注意，支持 JSX 语法并不等同于支持 React。React 对 JSX 语法应用了 ESLint 无法识别的特定语义。如果你在使用 React，我们建议使用 [`eslint-plugin-react`](https://github.com/jsx-eslint/eslint-plugin-react) 或 [`@eslint-react/eslint-plugin`](https://github.com/rel1cx/eslint-react)。
:::

### 指定全局变量（Specify Globals）

ESLint 的一些核心规则依赖于对你的代码在运行时可用全局变量的了解。由于这些全局变量在不同环境之间差异很大，并且可能在运行时被修改，ESLint 不会对你的执行环境中存在哪些全局变量做任何假设。如果你想使用需要了解可用全局变量的规则，可以在配置文件中定义全局变量，或者在源代码中使用配置注释。

#### 使用配置注释（Use configuration comments）

要在 JavaScript 文件内使用注释指定全局变量，请使用以下格式：

```js
/* global var1, var2 */
```

这定义了两个全局变量 `var1` 和 `var2`。如果你想可选地指定这些全局变量可以被写入（而不仅仅是被读取），可以为每个变量设置 `"writable"` 标志：

```js
/* global var1:writable, var2:writable */
```

#### 使用配置文件（Use configuration files）

要在[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file)中配置全局变量，将 `languageOptions.globals` 配置属性设置为一个对象，其键名为你想使用的每个全局变量。对于每个全局变量键，将对应的值设置为 `"writable"` 以允许覆盖该变量，或设置为 `"readonly"` 以禁止覆盖。例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		languageOptions: {
			globals: {
				var1: "writable",
				var2: "readonly",
			},
		},
	},
]);
```

此配置允许在你的代码中覆盖 `var1`，但不允许覆盖 `var2`。

可以通过将全局变量的值设置为 `"off"` 来禁用它们。例如，在一个大多数全局变量可用但 `Promise` 不可用的环境中，你可以使用此配置：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		languageOptions: {
			globals: {
				Promise: "off",
			},
		},
	},
]);
```

::: tip
由于历史原因，布尔值 `false` 和字符串值 `"readable"` 等同于 `"readonly"`。同样，布尔值 `true` 和字符串值 `"writeable"` 等同于 `"writable"`。但是，这些旧值的使用已被废弃。
:::

#### 预定义全局变量（Predefined global variables）

除了根据配置的 `languageOptions.ecmaVersion` 自动启用的 ECMAScript 标准内置全局变量之外，ESLint 不提供预定义的全局变量集合。你可以使用 [`globals`](https://www.npmjs.com/package/globals) 包来额外启用特定环境的所有全局变量。例如，下面展示如何将 `console` 以及其他浏览器全局变量添加到你的配置中：

```js
// eslint.config.js
import globals from "globals";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		languageOptions: {
			globals: {
				...globals.browser,
			},
		},
	},
]);
```

你可以用同样的方式包含多个不同的全局变量集合。下面的示例同时包含了 Web 浏览器和 [Jest](https://jestjs.io/) 的全局变量：

```js
// eslint.config.js
import globals from "globals";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		languageOptions: {
			globals: {
				...globals.browser,
				...globals.jest,
			},
		},
	},
]);
```


---

## 配置规则（Configure Rules）

> 原文链接：https://eslint.org/docs/latest/use/configure/rules

规则用于验证你写的代码是否满足特定的规范, 以及在不满足的时候应该做什么

一个规则还可以包含额外的配置

ESLint 自带大量[内置规则](https://eslint.org/docs/latest/rules/)，你也可以通过插件添加更多规则。



要对规则进行配置, 那么有两种方式

- 在**配置文件**中配置规则
- 直接在代码中**使用注释来配置规则**, 被称为内联



### 规则严重级别（Rule Severities）

要更改规则的严重级别，将规则 ID 设置为以下值之一：

- `"off"` 或 `0` - 关闭该规则的校验
- `"warn"` 或 `1` - 将该规则作为警告启用（不影响退出码）。通常用于不强制遵守, 即使不遵守也可以通过CI/CD和提交代码,  或者临时关闭某个规则
- `"error"` 或 `2` - 将该规则作为错误启用（触发时退出码为 1）。通常用于在CI/CD, pre-commit, 拉取请求合并的时候, 要求强制遵守, 因为不遵守的话, ESLint会直接退出, 并且状态码为1

### 使用配置文件（Use Configuration Files）

要在[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file)中配置规则，使用 `rules` 键以及错误级别和你想使用的任何选项。例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		rules: {
			eqeqeq: "off",
			"no-unused-vars": "error",
			"prefer-const": ["error", { ignoreReadBeforeAssign: true }],
		},
	},
]);
```

当多个配置对象指定同一条规则时，规则配置会被**合并**，后面的对象优先于之前的任何对象。例如：

```js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		rules: {
			semi: ["error", "never"],
		},
	},
	{
		rules: {
			semi: ["warn", "always"],
		},
	},
]);
```

使用此配置时，`semi` 的最终规则配置是 `["warn", "always"]`，因为它在数组中最后出现。数组表示该配置同时包含严重级别和选项。你可以通过只定义一个字符串或数字来仅更改严重级别，如本例所示：

```js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		rules: {
			semi: ["error", "never"],
		},
	},
	{
		rules: {
			semi: "warn",
		},
	},
]);
```

这里，第二个配置对象只覆盖了严重级别，因此 `semi` 的最终配置是 `["warn", "never"]`。



> 通过内联注释的方式配置的规则具有最高优先级，并在所有配置合并之后才应用。

### 使用配置注释（Use configuration comments）

要在文件内使用配置注释来配置规则，使用以下格式的注释：

```js
/* eslint eqeqeq: "off", curly: "error" */
/* eslint eqeqeq: 0, curly: 2 */
```

在这个例子中，[`eqeqeq(强制要求使用===和!==, 不能使用==和!=)`](https://eslint.org/docs/latest/rules/eqeqeq) 被关闭，[`curly`](https://eslint.org/docs/latest/rules/curly) 被作为错误启用。





如果规则有额外的选项，你可以使用数组字面量语法来指定它们，例如：

```js
/* eslint quotes: ["error", "double"], curly: 2 */
```

数组中的第一项始终是规则严重级别（数字或字符串）。后面跟具体的额外选项

该注释为 [`quotes`](https://eslint.org/docs/latest/rules/quotes) 规则指定了 `"double"` 选项, 表示quotes规则强制字符串使用一致的引号风格, 并且是双引号



##### 配置注释描述（Configuration Comment Descriptions）

配置注释可以包含描述，以解释为什么需要该注释。描述必须出现在配置之后，并通过两个或更多连续的 `-` 字符与配置分隔。例如：

```js
/* eslint eqeqeq: "off", curly: "error" -- Here's a description about why this configuration is necessary. */
```

```js
/* eslint eqeqeq: "off", curly: "error"
    --------
    Here's a description about why this configuration is necessary. */
```

```js
/* eslint eqeqeq: "off", curly: "error"
 * --------
 * This will not work due to the line above starting with a '*' character.
 */
```



##### 报告未使用的 `eslint` 内联配置注释（Report unused `eslint` inline config comments）

 随着你的配置文件（eslint.config.js）不断变化，某些内联注释可能变得多余——它配置的内容和配置文件里已经设置的完全一样，没有带来任何改变。这种注释就叫"未使用的内联配置（unused inline config）"。

 比如配置文件里已经把 curly 设为 "error"：

```js
// eslint.config.js
export default defineConfig([
  { rules: { curly: "error" } },
]);
```

某源文件里又写了：

```js
/* eslint curly: "error" */   // ← 多余！和配置文件相同，没有任何作用
```

开启 reportUnusedInlineConfigs: "error" 后，ESLint 就会对这条注释报错，提醒你删掉它，保持代码整洁。

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		linterOptions: {
			reportUnusedInlineConfigs: "error",
		},
	},
]);
```

此设置默认为 `"off"`。

此设置类似于 [`--report-unused-inline-configs`](https://eslint.org/docs/latest/use/command-line-interface#--report-unused-inline-configs) CLI 选项。

> 要围绕配置注释强制执行最佳实践，请查看 [`eslint-plugin-eslint-comments`](https://github.com/eslint-community/eslint-plugin-eslint-comments)（[文档](https://eslint-community.github.io/eslint-plugin-eslint-comments/)）。



### 来自插件的规则（Rules from Plugins）

插件中也可以包含一些特有的规则, 如果你想要使用这些规则, 并配置告警等级, 那么在规则 ID 前加上插件命名空间和 `/`。

在[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file)中，例如：

```js
// eslint.config.js
import example from "eslint-plugin-example";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		plugins: {
            // 这里相当于"example": example
            // key是插件的名字, 可以随便定义, value是插件对象本身
            // 按照约定, 插件的npm包名字一般是eslint-plugin-aaa, 那么在导入插件的时候, key就配置为aaa
            // 之后就可以通过"key/rule_name"来引用插件中的规则了
			example,
		},
		rules: {
            // 上面plugins中定义的插件, 只是让eslint加载对应的插件, 并不会自动启用插件中的规则
            // 所以还需要再rules中定义规则的告警等级, 这样才会启用插件
			"example/rule1": "warn",
		},
	},
]);
```

在这个配置文件中，规则 `example/rule1` 来自名为 `eslint-plugin-example` 的插件。

你也可以在代码文件中使用注释来定义插件中的规则的告警等级，例如：

```js
/* eslint "example/rule1": "error" */
```

这种方式必须先在配置文件的plugins中配置example这个插件, 才可以这样

仅仅是使用注释并不会自动加载这个插件



### 禁用规则（Disable Rules）

#### 使用配置文件（Use configuration files）

要在[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file)中为一组文件禁用规则，使用一个带有 `files` 键的后续配置对象。例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		rules: {
			"no-unused-expressions": "error",
		},
	},
	{
		files: ["*-test.js", "*.spec.js"],
		rules: {
			"no-unused-expressions": "off", // 禁用no-unused-expressions规则
		},
	},
]);
```



#### 使用配置注释（Use configuration comments）

<font color=red>(不用看这一部分, 直接禁用内联注释)</font>

- **谨慎使用。** 仅在有明确且合理的情况下使用内联注释来禁用eslint的规则, 内联注释不应该成为解决 lint 错误的默认方案。
- **记录原因。** 使用内联注释禁用eslint规则的时候, 一定要在`--`的后面说明为什么禁用该规则以及为什么在特定情况下有必要这样做。
- **临时方案。** 如果使用内联注释来禁用规则, 只是为了解决紧迫问题的临时措施, 请创建后续任务以妥善解决根本问题。这可以确保禁用注释在之后被重新审视和解决。
- **代码审查和结对编程。** 鼓励团队成员定期审查彼此的代码。代码审查可以帮助识别禁用注释背后的原因，并确保它们被恰当使用。
- **配置。** **<font color=red>只要有可能，优先使用 ESLint 配置文件而不是禁用注释</font>**。配置文件可以实现一致的、项目范围的规则处理。

要禁用文件某一部分的规则警告，使用以下格式的块注释：

```js
/* eslint-disable */

alert("foo"); // 这中间的部分会禁用所有eslint规则

/* eslint-enable */
```

你也可以禁用或启用特定规则的警告：

```js
/* eslint-disable no-alert, no-console */

alert("foo");
console.log("bar");

/* eslint-enable no-alert, no-console */
```


要禁用整个文件的规则警告，在文件顶部放置一个 `/* eslint-disable */` 块注释：

```js
/* eslint-disable */   // 禁用所有规则

alert("foo");

/* eslint-enable * */ // 启用所有规则
```

你也可以为整个文件禁用或启用特定规则：

```js
/* eslint-disable no-alert */

alert("foo");
```

要确保某条规则永不被应用（无论之后有任何启用/禁用行）：

```js
/* eslint no-alert: "off" */

alert("foo");
```

要在特定行上禁用所有规则，使用以下格式之一的行注释或块注释：

```js
alert("foo"); // eslint-disable-line

// eslint-disable-next-line
alert("foo");

/* eslint-disable-next-line */
alert("foo");

alert("foo"); /* eslint-disable-line */
```

要在特定行上禁用特定规则：

```js
alert("foo"); // eslint-disable-line no-alert

// eslint-disable-next-line no-alert
alert("foo");

alert("foo"); /* eslint-disable-line no-alert */

/* eslint-disable-next-line no-alert */
alert("foo");
```

要在特定行上禁用多条规则：

```js
alert("foo"); // eslint-disable-line no-alert, quotes, semi

// eslint-disable-next-line no-alert, quotes, semi
alert("foo");

alert("foo"); /* eslint-disable-line no-alert, quotes, semi */

/* eslint-disable-next-line no-alert, quotes, semi */
alert("foo");

/* eslint-disable-next-line
  no-alert,
  quotes,
  semi
*/
alert("foo");
```

以上所有方法也适用于插件规则。例如，要禁用 `eslint-plugin-example` 的 `rule-name` 规则，将插件名称（`example`）和规则名称（`rule-name`）组合为 `example/rule-name`：

```js
foo(); // eslint-disable-line example/rule-name
foo(); /* eslint-disable-line example/rule-name */
```

> 禁用文件某一部分的规则, 告诉 ESLint即使代码有违规也不要报告。然而，ESLint 仍会解析整个文件，因此被禁用的代码仍然需要语法正确的 JavaScript 代码。

##### 注释描述（Comment descriptions）

禁用规则的注释可以包含描述，以解释为什么禁用或重新启用规则是必要的。

描述必须出现在配置之后，并需要通过两个或更多连续的 `-` 字符与配置分隔。例如：

```js
// eslint-disable-next-line no-console -- Here's a description about why this configuration is necessary.
console.log("hello");

/* eslint-disable-next-line no-console --
 * Here's a very long description about why this configuration is necessary
 * along with some additional information
 **/
console.log("hello");
```

#### 禁用内联注释（Disable Inline Comments）

要禁用所有内联配置注释，在配置文件中使用 `noInlineConfig` 设置。例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		linterOptions: {
			noInlineConfig: true,
		},
		rules: {
			"no-unused-expressions": "error",
		},
	},
]);
```

你也可以使用 [`--no-inline-config`](https://eslint.org/docs/latest/use/command-line-interface#--no-inline-config) CLI 选项来禁用规则注释以及其他内联配置。

##### 报告未使用的 `eslint-disable` 注释（Report unused `eslint-disable` comments）

要报告未使用的 `eslint-disable` 注释（即那些禁用了本不会在被禁用行上报问题的规则的注释），使用 `reportUnusedDisableDirectives` 设置。例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		linterOptions: {
			reportUnusedDisableDirectives: "error",
		},
	},
]);
```

此设置默认为 `"warn"`。

此设置类似于 [`--report-unused-disable-directives`](https://eslint.org/docs/latest/use/command-line-interface#--report-unused-disable-directives) 和 [`--report-unused-disable-directives-severity`](https://eslint.org/docs/latest/use/command-line-interface#--report-unused-disable-directives-severity) CLI 选项。


---

## 配置插件（Configure Plugins）

> 原文链接：https://eslint.org/docs/latest/use/configure/plugins

你可以使用插件来扩展eslint, 插件可以包含：

- 自定义规则：验证你的代码是否满足某个预期，以及当不满足预期时该做什么。
- 自定义配置：有关如何使用这些配置的详细信息，请参阅插件的文档。
- 自定义处理器：从其他类型的文件中提取 JavaScript 代码，或在 lint 之前对代码进行预处理。

### 配置插件（Configure Plugins）

ESLint 支持使用第三方插件。插件只是具有特定接口的对象。

要在[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file)中配置插件，使用 `plugins` 键，它包含一个对象，其属性表示插件命名空间，值为插件对象。

```js
// eslint.config.js
import example from "eslint-plugin-example";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		plugins: {
			example,
		},
		rules: {
			"example/rule1": "warn",
		},
	},
]);
```

> 为插件创建命名空间时，约定是使用不带 `eslint-plugin-` 前缀的 npm 包名。在前面的示例中，`eslint-plugin-example` 被赋予了 `example` 命名空间。



#### 配置本地插件（Configure a Local Plugin）

插件不需要发布到 npm 就能与 ESLint 一起使用。你也可以直接从文件加载插件，如本例所示：

```js
// eslint.config.js
import local from "./my-local-plugin.js";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		plugins: {
			local,
		},
		rules: {
			"local/rule1": "warn",
		},
	},
]);
```

这里使用了命名空间 `local`，但你也可以使用任何你喜欢的名称。



#### 配置虚拟插件（Configure a Virtual Plugin）

插件定义可以直接在你的配置中虚拟创建。例如，假设你有一条包含在名为 `my-rule.js` 的文件中的规则，你想在配置中启用它。你可以定义一个虚拟插件来实现，如本例所示：

```js
// eslint.config.js
import myRule from "./rules/my-rule.js"; // 1. 导入自定义的规则
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		plugins: {
             // 这里定义了一个名为local的插件, 但是他不是一个真正的插件, 只是一个虚拟的插件
			local: {
                // 这个插件包含了一个名为my-rule的规则
				rules: {
					"my-rule": myRule,
				},
			},
		},
		rules: {
            // 使用虚拟插件中的规则
			"local/my-rule": "warn",
		},
	},
]);
```

使用虚拟插件的优点在于, 你不需要创建完整的插件, 只需要创建自定义规则, 然后创建一个虚拟插件, 将你的自定义规则包含进去就可以按照插件的方式来使用你的自定义规则

( 插件的完整格式请参见[创建插件](https://eslint.org/docs/latest/extend/plugins)。）



### 使用插件中的规则（Use Plugin Rules）

有些插件会包含规则, 要使用这些规则，可以在配置对象中使用 `plugins` 键指定插件。`plugins` 键的值是一个对象，其中插件名称是属性名，值是插件对象本身。下面是一个示例：

```js
// eslint.config.js
import jsdoc from "eslint-plugin-jsdoc";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.js"],
		plugins: {
            // 让eslint加载这个jsdoc插件
            // key是插件的名字, 可以随便定义, 但是一般按照约定俗成, 插件是eslint-plugin-jsdoc, 那么插件名字就是去掉eslint-plugin-前缀
            // values是具体的插件对象
			jsdoc: jsdoc, 
		},
		rules: {
            // 使用"插件名字/规则名字" 来定义插件中的规则的告警等级
			"jsdoc/require-description": "error",
			"jsdoc/check-values": "error",
		},
	},
]);
```

在此配置中，JSDoc 插件被定义为名称 `jsdoc`。每条规则名称中的前缀 `jsdoc/` 表示该规则来自该名称的插件，而不是来自 ESLint 本身。

因为插件名称和插件对象都是 `jsdoc`，你也可以将配置简写为：

```js
import jsdoc from "eslint-plugin-jsdoc";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.js"],
		plugins: {
			jsdoc,
		},
		rules: {
			"jsdoc/require-description": "error",
			"jsdoc/check-values": "error",
		},
	},
]);
```

虽然这是最常见的约定，但你不必使用插件规定的相同名称。你可以指定任何你喜欢的前缀，例如：

```js
import jsdoc from "eslint-plugin-jsdoc";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.js"],
		plugins: {
			jsd: jsdoc,
		},
		rules: {
			"jsd/require-description": "error",
			"jsd/check-values": "error",
		},
	},
]);
```

此配置对象使用 `jsd` 作为插件前缀，而不是 `jsdoc`。



### 指定插件中的处理器（Specify a Processor）

插件可以提供处理器。处理器可以从其他类型的文件中提取 JavaScript 代码，然后让 ESLint 检查这些 JavaScript 代码。或者，处理器可以在预处理期间转换 JavaScript 代码。

要在[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file)中指定处理器，使用 `processor` 键，并以 `namespace/processor-name` 格式指定处理器名称。例如，下面对 `*.md` 文件使用来自 `@eslint/markdown` 的处理器：

```js
// eslint.config.js
import markdown from "@eslint/markdown";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.md"],
		plugins: {
			markdown,
		},
		processor: "markdown/markdown",
	},
]);
```



处理器可以生成命名代码块，例如 `0.js` 和 `1.js`。ESLint 将这样的命名代码块视为原始文件的子文件。你可以用额外的配置对象为命名代码块指定额外的配置。例如，下面为 markdown 文件中以 `.js` 结尾的命名代码块禁用了 `strict` 规则：

```js
// eslint.config.js
import markdown from "@eslint/markdown";
import { defineConfig } from "eslint/config";

export default defineConfig([
	// 应用于所有 JavaScript 文件
	{
		rules: {
			strict: "error",
		},
	},

	// 应用于 Markdown 文件
	{
		files: ["**/*.md"],
		plugins: {
			markdown,
		},
		processor: "markdown/markdown",
	},

	// 仅应用于 Markdown 文件内的 JavaScript 块
	{
		files: ["**/*.md/*.js"],
		rules: {
			strict: "off",
		},
	},
]);
```

ESLint 只在命名代码块是 JavaScript 文件，或它们匹配某个配置对象中的 `files` 条目时才会检查它们。如果你想检查非 JavaScript 的命名代码块，请务必添加一个带有匹配 `files` 条目的配置对象。另请注意，[全局忽略](https://eslint.org/docs/latest/use/configure/ignore)也适用于命名代码块。

```js
// eslint.config.js
import markdown from "@eslint/markdown";
import { defineConfig } from "eslint/config";

export default defineConfig([
	// 应用于 Markdown 文件
	{
		files: ["**/*.md"],
		plugins: {
			markdown,
		},
		processor: "markdown/markdown",
	},

	// 应用于所有 .jsx 文件，包括 Markdown 文件内的 jsx 块
	{
		files: ["**/*.jsx"],
		languageOptions: {
			parserOptions: {
				ecmaFeatures: {
					jsx: true,
				},
			},
		},
	},

	// 忽略 test.md 文件内的 jsx 块
	{
		ignores: ["**/test.md/*.jsx"],
	},
]);
```



### 指定语言（Specify a Language）

插件可以提供语言。语言让 ESLint 能够检查 JavaScript 以外的编程语言。要在[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file)中指定语言，使用 `language` 键，并以 `namespace/language-name` 格式指定语言名称。例如，下面对 `*.json` 文件使用来自 `@eslint/json` 的 `json/jsonc` 语言：

```js
// eslint.config.js
import json from "@eslint/json";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.json"],
		plugins: {
			json,
		},
		language: "json/jsonc",
	},
]);
```

> 当你在配置对象中指定 `language` 时，`languageOptions` 就变成特定于该语言的。每种语言定义自己的 `languageOptions`，因此请查看插件的文档以确定哪些选项可用。



### 常见问题（Common Problems）

- [使用 ESLint < v9.0.0 API 的插件规则](https://eslint.org/docs/latest/use/troubleshooting/v9-rule-api-changes)
- [插件配置尚未升级到 flat config](https://eslint.org/docs/latest/use/configure/migration-guide#using-eslintrc-configs-in-flat-config)




## 配置解析器（Configure a Parser）

> 原文链接：https://eslint.org/docs/latest/use/configure/parser



你可以使用自定义解析器将 JavaScript 代码转换为抽象语法树（AST），供 ESLint 进行校验。

<font color=red>ESLint使用Espree作为默认的解析器， 如果Espress无法解析你的代码， 那么你可能需要添加自定义解析器。</font>



### 配置自定义解析器（Configure a Custom Parser）

在许多情况下，你可以使用 ESLint 自带的[默认解析器(espree)](https://github.com/eslint/js/tree/main/packages/espree)来解析你的 JavaScript 代码。你可以选择性地使用 `languageOptions.parser` 属性覆盖默认解析器。`languageOptions.parser` 属性必须是一个符合[解析器接口](https://eslint.org/docs/latest/extend/custom-parsers)的对象。例如，你可以使用 [`@babel/eslint-parser`](https://www.npmjs.com/package/@babel/eslint-parser) 包来让 ESLint 解析实验性语法：

```js
// eslint.config.js
import babelParser from "@babel/eslint-parser";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.js", "**/*.mjs"],
		languageOptions: {
			parser: babelParser,
		},
	},
]);
```

此配置确保使用 Babel 解析器（而非默认的 Espree 解析器）来解析所有以 `.js` 和 `.mjs` 结尾的文件。

已知以下第三方解析器与 ESLint 兼容：

- [Esprima](https://www.npmjs.com/package/esprima)
- [@babel/eslint-parser](https://www.npmjs.com/package/@babel/eslint-parser) - 对 [Babel](https://babeljs.io) 解析器的封装，使其与 ESLint 兼容。
- [@typescript-eslint/parser](https://www.npmjs.com/package/@typescript-eslint/parser) - 将 TypeScript 转换为 ESTree 兼容形式的解析器，使其可以在 ESLint 中使用。

> 不能保证外部解析器能与 ESLint 正常配合工作。对于仅影响第三方解析器的不兼容性问题，ESLint 不会修复相关 bug。



### 配置解析器选项（Configure Parser Options）

解析器可以接受选项来改变其行为。`languageOptions.parserOptions` 用于直接向解析器传递选项。每个解析器的选项都不同，因此你需要查阅所使用解析器的文档以了解可用选项。下面是为 Babel ESLint 解析器设置解析器选项的示例：

```js
// eslint.config.js
import babelParser from "@babel/eslint-parser";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		languageOptions: {
			parser: babelParser,
			parserOptions: {
				requireConfigFile: false,
				babelOptions: {
					babelrc: false,
					configFile: false,
					presets: ["@babel/preset-env"],
				},
			},
		},
	},
]);
```

::: tip
除了在 `languageOptions.parserOptions` 中指定的选项外，ESLint 还会将 `languageOptions.ecmaVersion` 和 `languageOptions.sourceType` 传递给所有解析器。ESLint 内部使用 `languageOptions.ecmaVersion`（例如，用于添加内置全局变量），传递这些值可以让解析器了解 ESLint 求值 JavaScript 代码的上下文。如果你需要专门向解析器传递不同的 `ecmaVersion` 或 `sourceType`，可以设置 `languageOptions.parserOptions.ecmaVersion` 或 `languageOptions.parserOptions.sourceType`。对于解析器而言，这些选项将优先于对应的 `languageOptions`。
:::




## 合并配置（Combine Configs）

> 原文链接：https://eslint.org/docs/latest/use/configure/combine-configs

在许多情况下，你不会从头开始编写 ESLint 配置文件，而是会使用预定义配置和可共享配置，再加上你自己的配置来覆盖前者, 达到自定义的效果，来创建项目的配置。本页介绍一些可以在配置文件中用来合并配置的模式。

### 应用一个配置对象（Apply a Config Object）

如果你从另一个模块导入一个[配置对象](https://eslint.org/docs/latest/use/core-concepts/glossary#config-object)，你可以通过创建一个带有 `files` 键的新对象，并使用 `extends` 键合入导入这个配置对象的其余属性，从而将其仅应用于特定的文件。例如：

```js
// eslint.config.js
import js from "@eslint/js"; // 1. 导入js插件对象
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.js"], // 5. 将这些规则应用到所有的js文件上面
		plugins: {
			js, // 2. 让eslint加载js插件对象
		},
		extends: ["js/recommended"], // 3. recommend会启用一批官方推荐的 JavaScript 规则。
		rules: {
			"no-unused-vars": "warn", // 4. 重新调整特定规则的告警等级
		},
	},
]);
```



### 应用一个配置数组（Apply a Config Array）

如果你从另一个模块导入一个[配置数组](https://eslint.org/docs/latest/use/core-concepts/glossary#config-array)，你可以通过使用 `extends` 键将配置数组（配置对象的数组）仅应用于文件的子集。

假设 `eslint-config-example` 是一个共享配置包，它导出的内容如下：

```js
// eslint-config-example/index.js
export default [
    {
        rules: {
            "no-console": "error",
        },
    },
    {
        rules: {
            "eqeqeq": "error",
        },
    },
];
```

注意，这里导出的是一个数组，而不是一个普通配置对象。那么你可以这样使用这个共享配置包中的配置

```js
// eslint.config.js
import exampleConfigs from "eslint-config-example";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.js"],
		extends: [exampleConfigs], // 继承exampleConfigs中的所有配置, 并只应用于**/*.js文件
		rules: {
			"no-unused-vars": "warn",
		},
	},
]);
```

上面的配置等效于

~~~js
[
    {
        files: ["**/*.js"],
        rules: {
            "no-console": "error",
        },
    },
    {
        files: ["**/*.js"],
        rules: {
            "eqeqeq": "error",
        },
    },
    {
        files: ["**/*.js"],
        rules: {
            "no-unused-vars": "warn",
        },
    },
]
~~~






## 全局忽略文件（Ignore Files）

> 原文链接：https://eslint.org/docs/latest/use/configure/ignore

> 本页介绍如何使用 `globalIgnores()` 函数完全忽略文件和目录。有关非全局忽略的更多信息，请参见[指定 files 和 ignores](https://eslint.org/docs/latest/use/configure/configuration-files#specifying-files-with-files-and-ignores)。有关全局忽略和非全局忽略之间区别的更多信息，请参见[使用 `ignores` 全局忽略文件](https://eslint.org/docs/latest/use/configure/configuration-files#globally-ignoring-files-with-ignores)。

你可以通过以下方式指定一个或多个 glob 模式，让 ESLint 在检查时忽略某些文件和目录：

- 在你的 `eslint.config.js` 文件中。
- 在命令行上使用 `--ignore-pattern`。

### 全局忽略文件（Ignore Files）

在你的 `eslint.config.js` 文件中，你可以使用 `globalIgnores()` 辅助函数来指示要忽略的文件模式。下面是一个示例：

```js
// eslint.config.js
import { defineConfig, globalIgnores } from "eslint/config";

export default defineConfig([
    globalIgnores([".config/*"]) // 注意这里只匹配一层, .config/sub/index.js不会被忽略
]);
```

此配置指定 `.config` 目录中的所有文件都应被忽略。

> eslint中已经内置了两个全局忽略的目录 `["**/node_modules/", ".git/"]`。



默认情况下，ESLint会检查 `**/*.js`、`**/*.cjs` 和 `**/*.mjs` 。如果你不想让 ESLint 检查这些文件，可以使用 `globalIgnores()` 忽略它们：

```js
// eslint.config.js
import { defineConfig, globalIgnores } from "eslint/config";

export default defineConfig([
	globalIgnores(["**/*.js", "**/*.cjs", "**/*.mjs"]),
]);
```





当 `files` 属性中指定了非 JS 文件时，ESLint 仍会检查默认的`**/*.js`、`**/*.cjs` 和 `**/*.mjs`文件。

要只检查 `files` 属性中指定的文件，你必须忽略默认文件模式：

```js
// eslint.config.js
import { defineConfig, globalIgnores } from "eslint/config";

export default defineConfig([
    // 忽略js, cjs, mjs文件
	globalIgnores(["**/*.js", "**/*.cjs", "**/*.mjs"]),
	{
		files: ["**/*.ts"], // 检查ts文件
		ignores: [".config/**"],
		rules: {/* ... */},
	},
]);
```



你也可以在命令行上使用 [`--ignore-pattern`](https://eslint.org/docs/latest/use/command-line-interface#--ignore-pattern) 忽略文件，例如：

```shell
npx eslint . --ignore-pattern '.config/*'
```



### 全局忽略目录（Ignore Directories）

忽略目录的方式与忽略文件相同，即向 `globalIgnores()` 辅助函数传递一个模式。例如，下面整体忽略 `.config` 目录（意味着文件搜索完全不会进入该目录）：

```js
// eslint.config.js
import { defineConfig, globalIgnores } from "eslint/config";

export default defineConfig([
    globalIgnores([".config/"])
]);
```

与 `.gitignore` 不同，像 `.config` 这样的忽略模式只会忽略与配置文件位于同一目录中的 `.config` 目录。如果你想递归地忽略所有名为 `.config` 的目录，需要使用 `**/.config/`，如本例所示：

```js
// eslint.config.js
import { defineConfig, globalIgnores } from "eslint/config";

export default defineConfig([globalIgnores(["**/.config/"])]);
```



### 取消忽略文件和目录（Unignore Files and Directories）

你也可以取消忽略被先前模式（包括默认模式）忽略的文件和目录。例如，此配置取消忽略了 `node_modules/mylibrary`：

```js
// eslint.config.js
import { defineConfig, globalIgnores } from "eslint/config";

export default defineConfig([
	globalIgnores([
		"!node_modules/", // 取消忽略 `node_modules/` 目录
		"node_modules/*", // 忽略其内容
		"!node_modules/mylibrary/", // 取消忽略 `node_modules/mylibrary` 目录
	]),
]);
```

如果你想忽略一个目录但排除特定的文件或子目录，则必须使用忽略模式 `directory/**/*` 而不是 `directory/**`。模式 `directory/**` 会忽略整个目录及其内容，因此遍历会完全跳过该目录，你无法取消忽略其中的任何内容。

例如，`build/**` 忽略 `build` 目录及其内容，而 `build/**/*` 只忽略其内容。如果你想忽略 `build` 目录中除 `build/test.js` 之外的所有内容，你需要创建如下配置：

```js
// eslint.config.js
import { defineConfig, globalIgnores } from "eslint/config";

export default defineConfig([
	globalIgnores([
		"build/**/*", // 忽略 `build/` 目录内及其下的所有内容，但不忽略 `build/` 目录本身
		"!build/test.js", // 取消忽略 `build/test.js`
	]),
]);
```



如果你想忽略一个目录，但排除该目录下任意层级的特定文件，你还应确保子目录不被忽略。请注意，以 `/` 结尾的模式只匹配目录，而不以 `/` 结尾的模式同时匹配文件和目录，因此不可能写出只忽略文件的单一模式，但你可以用两个模式来实现：一个忽略所有内容，另一个取消忽略子目录。

例如，此配置忽略 `build` 目录内及其下的所有文件，但任意层级下名为 `test.js` 的文件除外：

```js
// eslint.config.js
import { defineConfig, globalIgnores } from "eslint/config";

export default defineConfig([
	globalIgnores([
		"build/**/*", // 忽略 `build/` 目录内及其下的所有内容，但不忽略 `build/` 目录本身
		"!build/**/*/", // 取消忽略所有子目录
		"!build/**/test.js", // 取消忽略 `test.js` 文件
	]),
]);
```



如果你想忽略 ESLint 的默认文件模式（`**/*.js`、`**/*.cjs` 和 `**/*.mjs`），同时仍检查特定文件或目录，可以使用否定模式来取消忽略它们：

```js
// eslint.config.js
import { defineConfig, globalIgnores } from "eslint/config";

export default defineConfig([
	globalIgnores(["**/*.js", "**/*.cjs", "**/*.mjs", "!**/src/**/*.js"]),
]);
```

此配置忽略所有匹配默认模式的文件，但位于 `src` 目录中的 JavaScript 文件除外。

>请注意，只有全局 `ignores` 模式可以匹配目录。
>特定于某个配置的 `ignores` 模式只会匹配文件名。



你也可以在命令行上使用 [`--ignore-pattern`](https://eslint.org/docs/latest/use/command-line-interface#--ignore-pattern) 取消忽略文件，例如：

```shell
npx eslint . --ignore-pattern '!node_modules/'
```





### Glob 模式解析（Glob Pattern Resolution）

glob 模式的求值方式取决于它们所在的位置和使用方式：

1. 当在 `eslint.config.js` 文件中使用 `globalIgnores()` 时，glob 模式相对于 `eslint.config.js` 文件求值。
1. 当在使用 [`--config`](https://eslint.org/docs/latest/use/command-line-interface#-c---config) 命令行选项指定的替代配置文件中使用 `globalIgnores()` 时，glob 模式相对于当前工作目录求值。
1. 当使用 [`--ignore-pattern`](https://eslint.org/docs/latest/use/command-line-interface#--ignore-pattern) 时，glob 模式相对于当前工作目录求值。



### 命名全局忽略配置（Name the Global Ignores Config）

默认情况下，`globalIgnores()` 会为表示你的忽略项的配置分配一个名称。你可以通过向 `globalIgnores()` 提供第二个参数来覆盖此名称，该参数是你想用来代替默认名称的名称：

```js
// eslint.config.js
import { defineConfig, globalIgnores } from "eslint/config";

export default defineConfig([
	globalIgnores(["build/**/*"], "Ignore Build Directory"),
]);
```

本例中的 `"Ignore Build Directory"` 字符串是为全局忽略创建的配置的名称。这对调试很有用。



### 已忽略文件警告（Ignored File Warnings）

当你向 ESLint CLI 传递目录时，文件和目录会被静默忽略。如果你向 ESLint 传递一个特定文件，ESLint 会创建一个该文件被跳过的警告。例如，假设你有一个如下所示的 `eslint.config.js` 文件：

```js
// eslint.config.js
import { defineConfig, globalIgnores } from "eslint/config";

export default defineConfig([globalIgnores(["foo.js"])]);
```

然后你运行：

```shell
npx eslint foo.js
```

你会看到这个警告：

```text
foo.js
  0:0  warning  File ignored because of a matching ignore pattern. Use "--no-ignore" to disable file ignore settings or use "--no-warn-ignored" to suppress this warning.

✖ 1 problem (0 errors, 1 warning)
```

出现此消息是因为 ESLint 不确定你是否真的想检查该文件。如消息所示，你可以使用 `--no-ignore` 来不使用忽略规则。



### 包含 `.gitignore` 文件（Include `.gitignore` Files）

如果你想包含来自 [`.gitignore`](https://git-scm.com/docs/gitignore) 文件或任何其他包含 gitignore 风格模式的文件中的模式，可以使用 `includeIgnoreFile` 工具函数。

```js
// eslint.config.js

import { defineConfig, includeIgnoreFile } from "eslint/config";
import { fileURLToPath } from "node:url";

const gitignorePath = fileURLToPath(new URL(".gitignore", import.meta.url));

export default defineConfig([
	includeIgnoreFile(gitignorePath, { gitignoreResolution: true }),
	{
		// 你的覆盖配置
	},
]);
```

这会自动加载指定的文件，并将 gitignore 风格的模式转换为包含 `ignores` glob 模式的配置对象。`{ gitignoreResolution: true }` 选项确保模式将相对于指定文件的位置进行解释。

也可以提供忽略文件路径的数组：

```js
// eslint.config.js

import { defineConfig, includeIgnoreFile } from "eslint/config";
import { fileURLToPath } from "node:url";

const rootGitignorePath = fileURLToPath(new URL(".gitignore", import.meta.url));
const nestedGitignorePath = fileURLToPath(
	new URL("some/other/folder/.gitignore", import.meta.url),
);

export default defineConfig([
	includeIgnoreFile([rootGitignorePath, nestedGitignorePath], {
		gitignoreResolution: true,
	}),
	{
		// 你的覆盖配置
	},
]);
```

在这种情况下，将返回一个配置对象数组。

#### 包含其他忽略文件（Include other ignore files）

如果省略 `{ gitignoreResolution: true }`，或者指定 `{ gitignoreResolution: false }`，`includeIgnoreFile()` 将相对于 ESLint 配置文件的位置来解释忽略模式。

```js
// eslint.config.js

import { defineConfig, includeIgnoreFile } from "eslint/config";
import { fileURLToPath } from "node:url";

const eslintIgnorePath = fileURLToPath(
	new URL("eslint-ignore-patterns", import.meta.url),
);

export default defineConfig([
	includeIgnoreFile(eslintIgnorePath),
	{
		// 你的覆盖配置
	},
]);
```

相比之下，`.gitignore` 文件的语义是忽略模式应相对于 `.gitignore` 文件本身的位置进行解释。因此，当包含来自 `.gitignore` 文件的忽略模式时，应始终使用 `{ gitignoreResolution: true }`——它告诉 `includeIgnoreFile()` 匹配这种行为。

#### 自定义输出配置的名称（Customize name of output configs）

默认情况下，`includeIgnoreFile()` 会为表示你的忽略项的配置分配一个名称。你可以通过在 `includeIgnoreFile()` 的第二个参数中提供名称来覆盖它：

```js
// eslint.config.js

import { defineConfig, includeIgnoreFile } from "eslint/config";
import { fileURLToPath } from "node:url";

const gitignorePath = fileURLToPath(new URL(".gitignore", import.meta.url));

export default defineConfig([
	includeIgnoreFile(gitignorePath, {
		gitignoreResolution: true,
		name: "Imported .gitignore patterns",
	}),
	{
		// 你的覆盖配置
	},
]);
```




## 调试你的配置（Debug Your Configuration）

> 原文链接：https://eslint.org/docs/latest/use/configure/debug

有时候你的eslint的配置很多, 还可能有多个配置文件

如果你不确定为什么某个文件没有被检测, 那么eslint提供了如下几个模式来帮助你调试





### 在调试模式下运行 CLI（Run the CLI in Debug Mode）

**适用场景：** 但你的项目有多个eslint配置文件, 而你不知道到底使用的是哪一个的时候

**操作方法：** 带上 [`--debug`](https://eslint.org/docs/latest/use/command-line-interface#--debug) 命令行标志运行 ESLint，并传入要检查的文件，如下所示：

```shell
npx eslint --debug file.js
```

这会将 ESLint 的所有调试信息输出到控制台。你应该将此输出复制到一个文件中，然后搜索 `eslint.config.js` 来查看加载了哪个文件。下面是一些示例输出：

```text
eslint:eslint Using file patterns: [ 'bin/eslint.js' ] +0ms
eslint:eslint Searching for eslint.config.js +0ms
eslint:eslint Loading config from C:\Users\nzakas\projects\eslint\eslint\eslint.config.js +5ms
eslint:eslint Config file URL is file:///C:/Users/nzakas/projects/eslint/eslint/eslint.config.js +0ms
```





### 打印文件计算后的配置（Print a File's Calculated Configuration）

**适用场景：** 你不确定为什么 lint 检查没有产生预期结果，可能是因为你的规则配置似乎没有被采纳，或者使用了错误的语言选项。

**操作方法：** 带上 [`--print-config`](https://eslint.org/docs/latest/use/command-line-interface#--print-config) 命令行标志运行 ESLint，并传入要检查的文件，如下所示：

```shell
npx eslint --print-config file.js
```

这会输出该文件计算后配置的 JSON 表示，例如：

```json
{
	"linterOptions": {
		"reportUnusedDisableDirectives": 1
	},
	"language": "@/js",
	"languageOptions": {
		"sourceType": "module",
		"ecmaVersion": "latest"
	},
	"plugins": ["@"],
	"rules": {
		"prefer-const": 2
	}
}
```

::: tip
你不会看到 `files`、`ignores` 或 `name` 的条目，因为它们只用于计算最终配置，因此不会出现在结果中。你会看到 ESLint 自身应用的任何默认配置。
:::



### 使用配置检查器（Use the Config Inspector）

**适用场景：** 你不确定配置文件中的某些配置对象是否匹配给定的文件名。

**操作方法：** 带上 [`--inspect-config`](https://eslint.org/docs/latest/use/command-line-interface#--inspect-config) 命令行标志运行 ESLint，如下所示：

```shell
npx eslint --inspect-config
```

这会通过安装并启动 [`@eslint/config-inspector`](https://github.com/eslint/config-inspector) 来启动配置检查器。然后你可以输入相关文件名，查看哪些配置对象会被应用。

![配置检查器截图，显示哪些配置对象匹配 index.js](https://eslint.org/docs/latest/assets/images/configure/config-inspector.png)

配置检查器还会显示哪些规则已废弃、你正在使用的可用规则数量等信息。



## 配置迁移指南（Configuration Migration Guide）

(<font color=red> 这部分不用看,  前一个得儿</font>)

> 原文链接：https://eslint.org/docs/latest/use/configure/migration-guide

本指南概述了如何将你的 ESLint 配置文件从 eslintrc 格式（通常在 `.eslintrc.js` 或 `.eslintrc.json` 文件中配置）迁移到新的 flat config 格式（通常在 `eslint.config.js` 文件中配置）。

要了解更多关于 flat config 格式的信息，请参阅[这篇博客文章](https://eslint.org/blog/2022/08/new-config-system-part-2/)。

有关 flat 配置文件的参考信息，请参阅[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files)。

### 迁移你的配置文件（Migrate Your Config File）

首先，在你现有的配置文件（`.eslintrc`、`.eslintrc.json`、`.eslintrc.yml`）上使用[配置迁移工具](https://npmjs.com/package/@eslint/migrate-config)，如下所示：

```shell
npx @eslint/migrate-config .eslintrc.json
```

这会为你的 `eslint.config.js` 文件创建一个起点，但不保证无需进一步修改就能立即工作。不过，它会自动完成本指南中提到的大部分转换工作。

::: important
配置迁移工具对 `.eslintrc.js` 文件的支持还不够好。如果你使用的是 `.eslintrc.js`，迁移结果是一个与你的配置求值输出相匹配的配置文件，其中不会包含任何函数、条件语句或配置中原始数据以外的任何内容。
:::

### 开始使用 Flat Config 文件（Start Using Flat Config Files）

自 ESLint v9.0.0 起，flat config 文件格式已成为默认的配置文件格式。你可以直接使用 flat config 文件格式，无需任何额外配置。

要在 ESLint v8 中使用 flat config，请在项目根目录放置一个 `eslint.config.js` 文件，**或者**将 `ESLINT_USE_FLAT_CONFIG` 环境变量设置为 `true`。

### 两种配置文件格式之间没有变化的内容（Things That Haven’t Changed between Configuration File Formats）

虽然配置文件格式从 eslintrc 变为了 flat config，但以下内容保持不变：

- 配置规则的语法。
- 配置处理器的语法。
- CLI，除了 [CLI 标志变更](#cli-标志变更)中提到的标志变化。
- 全局变量的配置方式相同，但在不同的属性上（见[配置语言选项](#配置语言选项)）。

### 两种配置格式之间的关键区别（Key Differences between Configuration Formats）

eslintrc 和 flat config 格式之间最值得注意的几个区别如下：

#### 导入插件和自定义解析器（Import Plugins and Custom Parsers）

eslintrc 文件在 `plugins` 属性内使用基于字符串的导入系统来加载插件，在 `extends` 属性内加载外部配置。

flat config 文件将插件和解析器表示为 JavaScript 对象。这意味着你可以使用 CommonJS `require()` 或 ES 模块 `import` 语句从外部文件加载插件和自定义解析器。

例如，这个 eslintrc 配置文件加载 `eslint-plugin-jsdoc` 并配置来自该插件的规则：

```javascript
// .eslintrc.js

module.exports = {
	// ...其他配置
	plugins: ["jsdoc"],
	rules: {
		"jsdoc/require-description": "error",
		"jsdoc/check-values": "error",
	},
	// ...其他配置
};
```

在 flat config 中，你会这样做同样的事情：

```javascript
// eslint.config.js

import { defineConfig } from "eslint/config";
import jsdoc from "eslint-plugin-jsdoc";

export default defineConfig([
	{
		files: ["**/*.js"],
		plugins: {
			jsdoc: jsdoc,
		},
		rules: {
			"jsdoc/require-description": "error",
			"jsdoc/check-values": "error",
		},
	},
]);
```

::: tip
如果你导入一个插件并遇到诸如 "TypeError: context.getScope is not a function" 的错误，这意味着该插件尚未更新到 ESLint v9.x 规则 API。虽然你应该向相应的插件提交 issue，但你可以使用[兼容性工具](https://eslint.org/blog/2024/05/eslint-compatibility-utilities/)手动修补插件，使其在 ESLint v9.x 中工作。
:::

#### 自定义解析器（Custom Parsers）

在 eslintrc 文件中，导入自定义解析器类似于导入插件：使用字符串指定解析器的名称。

在 flat config 文件中，将自定义解析器作为模块导入，然后将其赋值给配置对象的 `languageOptions.parser` 属性。

例如，这个 eslintrc 配置文件使用 `@babel/eslint-parser` 解析器：

```javascript
// .eslintrc.js

module.exports = {
	// ...其他配置
	parser: "@babel/eslint-parser",
	// ...其他配置
};
```

在 flat config 中，你会这样做同样的事情：

```javascript
// eslint.config.js

import { defineConfig } from "eslint/config";
import babelParser from "@babel/eslint-parser";

export default defineConfig([
	{
		// ...其他配置
		languageOptions: {
			parser: babelParser,
		},
		// ...其他配置
	},
]);
```

#### 处理器（Processors）

在 eslintrc 文件中，处理器必须在插件中定义，然后在配置中按名称引用。以点开头的处理器表示以文件扩展名命名的处理器，ESLint 会自动为该文件扩展名配置它。

在 flat config 文件中，处理器仍可以按名称从插件中引用，但现在也可以直接插入配置中。处理器*永远不会*被自动配置，必须在配置中显式设置。

以一个带处理器的自定义插件为例：

```javascript
// node_modules/eslint-plugin-someplugin/index.js
module.exports = {
	processors: {
		".md": {
			preprocess() {},
			postprocess() {},
		},
		someProcessor: {
			preprocess() {},
			postprocess() {},
		},
	},
};
```

在 eslintrc 中，你会这样配置：

```javascript
// .eslintrc.js
module.exports = {
	plugins: ["someplugin"],
	processor: "someplugin/someProcessor",
};
```

ESLint 还会自动添加与以下等价的内容：

```javascript
{
	overrides: [
		{
			files: ["**/*.md"],
			processor: "someplugin/.md",
		},
	];
}
```

在 flat config 中，以下都是表达相同含义的有效方式：

```javascript
// eslint.config.js
import { defineConfig } from "eslint/config";
import somePlugin from "eslint-plugin-someplugin";

export default defineConfig([
	{
		plugins: { somePlugin },
		processor: "somePlugin/someProcessor",
	},
	{
		plugins: { somePlugin },
		// 我们可以将处理器对象直接嵌入配置中
		processor: somePlugin.processors.someProcessor,
	},
	{
		// 直接使用处理器时，插件不需要出现在配置中
		processor: somePlugin.processors.someProcessor,
	},
]);
```

请注意，由于 `.md` 处理器*不会*被 flat config 自动添加，你还需要指定一个额外的配置元素：

```javascript
{
    files: ["**/*.md"],
    processor: somePlugin.processors[".md"]
}
```

#### 基于 Glob 的配置（Glob-Based Configs）

默认情况下，eslintrc 文件检查其所在目录及其子目录中的所有文件（被 `.eslintignore` 覆盖的除外）。如果你想为不同的文件 glob 模式使用不同的配置，可以在 `overrides` 属性中指定。

默认情况下，flat config 文件在导出的数组中支持基于不同 glob 模式的配置。你可以在配置对象的 `files` 属性中包含 glob 模式。如果你不指定 `files` 属性，配置默认使用 glob 模式 `"**/*.{js,mjs,cjs}"`。基本上，flat config 文件中的所有配置都类似于 eslintrc 的 `overrides` 属性。

##### eslintrc 示例（eslintrc Examples）

例如，这个 eslintrc 文件应用于其所在目录及其子目录中的所有文件：

```javascript
// .eslintrc.js

module.exports = {
	// ...其他配置
	rules: {
		semi: ["warn", "always"],
	},
};
```

这个 eslintrc 文件使用 overrides 支持多个配置：

```javascript
// .eslintrc.js

module.exports = {
	// ...其他配置
	overrides: [
		{
			files: ["src/**/*"],
			rules: {
				semi: ["warn", "always"],
			},
		},
		{
			files: ["test/**/*"],
			rules: {
				"no-console": "off",
			},
		},
	],
};
```

对于 flat config，下面是一个使用默认 glob 模式的配置：

```javascript
// eslint.config.js

import { defineConfig } from "eslint/config";
import js from "@eslint/js";

export default defineConfig([
	js.configs.recommended, // 应用于所有文件的推荐配置
	// 覆盖推荐配置
	{
		rules: {
			indent: ["error", 2],
			"no-unused-vars": "warn",
		},
		// ...其他配置
	},
]);
```

一个支持针对不同 glob 模式的多个配置的 flat config 示例：

```javascript
// eslint.config.js

import { defineConfig } from "eslint/config";
import js from "@eslint/js";

export default defineConfig([
	js.configs.recommended, // 应用于所有文件的推荐配置
	// 针对特定文件模式的覆盖
	{
		files: ["src/**/*", "test/**/*"],
		rules: {
			semi: ["warn", "always"],
		},
	},
	{
		files: ["test/**/*"],
		rules: {
			"no-console": "off",
		},
	},
	// ...其他配置
]);
```

#### 配置语言选项（Configure Language Options）

在 eslintrc 文件中，你通过 `env`、`globals` 和 `parserOptions` 属性配置各种语言选项。特定运行时的全局变量组（例如浏览器 JavaScript 的 `document` 和 `window`；Node.js 的 `process` 和 `require`）通过 `env` 属性配置。

在 flat config 文件中，`globals` 和 `parserOptions` 被整合到 `languageOptions` 键下；`env` 属性不复存在。特定运行时的全局变量组从 [globals](https://www.npmjs.com/package/globals) npm 包导入，并包含在 `globals` 属性中。你可以使用展开运算符（`...`）一次导入多个全局变量。

例如，下面是一个带语言选项的 eslintrc 文件：

```javascript
// .eslintrc.js

module.exports = {
	env: {
		browser: true,
		node: true,
	},
	globals: {
		myCustomGlobal: "readonly",
	},
	parserOptions: {
		ecmaVersion: 2022,
		sourceType: "module",
	},
	// ...其他配置
};
```

这是 flat config 中的相同配置：

```javascript
// eslint.config.js

import { defineConfig } from "eslint/config";
import globals from "globals";

export default defineConfig([
	{
		languageOptions: {
			ecmaVersion: 2022,
			sourceType: "module",
			globals: {
				...globals.browser,
				...globals.node,
				myCustomGlobal: "readonly",
			},
		},
		// ...其他配置
	},
]);
```

::: tip
你需要从 npm 安装 `globals` 包才能使此示例工作。ESLint 不会自动安装它。
:::

#### `eslint-env` 配置注释（`eslint-env` Configuration Comments）

在 eslintrc 配置系统中，可以使用 `eslint-env` 配置注释为文件定义全局变量。
在使用 flat config 进行检查时，这些注释不再被识别，并且会被报告为错误。
因此，从 eslintrc 迁移到 flat config 时，应从所有文件中移除 `eslint-env` 配置注释。
它们可以被等价但更冗长的 `global` 配置注释替换，或者干脆删除，改为在配置文件中使用 `globals` 定义。

例如，使用 eslintrc 时，被检查的文件可能如下所示：

```javascript
// tests/my-file.js

/* eslint-env mocha */

describe("unit tests", () => {
	it("should pass", () => {
		// ...
	});
});
```

在上面的示例中，由于 `/* eslint-env mocha */` 注释，`describe` 和 `it` 会被识别为全局标识符。

使用 flat config 时，可以通过 `global` 配置注释达到相同的效果，例如：

```javascript
// tests/my-file.js

/* global describe, it -- Globals defined by Mocha */

describe("unit tests", () => {
	it("should pass", () => {
		// ...
	});
});
```

另一种选择是从被检查的文件中移除注释，并在配置中定义全局变量，例如：

```javascript
// eslint.config.js

import { defineConfig } from "eslint/config";
import globals from "globals";

export default defineConfig([
	// ...其他配置
	{
		files: ["tests/**"],
		languageOptions: {
			globals: {
				...globals.mocha,
			},
		},
	},
]);
```

#### 预定义和可共享配置（Predefined and Shareable Configs）

在 eslintrc 文件中，使用 `extends` 属性来使用预定义和可共享配置。ESLint 自带两个可以作为字符串访问的预定义配置：

- `"eslint:recommended"`：ESLint 推荐的规则。
- `"eslint:all"`：ESLint 附带的所有规则。

你也可以使用 `extends` 属性来继承可共享配置。可共享配置可以是本地配置文件的路径或 npm 包名。

在 flat config 文件中，预定义配置从单独的模块导入到 flat config 文件中。`recommended` 和 `all` 规则配置位于 [`@eslint/js`](https://www.npmjs.com/package/@eslint/js) 包中。你必须导入此包才能使用这些配置：

```shell
npm install --save-dev @eslint/js
```

你可以将每个配置添加到导出的数组中，或公开其中的特定规则。使用 flat config 时，你必须导入本地配置文件和 npm 包配置的模块。

例如，下面是一个使用内置 `eslint:recommended` 配置的 eslintrc 文件：

```javascript
// .eslintrc.js

module.exports = {
	// ...其他配置
	extends: "eslint:recommended",
	rules: {
		semi: ["warn", "always"],
	},
	// ...其他配置
};
```

这个 eslintrc 文件使用了内置配置、本地自定义配置和来自 npm 包的可共享配置：

```javascript
// .eslintrc.js

module.exports = {
	// ...其他配置
	extends: [
		"eslint:recommended",
		"./custom-config.js",
		"eslint-config-my-config",
	],
	rules: {
		semi: ["warn", "always"],
	},
	// ...其他配置
};
```

要在 flat config 中使用相同的配置，你会这样做：

```javascript
// eslint.config.js

import { defineConfig } from "eslint/config";
import js from "@eslint/js";
import customConfig from "./custom-config.js";
import myConfig from "eslint-config-my-config";

export default defineConfig([
	js.configs.recommended,
	customConfig,
	myConfig,
	{
		rules: {
			semi: ["warn", "always"],
		},
		// ...其他配置
	},
]);
```

请注意，因为你只是导入 JavaScript 模块，所以可以在 ESLint 使用配置对象之前对其进行修改。例如，你可能希望某个配置对象只应用于你的测试文件：

```javascript
// eslint.config.js

import { defineConfig } from "eslint/config";
import js from "@eslint/js";
import customTestConfig from "./custom-test-config.js";

export default defineConfig([
	js.configs.recommended,
	{
		...customTestConfig,
		files: ["**/*.test.js"],
	},
]);
```

##### 在 Flat Config 中使用 eslintrc 配置（Use eslintrc Configs in Flat Config）

你可能会发现，你依赖的某个可共享配置尚未更新为 flat config 格式。在这种情况下，你可以使用 `FlatCompat` 工具将 eslintrc 格式转换为 flat config 格式。首先，安装 `@eslint/eslintrc` 包：

```shell
npm install --save-dev @eslint/eslintrc
```

然后，导入 `FlatCompat` 并创建一个新实例来转换现有的 eslintrc 配置。例如，如果 npm 包 `eslint-config-my-config` 是 eslintrc 格式，你可以这样写：

```js
import { defineConfig } from "eslint/config";
import { FlatCompat } from "@eslint/eslintrc";
import path from "path";
import { fileURLToPath } from "url";

// 模拟 CommonJS 变量——如果使用 CommonJS 则不需要
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const compat = new FlatCompat({
	baseDirectory: __dirname,
});

export default defineConfig([
	// 模拟 ESLintRC 风格的 extends
	...compat.extends("eslint-config-my-config"),
]);
```

此示例使用 `FlatCompat#extends()` 方法将 `eslint-config-my-config` 插入 flat config 数组。

有关 `FlatCompat` 类的更多信息，请参阅[包的 README](https://github.com/eslint/eslintrc#usage)。

#### 忽略文件（Ignore Files）

使用 eslintrc 时，你可以通过在项目根目录创建一个单独的 `.eslintignore` 文件来让 ESLint 忽略文件。`.eslintignore` 文件使用与 `.gitignore` 文件相同的 glob 模式语法。或者，你可以在 eslintrc 文件中使用 `ignorePatterns` 属性。

要在 flat config 中忽略文件，可以在不包含其他属性的配置对象中使用 `ignores` 属性。`ignores` 属性接受一个 glob 模式数组。flat config 不支持从 `.eslintignore` 文件加载忽略模式，因此你需要将这些模式直接迁移到 flat config 中。

例如，下面是一个可以与 eslintrc 配置一起使用的 `.eslintignore` 示例：

```shell
# .eslintignore
temp.js
config/*
# ...其他被忽略的文件
```

以下是在 `.eslintrc.js` 文件中表示为 `ignorePatterns` 的相同模式：

```javascript
// .eslintrc.js
module.exports = {
	// ...其他配置
	ignorePatterns: ["temp.js", "config/*"],
};
```

flat config 中等价的忽略模式如下所示：

```javascript
import { defineConfig } from "eslint/config";

export default defineConfig([
	// ...其他配置
	{
		// 注意：此对象中不应有其他属性
		ignores: ["**/temp.js", "config/*"],
	},
]);
```

在 `.eslintignore` 中，`temp.js` 忽略所有名为 `temp.js` 的文件，而在 flat config 中，你需要将其指定为 `**/temp.js`。flat config 中的模式 `temp.js` 只忽略与配置文件位于同一目录中的名为 `temp.js` 的文件。

::: important
在 flat config 中，点文件（例如 `.dotfile.js`）默认不再被忽略。如果你想忽略点文件，请添加忽略模式 `"**/.*"`。
:::

#### Linter 选项（Linter Options）

eslintrc 文件允许你使用 `noInlineConfig` 和 `reportUnusedDisableDirectives` 属性来配置 linter 本身。

flat config 系统引入了一个新的顶级属性 `linterOptions`，你可以用它来配置 linter。在 `linterOptions` 对象中，你可以包含 `noInlineConfig` 和 `reportUnusedDisableDirectives`。

例如，下面是一个启用了 linter 选项的 eslintrc 文件：

```javascript
// .eslintrc.js

module.exports = {
	// ...其他配置
	noInlineConfig: true,
	reportUnusedDisableDirectives: true,
};
```

这是 flat config 中的相同选项：

```javascript
// eslint.config.js

import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		// ...其他配置
		linterOptions: {
			noInlineConfig: true,
			reportUnusedDisableDirectives: "warn",
		},
	},
]);
```

#### CLI 标志变更（CLI Flag Changes）

以下 CLI 标志在 flat config 文件格式中不再受支持：

- `--env`
- `--ignore-path`
- `--no-eslintrc`
- `--resolve-plugins-relative-to`
- `--rulesdir`

##### `--env`

`--env` 标志用于启用特定环境的全局变量（例如 `browser` 或 `node`）。flat config 不支持此标志。取而代之的是，直接在你的配置中定义相关全局变量。更多细节请参见[指定全局变量](https://eslint.org/docs/latest/use/configure/language-options#specifying-globals)。

例如，如果你之前使用 `--env browser,node`，你需要像这样更新你的配置文件：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";
import globals from "globals";

export default defineConfig([
	{
		languageOptions: {
			globals: {
				...globals.browser,
				...globals.node,
			},
		},
	},
]);
```

##### `--ignore-path`

`--ignore-path` 标志用于指定将哪个文件用作你的 `.eslintignore`。flat config 不从 `.eslintignore` 文件加载忽略模式，也不支持此标志。如果你想包含来自 `.gitignore` 文件的模式，使用 `@eslint/compat` 中的 `includeIgnoreFile()`。更多细节请参见[包含 `.gitignore` 文件](https://eslint.org/docs/latest/use/configure/ignore#including-gitignore-files)。

例如，如果你之前使用 `--ignore-path .gitignore`：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";
import { includeIgnoreFile } from "@eslint/compat";
import { fileURLToPath } from "node:url";

const gitignorePath = fileURLToPath(new URL(".gitignore", import.meta.url));

export default defineConfig([
	includeIgnoreFile(gitignorePath, "Imported .gitignore patterns"),
	// 其他配置
]);
```

*更新（2026-05-01）：`@eslint/compat` 中的 `includeIgnoreFile()` 已被弃用，推荐使用 `@eslint/config-helpers` 中的 `includeIgnoreFile()`（在 `eslint/config` 中重新导出）。[新 API](https://eslint.org/docs/latest/use/configure/ignore#including-gitignore-files) 更加灵活，并且可以选择相对于忽略文件来解释忽略模式。*

##### `--no-eslintrc`

`--no-eslintrc` 标志已被替换为 `--no-config-lookup`。

##### `--resolve-plugins-relative-to`

`--resolve-plugins-relative-to` 标志用于指示配置文件中引用的插件应相对于哪个目录解析。这是必要的，因为可共享配置只能解析作为对等依赖或父包依赖的插件。

使用 flat config 时，可共享配置可以直接指定其依赖项，因此不再需要此标志。

##### `--rulesdir`

`--rulesdir` 标志用于从指定目录加载额外的规则。使用 flat config 时不再支持此功能。你可以改为直接在配置中创建一个包含本地规则的插件，如下所示：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";
import myRule from "./rules/my-rule.js";

export default defineConfig([
	{
		// 定义插件
		plugins: {
			local: {
				rules: {
					"my-rule": myRule,
				},
			},
		},

		// 配置规则
		rules: {
			"local/my-rule": ["error"],
		},
	},
]);
```

#### 不再支持 `package.json` 配置（`package.json` Configuration No Longer Supported）

使用 eslintrc 时，可以使用 `package.json` 文件中的 `eslintConfig` 键来配置 ESLint。

使用 flat config 时，不能再使用 `package.json` 文件来配置 ESLint。你需要将配置移到一个单独的文件中。

#### 其他变更（Additional Changes）

以下是从 eslintrc 到 flat config 文件格式的变更：

- `root` 选项不复存在。（flat config 文件的行为如同设置了 `root: true`。）
- `files` 选项不能再是单个字符串，它必须是一个数组。
- `sourceType` 选项现在支持新值 `"commonjs"`（`.eslintrc` 也支持它，但从未记录在文档中）。

### Flat Config 文件的 TypeScript 类型（TypeScript Types for Flat Config Files）

你可以在 [GitHub 上的 `lib/types` 源码目录](https://github.com/eslint/eslint/tree/main/lib/types)中查看 flat config 文件格式的 TypeScript 类型。配置数组中对象的接口称为 `Linter.Config`。

你可以在 [`lib/types/index.d.ts`](https://github.com/eslint/eslint/blob/main/lib/types/index.d.ts) 中查看类型定义。

### Visual Studio Code 支持（Visual Studio Code Support）

ESLint v9.x 支持已在 [`vscode-eslint`](https://marketplace.visualstudio.com/items?itemName=dbaeumer.vscode-eslint) v3.0.10 中添加。

在 v3.0.10 之前的 `vscode-eslint` 版本中，新配置系统默认未启用。要启用对新配置文件的支持，请编辑你的 `.vscode/settings.json` 文件并添加以下内容：

```json
{
	// 仅在 vscode-eslint < v3.0.10 中需要
	"eslint.experimental.useFlatConfig": true
}
```

在 ESLint 插件的未来版本中，你将不再需要手动启用此功能。

### 延伸阅读（Further Reading）

- [flat config 文件格式概述博客文章](https://eslint.org/blog/2022/08/new-config-system-part-2/)
- [新配置系统的 API 用法博客文章](https://eslint.org/blog/2022/08/new-config-system-part-3/)
- [新配置系统背景博客文章](https://eslint.org/blog/2022/08/new-config-system-part-1/)


---

