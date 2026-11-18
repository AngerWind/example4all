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
