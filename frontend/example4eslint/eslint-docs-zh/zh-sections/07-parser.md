## 配置解析器（Configure a Parser）

> 原文链接：https://eslint.org/docs/latest/use/configure/parser

::: tip
本页介绍如何使用 flat config 格式配置解析器。
:::

你可以使用自定义解析器将 JavaScript 代码转换为抽象语法树（AST），供 ESLint 进行求值。如果你的代码与 ESLint 的默认解析器 Espree 不兼容，你可能需要添加自定义解析器。

### 配置自定义解析器（Configure a Custom Parser）

在许多情况下，你可以使用 ESLint 自带的[默认解析器](https://github.com/eslint/js/tree/main/packages/espree)来解析你的 JavaScript 代码。你可以选择性地使用 `parser` 属性覆盖默认解析器。`parser` 属性必须是一个符合[解析器接口](https://eslint.org/docs/latest/extend/custom-parsers)的对象。例如，你可以使用 [`@babel/eslint-parser`](https://www.npmjs.com/package/@babel/eslint-parser) 包来让 ESLint 解析实验性语法：

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

::: warning
不能保证外部解析器能与 ESLint 正常配合工作。对于仅影响第三方解析器的不兼容性问题，ESLint 不会修复相关 bug。
:::

### 配置解析器选项（Configure Parser Options）

解析器可以接受选项来改变其行为。`languageOptions.parserOptions` 用于直接向解析器传递选项。这些选项始终是特定于解析器的，因此你需要查阅所使用解析器的文档以了解可用选项。下面是为 Babel ESLint 解析器设置解析器选项的示例：

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
