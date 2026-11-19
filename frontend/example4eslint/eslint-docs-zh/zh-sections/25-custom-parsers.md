## 自定义解析器（Custom Parsers）

> 原文链接：https://eslint.org/docs/latest/extend/custom-parsers

ESLint 自定义解析器让你扩展 ESLint，支持对代码中新的非标准 JavaScript 语言特性或自定义语法进行 lint。解析器负责接收你的代码，并将其转换为 ESLint 可以分析和检查的抽象语法树（AST）。

### 创建自定义解析器（Create a Custom Parser）

#### 自定义解析器中的方法（Methods in Custom Parsers）

自定义解析器是一个带有 `parse()` 或 `parseForESLint()` 方法的 JavaScript 对象。`parse` 方法只返回 AST，而 `parseForESLint()` 还会返回额外的值，让解析器能更进一步定制 ESLint 的行为。

这两个方法都应该是实例（自有）属性，接收源代码作为第一个参数，以及一个可选的配置对象作为第二个参数——该配置对象就是配置文件中提供的 [`parserOptions`](https://eslint.org/docs/latest/use/configure/language-options#specifying-parser-options)。

```javascript
// customParser.js

const espree = require("espree");

// 记录解析每个文件所花的时长。
function parse(code, options) {
	const label = `Parsing file "${options.filePath}"`;
	console.time(label);
	const ast = espree.parse(code, options);
	console.timeEnd(label);
	return ast; // 只返回 AST。
}

module.exports = { parse };
```

#### `parse` 返回对象（`parse` Return Object）

`parse` 方法只需返回 [AST](#ast-规范ast-specification) 对象。

#### `parseForESLint` 返回对象（`parseForESLint` Return Object）

`parseForESLint` 方法应返回一个对象，包含必需的 `ast` 属性，以及可选的 `services`、`scopeManager` 和 `visitorKeys` 属性。

- `ast` 应包含 [AST](#ast-规范ast-specification) 对象。
- `services` 可以包含任何依赖于解析器的服务（例如节点的类型检查器）。`services` 属性的值可通过 `context.sourceCode.parserServices` 供规则使用。默认为空对象。
- `scopeManager` 可以是一个 [ScopeManager](https://eslint.org/docs/latest/extend/scope-manager-interface) 对象。自定义解析器可以为实验性/增强语法使用定制的作用域分析。默认值是由 [eslint-scope](https://github.com/eslint/js/tree/main/packages/eslint-scope) 创建的 `ScopeManager` 对象。
    - ESLint v4.14.0 添加了对 `scopeManager` 的支持。支持 `scopeManager` 的 ESLint 版本会在 `parserOptions` 中提供 `eslintScopeManager: true` 属性，可用于特性检测。
    - 自 ESLint v10.0.0 起，`ScopeManager` 必须自动解析代码中声明的全局变量的引用，并提供一个实例方法 `addGlobals(names: string[])`，在全局作用域中创建给定名称的变量并解析对它们的引用。
- `visitorKeys` 可以是一个用于自定义 AST 遍历的对象。对象的键是 AST 节点的类型。每个值是应被遍历的属性名数组。默认值是 [`eslint-visitor-keys` 的 KEYS](https://github.com/eslint/js/tree/main/packages/eslint-visitor-keys#evkkeys)。
    - ESLint v4.14.0 添加了对 `visitorKeys` 的支持。支持 `visitorKeys` 的 ESLint 版本会在 `parserOptions` 中提供 `eslintVisitorKeys: true` 属性，可用于特性检测。

#### 自定义解析器中的元数据（Meta Data in Custom Parsers）

为了更方便地调试和更有效地缓存自定义解析器，建议在自定义解析器的根级 `meta` 对象中提供名称和版本，如下所示：

```js
// name 和 version 的首选位置
module.exports = {
	meta: {
		name: "eslint-parser-custom",
		version: "1.2.3",
	},
};
```

`meta.name` 属性应与你的自定义解析器的 npm 包名一致，`meta.version` 属性应与你的自定义解析器的 npm 包版本一致。最简单的做法是从你的 `package.json` 中读取这些信息。

### AST 规范（AST Specification）

自定义解析器应创建的 AST 基于 [ESTree](https://github.com/estree/estree)。该 AST 还需要一些关于源代码细节信息的额外属性。

#### 所有节点（All Nodes）

所有节点必须有 `range` 属性。

- `range`（`number[]`）是一个由两个数字组成的数组。两个数字都是从 0 开始的索引，表示在源代码字符数组中的位置。第一个是节点的起始位置，第二个是节点的结束位置。`code.slice(node.range[0], node.range[1])` 必须是该节点的文本。此范围不包括节点周围的空格/括号。
- `loc`（`SourceLocation`）不得为 `null`。[ESTree 将 `loc` 属性定义为可空](https://github.com/estree/estree/blob/25834f7247d44d3156030f8e8a2d07644d771fdb/es5.md#node-objects)，但 ESLint 要求此属性必须存在。`SourceLocation#source` 属性可以是 `undefined`，ESLint 不使用 `SourceLocation#source` 属性。

所有节点的 `parent` 属性必须是可重写的。在任何规则访问 AST 之前，ESLint 会在遍历时将每个节点的 `parent` 属性设置为其父节点。

#### `Program` 节点（The `Program` Node）

`Program` 节点必须有 `tokens` 和 `comments` 属性。这两个属性都是下述 `Token` 接口的数组。

```ts
interface Token {
	type: string;
	loc: SourceLocation;
	// 关于 `range` 属性的细节，请参见“所有节点”一节。
	range: [number, number];
	value: string;
}
```

- `tokens`（`Token[]`）是影响程序行为的 token 数组。token 之间可以存在任意空格，因此规则会检查 `Token#range` 来检测 token 之间的空格。此数组必须按 `Token#range[0]` 排序。
- `comments`（`Token[]`）是注释 token 的数组。此数组必须按 `Token#range[0]` 排序。

所有 token 和注释的范围索引不得与其他 token 和注释的范围重叠。

#### `Literal` 节点（The `Literal` Node）

`Literal` 节点必须有 `raw` 属性。

- `raw`（`string`）是此字面量的源代码。它与 `code.slice(node.range[0], node.range[1])` 相同。

### 打包自定义解析器（Packaging a Custom Parser）

要将你的自定义解析器发布到 npm，请执行以下操作：

1. 按照上面[创建自定义解析器](#创建自定义解析器create-a-custom-parser)一节创建自定义解析器。
1. 为自定义解析器[创建一个 npm 包](https://docs.npmjs.com/creating-node-js-modules)。
1. 在你的 `package.json` 文件中，将 [`main`](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#main) 字段设置为导出你的自定义解析器的文件。
1. [发布该 npm 包](https://docs.npmjs.com/creating-and-publishing-unscoped-public-packages)。

有关发布 npm 包的更多信息，请参阅 [npm 文档](https://docs.npmjs.com/)。

发布 npm 包后，你可以通过将该包添加到项目中来使用它。例如：

```shell
npm install --save-dev eslint-parser-myparser
```

然后使用 `languageOptions.parser` 属性将自定义解析器添加到你的 ESLint 配置文件中。例如：

```js
// eslint.config.js

const { defineConfig } = require("eslint/config");
const myparser = require("eslint-parser-myparser");

module.exports = defineConfig([
	{
		languageOptions: {
			parser: myparser,
		},
		// ...其余配置
	},
]);
```

使用旧版配置时，将 `parser` 属性指定为字符串：

```js
// .eslintrc.js

module.exports = {
	parser: "eslint-parser-myparser",
	// ...其余配置
};
```

要了解更多关于在项目中使用 ESLint 解析器的信息，请参阅[配置解析器（Configure a Parser）](https://eslint.org/docs/latest/use/configure/parser)。

### 示例（Example）

自定义解析器的复杂示例，请参阅 [`@typescript-eslint/parser`](https://github.com/typescript-eslint/typescript-eslint/tree/main/packages/parser) 的源代码。

一个简单的自定义解析器，向规则提供 `context.sourceCode.parserServices.foo()` 方法：

```javascript
// awesome-custom-parser.js
var espree = require("espree");
function parseForESLint(code, options) {
	return {
		ast: espree.parse(code, options),
		services: {
			foo: function () {
				console.log("foo");
			},
		},
		scopeManager: null,
		visitorKeys: null,
	};
}

module.exports = { parseForESLint };
```

在 ESLint 配置文件中包含该自定义解析器：

```js
// eslint.config.js
const { defineConfig } = require("eslint/config");

module.exports = defineConfig([
	{
		languageOptions: {
			parser: require("./path/to/awesome-custom-parser"),
		},
	},
]);
```

或者使用旧版配置：

```js
// .eslintrc.json
{
    "parser": "./path/to/awesome-custom-parser.js"
}
```
