### 选择器（Selectors）

> 原文链接：https://eslint.org/docs/latest/extend/selectors

一些规则和 API 允许使用选择器来查询 AST。本页旨在：

1. 解释什么是选择器
2. 描述创建选择器的语法
3. 描述选择器可以用于什么

#### 什么是选择器？（What is a selector?）

选择器是一个可用于匹配抽象语法树（AST）中节点的字符串。它对于描述代码中的特定语法模式很有用。

AST 选择器的语法类似于 [CSS 选择器](https://developer.mozilla.org/zh-CN/docs/Web/CSS/CSS_Selectors)的语法。如果你以前用过 CSS 选择器，AST 选择器的语法应该很容易理解。

最简单的选择器就是节点类型。节点类型选择器会匹配具有给定类型的所有节点。例如，考虑以下程序：

```js
var foo = 1;
bar.baz();
```

选择器 `"Identifier"` 会匹配程序中的所有 `Identifier` 节点。在这个例子中，该选择器会匹配 `foo`、`bar` 和 `baz` 对应的节点。

选择器不限于匹配单个节点类型。例如，选择器 `VariableDeclarator > Identifier` 会匹配所有直接父节点是 `VariableDeclarator` 的 `Identifier` 节点。在上面的程序中，它会匹配 `foo` 的节点，但不匹配 `bar` 和 `baz` 的节点。

#### 选择器可以有哪些语法？（What syntax can selectors have?）

支持以下选择器：

- AST 节点类型：`ForStatement`
- 通配符（匹配所有节点）：`*`
- 属性存在：`[attr]`
- 属性值：`[attr="foo"]` 或 `[attr=123]`
- 属性正则：`[attr=/foo.*/]`
- 属性条件：`[attr!="foo"]`、`[attr>2]`、`[attr<3]`、`[attr>=2]` 或 `[attr<=3]`
- 嵌套属性：`[attr.level2="foo"]`
- 字段：`FunctionDeclaration > Identifier.id`
- 第一个或最后一个子节点：`:first-child` 或 `:last-child`
- 第 n 个子节点（不支持 ax+b 形式）：`:nth-child(2)`
- 倒数第 n 个子节点（不支持 ax+b 形式）：`:nth-last-child(1)`
- 后代：`FunctionExpression ReturnStatement`
- 子节点：`UnaryExpression > Literal`
- 后续兄弟：`VariableDeclaration ~ VariableDeclaration`
- 相邻兄弟：`ArrayExpression > Literal + SpreadElement`
- 否定：`:not(ForStatement)`
- 匹配任意：`:matches([attr] > :first-child, :last-child)` 或 `:is([attr] > :first-child, :last-child)`
- AST 节点类别：`:statement`、`:expression`、`:declaration`、`:function` 或 `:pattern`

这套语法非常强大，可用于精确选择代码中的许多语法模式。

<sup>本节的示例改编自 [esquery](https://github.com/estools/esquery) 文档。</sup>

#### 选择器可以用于什么？（What can selectors be used for?）

如果你在编写自定义 ESLint 规则，你可能会有兴趣使用选择器来检查 AST 的特定部分。如果你在为代码库配置 ESLint，你可能会有兴趣用选择器限制特定的语法模式。

##### 在规则中监听选择器（Listening for selectors in rules）

编写自定义 ESLint 规则时，你可以在 AST 被遍历时监听匹配特定选择器的节点。

```js
module.exports = {
	create(context) {
		// ...

		return {
			// 此监听器会对所有带块语句的 IfStatement 节点调用。
			"IfStatement > BlockStatement": function (blockStatementNode) {
				// ...你的逻辑
			},

			// 此监听器会对所有参数超过 3 个的函数声明调用。
			"FunctionDeclaration[params.length>3]": function (
				functionDeclarationNode,
			) {
				// ...你的逻辑
			},
		};
	},
};
```

在选择器末尾添加 `:exit` 会使监听器在遍历期间退出匹配节点时调用，而不是在进入时调用。

如果两个或更多选择器匹配同一个节点，它们的监听器将按特异性（specificity）递增的顺序调用。AST 选择器的特异性类似于 CSS 选择器的特异性：

- 比较两个选择器时，包含更多类选择器、属性选择器和伪类选择器（不包括 `:not()`）的选择器特异性更高。
- 如果类/属性/伪类数量相同，则包含更多节点类型选择器的选择器特异性更高。

如果多个选择器特异性相同，它们的监听器将按该节点的字母顺序调用。

##### 用选择器限制语法（Restricting syntax with selectors）

使用 [no-restricted-syntax](https://eslint.org/docs/latest/rules/no-restricted-syntax) 规则，你可以限制代码中特定语法的使用。例如，你可以使用以下配置禁止函数体不是块语句的 `if` 语句：

```json
{
	"rules": {
		"no-restricted-syntax": [
			"error",
			"IfStatement > :not(BlockStatement).consequent"
		]
	}
}
```

……或者等价地，你可以使用这个配置：

```json
{
	"rules": {
		"no-restricted-syntax": [
			"error",
			"IfStatement[consequent.type!='BlockStatement']"
		]
	}
}
```

再举一个例子，你可以禁止调用 `require()`：

```json
{
	"rules": {
		"no-restricted-syntax": [
			"error",
			"CallExpression[callee.name='require']"
		]
	}
}
```

或者你可以强制要求 `setTimeout` 调用总是带两个参数：

```json
{
	"rules": {
		"no-restricted-syntax": [
			"error",
			"CallExpression[callee.name='setTimeout'][arguments.length!=2]"
		]
	}
}
```

在 `no-restricted-syntax` 规则中使用选择器，可以让你对代码库中有问题的模式进行大量控制，而无需编写自定义规则来检测每种模式。

##### 使用正则表达式（Using regular expressions）

你可以在属性选择器中使用正则表达式。例如，以下选择器匹配所有 `name` 值以 "foo" 开头的 `Identifier` 节点：

```text
Identifier[name=/^foo/]
```

你也可以在正则表达式中使用正斜杠。例如，以下配置禁止从 `some/path` 导入：

```json
{
	"rules": {
		"no-restricted-syntax": [
			"error",
			"ImportDeclaration[source.value=/^some\\/path$/]"
		]
	}
}
```

请注意，正则表达式中的 `/` 字符需要转义（`\/`），以免被解释为正则表达式的结束。此外，由于选择器位于 JSON 字符串内，反斜杠字符本身也需要转义（`\\`）。
