### 自定义规则（Custom Rules）

> 原文链接：https://eslint.org/docs/latest/extend/custom-rules

你可以创建自定义规则与 ESLint 一起使用。如果[核心规则](https://eslint.org/docs/latest/rules/)没有覆盖你的用例，你可能需要创建自定义规则。

下面是自定义规则的基本格式：

```js
// customRule.js

module.exports = {
	meta: {
		type: "suggestion",
		docs: {
			description: "Description of the rule",
		},
		fixable: "code",
		schema: [], // 没有选项
	},
	create: function (context) {
		return {
			// 回调函数
		};
	},
};
```

::: warning
`eslint` 包中附带的核心规则不被视为公共 API 的一部分，也不是为被继承而设计的。在这些规则之上构建是脆弱的，很可能在未来的某个时刻导致你的规则完全失效。如果你有兴趣创建与核心规则类似的规则，你应该先将该规则文件复制到你的项目中，然后再在此基础上继续。
:::

#### 规则结构（Rule Structure）

规则的源文件导出一个具有以下属性的对象。自定义规则和核心规则都遵循此格式。

`meta`：（`object`）包含规则的元数据：

- `type`：（`string`）指示规则的类型，为 `"problem"`、`"suggestion"` 或 `"layout"` 之一：
    - `"problem"`：该规则识别的是会导致错误或可能导致混乱行为的代码。开发者应将其视为高优先级来解决。
    - `"suggestion"`：该规则识别的是可以用更好方式完成的事情，但即使代码不更改也不会发生错误。
    - `"layout"`：该规则主要关注空白、分号、逗号和括号——程序中所有决定代码外观而非执行方式的部分。这些规则处理的是 AST 中未指定的代码部分。

    规则类型可以与 CLI 选项 [`--fix-type`](https://eslint.org/docs/latest/use/command-line-interface#--fix-type) 一起使用，以指定要应用的修复类型。

- `docs`：（`object`）常用于文档生成和工具的属性。核心规则必需，自定义规则可选。自定义规则可以根据需要在此包含额外的属性。
    - `description`：（`string`）提供规则的简短描述。对于核心规则，它用于[规则索引](https://eslint.org/docs/latest/rules/)。
    - `dialects`：（`string[]`）该规则旨在检查的语言方言。例如 `["JavaScript", "TypeScript"]`。
    - `frozen`：（`boolean`）对于核心规则，指定该规则是否已[冻结](https://eslint.org/docs/latest/contribute/core-rules#frozen-rules)且不再接受功能请求。
    - `recommended`：（`unknown`）对于核心规则，这是一个布尔值，指定该规则是否被 `@eslint/js` 的 `recommended` 配置启用。
    - `url`：（`string`）指定可以访问完整文档的 URL。代码编辑器常用它在高亮的规则违规上提供有用的链接。

- `messages`：（`object`）包含该规则的违规消息和建议消息的对象。核心规则必需，自定义规则可选。消息可以在 `context.report()` 调用中通过它们的键（`messageId`）引用。更多信息请参见 [`messageId`](#messageids)。
- `fixable`：（`string`）如果[命令行](https://eslint.org/docs/latest/use/command-line-interface#--fix)上的 `--fix` 选项可以自动修复该规则报告的问题，则为 `"code"` 或 `"whitespace"`。

    **重要：** 对于可修复的规则，`fixable` 属性是必需的。如果未指定此属性，每当规则尝试产生修复时，ESLint 都会抛出错误。如果规则不可修复，请省略 `fixable` 属性。

- `hasSuggestions`：（`boolean`）指定规则是否可以返回建议（如果省略则默认为 `false`）。

    **重要：** 对于提供建议的规则，`hasSuggestions` 属性是必需的。如果此属性未设置为 `true`，每当规则尝试产生建议时，ESLint 都会抛出错误。如果规则不提供建议，请省略 `hasSuggestions` 属性。

- `schema`：（`object | array | false`）指定[选项](#选项模式options-schemas)，以便 ESLint 可以阻止无效的[规则配置](https://eslint.org/docs/latest/use/configure/rules)。当规则有选项时是必需的。

- `defaultOptions`：（`array`）指定规则的[默认选项](#选项默认值option-defaults)。如果存在，用户配置中提供的任何选项都将递归地合并到它们之上。

- `languages`：（`array`）指定该规则设计用于处理的语言。每个条目是 `"pluginName/languageName"` 格式的字符串（例如 `"js/js"`、`"markdown/gfm"`）。特殊值：
    - `"*"` —— 该规则适用于任何语言。
    - `"pluginName/*"` —— 该规则适用于给定插件提供的任何语言。

    如果未指定 `languages`，则假定该规则适用于所有语言。当指定了 `languages` 且没有任何条目匹配当前语言时，ESLint 会抛出错误。

    ```js
    // 规则仅在当前语言为内置 JavaScript 语言时运行
    meta: {
        languages: ["js/js"],
    }

    // 规则可运行于 "markdown" 插件的任何语言
    meta: {
        languages: ["markdown/*"],
    }

    // 规则可运行于任何语言
    meta: {
        languages: ["*"],
    }
    ```

    语言标识符按以下顺序匹配：
    1. **通配符 `"*"`** —— 匹配任何语言。
    2. **直接字符串匹配** —— 条目与当前语言字符串完全相等（例如 `"test/lang"`）。
    3. **`"plugin/*"` 通配符** —— 匹配插件名（或 `meta.namespace`）等于条目插件部分的任何语言。
    4. **命名空间匹配** —— 当以不同名称注册的插件的 `meta.namespace` 等于条目的插件部分时，也可以匹配。

    ```js
    // 规则运行于 meta.namespace 为 "markdown" 的插件的特定语言
    //（例如，插件注册为 "md"，但声明了 meta.namespace = "markdown"）
    meta: {
        languages: ["markdown/gfm"],
    }
    ```

- `deprecated`：（`boolean | DeprecatedInfo`）指示该规则是否已废弃。如果规则未被废弃，可以省略 `deprecated` 属性。
  有一个专门的页面介绍 [DeprecatedInfo](https://eslint.org/docs/latest/extend/rule-deprecation)。

- `replacedBy`：（`array`，**已废弃**，请改用 `meta.deprecated.replacedBy`。）对于已废弃的规则，指定替代规则。

`create()`：返回一个对象，其方法由 ESLint 在遍历 JavaScript 代码的抽象语法树（由 [ESTree](https://github.com/estree/estree) 定义的 AST）时调用以"访问"节点：

- 如果键是节点类型或[选择器](https://eslint.org/docs/latest/extend/selectors)，ESLint 会在**向下**遍历树时调用该**访问器（visitor）**函数。
- 如果键是节点类型或[选择器](https://eslint.org/docs/latest/extend/selectors)加上 `:exit`，ESLint 会在**向上**遍历树时调用该**访问器**函数。
- 如果键是事件名称，ESLint 会为[代码路径分析](https://eslint.org/docs/latest/extend/code-path-analysis)调用该**处理器（handler）**函数。

规则可以使用当前节点及其周围的树来报告或修复问题。

下面是 [array-callback-return](https://eslint.org/docs/latest/rules/array-callback-return) 规则的方法：

```js
function checkLastSegment (node) {
    // 如果最后的代码路径段可达，则为函数报告问题
}

module.exports = {
    meta: { ... },
    create: function(context) {
        // 声明规则的状态
        return {
            ReturnStatement: function(node) {
                // 向下遍历时位于 ReturnStatement 节点
            },
            // 向上遍历时位于函数表达式节点：
            "FunctionExpression:exit": checkLastSegment,
            "ArrowFunctionExpression:exit": checkLastSegment,
            onCodePathStart: function (codePath, node) {
                // 开始分析代码路径时
            },
            onCodePathEnd: function(codePath, node) {
                // 结束分析代码路径时
            }
        };
    }
};
```

::: tip
你可以使用 [Code Explorer](https://explorer.eslint.org) 查看任何 JavaScript 代码的完整 AST。
:::

#### Context 对象（The Context Object）

`context` 对象是规则中 `create` 方法的唯一参数。例如：

```js
// customRule.js

module.exports = {
    meta: { ... },
    // `context` 对象就是参数
    create(context) {
       // ...
    }
};
```

顾名思义，`context` 对象包含与规则上下文相关的信息。

`context` 对象具有以下属性：

- `id`：（`string`）规则 ID。
- `filename`：（`string`）与源代码关联的文件名。
- `physicalFilename`：（`string`）检查文件时，它提供文件在磁盘上的完整路径，不含任何代码块信息。检查文本时，它提供传给 `--stdin-filename` 的值，未指定时为 `<text>`。
- `cwd`：（`string`）传给 [Linter](https://eslint.org/docs/latest/integrate/nodejs-api#linter) 的 `cwd` 选项。它是应被视为当前工作目录的目录路径。
- `options`：（`array`）此规则[已配置选项](https://eslint.org/docs/latest/use/configure/rules)的数组。此数组不包含规则严重级别（见[专门一节](#访问传递给规则的选项accessing-options-passed-to-a-rule)）。
- `sourceCode`：（`object`）一个 `SourceCode` 对象，可用于处理传给 ESLint 的源代码（见[访问源代码](#访问源代码accessing-the-source-code)）。
- `settings`：（`object`）来自配置的[共享设置](https://eslint.org/docs/latest/use/configure/configuration-files#configuring-shared-settings)。
- `languageOptions`：（`object`）[每个属性的更多细节](https://eslint.org/docs/latest/use/configure/language-options)
    - `sourceType`：（`'script' | 'module' | 'commonjs'`）当前文件的模式。
    - `ecmaVersion`：（`number`）用于解析当前文件的 ECMA 版本。
    - `parser`：（`object`）用于解析当前文件的解析器。
    - `parserOptions`：（`object`）为此文件配置的解析器选项。
    - `globals`：（`object`）指定的全局变量。

此外，`context` 对象还有以下方法：

- `report(descriptor)`。报告代码中的问题（见[专门一节](#报告问题report-problems)）。

**注意：** ESLint 的早期版本在 `context` 对象上支持额外的方法。这些方法已在新格式中移除，不应再依赖。

##### 报告问题（Report Problems）

编写自定义规则时使用的主要方法是 `context.report()`，它发布一个警告或错误（取决于使用的配置）。此方法接受单个参数，即一个包含以下属性的对象：

- `messageId`：（`string`）消息的 ID（见 [messageIds](#messageids)）（推荐，优于 `message`）。
- `message`：（`string`）问题消息（`messageId` 的替代方案）。
- `node`：（可选 `object`）可以是与问题相关的 AST 节点、token 或注释。如果存在且未指定 `loc`，则使用节点的起始位置作为问题的位置。
- `loc`：（可选 `object`）指定问题的位置。如果同时指定了 `loc` 和 `node`，则使用 `loc` 中的位置而不是 `node`。
    - `start`：起始位置的对象。
        - `line`：（`number`）问题发生的 1 起始行号。
        - `column`：（`number`）问题发生的 0 起始列号。
    - `end`：结束位置的对象。
        - `line`：（`number`）问题发生的 1 起始行号。
        - `column`：（`number`）问题发生的 0 起始列号。
- `data`：（可选 `object`）`message` 的[占位符](#使用消息占位符use-message-placeholders)数据。
- `fix(fixer)`：（可选 `function`）应用[修复](#应用修复applying-fixes)来解决问题。

请注意，`node` 或 `loc` 至少需要提供一个。

最简单的示例是只使用 `node` 和 `message`：

```js
context.report({
	node: node,
	message: "Unexpected identifier",
});
```

节点包含了确定违规文本的行号和列号以及表示该节点的源文本所需的全部信息。

###### 使用消息占位符（Use Message Placeholders）

你也可以在消息中使用占位符并提供 `data`：

```js
context.report({
    node: node,
    message: "Unexpected identifier: {{ identifier }}",
    data: {
        identifier: node.name
    }
});
```

请注意，消息参数中的首尾空白是可选的。

节点包含了确定违规文本的行号和列号以及表示该节点的源文本所需的全部信息。

###### `messageId`（`messageId`s）

`messageId` 是在 `context.report()` 调用中报告消息的推荐方式，因为它有以下好处：

- 规则违规消息可以集中存储在 `meta.messages` 对象中，便于管理。
- 规则违规消息不需要在规则文件和规则测试文件中重复。
- 因此，修改规则违规消息的门槛更低，鼓励更频繁地贡献以改进和优化它们，使其最清晰、最有用。

规则文件：

```js
// avoid-name.js

module.exports = {
    meta: {
        messages: {
            avoidName: "Avoid using variables named '{{ name }}'"
        }
    },
    create(context) {
        return {
            Identifier(node) {
                if (node.name === "foo") {
                    context.report({
                        node,
                        messageId: "avoidName",
                        data: {
                            name: "foo",
                        }
                    });
                }
            }
        };
    }
};
```

被检查的文件：

```javascript
// someFile.js

var foo = 2;
//  ^ 错误：Avoid using variables named 'foo'
```

在你的测试中：

```javascript
// avoid-name.test.js

var rule = require("../../../lib/rules/avoid-name");
var RuleTester = require("eslint").RuleTester;

var ruleTester = new RuleTester();
ruleTester.run("avoid-name", rule, {
	valid: ["bar", "baz"],
	invalid: [
		{
			code: "foo",
			errors: [
				{
					messageId: "avoidName",
				},
			],
		},
	],
});
```

###### 应用修复（Applying Fixes）

如果你希望 ESLint 尝试修复你报告的问题，可以在使用 `context.report()` 时指定 `fix` 函数。`fix` 函数接收单个参数——一个 `fixer` 对象，你可以用它应用修复。例如：

```js
context.report({
	node: node,
	message: "Missing semicolon",
	fix(fixer) {
		return fixer.insertTextAfter(node, ";");
	},
});
```

这里，`fix()` 函数用于在节点后插入一个分号。请注意，修复不会立即应用，并且如果与其他修复冲突，可能根本不会应用。应用修复后，ESLint 会在修复后的代码上再次运行所有已启用的规则，可能应用更多修复。此过程最多重复 10 次，或直到不再发现可修复的问题。之后，任何剩余的问题将照常报告。

**重要：** 对于可修复的规则，`meta.fixable` 属性是必需的。如果实现了 `fix` 函数的规则没有[导出](#规则结构rule-structure) `meta.fixable` 属性，ESLint 会抛出错误。

`fixer` 对象有以下方法：

- `insertTextAfter(nodeOrToken, text)`：在给定节点或 token 之后插入文本。
- `insertTextAfterRange(range, text)`：在给定范围之后插入文本。
- `insertTextBefore(nodeOrToken, text)`：在给定节点或 token 之前插入文本。
- `insertTextBeforeRange(range, text)`：在给定范围之前插入文本。
- `remove(nodeOrToken)`：移除给定节点或 token。
- `removeRange(range)`：移除给定范围内的文本。
- `replaceText(nodeOrToken, text)`：替换给定节点或 token 中的文本。
- `replaceTextRange(range, text)`：替换给定范围内的文本。

`range` 是一个包含源代码中字符索引的两项数组。第一项是范围的开始（含），第二项是范围的结束（不含）。每个节点和 token 都有一个 `range` 属性，用于标识它们所表示的源代码范围。

上述方法返回一个 `fixing` 对象。
`fix()` 函数可以返回以下值：

- 一个 `fixing` 对象。
- 一个包含 `fixing` 对象的数组。
- 一个枚举 `fixing` 对象的可迭代对象。特别地，`fix()` 函数可以是生成器。

如果你编写的 `fix()` 函数返回多个 `fixing` 对象，这些 `fixing` 对象不得重叠。

修复的最佳实践：

1. 避免任何可能改变代码运行时行为并导致其停止工作的修复。
1. 让修复尽可能小。不必要的大修复可能与其他修复冲突，并阻止它们被应用。
1. 每条消息只做一个修复。这是强制执行的，因为你必须从 `fix()` 返回 fixer 操作的结果。
1. 由于在应用第一轮修复后会再次运行所有规则，因此规则没有必要检查修复的代码风格是否会导致另一条规则报告错误。
    - 例如，假设一个 fixer 想给对象键加上引号，但不确定用户更喜欢单引号还是双引号。

        ```js
        { foo: 1 }

        // 应该被修复为
        { 'foo': 1 }

        // 或
        { "foo": 1 }
        ```

    - 这个 fixer 可以随意选择一种引号类型。如果猜错了，生成的代码将被 [`quotes`](https://eslint.org/docs/latest/rules/quotes) 规则自动报告并修复。

注意：让修复尽可能小是最佳实践，但在某些情况下，扩展修复范围可能是正确的，以便有意阻止其他规则在同一轮中对周围范围进行修复。例如，如果替换文本声明了一个新变量，那么阻止该变量作用域内的其他更改可能很有用，因为它们可能导致名称冲突。

下面的示例替换 `node`，同时确保在同一轮中不会在 `node.parent` 范围内应用其他修复：

```js
context.report({
	node,
	message,
	*fix(fixer) {
		yield fixer.replaceText(node, replacementText);

		// 将修复范围扩展到 `node.parent` 的范围
		yield fixer.insertTextBefore(node.parent, "");
		yield fixer.insertTextAfter(node.parent, "");
	},
});
```

###### 冲突的修复（Conflicting Fixes）

冲突的修复是指对源代码同一部分应用不同更改的修复。
无法指定应用哪个冲突的修复。

例如，如果两个修复都想修改第 0 到 5 个字符，则只应用其中一个。

###### 提供建议（Providing Suggestions）

在某些情况下，修复不适合自动应用——例如，修复可能改变功能，或者根据实现意图有多种有效的修复方式（见上文[应用修复](#应用修复applying-fixes)的最佳实践）。在这些情况下，`context.report()` 上有一个替代的 `suggest` 选项，允许其他工具（如编辑器）为用户提供手动应用建议的辅助功能。

要提供建议，在 report 参数中使用 `suggest` 键，其值为建议对象的数组。建议对象代表可以应用的单个建议，需要一个描述应用建议会做什么的 `desc` 键字符串或 `messageId` 键（见[下文](#建议-messageidssuggestion-messageids)），以及一个定义建议结果的 `fix` 键函数。此 `fix` 函数遵循与常规修复相同的 API（在上文[应用修复](#应用修复applying-fixes)中介绍）。

```js
context.report({
    node: node,
    message: "Unnecessary escape character: \\{{character}}.",
    data: { character },
    suggest: [
        {
            desc: "Remove the `\\`. This maintains the current functionality.",
            fix: function(fixer) {
                return fixer.removeRange(range);
            }
        },
        {
            desc: "Replace the `\\` with `\\\\` to include the actual backslash character.",
            fix: function(fixer) {
                return fixer.insertTextBeforeRange(range, "\\");
            }
        }
    ]
});
```

**重要：** 对于提供建议的规则，`meta.hasSuggestions` 属性是必需的。如果规则尝试产生建议但没有[导出](#规则结构rule-structure)此属性，ESLint 会抛出错误。

**注意：** 建议作为独立的更改应用，不会触发多轮修复。每个建议应专注于代码中的单一更改，不应试图符合用户定义的风格。例如，如果建议在代码库中添加新语句，它不应试图匹配正确的缩进或符合用户对分号有无的偏好。所有这些都可以由用户触发时的多轮自动修复来纠正。

建议的最佳实践：

1. 不要试图做太多事情，建议可能引入大量破坏性变更的大型重构。
1. 如上所述，不要试图符合用户定义的风格。

建议旨在提供修复。如果建议的 `fix` 函数返回 `null` 或空数组/序列，ESLint 会自动从 lint 输出中移除整个建议。

###### 建议 `messageId`（Suggestion `messageId`s）

建议可以使用 `messageId` 代替 `desc` 键。这与整体错误的 `messageId` 工作方式相同（见 [messageIds](#messageids)。下面是如何在规则中使用建议 `messageId` 的示例：

```js
module.exports = {
    meta: {
        messages: {
            unnecessaryEscape: "Unnecessary escape character: \\{{character}}.",
            removeEscape: "Remove the `\\`. This maintains the current functionality.",
            escapeBackslash: "Replace the `\\` with `\\\\` to include the actual backslash character."
        },
        hasSuggestions: true
    },
    create: function(context) {
        // ...
        context.report({
            node: node,
            messageId: 'unnecessaryEscape',
            data: { character },
            suggest: [
                {
                    messageId: "removeEscape", // 建议 messageId
                    fix: function(fixer) {
                        return fixer.removeRange(range);
                    }
                },
                {
                    messageId: "escapeBackslash", // 建议 messageId
                    fix: function(fixer) {
                        return fixer.insertTextBeforeRange(range, "\\");
                    }
                }
            ]
        });
    }
};
```

###### 建议消息中的占位符（Placeholders in Suggestion Messages）

你也可以在建议消息中使用占位符。这与整体错误的占位符工作方式相同（见[使用消息占位符](#使用消息占位符use-message-placeholders)）。

请注意，你必须在建议的对象上提供 `data`。建议消息不能使用整体错误的 `data` 中的属性。

```js
module.exports = {
    meta: {
        messages: {
            unnecessaryEscape: "Unnecessary escape character: \\{{character}}.",
            removeEscape: "Remove `\\` before {{character}}.",
        },
        hasSuggestions: true
    },
    create: function(context) {
        // ...
        context.report({
            node: node,
            messageId: "unnecessaryEscape",
            data: { character }, // unnecessaryEscape 整体消息的 data
            suggest: [
                {
                    messageId: "removeEscape",
                    data: { character }, // removeEscape 建议消息的 data
                    fix: function(fixer) {
                        return fixer.removeRange(range);
                    }
                }
            ]
        });
    }
};
```

##### 访问传递给规则的选项（Accessing Options Passed to a Rule）

有些规则需要选项才能正常工作。这些选项出现在配置中（`.eslintrc`、命令行接口或注释）。例如：

```json
{
	"quotes": ["error", "double"]
}
```

本例中的 `quotes` 规则有一个选项 `"double"`（`error` 是错误级别）。你可以使用 `context.options` 检索规则的选项，它是一个包含规则每个已配置选项的数组。在本例中，`context.options[0]` 将包含 `"double"`：

```js
module.exports = {
	meta: {
		schema: [
			{
				enum: ["single", "double", "backtick"],
			},
		],
	},
	create: function (context) {
		var isDouble = context.options[0] === "double";

		// ...
	},
};
```

由于 `context.options` 只是一个数组，你可以用它来确定传入了多少选项，以及检索实际的选项本身。请记住，错误级别不是 `context.options` 的一部分，因为错误级别无法从规则内部获知或修改。

使用选项时，请确保你的规则在未提供选项的情况下有一些合理的默认值。

带选项的规则必须指定 [schema](#选项模式options-schemas)。

###### 避免不可序列化的选项（Avoid Non-Serializable Options）

规则选项和[共享设置](https://eslint.org/docs/latest/use/configure/configuration-files#configuring-shared-settings)应只包含可以序列化为 JSON 的值，例如字符串、数字、布尔值、`null`、数组和普通对象。避免设计期望在 `context.options` 或 `context.settings` 中包含不可序列化值（如正则表达式、函数或类实例）的规则。

某些 ESLint 功能与不可序列化的值配合不佳。例如，当启用[缓存](https://eslint.org/docs/latest/use/command-line-interface#caching)时，如果更新后的值产生相同的序列化配置，ESLint 可能无法检测到变化，因此可能继续报告过时的结果。如果你的规则需要正则表达式，请改为以字符串选项接受该模式，并在规则内部创建 `RegExp` 对象。

##### 访问源代码（Accessing the Source Code）

`SourceCode` 对象是获取有关被检查源代码更多信息的主要对象。你可以随时使用 `context.sourceCode` 属性检索 `SourceCode` 对象：

```js
module.exports = {
	create: function (context) {
		var sourceCode = context.sourceCode;

		// ...
	},
};
```

获得 `SourceCode` 实例后，你可以使用以下方法处理代码：

- `getText(node, beforeCount, afterCount)`：返回给定节点、token 或注释的源代码，可选地包含其范围前后指定数量的字符。如果 `node` 被省略或为 `undefined`，则返回整个源文本（见[专门一节](#访问源文本accessing-the-source-text)）。
- `getAllComments()`：返回源代码中所有注释的数组（见[专门一节](#访问注释accessing-comments)）。
- `getCommentsBefore(nodeOrToken)`：返回紧邻给定节点或 token 之前的注释 token 数组（见[专门一节](#访问注释accessing-comments)）。
- `getCommentsAfter(nodeOrToken)`：返回紧邻给定节点或 token 之后的注释 token 数组（见[专门一节](#访问注释accessing-comments)）。
- `getCommentsInside(node)`：返回给定节点内所有注释 token 的数组（见[专门一节](#访问注释accessing-comments)）。
- `isSpaceBetween(nodeOrToken, nodeOrToken)`：如果两个 token 之间存在空白字符，则返回 true；如果给定的是节点，则检查第一个节点的最后一个 token 和第二个节点的第一个 token 之间。
- `isGlobalReference(node)`：如果标识符引用的是通过 `languageOptions.globals`、`/* global */` 注释或 `ecmaVersion` 配置的全局变量，而不是由局部绑定声明的，则返回 true。
- `getFirstToken(node, skipOptions)`：返回表示给定节点的第一个 token。
- `getFirstTokens(node, countOptions)`：返回表示给定节点的前 `count` 个 token。
- `getLastToken(node, skipOptions)`：返回表示给定节点的最后一个 token。
- `getLastTokens(node, countOptions)`：返回表示给定节点的最后 `count` 个 token。
- `getTokenAfter(nodeOrToken, skipOptions)`：返回给定节点或 token 之后的第一个 token。
- `getTokensAfter(nodeOrToken, countOptions)`：返回给定节点或 token 之后的 `count` 个 token。
- `getTokenBefore(nodeOrToken, skipOptions)`：返回给定节点或 token 之前的第一个 token。
- `getTokensBefore(nodeOrToken, countOptions)`：返回给定节点或 token 之前的 `count` 个 token。
- `getFirstTokenBetween(nodeOrToken1, nodeOrToken2, skipOptions)`：返回两个节点或 token 之间的第一个 token。
- `getFirstTokensBetween(nodeOrToken1, nodeOrToken2, countOptions)`：返回两个节点或 token 之间的前 `count` 个 token。
- `getLastTokenBetween(nodeOrToken1, nodeOrToken2, skipOptions)`：返回两个节点或 token 之间的最后一个 token。
- `getLastTokensBetween(nodeOrToken1, nodeOrToken2, countOptions)`：返回两个节点或 token 之间的最后 `count` 个 token。
- `getTokens(node)`：返回给定节点的所有 token。
- `getTokensBetween(nodeOrToken1, nodeOrToken2)`：返回两个节点之间的所有 token。
- `getTokenByRangeStart(index, rangeOptions)`：返回在源代码中以给定索引开始范围的 token。
- `getNodeByRangeIndex(index)`：返回 AST 中包含给定源索引的最深节点。
- `getLocFromIndex(index)`：返回一个具有 `line` 和 `column` 属性的对象，对应给定源索引的位置。`line` 从 1 开始，`column` 从 0 开始。
- `getIndexFromLoc(loc)`：返回给定位置在源代码中的索引，其中 `loc` 是具有 1 起始 `line` 键和 0 起始 `column` 键的对象。
- `commentsExistBetween(nodeOrToken1, nodeOrToken2)`：如果两个节点之间存在注释，则返回 `true`。
- `getAncestors(node)`：返回给定节点的祖先数组，从 AST 的根开始，一直到给定节点的直接父节点。此数组不包含给定节点本身。
- `getDeclaredVariables(node)`：返回给定节点声明的[变量](https://eslint.org/docs/latest/extend/scope-manager-interface#variable-interface)列表。此信息可用于跟踪变量的引用。
    - 如果节点是 `VariableDeclaration`，返回声明中声明的所有变量。
    - 如果节点是 `VariableDeclarator`，返回声明符中声明的所有变量。
    - 如果节点是 `FunctionDeclaration` 或 `FunctionExpression`，除了函数参数的变量外，还返回函数名的变量。
    - 如果节点是 `ArrowFunctionExpression`，返回参数的变量。
    - 如果节点是 `ClassDeclaration` 或 `ClassExpression`，返回类名的变量。
    - 如果节点是 `CatchClause`，返回异常参数的变量。
    - 如果节点是 `ImportDeclaration`，返回其所有 specifier 的变量。
    - 如果节点是 `ImportSpecifier`、`ImportDefaultSpecifier` 或 `ImportNamespaceSpecifier`，返回声明的变量。
    - 否则，如果节点未声明任何变量，则返回空数组。
- `getScope(node)`：返回给定节点的[作用域](https://eslint.org/docs/latest/extend/scope-manager-interface#scope-interface)。此信息可用于跟踪变量的引用。
- `markVariableAsUsed(name, refNode)`：将由给定引用节点指示的作用域中具有给定名称的变量标记为已使用。这会影响 [no-unused-vars](https://eslint.org/docs/latest/rules/no-unused-vars) 规则。如果找到具有给定名称的变量并标记为已使用，则返回 `true`，否则返回 `false`。

`skipOptions` 是一个有 3 个属性的对象：`skip`、`includeComments` 和 `filter`。默认值为 `{skip: 0, includeComments: false, filter: null}`。

- `skip`：（`number`）正整数，跳过的 token 数量。如果同时给定 `filter` 选项，被过滤掉的 token 不计入跳过数。
- `includeComments`：（`boolean`）在结果中包含注释 token 的标志。
- `filter(token)`：接收 token 作为第一个参数的函数。如果函数返回 `false`，则结果排除该 token。

`countOptions` 是一个有 3 个属性的对象：`count`、`includeComments` 和 `filter`。默认值为 `{count: 0, includeComments: false, filter: null}`。

- `count`：（`number`）正整数，返回 token 的最大数量。
- `includeComments`：（`boolean`）在结果中包含注释 token 的标志。
- `filter(token)`：接收 token 作为第一个参数的函数，如果函数返回 `false`，则结果排除该 token。

`rangeOptions` 是一个有 1 个属性 `includeComments` 的对象。默认值为 `{includeComments: false}`。

- `includeComments`：（`boolean`）在结果中包含注释 token 的标志。

还有一些属性可以访问：

- `hasBOM`：（`boolean`）指示源代码是否具有 Unicode BOM 的标志。
- `text`：（`string`）被检查代码的完整文本。Unicode BOM 已从此文本中去除。
- `ast`：（`object`）被检查代码的 AST 的 `Program` 节点。
- `scopeManager`：代码的 [ScopeManager](https://eslint.org/docs/latest/extend/scope-manager-interface#scopemanager-interface) 对象。
- `visitorKeys`：（`object`）遍历此 AST 的访问器键。
- `parserServices`：（`object`）包含解析器为规则提供的服务。默认解析器不提供任何服务。但是，如果规则旨在与自定义解析器一起使用，它可以使用 `parserServices` 访问该解析器提供的任何内容。（例如，TypeScript 解析器可以提供获取给定节点计算类型的能力。）
- `lines`：（`array`）按规范定义的行分隔符拆分的行数组。

每当你需要获取有关被检查代码的更多信息时，都应该使用 `SourceCode` 对象。

###### 访问源文本（Accessing the Source Text）

如果你的规则需要获取实际的 JavaScript 源代码来处理，请使用 `sourceCode.getText()` 方法。此方法的工作方式如下：

```js
// 获取全部源代码
var source = sourceCode.getText();

// 只获取此 AST 节点的源代码
var nodeSource = sourceCode.getText(node);

// 只获取此 token 的源代码
var tokenSource = sourceCode.getText(token);

// 只获取此注释的源代码
var commentSource = sourceCode.getText(comment);

// 获取 AST 节点加上前两个字符的源代码
var nodeSourceWithPrev = sourceCode.getText(node, 2);

// 获取 AST 节点加上后两个字符的源代码
var nodeSourceWithFollowing = sourceCode.getText(node, 0, 2);
```

通过这种方式，当 AST 没有提供适当的数据（例如逗号、分号、括号等的位置）时，你可以在 JavaScript 文本本身中查找模式。

###### 访问注释（Accessing Comments）

虽然注释在技术上不属于 AST，但 ESLint 提供了 `sourceCode.getAllComments()`、`sourceCode.getCommentsBefore()`、`sourceCode.getCommentsAfter()` 和 `sourceCode.getCommentsInside()` 来访问它们。

`sourceCode.getCommentsBefore()`、`sourceCode.getCommentsAfter()` 和 `sourceCode.getCommentsInside()` 对于需要检查与给定节点或 token 相关的注释的规则很有用。

请记住，这些方法的结果是按需计算的。

你还可以使用 `includeComments` 选项通过许多 `sourceCode` 的方法访问注释。

##### 选项模式（Options Schemas）

带选项的规则必须指定 `meta.schema` 属性，它是规则选项的 [JSON Schema](https://json-schema.org/) 格式描述，ESLint 将使用它来验证配置选项，并在无效或意外的输入通过 `context.options` 传给规则之前阻止它们。

如果你的规则有选项，强烈建议你指定用于选项验证的 schema。不过，可以通过设置 `schema: false` 选择退出选项验证，但不鼓励这样做，因为它会增加 bug 和错误的几率。

对于未指定 `meta.schema` 属性的规则，当传入任何选项时，ESLint 会抛出错误。如果你的规则没有选项，不要设置 `schema: false`，只需省略 schema 属性或使用 `schema: []`，两者都会阻止传入任何选项。

验证规则配置时，有五个步骤：

1. 如果规则配置不是数组，则将该值包装成数组（例如 `"off"` 变为 `["off"]`）；如果规则配置是数组，则直接使用。
2. ESLint 将规则配置数组的第一个元素验证为严重级别（`"off"`、`"warn"`、`"error"`、`0`、`1`、`2`）
3. 如果严重级别是 `off` 或 `0`，则禁用该规则并停止验证，忽略规则配置数组的任何其他元素。
4. 如果规则已启用，则数组中严重级别之后的任何元素都被复制到 `context.options` 数组中（例如配置 `["warn", "never", { someOption: 5 }]` 会得到 `context.options = ["never", { someOption: 5 }]`）
5. 对 `context.options` 数组运行规则的 schema 验证。

注意：这意味着规则 schema 无法验证严重级别。规则 schema 只验证规则配置中严重级别*之后*的数组元素。规则无法知道自己被配置为什么严重级别。

规则的 `schema` 有两种格式：

- JSON Schema 对象的数组
    - 每个元素将与 `context.options` 数组中的相同位置进行校验。
    - 如果 `context.options` 数组的元素少于 schema 的数量，则不匹配的 schema 将被忽略。
    - 如果 `context.options` 数组的元素多于 schema 的数量，则验证失败。
    - 使用这种格式有两个重要后果：
        - 用户不为你的规则提供任何选项（除严重级别外）*总是有效的*。
        - 如果你指定一个空数组，那么用户为你的规则提供任何选项（除严重级别外）*总是错误的*。
- 一个完整的 JSON Schema 对象，用于验证 `context.options` 数组
    - 即使你的规则只接受一个选项，schema 也应假定要验证的是一个选项数组。
    - schema 可以任意复杂，因此你可以通过 `oneOf`、`anyOf` 等验证完全不同的潜在选项集。
    - 支持的 JSON Schema 版本是 [Draft-04](https://json-schema.org/draft-04/schema)，因此一些较新的特性（如 `if` 或 `$data`）不可用。
        - 目前，由于生态系统兼容性方面的考虑，明确计划不将 schema 支持更新到此级别以上。更多背景请参见[这条评论](https://github.com/eslint/eslint/issues/13888#issuecomment-872591875)。

例如，`yoda` 规则接受一个 `"always"` 或 `"never"` 的主模式参数，以及一个带可选属性 `exceptRange` 的额外选项对象：

```js
// 有效配置：
// "yoda": "warn"
// "yoda": ["error"]
// "yoda": ["error", "always"]
// "yoda": ["error", "never", { "exceptRange": true }]
// 无效配置：
// "yoda": ["warn", "never", { "exceptRange": true }, 5]
// "yoda": ["error", { "exceptRange": true }, "never"]
module.exports = {
	meta: {
		schema: [
			{
				enum: ["always", "never"],
			},
			{
				type: "object",
				properties: {
					exceptRange: { type: "boolean" },
				},
				additionalProperties: false,
			},
		],
	},
};
```

这是等价的基于对象的 schema：

```js
// 有效配置：
// "yoda": "warn"
// "yoda": ["error"]
// "yoda": ["error", "always"]
// "yoda": ["error", "never", { "exceptRange": true }]
// 无效配置：
// "yoda": ["warn", "never", { "exceptRange": true }, 5]
// "yoda": ["error", { "exceptRange": true }, "never"]
module.exports = {
	meta: {
		schema: {
			type: "array",
			minItems: 0,
			maxItems: 2,
			items: [
				{
					enum: ["always", "never"],
				},
				{
					type: "object",
					properties: {
						exceptRange: { type: "boolean" },
					},
					additionalProperties: false,
				},
			],
		},
	},
};
```

对象 schema 可以更精确、更严格地限制允许的内容。例如，下面的 schema 始终要求指定第一个选项（0 到 10 之间的数字），但第二个选项是可选的，并且可以是明确设置了某些选项的对象，或者是 `"off"` 或 `"strict"`：

```js
// 有效配置：
// "someRule": ["error", 6]
// "someRule": ["error", 5, "strict"]
// "someRule": ["warn", 10, { someNonOptionalProperty: true }]
// 无效配置：
// "someRule": "warn"
// "someRule": ["error"]
// "someRule": ["warn", 15]
// "someRule": ["warn", 7, { }]
// "someRule": ["error", 3, "on"]
// "someRule": ["warn", 7, { someOtherProperty: 5 }]
// "someRule": ["warn", 7, { someNonOptionalProperty: false, someOtherProperty: 5 }]
module.exports = {
	meta: {
		schema: {
			type: "array",
			minItems: 1, // 不能只指定严重级别！
			maxItems: 2,
			items: [
				{
					type: "number",
					minimum: 0,
					maximum: 10,
				},
				{
					anyOf: [
						{
							type: "object",
							properties: {
								someNonOptionalProperty: { type: "boolean" },
							},
							required: ["someNonOptionalProperty"],
							additionalProperties: false,
						},
						{
							enum: ["off", "strict"],
						},
					],
				},
			],
		},
	},
};
```

请记住，规则选项始终是数组，因此请注意不要在顶层为非数组类型指定 schema。如果你的 schema 没有在顶层指定数组，用户将*永远无法*启用你的规则，因为当规则启用时，他们的配置总是无效的。

下面是一个总是验证失败的 schema 示例：

```js
// 可能试图验证 ["error", { someOptionalProperty: true }]
// 但当规则启用时，配置总是验证失败，因为选项是数组，不匹配 "object"
module.exports = {
	meta: {
		schema: {
			type: "object",
			properties: {
				someOptionalProperty: {
					type: "boolean",
				},
			},
			additionalProperties: false,
		},
	},
};
```

**注意：**如果你的规则 schema 使用 JSON schema 的 [`$ref`](https://json-schema.org/understanding-json-schema/structuring.html#ref) 属性，你必须使用完整的 JSON Schema 对象，而不是位置属性 schema 的数组。这是因为 ESLint 会将数组简写转换为单个 schema，而不更新引用，这会使它们不正确（它们会被忽略）。

要了解更多关于 JSON Schema 的信息，我们建议查看 [JSON Schema 网站](https://json-schema.org/learn/miscellaneous-examples)上的一些示例，或阅读免费的 [Understanding JSON Schema](https://json-schema.org/understanding-json-schema/) 电子书。

##### 选项默认值（Option Defaults）

规则可以指定一个 `meta.defaultOptions` 数组，为任何选项提供默认值。
当规则在用户配置中启用时，ESLint 会将用户提供的任何选项元素递归地合并到默认元素之上。

例如，给定以下默认值：

```js
export default {
	meta: {
		defaultOptions: [
			{
				alias: "basic",
			},
		],
		schema: [
			{
				type: "object",
				properties: {
					alias: {
						type: "string",
					},
				},
				additionalProperties: false,
			},
		],
	},
	create(context) {
		const [{ alias }] = context.options;

		return {/* ... */};
	},
};
```

除非用户配置指定了不同的值（例如 `["error", { alias: "complex" }]`），否则该规则的运行时 `alias` 值将是 `"basic"`。

选项数组的每个元素按照以下规则合并：

- 任何缺失的值或用户显式提供的 `undefined` 将回退到默认选项
- 用户提供的数组和除 `undefined` 之外的基本类型值会覆盖默认选项
- 用户提供的对象将合并到默认选项对象中，否则替换非对象的默认值

选项默认值也将根据规则的 `meta.schema` 进行验证。

**注意：** ESLint 内部使用 [Ajv](https://ajv.js.org) 进行 schema 验证，并启用了其 [`useDefaults` 选项](https://ajv.js.org/guide/modifying-data.html#assigning-defaults)。
用户提供的选项和 `meta.defaultOptions` 选项都将覆盖规则 schema 中指定的任何默认值。
ESLint 可能会在未来的主版本中禁用 Ajv 的 `useDefaults`。

##### 访问 Shebang（Accessing Shebangs）

[Shebang（#!）](<https://en.wikipedia.org/wiki/Shebang_(Unix)>)由 `"Shebang"` 类型的唯一 token 表示。它们被视为注释，可以通过[访问注释](#访问注释accessing-comments)一节中介绍的方法访问，例如 `sourceCode.getAllComments()`。

##### 访问变量作用域（Accessing Variable Scopes）

`SourceCode#getScope(node)` 方法返回给定节点的作用域。这是一个很有用的方法，用于查找有关给定作用域中变量以及它们在其他作用域中如何使用信息。

::: tip
你可以使用 [Code Explorer](https://explorer.eslint.org) 查看任何 JavaScript 代码的作用域信息。
:::

###### 作用域类型（Scope types）

下表列出了 AST 节点类型及其对应的作用域类型。有关作用域类型的更多信息，请参阅 [`Scope` 对象文档](https://eslint.org/docs/latest/extend/scope-manager-interface#scope-interface)。

| AST 节点类型              | 作用域类型           |
| :------------------------ | :------------------- |
| `Program`                 | `global`             |
| `FunctionDeclaration`     | `function`           |
| `FunctionExpression`      | `function`           |
| `ArrowFunctionExpression` | `function`           |
| `ClassDeclaration`        | `class`              |
| `ClassExpression`         | `class`              |
| `StaticBlock`             | `class-static-block` |
| `BlockStatement` ※1       | `block`              |
| `SwitchStatement` ※1      | `switch`             |
| `ForStatement` ※2         | `for`                |
| `ForInStatement` ※2       | `for`                |
| `ForOfStatement` ※2       | `for`                |
| `WithStatement`           | `with`               |
| `CatchClause`             | `catch`              |
| 其他                      | ※3 ※4                |

**※1** 仅当配置的解析器提供块级作用域特性时。当 `languageOptions.ecmaVersion` 不小于 `6` 时，默认解析器提供块级作用域特性。<br>
**※2** 仅当 `for` 语句将迭代变量定义为块级作用域变量时（例如 `for (let i = 0;;) {}`）。<br>
**※3** 拥有自己作用域的最近祖先节点的作用域。如果最近的祖先节点有多个作用域，则选择最内层的作用域（例如，如果 `Program#sourceType` 为 `"module"`，`Program` 节点有一个 `global` 作用域和一个 `module` 作用域。最内层的作用域是 `module` 作用域）。<br>
**※4** 每个 `PropertyDefinition#value` 节点（它可以是任何表达式节点类型）都有一个 `class-field-initializer` 作用域。例如，在 `class C { field = 1 }` 中，表示 `1` 的 `Literal` 节点具有 `class-field-initializer` 作用域。如果该节点有其他作用域，`class-field-initializer` 作用域将是最外层的。例如，在 `class C { field = () => {} }` 中，`ArrowFunctionExpression` 节点有两个作用域：`class-field-initializer` 和 `function`。

###### 作用域变量（Scope Variables）

`Scope#variables` 属性包含一个 [`Variable` 对象](https://eslint.org/docs/latest/extend/scope-manager-interface#variable-interface)的数组。这些是在当前作用域中声明的变量。你可以使用这些 `Variable` 对象来跟踪整个模块中对变量的引用。

在每个 `Variable` 内部，`Variable#references` 属性包含一个 [`Reference` 对象](https://eslint.org/docs/latest/extend/scope-manager-interface#reference-interface)的数组。`Reference` 数组包含模块源代码中引用该变量的所有位置。

同样在每个 `Variable` 内部，`Variable#defs` 属性包含一个 [`Definition` 对象](https://eslint.org/docs/latest/extend/scope-manager-interface#definition-interface)的数组。你可以使用这些 `Definition` 来找到变量的定义位置。

全局变量有以下额外属性：

- `Variable#writeable`（`boolean | undefined`）…… 如果为 `true`，此全局变量可以被赋任意值。如果为 `false`，此全局变量是只读的。
- `Variable#eslintExplicitGlobal`（`boolean | undefined`）…… 如果为 `true`，此全局变量是由源代码文件中的 `/* globals */` 指令注释定义的。
- `Variable#eslintExplicitGlobalComments`（`Comment[] | undefined`）…… 在源代码文件中定义此全局变量的 `/* globals */` 指令注释数组。如果没有 `/* globals */` 指令注释，此属性为 `undefined`。
- `Variable#eslintImplicitGlobalSetting`（`"readonly" | "writable" | undefined`）…… 配置文件中的配置值。如果存在 `/* globals */` 指令注释，这可能与 `variable.writeable` 不同。

有关使用 `SourceCode#getScope()` 跟踪变量的示例，请参阅以下内置规则的源代码：

- [no-shadow](https://github.com/eslint/eslint/blob/main/lib/rules/no-shadow.js)：在 `Program` 节点调用 `sourceCode.getScope()`，并检查所有子作用域，以确保变量名没有在更低的作用域中被复用。（[no-shadow](https://eslint.org/docs/latest/rules/no-shadow) 文档）
- [no-redeclare](https://github.com/eslint/eslint/blob/main/lib/rules/no-redeclare.js)：在每个作用域调用 `sourceCode.getScope()`，以确保变量没有在同一作用域中被声明两次。（[no-redeclare](https://eslint.org/docs/latest/rules/no-redeclare) 文档）

##### 将变量标记为已使用（Marking Variables as Used）

某些 ESLint 规则（如 [`no-unused-vars`](https://eslint.org/docs/latest/rules/no-unused-vars)）会检查变量是否已被使用。ESLint 本身只知道变量访问的标准规则，因此自定义的变量访问方式可能不会被登记为"已使用"。

为帮助解决这个问题，你可以使用 `sourceCode.markVariableAsUsed()` 方法。此方法接受两个参数：要标记为已使用的变量名，以及一个可选的引用节点（指示你正在处理的作用域）。下面是一个示例：

```js
module.exports = {
	create: function (context) {
		var sourceCode = context.sourceCode;

		return {
			ReturnStatement(node) {
				// 在函数的作用域中查找 myCustomVar 并标记为已使用
				sourceCode.markVariableAsUsed("myCustomVar", node);

				// 或：在全局作用域中查找 myCustomVar 并标记为已使用
				sourceCode.markVariableAsUsed("myCustomVar");
			},
		};
		// ...
	},
};
```

这里，`myCustomVar` 变量相对于一个 `ReturnStatement` 节点被标记为已使用，这意味着 ESLint 将从最接近该节点的作用域开始搜索。如果省略第二个参数，则使用顶层作用域。（对于 ESM 文件，顶层作用域是模块作用域；对于 CommonJS 文件，顶层作用域是第一个函数作用域。）

##### 访问代码路径（Accessing Code Paths）

ESLint 在遍历 AST 时分析代码路径。你可以通过七个与代码路径相关的事件访问代码路径对象。更多信息请参阅[代码路径分析（Code Path Analysis）](https://eslint.org/docs/latest/extend/code-path-analysis)。

#### 规则单元测试（Rule Unit Tests）

ESLint 提供了 [`RuleTester`](https://eslint.org/docs/latest/integrate/nodejs-api#ruletester) 工具，使为规则编写测试变得容易。

#### 规则命名约定（Rule Naming Conventions）

虽然你可以给自定义规则起任何名字，但核心规则有命名约定。将这些相同的命名约定应用到你的自定义规则可能会更清晰。要了解更多，请参阅[核心规则命名约定](https://eslint.org/docs/latest/contribute/core-rules#rule-naming-conventions)文档。

#### 运行时规则（Runtime Rules）

你可以直接在配置文件中包含自定义规则。这非常适合那些特定于你的项目或公司、不适合随 ESLint 一起发布或作为插件发布的规则。

这些规则的书写格式与所有其他规则相同。像创建任何其他规则一样创建你的规则，然后按照以下步骤操作：

1. 将你的规则导入到你的[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file)中。
2. 在 `plugins` 键下定义一个[虚拟插件](https://eslint.org/docs/latest/use/configure/plugins#configuring-a-virtual-plugin)，并将你的规则添加到它的 `rules` 对象中。
3. 在 `rules` 键下为你的规则指定严重级别，使用插件命名空间作为规则 ID 的前缀（例如 `local/my-rule`）。除非规则的严重级别是 `"warn"` 或 `"error"`，否则它不会运行。

例如，如果你的规则在 `rules/my-rule.js` 中，你的配置文件将如下所示：

```js
// eslint.config.js
import { defineConfig } from "eslint/config";
import myRule from "./rules/my-rule.js";

export default defineConfig([
	{
		// 定义插件
		plugins: {
			local: {
				rules: {
					"my-rule": myRule,
				},
			},
		},

		// 配置规则
		rules: {
			"local/my-rule": "error",
		},
	},
]);
```

#### 分析规则性能（Profile Rule Performance）

ESLint 有一个内置方法来跟踪各个规则的性能。设置 `TIMING` 环境变量会在 lint 完成时触发显示运行时间最长的十条规则，以及它们各自的运行时间（规则创建 + 规则执行）和占规则总处理时间（规则创建 + 规则执行）百分比的相对性能影响。

```bash
$ TIMING=1 eslint lib
Rule                    | Time (ms) | Relative
:-----------------------|----------:|--------:
no-multi-spaces         |    52.472 |     6.1%
camelcase               |    48.684 |     5.7%
no-irregular-whitespace |    43.847 |     5.1%
valid-jsdoc             |    40.346 |     4.7%
handle-callback-err     |    39.153 |     4.6%
space-infix-ops         |    35.444 |     4.1%
no-undefined            |    25.693 |     3.0%
no-shadow               |    22.759 |     2.7%
no-empty-class          |    21.976 |     2.6%
semi                    |    19.359 |     2.3%
```

要明确测试单条规则，组合使用 `--no-config-lookup` 和 `--rule` 选项：

```bash
$ TIMING=1 eslint --no-config-lookup --rule "quotes: [2, 'double']" lib
Rule   | Time (ms) | Relative
:------|----------:|--------:
quotes |    18.066 |   100.0%
```

要查看更长的结果列表（超过 10 条），将环境变量设置为其他值，例如 `TIMING=50` 或 `TIMING=all`。

要获取更细粒度的计时信息（每个文件每条规则），请改用 [`stats`](https://eslint.org/docs/latest/extend/stats) 选项。
