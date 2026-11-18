## 配置语言选项（Configure Language Options）

> 原文链接：https://eslint.org/docs/latest/use/configure/language-options

::: tip
本页介绍如何使用 flat config 格式配置语言选项。
:::

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
