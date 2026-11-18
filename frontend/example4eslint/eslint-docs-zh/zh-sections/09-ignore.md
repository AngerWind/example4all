## 忽略文件（Ignore Files）

> 原文链接：https://eslint.org/docs/latest/use/configure/ignore

::: tip
本页介绍如何使用 flat config 格式忽略文件。
:::

::: tip
本页介绍如何使用 `globalIgnores()` 函数完全忽略文件和目录。有关非全局忽略的更多信息，请参见[指定 files 和 ignores](https://eslint.org/docs/latest/use/configure/configuration-files#specifying-files-with-files-and-ignores)。有关全局忽略和非全局忽略之间区别的更多信息，请参见[使用 `ignores` 全局忽略文件](https://eslint.org/docs/latest/use/configure/configuration-files#globally-ignoring-files-with-ignores)。
:::

你可以通过在以下方式中指定一个或多个 glob 模式，来配置 ESLint 在检查时忽略某些文件和目录：

- 在你的 `eslint.config.js` 文件中。
- 在命令行上使用 `--ignore-pattern`。

### 忽略文件（Ignore Files）

在你的 `eslint.config.js` 文件中，你可以使用 `globalIgnores()` 辅助函数来指示要忽略的文件模式。下面是一个示例：

```js
// eslint.config.js
import { defineConfig, globalIgnores } from "eslint/config";

export default defineConfig([globalIgnores([".config/*"])]);
```

此配置指定 `.config` 目录中的所有文件都应被忽略。此模式添加在默认模式之后，默认模式是 `["**/node_modules/", ".git/"]`。

默认情况下，ESLint 检查匹配 `**/*.js`、`**/*.cjs` 和 `**/*.mjs` 模式的文件。如果你不想让 ESLint 检查这些文件，可以使用 `globalIgnores()` 忽略它们：

```js
// eslint.config.js
import { defineConfig, globalIgnores } from "eslint/config";

export default defineConfig([
	globalIgnores(["**/*.js", "**/*.cjs", "**/*.mjs"]),
]);
```

当 `files` 属性中指定了非 JS 文件时，ESLint 仍会检查匹配默认模式的文件。要只检查 `files` 属性中指定的文件，你必须忽略默认文件模式：

```js
// eslint.config.js
import { defineConfig, globalIgnores } from "eslint/config";

export default defineConfig([
	globalIgnores(["**/*.js", "**/*.cjs", "**/*.mjs"]),
	{
		files: ["**/*.ts"],
		ignores: [".config/**"],
		rules: {/* ... */},
	},
]);
```

你也可以在命令行上使用 [`--ignore-pattern`](https://eslint.org/docs/latest/use/command-line-interface#--ignore-pattern) 忽略文件，例如：

```shell
npx eslint . --ignore-pattern '.config/*'
```

### 忽略目录（Ignore Directories）

忽略目录的方式与忽略文件相同，即向 `globalIgnores()` 辅助函数传递一个模式。例如，下面整体忽略 `.config` 目录（意味着文件搜索完全不会进入该目录）：

```js
// eslint.config.js
import { defineConfig, globalIgnores } from "eslint/config";

export default defineConfig([globalIgnores([".config/"])]);
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

::: important
请注意，只有全局 `ignores` 模式可以匹配目录。
特定于某个配置的 `ignores` 模式只会匹配文件名。
:::

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
