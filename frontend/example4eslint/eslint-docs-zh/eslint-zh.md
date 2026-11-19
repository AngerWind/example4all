# ESLint 中文文档：入门（Getting Started）、核心概念（Core Concepts）、配置（Configure ESLint）与扩展（Extend ESLint）

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



### 配置文件（Configuration File）

ESLint 配置文件可以命名为以下任意一种：

- `eslint.config.js`
- `eslint.config.mjs`
- `eslint.config.cjs`
- `eslint.config.ts`（需要[额外设置](#typescript-配置文件)）
- `eslint.config.mts`（需要[额外设置](#typescript-配置文件)）
- `eslint.config.cts`（需要[额外设置](#typescript-配置文件)）

优先级从高到低



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

- `files` - 一个 glob 模式数组，指示配置对象应应用于哪些文件。

    如果未指定，配置对象应用于被任何其他配置对象匹配的所有文件。

- `ignores` - 一个 glob 模式数组，指示配置对象不应应用于哪些文件。

    如果未指定，配置对象应用于 `files` 匹配的所有文件。如果 `ignores` 在配置对象中没有与其他任何键一起使用，那么这些模式将作为[全局忽略](#使用-ignores-全局忽略文件)，应用于每个配置对象。

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

- `plugins` - 一个对象，包含插件名称到插件对象的名称-值映射。

    当指定了 `files` 时，这些插件仅对匹配的文件可用。

- `rules` - 一个对象，包含已配置的规则。

    当指定了 `files` 或 `ignores` 时，这些规则配置仅对匹配的文件可用。

- `settings` - 一个对象，包含应对所有规则可用的信息名称-值对。

#### 指定 `files` 和 `ignores`（Specify `files` and `ignores`）

`files`和`ignroes`可以通过 [`minimatch`](https://www.npmjs.com/package/minimatch) 语法来定义要匹配或者忽略的文件,  默认他们的路径是相对于配置文件的, 如果配置对象指定了basePath, 那么他们的路径就是相对于basePath

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

	// files和ignores结合使用, 匹配所有以 .js 结尾的文件，但 __tests 中的除外
	{
		files: ["**/*.js"],
		ignores: ["__tests/**"],
		rules: {
			"no-console": "error",
		},
	},
]);
```



如果你只有一个不带`files`的配置对象, 那么默认情况下, 他会作用于所有的 `**/*.js`、`**/*.cjs` 和 `**/*.mjs` 

~~~js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	// 匹配所有以 .js, cjs, mjs 结尾的文件
	{
		rules: {
			semi: "error",
		},
	}
]);
~~~



不带 `files` 或 `ignores` 的配置对象会自动应用于被**任何其他配置对象匹配的任何文件**。例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
      // 因为没有写files, 那么他匹配的文件就是其他配置对象匹配的文件, 也就是tests/**/*.js + **/*.js、**/*.cjs、**/*.mjs
      {
          rules: {
              semi: "error",
              "no-unused-vars": "warn",
          },
      },

      // 只对测试文件放宽
      {
          files: ["tests/**/*.js"],
          rules: {
              "no-unused-vars": "off",   // 覆盖基础配置
          },
      },
  ]);
```



这里有两个比较隐性的坑

1. 因为eslint默认匹配`**/*.js、**/*.cjs、**/*.mjs`文件, 所以如下的配置

   ~~~js
   export default defineConfig([
         { rules: { semi: "error" } },   // 不带 files
     ]);
   ~~~

   你在执行`npx eslint example.js` 是可以成功的

   但是在执行`npx eslint example.txt`的时候会不生效,  因为`example.txt`并没有配匹配

2. 因为不带 `files` 或 `ignores` 的配置对象会自动应用于被**任何其他配置对象匹配的任何文件**,  那么如果你有一个配置对象他匹配的不是js/cjs/mjs文件, 而是其他的文件, 比如md文件

   ~~~js
   export default defineConfig([
         // 不带files, 匹配所有的md文件 和 默认的配置中的js/cjs/mjs文件
       // 所以这会导致semi和no-unsed-vars规则跑到md文件上, 导致一堆报错
         {
             rules: {
                 semi: "error",
                 "no-unused-vars": "warn",
             },
         },
   
         // 匹配所有的md文件
         {
             files: ["**/*.md"],
             plugins: { markdown },
             processor: "markdown/markdown",
         },
     ]);
   ~~~

   



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

> 以点开头的文件名（如 `.gitignore`）被视为只有扩展名而没有基本名称。以 `.gitignore` 为例，其扩展名是 `gitignore`，因此该文件匹配模式 `"**/.gitignore"`，但不匹配 `"**/*.gitignore"`。



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

> 使用 [config inspector](https://github.com/eslint/config-inspector)（CLI 中的 `--inspect-config`）来测试哪些配置对象适用于特定文件。



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







## 配置语言选项（Configure Language Options）

> 原文链接：https://eslint.org/docs/latest/use/configure/language-options



JavaScript 生态系统包含各种各样的运行时、版本、扩展和框架。它们各自可能支持不同的语法和全局变量。ESLint 允许你配置项目中使用到的 JavaScript 配置语言选项，例如自定义全局变量。你也可以使用插件来扩展 ESLint，使其支持项目的语言选项。

### 指定 JavaScript 选项（Specify JavaScript Options）

ESLint 允许你指定想要支持的 JavaScript 语言选项。默认情况下，ESLint 支持使用最新的 stage 4 ECMAScript 语法(stage4表示已经完成, 并纳入标准的特性)和 ECMAScript 模块（ESM）模式。你可以通过 `languageOptions` 键覆盖这些设置

- `ecmaVersion`（默认值：`"latest"`）- 指示被检查代码的 ECMAScript 版本，同时决定可用的语法和全局变量。分别设置为 `3` 或 `5` 表示 ECMAScript 3 和 5。另外，你可以使用 `2015` 至今的任何年份。

    在大多数情况下，我们建议使用默认值 `"latest"`，以确保始终使用最新的 ECMAScript 版本。

    > 如果设置为2017, 标准支持es2017以及之前的所有语法

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
            // 指定js的语法版本, 2015对应es6
            // 2017对应es2017, 2024对应es2024, latest表示最新版本
			ecmaVersion: 5,
			sourceType: "script", // 指定js的模块标准
		},
	},
]);
```





### 指定解析器选项（Specify Parser Options）

如果你使用的是 ESLint 内置解析器，还可以通过指定 `languageOptions.parserOptions` 键来改变 ESLint 解释代码的方式。所有选项默认为 `false`：

- `allowReserved` - 允许使用保留字作为标识符（仅在 `ecmaVersion` 为 `3` 时有用）。
- `ecmaFeatures` - 一个对象，指示你想使用哪些额外的语言特性：
    - `globalReturn` - 允许在全局作用域中使用 `return` 语句。此选项仅在 `languageOptions.sourceType` 设置为 `"script"` 时生效。当 `languageOptions.sourceType` 设置为 `"commonjs"` 时，无论此选项如何，顶层 `return` 语句都是被允许的。当 `languageOptions.sourceType` 设置为 `"module"` 时，即使此选项设置为 `true`，顶层 `return` 语句也是不允许的。
    - `impliedStrict` - 启用全局[严格模式](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Strict_mode)（仅在 `ecmaVersion` 为 `5` 或更高时有用）。
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

> 虽然可以在 `languageOptions.parserOptions` 中传递 `ecmaVersion` 和 `sourceType`，但对于默认解析器我们不建议这样做。相反，请直接在 `languageOptions` 中配置这些选项。如果两者都被指定，对于解析器而言，`languageOptions.parserOptions` 中的选项优先于 `languageOptions` 中的选项。

> 请注意，支持 JSX 语法并不等同于支持 React。React 对 JSX 语法应用了 ESLint 无法识别的特定语义。如果你在使用 React，我们建议使用 [`eslint-plugin-react`](https://github.com/jsx-eslint/eslint-plugin-react) 或 [`@eslint-react/eslint-plugin`](https://github.com/rel1cx/eslint-react)。





### 指定全局变量（Specify Globals）

ESLint 的一些核心规则依赖于对你的代码在运行时可用全局变量的了解。\

由于这些全局变量在不同环境之间差异很大，并且可能在运行时被修改，ESLint 不会对你的执行环境中存在哪些全局变量做任何假设。如果你想使用需要了解可用全局变量的规则，可以在配置文件中定义全局变量，或者在源代码中使用配置注释。

#### 使用配置注释（Use configuration comments）

要在 JavaScript 文件内使用注释指定全局变量，请使用以下格式：

```js
/* global var1, var2 */
// 这样你就可以直接使用var1和var2这两个全局变量了, 如果不加上面的注释, 那么eslint会报错, var1是undefined
console.log(var1)
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

# 扩展 ESLint（Extend ESLint）

> 原文链接：https://eslint.org/docs/latest/extend/

本指南面向希望扩展 ESLint 功能的读者。

为了扩展 ESLint，建议你具备以下条件：

- 了解 JavaScript，因为 ESLint 是用 JavaScript 编写的。
- 对 Node.js 有一定了解，因为 ESLint 运行在 Node.js 之上。
- 能熟练使用命令行程序。

如果你符合以上条件，请继续阅读以开始。

## [扩展 ESLint 的方式（Ways to Extend ESLint）](https://eslint.org/docs/latest/extend/ways-to-extend)

本页总结了扩展 ESLint 的各种方式，以及这些扩展如何协同组合在一起。

## [创建插件（Create Plugins）](https://eslint.org/docs/latest/extend/plugins)

你已经为 ESLint 开发了自定义规则，并希望与社区分享。你可以在 npm 上发布一个 ESLint 插件。

## [自定义规则教程（Custom Rule Tutorial）](https://eslint.org/docs/latest/extend/custom-rule-tutorial)

一个带你一步步创建 ESLint 自定义规则的教程。

## [自定义规则（Custom Rules）](https://eslint.org/docs/latest/extend/custom-rules)

本节介绍如何创建可与 ESLint 一起使用的自定义规则。

## [自定义格式化器（Custom Formatters）](https://eslint.org/docs/latest/extend/custom-formatters)

本节介绍如何创建自定义格式化器来控制 ESLint 的输出。

## [自定义解析器（Custom Parsers）](https://eslint.org/docs/latest/extend/custom-parsers)

如果你不想使用 ESLint 的默认解析器，本节介绍如何创建自定义解析器。

## [自定义处理器（Custom Processors）](https://eslint.org/docs/latest/extend/custom-processors)

本节介绍如何使用自定义处理器让 ESLint 处理 JavaScript 以外的文件。

## [共享配置（Share Configurations）](https://eslint.org/docs/latest/extend/shareable-configs)

本节介绍如何将 ESLint 配置打包到 JavaScript 包中并共享。


---

## 扩展 ESLint 的方式（Ways to Extend ESLint）

> 原文链接：https://eslint.org/docs/latest/extend/ways-to-extend

ESLint 是高度可插拔和可配置的。你可以通过多种方式扩展 ESLint 的功能。

本页介绍扩展 ESLint 的各种方式，以及这些扩展如何协同组合在一起。

### 插件（Plugins）

插件让你可以向项目添加自己的 ESLint 自定义规则和自定义处理器。你可以将插件发布为 npm 模块。

