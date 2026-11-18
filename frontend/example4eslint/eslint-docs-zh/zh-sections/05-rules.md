## 配置规则（Configure Rules）

> 原文链接：https://eslint.org/docs/latest/use/configure/rules

::: tip
本页介绍如何使用 flat config 格式配置规则。
:::

规则是 ESLint 的核心构建块。规则验证你的代码是否满足某个预期，以及当不满足预期时该做什么。规则还可以包含特定于该规则的额外配置选项。

ESLint 自带大量[内置规则](https://eslint.org/docs/latest/rules/)，你也可以通过插件添加更多规则。你可以使用配置注释或配置文件来修改项目使用的规则。

### 规则严重级别（Rule Severities）

要更改规则的严重级别，将规则 ID 设置为以下值之一：

- `"off"` 或 `0` - 关闭该规则。
- `"warn"` 或 `1` - 将该规则作为警告启用（不影响退出码）。
- `"error"` 或 `2` - 将该规则作为错误启用（触发时退出码为 1）。

规则通常被设置为 `"error"`，以便在持续集成测试、pre-commit 检查和拉取请求合并期间强制遵守规则，因为这样会使 ESLint 以非零退出码退出。

如果你不想强制遵守某条规则，但仍希望 ESLint 报告该规则的违规情况，请将严重级别设置为 `"warn"`。这通常用于以下场景：引入一条最终会设置为 `"error"` 的新规则时；规则标记的不是潜在的构建时或运行时错误（例如未使用的变量）时；或者规则无法确定是否确实发现了问题（规则可能有误报，需要人工审查）时。

#### 使用配置注释（Use configuration comments）

要在文件内使用配置注释来配置规则，使用以下格式的注释：

```js
/* eslint eqeqeq: "off", curly: "error" */
```

在这个例子中，[`eqeqeq`](https://eslint.org/docs/latest/rules/eqeqeq) 被关闭，[`curly`](https://eslint.org/docs/latest/rules/curly) 被作为错误启用。你也可以使用规则严重级别的数字等价形式：

```js
/* eslint eqeqeq: 0, curly: 2 */
```

这个例子与上一个例子相同，只是使用了数字代码而不是字符串值。[`eqeqeq`](https://eslint.org/docs/latest/rules/eqeqeq) 规则被关闭，[`curly`](https://eslint.org/docs/latest/rules/curly) 规则被设置为错误。

如果规则有额外的选项，你可以使用数组字面量语法来指定它们，例如：

```js
/* eslint quotes: ["error", "double"], curly: 2 */
```

该注释为 [`quotes`](https://eslint.org/docs/latest/rules/quotes) 规则指定了 `"double"` 选项。数组中的第一项始终是规则严重级别（数字或字符串）。

##### 配置注释描述（Configuration Comment Descriptions）

配置注释可以包含描述，以解释为什么需要该注释。描述必须出现在配置之后，并通过两个或更多连续的 `-` 字符与配置分隔。例如：

```js
/* eslint eqeqeq: "off", curly: "error" -- Here's a description about why this configuration is necessary. */
```

```js
/* eslint eqeqeq: "off", curly: "error"
    --------
    Here's a description about why this configuration is necessary. */
```

```js
/* eslint eqeqeq: "off", curly: "error"
 * --------
 * This will not work due to the line above starting with a '*' character.
 */
```

##### 报告未使用的 `eslint` 内联配置注释（Report unused `eslint` inline config comments）

要报告未使用的 `eslint` 内联配置注释（即那些与已配置内容相比没有带来任何改变的注释），使用 `reportUnusedInlineConfigs` 设置。例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		linterOptions: {
			reportUnusedInlineConfigs: "error",
		},
	},
]);
```

此设置默认为 `"off"`。

此设置类似于 [`--report-unused-inline-configs`](https://eslint.org/docs/latest/use/command-line-interface#--report-unused-inline-configs) CLI 选项。

::: tip
要围绕配置注释强制执行最佳实践，请查看 [`eslint-plugin-eslint-comments`](https://github.com/eslint-community/eslint-plugin-eslint-comments)（[文档](https://eslint-community.github.io/eslint-plugin-eslint-comments/)）。
:::

#### 使用配置文件（Use Configuration Files）

要在[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file)中配置规则，使用 `rules` 键以及错误级别和你想使用的任何选项。例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		rules: {
			eqeqeq: "off",
			"no-unused-vars": "error",
			"prefer-const": ["error", { ignoreReadBeforeAssign: true }],
		},
	},
]);
```

当多个配置对象指定同一条规则时，规则配置会被合并，后面的对象优先于之前的任何对象。例如：

```js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		rules: {
			semi: ["error", "never"],
		},
	},
	{
		rules: {
			semi: ["warn", "always"],
		},
	},
]);
```

使用此配置时，`semi` 的最终规则配置是 `["warn", "always"]`，因为它在数组中最后出现。数组表示该配置同时包含严重级别和选项。你可以通过只定义一个字符串或数字来仅更改严重级别，如本例所示：

```js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		rules: {
			semi: ["error", "never"],
		},
	},
	{
		rules: {
			semi: "warn",
		},
	},
]);
```

这里，第二个配置对象只覆盖了严重级别，因此 `semi` 的最终配置是 `["warn", "never"]`。

::: important
通过配置注释配置的规则具有最高优先级，并在所有配置文件设置之后应用。
:::

### 来自插件的规则（Rules from Plugins）

要配置在插件中定义的规则，在规则 ID 前加上插件命名空间和 `/`。

在[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file)中，例如：

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

在这个配置文件中，规则 `example/rule1` 来自名为 `eslint-plugin-example` 的插件。

你也可以在配置注释中使用这种格式，例如：

```js
/* eslint "example/rule1": "error" */
```

:::important
为了在配置注释中使用插件规则，你的配置文件必须加载该插件，并在配置的 `plugins` 对象中指定它。配置注释本身无法加载插件。
:::

### 禁用规则（Disable Rules）

#### 使用配置注释（Use configuration comments）

- **谨慎使用。** 内联禁用 ESLint 规则应受到限制，仅在有明确且合理理由的情况下使用。内联禁用规则不应成为解决 lint 错误的默认方案。
- **记录原因。** 在注释的 `--` 部分之后提供注释，解释禁用特定规则的原因。该文档应说明为什么禁用该规则以及为什么在特定情况下有必要这样做。
- **临时方案。** 如果禁用注释是作为解决紧迫问题的临时措施而添加的，请创建后续任务以妥善解决根本问题。这可以确保禁用注释在之后被重新审视和解决。
- **代码审查和结对编程。** 鼓励团队成员定期审查彼此的代码。代码审查可以帮助识别禁用注释背后的原因，并确保它们被恰当使用。
- **配置。** 只要有可能，优先使用 ESLint 配置文件而不是禁用注释。配置文件可以实现一致的、项目范围的规则处理。

要禁用文件某一部分的规则警告，使用以下格式的块注释：

```js
/* eslint-disable */

alert("foo");

/* eslint-enable */
```

你也可以禁用或启用特定规则的警告：

```js
/* eslint-disable no-alert, no-console */

alert("foo");
console.log("bar");

/* eslint-enable no-alert, no-console */
```

::: warning
不带任何特定规则的 `/* eslint-enable */` 会导致所有被禁用的规则被重新启用。
:::

要禁用整个文件的规则警告，在文件顶部放置一个 `/* eslint-disable */` 块注释：

```js
/* eslint-disable */

alert("foo");
```

你也可以为整个文件禁用或启用特定规则：

```js
/* eslint-disable no-alert */

alert("foo");
```

要确保某条规则永不被应用（无论之后有任何启用/禁用行）：

```js
/* eslint no-alert: "off" */

alert("foo");
```

要在特定行上禁用所有规则，使用以下格式之一的行注释或块注释：

```js
alert("foo"); // eslint-disable-line

// eslint-disable-next-line
alert("foo");

/* eslint-disable-next-line */
alert("foo");

alert("foo"); /* eslint-disable-line */
```

要在特定行上禁用特定规则：

```js
alert("foo"); // eslint-disable-line no-alert

// eslint-disable-next-line no-alert
alert("foo");

alert("foo"); /* eslint-disable-line no-alert */

/* eslint-disable-next-line no-alert */
alert("foo");
```

要在特定行上禁用多条规则：

```js
alert("foo"); // eslint-disable-line no-alert, quotes, semi

// eslint-disable-next-line no-alert, quotes, semi
alert("foo");

alert("foo"); /* eslint-disable-line no-alert, quotes, semi */

/* eslint-disable-next-line no-alert, quotes, semi */
alert("foo");

/* eslint-disable-next-line
  no-alert,
  quotes,
  semi
*/
alert("foo");
```

以上所有方法也适用于插件规则。例如，要禁用 `eslint-plugin-example` 的 `rule-name` 规则，将插件名称（`example`）和规则名称（`rule-name`）组合为 `example/rule-name`：

```js
foo(); // eslint-disable-line example/rule-name
foo(); /* eslint-disable-line example/rule-name */
```

::: tip
禁用文件某一部分警告的注释告诉 ESLint 不要报告被禁用代码的规则违规。然而，ESLint 仍会解析整个文件，因此被禁用的代码仍然需要是语法上有效的 JavaScript。
:::

##### 注释描述（Comment descriptions）

配置注释可以包含描述，以解释为什么禁用或重新启用规则是必要的。描述必须出现在配置之后，并需要通过两个或更多连续的 `-` 字符与配置分隔。例如：

```js
// eslint-disable-next-line no-console -- Here's a description about why this configuration is necessary.
console.log("hello");

/* eslint-disable-next-line no-console --
 * Here's a very long description about why this configuration is necessary
 * along with some additional information
 **/
console.log("hello");
```

#### 使用配置文件（Use configuration files）

要在[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file)中为一组文件禁用规则，使用一个带有 `files` 键的后续配置对象。例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		rules: {
			"no-unused-expressions": "error",
		},
	},
	{
		files: ["*-test.js", "*.spec.js"],
		rules: {
			"no-unused-expressions": "off",
		},
	},
]);
```

#### 禁用内联注释（Disable Inline Comments）

要禁用所有内联配置注释，在配置文件中使用 `noInlineConfig` 设置。例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		linterOptions: {
			noInlineConfig: true,
		},
		rules: {
			"no-unused-expressions": "error",
		},
	},
]);
```

你也可以使用 [`--no-inline-config`](https://eslint.org/docs/latest/use/command-line-interface#--no-inline-config) CLI 选项来禁用规则注释以及其他内联配置。

##### 报告未使用的 `eslint-disable` 注释（Report unused `eslint-disable` comments）

要报告未使用的 `eslint-disable` 注释（即那些禁用了本不会在被禁用行上报问题的规则的注释），使用 `reportUnusedDisableDirectives` 设置。例如：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";

export default defineConfig([
	{
		linterOptions: {
			reportUnusedDisableDirectives: "error",
		},
	},
]);
```

此设置默认为 `"warn"`。

此设置类似于 [`--report-unused-disable-directives`](https://eslint.org/docs/latest/use/command-line-interface#--report-unused-disable-directives) 和 [`--report-unused-disable-directives-severity`](https://eslint.org/docs/latest/use/command-line-interface#--report-unused-disable-directives-severity) CLI 选项。
