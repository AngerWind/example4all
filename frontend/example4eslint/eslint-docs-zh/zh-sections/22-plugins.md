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
