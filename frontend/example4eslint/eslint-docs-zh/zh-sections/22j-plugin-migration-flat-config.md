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
