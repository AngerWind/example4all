## 调试你的配置（Debug Your Configuration）

> 原文链接：https://eslint.org/docs/latest/use/configure/debug

ESLint 会根据你的配置文件和命令行选项为每个被检查的文件创建一份配置。配置文件越大，就越难确定为什么某个文件没有按预期被检查。为了帮助调试配置，ESLint 提供了几个工具。

### 在调试模式下运行 CLI（Run the CLI in Debug Mode）

**适用场景：** 你不确定是否读取了正确的配置文件。当同一项目中存在多个配置文件时，可能会出现这种情况。

**操作方法：** 带上 [`--debug`](https://eslint.org/docs/latest/use/command-line-interface#--debug) 命令行标志运行 ESLint，并传入要检查的文件，如下所示：

```shell
npx eslint --debug file.js
```

这会将 ESLint 的所有调试信息输出到控制台。你应该将此输出复制到一个文件中，然后搜索 `eslint.config.js` 来查看加载了哪个文件。下面是一些示例输出：

```text
eslint:eslint Using file patterns: [ 'bin/eslint.js' ] +0ms
eslint:eslint Searching for eslint.config.js +0ms
eslint:eslint Loading config from C:\Users\nzakas\projects\eslint\eslint\eslint.config.js +5ms
eslint:eslint Config file URL is file:///C:/Users/nzakas/projects/eslint/eslint/eslint.config.js +0ms
```

### 打印文件计算后的配置（Print a File's Calculated Configuration）

**适用场景：** 你不确定为什么 lint 检查没有产生预期结果，可能是因为你的规则配置似乎没有被采纳，或者使用了错误的语言选项。

**操作方法：** 带上 [`--print-config`](https://eslint.org/docs/latest/use/command-line-interface#--print-config) 命令行标志运行 ESLint，并传入要检查的文件，如下所示：

```shell
npx eslint --print-config file.js
```

这会输出该文件计算后配置的 JSON 表示，例如：

```json
{
	"linterOptions": {
		"reportUnusedDisableDirectives": 1
	},
	"language": "@/js",
	"languageOptions": {
		"sourceType": "module",
		"ecmaVersion": "latest"
	},
	"plugins": ["@"],
	"rules": {
		"prefer-const": 2
	}
}
```

::: tip
你不会看到 `files`、`ignores` 或 `name` 的条目，因为它们只用于计算最终配置，因此不会出现在结果中。你会看到 ESLint 自身应用的任何默认配置。
:::

### 使用配置检查器（Use the Config Inspector）

**适用场景：** 你不确定配置文件中的某些配置对象是否匹配给定的文件名。

**操作方法：** 带上 [`--inspect-config`](https://eslint.org/docs/latest/use/command-line-interface#--inspect-config) 命令行标志运行 ESLint，如下所示：

```shell
npx eslint --inspect-config
```

这会通过安装并启动 [`@eslint/config-inspector`](https://github.com/eslint/config-inspector) 来启动配置检查器。然后你可以输入相关文件名，查看哪些配置对象会被应用。

![配置检查器截图，显示哪些配置对象匹配 index.js](https://eslint.org/docs/latest/assets/images/configure/config-inspector.png)

配置检查器还会显示哪些规则已废弃、你正在使用的可用规则数量等信息。
