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
