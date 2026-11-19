### 语言（Languages）

> 原文链接：https://eslint.org/docs/latest/extend/languages

从 ESLint v9.7.0 开始，你可以通过插件为 ESLint 扩展额外的语言。虽然 ESLint 最初只是一个严格针对 JavaScript 的 linter，但 ESLint 核心是通用的，可以用于检查任何编程语言。每种语言被定义为一个对象，包含检查文件所需的全部解析、求值和遍历功能。这些语言随后通过插件分发，供用户在配置中使用。

#### 语言要求（Language Requirements）

要创建一种语言，你需要：

1. **一个解析器（parser）。** 解析器是将纯文本转换为数据结构的部件。ESLint 对数据结构的格式没有特定要求，因此你可以使用任何已有的解析器，或自己编写。
1. **一个 `SourceCode` 对象。** ESLint 通过 `SourceCode` 对象来使用 AST。每个 `SourceCode` 有一些必须实现的方法，你也可以添加更多想暴露给规则的方法或属性。
1. **一个 `Language` 对象。** `Language` 对象包含语言本身的信息，以及解析和创建 `SourceCode` 对象的方法。

##### 语言的解析器要求（Parser Requirements for Languages）

首先，确保你有一个可以从 JavaScript 调用的解析器。解析器必须返回一个表示被解析代码的数据结构。大多数解析器返回[抽象语法树](https://en.wikipedia.org/wiki/Abstract_syntax_tree)（AST）来表示代码，但也可以返回[具体语法树](https://en.wikipedia.org/wiki/Parse_tree)（CST）。返回 AST 还是 CST 对 ESLint 来说无所谓，重要的是有一个可遍历的数据结构。

虽然 AST 或 CST 没有必须遵循的特定结构，但当树中的每个节点包含以下信息时，会更容易与 ESLint 集成：

1. **类型（Type）** - 每个节点上需要一个表示节点类型的属性。例如，在 JavaScript 中，`type` 属性为每个节点保存此信息。ESLint 规则使用节点类型来定义访问器（visitor）方法，因此每个节点都可以通过字符串标识很重要。属性名无所谓（下文进一步讨论），只要存在即可。大多数解析器通常将此属性命名为 `type` 或 `kind`。
1. **位置（Location）** - 每个节点上需要一个表示节点在原始源代码中位置的属性。位置必须包含：
    - 节点开始的行
    - 节点开始的列
    - 节点结束的行
    - 节点结束的列

    与节点类型一样，属性名无所谓。两个常见的属性名是 `loc`（如 [ESTree](https://github.com/estree/estree/blob/3851d4a6eae5e5473371893959b88b62007469e8/es5.md#node-objects)）和 `position`（如 [Unist](https://github.com/syntax-tree/unist?tab=readme-ov-file#node)）。ESLint 使用此信息来报告错误和规则违规。

1. **范围（Range）** - 每个节点上需要一个表示节点源码在源代码中位置的属性。范围指示第一个字符所在的索引和最后一个字符之后的索引，使得调用 `code.slice(start, end)` 返回该节点表示的文本。同样，不要求特定的属性名，此信息甚至可以与位置信息合并。ESTree 使用 `range` 属性，而 Unist 将此信息与位置信息一起包含在 `position` 上。ESLint 使用此信息来应用自动修复。

##### `SourceCode` 对象（The `SourceCode` Object）

ESLint 将源代码信息保存在 `SourceCode` 对象中。这个对象是 ESLint 内部和为处理代码而编写的规则（通过 `context.sourceCode`）共同使用的 API。`SourceCode` 对象必须实现 [`@eslint/core`](https://npmjs.com/package/@eslint/core) 包中定义的 `TextSourceCode` 接口。

一个基本的 `SourceCode` 对象必须实现以下内容：

- `ast` - 包含源代码的 AST 或 CST 的属性。
- `text` - 源代码的文本。
- `getLoc(nodeOrToken)` - 返回给定节点、token 或注释的位置的方法。返回值必须与 ESTree 使用的 `loc` 结构匹配。
- `getRange(nodeOrToken)` - 返回给定节点、token 或注释的范围的方法。必须返回一个数组，第一项是起始索引，第二项是结束索引。
- `traverse()` - 返回用于遍历 AST 或 CST 的可迭代对象的方法。迭代器必须返回实现 `@eslint/core` 中 `VisitTraversalStep` 或 `CallTraversalStep` 的对象。

以下可选成员允许你自定义 ESLint 与该对象的交互方式：

- `visitorKeys` - 特定于此 `SourceCode` 对象的访问器键。通常不需要，因为大多数时候使用 `Language#visitorKeys`。
- `applyLanguageOptions(languageOptions)` - 如果你有需要在解析后应用的特定语言选项，可以在此方法中应用。
- `getDisableDirectives()` - 返回代码中的任何禁用指令。ESLint 用它应用禁用指令并跟踪未使用的指令。
- `getInlineConfigNodes()` - 返回任何内联配置节点。当启用 `noInlineConfig` 时，ESLint 用它报告错误。
- `applyInlineConfig()` - 向 ESLint 返回内联配置元素。ESLint 用它改变被检查文件的配置。
- `finalize()` - 此方法在 lint 开始之前调用，是你修改 `SourceCode` 的最后机会。如果你定义了 `applyLanguageOptions()` 或 `applyInlineConfig()`，那么在 `SourceCode` 对象就绪之前，你可能还有额外的更改要应用。

此外，以下成员在 `SourceCode` 对象上很常见，建议实现：

- `lines` - 源代码的各行，字符串数组。
- `getParent(node)` - 返回给定节点的父节点，如果节点是根节点则返回 `undefined`。
- `getAncestors(node)` - 返回节点的祖先数组，第一项是树的根，后续各项是从根通往 `node` 的后代节点。
- `getText(node, beforeCount, afterCount)` - 返回表示给定节点、token 或注释的字符串，可选地包含其范围前后指定数量的字符。如果 `node` 被省略或为 `undefined`，则返回整个源文本。

基本 `SourceCode` 类的示例请参见 [`JSONSourceCode`](https://github.com/eslint/json/blob/main/src/languages/json-source-code.js)。

::: tip
[`@eslint/plugin-kit`](https://npmjs.com/package/@eslint/plugin-kit) 包包含多个旨在让创建 `SourceCode` 对象更容易的类。特别是 `TextSourceCodeBase` 类，它实现了 `TextSourceCode` 接口，并提供了 `SourceCode` 对象中常见的一些基础功能。
:::

##### `Language` 对象（The `Language` Object）

`Language` 对象包含关于编程语言的所有信息，以及与用该语言编写的代码交互的方法。ESLint 使用此对象来确定如何处理特定文件。`Language` 对象必须实现 [`@eslint/core`](https://npmjs.com/package/@eslint/core) 包中定义的 `Language` 接口。

一个基本的 `Language` 对象必须实现以下内容：

- `fileType` - 应为 `"text"`（未来我们还将支持 `"binary"`）
- `lineStart` - 0 或 1，指示 AST 如何表示文件的第一行。ESLint 用它正确显示错误位置。
- `columnStart` - 0 或 1，指示 AST 如何表示每行的第一列。ESLint 用它正确显示错误位置。
- `nodeTypeKey` - 指示节点类型的属性名（通常是 `"type"` 或 `"kind"`）。
- `validateLanguageOptions(languageOptions)` - 校验该语言的语言选项。当某个预期的语言选项类型或值不正确时，此方法应抛出校验错误。非预期的语言选项应被静默忽略，不应抛出错误。即使语言没有指定任何选项，此方法也是必需的。
- `parse(file, context)` - 将给定文件解析为 AST 或 CST，还可以包含供规则使用的额外值。由 ESLint 内部调用。
- `createSourceCode(file, parseResult, context)` - 创建 `SourceCode` 对象。由 ESLint 在 `parse()` 之后内部调用，第二个参数就是 `parse()` 的确切返回值。

以下可选成员允许你自定义 ESLint 与该对象的交互方式：

- `visitorKeys` - 特定于该 AST 或 CST 的访问器键。用于优化 ESLint 内部对 AST 或 CST 的遍历。虽然不是必需的，但强烈建议提供，尤其是对于与 ESTree 格式差异较大的 AST 或 CST 格式。
- `defaultLanguageOptions` - 使用该语言时的默认 `languageOptions`。在计算被检查文件的配置时，用户指定的 `languageOptions` 会与此对象合并。
- `matchesSelectorClass(className, node, ancestry)` - 允许你指定匹配多个节点的选择器类，例如 `:expression`。每当 [esquery](https://github.com/estools/esquery) 选择器包含 `:` 后跟标识符时，就会调用此方法。
- `normalizeLanguageOptions(languageOptions)` - 接收已校验的语言选项对象并规范化其值。这有助于在语言选项属性变化时保持向后兼容，也可以通过 `toJSON()` 方法添加自定义序列化。

基本 `Language` 类的示例请参见 [`JSONLanguage`](https://github.com/eslint/json/blob/main/src/languages/json-language.js)。

#### 在插件中发布语言（Publish a Language in a Plugin）

语言的发布方式与处理器和规则类似，都在插件中定义。在插件中定义 `languages` 键，它是一个对象，键是语言名称，值是语言对象。下面是一个示例：

```js
import { myLanguage } from "../languages/my.js";

const plugin = {
	// name 和 version 的首选位置
	meta: {
		name: "eslint-plugin-example",
		version: "1.2.3",
	},
	languages: {
		my: myLanguage,
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

要在配置文件中使用插件中的语言，导入该插件并将其包含在 `plugins` 键中并指定命名空间。然后，使用该命名空间在 `language` 配置中引用语言，如下所示：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";
import example from "eslint-plugin-example";

export default defineConfig([
	{
		files: ["**/*.my"],
		plugins: {
			example,
		},
		language: "example/my",
	},
]);
```

更多细节请参见插件配置文档中的[指定语言（Specify a Language）](https://eslint.org/docs/latest/use/configure/plugins#specifying-a-language)。
