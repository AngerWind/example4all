## 配置迁移指南（Configuration Migration Guide）

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
