## 统计数据（Stats Data）

> 原文链接：https://eslint.org/docs/latest/extend/stats

虽然可以通过设置 [TIMING](https://eslint.org/docs/latest/extend/custom-rules#profiling-rule-performance) 环境变量来对一次 ESLint 运行进行整体规则性能分析，但有时获取更*细粒度*的计时数据（每个文件每条规则的 lint 时间）或收集其他感兴趣的指标会很有用——尤其是在开发新的[自定义插件](https://eslint.org/docs/latest/extend/plugins)、评估或基准测试新的语言或规则集时。对于这些用例，你可以选择从 ESLint 收集运行时统计数据。

### 启用统计收集（Enable stats collection）

要启用统计数据收集，你可以：

1. 使用 `--stats` CLI 选项。这会将统计数据传入用于输出 ESLint 结果的格式化器。（注意：并非所有格式化器都会输出统计数据。）
1. 在 `ESLint` 构造函数上设置 `stats: true` 选项。

启用统计数据后，每个 [LintResult](https://eslint.org/docs/latest/integrate/nodejs-api#-lintresult-type) 对象会新增一个 `stats` 键，包含解析时间、修复时间、每条规则的 lint 时间等数据。

因此，它不能通过 stdout 直接获得，但可以通过 CLI 使用格式化器，或通过 Node.js API 方便地获取，以满足你的特定需求。

### ◆ Stats 类型（◆ Stats type）

`Stats` 值是每次 lint 运行的计时信息。[LintResult](https://eslint.org/docs/latest/integrate/nodejs-api#-lintresult-type) 类型的 `stats` 属性包含它。它有以下属性：

- `fixPasses`（`number`）<br>
  ESLint 在 lint 之后至少应用了一次修复的次数。
- `times`（`{ passes: TimePass[] }`）<br>
  在（解析、修复、lint）一个文件上花费的时间，其中 lint 指的是每条规则的计时信息。
    - `TimePass`（`{ parse?: ParseTime, rules?: Record<string, RuleTime>, fix: FixTime, total: number }`）<br>
      一个包含（解析、修复、lint）所花时间的对象
        - `ParseTime`（`{ total: number }`）<br>
          解析文件所花费的总时间。如果没有执行解析，则省略 `parse` 属性，例如当处理器的 `preprocess()` 方法返回空数组时。
        - `RuleTime`（`{ total: number }`）<br>
          在一条规则上花费的总时间。
        - `FixTime`（`{ total: number }`）<br>
          对代码应用修复所花费的总时间。

#### CLI 用法（CLI usage）

考虑以下示例：

```js [file-to-fix.js]
/*eslint no-regex-spaces: "error", wrap-regex: "error"*/

function a() {
	return / foo/.test("bar");
}
```

带 `--stats` 运行 ESLint，并通过内置的 [`json` 格式化器](https://eslint.org/docs/latest/use/formatters/)输出为 JSON：

```shell
npx eslint file-to-fix.js --fix --stats -f json
```

这会产生以下 `stats` 条目，作为格式化后的 lint 结果对象的一部分：

```json
{
	"times": {
		"passes": [
			{
				"parse": {
					"total": 3.975959
				},
				"rules": {
					"no-regex-spaces": {
						"total": 0.160792
					},
					"wrap-regex": {
						"total": 0.422626
					}
				},
				"fix": {
					"total": 0.080208
				},
				"total": 12.765959
			},
			{
				"parse": {
					"total": 0.623542
				},
				"rules": {
					"no-regex-spaces": {
						"total": 0.043084
					},
					"wrap-regex": {
						"total": 0.007959
					}
				},
				"fix": {
					"total": 0
				},
				"total": 1.148875
			}
		]
	},
	"fixPasses": 1
}
```

请注意，对于上面的简单示例，所有规则时间之和应可直接与 TIMING 输出的第一列相比较。使用 `TIMING=all` 运行相同的命令，你可以验证这一点：

```bash
$ TIMING=all npx eslint file-to-fix.js --fix --stats -f json
...
Rule            | Time (ms) | Relative
:---------------|----------:|--------:
wrap-regex      |     0.431 |    67.9%
no-regex-spaces |     0.204 |    32.1%
```

#### API 用法（API Usage）

你可以使用 Node.js API 实现同样的效果，只需向 `ESLint` 构造函数传递 `stats: true` 选项。例如：

```js
const { ESLint } = require("eslint");

(async function main() {
	// 1. 创建实例。
	const eslint = new ESLint({ stats: true, fix: true });

	// 2. 检查文件。
	const results = await eslint.lintFiles(["file-to-fix.js"]);

	// 3. 格式化结果。
	const formatter = await eslint.loadFormatter("json");
	const resultText = formatter.format(results);

	// 4. 输出。
	console.log(resultText);
})().catch(error => {
	process.exitCode = 1;
	console.error(error);
});
```
