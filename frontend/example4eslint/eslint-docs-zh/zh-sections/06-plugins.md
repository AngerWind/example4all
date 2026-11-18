## 配置插件（Configure Plugins）

> 原文链接：https://eslint.org/docs/latest/use/configure/plugins

::: tip
本页介绍如何使用 flat config 格式配置插件。
:::

你可以通过多种不同的方式使用插件来扩展 ESLint。插件可以包含：

- 自定义规则：验证你的代码是否满足某个预期，以及当不满足预期时该做什么。
- 自定义配置：有关如何使用这些配置的详细信息，请参阅插件的文档。
- 自定义处理器：从其他类型的文件中提取 JavaScript 代码，或在 lint 之前对代码进行预处理。

### 配置插件（Configure Plugins）

ESLint 支持使用第三方插件。插件只是符合 ESLint 认可的特定接口的对象。

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

::: tip
为插件创建命名空间时，约定是使用不带 `eslint-plugin-` 前缀的 npm 包名。在前面的示例中，`eslint-plugin-example` 被赋予了 `example` 命名空间。
:::

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
import myRule from "./rules/my-rule.js";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		plugins: {
			local: {
				rules: {
					"my-rule": myRule,
				},
			},
		},
		rules: {
			"local/my-rule": "warn",
		},
	},
]);
```

这里，`local` 命名空间用于定义一个虚拟插件。规则 `myRule` 随后在虚拟插件的 `rules` 对象内被命名为 `my-rule`。（插件的完整格式请参见[创建插件](https://eslint.org/docs/latest/extend/plugins)。）然后你可以将该规则引用为 `local/my-rule` 来配置它。

### 使用插件规则（Use Plugin Rules）

你可以使用插件中包含的特定规则。为此，在配置对象中使用 `plugins` 键指定插件。`plugins` 键的值是一个对象，其中插件名称是属性名，值是插件对象本身。下面是一个示例：

```js
// eslint.config.js
import jsdoc from "eslint-plugin-jsdoc";
import { defineConfig } from "eslint/config";

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

### 指定处理器（Specify a Processor）

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

::: tip
当你在配置对象中指定 `language` 时，`languageOptions` 就变成特定于该语言的。每种语言定义自己的 `languageOptions`，因此请查看插件的文档以确定哪些选项可用。
:::

### 常见问题（Common Problems）

- [使用 ESLint < v9.0.0 API 的插件规则](https://eslint.org/docs/latest/use/troubleshooting/v9-rule-api-changes)
- [插件配置尚未升级到 flat config](https://eslint.org/docs/latest/use/configure/migration-guide#using-eslintrc-configs-in-flat-config)