插件很有用，因为你的项目可能需要一些核心 `eslint` 包中没有包含的 ESLint 配置。例如，如果你在使用像 [React](https://react.dev/) 这样的前端 JavaScript 库，或像 [Vue](https://vuejs.org/) 这样的框架，这些工具的一些特性需要 ESLint 核心规则范围之外的自定义规则。

插件通常会搭配一个 ESLint 配置，将插件的一组功能应用到项目中。你也可以在插件中包含配置。

例如，[`eslint-plugin-react`](https://www.npmjs.com/package/eslint-plugin-react) 是一个 ESLint 插件，包含专门针对 React 项目的规则。这些规则包括强制统一使用 React 组件生命周期方法、要求渲染动态列表时使用 key prop 等。

要了解更多关于创建可包含在插件中的扩展的信息，请参阅以下文档：

- [自定义规则（Custom Rules）](https://eslint.org/docs/latest/extend/custom-rules)
- [自定义处理器（Custom Processors）](https://eslint.org/docs/latest/extend/custom-processors)
- [插件中的配置（Configs in Plugins）](https://eslint.org/docs/latest/extend/plugins#configs-in-plugins)

要了解更多关于将这些扩展打包成插件的信息，请参阅[创建插件（Plugins）](https://eslint.org/docs/latest/extend/plugins)。

### 可共享配置（Shareable Configs）

ESLint 可共享配置是预定义的 ESLint 配置，你可以在自己的项目中使用。它们把规则和其他配置打包在一个 npm 包中。凡是可以放在配置文件中的内容，都可以放进可共享配置中。

你可以独立发布可共享配置，也可以将其作为插件的一部分发布。

例如，一个流行的可共享配置是 [`eslint-config-airbnb`](https://www.npmjs.com/package/eslint-config-airbnb)，除了一些[解析器选项](https://eslint.org/docs/latest/use/configure/language-options#specifying-parser-options)之外，它还包含各种规则。这是一套为匹配 [Airbnb JavaScript 风格指南](https://github.com/airbnb/javascript)而设计的 ESLint 规则集。通过使用 `eslint-config-airbnb` 可共享配置，你可以在项目中自动强制执行 Airbnb 风格指南，而无需手动配置每条规则。

要了解更多关于创建可共享配置的信息，请参阅[共享配置（Share Configurations）](https://eslint.org/docs/latest/extend/shareable-configs)。

### 自定义格式化器（Custom Formatters）

自定义格式化器接收 ESLint 的 lint 结果，并以你定义的格式输出结果。自定义格式化器让你以最适合自己需求的格式展示 lint 结果，无论是特定的文件格式、某种展示风格，还是为特定工具优化的格式。只有当[内置格式化器](https://eslint.org/docs/latest/use/formatters/)不能满足你的使用场景时，你才需要创建自定义格式化器。

例如，自定义格式化器 [eslint-formatter-gitlab](https://www.npmjs.com/package/eslint-formatter-gitlab) 可用于在 [GitLab](https://about.gitlab.com/) 代码质量报告中展示 ESLint 结果。

要了解更多关于创建自定义格式化器的信息，请参阅[自定义格式化器（Custom Formatters）](https://eslint.org/docs/latest/extend/custom-formatters)。

### 自定义解析器（Custom Parsers）

ESLint 自定义解析器是一种扩展 ESLint 的方式，使其支持对代码中的新语言特性或自定义语法进行 lint。解析器负责接收你的代码，并将其转换为 ESLint 可以分析和检查的抽象语法树（AST）。

ESLint 自带一个内置的 JavaScript 解析器（Espree），但自定义解析器允许你对其他语言进行 lint，或扩展内置解析器的 lint 能力。

例如，自定义解析器 [@typescript-eslint/parser](https://typescript-eslint.io/packages/parser) 扩展了 ESLint，使其能够检查 TypeScript 代码。

自定义解析器也可以包含在插件中。

要了解更多关于创建自定义解析器的信息，请参阅[自定义解析器（Custom Parsers）](https://eslint.org/docs/latest/extend/custom-parsers)。


---

## 创建插件（Create Plugins）

> 原文链接：https://eslint.org/docs/latest/extend/plugins

ESLint 插件为 ESLint 扩展额外的功能。在大多数情况下，你会通过创建插件来扩展 ESLint，将你想在多个项目之间共享的额外功能封装起来。

### 创建插件（Create a plugin）

插件是一个向 ESLint 暴露特定属性的 JavaScript 对象：

- `meta` - 关于插件的信息。
- `configs` - 包含命名配置的对象。
- `rules` - 包含自定义规则定义的对象。
- `processors` - 包含命名处理器的对象。

首先，创建一个 JavaScript 文件并导出一个包含你希望 ESLint 使用的属性的对象。为了让你的插件尽可能易于维护，我们建议你将插件入口文件格式化为如下结构：

```js
const plugin = {
	meta: {},
	configs: {},
	rules: {},
	processors: {},
};

// ESM 方式
export default plugin;

// 或者 CommonJS 方式
module.exports = plugin;
```

如果你计划将插件作为 npm 包分发，请确保导出插件对象的模块是包的默认导出。这样，当在命令行的 [`--plugin` 选项](https://eslint.org/docs/latest/use/command-line-interface#--plugin)中指定该插件时，ESLint 才能导入它。

#### 插件中的元数据（Meta Data in Plugins）

为了更方便地调试和更有效地缓存插件，建议在插件根级的 `meta` 对象中提供 `name`、`version` 和 `namespace`，如下所示：

```js
const plugin = {
	// name 和 version 的首选位置
	meta: {
		name: "eslint-plugin-example",
		version: "1.2.3",
		namespace: "example",
	},
	rules: {
		// 在这里添加规则
	},
};

// ESM 方式
export default plugin;

// 或者 CommonJS 方式
module.exports = plugin;
```

`meta.name` 属性应与你的插件的 npm 包名一致，`meta.version` 属性应与你的插件的 npm 包版本一致。`meta.namespace` 属性应与你希望用户用来访问插件的规则、处理器、语言和配置的前缀一致。命名空间通常是你包名中 `eslint-plugin-` 之后的部分，这就是本例使用 `"example"` 的原因。提供命名空间可以让 `defineConfig()` 函数即使用户在配置文件中指定了不同的命名空间，也能找到你的插件。

添加名称和版本最简单的方法是从你的 `package.json` 中读取这些信息，如本例所示：

```js
import fs from "fs";

const pkg = JSON.parse(
	fs.readFileSync(new URL("./package.json", import.meta.url), "utf8"),
);

const plugin = {
	// name 和 version 的首选位置
	meta: {
		name: pkg.name,
		version: pkg.version,
		namespace: "example",
	},
	rules: {
		// 在这里添加规则
	},
};

export default plugin;
```

::: tip
虽然插件名称没有限制，但遵循以下命名约定有助于其他人在 [npm](https://npmjs.com) 上找到你的插件：

- **无作用域（Unscoped）：** 如果你的 npm 包名不带作用域（不以 `@` 开头），那么插件名应以 `eslint-plugin-` 开头，例如 `eslint-plugin-example`。
- **带作用域（Scoped）：** 如果你的 npm 包名带作用域，那么插件名应为 `@<scope>/eslint-plugin-<plugin-name>` 格式，例如 `@jquery/eslint-plugin-jquery`，或者甚至是 `@<scope>/eslint-plugin`，例如 `@jquery/eslint-plugin`。

:::

作为替代方式，你也可以在插件根级暴露 `name` 和 `version` 属性，例如：

```js
const plugin = {
	// name 和 version 的替代位置
	name: "eslint-plugin-example",
	version: "1.2.3",
	rules: {
		// 在这里添加规则
	},
};

// ESM 方式
export default plugin;

// 或者 CommonJS 方式
module.exports = plugin;
```

::: important
虽然 `meta` 对象是提供插件名称和版本的首选方式，但这种格式也是可以接受的，保留它是为了向后兼容。
:::

#### 插件中的规则（Rules in Plugins）

插件可以暴露自定义规则供 ESLint 使用。为此，插件必须导出一个 `rules` 对象，包含规则 ID 到规则的键值映射。规则 ID 不需要遵循任何命名约定，只是不应包含 `/` 字符（例如，可以是 `dollar-sign`，但不能是 `foo/dollar-sign`）。要了解更多关于在插件中创建自定义规则的信息，请参阅[自定义规则（Custom Rules）](https://eslint.org/docs/latest/extend/custom-rules)。

```js
const plugin = {
	meta: {
		name: "eslint-plugin-example",
		version: "1.2.3",
	},
	rules: {
		"dollar-sign": {
			create(context) {
				// 规则实现……
			},
		},
	},
};

// ESM 方式
export default plugin;

// 或者 CommonJS 方式
module.exports = plugin;
```

要在配置文件中使用插件中的规则，导入该插件并将其包含在 `plugins` 键中并指定命名空间。然后，使用该命名空间在 `rules` 配置中引用规则，如下所示：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";
import example from "eslint-plugin-example";

export default defineConfig([
	{
		files: ["**/*.js"], // 你想应用此配置的任何模式
		plugins: {
			example,
		},
		rules: {
			"example/dollar-sign": "error",
		},
	},
]);
```

::: warning
不以 `@` 开头的命名空间不能包含 `/`；以 `@` 开头的命名空间可以包含 `/`。例如，`eslint/plugin` 不是有效的命名空间，但 `@eslint/plugin` 是有效的。此限制是为了向后兼容 eslintrc 的插件命名限制。
:::

#### 插件中的处理器（Processors in Plugins）

插件可以通过提供 `processors` 对象来暴露[处理器](https://eslint.org/docs/latest/extend/custom-processors)供配置文件使用。与规则类似，`processors` 对象中的每个键是处理器名称，每个值是处理器对象本身。下面是一个示例：

```js
const plugin = {
	meta: {
		name: "eslint-plugin-example",
		version: "1.2.3",
	},
	processors: {
		"processor-name": {
			preprocess(text, filename) {
				/* ... */
			},
			postprocess(messages, filename) {
				/* ... */
			},
		},
	},
};

// ESM 方式
export default plugin;

// 或者 CommonJS 方式
module.exports = plugin;
```

要在配置文件中使用插件中的处理器，导入该插件并将其包含在 `plugins` 键中并指定命名空间。然后，使用该命名空间在 `processor` 配置中引用处理器，如下所示：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";
import example from "eslint-plugin-example";

export default defineConfig([
	{
		files: ["**/*.txt"],
		plugins: {
			example,
		},
		processor: "example/processor-name",
	},
]);
```

#### 插件中的配置（Configs in Plugins）

你可以通过在 `configs` 键下指定配置，将配置捆绑在插件内。当你想把一组自定义规则与一个启用推荐选项的配置捆绑在一起时，这很有用。每个插件支持多个配置。

你可以在同样包含在插件中的配置里引用该插件的单个规则。在配置中，你必须在 `plugins` 对象中指定你的插件名称，以及你想启用的属于该插件的任何规则。任何插件规则都必须以插件命名空间为前缀。下面是一个示例：

```js
const plugin = {
	meta: {
		name: "eslint-plugin-example",
		version: "1.2.3",
	},
	configs: {},
	rules: {
		"dollar-sign": {
			create(context) {
				// 规则实现……
			},
		},
	},
};

// 在这里赋值 configs，这样我们可以引用 `plugin`
Object.assign(plugin.configs, {
	recommended: [
		{
			plugins: {
				example: plugin,
			},
			rules: {
				"example/dollar-sign": "error",
			},
			languageOptions: {
				globals: {
					myGlobal: "readonly",
				},
				parserOptions: {
					ecmaFeatures: {
						jsx: true,
					},
				},
			},
		},
	],
});

// ESM 方式
export default plugin;

// 或者 CommonJS 方式
module.exports = plugin;
```

这个插件导出了一个 `recommended` 配置，它是包含一个配置对象的数组。当只有一个配置对象时，你也可以直接导出该对象，而不需要外层数组。

要在配置文件中使用插件中的配置，导入该插件并使用 `extends` 键引用配置的名称，如下所示：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";
import example from "eslint-plugin-example";

export default defineConfig([
	{
		files: ["**/*.js"], // 你想应用此配置的任何模式
		plugins: {
			example,
		},
		extends: ["example/recommended"],
	},
]);
```

::: important
插件不能强制使用特定配置。用户必须在自己的配置文件中手动包含插件的配置。
:::

##### 旧版配置的向后兼容（Backwards Compatibility for Legacy Configs）

如果你的插件需要导出同时适用于当前（flat config）系统和旧（eslintrc）系统的配置，你可以从 `configs` 键导出两种配置类型。导出旧版配置时，我们建议名称以 `"legacy-"` 为前缀（例如 `"legacy-recommended"`），以明确该配置应如何使用。

如果你正在维护一个早在 ESLint v9.0.0 之前就存在的插件，那么你可能已经有名为 `"recommended"` 之类的旧版配置。如果你不想更新配置名称，也可以在 `configs` 对象中创建一个以 `"flat/"` 为前缀的额外条目（例如 `"flat/recommended"`）。下面是一个示例：

```js
const plugin = {
	meta: {
		name: "eslint-plugin-example",
		version: "1.2.3",
	},
	configs: {},
	rules: {
		"dollar-sign": {
			create(context) {
				// 规则实现……
			},
		},
	},
};

// 在这里赋值 configs，这样我们可以引用 `plugin`
Object.assign(plugin.configs, {
	// flat config 格式
	"flat/recommended": [
		{
			plugins: {
				example: plugin,
			},
			rules: {
				"example/dollar-sign": "error",
			},
			languageOptions: {
				globals: {
					myGlobal: "readonly",
				},
				parserOptions: {
					ecmaFeatures: {
						jsx: true,
					},
				},
			},
		},
	],

	// eslintrc 格式
	recommended: {
		plugins: ["example"],
		rules: {
			"example/dollar-sign": "error",
		},
		globals: {
			myGlobal: "readonly",
		},
		parserOptions: {
			ecmaFeatures: {
				jsx: true,
			},
		},
	},
});

// ESM 方式
export default plugin;

// 或者 CommonJS 方式
module.exports = plugin;
```

使用这种方法，两种配置系统都能识别 `"recommended"`。旧配置系统使用 `recommended` 键，而当前配置系统使用 `flat/recommended` 键。`defineConfig()` 辅助函数首先查看 `recommended` 键，如果它格式不正确，则查找 `flat/recommended` 键。这为你提供了一条升级路径：当你不再需要支持旧配置系统时，可以之后将 `flat/recommended` 重命名为 `recommended`。

### 测试插件（Testing a Plugin）

ESLint 提供了 [`RuleTester`](https://eslint.org/docs/latest/integrate/nodejs-api#ruletester) 工具，让你可以轻松测试插件的规则。

### 对插件进行 Lint（Linting a Plugin）

ESLint 插件也应该被 lint！建议使用以下包的 `recommended` 配置来 lint 你的插件：

- [eslint](https://www.npmjs.com/package/eslint)
- [eslint-plugin-eslint-plugin](https://www.npmjs.com/package/eslint-plugin-eslint-plugin)
- [eslint-plugin-n](https://www.npmjs.com/package/eslint-plugin-n)

### 分享插件（Share Plugins）

为了让你的插件可以公开使用，你必须将其发布到 npm 上。发布时，请务必：

1. **将 ESLint 列为 peer dependency。** 因为插件旨在与 ESLint 一起使用，所以将 `eslint` 包添加为 peer dependency 很重要。为此，手动编辑你的 `package.json` 文件，包含一个 `peerDependencies` 块，如下所示：

    ```json
    {
    	"peerDependencies": {
    		"eslint": ">=10.0.0"
    	}
    }
    ```

2. **指定 keywords。** ESLint 插件应在你的 `package.json` 文件中指定 `eslint`、`eslintplugin` 和 `eslint-plugin` 作为 [keywords](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#keywords)。


---

### 自定义规则教程（Custom Rule Tutorial）

> 原文链接：https://eslint.org/docs/latest/extend/custom-rule-tutorial

本教程介绍如何为 ESLint 创建自定义规则，并通过插件进行分发。

你可以创建自定义规则来验证代码是否满足某个预期，并确定当不满足预期时该做什么。插件将自定义规则和其他配置打包，让你可以轻松地在不同项目中共享和复用它们。

要了解更多关于自定义规则和插件的信息，请参阅以下文档：

- [自定义规则（Custom Rules）](https://eslint.org/docs/latest/extend/custom-rules)
- [创建插件（Plugins）](https://eslint.org/docs/latest/extend/plugins)

#### 为什么要创建自定义规则？（Why Create a Custom Rule?）

当 ESLint [内置规则](https://eslint.org/docs/latest/rules/)和社区发布的自定义规则无法满足你的需求时，就创建自定义规则。你可能创建自定义规则来强制执行公司或项目的最佳实践、防止特定 bug 重现，或确保遵守风格指南。

在创建并非特定于你的公司或项目的自定义规则之前，值得先在网上搜索一下，看看是否有人发布了带有能解决你用例的自定义规则的插件。很可能该规则已经存在。

#### 前提条件（Prerequisites）

在开始之前，请确保你的开发环境中已安装以下内容：

- [Node.js](https://nodejs.org/en/download/)
- [npm](https://www.npmjs.com/)

本教程还假定你对 ESLint 和 ESLint 规则有基本的了解。

#### 自定义规则（The Custom Rule）

本教程中的自定义规则要求所有名为 `foo` 的 `const` 变量必须被赋值为字符串字面量 `"bar"`。规则定义在 `enforce-foo-bar.js` 文件中。该规则还会建议将任何赋给 `const foo` 的其他值替换为 `"bar"`。

例如，假设你有以下 `foo.js` 文件：

```javascript
// foo.js

const foo = "baz123";
```

使用该规则运行 ESLint 会将 `"baz123"` 标记为变量 `foo` 的错误值。如果 ESLint 以自动修复模式运行，ESLint 会将文件修复为包含以下内容：

```javascript
// foo.js

const foo = "bar";
```

#### 第 1 步：搭建项目（Step 1: Set up Your Project）

首先，为你的自定义规则创建一个新项目。创建一个新目录，在其中初始化一个新的 npm 项目，并为自定义规则创建一个新文件：

```shell
mkdir eslint-custom-rule-example # 创建目录
cd eslint-custom-rule-example # 进入目录
npm init -y # 初始化新的 npm 项目
touch enforce-foo-bar.js # 创建文件 enforce-foo-bar.js
```

#### 第 2 步：搭出规则文件的骨架（Step 2: Stub Out the Rule File）

在 `enforce-foo-bar.js` 文件中，为 `enforce-foo-bar` 自定义规则添加一些脚手架。同时，添加一个包含规则基本信息的 `meta` 对象。

```javascript
// enforce-foo-bar.js

module.exports = {
	meta: {
		// TODO: 添加元数据
	},
	create(context) {
		return {
			// TODO: 添加回调函数
		};
	},
};
```

#### 第 3 步：添加规则元数据（Step 3: Add Rule Metadata）

在编写规则之前，先向规则对象添加一些元数据。ESLint 在运行规则时会使用这些信息。

首先导出一个带有 `meta` 属性的对象，其中包含规则的元数据，例如规则类型、文档和可修复性。在这个例子中，规则类型是 "problem"，描述是 "Enforce that a variable named `foo` can only be assigned a value of 'bar'."，并且规则可以通过修改代码进行修复。

因为这条规则针对 JavaScript，我们还添加了 `languages: ["js/js"]`，声明它只适用于内置的 JavaScript 语言。如果这条规则对非 JavaScript 语言启用，ESLint 会抛出错误。

```javascript
// enforce-foo-bar.js

module.exports = {
	meta: {
		type: "problem",
		docs: {
			description:
				"Enforce that a variable named `foo` can only be assigned a value of 'bar'.",
		},
		fixable: "code",
		schema: [],
		languages: ["js/js"],
	},
	create(context) {
		return {
			// TODO: 添加回调函数
		};
	},
};
```

要了解更多关于规则元数据的信息，请参阅[规则结构（Rule Structure）](https://eslint.org/docs/latest/extend/custom-rules#rule-structure)。

#### 第 4 步：添加规则访问器方法（Step 4: Add Rule Visitor Methods）

定义规则的 `create` 函数，它接受一个 `context` 对象并返回一个对象，该对象为你想处理的每种语法节点类型各有一个属性。在这个例子中，你想处理 `VariableDeclarator` 节点。
你可以选择任何 [ESTree 节点类型](https://github.com/estree/estree)或[选择器](https://eslint.org/docs/latest/extend/selectors)。

::: tip
你可以使用 [Code Explorer](https://explorer.eslint.org) 查看任何 JavaScript 代码的 AST。这有助于确定你想要针对的节点类型。
:::

在 `VariableDeclarator` 访问器方法内部，检查节点是否代表一个 `const` 变量声明、其名称是否为 `foo`，以及它是否未被赋值为字符串 `"bar"`。你通过求值传递给 `VariableDeclaration` 方法的 `node` 来完成这些检查。

如果 `const foo` 声明被赋值为 `"bar"`，那么规则什么都不做。如果 `const foo` **没有**被赋值为 `"bar"`，那么 `context.report()` 会向 ESLint 报告一个错误。错误报告包含关于错误以及如何修复它的信息。

```javascript
// enforce-foo-bar.js
module.exports = {
    meta: {
        type: "problem",
        docs: {
            description: "Enforce that a variable named `foo` can only be assigned a value of 'bar'."
        },
        fixable: "code",
        schema: [],
        languages: ["js/js"]
    },
    create(context) {
        return {

            // 对每个变量声明符执行函数中的操作
            VariableDeclarator(node) {

                // 检查是否为 `const` 变量声明
                if (node.parent.kind === "const") {

                    // 检查变量名是否为 `foo`
                    if (node.id.type === "Identifier" && node.id.name === "foo") {

                        // 检查变量的值是否为 "bar"
                        if (node.init && node.init.type === "Literal" && node.init.value !== "bar") {

                            /*
                             * 向 ESLint 报告错误。错误消息使用
                             * 消息占位符将错误的值包含在错误消息中。
                             * 还包含一个 `fix(fixer)` 函数，将任何赋给
                             * `const foo` 的值替换为 "bar"。
                             */
                            context.report({
                                node,
                                message: 'Value other than "bar" assigned to `const foo`. Unexpected value: {{ notBar }}.',
                                data: {
                                    notBar: node.init.value
                                },
                                fix(fixer) {
                                    return fixer.replaceText(node.init, '"bar"');
                                }
                            });
                        }
                    }
                }
            }
        };
    }
};
```

#### 第 5 步：搭建测试（Step 5: Set up Testing）

规则编写完成后，你可以测试它，确保它按预期工作。

ESLint 提供了内置的 [`RuleTester`](https://eslint.org/docs/latest/integrate/nodejs-api#ruletester) 类来测试规则。你不需要使用第三方测试库来测试 ESLint 规则，但 `RuleTester` 可以与 Mocha 和 Jest 等工具无缝配合。

接下来，创建测试文件 `enforce-foo-bar.test.js`：

```shell
touch enforce-foo-bar.test.js
```

你将在测试文件中使用 `eslint` 包。将其安装为开发依赖：

```shell
npm install --save-dev eslint
```

并在你的 `package.json` 文件中添加一个测试脚本来运行测试：

```javascript
// package.json
{
    // ...其他配置
    "scripts": {
        "test": "node enforce-foo-bar.test.js"
    },
    // ...其他配置
}
```

#### 第 6 步：编写测试（Step 6: Write the Test）

要使用 `RuleTester` 编写测试，将该类和你的自定义规则导入 `enforce-foo-bar.test.js` 文件。

`RuleTester#run()` 方法针对有效和无效测试用例测试规则。如果规则未能通过任何测试场景，此方法会抛出错误。
`RuleTester` 要求至少存在一个有效和一个无效的测试场景。

```javascript
// enforce-foo-bar.test.js
const { RuleTester } = require("eslint");
const fooBarRule = require("./enforce-foo-bar");

const ruleTester = new RuleTester({
	// 必须至少使用 ecmaVersion 2015，
	// 因为 `const` 变量是在那时引入的。
	languageOptions: { ecmaVersion: 2015 },
});

// 如果 ruleTester.run() 中的测试未通过，则抛出错误
ruleTester.run(
	"enforce-foo-bar", // 规则名称
	fooBarRule, // 规则代码
	{
		// 检查
		// 'valid' 检查应该通过的用例
		valid: [
			{
				code: "const foo = 'bar';",
			},
		],
		// 'invalid' 检查不应通过的用例
		invalid: [
			{
				code: "const foo = 'baz';",
				output: 'const foo = "bar";',
				errors: 1,
			},
		],
	},
);

console.log("All tests passed!");
```

使用以下命令运行测试：

```shell
npm test
```

如果测试通过，你应该在控制台中看到以下内容：

```shell
All tests passed!
```

#### 第 7 步：将自定义规则打包进插件（Step 7: Bundle the Custom Rule in a Plugin）

现在你已经编写了自定义规则并验证了它可以工作，你可以将它包含在插件中。使用插件，你可以在 npm 包中分享该规则，以便在其他项目中使用。

创建插件文件：

```shell
touch eslint-plugin-example.js
```

现在编写插件代码。插件只是导出的 JavaScript 对象。要在插件中包含规则，将其包含在插件的 `rules` 对象中，该对象包含规则名称及其源代码的键值对。

要了解更多关于创建插件的信息，请参阅[创建插件（Create Plugins）](https://eslint.org/docs/latest/extend/plugins)。

```javascript
// eslint-plugin-example.js

const fooBarRule = require("./enforce-foo-bar");
const plugin = { rules: { "enforce-foo-bar": fooBarRule } };
module.exports = plugin;
```

#### 第 8 步：在本地使用插件（Step 8: Use the Plugin Locally）

你可以使用本地定义的插件在项目中执行自定义规则。要使用本地插件，在 ESLint 配置文件的 `plugins` 属性中指定插件的路径。

你可能想在以下场景之一中使用本地定义的插件：

- 你想在发布到 npm 之前测试插件。
- 你想使用一个插件，但不想将其发布到 npm。

在将插件添加到项目之前，先使用 [flat 配置文件](https://eslint.org/docs/latest/use/configure/configuration-files) `eslint.config.js` 为你的项目创建 ESLint 配置：

```shell
touch eslint.config.js
```

然后，将以下代码添加到 `eslint.config.js`：

```javascript
// eslint.config.js
"use strict";

// 导入 `defineConfig` 辅助函数
const { defineConfig } = require("eslint/config");
// 在本地导入 ESLint 插件
const eslintPluginExample = require("./eslint-plugin-example");

module.exports = defineConfig([
	{
		files: ["**/*.js"],
		languageOptions: {
			sourceType: "commonjs",
			ecmaVersion: "latest",
		},
		// 使用本地定义的 eslint-plugin-example 插件
		plugins: { example: eslintPluginExample },
		rules: {
			"example/enforce-foo-bar": "error",
		},
	},
]);
```

在测试规则之前，你必须创建一个用于测试规则的文件。

创建文件 `example.js`：

```shell
touch example.js
```

将以下代码添加到 `example.js`：

```javascript
// example.js

function correctFooBar() {
	const foo = "bar";
}

function incorrectFoo() {
	const foo = "baz"; // 问题！
}
```

现在你可以用本地定义的插件测试自定义规则了。

对 `example.js` 运行 ESLint：

```shell
npx eslint example.js
```

这会在终端中产生以下输出：

```text
/<path-to-directory>/eslint-custom-rule-example/example.js
  8:11  error  Value other than "bar" assigned to `const foo`. Unexpected value: baz  example/enforce-foo-bar

✖ 1 problem (1 error, 0 warnings)
  1 error and 0 warnings potentially fixable with the `--fix` option.
```

#### 第 9 步：发布插件（Step 9: Publish the Plugin）

要将包含规则的插件发布到 npm，你需要配置 `package.json`。在相应字段中添加以下内容：

1. `"name"`：包的唯一名称。npm 上不能有其他包使用相同的名称。
1. `"main"`：插件文件的相对路径。按照此示例，路径为 `"eslint-plugin-example.js"`。
1. `"description"`：在 npm 上可查看的包的描述。
1. `"peerDependencies"`：添加 `"eslint": ">=10.0.0"` 作为 peer dependency。使用该插件需要任何大于或等于该版本的版本。将 `eslint` 声明为 peer dependency 要求用户在插件之外单独将该包添加到项目中。
1. `"keywords"`：包含标准关键字 `["eslint", "eslintplugin", "eslint-plugin"]`，使包易于查找。你也可以添加任何其他可能与你的插件相关的关键字。

一个完整的、带注释的插件 `package.json` 文件示例：

```javascript
// package.json
{
  // npm 包名称。
  // 添加你自己的包名。eslint-plugin-example 已被占用！
  "name": "eslint-plugin-example",
  "version": "1.0.0",
  "description": "ESLint plugin for enforce-foo-bar rule.",
  "main": "eslint-plugin-example.js", // 插件入口点
  "scripts": {
    "test": "node enforce-foo-bar.test.js"
  },
  // 添加 eslint>=10.0.0 作为 peer dependency。
  "peerDependencies": {
    "eslint": ">=10.0.0"
  },
  // 添加这些标准关键字，让插件易于查找！
  "keywords": [
    "eslint",
    "eslintplugin",
    "eslint-plugin"
  ],
  "author": "",
  "license": "ISC",
  "devDependencies": {
    "eslint": "^10.0.0"
  }
}
```

要发布该包，运行 `npm publish` 并按照 CLI 提示操作。

你应该能看到该包已在 npm 上线！

#### 第 10 步：使用已发布的自定义规则（Step 10: Use the Published Custom Rule）

接下来，你可以使用已发布的插件。

在你的项目中运行以下命令下载该包：

```shell
# 在此处添加你的包名
npm install --save-dev eslint-plugin-example
```

更新 `eslint.config.js` 以使用打包版本的插件：

```javascript
// eslint.config.js
"use strict";

// 导入从 npm 下载的插件
const eslintPluginExample = require("eslint-plugin-example");

// ...其余配置
```

现在你可以测试自定义规则了。

使用下载的插件，对你在第 8 步中创建的 `example.js` 文件运行 ESLint：

```shell
npx eslint example.js
```

这会在终端中产生以下输出：

```text
/<path-to-directory>/eslint-custom-rule-example/example.js
  8:11  error  Value other than "bar" assigned to `const foo`. Unexpected value: baz  example/enforce-foo-bar

✖ 1 problem (1 error, 0 warnings)
  1 error and 0 warnings potentially fixable with the `--fix` option.
```

如上面的消息所示，你实际上可以使用 `--fix` 标志修复该问题，将变量赋值更正为 `"bar"`。

带 `--fix` 标志再次运行 ESLint：

```shell
npx eslint example.js --fix
```

运行时终端中没有错误输出，但你可以在 `example.js` 中看到修复已被应用。你应该看到以下内容：

```javascript
// example.js

// ...文件其余部分

function incorrectFoo() {
	const foo = "bar"; // 已修复！
}
```

#### 总结（Summary）

在本教程中，你创建了一个自定义规则，要求所有名为 `foo` 的 `const` 变量必须被赋值为字符串 `"bar"`，并建议将任何赋给 `const foo` 的其他值替换为 `"bar"`。你还将该规则添加到插件中，并在 npm 上发布了该插件。

通过这样做，你学到了以下实践，可以将它们应用于创建其他自定义规则和插件：

1. 创建自定义 ESLint 规则
1. 测试自定义规则
1. 将规则打包进插件
1. 发布插件
1. 使用插件中的规则

#### 查看教程代码（View the Tutorial Code）

你可以查看[本教程的带注释源代码](https://github.com/eslint/eslint/tree/main/docs/_examples/custom-rule-tutorial-code)。


---

### 自定义规则（Custom Rules）

> 原文链接：https://eslint.org/docs/latest/extend/custom-rules

你可以创建自定义规则与 ESLint 一起使用。如果[核心规则](https://eslint.org/docs/latest/rules/)没有覆盖你的用例，你可能需要创建自定义规则。

下面是自定义规则的基本格式：

```js
// customRule.js

module.exports = {
	meta: {
		type: "suggestion",
		docs: {
			description: "Description of the rule",
		},
		fixable: "code",
		schema: [], // 没有选项
	},
	create: function (context) {
		return {
			// 回调函数
		};
	},
};
```

::: warning
`eslint` 包中附带的核心规则不被视为公共 API 的一部分，也不是为被继承而设计的。在这些规则之上构建是脆弱的，很可能在未来的某个时刻导致你的规则完全失效。如果你有兴趣创建与核心规则类似的规则，你应该先将该规则文件复制到你的项目中，然后再在此基础上继续。
:::

#### 规则结构（Rule Structure）

规则的源文件导出一个具有以下属性的对象。自定义规则和核心规则都遵循此格式。

`meta`：（`object`）包含规则的元数据：

- `type`：（`string`）指示规则的类型，为 `"problem"`、`"suggestion"` 或 `"layout"` 之一：
    - `"problem"`：该规则识别的是会导致错误或可能导致混乱行为的代码。开发者应将其视为高优先级来解决。
    - `"suggestion"`：该规则识别的是可以用更好方式完成的事情，但即使代码不更改也不会发生错误。
    - `"layout"`：该规则主要关注空白、分号、逗号和括号——程序中所有决定代码外观而非执行方式的部分。这些规则处理的是 AST 中未指定的代码部分。

    规则类型可以与 CLI 选项 [`--fix-type`](https://eslint.org/docs/latest/use/command-line-interface#--fix-type) 一起使用，以指定要应用的修复类型。

- `docs`：（`object`）常用于文档生成和工具的属性。核心规则必需，自定义规则可选。自定义规则可以根据需要在此包含额外的属性。
    - `description`：（`string`）提供规则的简短描述。对于核心规则，它用于[规则索引](https://eslint.org/docs/latest/rules/)。
    - `dialects`：（`string[]`）该规则旨在检查的语言方言。例如 `["JavaScript", "TypeScript"]`。
    - `frozen`：（`boolean`）对于核心规则，指定该规则是否已[冻结](https://eslint.org/docs/latest/contribute/core-rules#frozen-rules)且不再接受功能请求。
    - `recommended`：（`unknown`）对于核心规则，这是一个布尔值，指定该规则是否被 `@eslint/js` 的 `recommended` 配置启用。
    - `url`：（`string`）指定可以访问完整文档的 URL。代码编辑器常用它在高亮的规则违规上提供有用的链接。

- `messages`：（`object`）包含该规则的违规消息和建议消息的对象。核心规则必需，自定义规则可选。消息可以在 `context.report()` 调用中通过它们的键（`messageId`）引用。更多信息请参见 [`messageId`](#messageids)。
- `fixable`：（`string`）如果[命令行](https://eslint.org/docs/latest/use/command-line-interface#--fix)上的 `--fix` 选项可以自动修复该规则报告的问题，则为 `"code"` 或 `"whitespace"`。

    **重要：** 对于可修复的规则，`fixable` 属性是必需的。如果未指定此属性，每当规则尝试产生修复时，ESLint 都会抛出错误。如果规则不可修复，请省略 `fixable` 属性。

- `hasSuggestions`：（`boolean`）指定规则是否可以返回建议（如果省略则默认为 `false`）。

    **重要：** 对于提供建议的规则，`hasSuggestions` 属性是必需的。如果此属性未设置为 `true`，每当规则尝试产生建议时，ESLint 都会抛出错误。如果规则不提供建议，请省略 `hasSuggestions` 属性。

- `schema`：（`object | array | false`）指定[选项](#选项模式options-schemas)，以便 ESLint 可以阻止无效的[规则配置](https://eslint.org/docs/latest/use/configure/rules)。当规则有选项时是必需的。

- `defaultOptions`：（`array`）指定规则的[默认选项](#选项默认值option-defaults)。如果存在，用户配置中提供的任何选项都将递归地合并到它们之上。

- `languages`：（`array`）指定该规则设计用于处理的语言。每个条目是 `"pluginName/languageName"` 格式的字符串（例如 `"js/js"`、`"markdown/gfm"`）。特殊值：
    - `"*"` —— 该规则适用于任何语言。
    - `"pluginName/*"` —— 该规则适用于给定插件提供的任何语言。

    如果未指定 `languages`，则假定该规则适用于所有语言。当指定了 `languages` 且没有任何条目匹配当前语言时，ESLint 会抛出错误。

    ```js
    // 规则仅在当前语言为内置 JavaScript 语言时运行
    meta: {
        languages: ["js/js"],
    }

    // 规则可运行于 "markdown" 插件的任何语言
    meta: {
        languages: ["markdown/*"],
    }

    // 规则可运行于任何语言
    meta: {
        languages: ["*"],
    }
    ```

    语言标识符按以下顺序匹配：
    1. **通配符 `"*"`** —— 匹配任何语言。
    2. **直接字符串匹配** —— 条目与当前语言字符串完全相等（例如 `"test/lang"`）。
    3. **`"plugin/*"` 通配符** —— 匹配插件名（或 `meta.namespace`）等于条目插件部分的任何语言。
    4. **命名空间匹配** —— 当以不同名称注册的插件的 `meta.namespace` 等于条目的插件部分时，也可以匹配。

    ```js
    // 规则运行于 meta.namespace 为 "markdown" 的插件的特定语言
    //（例如，插件注册为 "md"，但声明了 meta.namespace = "markdown"）
    meta: {
        languages: ["markdown/gfm"],
    }
    ```

- `deprecated`：（`boolean | DeprecatedInfo`）指示该规则是否已废弃。如果规则未被废弃，可以省略 `deprecated` 属性。
  有一个专门的页面介绍 [DeprecatedInfo](https://eslint.org/docs/latest/extend/rule-deprecation)。

- `replacedBy`：（`array`，**已废弃**，请改用 `meta.deprecated.replacedBy`。）对于已废弃的规则，指定替代规则。

`create()`：返回一个对象，其方法由 ESLint 在遍历 JavaScript 代码的抽象语法树（由 [ESTree](https://github.com/estree/estree) 定义的 AST）时调用以"访问"节点：

- 如果键是节点类型或[选择器](https://eslint.org/docs/latest/extend/selectors)，ESLint 会在**向下**遍历树时调用该**访问器（visitor）**函数。
- 如果键是节点类型或[选择器](https://eslint.org/docs/latest/extend/selectors)加上 `:exit`，ESLint 会在**向上**遍历树时调用该**访问器**函数。
- 如果键是事件名称，ESLint 会为[代码路径分析](https://eslint.org/docs/latest/extend/code-path-analysis)调用该**处理器（handler）**函数。

规则可以使用当前节点及其周围的树来报告或修复问题。

下面是 [array-callback-return](https://eslint.org/docs/latest/rules/array-callback-return) 规则的方法：

```js
function checkLastSegment (node) {
    // 如果最后的代码路径段可达，则为函数报告问题
}

module.exports = {
    meta: { ... },
    create: function(context) {
        // 声明规则的状态
        return {
            ReturnStatement: function(node) {
                // 向下遍历时位于 ReturnStatement 节点
            },
            // 向上遍历时位于函数表达式节点：
            "FunctionExpression:exit": checkLastSegment,
            "ArrowFunctionExpression:exit": checkLastSegment,
            onCodePathStart: function (codePath, node) {
                // 开始分析代码路径时
            },
            onCodePathEnd: function(codePath, node) {
                // 结束分析代码路径时
            }
        };
    }
};
```

::: tip
你可以使用 [Code Explorer](https://explorer.eslint.org) 查看任何 JavaScript 代码的完整 AST。
:::

#### Context 对象（The Context Object）

`context` 对象是规则中 `create` 方法的唯一参数。例如：

```js
// customRule.js

module.exports = {
    meta: { ... },
    // `context` 对象就是参数
    create(context) {
       // ...
    }
};
```

顾名思义，`context` 对象包含与规则上下文相关的信息。

`context` 对象具有以下属性：

- `id`：（`string`）规则 ID。
- `filename`：（`string`）与源代码关联的文件名。
- `physicalFilename`：（`string`）检查文件时，它提供文件在磁盘上的完整路径，不含任何代码块信息。检查文本时，它提供传给 `--stdin-filename` 的值，未指定时为 `<text>`。
- `cwd`：（`string`）传给 [Linter](https://eslint.org/docs/latest/integrate/nodejs-api#linter) 的 `cwd` 选项。它是应被视为当前工作目录的目录路径。
- `options`：（`array`）此规则[已配置选项](https://eslint.org/docs/latest/use/configure/rules)的数组。此数组不包含规则严重级别（见[专门一节](#访问传递给规则的选项accessing-options-passed-to-a-rule)）。
- `sourceCode`：（`object`）一个 `SourceCode` 对象，可用于处理传给 ESLint 的源代码（见[访问源代码](#访问源代码accessing-the-source-code)）。
- `settings`：（`object`）来自配置的[共享设置](https://eslint.org/docs/latest/use/configure/configuration-files#configuring-shared-settings)。
- `languageOptions`：（`object`）[每个属性的更多细节](https://eslint.org/docs/latest/use/configure/language-options)
    - `sourceType`：（`'script' | 'module' | 'commonjs'`）当前文件的模式。
    - `ecmaVersion`：（`number`）用于解析当前文件的 ECMA 版本。
    - `parser`：（`object`）用于解析当前文件的解析器。
    - `parserOptions`：（`object`）为此文件配置的解析器选项。
    - `globals`：（`object`）指定的全局变量。

此外，`context` 对象还有以下方法：

- `report(descriptor)`。报告代码中的问题（见[专门一节](#报告问题report-problems)）。

**注意：** ESLint 的早期版本在 `context` 对象上支持额外的方法。这些方法已在新格式中移除，不应再依赖。

##### 报告问题（Report Problems）

编写自定义规则时使用的主要方法是 `context.report()`，它发布一个警告或错误（取决于使用的配置）。此方法接受单个参数，即一个包含以下属性的对象：

- `messageId`：（`string`）消息的 ID（见 [messageIds](#messageids)）（推荐，优于 `message`）。
- `message`：（`string`）问题消息（`messageId` 的替代方案）。
- `node`：（可选 `object`）可以是与问题相关的 AST 节点、token 或注释。如果存在且未指定 `loc`，则使用节点的起始位置作为问题的位置。
- `loc`：（可选 `object`）指定问题的位置。如果同时指定了 `loc` 和 `node`，则使用 `loc` 中的位置而不是 `node`。
    - `start`：起始位置的对象。
        - `line`：（`number`）问题发生的 1 起始行号。
        - `column`：（`number`）问题发生的 0 起始列号。
    - `end`：结束位置的对象。
        - `line`：（`number`）问题发生的 1 起始行号。
        - `column`：（`number`）问题发生的 0 起始列号。
- `data`：（可选 `object`）`message` 的[占位符](#使用消息占位符use-message-placeholders)数据。
- `fix(fixer)`：（可选 `function`）应用[修复](#应用修复applying-fixes)来解决问题。

请注意，`node` 或 `loc` 至少需要提供一个。

最简单的示例是只使用 `node` 和 `message`：

```js
context.report({
	node: node,
	message: "Unexpected identifier",
});
```

节点包含了确定违规文本的行号和列号以及表示该节点的源文本所需的全部信息。

###### 使用消息占位符（Use Message Placeholders）

你也可以在消息中使用占位符并提供 `data`：

```js
context.report({
    node: node,
    message: "Unexpected identifier: {{ identifier }}",
    data: {
        identifier: node.name
    }
});
```

请注意，消息参数中的首尾空白是可选的。

节点包含了确定违规文本的行号和列号以及表示该节点的源文本所需的全部信息。

###### `messageId`（`messageId`s）

`messageId` 是在 `context.report()` 调用中报告消息的推荐方式，因为它有以下好处：

- 规则违规消息可以集中存储在 `meta.messages` 对象中，便于管理。
- 规则违规消息不需要在规则文件和规则测试文件中重复。
- 因此，修改规则违规消息的门槛更低，鼓励更频繁地贡献以改进和优化它们，使其最清晰、最有用。

规则文件：

```js
// avoid-name.js

module.exports = {
    meta: {
        messages: {
            avoidName: "Avoid using variables named '{{ name }}'"
        }
    },
    create(context) {
        return {
            Identifier(node) {
                if (node.name === "foo") {
                    context.report({
                        node,
                        messageId: "avoidName",
                        data: {
                            name: "foo",
                        }
                    });
                }
            }
        };
    }
};
```

被检查的文件：

```javascript
// someFile.js

var foo = 2;
//  ^ 错误：Avoid using variables named 'foo'
```

在你的测试中：

```javascript
// avoid-name.test.js

var rule = require("../../../lib/rules/avoid-name");
var RuleTester = require("eslint").RuleTester;

var ruleTester = new RuleTester();
ruleTester.run("avoid-name", rule, {
	valid: ["bar", "baz"],
	invalid: [
		{
			code: "foo",
			errors: [
				{
					messageId: "avoidName",
				},
			],
		},
	],
});
```

###### 应用修复（Applying Fixes）

如果你希望 ESLint 尝试修复你报告的问题，可以在使用 `context.report()` 时指定 `fix` 函数。`fix` 函数接收单个参数——一个 `fixer` 对象，你可以用它应用修复。例如：

```js
context.report({
	node: node,
	message: "Missing semicolon",
	fix(fixer) {
		return fixer.insertTextAfter(node, ";");
	},
});
```

这里，`fix()` 函数用于在节点后插入一个分号。请注意，修复不会立即应用，并且如果与其他修复冲突，可能根本不会应用。应用修复后，ESLint 会在修复后的代码上再次运行所有已启用的规则，可能应用更多修复。此过程最多重复 10 次，或直到不再发现可修复的问题。之后，任何剩余的问题将照常报告。

**重要：** 对于可修复的规则，`meta.fixable` 属性是必需的。如果实现了 `fix` 函数的规则没有[导出](#规则结构rule-structure) `meta.fixable` 属性，ESLint 会抛出错误。

`fixer` 对象有以下方法：

- `insertTextAfter(nodeOrToken, text)`：在给定节点或 token 之后插入文本。
- `insertTextAfterRange(range, text)`：在给定范围之后插入文本。
- `insertTextBefore(nodeOrToken, text)`：在给定节点或 token 之前插入文本。
- `insertTextBeforeRange(range, text)`：在给定范围之前插入文本。
- `remove(nodeOrToken)`：移除给定节点或 token。
- `removeRange(range)`：移除给定范围内的文本。
- `replaceText(nodeOrToken, text)`：替换给定节点或 token 中的文本。
- `replaceTextRange(range, text)`：替换给定范围内的文本。

`range` 是一个包含源代码中字符索引的两项数组。第一项是范围的开始（含），第二项是范围的结束（不含）。每个节点和 token 都有一个 `range` 属性，用于标识它们所表示的源代码范围。

上述方法返回一个 `fixing` 对象。
`fix()` 函数可以返回以下值：

- 一个 `fixing` 对象。
- 一个包含 `fixing` 对象的数组。
- 一个枚举 `fixing` 对象的可迭代对象。特别地，`fix()` 函数可以是生成器。

如果你编写的 `fix()` 函数返回多个 `fixing` 对象，这些 `fixing` 对象不得重叠。

修复的最佳实践：

1. 避免任何可能改变代码运行时行为并导致其停止工作的修复。
1. 让修复尽可能小。不必要的大修复可能与其他修复冲突，并阻止它们被应用。
1. 每条消息只做一个修复。这是强制执行的，因为你必须从 `fix()` 返回 fixer 操作的结果。
1. 由于在应用第一轮修复后会再次运行所有规则，因此规则没有必要检查修复的代码风格是否会导致另一条规则报告错误。
    - 例如，假设一个 fixer 想给对象键加上引号，但不确定用户更喜欢单引号还是双引号。

        ```js
        { foo: 1 }

        // 应该被修复为
        { 'foo': 1 }

        // 或
        { "foo": 1 }
        ```

    - 这个 fixer 可以随意选择一种引号类型。如果猜错了，生成的代码将被 [`quotes`](https://eslint.org/docs/latest/rules/quotes) 规则自动报告并修复。

注意：让修复尽可能小是最佳实践，但在某些情况下，扩展修复范围可能是正确的，以便有意阻止其他规则在同一轮中对周围范围进行修复。例如，如果替换文本声明了一个新变量，那么阻止该变量作用域内的其他更改可能很有用，因为它们可能导致名称冲突。

下面的示例替换 `node`，同时确保在同一轮中不会在 `node.parent` 范围内应用其他修复：

```js
context.report({
	node,
	message,
	*fix(fixer) {
		yield fixer.replaceText(node, replacementText);

		// 将修复范围扩展到 `node.parent` 的范围
		yield fixer.insertTextBefore(node.parent, "");
		yield fixer.insertTextAfter(node.parent, "");
	},
});
```

###### 冲突的修复（Conflicting Fixes）

冲突的修复是指对源代码同一部分应用不同更改的修复。
无法指定应用哪个冲突的修复。

例如，如果两个修复都想修改第 0 到 5 个字符，则只应用其中一个。

###### 提供建议（Providing Suggestions）

在某些情况下，修复不适合自动应用——例如，修复可能改变功能，或者根据实现意图有多种有效的修复方式（见上文[应用修复](#应用修复applying-fixes)的最佳实践）。在这些情况下，`context.report()` 上有一个替代的 `suggest` 选项，允许其他工具（如编辑器）为用户提供手动应用建议的辅助功能。

要提供建议，在 report 参数中使用 `suggest` 键，其值为建议对象的数组。建议对象代表可以应用的单个建议，需要一个描述应用建议会做什么的 `desc` 键字符串或 `messageId` 键（见[下文](#建议-messageidssuggestion-messageids)），以及一个定义建议结果的 `fix` 键函数。此 `fix` 函数遵循与常规修复相同的 API（在上文[应用修复](#应用修复applying-fixes)中介绍）。

```js
context.report({
    node: node,
    message: "Unnecessary escape character: \\{{character}}.",
    data: { character },
    suggest: [
        {
            desc: "Remove the `\\`. This maintains the current functionality.",
            fix: function(fixer) {
                return fixer.removeRange(range);
            }
        },
        {
            desc: "Replace the `\\` with `\\\\` to include the actual backslash character.",
            fix: function(fixer) {
                return fixer.insertTextBeforeRange(range, "\\");
            }
        }
    ]
});
```

**重要：** 对于提供建议的规则，`meta.hasSuggestions` 属性是必需的。如果规则尝试产生建议但没有[导出](#规则结构rule-structure)此属性，ESLint 会抛出错误。

**注意：** 建议作为独立的更改应用，不会触发多轮修复。每个建议应专注于代码中的单一更改，不应试图符合用户定义的风格。例如，如果建议在代码库中添加新语句，它不应试图匹配正确的缩进或符合用户对分号有无的偏好。所有这些都可以由用户触发时的多轮自动修复来纠正。

建议的最佳实践：

1. 不要试图做太多事情，建议可能引入大量破坏性变更的大型重构。
1. 如上所述，不要试图符合用户定义的风格。

建议旨在提供修复。如果建议的 `fix` 函数返回 `null` 或空数组/序列，ESLint 会自动从 lint 输出中移除整个建议。

###### 建议 `messageId`（Suggestion `messageId`s）

建议可以使用 `messageId` 代替 `desc` 键。这与整体错误的 `messageId` 工作方式相同（见 [messageIds](#messageids)。下面是如何在规则中使用建议 `messageId` 的示例：

```js
module.exports = {
    meta: {
        messages: {
            unnecessaryEscape: "Unnecessary escape character: \\{{character}}.",
            removeEscape: "Remove the `\\`. This maintains the current functionality.",
            escapeBackslash: "Replace the `\\` with `\\\\` to include the actual backslash character."
        },
        hasSuggestions: true
    },
    create: function(context) {
        // ...
        context.report({
            node: node,
            messageId: 'unnecessaryEscape',
            data: { character },
            suggest: [
                {
                    messageId: "removeEscape", // 建议 messageId
                    fix: function(fixer) {
                        return fixer.removeRange(range);
                    }
                },
                {
                    messageId: "escapeBackslash", // 建议 messageId
                    fix: function(fixer) {
                        return fixer.insertTextBeforeRange(range, "\\");
                    }
                }
            ]
        });
    }
};
```

###### 建议消息中的占位符（Placeholders in Suggestion Messages）

你也可以在建议消息中使用占位符。这与整体错误的占位符工作方式相同（见[使用消息占位符](#使用消息占位符use-message-placeholders)）。

请注意，你必须在建议的对象上提供 `data`。建议消息不能使用整体错误的 `data` 中的属性。

```js
module.exports = {
    meta: {
        messages: {
            unnecessaryEscape: "Unnecessary escape character: \\{{character}}.",
            removeEscape: "Remove `\\` before {{character}}.",
        },
        hasSuggestions: true
    },
    create: function(context) {
        // ...
        context.report({
            node: node,
            messageId: "unnecessaryEscape",
            data: { character }, // unnecessaryEscape 整体消息的 data
            suggest: [
                {
                    messageId: "removeEscape",
                    data: { character }, // removeEscape 建议消息的 data
                    fix: function(fixer) {
                        return fixer.removeRange(range);
                    }
                }
            ]
        });
    }
};
```

##### 访问传递给规则的选项（Accessing Options Passed to a Rule）

有些规则需要选项才能正常工作。这些选项出现在配置中（`.eslintrc`、命令行接口或注释）。例如：

```json
{
	"quotes": ["error", "double"]
}
```

本例中的 `quotes` 规则有一个选项 `"double"`（`error` 是错误级别）。你可以使用 `context.options` 检索规则的选项，它是一个包含规则每个已配置选项的数组。在本例中，`context.options[0]` 将包含 `"double"`：

```js
module.exports = {
	meta: {
		schema: [
			{
				enum: ["single", "double", "backtick"],
			},
		],
	},
	create: function (context) {
		var isDouble = context.options[0] === "double";

		// ...
	},
};
```

由于 `context.options` 只是一个数组，你可以用它来确定传入了多少选项，以及检索实际的选项本身。请记住，错误级别不是 `context.options` 的一部分，因为错误级别无法从规则内部获知或修改。

使用选项时，请确保你的规则在未提供选项的情况下有一些合理的默认值。

带选项的规则必须指定 [schema](#选项模式options-schemas)。

###### 避免不可序列化的选项（Avoid Non-Serializable Options）

规则选项和[共享设置](https://eslint.org/docs/latest/use/configure/configuration-files#configuring-shared-settings)应只包含可以序列化为 JSON 的值，例如字符串、数字、布尔值、`null`、数组和普通对象。避免设计期望在 `context.options` 或 `context.settings` 中包含不可序列化值（如正则表达式、函数或类实例）的规则。

某些 ESLint 功能与不可序列化的值配合不佳。例如，当启用[缓存](https://eslint.org/docs/latest/use/command-line-interface#caching)时，如果更新后的值产生相同的序列化配置，ESLint 可能无法检测到变化，因此可能继续报告过时的结果。如果你的规则需要正则表达式，请改为以字符串选项接受该模式，并在规则内部创建 `RegExp` 对象。

##### 访问源代码（Accessing the Source Code）

`SourceCode` 对象是获取有关被检查源代码更多信息的主要对象。你可以随时使用 `context.sourceCode` 属性检索 `SourceCode` 对象：

```js
module.exports = {
	create: function (context) {
		var sourceCode = context.sourceCode;

		// ...
	},
};
```

获得 `SourceCode` 实例后，你可以使用以下方法处理代码：

- `getText(node, beforeCount, afterCount)`：返回给定节点、token 或注释的源代码，可选地包含其范围前后指定数量的字符。如果 `node` 被省略或为 `undefined`，则返回整个源文本（见[专门一节](#访问源文本accessing-the-source-text)）。
- `getAllComments()`：返回源代码中所有注释的数组（见[专门一节](#访问注释accessing-comments)）。
- `getCommentsBefore(nodeOrToken)`：返回紧邻给定节点或 token 之前的注释 token 数组（见[专门一节](#访问注释accessing-comments)）。
- `getCommentsAfter(nodeOrToken)`：返回紧邻给定节点或 token 之后的注释 token 数组（见[专门一节](#访问注释accessing-comments)）。
- `getCommentsInside(node)`：返回给定节点内所有注释 token 的数组（见[专门一节](#访问注释accessing-comments)）。
- `isSpaceBetween(nodeOrToken, nodeOrToken)`：如果两个 token 之间存在空白字符，则返回 true；如果给定的是节点，则检查第一个节点的最后一个 token 和第二个节点的第一个 token 之间。
- `isGlobalReference(node)`：如果标识符引用的是通过 `languageOptions.globals`、`/* global */` 注释或 `ecmaVersion` 配置的全局变量，而不是由局部绑定声明的，则返回 true。
- `getFirstToken(node, skipOptions)`：返回表示给定节点的第一个 token。
- `getFirstTokens(node, countOptions)`：返回表示给定节点的前 `count` 个 token。
- `getLastToken(node, skipOptions)`：返回表示给定节点的最后一个 token。
- `getLastTokens(node, countOptions)`：返回表示给定节点的最后 `count` 个 token。
- `getTokenAfter(nodeOrToken, skipOptions)`：返回给定节点或 token 之后的第一个 token。
- `getTokensAfter(nodeOrToken, countOptions)`：返回给定节点或 token 之后的 `count` 个 token。
- `getTokenBefore(nodeOrToken, skipOptions)`：返回给定节点或 token 之前的第一个 token。
- `getTokensBefore(nodeOrToken, countOptions)`：返回给定节点或 token 之前的 `count` 个 token。
- `getFirstTokenBetween(nodeOrToken1, nodeOrToken2, skipOptions)`：返回两个节点或 token 之间的第一个 token。
- `getFirstTokensBetween(nodeOrToken1, nodeOrToken2, countOptions)`：返回两个节点或 token 之间的前 `count` 个 token。
- `getLastTokenBetween(nodeOrToken1, nodeOrToken2, skipOptions)`：返回两个节点或 token 之间的最后一个 token。
- `getLastTokensBetween(nodeOrToken1, nodeOrToken2, countOptions)`：返回两个节点或 token 之间的最后 `count` 个 token。
- `getTokens(node)`：返回给定节点的所有 token。
- `getTokensBetween(nodeOrToken1, nodeOrToken2)`：返回两个节点之间的所有 token。
- `getTokenByRangeStart(index, rangeOptions)`：返回在源代码中以给定索引开始范围的 token。
- `getNodeByRangeIndex(index)`：返回 AST 中包含给定源索引的最深节点。
- `getLocFromIndex(index)`：返回一个具有 `line` 和 `column` 属性的对象，对应给定源索引的位置。`line` 从 1 开始，`column` 从 0 开始。
- `getIndexFromLoc(loc)`：返回给定位置在源代码中的索引，其中 `loc` 是具有 1 起始 `line` 键和 0 起始 `column` 键的对象。
- `commentsExistBetween(nodeOrToken1, nodeOrToken2)`：如果两个节点之间存在注释，则返回 `true`。
- `getAncestors(node)`：返回给定节点的祖先数组，从 AST 的根开始，一直到给定节点的直接父节点。此数组不包含给定节点本身。
- `getDeclaredVariables(node)`：返回给定节点声明的[变量](https://eslint.org/docs/latest/extend/scope-manager-interface#variable-interface)列表。此信息可用于跟踪变量的引用。
    - 如果节点是 `VariableDeclaration`，返回声明中声明的所有变量。
    - 如果节点是 `VariableDeclarator`，返回声明符中声明的所有变量。
    - 如果节点是 `FunctionDeclaration` 或 `FunctionExpression`，除了函数参数的变量外，还返回函数名的变量。
    - 如果节点是 `ArrowFunctionExpression`，返回参数的变量。
    - 如果节点是 `ClassDeclaration` 或 `ClassExpression`，返回类名的变量。
    - 如果节点是 `CatchClause`，返回异常参数的变量。
    - 如果节点是 `ImportDeclaration`，返回其所有 specifier 的变量。
    - 如果节点是 `ImportSpecifier`、`ImportDefaultSpecifier` 或 `ImportNamespaceSpecifier`，返回声明的变量。
    - 否则，如果节点未声明任何变量，则返回空数组。
- `getScope(node)`：返回给定节点的[作用域](https://eslint.org/docs/latest/extend/scope-manager-interface#scope-interface)。此信息可用于跟踪变量的引用。
- `markVariableAsUsed(name, refNode)`：将由给定引用节点指示的作用域中具有给定名称的变量标记为已使用。这会影响 [no-unused-vars](https://eslint.org/docs/latest/rules/no-unused-vars) 规则。如果找到具有给定名称的变量并标记为已使用，则返回 `true`，否则返回 `false`。

`skipOptions` 是一个有 3 个属性的对象：`skip`、`includeComments` 和 `filter`。默认值为 `{skip: 0, includeComments: false, filter: null}`。

- `skip`：（`number`）正整数，跳过的 token 数量。如果同时给定 `filter` 选项，被过滤掉的 token 不计入跳过数。
- `includeComments`：（`boolean`）在结果中包含注释 token 的标志。
- `filter(token)`：接收 token 作为第一个参数的函数。如果函数返回 `false`，则结果排除该 token。

`countOptions` 是一个有 3 个属性的对象：`count`、`includeComments` 和 `filter`。默认值为 `{count: 0, includeComments: false, filter: null}`。

- `count`：（`number`）正整数，返回 token 的最大数量。
- `includeComments`：（`boolean`）在结果中包含注释 token 的标志。
- `filter(token)`：接收 token 作为第一个参数的函数，如果函数返回 `false`，则结果排除该 token。

`rangeOptions` 是一个有 1 个属性 `includeComments` 的对象。默认值为 `{includeComments: false}`。

- `includeComments`：（`boolean`）在结果中包含注释 token 的标志。

还有一些属性可以访问：

- `hasBOM`：（`boolean`）指示源代码是否具有 Unicode BOM 的标志。
- `text`：（`string`）被检查代码的完整文本。Unicode BOM 已从此文本中去除。
- `ast`：（`object`）被检查代码的 AST 的 `Program` 节点。
- `scopeManager`：代码的 [ScopeManager](https://eslint.org/docs/latest/extend/scope-manager-interface#scopemanager-interface) 对象。
- `visitorKeys`：（`object`）遍历此 AST 的访问器键。
- `parserServices`：（`object`）包含解析器为规则提供的服务。默认解析器不提供任何服务。但是，如果规则旨在与自定义解析器一起使用，它可以使用 `parserServices` 访问该解析器提供的任何内容。（例如，TypeScript 解析器可以提供获取给定节点计算类型的能力。）
- `lines`：（`array`）按规范定义的行分隔符拆分的行数组。

每当你需要获取有关被检查代码的更多信息时，都应该使用 `SourceCode` 对象。

###### 访问源文本（Accessing the Source Text）

如果你的规则需要获取实际的 JavaScript 源代码来处理，请使用 `sourceCode.getText()` 方法。此方法的工作方式如下：

```js
// 获取全部源代码
var source = sourceCode.getText();

// 只获取此 AST 节点的源代码
var nodeSource = sourceCode.getText(node);

// 只获取此 token 的源代码
var tokenSource = sourceCode.getText(token);

// 只获取此注释的源代码
var commentSource = sourceCode.getText(comment);

// 获取 AST 节点加上前两个字符的源代码
var nodeSourceWithPrev = sourceCode.getText(node, 2);

// 获取 AST 节点加上后两个字符的源代码
var nodeSourceWithFollowing = sourceCode.getText(node, 0, 2);
```

通过这种方式，当 AST 没有提供适当的数据（例如逗号、分号、括号等的位置）时，你可以在 JavaScript 文本本身中查找模式。

###### 访问注释（Accessing Comments）

虽然注释在技术上不属于 AST，但 ESLint 提供了 `sourceCode.getAllComments()`、`sourceCode.getCommentsBefore()`、`sourceCode.getCommentsAfter()` 和 `sourceCode.getCommentsInside()` 来访问它们。

`sourceCode.getCommentsBefore()`、`sourceCode.getCommentsAfter()` 和 `sourceCode.getCommentsInside()` 对于需要检查与给定节点或 token 相关的注释的规则很有用。

请记住，这些方法的结果是按需计算的。

你还可以使用 `includeComments` 选项通过许多 `sourceCode` 的方法访问注释。

##### 选项模式（Options Schemas）

带选项的规则必须指定 `meta.schema` 属性，它是规则选项的 [JSON Schema](https://json-schema.org/) 格式描述，ESLint 将使用它来验证配置选项，并在无效或意外的输入通过 `context.options` 传给规则之前阻止它们。

如果你的规则有选项，强烈建议你指定用于选项验证的 schema。不过，可以通过设置 `schema: false` 选择退出选项验证，但不鼓励这样做，因为它会增加 bug 和错误的几率。

对于未指定 `meta.schema` 属性的规则，当传入任何选项时，ESLint 会抛出错误。如果你的规则没有选项，不要设置 `schema: false`，只需省略 schema 属性或使用 `schema: []`，两者都会阻止传入任何选项。

验证规则配置时，有五个步骤：

1. 如果规则配置不是数组，则将该值包装成数组（例如 `"off"` 变为 `["off"]`）；如果规则配置是数组，则直接使用。
2. ESLint 将规则配置数组的第一个元素验证为严重级别（`"off"`、`"warn"`、`"error"`、`0`、`1`、`2`）
3. 如果严重级别是 `off` 或 `0`，则禁用该规则并停止验证，忽略规则配置数组的任何其他元素。
4. 如果规则已启用，则数组中严重级别之后的任何元素都被复制到 `context.options` 数组中（例如配置 `["warn", "never", { someOption: 5 }]` 会得到 `context.options = ["never", { someOption: 5 }]`）
5. 对 `context.options` 数组运行规则的 schema 验证。

注意：这意味着规则 schema 无法验证严重级别。规则 schema 只验证规则配置中严重级别*之后*的数组元素。规则无法知道自己被配置为什么严重级别。

规则的 `schema` 有两种格式：

- JSON Schema 对象的数组
    - 每个元素将与 `context.options` 数组中的相同位置进行校验。
    - 如果 `context.options` 数组的元素少于 schema 的数量，则不匹配的 schema 将被忽略。
    - 如果 `context.options` 数组的元素多于 schema 的数量，则验证失败。
    - 使用这种格式有两个重要后果：
        - 用户不为你的规则提供任何选项（除严重级别外）*总是有效的*。
        - 如果你指定一个空数组，那么用户为你的规则提供任何选项（除严重级别外）*总是错误的*。
- 一个完整的 JSON Schema 对象，用于验证 `context.options` 数组
    - 即使你的规则只接受一个选项，schema 也应假定要验证的是一个选项数组。
    - schema 可以任意复杂，因此你可以通过 `oneOf`、`anyOf` 等验证完全不同的潜在选项集。
    - 支持的 JSON Schema 版本是 [Draft-04](https://json-schema.org/draft-04/schema)，因此一些较新的特性（如 `if` 或 `$data`）不可用。
        - 目前，由于生态系统兼容性方面的考虑，明确计划不将 schema 支持更新到此级别以上。更多背景请参见[这条评论](https://github.com/eslint/eslint/issues/13888#issuecomment-872591875)。

例如，`yoda` 规则接受一个 `"always"` 或 `"never"` 的主模式参数，以及一个带可选属性 `exceptRange` 的额外选项对象：

```js
// 有效配置：
// "yoda": "warn"
// "yoda": ["error"]
// "yoda": ["error", "always"]
// "yoda": ["error", "never", { "exceptRange": true }]
// 无效配置：
// "yoda": ["warn", "never", { "exceptRange": true }, 5]
// "yoda": ["error", { "exceptRange": true }, "never"]
module.exports = {
	meta: {
		schema: [
			{
				enum: ["always", "never"],
			},
			{
				type: "object",
				properties: {
					exceptRange: { type: "boolean" },
				},
				additionalProperties: false,
			},
		],
	},
};
```

这是等价的基于对象的 schema：

```js
// 有效配置：
// "yoda": "warn"
// "yoda": ["error"]
// "yoda": ["error", "always"]
// "yoda": ["error", "never", { "exceptRange": true }]
// 无效配置：
// "yoda": ["warn", "never", { "exceptRange": true }, 5]
// "yoda": ["error", { "exceptRange": true }, "never"]
module.exports = {
	meta: {
		schema: {
			type: "array",
			minItems: 0,
			maxItems: 2,
			items: [
				{
					enum: ["always", "never"],
				},
				{
					type: "object",
					properties: {
						exceptRange: { type: "boolean" },
					},
					additionalProperties: false,
				},
			],
		},
	},
};
```

对象 schema 可以更精确、更严格地限制允许的内容。例如，下面的 schema 始终要求指定第一个选项（0 到 10 之间的数字），但第二个选项是可选的，并且可以是明确设置了某些选项的对象，或者是 `"off"` 或 `"strict"`：

```js
// 有效配置：
// "someRule": ["error", 6]
// "someRule": ["error", 5, "strict"]
// "someRule": ["warn", 10, { someNonOptionalProperty: true }]
// 无效配置：
// "someRule": "warn"
// "someRule": ["error"]
// "someRule": ["warn", 15]
// "someRule": ["warn", 7, { }]
// "someRule": ["error", 3, "on"]
// "someRule": ["warn", 7, { someOtherProperty: 5 }]
// "someRule": ["warn", 7, { someNonOptionalProperty: false, someOtherProperty: 5 }]
module.exports = {
	meta: {
		schema: {
			type: "array",
			minItems: 1, // 不能只指定严重级别！
			maxItems: 2,
			items: [
				{
					type: "number",
					minimum: 0,
					maximum: 10,
				},
				{
					anyOf: [
						{
							type: "object",
							properties: {
								someNonOptionalProperty: { type: "boolean" },
							},
							required: ["someNonOptionalProperty"],
							additionalProperties: false,
						},
						{
							enum: ["off", "strict"],
						},
					],
				},
			],
		},
	},
};
```

请记住，规则选项始终是数组，因此请注意不要在顶层为非数组类型指定 schema。如果你的 schema 没有在顶层指定数组，用户将*永远无法*启用你的规则，因为当规则启用时，他们的配置总是无效的。

下面是一个总是验证失败的 schema 示例：

```js
// 可能试图验证 ["error", { someOptionalProperty: true }]
// 但当规则启用时，配置总是验证失败，因为选项是数组，不匹配 "object"
module.exports = {
	meta: {
		schema: {
			type: "object",
			properties: {
				someOptionalProperty: {
					type: "boolean",
				},
			},
			additionalProperties: false,
		},
	},
};
```

**注意：**如果你的规则 schema 使用 JSON schema 的 [`$ref`](https://json-schema.org/understanding-json-schema/structuring.html#ref) 属性，你必须使用完整的 JSON Schema 对象，而不是位置属性 schema 的数组。这是因为 ESLint 会将数组简写转换为单个 schema，而不更新引用，这会使它们不正确（它们会被忽略）。

要了解更多关于 JSON Schema 的信息，我们建议查看 [JSON Schema 网站](https://json-schema.org/learn/miscellaneous-examples)上的一些示例，或阅读免费的 [Understanding JSON Schema](https://json-schema.org/understanding-json-schema/) 电子书。

##### 选项默认值（Option Defaults）

规则可以指定一个 `meta.defaultOptions` 数组，为任何选项提供默认值。
当规则在用户配置中启用时，ESLint 会将用户提供的任何选项元素递归地合并到默认元素之上。

例如，给定以下默认值：

```js
export default {
	meta: {
		defaultOptions: [
			{
				alias: "basic",
			},
		],
		schema: [
			{
				type: "object",
				properties: {
					alias: {
						type: "string",
					},
				},
				additionalProperties: false,
			},
		],
	},
	create(context) {
		const [{ alias }] = context.options;

		return {/* ... */};
	},
};
```

除非用户配置指定了不同的值（例如 `["error", { alias: "complex" }]`），否则该规则的运行时 `alias` 值将是 `"basic"`。

选项数组的每个元素按照以下规则合并：

- 任何缺失的值或用户显式提供的 `undefined` 将回退到默认选项
- 用户提供的数组和除 `undefined` 之外的基本类型值会覆盖默认选项
- 用户提供的对象将合并到默认选项对象中，否则替换非对象的默认值

选项默认值也将根据规则的 `meta.schema` 进行验证。

**注意：** ESLint 内部使用 [Ajv](https://ajv.js.org) 进行 schema 验证，并启用了其 [`useDefaults` 选项](https://ajv.js.org/guide/modifying-data.html#assigning-defaults)。
用户提供的选项和 `meta.defaultOptions` 选项都将覆盖规则 schema 中指定的任何默认值。
ESLint 可能会在未来的主版本中禁用 Ajv 的 `useDefaults`。

##### 访问 Shebang（Accessing Shebangs）

[Shebang（#!）](<https://en.wikipedia.org/wiki/Shebang_(Unix)>)由 `"Shebang"` 类型的唯一 token 表示。它们被视为注释，可以通过[访问注释](#访问注释accessing-comments)一节中介绍的方法访问，例如 `sourceCode.getAllComments()`。

##### 访问变量作用域（Accessing Variable Scopes）

`SourceCode#getScope(node)` 方法返回给定节点的作用域。这是一个很有用的方法，用于查找有关给定作用域中变量以及它们在其他作用域中如何使用信息。

::: tip
你可以使用 [Code Explorer](https://explorer.eslint.org) 查看任何 JavaScript 代码的作用域信息。
:::

###### 作用域类型（Scope types）

下表列出了 AST 节点类型及其对应的作用域类型。有关作用域类型的更多信息，请参阅 [`Scope` 对象文档](https://eslint.org/docs/latest/extend/scope-manager-interface#scope-interface)。

| AST 节点类型              | 作用域类型           |
| :------------------------ | :------------------- |
| `Program`                 | `global`             |
| `FunctionDeclaration`     | `function`           |
| `FunctionExpression`      | `function`           |
| `ArrowFunctionExpression` | `function`           |
| `ClassDeclaration`        | `class`              |
| `ClassExpression`         | `class`              |
| `StaticBlock`             | `class-static-block` |
| `BlockStatement` ※1       | `block`              |
| `SwitchStatement` ※1      | `switch`             |
| `ForStatement` ※2         | `for`                |
| `ForInStatement` ※2       | `for`                |
| `ForOfStatement` ※2       | `for`                |
| `WithStatement`           | `with`               |
| `CatchClause`             | `catch`              |
| 其他                      | ※3 ※4                |

**※1** 仅当配置的解析器提供块级作用域特性时。当 `languageOptions.ecmaVersion` 不小于 `6` 时，默认解析器提供块级作用域特性。<br>
**※2** 仅当 `for` 语句将迭代变量定义为块级作用域变量时（例如 `for (let i = 0;;) {}`）。<br>
**※3** 拥有自己作用域的最近祖先节点的作用域。如果最近的祖先节点有多个作用域，则选择最内层的作用域（例如，如果 `Program#sourceType` 为 `"module"`，`Program` 节点有一个 `global` 作用域和一个 `module` 作用域。最内层的作用域是 `module` 作用域）。<br>
**※4** 每个 `PropertyDefinition#value` 节点（它可以是任何表达式节点类型）都有一个 `class-field-initializer` 作用域。例如，在 `class C { field = 1 }` 中，表示 `1` 的 `Literal` 节点具有 `class-field-initializer` 作用域。如果该节点有其他作用域，`class-field-initializer` 作用域将是最外层的。例如，在 `class C { field = () => {} }` 中，`ArrowFunctionExpression` 节点有两个作用域：`class-field-initializer` 和 `function`。

###### 作用域变量（Scope Variables）

`Scope#variables` 属性包含一个 [`Variable` 对象](https://eslint.org/docs/latest/extend/scope-manager-interface#variable-interface)的数组。这些是在当前作用域中声明的变量。你可以使用这些 `Variable` 对象来跟踪整个模块中对变量的引用。

在每个 `Variable` 内部，`Variable#references` 属性包含一个 [`Reference` 对象](https://eslint.org/docs/latest/extend/scope-manager-interface#reference-interface)的数组。`Reference` 数组包含模块源代码中引用该变量的所有位置。

同样在每个 `Variable` 内部，`Variable#defs` 属性包含一个 [`Definition` 对象](https://eslint.org/docs/latest/extend/scope-manager-interface#definition-interface)的数组。你可以使用这些 `Definition` 来找到变量的定义位置。

全局变量有以下额外属性：

- `Variable#writeable`（`boolean | undefined`）…… 如果为 `true`，此全局变量可以被赋任意值。如果为 `false`，此全局变量是只读的。
- `Variable#eslintExplicitGlobal`（`boolean | undefined`）…… 如果为 `true`，此全局变量是由源代码文件中的 `/* globals */` 指令注释定义的。
- `Variable#eslintExplicitGlobalComments`（`Comment[] | undefined`）…… 在源代码文件中定义此全局变量的 `/* globals */` 指令注释数组。如果没有 `/* globals */` 指令注释，此属性为 `undefined`。
- `Variable#eslintImplicitGlobalSetting`（`"readonly" | "writable" | undefined`）…… 配置文件中的配置值。如果存在 `/* globals */` 指令注释，这可能与 `variable.writeable` 不同。

有关使用 `SourceCode#getScope()` 跟踪变量的示例，请参阅以下内置规则的源代码：

- [no-shadow](https://github.com/eslint/eslint/blob/main/lib/rules/no-shadow.js)：在 `Program` 节点调用 `sourceCode.getScope()`，并检查所有子作用域，以确保变量名没有在更低的作用域中被复用。（[no-shadow](https://eslint.org/docs/latest/rules/no-shadow) 文档）
- [no-redeclare](https://github.com/eslint/eslint/blob/main/lib/rules/no-redeclare.js)：在每个作用域调用 `sourceCode.getScope()`，以确保变量没有在同一作用域中被声明两次。（[no-redeclare](https://eslint.org/docs/latest/rules/no-redeclare) 文档）

##### 将变量标记为已使用（Marking Variables as Used）

某些 ESLint 规则（如 [`no-unused-vars`](https://eslint.org/docs/latest/rules/no-unused-vars)）会检查变量是否已被使用。ESLint 本身只知道变量访问的标准规则，因此自定义的变量访问方式可能不会被登记为"已使用"。

为帮助解决这个问题，你可以使用 `sourceCode.markVariableAsUsed()` 方法。此方法接受两个参数：要标记为已使用的变量名，以及一个可选的引用节点（指示你正在处理的作用域）。下面是一个示例：

```js
module.exports = {
	create: function (context) {
		var sourceCode = context.sourceCode;

		return {
			ReturnStatement(node) {
				// 在函数的作用域中查找 myCustomVar 并标记为已使用
				sourceCode.markVariableAsUsed("myCustomVar", node);

				// 或：在全局作用域中查找 myCustomVar 并标记为已使用
				sourceCode.markVariableAsUsed("myCustomVar");
			},
		};
		// ...
	},
};
```

这里，`myCustomVar` 变量相对于一个 `ReturnStatement` 节点被标记为已使用，这意味着 ESLint 将从最接近该节点的作用域开始搜索。如果省略第二个参数，则使用顶层作用域。（对于 ESM 文件，顶层作用域是模块作用域；对于 CommonJS 文件，顶层作用域是第一个函数作用域。）

##### 访问代码路径（Accessing Code Paths）

ESLint 在遍历 AST 时分析代码路径。你可以通过七个与代码路径相关的事件访问代码路径对象。更多信息请参阅[代码路径分析（Code Path Analysis）](https://eslint.org/docs/latest/extend/code-path-analysis)。

#### 规则单元测试（Rule Unit Tests）

ESLint 提供了 [`RuleTester`](https://eslint.org/docs/latest/integrate/nodejs-api#ruletester) 工具，使为规则编写测试变得容易。

#### 规则命名约定（Rule Naming Conventions）

虽然你可以给自定义规则起任何名字，但核心规则有命名约定。将这些相同的命名约定应用到你的自定义规则可能会更清晰。要了解更多，请参阅[核心规则命名约定](https://eslint.org/docs/latest/contribute/core-rules#rule-naming-conventions)文档。

#### 运行时规则（Runtime Rules）

你可以直接在配置文件中包含自定义规则。这非常适合那些特定于你的项目或公司、不适合随 ESLint 一起发布或作为插件发布的规则。

这些规则的书写格式与所有其他规则相同。像创建任何其他规则一样创建你的规则，然后按照以下步骤操作：

1. 将你的规则导入到你的[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file)中。
2. 在 `plugins` 键下定义一个[虚拟插件](https://eslint.org/docs/latest/use/configure/plugins#configuring-a-virtual-plugin)，并将你的规则添加到它的 `rules` 对象中。
3. 在 `rules` 键下为你的规则指定严重级别，使用插件命名空间作为规则 ID 的前缀（例如 `local/my-rule`）。除非规则的严重级别是 `"warn"` 或 `"error"`，否则它不会运行。

例如，如果你的规则在 `rules/my-rule.js` 中，你的配置文件将如下所示：

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
			"local/my-rule": "error",
		},
	},
]);
```

#### 分析规则性能（Profile Rule Performance）

ESLint 有一个内置方法来跟踪各个规则的性能。设置 `TIMING` 环境变量会在 lint 完成时触发显示运行时间最长的十条规则，以及它们各自的运行时间（规则创建 + 规则执行）和占规则总处理时间（规则创建 + 规则执行）百分比的相对性能影响。

```bash
$ TIMING=1 eslint lib
Rule                    | Time (ms) | Relative
:-----------------------|----------:|--------:
no-multi-spaces         |    52.472 |     6.1%
camelcase               |    48.684 |     5.7%
no-irregular-whitespace |    43.847 |     5.1%
valid-jsdoc             |    40.346 |     4.7%
handle-callback-err     |    39.153 |     4.6%
space-infix-ops         |    35.444 |     4.1%
no-undefined            |    25.693 |     3.0%
no-shadow               |    22.759 |     2.7%
no-empty-class          |    21.976 |     2.6%
semi                    |    19.359 |     2.3%
```

要明确测试单条规则，组合使用 `--no-config-lookup` 和 `--rule` 选项：

```bash
$ TIMING=1 eslint --no-config-lookup --rule "quotes: [2, 'double']" lib
Rule   | Time (ms) | Relative
:------|----------:|--------:
quotes |    18.066 |   100.0%
```

要查看更长的结果列表（超过 10 条），将环境变量设置为其他值，例如 `TIMING=50` 或 `TIMING=all`。

要获取更细粒度的计时信息（每个文件每条规则），请改用 [`stats`](https://eslint.org/docs/latest/extend/stats) 选项。


---

### 代码路径分析（Code Path Analysis）

> 原文链接：https://eslint.org/docs/latest/extend/code-path-analysis

::: tip
本页适用于 JS [语言](https://eslint.org/docs/latest/extend/languages)的 ESLint 规则。这包括使用替代解析器处理 TypeScript 和 Vue 等 JS 语法扩展的规则，但不包括 CSS 或 Markdown 等不相关语言的规则。
:::

ESLint 规则可以访问一个用于分析代码路径的 API。
代码路径表示执行在程序中可能经过的控制流图。
它由代码路径段（code path segment）组成，这些段在 `if`、`while`、`return`、`continue` 等分支结构和其他控制流语句处分叉和汇合。

下面是一个非常基础的程序的代码路径图：

```js
if (someCondition()) {
	foo();
}
bar();
```

![代码路径示例](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-simple-if-branch.svg)

::: tip
你可以使用 [Code Explorer](https://explorer.eslint.org) 查看任何 JavaScript 代码的代码路径图。
:::

#### 对象（Objects）

一个程序/文件包含多个代码路径：一个用于全局/顶层执行，另外每个函数、类字段初始化器和类静态块各有一个。
代码路径分析 API 中有两个基本对象：`CodePath` 和 `CodePathSegment`。

##### `CodePath`

`CodePath` 对象描述程序中的一个控制流图。它有以下属性：

- `id`（`string`）- 唯一标识代码路径的字符串。规则可以使用 `id` 作为键，为每个代码路径保存额外信息。
- `origin`（`string`）- 在程序中创建此代码路径的构造。可以是 `"program"`、`"function"`、`"class-field-initializer"` 或 `"class-static-block"`。
- `initialSegment`（`CodePathSegment`）- 代码路径的初始段。
- `finalSegments`（`CodePathSegment[]`）- 代码路径的最终段，包括返回（returned）和抛出（thrown）的段。
- `returnedSegments`（`CodePathSegment[]`）- 代码路径的最终段，只包括返回的段。
- `thrownSegments`（`CodePathSegment[]`）- 代码路径的最终段，只包括抛出的段。
- `upper`（`CodePath | null`）- 包含此代码路径的函数/类/全局作用域的代码路径。
- `childCodePaths`（`CodePath[]`）- 此代码路径包含的函数/类的代码路径。

`CodePath` 有以下方法：

- `traverseSegments(optionsOrCallback, callback)` - 以深度优先的顺序，从初始段到最终段遍历此代码路径中所有可达的段。你可以选择性地传递一个带有 `first`（`CodePathSegment`）和 `last`（`CodePathSegment`）属性的选项对象来限制遍历范围。回调接收一个 `CodePathSegment` 和一个控制器对象，并以 `this` 设置为当前 `CodePath` 的方式调用。控制器有一个 `skip()` 方法用于跳过当前分支中的后续段，以及一个 `break()` 方法用于停止遍历。

##### `CodePathSegment`

`CodePathSegment` 是代码路径中没有分支的一部分。在程序的每个分支点，一个 `CodePathSegment` 结束，并包含指向下一个可能的 `CodePathSegment` 的链接，形成有向图（一般来说可以包含环，例如存在 `while` 循环时）。`CodePathSegment` 还包含指向前序可能段的链接，用于程序分支汇合的情况（例如 `if` 块之后）。

`CodePathSegment` 有以下属性：

- `id`（`string`）- 唯一标识代码路径段的字符串。规则可以使用 `id` 作为键，为每个段保存额外信息。
- `nextSegments`（`CodePathSegment[]`）- 下一个（可达的）段。如果是分叉，则有两个或更多。如果是最终段，则为空。
- `prevSegments`（`CodePathSegment[]`）- 前一个（可达的）段。如果是汇合，则有两个或更多。如果是初始段，则为空。
- `allNextSegments`（`CodePathSegment[]`）- 下一个段，包括可达和不可达的段。
- `allPrevSegments`（`CodePathSegment[]`）- 前一个段，包括可达和不可达的段。
- `reachable`（`boolean`）- 指示该段是否可达的标志。在 `return`、`throw`、`break` 或 `continue` 之后的段中，此值为 `false`。

#### 访问代码路径（Accessing Code Paths）

要在规则中使用代码路径分析，你可以在规则的 `create()` 方法导出的对象（即包含 AST 节点访问器的同一个对象）中定义代码路径事件（详见下文）的事件处理器。所有访问器（包括代码路径访问器）都在对源代码的单次遍历中按源代码顺序调用。这意味着多个代码路径可能同时处于打开状态，需要手动跟踪（见下文示例["跟踪当前段位置"](#跟踪当前段位置track-current-segment-position)）。

```js
export default {
	meta: {
		// ...
	},
	create(context) {
		return {
			/**
			 * 在开始遍历代码路径时调用。
			 *
			 * @param {CodePath} codePath - 新的代码路径。
			 * @param {ASTNode} node - 当前节点。
			 * @returns {void}
			 */
			onCodePathStart(codePath, node) {
				// 对 codePath 做些什么
			},

			/**
			 * 在结束遍历代码路径时调用。
			 *
			 * @param {CodePath} codePath - 已完成的代码路径。
			 * @param {ASTNode} node - 当前节点。
			 * @returns {void}
			 */
			onCodePathEnd(codePath, node) {
				// 对 codePath 做些什么
			},

			/**
			 * 在进入一个可达的代码路径段时调用。
			 *
			 * @param {CodePathSegment} segment - 新的代码路径段。
			 * @param {ASTNode} node - 当前节点。
			 * @returns {void}
			 */
			onCodePathSegmentStart(segment, node) {
				// 对 segment 做些什么
			},

			/**
			 * 在退出一个可达的代码路径段时调用。
			 *
			 * @param {CodePathSegment} segment - 离开的代码路径段。
			 * @param {ASTNode} node - 当前节点。
			 * @returns {void}
			 */
			onCodePathSegmentEnd(segment, node) {
				// 对 segment 做些什么
			},

			/**
			 * 在进入一个不可达的代码路径段时调用。
			 *
			 * @param {CodePathSegment} segment - 进入的代码路径段。
			 * @param {ASTNode} node - 当前节点。
			 * @returns {void}
			 */
			onUnreachableCodePathSegmentStart(segment, node) {
				// 对 segment 做些什么
			},

			/**
			 * 在退出一个不可达的代码路径段时调用。
			 *
			 * @param {CodePathSegment} segment - 退出的代码路径段。
			 * @param {ASTNode} node - 当前节点。
			 * @returns {void}
			 */
			onUnreachableCodePathSegmentEnd(segment, node) {
				// 对 segment 做些什么
			},

			/**
			 * 在即将退出一个可达段（`fromSegment`）之前调用，
			 * 此时离开该段的控制流可以循环回到源代码中更早的
			 * 位置（`toSegment` 的起点），例如在循环体末尾或
			 * `continue;` 语句处。请注意，在某些情况下
			 * `fromSegment === toSegment`，例如简单的
			 * `do...while` 循环或测试/更新表达式为空的
			 * `for (;;)` 循环。
			 *
			 * 此事件总是发生在 `fromSegment` 的
			 * `onCodePathSegmentStart` 和 `onCodePathSegmentEnd` 之间。
			 * 而 `toSegment` 的 `onCodePathSegmentStart` 必定已经被调用，
			 * 其 `onCodePathSegmentEnd` 当且仅当
			 * `toSegment !== fromSegment` 时已经被调用。
			 *
			 * @param {CodePathSegment} fromSegment - 循环起始的代码路径段。
			 * @param {CodePathSegment} toSegment - 循环去往的代码路径段。
			 * @param {ASTNode} node - 当前节点。
			 * @returns {void}
			 */
			onCodePathSegmentLoop(fromSegment, toSegment, node) {
				// 对 segment 做些什么
			},

			// AST 节点访问器……
		};
	},
};
```

::: important

在旧版 ESLint（ESLint v9 之前）中，代码路径和代码路径段是在遍历过程中构建的，因此当事件被调用时，事件载荷中的代码路径和段只被部分初始化。

然而，自 ESLint v9 起，ESLint 会在规则运行之前对 AST 执行一次完整的预扫描，以构建完整的代码路径信息（[参见迁移指南](https://eslint.org/docs/latest/use/migrate-to-9.0.0#-code-paths-are-now-precalculated)）。这意味着当任何 `onCodePath*` 事件触发时，它接收的 `CodePath` 和 `CodePathSegment` 对象已经包含了所有数据（例如 `childCodePaths`、`nextSegments`、`prevSegments`），即使对于规则所见的遍历中尚未访问的代码部分也是如此。你不需要等待稍后的事件（如 `onCodePathEnd`）就能安全地读取这些属性。

:::

::: warning

`CodePath` 和 `CodePathSegment` 对象是可变的，并且在所有代码路径事件和所有规则之间共享，因此规则应注意永远不要修改它们。

:::

##### 示例：简单程序（Example: Simple Program）

考虑以下简单但并不平凡的程序：

```js
function foo(x) {
	if (x) {
		setTimeout(() => {
			console.log("logging x after 100 ms", x);
		}, 100);
	}
}

function bar(y) {
	if (typeof y === "string") {
		console.log(y);
	} else {
		console.log("not a string");
	}
}

if (Math.random() < 0.5) {
	foo();
} else {
	bar();
}
```

它有 4 个代码路径：

1. 顶层代码（`origin: "program"`）：

    ![简单程序代码路径 1](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-simpleprogram-codepath1.svg)

1. 函数 `foo`（`origin: "function"`）：

    ![简单程序代码路径 2](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-simpleprogram-codepath2.svg)

1. 在 `foo` 内部定义的箭头函数回调（`origin: "function"`）：

    ![简单程序代码路径 3](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-simpleprogram-codepath3.svg)

1. 函数 `bar`（`origin: "function"`）：

    ![简单程序代码路径 4](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-simpleprogram-codepath4.svg)

其代码路径事件按以下顺序调用（缩进表示 start/end 对之间的嵌套）：

```text
onCodePathStart                  s1   (top level)
  onCodePathSegmentStart         s1_1 (before the `if`, up through its test)
    onCodePathStart              s2   (foo)
      onCodePathSegmentStart     s2_1 (before `if (x)`, up through its test)
      onCodePathSegmentEnd       s2_1
      onCodePathSegmentStart     s2_2 (`if (x)` true branch: the `setTimeout(...)` call)
        onCodePathStart          s3   (arrow function callback)
          onCodePathSegmentStart s3_1 (the whole callback body)
          onCodePathSegmentEnd   s3_1
        onCodePathEnd            s3
      onCodePathSegmentEnd       s2_2
      onCodePathSegmentStart     s2_3 (after `if (x)`; falls off the end of `foo`)
      onCodePathSegmentEnd       s2_3
    onCodePathEnd                s2
    onCodePathStart              s4   (bar)
      onCodePathSegmentStart     s4_1 (before the `if`, up through its test)
      onCodePathSegmentEnd       s4_1
      onCodePathSegmentStart     s4_2 (`if` true branch: `console.log(y)`)
      onCodePathSegmentEnd       s4_2
      onCodePathSegmentStart     s4_3 (`else` branch: `console.log("not a string")`)
      onCodePathSegmentEnd       s4_3
      onCodePathSegmentStart     s4_4 (after the `if`/`else`, where the branches join; falls off the end of `bar`)
      onCodePathSegmentEnd       s4_4
    onCodePathEnd                s4
  onCodePathSegmentEnd           s1_1
  onCodePathSegmentStart         s1_2 (top-level `if` true branch: the `foo();` call)
  onCodePathSegmentEnd           s1_2
  onCodePathSegmentStart         s1_3 (top-level `else` branch: the `bar();` call)
  onCodePathSegmentEnd           s1_3
  onCodePathSegmentStart         s1_4 (after the top-level `if`/`else`, where the branches join)
  onCodePathSegmentEnd           s1_4
onCodePathEnd                    s1
```

##### 关于 `onCodePathSegmentLoop`（About `onCodePathSegmentLoop`）

每当遍历到达控制流循环回到之前已进入的某个段的位置时，就会触发此事件。它主要在循环末尾触发。

考虑以下 `while` 循环的代码路径：

```js
while (a) {
	a = foo();
}
bar();
```

![循环事件示例](https://eslint.org/docs/latest/assets/images/code-path-analysis/loop-event-example-while.svg)

此程序的事件序列如下所示（缩进表示 start/end 对之间的嵌套）：

```text
onCodePathStart           (program)
  onCodePathSegmentStart  (before the loop)
  onCodePathSegmentEnd    (before the loop)
  onCodePathSegmentStart  (test: `a`)
  onCodePathSegmentEnd    (test: `a`)
  onCodePathSegmentStart  (body: `a = foo();`)
    onCodePathSegmentLoop (body -> test, highlighted above in red)
  onCodePathSegmentEnd    (body)
  onCodePathSegmentStart  (after the loop: `bar();`)
  onCodePathSegmentEnd    (after the loop)
onCodePathEnd             (program)
```

请注意，`onCodePathSegmentLoop` 在 `body` 段的 `onCodePathSegmentEnd` *之前*触发：它表示控制流从 `body` 末尾的某个点循环回到 `test`。因为 `test` 在之前的步骤中已经被完整访问过，所以它的 `onCodePathSegmentStart`/`onCodePathSegmentEnd` 对不会再次触发。

`onCodePathSegmentLoop` 事件可用于分析循环中的控制流。例如，参见 [`no-unreachable-loop`](https://eslint.org/docs/latest/rules/no-unreachable-loop) 的实现。

#### 使用示例（Usage Examples）

##### 跟踪当前段位置（Track current segment position）

要跟踪当前代码路径段的位置，你可以定义一个这样的规则：

```js
export default {
	meta: {
		// ...
	},
	create(context) {
		// 跟踪我们当前所在的代码路径
		let currentCodePath;

		// 一个 Set，跟踪我们在当前代码路径中已遍历的段
		let currentSegments;

		// 栈，跟踪所有打开路径的当前段 Set。
		const allCurrentSegments = [];

		return {
			onCodePathStart(codePath) {
				currentCodePath = codePath;
				allCurrentSegments.push(currentSegments);
				currentSegments = new Set();
			},

			onCodePathEnd(codePath) {
				currentCodePath = codePath.upper;
				currentSegments = allCurrentSegments.pop();
			},

			onCodePathSegmentStart(segment) {
				currentSegments.add(segment);
			},

			onCodePathSegmentEnd(segment) {
				currentSegments.delete(segment);
			},

			onUnreachableCodePathSegmentStart(segment) {
				currentSegments.add(segment);
			},

			onUnreachableCodePathSegmentEnd(segment) {
				currentSegments.delete(segment);
			},
		};
	},
};
```

在此示例中，`currentCodePath` 变量用于访问当前正在遍历的代码路径，`currentSegments` 变量跟踪该代码路径中到该时点为止已遍历的段。请注意，`currentSegments` 以空集开始，也以空集结束，随着遍历的进行不断更新。

跟踪当前段位置有助于分析通向特定节点的代码路径，如下一个示例所示。

##### 查找不可达节点（Find an unreachable node）

要查找不可达节点，跟踪当前段位置，然后使用节点访问器检查是否有任何段是可达的。例如，下面的规则查找任何不可达的 `ExpressionStatement`。

```js
function areAnySegmentsReachable(segments) {
	for (const segment of segments) {
		if (segment.reachable) {
			return true;
		}
	}

	return false;
}

export default {
	meta: {
		// ...
	},
	create(context) {
		// 跟踪我们当前所在的代码路径
		let currentCodePath;

		// 一个 Set，跟踪我们在当前代码路径中已遍历的段
		let currentSegments;

		// 栈，跟踪所有打开路径的当前段 Set。
		const allCurrentSegments = [];

		return {
			onCodePathStart(codePath) {
				currentCodePath = codePath;
				allCurrentSegments.push(currentSegments);
				currentSegments = new Set();
			},

			onCodePathEnd(codePath) {
				currentCodePath = codePath.upper;
				currentSegments = allCurrentSegments.pop();
			},

			onCodePathSegmentStart(segment) {
				currentSegments.add(segment);
			},

			onCodePathSegmentEnd(segment) {
				currentSegments.delete(segment);
			},

			onUnreachableCodePathSegmentStart(segment) {
				currentSegments.add(segment);
			},

			onUnreachableCodePathSegmentEnd(segment) {
				currentSegments.delete(segment);
			},

			ExpressionStatement(node) {
				// 检查通向此节点的所有代码路径段
				if (!areAnySegmentsReachable(currentSegments)) {
					context.report({ message: "Unreachable!", node });
				}
			},
		};
	},
};
```

另请参阅：
[no-unreachable](https://github.com/eslint/eslint/blob/HEAD/lib/rules/no-unreachable.js)、
[no-fallthrough](https://github.com/eslint/eslint/blob/HEAD/lib/rules/no-fallthrough.js)、
[consistent-return](https://github.com/eslint/eslint/blob/HEAD/lib/rules/consistent-return.js)

##### 检查函数是否在每条路径中都被调用（Check if a function is called in every path）

此示例检查参数 `cb` 是否在每条路径中都被调用。

```js
function getCbParameter(codePath, node, context) {
	if (codePath.origin !== "function") {
		return null;
	}

	return (
		context.sourceCode
			.getDeclaredVariables(node)
			.find(
				variable =>
					variable.name === "cb" &&
					variable.defs.some(def => def.type === "Parameter"),
			) ?? null
	);
}

function hasPathWithoutCall(codePath, segmentsWithCall) {
	const visited = new Set();
	const stack = [codePath.initialSegment];

	while (stack.length > 0) {
		const segment = stack.pop();

		if (visited.has(segment) || segmentsWithCall.has(segment)) {
			continue;
		}
		visited.add(segment);

		if (codePath.finalSegments.includes(segment)) {
			return true;
		}

		stack.push(...segment.nextSegments);
	}

	return false;
}

export default {
	meta: {
		// ...
	},
	create(context) {
		let codePathInfo = null;
		const codePathInfoStack = [];

		return {
			onCodePathStart(codePath, node) {
				codePathInfoStack.push(codePathInfo);

				codePathInfo = {
					cbVariable: getCbParameter(codePath, node, context),
					currentSegments: new Set(),
					segmentsWithCall: new Set(),
				};
			},

			onCodePathEnd(codePath, node) {
				const { cbVariable, segmentsWithCall } = codePathInfo;

				codePathInfo = codePathInfoStack.pop();

				if (!cbVariable) {
					return;
				}

				if (hasPathWithoutCall(codePath, segmentsWithCall)) {
					context.report({
						message: "`cb` should be called in every path.",
						node,
					});
				}
			},

			onCodePathSegmentStart(segment) {
				codePathInfo.currentSegments.add(segment);
			},

			onCodePathSegmentEnd(segment) {
				codePathInfo.currentSegments.delete(segment);
			},

			CallExpression(node) {
				const { cbVariable } = codePathInfo;

				if (
					cbVariable?.references.some(
						reference => reference.identifier === node.callee,
					)
				) {
					for (const segment of codePathInfo.currentSegments) {
						codePathInfo.segmentsWithCall.add(segment);
					}
				}
			},
		};
	},
};
```

另请参阅：
[constructor-super](https://github.com/eslint/eslint/blob/HEAD/lib/rules/constructor-super.js)、
[no-this-before-super](https://github.com/eslint/eslint/blob/HEAD/lib/rules/no-this-before-super.js)

#### 局限性（Limitations）

ESLint 的代码路径分析是对 JavaScript 程序实际运行时可能性的近似，而不是精确模型。

例如，在现代 JavaScript 中，几乎任何东西在技术上都可能抛出异常，包括函数调用、属性访问，甚至[引用已声明的标识符](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/let#temporal_dead_zone_tdz)。
然而，在 `try` 块之外，代码路径分析假定所有这些操作都会成功，而不是对每个可能的抛出点建模。有关 `try` 构造中控制流如何建模的详细信息，请参见下面的 `TryStatement` 示例。

另一个例子是，代码路径分析将所有函数调用视为不透明的，即使能清楚地看出正在调用哪个函数。请参见下面带函数调用的示例。

#### 代码路径示例（Code Path Examples）

##### Hello World

```js
console.log("Hello world!");
```

![Hello World](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-hello-world.svg)

##### `IfStatement`

```js
if (a) {
	foo();
} else {
	bar();
}
```

![`IfStatement`](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-ifstatement.svg)

##### `IfStatement`（链式）

```js
if (a) {
	foo();
} else if (b) {
	bar();
} else if (c) {
	baz();
}
```

![`IfStatement`（链式）](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-ifstatement-chain.svg)

##### `LogicalExpression`

请注意，由于短路行为，`&&` 和 `||` 是分支结构。

```js
const foo = a && b;
```

![`LogicalExpression`](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-logicalexpression.svg)

##### `SwitchStatement`

```js
switch (a) {
	case 0:
		foo();
		break;

	case 1:
	case 2:
		bar();
	// 贯穿（fallthrough）

	case 3:
		baz();
		break;
}
```

![`SwitchStatement`](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-switchstatement.svg)

##### `SwitchStatement`（带 `default`）

```js
switch (a) {
	case 0:
		foo();
		break;

	case 1:
	case 2:
		bar();
	// 贯穿（fallthrough）

	case 3:
		baz();
		break;

	default:
		quux();
		break;
}
```

![`SwitchStatement`（带 `default`）](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-switchstatement-has-default.svg)

##### `TryStatement`（try-catch）

```js
try {
	foo();
	if (a) {
		throw new Error();
	}
	bar();
} catch (err) {
	baz(err);
}
last();
```

代码路径分析会在以下位置创建从 `try` 块到 `catch` 块的路径：

- `throw` 语句。
- `try` 块中第一个可能抛出的节点（例如函数调用）。
- `try` 块的末尾。

![`TryStatement`（try-catch）](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-trystatement-try-catch.svg)

##### `TryStatement`（try-finally）

```js
try {
	foo();
	bar();
} finally {
	baz();
}
last();
```

如果没有 `catch` 块，`finally` 块有两个当前段：正常路径和离开路径。请注意，即使 `try` 块不包含 `throw` 或 `return`，离开路径在这里也存在，因为像 `foo()` 这样的函数调用被视为可能抛出。

如果你在此代码上运行上面的"查找不可达节点"示例，遍历 `finally` 块时 `currentSegments.size` 将是 `2`。

![`TryStatement`（try-finally）](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-trystatement-try-finally.svg)

##### `TryStatement`（try-catch-finally）

```js
try {
	foo();
	bar();
} catch (err) {
	baz(err);
} finally {
	quux();
}
last();
```

![`TryStatement`（try-catch-finally）](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-trystatement-try-catch-finally.svg)

##### `WhileStatement`

```js
while (a) {
	foo();
	if (b) {
		continue;
	}
	bar();
}
```

![`WhileStatement`](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-whilestatement.svg)

##### `DoWhileStatement`

```js
do {
	foo();
	bar();
} while (a);
```

![`DoWhileStatement`](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-dowhilestatement.svg)

##### `ForStatement`

```js
for (let i = 0; i < 10; ++i) {
	foo();
	if (b) {
		break;
	}
	bar();
}
```

![`ForStatement`](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-forstatement.svg)

##### `ForStatement`（无限循环）

```js
for (;;) {
	foo();
}
bar();
```

![`ForStatement`（无限循环）](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-forstatement-infinite-loop.svg)

##### `ForInStatement`

```js
for (let key in obj) {
	foo(key);
}
```

![`ForInStatement`](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-forinstatement.svg)

##### 当有函数时（When there is a function）

```js
function foo(a) {
	if (a) {
		return;
	}
	bar();
}

foo(false);
```

此程序由两个代码路径组成：

1. 全局代码路径

    ![当有函数时——全局代码路径](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-when-there-is-a-function-1.svg)

1. 函数 `foo()` 的代码路径

    ![当有函数时——函数代码路径](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-when-there-is-a-function-2.svg)

请注意，从包含对 `foo()` 调用的代码路径段到包含 `foo()` 实现的代码路径段之间没有链接。这是因为在代码路径分析中，函数调用被视为不透明的，即使从源代码中可以明确看出正在调用哪个函数。

##### 生成器函数中的 `YieldExpression`（`YieldExpression` in generator functions）

```js
function* generator(flag) {
	yield 1;
	if (flag) {
		foo();
	}
	bar();
}
```

在生成器函数中，`yield` 可以创建三个代码路径：

- 到下一段。
- 到抛出路径。
- 到函数末尾。

![生成器函数中的 `YieldExpression`](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-yieldExpression-in-generator-functions.svg)


---

### 选择器（Selectors）

> 原文链接：https://eslint.org/docs/latest/extend/selectors

一些规则和 API 允许使用选择器来查询 AST。本页旨在：

1. 解释什么是选择器
2. 描述创建选择器的语法
3. 描述选择器可以用于什么

#### 什么是选择器？（What is a selector?）

选择器是一个可用于匹配抽象语法树（AST）中节点的字符串。它对于描述代码中的特定语法模式很有用。

AST 选择器的语法类似于 [CSS 选择器](https://developer.mozilla.org/zh-CN/docs/Web/CSS/CSS_Selectors)的语法。如果你以前用过 CSS 选择器，AST 选择器的语法应该很容易理解。

最简单的选择器就是节点类型。节点类型选择器会匹配具有给定类型的所有节点。例如，考虑以下程序：

```js
var foo = 1;
bar.baz();
```

选择器 `"Identifier"` 会匹配程序中的所有 `Identifier` 节点。在这个例子中，该选择器会匹配 `foo`、`bar` 和 `baz` 对应的节点。

选择器不限于匹配单个节点类型。例如，选择器 `VariableDeclarator > Identifier` 会匹配所有直接父节点是 `VariableDeclarator` 的 `Identifier` 节点。在上面的程序中，它会匹配 `foo` 的节点，但不匹配 `bar` 和 `baz` 的节点。

#### 选择器可以有哪些语法？（What syntax can selectors have?）

支持以下选择器：

- AST 节点类型：`ForStatement`
- 通配符（匹配所有节点）：`*`
- 属性存在：`[attr]`
- 属性值：`[attr="foo"]` 或 `[attr=123]`
- 属性正则：`[attr=/foo.*/]`
- 属性条件：`[attr!="foo"]`、`[attr>2]`、`[attr<3]`、`[attr>=2]` 或 `[attr<=3]`
- 嵌套属性：`[attr.level2="foo"]`
- 字段：`FunctionDeclaration > Identifier.id`
- 第一个或最后一个子节点：`:first-child` 或 `:last-child`
- 第 n 个子节点（不支持 ax+b 形式）：`:nth-child(2)`
- 倒数第 n 个子节点（不支持 ax+b 形式）：`:nth-last-child(1)`
- 后代：`FunctionExpression ReturnStatement`
- 子节点：`UnaryExpression > Literal`
- 后续兄弟：`VariableDeclaration ~ VariableDeclaration`
- 相邻兄弟：`ArrayExpression > Literal + SpreadElement`
- 否定：`:not(ForStatement)`
- 匹配任意：`:matches([attr] > :first-child, :last-child)` 或 `:is([attr] > :first-child, :last-child)`
- AST 节点类别：`:statement`、`:expression`、`:declaration`、`:function` 或 `:pattern`

这套语法非常强大，可用于精确选择代码中的许多语法模式。

<sup>本节的示例改编自 [esquery](https://github.com/estools/esquery) 文档。</sup>

#### 选择器可以用于什么？（What can selectors be used for?）

如果你在编写自定义 ESLint 规则，你可能会有兴趣使用选择器来检查 AST 的特定部分。如果你在为代码库配置 ESLint，你可能会有兴趣用选择器限制特定的语法模式。

##### 在规则中监听选择器（Listening for selectors in rules）

编写自定义 ESLint 规则时，你可以在 AST 被遍历时监听匹配特定选择器的节点。

```js
module.exports = {
	create(context) {
		// ...

		return {
			// 此监听器会对所有带块语句的 IfStatement 节点调用。
			"IfStatement > BlockStatement": function (blockStatementNode) {
				// ...你的逻辑
			},

			// 此监听器会对所有参数超过 3 个的函数声明调用。
			"FunctionDeclaration[params.length>3]": function (
				functionDeclarationNode,
			) {
				// ...你的逻辑
			},
		};
	},
};
```

在选择器末尾添加 `:exit` 会使监听器在遍历期间退出匹配节点时调用，而不是在进入时调用。

如果两个或更多选择器匹配同一个节点，它们的监听器将按特异性（specificity）递增的顺序调用。AST 选择器的特异性类似于 CSS 选择器的特异性：

- 比较两个选择器时，包含更多类选择器、属性选择器和伪类选择器（不包括 `:not()`）的选择器特异性更高。
- 如果类/属性/伪类数量相同，则包含更多节点类型选择器的选择器特异性更高。

如果多个选择器特异性相同，它们的监听器将按该节点的字母顺序调用。

##### 用选择器限制语法（Restricting syntax with selectors）

使用 [no-restricted-syntax](https://eslint.org/docs/latest/rules/no-restricted-syntax) 规则，你可以限制代码中特定语法的使用。例如，你可以使用以下配置禁止函数体不是块语句的 `if` 语句：

```json
{
	"rules": {
		"no-restricted-syntax": [
			"error",
			"IfStatement > :not(BlockStatement).consequent"
		]
	}
}
```

……或者等价地，你可以使用这个配置：

```json
{
	"rules": {
		"no-restricted-syntax": [
			"error",
			"IfStatement[consequent.type!='BlockStatement']"
		]
	}
}
```

再举一个例子，你可以禁止调用 `require()`：

```json
{
	"rules": {
		"no-restricted-syntax": [
			"error",
			"CallExpression[callee.name='require']"
		]
	}
}
```

或者你可以强制要求 `setTimeout` 调用总是带两个参数：

```json
{
	"rules": {
		"no-restricted-syntax": [
			"error",
			"CallExpression[callee.name='setTimeout'][arguments.length!=2]"
		]
	}
}
```

在 `no-restricted-syntax` 规则中使用选择器，可以让你对代码库中有问题的模式进行大量控制，而无需编写自定义规则来检测每种模式。

##### 使用正则表达式（Using regular expressions）

你可以在属性选择器中使用正则表达式。例如，以下选择器匹配所有 `name` 值以 "foo" 开头的 `Identifier` 节点：

```text
Identifier[name=/^foo/]
```

你也可以在正则表达式中使用正斜杠。例如，以下配置禁止从 `some/path` 导入：

```json
{
	"rules": {
		"no-restricted-syntax": [
			"error",
			"ImportDeclaration[source.value=/^some\\/path$/]"
		]
	}
}
```

请注意，正则表达式中的 `/` 字符需要转义（`\/`），以免被解释为正则表达式的结束。此外，由于选择器位于 JSON 字符串内，反斜杠字符本身也需要转义（`\\`）。


---

### 作用域管理器（ScopeManager）

> 原文链接：https://eslint.org/docs/latest/extend/scope-manager-interface

本文档基于 [eslint-scope](https://github.com/eslint/js/tree/main/packages/eslint-scope)（[escope](https://github.com/estools/escope) 的一个 fork）的实现编写，并废弃了一些 ESLint 未使用的成员。

---

#### ScopeManager 接口（ScopeManager interface）

`ScopeManager` 对象包含所有变量作用域。

##### 字段（Fields）

###### scopes

- **类型：** `Scope[]`
- **描述：** 所有作用域。

###### globalScope

- **类型：** `Scope`
- **描述：** 根作用域。

##### 方法（Methods）

###### acquire(node, inner = false)

- **参数：**
    - `node`（`ASTNode`）…… 要获取其作用域的 AST 节点。
    - `inner`（`boolean`）…… 如果节点有多个作用域，通常返回最外层的作用域。如果 `inner` 为 `true`，则返回最内层的作用域。默认为 `false`。
- **返回类型：** `Scope | null`
- **描述：** 获取给定 AST 节点的作用域。所获作用域的 `block` 属性即为该节点。此方法永远不会返回 `function-expression-name` 作用域。如果该节点没有自己的作用域，则返回 `null`。

###### addGlobals(names)

- **参数：**
    - `names`（`string[]`）…… 要添加到全局作用域的变量名。
- **返回类型：** `undefined`
- **描述：** 向全局作用域添加变量并解析对它们的引用。此方法由 ESLint 核心使用，绝不应在规则中使用。

###### getDeclaredVariables(node)

- **参数：**
    - `node`（`ASTNode`）…… 要获取其变量的 AST 节点。
- **返回类型：** `Variable[]`
- **描述：** 获取给定 AST 节点定义的变量。所获变量的 `def[].node`/`def[].parent` 属性即为该节点。如果该节点未定义任何变量，则返回空数组。

##### 已废弃成员（Deprecated members）

这些成员已定义但在 ESLint 中未使用。

###### isModule()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此程序是模块，则为 `true`。

###### isImpliedStrict()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此程序隐式处于严格模式，则为 `true`。即 `options.impliedStrict === true`。

###### isStrictModeSupported()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此程序支持严格模式，则为 `true`。即 `options.ecmaVersion >= 5`。

###### acquireAll(node)

- **参数：**
    - `node`（`ASTNode`）…… 要获取其作用域的 AST 节点。
- **返回类型：** `Scope[] | null`
- **描述：** 获取给定 AST 节点的作用域。所获作用域的 `block` 属性即为该节点。如果该节点没有自己的作用域，则返回 `null`。

---

#### Scope 接口（Scope interface）

`Scope` 对象包含作用域中的所有变量和引用。

##### 字段（Fields）

###### type

- **类型：** `string`
- **描述：** 此作用域的类型。为 `"block"`、`"catch"`、`"class"`、`"class-field-initializer"`、`"class-static-block"`、`"for"`、`"function"`、`"function-expression-name"`、`"global"`、`"module"`、`"switch"`、`"with"` 之一。

###### isStrict

- **类型：** `boolean`
- **描述：** 如果此作用域处于严格模式，则为 `true`。

###### upper

- **类型：** `Scope | null`
- **描述：** 父作用域。如果这是全局作用域，则此属性为 `null`。

###### childScopes

- **类型：** `Scope[]`
- **描述：** 子作用域数组。不包括孙级作用域。

###### variableScope

- **类型：** `Scope`
- **描述：** `type` 为 `"class-field-initializer"`、`"class-static-block"`、`"function"`、`"global"` 或 `"module"` 之一的最近祖先。对于上述作用域本身，这是自引用。

> 这表示最内层的封闭函数或顶层作用域。类字段初始化器和类静态块是隐式函数。历史上，这是承载由 `var` 声明定义的变量的作用域，因此得名 `variableScope`。

###### block

- **类型：** `ASTNode`
- **描述：** 创建此作用域的 AST 节点。

###### variables

- **类型：** `Variable[]`
- **描述：** 在此作用域上定义的所有变量的数组。不包括在子作用域中定义的变量。

###### set

- **类型：** `Map<string, Variable>`
- **描述：** 从变量名到变量对象的映射。

###### references

- **类型：** `Reference[]`
- **描述：** 此作用域上所有引用的数组。不包括子作用域中的引用。

###### through

- **类型：** `Reference[]`
- **描述：** 在此作用域中无法解析的引用数组。

###### functionExpressionScope

- **类型：** `boolean`
- **描述：** 如果此作用域是 `"function-expression-name"` 作用域，则为 `true`。

###### implicit

此字段仅存在于根 `Scope` 对象（全局作用域）上。它提供关于隐式全局变量的信息。隐式全局变量是指既非内置也非显式声明，而是在非严格代码中通过给未声明变量赋值而隐式创建的变量。这些变量的 `Variable` 对象不会出现在根 `Scope` 对象的 `variables` 和 `set` 字段中。

`implicit` 字段的值是一个有两个属性的对象。

**variables**

- **类型：** `Variable[]`
- **描述：** 所有隐式全局变量的数组。

**set**

- **类型：** `Map<string, Variable>`
- **描述：** 隐式全局变量从变量名到变量对象的映射。

::: tip
在表示隐式全局变量的 `Variable` 对象中，`references` 始终为空数组。你可以在根 `Scope` 对象（全局作用域）的 `through` 字段中，与其他未解析的引用一起找到对这些变量的引用。
:::

##### 已废弃成员（Deprecated members）

这些成员已定义但在 ESLint 中未使用。

###### taints

- **类型：** `Map<string, boolean>`
- **描述：** 从变量名到 `tainted` 标志的映射。

###### dynamic

- **类型：** `boolean`
- **描述：** 如果此作用域是动态的，则为 `true`。即此作用域的类型是 `"global"` 或 `"with"`。

###### directCallToEvalScope

- **类型：** `boolean`
- **描述：** 如果此作用域包含 `eval()` 调用，则为 `true`。

###### thisFound

- **类型：** `boolean`
- **描述：** 如果此作用域包含 `this`，则为 `true`。

###### resolve(node)

- **参数：**
    - `node`（`ASTNode`）…… 要获取其引用对象的 AST 节点。节点类型必须是 `"Identifier"`。
- **返回类型：** `Reference | null`
- **描述：** 返回 `this.references.find(r => r.identifier === node)`。

###### isStatic()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 返回 `!this.dynamic`。

###### isArgumentsMaterialized()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果这是一个使用了 `arguments` 变量的 `"function"` 作用域，则为 `true`。

###### isThisMaterialized()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 返回 `this.thisFound`。

###### isUsedName(name)

- **参数：**
    - `name`（`string`）…… 要检查的名称。
- **返回类型：** `boolean`
- **描述：** 如果给定名称用于变量名或引用名，则为 `true`。

---

#### Variable 接口（Variable interface）

`Variable` 对象是变量的信息。

##### 字段（Fields）

###### name

- **类型：** `string`
- **描述：** 此变量的名称。

###### scope

- **类型：** `Scope`
- **描述：** 定义此变量的作用域。

###### identifiers

- **类型：** `ASTNode[]`
- **描述：** 定义此变量的 `Identifier` 节点数组。如果此变量被重复声明，此数组包含两个或更多节点。

###### references

- **类型：** `Reference[]`
- **描述：** 此变量的引用数组。

###### defs

- **类型：** `Definition[]`
- **描述：** 此变量的定义数组。

##### 已废弃成员（Deprecated members）

这些成员已定义但在 ESLint 中未使用。

###### tainted

- **类型：** `boolean`
- **描述：** `tainted` 标志。（始终为 `false`）

###### stack

- **类型：** `boolean`
- **描述：** `stack` 标志。（不确定其含义。）

---

#### Reference 接口（Reference interface）

`Reference` 对象是引用的信息。

##### 字段（Fields）

###### identifier

- **类型：** `ASTNode`
- **描述：** 此引用的 `Identifier` 或 `JSXIdentifier` 节点。

###### from

- **类型：** `Scope`
- **描述：** 此引用所在的 `Scope` 对象。

###### resolved

- **类型：** `Variable | null`
- **描述：** 此引用指向的 `Variable` 对象。如果未定义这样的变量，则为 `null`。

###### writeExpr

- **类型：** `ASTNode | null`
- **描述：** 右侧的 ASTNode 对象。

###### init

- **类型：** `boolean`
- **描述：** 如果此写入引用是变量初始化器或默认值，则为 `true`。

##### 方法（Methods）

###### isWrite()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此引用是写入，则为 `true`。

###### isRead()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此引用是读取，则为 `true`。

###### isWriteOnly()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此引用是写入但不读取，则为 `true`。

###### isReadOnly()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此引用是读取但不写入，则为 `true`。

###### isReadWrite()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此引用既读取又写入，则为 `true`。

##### 已废弃成员（Deprecated members）

这些成员已定义但在 ESLint 中未使用。

###### tainted

- **类型：** `boolean`
- **描述：** `tainted` 标志。（始终为 `false`）

###### flag

- **类型：** `number`
- **描述：** `1` 表示读取，`2` 表示写入，`3` 表示读取/写入。

###### partial

- **类型：** `boolean`
- **描述：** `partial` 标志。

###### isStatic()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此引用是静态解析的，则为 `true`。

---

#### Definition 接口（Definition interface）

`Definition` 对象是变量定义的信息。

##### 字段（Fields）

###### type

- **类型：** `string`
- **描述：** 此定义的类型。为 `"CatchClause"`、`"ClassName"`、`"FunctionName"`、`"ImplicitGlobalVariable"`、`"ImportBinding"`、`"Parameter"` 和 `"Variable"` 之一。

###### name

- **类型：** `ASTNode`
- **描述：** 此定义的 `Identifier` 节点。

###### node

- **类型：** `ASTNode`
- **描述：** 名称的封闭节点。

| type                       | node                                                                       |
| :------------------------- | :------------------------------------------------------------------------- |
| `"CatchClause"`            | `CatchClause`                                                              |
| `"ClassName"`              | `ClassDeclaration` 或 `ClassExpression`                                    |
| `"FunctionName"`           | `FunctionDeclaration` 或 `FunctionExpression`                              |
| `"ImplicitGlobalVariable"` | `AssignmentExpression` 或 `ForInStatement` 或 `ForOfStatement`             |
| `"ImportBinding"`          | `ImportSpecifier`、`ImportDefaultSpecifier` 或 `ImportNamespaceSpecifier`  |
| `"Parameter"`              | `FunctionDeclaration`、`FunctionExpression` 或 `ArrowFunctionExpression`   |
| `"Variable"`               | `VariableDeclarator`                                                       |

###### parent

- **类型：** `ASTNode | undefined | null`
- **描述：** 名称的封闭语句节点。

| type                       | parent                |
| :------------------------- | :-------------------- |
| `"CatchClause"`            | `null`                |
| `"ClassName"`              | `null`                |
| `"FunctionName"`           | `null`                |
| `"ImplicitGlobalVariable"` | `null`                |
| `"ImportBinding"`          | `ImportDeclaration`   |
| `"Parameter"`              | `null`                |
| `"Variable"`               | `VariableDeclaration` |

##### 已废弃成员（Deprecated members）

这些成员已定义但在 ESLint 中未使用。

###### index

- **类型：** `number | undefined | null`
- **描述：** 在声明语句中的索引。

###### kind

- **类型：** `string | undefined | null`
- **描述：** 声明语句的种类。


---

### 规则废弃（Rule Deprecation）

> 原文链接：https://eslint.org/docs/latest/extend/rule-deprecation

规则废弃元数据描述一条规则是否已废弃，以及在有替代规则时如何替代它。
旧格式使用两个顶层的[规则 meta](https://eslint.org/docs/latest/extend/custom-rules#rule-structure) 属性 `deprecated`（仅为布尔值）和 `replacedBy`。
在新格式中，`deprecated` 是一个布尔值或 `DeprecatedInfo` 类型的对象，而 `replacedBy` 应定义在 `deprecated` 内部，而不是顶层。

#### ◆ DeprecatedInfo 类型（◆ DeprecatedInfo type）

此类型表示规则废弃的一般信息。
每个属性都是可选的。

- `message`（`string`）<br>
  可展示给用户的一般性消息。可以包含此规则被废弃的原因或如何替代该规则。
- `url`（`string`）<br>
  包含关于此规则废弃更多信息的 URL。
- `replacedBy`（`ReplacedByInfo[]`）<br>
  关于该规则可用替代方案的信息。
  可以是空数组，以明确表示没有替代方案。
- `deprecatedSince`（`string`）<br>
  废弃该规则的版本的 [Semver](https://semver.org/)。应为表示特定版本的完整 semver 字符串，不带前导 `"v"`，例如 `"3.12.0"`。
- `availableUntil`（`string | null`）<br>
  可能移除该规则的版本的 [Semver](https://semver.org/)，例如下一个主版本。应为表示特定版本的完整 semver 字符串，不带前导 `"v"`，例如 `"4.0.0"`。
  特殊值 `null` 表示该规则不再变更，但会继续保留可用。

#### ◆ ReplacedByInfo 类型（◆ ReplacedByInfo type）

此类型描述一条规则的单个可能的替代方案。
每个属性都是可选的。

- `message`（`string`）<br>
  关于此规则替代方案的一般性消息。
- `url`（`string`）<br>
  包含关于此规则替代方案更多信息的 URL。
- `plugin`（`ExternalSpecifier`）<br>
  指定哪个插件拥有替代规则。
  名称应为包名；如果替代规则是 ESLint 核心规则，则应为 "eslint"。
  如果替代规则在同一个插件中，则应省略此属性。
- `rule`（`ExternalSpecifier`）<br>
  指定替代规则。
  如果插件只包含一条规则，或与该规则同名，则可以省略。

##### ◆ ExternalSpecifier 类型（◆ ExternalSpecifier type）

此类型表示一个外部资源。
每个属性都是可选的。

- `name`（`string`）<br>
  对于 `plugin` 是包名，对于 `rule` 是规则 ID。
- `url`（`string`）<br>
  指向插件/规则文档的 URL。


---

### 自定义处理器（Custom Processors）

> 原文链接：https://eslint.org/docs/latest/extend/custom-processors

你也可以创建自定义处理器，告诉 ESLint 如何处理标准 JavaScript 以外的文件。例如，你可以编写一个自定义处理器，从 Markdown 文件中提取并处理 JavaScript（[@eslint/markdown](https://www.npmjs.com/package/@eslint/markdown) 就包含了用于此目的的自定义处理器）。

::: tip
本页介绍如何创建用于 flat config 格式的自定义处理器。
:::

#### 自定义处理器规范（Custom Processor Specification）

要创建自定义处理器，从你的模块导出的对象必须符合以下接口：

```js
const plugin = {
	meta: {
		name: "eslint-plugin-example",
		version: "1.2.3",
	},
	processors: {
		"processor-name": {
			meta: {
				name: "eslint-processor-name",
				version: "1.2.3",
			},
			// 接收文件文本和文件名
			preprocess(text, filename) {
				// 在这里，你可以剔除任何非 JS 内容
				// 并拆分成多个待检查的字符串

				return [
					// 返回待检查代码块的数组
					{ text: code1, filename: "0.js" },
					{ text: code2, filename: "1.js" },
				];
			},

			// 接收 Message[][] 和文件名
			postprocess(messages, filename) {
				// `messages` 参数是 Message 对象的二维数组，
				// 其中每个顶层数组项包含与 preprocess() 方法返回数组中
				// 对应文本相关的 lint 消息数组

				// 你需要返回一个包含要保留消息的一维数组
				return [].concat(...messages);
			},

			supportsAutofix: true, // （可选，默认为 false）
		},
	},
};

// ESM 方式
export default plugin;

// 或者 CommonJS 方式
module.exports = plugin;
```

**`preprocess` 方法**接收文件内容和文件名作为参数，并返回一个待检查代码块的数组。这些代码块将被分别检查，但仍注册在该文件名名下。

代码块有两个属性：`text` 和 `filename`。`text` 属性是代码块的内容，`filename` 属性是代码块的名称。代码块的名称可以是任意值，但应包含文件扩展名，它告诉 ESLint 如何处理当前代码块。ESLint 会检查项目配置中匹配的 `files` 条目，以确定这些代码块是否应被检查。

由插件决定是返回非 JavaScript 文件的单个部分还是多个部分。例如，在处理 `.html` 文件时，你可能想通过合并所有脚本只返回数组中的一个项。然而对于 `.md` 文件，你可以返回多个项，因为每个 JavaScript 代码块可能是独立的。

**`postprocess` 方法**接收一个 lint 消息数组的二维数组和文件名。输入数组中的每一项对应 `preprocess` 方法返回的一个部分。`postprocess` 方法必须将所有错误的位置调整为与原始未处理代码中的位置相对应，并将它们聚合成一个扁平的数组返回。

报告的问题在每条 lint 消息中包含以下位置信息：

```typescript
type LintMessage = {
	/// 消息发生的 1 起始行号。
	line?: number;

	/// 消息发生的 1 起始列号。
	column?: number;

	/// 结束位置的 1 起始行号。
	endLine?: number;

	/// 结束位置的 1 起始列号。
	endColumn?: number;

	/// 如果为 `true`，这是一个致命错误。
	fatal?: boolean;

	/// 自动修复的信息。
	fix: Fix;

	/// 错误消息。
	message: string;

	/// 生成消息的规则 ID，如果不适用则为 `null`。
	ruleId: string | null;

	/// 消息的严重级别。
	severity: 0 | 1 | 2;

	/// 建议的信息。
	suggestions?: Suggestion[];
};

type Fix = {
	range: [number, number];
	text: string;
};

type Suggestion = {
	desc?: string;
	messageId?: string;
	fix: Fix;
};
```

默认情况下，使用自定义处理器时 ESLint 不执行自动修复，即使在命令行上启用了 `--fix` 标志也是如此。要让 ESLint 在使用你的处理器时自动修复代码，你应采取以下额外步骤：

1. 更新 `postprocess` 方法，额外转换报告问题的 `fix` 属性。所有可自动修复的问题都有一个 `fix` 属性，它是具有以下结构的对象：

    ```typescript
    {
        range: [number, number],
        text: string
    }
    ```

    `range` 属性包含代码中的两个索引，指向将被替换的连续文本段的起始和结束位置。`text` 属性是将替换给定范围的文本。

    在初始的问题列表中，`fix` 属性指向处理后 JavaScript 中的一个修复。`postprocess` 方法应转换该对象，使其指向原始未处理文件中的一个修复。

2. 给处理器添加 `supportsAutofix: true` 属性。

你可以在单个插件中同时拥有规则和自定义处理器。你也可以在一个插件中拥有多个处理器。要支持多个扩展名，将每个扩展名添加到 `processors` 元素中，并指向同一个对象。

#### `meta` 对象如何使用（How `meta` Objects are Used）

`meta` 对象帮助 ESLint 缓存使用处理器的配置，并提供更友好的调试消息。

##### 插件 `meta` 对象（Plugin `meta` Object）

[插件 `meta` 对象](https://eslint.org/docs/latest/extend/plugins#meta-data-in-plugins)提供关于插件本身的信息。当使用 `plugin-name/processor-name` 字符串格式指定处理器时，ESLint 会自动使用插件 `meta` 为处理器生成名称。这是处理器最常见的情况。

示例：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";
import example from "eslint-plugin-example";

export default defineConfig([
	{
		files: ["**/*.txt"], // 将处理器应用于文本文件
		plugins: {
			example,
		},
		processor: "example/processor-name",
	},
	// ...其他配置
]);
```

在此示例中，处理器名称是 `"example/processor-name"`，这就是将用于序列化配置的值。

##### 处理器 `meta` 对象（Processor `meta` Object）

每个处理器也可以指定自己的 `meta` 对象。当处理器对象被直接传递给配置中的 `processor` 时，会使用此信息。在这种情况下，ESLint 不知道处理器属于哪个插件。`meta.name` 属性应与处理器名称一致，`meta.version` 属性应与你的处理器的 npm 包版本一致。最简单的做法是从你的 `package.json` 中读取这些信息。

示例：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";
import example from "eslint-plugin-example";

export default defineConfig([
	{
		files: ["**/*.txt"],
		processor: example.processors["processor-name"],
	},
	// ...其他配置
]);
```

在此示例中，直接指定 `example.processors["processor-name"]` 会使用处理器自己的 `meta` 对象——当处理器不通过插件名称引用时，必须定义此对象以确保正确处理。

##### 为什么两个 meta 对象都需要（Why Both Meta Objects are Needed）

建议插件和每个处理器都提供各自的 meta 对象。这可以确保依赖 meta 对象的功能（如 `--print-config` 和 `--cache`）无论处理器在配置中以何种方式指定都能正常工作。

#### 在配置文件中指定处理器（Specify Processor in Config Files）

要在配置文件中使用插件中的处理器，导入该插件并将其包含在 `plugins` 键中并指定命名空间。然后，使用该命名空间在 `processor` 配置中引用处理器，如下所示：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";
import example from "eslint-plugin-example";

export default defineConfig([
	{
		files: ["**/*.txt"],
		plugins: {
			example,
		},
		processor: "example/processor-name",
	},
]);
```

更多细节请参见插件配置文档中的[指定处理器（Specify a Processor）](https://eslint.org/docs/latest/use/configure/plugins#specifying-a-processor)。


---

### 语言（Languages）

> 原文链接：https://eslint.org/docs/latest/extend/languages

从 ESLint v9.7.0 开始，你可以通过插件为 ESLint 扩展额外的语言。虽然 ESLint 最初只是一个严格针对 JavaScript 的 linter，但 ESLint 核心是通用的，可以用于检查任何编程语言。每种语言被定义为一个对象，包含检查文件所需的全部解析、求值和遍历功能。这些语言随后通过插件分发，供用户在配置中使用。

#### 语言要求（Language Requirements）

要创建一种语言，你需要：

1. **一个解析器（parser）。** 解析器是将纯文本转换为数据结构的部件。ESLint 对数据结构的格式没有特定要求，因此你可以使用任何已有的解析器，或自己编写。
1. **一个 `SourceCode` 对象。** ESLint 通过 `SourceCode` 对象来使用 AST。每个 `SourceCode` 有一些必须实现的方法，你也可以添加更多想暴露给规则的方法或属性。
1. **一个 `Language` 对象。** `Language` 对象包含语言本身的信息，以及解析和创建 `SourceCode` 对象的方法。

##### 语言的解析器要求（Parser Requirements for Languages）

首先，确保你有一个可以从 JavaScript 调用的解析器。解析器必须返回一个表示被解析代码的数据结构。大多数解析器返回[抽象语法树](https://en.wikipedia.org/wiki/Abstract_syntax_tree)（AST）来表示代码，但也可以返回[具体语法树](https://en.wikipedia.org/wiki/Parse_tree)（CST）。返回 AST 还是 CST 对 ESLint 来说无所谓，重要的是有一个可遍历的数据结构。

虽然 AST 或 CST 没有必须遵循的特定结构，但当树中的每个节点包含以下信息时，会更容易与 ESLint 集成：

1. **类型（Type）** - 每个节点上需要一个表示节点类型的属性。例如，在 JavaScript 中，`type` 属性为每个节点保存此信息。ESLint 规则使用节点类型来定义访问器（visitor）方法，因此每个节点都可以通过字符串标识很重要。属性名无所谓（下文进一步讨论），只要存在即可。大多数解析器通常将此属性命名为 `type` 或 `kind`。
1. **位置（Location）** - 每个节点上需要一个表示节点在原始源代码中位置的属性。位置必须包含：
    - 节点开始的行
    - 节点开始的列
    - 节点结束的行
    - 节点结束的列

    与节点类型一样，属性名无所谓。两个常见的属性名是 `loc`（如 [ESTree](https://github.com/estree/estree/blob/3851d4a6eae5e5473371893959b88b62007469e8/es5.md#node-objects)）和 `position`（如 [Unist](https://github.com/syntax-tree/unist?tab=readme-ov-file#node)）。ESLint 使用此信息来报告错误和规则违规。

1. **范围（Range）** - 每个节点上需要一个表示节点源码在源代码中位置的属性。范围指示第一个字符所在的索引和最后一个字符之后的索引，使得调用 `code.slice(start, end)` 返回该节点表示的文本。同样，不要求特定的属性名，此信息甚至可以与位置信息合并。ESTree 使用 `range` 属性，而 Unist 将此信息与位置信息一起包含在 `position` 上。ESLint 使用此信息来应用自动修复。

##### `SourceCode` 对象（The `SourceCode` Object）

ESLint 将源代码信息保存在 `SourceCode` 对象中。这个对象是 ESLint 内部和为处理代码而编写的规则（通过 `context.sourceCode`）共同使用的 API。`SourceCode` 对象必须实现 [`@eslint/core`](https://npmjs.com/package/@eslint/core) 包中定义的 `TextSourceCode` 接口。

一个基本的 `SourceCode` 对象必须实现以下内容：

- `ast` - 包含源代码的 AST 或 CST 的属性。
- `text` - 源代码的文本。
- `getLoc(nodeOrToken)` - 返回给定节点、token 或注释的位置的方法。返回值必须与 ESTree 使用的 `loc` 结构匹配。
- `getRange(nodeOrToken)` - 返回给定节点、token 或注释的范围的方法。必须返回一个数组，第一项是起始索引，第二项是结束索引。
- `traverse()` - 返回用于遍历 AST 或 CST 的可迭代对象的方法。迭代器必须返回实现 `@eslint/core` 中 `VisitTraversalStep` 或 `CallTraversalStep` 的对象。

以下可选成员允许你自定义 ESLint 与该对象的交互方式：

- `visitorKeys` - 特定于此 `SourceCode` 对象的访问器键。通常不需要，因为大多数时候使用 `Language#visitorKeys`。
- `applyLanguageOptions(languageOptions)` - 如果你有需要在解析后应用的特定语言选项，可以在此方法中应用。
- `getDisableDirectives()` - 返回代码中的任何禁用指令。ESLint 用它应用禁用指令并跟踪未使用的指令。
- `getInlineConfigNodes()` - 返回任何内联配置节点。当启用 `noInlineConfig` 时，ESLint 用它报告错误。
- `applyInlineConfig()` - 向 ESLint 返回内联配置元素。ESLint 用它改变被检查文件的配置。
- `finalize()` - 此方法在 lint 开始之前调用，是你修改 `SourceCode` 的最后机会。如果你定义了 `applyLanguageOptions()` 或 `applyInlineConfig()`，那么在 `SourceCode` 对象就绪之前，你可能还有额外的更改要应用。

此外，以下成员在 `SourceCode` 对象上很常见，建议实现：

- `lines` - 源代码的各行，字符串数组。
- `getParent(node)` - 返回给定节点的父节点，如果节点是根节点则返回 `undefined`。
- `getAncestors(node)` - 返回节点的祖先数组，第一项是树的根，后续各项是从根通往 `node` 的后代节点。
- `getText(node, beforeCount, afterCount)` - 返回表示给定节点、token 或注释的字符串，可选地包含其范围前后指定数量的字符。如果 `node` 被省略或为 `undefined`，则返回整个源文本。

基本 `SourceCode` 类的示例请参见 [`JSONSourceCode`](https://github.com/eslint/json/blob/main/src/languages/json-source-code.js)。

::: tip
[`@eslint/plugin-kit`](https://npmjs.com/package/@eslint/plugin-kit) 包包含多个旨在让创建 `SourceCode` 对象更容易的类。特别是 `TextSourceCodeBase` 类，它实现了 `TextSourceCode` 接口，并提供了 `SourceCode` 对象中常见的一些基础功能。
:::

##### `Language` 对象（The `Language` Object）

`Language` 对象包含关于编程语言的所有信息，以及与用该语言编写的代码交互的方法。ESLint 使用此对象来确定如何处理特定文件。`Language` 对象必须实现 [`@eslint/core`](https://npmjs.com/package/@eslint/core) 包中定义的 `Language` 接口。

一个基本的 `Language` 对象必须实现以下内容：

- `fileType` - 应为 `"text"`（未来我们还将支持 `"binary"`）
- `lineStart` - 0 或 1，指示 AST 如何表示文件的第一行。ESLint 用它正确显示错误位置。
- `columnStart` - 0 或 1，指示 AST 如何表示每行的第一列。ESLint 用它正确显示错误位置。
- `nodeTypeKey` - 指示节点类型的属性名（通常是 `"type"` 或 `"kind"`）。
- `validateLanguageOptions(languageOptions)` - 校验该语言的语言选项。当某个预期的语言选项类型或值不正确时，此方法应抛出校验错误。非预期的语言选项应被静默忽略，不应抛出错误。即使语言没有指定任何选项，此方法也是必需的。
- `parse(file, context)` - 将给定文件解析为 AST 或 CST，还可以包含供规则使用的额外值。由 ESLint 内部调用。
- `createSourceCode(file, parseResult, context)` - 创建 `SourceCode` 对象。由 ESLint 在 `parse()` 之后内部调用，第二个参数就是 `parse()` 的确切返回值。

以下可选成员允许你自定义 ESLint 与该对象的交互方式：

- `visitorKeys` - 特定于该 AST 或 CST 的访问器键。用于优化 ESLint 内部对 AST 或 CST 的遍历。虽然不是必需的，但强烈建议提供，尤其是对于与 ESTree 格式差异较大的 AST 或 CST 格式。
- `defaultLanguageOptions` - 使用该语言时的默认 `languageOptions`。在计算被检查文件的配置时，用户指定的 `languageOptions` 会与此对象合并。
- `matchesSelectorClass(className, node, ancestry)` - 允许你指定匹配多个节点的选择器类，例如 `:expression`。每当 [esquery](https://github.com/estools/esquery) 选择器包含 `:` 后跟标识符时，就会调用此方法。
- `normalizeLanguageOptions(languageOptions)` - 接收已校验的语言选项对象并规范化其值。这有助于在语言选项属性变化时保持向后兼容，也可以通过 `toJSON()` 方法添加自定义序列化。

基本 `Language` 类的示例请参见 [`JSONLanguage`](https://github.com/eslint/json/blob/main/src/languages/json-language.js)。

#### 在插件中发布语言（Publish a Language in a Plugin）

语言的发布方式与处理器和规则类似，都在插件中定义。在插件中定义 `languages` 键，它是一个对象，键是语言名称，值是语言对象。下面是一个示例：

```js
import { myLanguage } from "../languages/my.js";

const plugin = {
	// name 和 version 的首选位置
	meta: {
		name: "eslint-plugin-example",
		version: "1.2.3",
	},
	languages: {
		my: myLanguage,
	},
	rules: {
		// 在这里添加规则
	},
};

// ESM 方式
export default plugin;

// 或者 CommonJS 方式
module.exports = plugin;
```

要在配置文件中使用插件中的语言，导入该插件并将其包含在 `plugins` 键中并指定命名空间。然后，使用该命名空间在 `language` 配置中引用语言，如下所示：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";
import example from "eslint-plugin-example";

export default defineConfig([
	{
		files: ["**/*.my"],
		plugins: {
			example,
		},
		language: "example/my",
	},
]);
```

更多细节请参见插件配置文档中的[指定语言（Specify a Language）](https://eslint.org/docs/latest/use/configure/plugins#specifying-a-language)。


---

### 插件迁移到 Flat Config（Plugin Migration to Flat Config）

> 原文链接：https://eslint.org/docs/latest/extend/plugin-migration-flat-config

从 ESLint v9.0.0 开始，默认的配置系统将是新的 flat config 系统。为了让你的插件能够与 flat config 文件配合使用，你需要对现有插件做一些改动。

#### 推荐的插件结构（Recommended Plugin Structure）

为了让你的插件在 flat config 系统中更易于使用，建议你将现有的插件入口改为如下结构：

```js
const plugin = {
	meta: {},
	configs: {},
	rules: {},
	processors: {},
};

// ESM 方式
export default plugin;

// 或者 CommonJS 方式
module.exports = plugin;
```

在进行本页讨论的其他更改时，这种结构能提供最大的灵活性。

#### 添加插件 Meta 信息（Adding Plugin Meta Information）

使用旧的 eslintrc 配置系统时，ESLint 可以从包名中获取插件信息，但使用 flat config 时，ESLint 无法再访问插件包的名称。为了补上这些缺失的信息，你应该添加一个至少包含 `name` 键（理想情况下还有 `version` 键）的 `meta` 键，例如：

```js
const plugin = {
	meta: {
		name: "eslint-plugin-example",
		version: "1.0.0",
	},
	configs: {},
	rules: {},
	processors: {},
};

// ESM 方式
export default plugin;

// 或者 CommonJS 方式
module.exports = plugin;
```

如果你的插件是作为 npm 包发布的，那么 `name` 和 `version` 应与你的 `package.json` 文件中的一致；否则，你可以随意指定任何值。

没有这些 meta 信息，你的插件将无法与 `--cache` 和 `--print-config` 命令行选项一起使用。

#### 为 Flat Config 迁移规则（Migrating Rules for Flat Config）

插件中的 `rules` 键无需任何更改。一切都与旧的 eslintrc 配置系统相同。

#### 为 Flat Config 迁移处理器（Migrating Processors for Flat Config）

每个处理器应指定一个 `meta` 对象。更多信息请参见[完整文档](https://eslint.org/docs/latest/extend/custom-processors)。

只要你没有使用以文件扩展名命名的处理器，插件中的 `processors` 键就无需其他更改。如果你有任何以文件扩展名命名的处理器，必须将其名称更新为有效的标识符（数字和字母）。以文件扩展名命名的处理器在旧配置系统中会被自动应用，但在使用 flat config 时不会自动应用。下面是一个以文件扩展名命名的处理器的示例：

```js
const plugin = {
	configs: {},
	rules: {},
	processors: {
		// 不再支持
		".md": {
			preprocess() {},
			postprocess() {},
		},
	},
};

// ESM 方式
export default plugin;

// 或者 CommonJS 方式
module.exports = plugin;
```

名称 `".md"` 对处理器不再有效，因此必须替换为有效的标识符，例如 `markdown`：

```js
const plugin = {
	configs: {},
	rules: {},
	processors: {
		// 在新旧两种配置系统中都有效
		markdown: {
			preprocess() {},
			postprocess() {},
		},
	},
};

// ESM 方式
export default plugin;

// 或者 CommonJS 方式
module.exports = plugin;
```

要使用这个重命名后的处理器，你还需要在配置中手动指定它，例如：

```js
import { defineConfig } from "eslint/config";
import example from "eslint-plugin-example";

export default defineConfig([
	{
		files: ["**/*.md"],
		plugins: {
			example,
		},
		processor: "example/markdown",
	},
]);
```

如果你重命名了以文件扩展名命名的处理器，应该更新插件的文档，以告知你的用户。

#### 为 Flat Config 迁移配置（Migrating Configs for Flat Config）

如果你的插件导出的配置回引了插件自身，那么你需要将这些配置更新为 flat config 格式。作为迁移的一部分，你需要在 `plugins` 键中直接引用你的插件。例如，下面是名为 `eslint-plugin-example` 的插件以旧配置系统格式导出的配置：

```js
// 插件名称：eslint-plugin-example
module.exports = {
    configs: {

        // 由 example/recommended 引用的配置
        recommended: {
            plugins: ["example"],
            rules: {
                "example/rule1": "error",
                "example/rule2": "error"
            }
        }
    },
    rules: {
        rule1: {},
        rule2: {};
    }
};
```

要迁移到 flat config 格式，你需要按照推荐的插件结构，将配置移到 `plugin` 变量定义之后，如下所示：

```js
const plugin = {
	configs: {},
	rules: {},
	processors: {},
};

// 在这里赋值 configs，这样我们可以引用 `plugin`
Object.assign(plugin.configs, {
	recommended: {
		plugins: {
			example: plugin,
		},
		rules: {
			"example/rule1": "error",
			"example/rule2": "error",
		},
	},
});

// ESM 方式
export default plugin;

// 或者 CommonJS 方式
module.exports = plugin;
```

然后你的用户可以像这样使用这个导出的配置：

```js
import { defineConfig } from "eslint/config";
import example from "eslint-plugin-example";

export default defineConfig([
	// 使用 recommended 配置并提供你自己的覆盖
	{
		files: ["**/*.js", "**/*.cjs", "**/*.mjs"],
		plugins: {
			example,
		},
		extends: ["example/recommended"],
		rules: {
			"example/rule1": "warn",
		},
	},
]);
```

如果你的配置扩展了其他配置，你可以导出一个数组：

```js
const baseConfig = require("./base-config");

module.exports = {
	configs: {
		extendedConfig: [
			baseConfig,
			{
				rules: {
					"example/rule1": "error",
					"example/rule2": "error",
				},
			},
		],
	},
};
```

你应该更新文档，让插件用户知道如何引用导出的配置。

更多信息请参见[完整文档](https://eslint.org/docs/latest/extend/plugins#configs-in-plugins)。

#### 为 Flat Config 迁移环境（Migrating Environments for Flat Config）

flat config 不再支持环境（environments），因此我们建议将你的环境转换为导出的配置。例如，假设你导出了一个 `mocha` 环境，如下所示：

```js
// 插件名称：eslint-plugin-example
module.exports = {
    environments: {
        mocha: {
            globals: {
                it: true,
                xit: true,
                describe: true,
                xdescribe: true
            }
        }
    },
    rules: {
        rule1: {},
        rule2: {};
    }
};
```

要将此环境迁移为配置，你需要在 `plugin.configs` 对象中添加一个新键，其值是包含相同信息的 flat config 对象，如下所示：

```js
const plugin = {
	configs: {},
	rules: {},
	processors: {},
};

// 在这里赋值 configs，这样我们可以引用 `plugin`
Object.assign(plugin.configs, {
	mocha: {
		languageOptions: {
			globals: {
				it: "writeable",
				xit: "writeable",
				describe: "writeable",
				xdescribe: "writeable",
			},
		},
	},
});

// ESM 方式
export default plugin;

// 或者 CommonJS 方式
module.exports = plugin;
```

然后你的用户可以像这样使用这个导出的配置：

```js
import { defineConfig } from "eslint/config";
import example from "eslint-plugin-example";

export default defineConfig([
	{
		files: ["**/tests/*.js"],
		plugins: {
			example,
		},

		// 使用 mocha 全局变量
		extends: ["example/mocha"],

		// 并提供你自己的覆盖
		languageOptions: {
			globals: {
				it: "readonly",
			},
		},
	},
]);
```

你应该更新文档，让插件用户知道如何引用导出的配置。

#### 向后兼容（Backwards Compatibility）

如果你的插件需要同时适用于新旧两种配置系统，那么你需要：

1. **导出 CommonJS 入口。** 旧配置系统无法加载仅以 ESM 格式发布的插件。如果你的源代码是 ESM，那么你需要使用能够生成 CommonJS 版本的打包器，并在 `package.json` 文件中使用 [`exports`](https://nodejs.org/api/packages.html#package-entry-points) 键，以确保 Node.js 能找到 CommonJS 版本。
1. **保留 `environments` 键。** 如果你的插件导出自定义环境，你应该保持它们不变，同时按上文所述导出等价的 flat config。当 ESLint 以 flat config 模式运行时，`environments` 键会被忽略。
1. **同时导出 eslintrc 和 flat config。** `configs` 键只在配置被使用时才校验，因此你可以在 `configs` 键中同时提供两种格式的配置。我们建议为旧格式配置加上 `legacy-` 前缀。例如，如果你的主配置名为 `recommended` 且为 flat config 格式，那么你可以再有一个名为 `legacy-recommended` 的 eslintrc 格式配置。如果你不想更新配置名称，也可以在 configs 对象中创建一个以 `"flat/"` 为前缀的额外条目（例如 `"flat/recommended"`）。更多细节请参见[旧版配置的向后兼容](https://eslint.org/docs/latest/extend/plugins#backwards-compatibility-for-legacy-configs)。

#### 延伸阅读（Further Reading）

- [flat config 文件格式概述博客文章](https://eslint.org/blog/2022/08/new-config-system-part-2/)
- [新配置系统的 API 用法博客文章](https://eslint.org/blog/2022/08/new-config-system-part-3/)
- [新配置系统背景博客文章](https://eslint.org/blog/2022/08/new-config-system-part-1/)


---

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


---

## 自定义格式化器（Custom Formatters）

> 原文链接：https://eslint.org/docs/latest/extend/custom-formatters

自定义格式化器让你以最适合自己需求的格式展示 lint 结果，无论是特定的文件格式、某种展示风格，还是为特定工具优化的格式。

ESLint 也提供了你可以使用的[内置格式化器](https://eslint.org/docs/latest/use/formatters/)。

你可以直接在项目中包含自定义格式化器，或创建 npm 包单独分发。

### 创建自定义格式化器（Create a Custom Formatter）

每个格式化器是一个接收 `results` 对象和 `context` 作为参数并返回字符串的函数。例如，下面是内置 [JSON 格式化器](https://eslint.org/docs/latest/use/formatters/#json)的实现方式：

```js
//my-awesome-formatter.js
module.exports = function (results, context) {
	return JSON.stringify(results, null, 2);
};
```

格式化器也可以是 async 函数（自 ESLint v8.4.0 起），下面是一个简单示例：

```js
//my-awesome-formatter.js
module.exports = async function (results) {
	const formatted = await asyncTask();
	return formatted;
};
```

要使用此格式化器运行 ESLint，可以使用 [`-f`（或 `--format`）](https://eslint.org/docs/latest/use/command-line-interface#-f---format) 命令行标志。本地定义的自定义格式化器的路径必须以小数点（`.`）开头，例如 `./my-awesome-formatter.js` 或 `../formatters/my-awesome-formatter.js`。

```bash
eslint -f ./my-awesome-formatter.js src/
```

本节的其余部分包含如何使用自定义格式化器函数的参考信息。

#### `results` 参数（The `results` Argument）

传入格式化器的 `results` 对象是一个 [`LintResult`](https://eslint.org/docs/latest/integrate/nodejs-api#-lintresult-type) 对象的数组，包含各个文件的 lint 结果。下面是一个输出示例：

```js
[
	{
		filePath: "/path/to/a/file.js",
		messages: [
			{
				ruleId: "curly",
				severity: 2,
				message: "Expected { after 'if' condition.",
				line: 2,
				column: 1,
			},
			{
				ruleId: "no-process-exit",
				severity: 2,
				message: "Don't use process.exit(); throw an error instead.",
				line: 3,
				column: 1,
			},
		],
		errorCount: 2,
		warningCount: 0,
		fixableErrorCount: 0,
		fixableWarningCount: 0,
		source: "var err = doStuff();\nif (err) console.log('failed tests: ' + err);\nprocess.exit(1);\n",
	},
	{
		filePath: "/path/to/Gruntfile.js",
		messages: [],
		errorCount: 0,
		warningCount: 0,
		fixableErrorCount: 0,
		fixableWarningCount: 0,
	},
];
```

#### `context` 参数（The `context` Argument）

格式化器函数接收一个 `context` 对象作为第二个参数。该对象有以下属性：

- `color`（可选）：如果设置了 `--color`，此属性为 `true`。如果设置了 `--no-color`，则为 `false`。如果两个选项都未提供，则省略此属性。
- `cwd`：当前工作目录。此值来自 [ESLint](https://eslint.org/docs/latest/integrate/nodejs-api#-new-eslintoptions) 类的 `cwd` 构造函数选项。
- `maxWarningsExceeded`（可选）：如果设置了 `--max-warnings` 且警告数量超过了限制，此属性的值是一个包含两个属性的对象：
    - `maxWarnings`：`--max-warnings` 选项的值
    - `foundWarnings`：lint 警告的数量
- `rulesMeta`：规则的 `meta` 属性值。有关规则的更多信息，请参见[自定义规则（Custom Rules）](https://eslint.org/docs/latest/extend/custom-rules)页面。

例如，如果运行了 `no-extra-semi` 规则，该对象将如下所示：

```js
{
    cwd: "/path/to/cwd",
    maxWarningsExceeded: {
        maxWarnings: 5,
        foundWarnings: 6
    },
    rulesMeta: {
        "no-extra-semi": {
            type: "suggestion",
            docs: {
                description: "disallow unnecessary semicolons",
                recommended: true,
                url: "https://eslint.org/docs/rules/no-extra-semi"
            },
            fixable: "code",
            schema: [],
            messages: {
                unexpected: "Unnecessary semicolon."
            }
        }
    },
}
```

**注意：**如果 lint 是由已废弃的 `CLIEngine` 类执行的，`context` 参数可能是不同的值，因为这取决于 API 使用者。如果你想支持旧环境，请检查 `context` 参数是否是预期的值。

#### 向格式化器传递参数（Passing Arguments to Formatters）

虽然格式化器函数除了 results 对象和 context 之外不接收其他参数，但可以使用下面介绍的方法向自定义格式化器传递额外的数据。

##### 使用环境变量（Use Environment Variables）

自定义格式化器可以访问环境变量，因此可以基于环境变量数据改变其行为。

下面是一个使用 `FORMATTER_SKIP_WARNINGS` 环境变量来决定是否在结果中显示警告的示例：

```js
module.exports = function (results) {
	var skipWarnings = process.env.FORMATTER_SKIP_WARNINGS === "true";

	var results = results || [];
	var summary = results.reduce(
		function (seq, current) {
			current.messages.forEach(function (msg) {
				var logMessage = {
					filePath: current.filePath,
					ruleId: msg.ruleId,
					message: msg.message,
					line: msg.line,
					column: msg.column,
				};

				if (msg.severity === 1) {
					logMessage.type = "warning";
					seq.warnings.push(logMessage);
				}
				if (msg.severity === 2) {
					logMessage.type = "error";
					seq.errors.push(logMessage);
				}
			});
			return seq;
		},
		{
			errors: [],
			warnings: [],
		},
	);

	if (summary.errors.length > 0 || summary.warnings.length > 0) {
		var warnings = !skipWarnings ? summary.warnings : []; // 在这种情况下跳过警告

		var lines = summary.errors
			.concat(warnings)
			.map(function (msg) {
				return (
					"\n" +
					msg.type +
					" " +
					msg.ruleId +
					"\n  " +
					msg.filePath +
					":" +
					msg.line +
					":" +
					msg.column
				);
			})
			.join("\n");

		return lines + "\n";
	}
};
```

你会像这样带着设置好的环境变量和这个自定义格式化器运行 ESLint：

```bash
FORMATTER_SKIP_WARNINGS=true eslint -f ./my-awesome-formatter.js src/
```

输出将是：

```bash
error space-infix-ops
  src/configs/bundler.js:6:8

error semi
  src/configs/bundler.js:6:10
```

##### 复杂的参数传递（Complex Argument Passing）

如果你发现自定义格式化器模式无法为你想要的 ESLint 结果格式化方式提供足够的选项，最好的办法是使用 ESLint 内置的 [JSON 格式化器](https://eslint.org/docs/latest/use/formatters/#json)，并将输出通过管道传给第二个程序。例如：

```bash
eslint -f json src/ | your-program-that-reads-JSON --option
```

在此示例中，`your-program-that-reads-json` 程序可以接收 ESLint 结果的原始 JSON 并对其进行处理，然后输出自己的结果格式。你可以向该程序传递任意多的命令行参数来定制输出。

#### 为终端格式化（Formatting for Terminals）

像 [iTerm2](https://www.iterm2.com/) 或 [Guake](http://guake.org/) 这样的现代终端期望特定的结果格式，以便在点击文件名时自动打开它们。大多数终端为此支持以下格式：

```bash
file:line:column
```

### 打包自定义格式化器（Packaging a Custom Formatter）

自定义格式化器可以通过 npm 包分发。为此，创建一个名称格式为 `eslint-formatter-*` 的 npm 包，其中 `*` 是你的格式化器名称（例如 `eslint-formatter-awesome`）。然后项目应安装该包，并使用 [`-f`（或 `--format`）](https://eslint.org/docs/latest/use/command-line-interface#-f---format) 标志使用自定义格式化器，如下所示：

```bash
eslint -f awesome src/
```

因为当指定的格式化器不以小数点开头时，ESLint 知道去查找以 `eslint-formatter-` 开头的包，所以在使用打包的自定义格式化器时，你不需要输入 `eslint-formatter-`。

自定义格式化器的 `package.json` 提示：

- [`main`](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#main) 入口必须是实现你的自定义格式化器的 JavaScript 文件。
- 添加这些 [`keywords`](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#keywords) 以帮助用户找到你的格式化器：
    - `"eslint"`
    - `"eslint-formatter"`
    - `"eslintformatter"`

查看 [npm 上的所有自定义格式化器](https://www.npmjs.com/search?q=eslint-formatter)。

### 示例（Examples）

#### 摘要格式化器（Summary Formatter）

一个只报告错误和警告总数的格式化器如下所示：

```javascript
module.exports = function (results, context) {
	// 累计错误和警告
	var summary = results.reduce(
		function (seq, current) {
			seq.errors += current.errorCount;
			seq.warnings += current.warningCount;
			return seq;
		},
		{ errors: 0, warnings: 0 },
	);

	if (summary.errors > 0 || summary.warnings > 0) {
		return (
			"Errors: " +
			summary.errors +
			", Warnings: " +
			summary.warnings +
			"\n"
		);
	}

	return "";
};
```

使用上述摘要格式化器运行 `eslint`：

```bash
eslint -f ./my-awesome-formatter.js src/
```

将产生以下输出：

```bash
Errors: 2, Warnings: 4
```

#### 详细格式化器（Detailed Formatter）

一个更复杂的报告可能如下所示：

```javascript
module.exports = function (results, context) {
	var results = results || [];

	var summary = results.reduce(
		function (seq, current) {
			current.messages.forEach(function (msg) {
				var logMessage = {
					filePath: current.filePath,
					ruleId: msg.ruleId,
					ruleUrl: context.rulesMeta[msg.ruleId].docs.url,
					message: msg.message,
					line: msg.line,
					column: msg.column,
				};

				if (msg.severity === 1) {
					logMessage.type = "warning";
					seq.warnings.push(logMessage);
				}
				if (msg.severity === 2) {
					logMessage.type = "error";
					seq.errors.push(logMessage);
				}
			});
			return seq;
		},
		{
			errors: [],
			warnings: [],
		},
	);

	if (summary.errors.length > 0 || summary.warnings.length > 0) {
		var lines = summary.errors
			.concat(summary.warnings)
			.map(function (msg) {
				return (
					"\n" +
					msg.type +
					" " +
					msg.ruleId +
					(msg.ruleUrl ? " (" + msg.ruleUrl + ")" : "") +
					"\n  " +
					msg.filePath +
					":" +
					msg.line +
					":" +
					msg.column
				);
			})
			.join("\n");

		return lines + "\n";
	}
};
```

当你使用这个自定义格式化器运行 ESLint 时：

```bash
eslint -f ./my-awesome-formatter.js src/
```

输出为：

```bash
error space-infix-ops (https://eslint.org/docs/rules/space-infix-ops)
  src/configs/bundler.js:6:8
error semi (https://eslint.org/docs/rules/semi)
  src/configs/bundler.js:6:10
warning no-unused-vars (https://eslint.org/docs/rules/no-unused-vars)
  src/configs/bundler.js:5:6
warning no-unused-vars (https://eslint.org/docs/rules/no-unused-vars)
  src/configs/bundler.js:6:6
warning no-shadow (https://eslint.org/docs/rules/no-shadow)
  src/configs/bundler.js:65:32
warning no-unused-vars (https://eslint.org/docs/rules/no-unused-vars)
  src/configs/clean.js:3:6
```


---

## 自定义解析器（Custom Parsers）

> 原文链接：https://eslint.org/docs/latest/extend/custom-parsers

ESLint 自定义解析器让你扩展 ESLint，支持对代码中新的非标准 JavaScript 语言特性或自定义语法进行 lint。解析器负责接收你的代码，并将其转换为 ESLint 可以分析和检查的抽象语法树（AST）。

### 创建自定义解析器（Create a Custom Parser）

#### 自定义解析器中的方法（Methods in Custom Parsers）

自定义解析器是一个带有 `parse()` 或 `parseForESLint()` 方法的 JavaScript 对象。`parse` 方法只返回 AST，而 `parseForESLint()` 还会返回额外的值，让解析器能更进一步定制 ESLint 的行为。

这两个方法都应该是实例（自有）属性，接收源代码作为第一个参数，以及一个可选的配置对象作为第二个参数——该配置对象就是配置文件中提供的 [`parserOptions`](https://eslint.org/docs/latest/use/configure/language-options#specifying-parser-options)。

```javascript
// customParser.js

const espree = require("espree");

// 记录解析每个文件所花的时长。
function parse(code, options) {
	const label = `Parsing file "${options.filePath}"`;
	console.time(label);
	const ast = espree.parse(code, options);
	console.timeEnd(label);
	return ast; // 只返回 AST。
}

module.exports = { parse };
```

#### `parse` 返回对象（`parse` Return Object）

`parse` 方法只需返回 [AST](#ast-规范ast-specification) 对象。

#### `parseForESLint` 返回对象（`parseForESLint` Return Object）

`parseForESLint` 方法应返回一个对象，包含必需的 `ast` 属性，以及可选的 `services`、`scopeManager` 和 `visitorKeys` 属性。

- `ast` 应包含 [AST](#ast-规范ast-specification) 对象。
- `services` 可以包含任何依赖于解析器的服务（例如节点的类型检查器）。`services` 属性的值可通过 `context.sourceCode.parserServices` 供规则使用。默认为空对象。
- `scopeManager` 可以是一个 [ScopeManager](https://eslint.org/docs/latest/extend/scope-manager-interface) 对象。自定义解析器可以为实验性/增强语法使用定制的作用域分析。默认值是由 [eslint-scope](https://github.com/eslint/js/tree/main/packages/eslint-scope) 创建的 `ScopeManager` 对象。
    - ESLint v4.14.0 添加了对 `scopeManager` 的支持。支持 `scopeManager` 的 ESLint 版本会在 `parserOptions` 中提供 `eslintScopeManager: true` 属性，可用于特性检测。
    - 自 ESLint v10.0.0 起，`ScopeManager` 必须自动解析代码中声明的全局变量的引用，并提供一个实例方法 `addGlobals(names: string[])`，在全局作用域中创建给定名称的变量并解析对它们的引用。
- `visitorKeys` 可以是一个用于自定义 AST 遍历的对象。对象的键是 AST 节点的类型。每个值是应被遍历的属性名数组。默认值是 [`eslint-visitor-keys` 的 KEYS](https://github.com/eslint/js/tree/main/packages/eslint-visitor-keys#evkkeys)。
    - ESLint v4.14.0 添加了对 `visitorKeys` 的支持。支持 `visitorKeys` 的 ESLint 版本会在 `parserOptions` 中提供 `eslintVisitorKeys: true` 属性，可用于特性检测。

#### 自定义解析器中的元数据（Meta Data in Custom Parsers）

为了更方便地调试和更有效地缓存自定义解析器，建议在自定义解析器的根级 `meta` 对象中提供名称和版本，如下所示：

```js
// name 和 version 的首选位置
module.exports = {
	meta: {
		name: "eslint-parser-custom",
		version: "1.2.3",
	},
};
```

`meta.name` 属性应与你的自定义解析器的 npm 包名一致，`meta.version` 属性应与你的自定义解析器的 npm 包版本一致。最简单的做法是从你的 `package.json` 中读取这些信息。

### AST 规范（AST Specification）

自定义解析器应创建的 AST 基于 [ESTree](https://github.com/estree/estree)。该 AST 还需要一些关于源代码细节信息的额外属性。

#### 所有节点（All Nodes）

所有节点必须有 `range` 属性。

- `range`（`number[]`）是一个由两个数字组成的数组。两个数字都是从 0 开始的索引，表示在源代码字符数组中的位置。第一个是节点的起始位置，第二个是节点的结束位置。`code.slice(node.range[0], node.range[1])` 必须是该节点的文本。此范围不包括节点周围的空格/括号。
- `loc`（`SourceLocation`）不得为 `null`。[ESTree 将 `loc` 属性定义为可空](https://github.com/estree/estree/blob/25834f7247d44d3156030f8e8a2d07644d771fdb/es5.md#node-objects)，但 ESLint 要求此属性必须存在。`SourceLocation#source` 属性可以是 `undefined`，ESLint 不使用 `SourceLocation#source` 属性。

所有节点的 `parent` 属性必须是可重写的。在任何规则访问 AST 之前，ESLint 会在遍历时将每个节点的 `parent` 属性设置为其父节点。

#### `Program` 节点（The `Program` Node）

`Program` 节点必须有 `tokens` 和 `comments` 属性。这两个属性都是下述 `Token` 接口的数组。

```ts
interface Token {
	type: string;
	loc: SourceLocation;
	// 关于 `range` 属性的细节，请参见“所有节点”一节。
	range: [number, number];
	value: string;
}
```

- `tokens`（`Token[]`）是影响程序行为的 token 数组。token 之间可以存在任意空格，因此规则会检查 `Token#range` 来检测 token 之间的空格。此数组必须按 `Token#range[0]` 排序。
- `comments`（`Token[]`）是注释 token 的数组。此数组必须按 `Token#range[0]` 排序。

所有 token 和注释的范围索引不得与其他 token 和注释的范围重叠。

#### `Literal` 节点（The `Literal` Node）

`Literal` 节点必须有 `raw` 属性。

- `raw`（`string`）是此字面量的源代码。它与 `code.slice(node.range[0], node.range[1])` 相同。

### 打包自定义解析器（Packaging a Custom Parser）

要将你的自定义解析器发布到 npm，请执行以下操作：

1. 按照上面[创建自定义解析器](#创建自定义解析器create-a-custom-parser)一节创建自定义解析器。
1. 为自定义解析器[创建一个 npm 包](https://docs.npmjs.com/creating-node-js-modules)。
1. 在你的 `package.json` 文件中，将 [`main`](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#main) 字段设置为导出你的自定义解析器的文件。
1. [发布该 npm 包](https://docs.npmjs.com/creating-and-publishing-unscoped-public-packages)。

有关发布 npm 包的更多信息，请参阅 [npm 文档](https://docs.npmjs.com/)。

发布 npm 包后，你可以通过将该包添加到项目中来使用它。例如：

```shell
npm install --save-dev eslint-parser-myparser
```

然后使用 `languageOptions.parser` 属性将自定义解析器添加到你的 ESLint 配置文件中。例如：

```js
// eslint.config.js

const { defineConfig } = require("eslint/config");
const myparser = require("eslint-parser-myparser");

module.exports = defineConfig([
	{
		languageOptions: {
			parser: myparser,
		},
		// ...其余配置
	},
]);
```

使用旧版配置时，将 `parser` 属性指定为字符串：

```js
// .eslintrc.js

module.exports = {
	parser: "eslint-parser-myparser",
	// ...其余配置
};
```

要了解更多关于在项目中使用 ESLint 解析器的信息，请参阅[配置解析器（Configure a Parser）](https://eslint.org/docs/latest/use/configure/parser)。

### 示例（Example）

自定义解析器的复杂示例，请参阅 [`@typescript-eslint/parser`](https://github.com/typescript-eslint/typescript-eslint/tree/main/packages/parser) 的源代码。

一个简单的自定义解析器，向规则提供 `context.sourceCode.parserServices.foo()` 方法：

```javascript
// awesome-custom-parser.js
var espree = require("espree");
function parseForESLint(code, options) {
	return {
		ast: espree.parse(code, options),
		services: {
			foo: function () {
				console.log("foo");
			},
		},
		scopeManager: null,
		visitorKeys: null,
	};
}

module.exports = { parseForESLint };
```

在 ESLint 配置文件中包含该自定义解析器：

```js
// eslint.config.js
const { defineConfig } = require("eslint/config");

module.exports = defineConfig([
	{
		languageOptions: {
			parser: require("./path/to/awesome-custom-parser"),
		},
	},
]);
```

或者使用旧版配置：

```js
// .eslintrc.json
{
    "parser": "./path/to/awesome-custom-parser.js"
}
```


---

## 统计数据（Stats Data）

> 原文链接：https://eslint.org/docs/latest/extend/stats

虽然可以通过设置 [TIMING](https://eslint.org/docs/latest/extend/custom-rules#profiling-rule-performance) 环境变量来对一次 ESLint 运行进行整体规则性能分析，但有时获取更*细粒度*的计时数据（每个文件每条规则的 lint 时间）或收集其他感兴趣的指标会很有用——尤其是在开发新的[自定义插件](https://eslint.org/docs/latest/extend/plugins)、评估或基准测试新的语言或规则集时。对于这些用例，你可以选择从 ESLint 收集运行时统计数据。

### 启用统计收集（Enable stats collection）

要启用统计数据收集，你可以：

1. 使用 `--stats` CLI 选项。这会将统计数据传入用于输出 ESLint 结果的格式化器。（注意：并非所有格式化器都会输出统计数据。）
1. 在 `ESLint` 构造函数上设置 `stats: true` 选项。

启用统计数据后，每个 [LintResult](https://eslint.org/docs/latest/integrate/nodejs-api#-lintresult-type) 对象会新增一个 `stats` 键，包含解析时间、修复时间、每条规则的 lint 时间等数据。

因此，它不能通过 stdout 直接获得，但可以通过 CLI 使用格式化器，或通过 Node.js API 方便地获取，以满足你的特定需求。

### ◆ Stats 类型（◆ Stats type）

`Stats` 值是每次 lint 运行的计时信息。[LintResult](https://eslint.org/docs/latest/integrate/nodejs-api#-lintresult-type) 类型的 `stats` 属性包含它。它有以下属性：

- `fixPasses`（`number`）<br>
  ESLint 在 lint 之后至少应用了一次修复的次数。
- `times`（`{ passes: TimePass[] }`）<br>
  在（解析、修复、lint）一个文件上花费的时间，其中 lint 指的是每条规则的计时信息。
    - `TimePass`（`{ parse?: ParseTime, rules?: Record<string, RuleTime>, fix: FixTime, total: number }`）<br>
      一个包含（解析、修复、lint）所花时间的对象
        - `ParseTime`（`{ total: number }`）<br>
          解析文件所花费的总时间。如果没有执行解析，则省略 `parse` 属性，例如当处理器的 `preprocess()` 方法返回空数组时。
        - `RuleTime`（`{ total: number }`）<br>
          在一条规则上花费的总时间。
        - `FixTime`（`{ total: number }`）<br>
          对代码应用修复所花费的总时间。

#### CLI 用法（CLI usage）

考虑以下示例：

```js [file-to-fix.js]
/*eslint no-regex-spaces: "error", wrap-regex: "error"*/

function a() {
	return / foo/.test("bar");
}
```

带 `--stats` 运行 ESLint，并通过内置的 [`json` 格式化器](https://eslint.org/docs/latest/use/formatters/)输出为 JSON：

```shell
npx eslint file-to-fix.js --fix --stats -f json
```

这会产生以下 `stats` 条目，作为格式化后的 lint 结果对象的一部分：

```json
{
	"times": {
		"passes": [
			{
				"parse": {
					"total": 3.975959
				},
				"rules": {
					"no-regex-spaces": {
						"total": 0.160792
					},
					"wrap-regex": {
						"total": 0.422626
					}
				},
				"fix": {
					"total": 0.080208
				},
				"total": 12.765959
			},
			{
				"parse": {
					"total": 0.623542
				},
				"rules": {
					"no-regex-spaces": {
						"total": 0.043084
					},
					"wrap-regex": {
						"total": 0.007959
					}
				},
				"fix": {
					"total": 0
				},
				"total": 1.148875
			}
		]
	},
	"fixPasses": 1
}
```

请注意，对于上面的简单示例，所有规则时间之和应可直接与 TIMING 输出的第一列相比较。使用 `TIMING=all` 运行相同的命令，你可以验证这一点：

```bash
$ TIMING=all npx eslint file-to-fix.js --fix --stats -f json
...
Rule            | Time (ms) | Relative
:---------------|----------:|--------:
wrap-regex      |     0.431 |    67.9%
no-regex-spaces |     0.204 |    32.1%
```

#### API 用法（API Usage）

你可以使用 Node.js API 实现同样的效果，只需向 `ESLint` 构造函数传递 `stats: true` 选项。例如：

```js
const { ESLint } = require("eslint");

(async function main() {
	// 1. 创建实例。
	const eslint = new ESLint({ stats: true, fix: true });

	// 2. 检查文件。
	const results = await eslint.lintFiles(["file-to-fix.js"]);

	// 3. 格式化结果。
	const formatter = await eslint.loadFormatter("json");
	const resultText = formatter.format(results);

	// 4. 输出。
	console.log(resultText);
})().catch(error => {
	process.exitCode = 1;
	console.error(error);
});
```


---

