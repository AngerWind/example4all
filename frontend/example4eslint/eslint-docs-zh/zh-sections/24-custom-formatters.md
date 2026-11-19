## 自定义格式化器（Custom Formatters）

> 原文链接：https://eslint.org/docs/latest/extend/custom-formatters

自定义格式化器让你以最适合自己需求的格式展示 lint 结果，无论是特定的文件格式、某种展示风格，还是为特定工具优化的格式。

ESLint 也提供了你可以使用的[内置格式化器](https://eslint.org/docs/latest/use/formatters/)。

你可以直接在项目中包含自定义格式化器，或创建 npm 包单独分发。

### 创建自定义格式化器（Create a Custom Formatter）

每个格式化器是一个接收 `results` 对象和 `context` 作为参数并返回字符串的函数。例如，下面是内置 [JSON 格式化器](https://eslint.org/docs/latest/use/formatters/#json)的实现方式：

```js
//my-awesome-formatter.js
module.exports = function (results, context) {
	return JSON.stringify(results, null, 2);
};
```

格式化器也可以是 async 函数（自 ESLint v8.4.0 起），下面是一个简单示例：

```js
//my-awesome-formatter.js
module.exports = async function (results) {
	const formatted = await asyncTask();
	return formatted;
};
```

要使用此格式化器运行 ESLint，可以使用 [`-f`（或 `--format`）](https://eslint.org/docs/latest/use/command-line-interface#-f---format) 命令行标志。本地定义的自定义格式化器的路径必须以小数点（`.`）开头，例如 `./my-awesome-formatter.js` 或 `../formatters/my-awesome-formatter.js`。

```bash
eslint -f ./my-awesome-formatter.js src/
```

本节的其余部分包含如何使用自定义格式化器函数的参考信息。

#### `results` 参数（The `results` Argument）

传入格式化器的 `results` 对象是一个 [`LintResult`](https://eslint.org/docs/latest/integrate/nodejs-api#-lintresult-type) 对象的数组，包含各个文件的 lint 结果。下面是一个输出示例：

```js
[
	{
		filePath: "/path/to/a/file.js",
		messages: [
			{
				ruleId: "curly",
				severity: 2,
				message: "Expected { after 'if' condition.",
				line: 2,
				column: 1,
			},
			{
				ruleId: "no-process-exit",
				severity: 2,
				message: "Don't use process.exit(); throw an error instead.",
				line: 3,
				column: 1,
			},
		],
		errorCount: 2,
		warningCount: 0,
		fixableErrorCount: 0,
		fixableWarningCount: 0,
		source: "var err = doStuff();\nif (err) console.log('failed tests: ' + err);\nprocess.exit(1);\n",
	},
	{
		filePath: "/path/to/Gruntfile.js",
		messages: [],
		errorCount: 0,
		warningCount: 0,
		fixableErrorCount: 0,
		fixableWarningCount: 0,
	},
];
```

#### `context` 参数（The `context` Argument）

格式化器函数接收一个 `context` 对象作为第二个参数。该对象有以下属性：

- `color`（可选）：如果设置了 `--color`，此属性为 `true`。如果设置了 `--no-color`，则为 `false`。如果两个选项都未提供，则省略此属性。
- `cwd`：当前工作目录。此值来自 [ESLint](https://eslint.org/docs/latest/integrate/nodejs-api#-new-eslintoptions) 类的 `cwd` 构造函数选项。
- `maxWarningsExceeded`（可选）：如果设置了 `--max-warnings` 且警告数量超过了限制，此属性的值是一个包含两个属性的对象：
    - `maxWarnings`：`--max-warnings` 选项的值
    - `foundWarnings`：lint 警告的数量
- `rulesMeta`：规则的 `meta` 属性值。有关规则的更多信息，请参见[自定义规则（Custom Rules）](https://eslint.org/docs/latest/extend/custom-rules)页面。

例如，如果运行了 `no-extra-semi` 规则，该对象将如下所示：

```js
{
    cwd: "/path/to/cwd",
    maxWarningsExceeded: {
        maxWarnings: 5,
        foundWarnings: 6
    },
    rulesMeta: {
        "no-extra-semi": {
            type: "suggestion",
            docs: {
                description: "disallow unnecessary semicolons",
                recommended: true,
                url: "https://eslint.org/docs/rules/no-extra-semi"
            },
            fixable: "code",
            schema: [],
            messages: {
                unexpected: "Unnecessary semicolon."
            }
        }
    },
}
```

**注意：**如果 lint 是由已废弃的 `CLIEngine` 类执行的，`context` 参数可能是不同的值，因为这取决于 API 使用者。如果你想支持旧环境，请检查 `context` 参数是否是预期的值。

#### 向格式化器传递参数（Passing Arguments to Formatters）

虽然格式化器函数除了 results 对象和 context 之外不接收其他参数，但可以使用下面介绍的方法向自定义格式化器传递额外的数据。

##### 使用环境变量（Use Environment Variables）

自定义格式化器可以访问环境变量，因此可以基于环境变量数据改变其行为。

下面是一个使用 `FORMATTER_SKIP_WARNINGS` 环境变量来决定是否在结果中显示警告的示例：

```js
module.exports = function (results) {
	var skipWarnings = process.env.FORMATTER_SKIP_WARNINGS === "true";

	var results = results || [];
	var summary = results.reduce(
		function (seq, current) {
			current.messages.forEach(function (msg) {
				var logMessage = {
					filePath: current.filePath,
					ruleId: msg.ruleId,
					message: msg.message,
					line: msg.line,
					column: msg.column,
				};

				if (msg.severity === 1) {
					logMessage.type = "warning";
					seq.warnings.push(logMessage);
				}
				if (msg.severity === 2) {
					logMessage.type = "error";
					seq.errors.push(logMessage);
				}
			});
			return seq;
		},
		{
			errors: [],
			warnings: [],
		},
	);

	if (summary.errors.length > 0 || summary.warnings.length > 0) {
		var warnings = !skipWarnings ? summary.warnings : []; // 在这种情况下跳过警告

		var lines = summary.errors
			.concat(warnings)
			.map(function (msg) {
				return (
					"\n" +
					msg.type +
					" " +
					msg.ruleId +
					"\n  " +
					msg.filePath +
					":" +
					msg.line +
					":" +
					msg.column
				);
			})
			.join("\n");

		return lines + "\n";
	}
};
```

你会像这样带着设置好的环境变量和这个自定义格式化器运行 ESLint：

```bash
FORMATTER_SKIP_WARNINGS=true eslint -f ./my-awesome-formatter.js src/
```

输出将是：

```bash
error space-infix-ops
  src/configs/bundler.js:6:8

error semi
  src/configs/bundler.js:6:10
```

##### 复杂的参数传递（Complex Argument Passing）

如果你发现自定义格式化器模式无法为你想要的 ESLint 结果格式化方式提供足够的选项，最好的办法是使用 ESLint 内置的 [JSON 格式化器](https://eslint.org/docs/latest/use/formatters/#json)，并将输出通过管道传给第二个程序。例如：

```bash
eslint -f json src/ | your-program-that-reads-JSON --option
```

在此示例中，`your-program-that-reads-json` 程序可以接收 ESLint 结果的原始 JSON 并对其进行处理，然后输出自己的结果格式。你可以向该程序传递任意多的命令行参数来定制输出。

#### 为终端格式化（Formatting for Terminals）

像 [iTerm2](https://www.iterm2.com/) 或 [Guake](http://guake.org/) 这样的现代终端期望特定的结果格式，以便在点击文件名时自动打开它们。大多数终端为此支持以下格式：

```bash
file:line:column
```

### 打包自定义格式化器（Packaging a Custom Formatter）

自定义格式化器可以通过 npm 包分发。为此，创建一个名称格式为 `eslint-formatter-*` 的 npm 包，其中 `*` 是你的格式化器名称（例如 `eslint-formatter-awesome`）。然后项目应安装该包，并使用 [`-f`（或 `--format`）](https://eslint.org/docs/latest/use/command-line-interface#-f---format) 标志使用自定义格式化器，如下所示：

```bash
eslint -f awesome src/
```

因为当指定的格式化器不以小数点开头时，ESLint 知道去查找以 `eslint-formatter-` 开头的包，所以在使用打包的自定义格式化器时，你不需要输入 `eslint-formatter-`。

自定义格式化器的 `package.json` 提示：

- [`main`](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#main) 入口必须是实现你的自定义格式化器的 JavaScript 文件。
- 添加这些 [`keywords`](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#keywords) 以帮助用户找到你的格式化器：
    - `"eslint"`
    - `"eslint-formatter"`
    - `"eslintformatter"`

查看 [npm 上的所有自定义格式化器](https://www.npmjs.com/search?q=eslint-formatter)。

### 示例（Examples）

#### 摘要格式化器（Summary Formatter）

一个只报告错误和警告总数的格式化器如下所示：

```javascript
module.exports = function (results, context) {
	// 累计错误和警告
	var summary = results.reduce(
		function (seq, current) {
			seq.errors += current.errorCount;
			seq.warnings += current.warningCount;
			return seq;
		},
		{ errors: 0, warnings: 0 },
	);

	if (summary.errors > 0 || summary.warnings > 0) {
		return (
			"Errors: " +
			summary.errors +
			", Warnings: " +
			summary.warnings +
			"\n"
		);
	}

	return "";
};
```

使用上述摘要格式化器运行 `eslint`：

```bash
eslint -f ./my-awesome-formatter.js src/
```

将产生以下输出：

```bash
Errors: 2, Warnings: 4
```

#### 详细格式化器（Detailed Formatter）

一个更复杂的报告可能如下所示：

```javascript
module.exports = function (results, context) {
	var results = results || [];

	var summary = results.reduce(
		function (seq, current) {
			current.messages.forEach(function (msg) {
				var logMessage = {
					filePath: current.filePath,
					ruleId: msg.ruleId,
					ruleUrl: context.rulesMeta[msg.ruleId].docs.url,
					message: msg.message,
					line: msg.line,
					column: msg.column,
				};

				if (msg.severity === 1) {
					logMessage.type = "warning";
					seq.warnings.push(logMessage);
				}
				if (msg.severity === 2) {
					logMessage.type = "error";
					seq.errors.push(logMessage);
				}
			});
			return seq;
		},
		{
			errors: [],
			warnings: [],
		},
	);

	if (summary.errors.length > 0 || summary.warnings.length > 0) {
		var lines = summary.errors
			.concat(summary.warnings)
			.map(function (msg) {
				return (
					"\n" +
					msg.type +
					" " +
					msg.ruleId +
					(msg.ruleUrl ? " (" + msg.ruleUrl + ")" : "") +
					"\n  " +
					msg.filePath +
					":" +
					msg.line +
					":" +
					msg.column
				);
			})
			.join("\n");

		return lines + "\n";
	}
};
```

当你使用这个自定义格式化器运行 ESLint 时：

```bash
eslint -f ./my-awesome-formatter.js src/
```

输出为：

```bash
error space-infix-ops (https://eslint.org/docs/rules/space-infix-ops)
  src/configs/bundler.js:6:8
error semi (https://eslint.org/docs/rules/semi)
  src/configs/bundler.js:6:10
warning no-unused-vars (https://eslint.org/docs/rules/no-unused-vars)
  src/configs/bundler.js:5:6
warning no-unused-vars (https://eslint.org/docs/rules/no-unused-vars)
  src/configs/bundler.js:6:6
warning no-shadow (https://eslint.org/docs/rules/no-shadow)
  src/configs/bundler.js:65:32
warning no-unused-vars (https://eslint.org/docs/rules/no-unused-vars)
  src/configs/clean.js:3:6
```
