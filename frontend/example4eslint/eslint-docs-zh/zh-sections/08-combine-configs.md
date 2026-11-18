## 合并配置（Combine Configs）

> 原文链接：https://eslint.org/docs/latest/use/configure/combine-configs

在许多情况下，你不会从头开始编写 ESLint 配置文件，而是会结合使用预定义配置和可共享配置，再加上你自己的覆盖配置，来创建项目的配置。本页介绍一些可以在配置文件中用来合并配置的模式。

### 应用一个配置对象（Apply a Config Object）

如果你从另一个模块导入一个[配置对象](https://eslint.org/docs/latest/use/core-concepts/glossary#config-object)，你可以通过创建一个带有 `files` 键的新对象，并使用 `extends` 键合入导入对象的其余属性，从而将其仅应用于文件的子集。例如：

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

这里，`"js/recommended"` 预定义配置首先应用于匹配 `"**/*.js"` 模式的文件，然后再添加所需的 `no-unused-vars` 配置。

### 应用一个配置数组（Apply a Config Array）

如果你从另一个模块导入一个[配置数组](https://eslint.org/docs/latest/use/core-concepts/glossary#config-array)，你可以通过使用 `extends` 键将配置数组（配置对象的数组）仅应用于文件的子集。例如：

```js
// eslint.config.js
import exampleConfigs from "eslint-config-example";
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		files: ["**/*.js"],
		extends: [exampleConfigs],
		rules: {
			"no-unused-vars": "warn",
		},
	},
]);
```

这里，`exampleConfigs` 可共享配置首先应用于匹配 `"**/*.js"` 模式的文件，然后另一个配置对象再添加所需的 `no-unused-vars` 配置。
