## 配置 Prettier（Configuring Prettier）

### 选项（Options）

> 原文链接：https://prettier.io/docs/options

Prettier 附带少量格式化选项。

**要了解更多关于 Prettier 对选项的立场——请参见[选项理念](https://prettier.io/docs/option-philosophy)。**

如果你要更改任何选项，建议通过[配置文件](https://prettier.io/docs/configuration)进行。这样 Prettier CLI、[编辑器集成](https://prettier.io/docs/editors)和其他工具都能知道你使用了哪些选项。

#### 实验性三元表达式格式（Experimental Ternaries）

在 Prettier 的[新三元表达式格式](https://github.com/prettier/prettier/pull/13183)成为默认行为之前试用它。

有效选项：

- `true` - 使用 curious ternaries，问号放在条件之后。
- `false` - 保留三元表达式的默认行为；问号与结果表达式保持在同一行。

| 默认值  | CLI 覆盖                   | API 覆盖                          |
| ------- | -------------------------- | --------------------------------- |
| `false` | `--experimental-ternaries` | `experimentalTernaries: <bool>`   |

#### 实验性运算符位置（Experimental Operator Position）

有效选项：

- `"start"` - 当二元表达式换行时，将运算符打印在新行的开头。
- `"end"` - 默认行为；当二元表达式换行时，将运算符打印在前一行的末尾。

| 默认值  | CLI 覆盖                                                          | API 覆盖                                                         |
| ------- | ----------------------------------------------------------------- | ---------------------------------------------------------------- |
| `"end"` | <code>--experimental-operator-position \<start&#124;end></code>   | <code>experimentalOperatorPosition: "\<start&#124;end>"</code>   |

#### 打印宽度（Print Width）

指定 printer 换行的行长度。

:::warning

**为了可读性，我们不建议使用超过 80 个字符：**

在代码风格指南中，最大行长度规则通常设置为 100 或 120。然而，当人类编写代码时，他们不会努力让每一行都达到最大列数。开发者经常使用空白来拆分长行以提高可读性。在实践中，平均行长度往往远低于最大值。

Prettier 的 printWidth 选项的工作方式不同。它不是硬性的行长度上限。它是一种告诉 Prettier 你大致希望行有多长的方式。Prettier 会生成更短和更长的行，但总体上会努力满足指定的 printWidth。

请记住，计算机是笨的。你需要明确告诉它们该做什么，而人类可以自己做出（隐式的）判断，例如何时换行。

换句话说，不要试图把 printWidth 当成 ESLint 的 [max-len](https://eslint.org/docs/rules/max-len) 来用——它们不是一回事。max-len 只说明允许的最大行长度，而不说明通常首选的长度——后者才是 printWidth 所指定的。

:::

| 默认值 | CLI 覆盖              | API 覆盖            |
| ------ | --------------------- | ------------------- |
| `80`   | `--print-width <int>` | `printWidth: <int>` |

在 [`.editorconfig` 文件](https://editorconfig.org/)中设置 `max_line_length` 将配置 Prettier 的打印宽度，除非被覆盖。

（如果你在格式化 Markdown 时不想要换行，可以将 [Prose Wrap](#散文换行prose-wrap) 选项设置为禁用它。）

#### 制表符宽度（Tab Width）

指定每个缩进级别的空格数。

| 默认值 | CLI 覆盖            | API 覆盖          |
| ------ | ------------------- | ----------------- |
| `2`    | `--tab-width <int>` | `tabWidth: <int>` |

在 [`.editorconfig` 文件](https://editorconfig.org/)中设置 `indent_size` 或 `tab_width` 将配置 Prettier 的制表符宽度，除非被覆盖。

#### 制表符（Tabs）

用制表符而不是空格缩进行。

| 默认值  | CLI 覆盖     | API 覆盖          |
| ------- | ------------ | ----------------- |
| `false` | `--use-tabs` | `useTabs: <bool>` |

在 [`.editorconfig` 文件](https://editorconfig.org/)中设置 `indent_style` 将配置 Prettier 的制表符使用，除非被覆盖。

（制表符将用于*缩进*，但 Prettier 使用空格来*对齐*内容，例如在三元表达式中。这种行为被称为 [SmartTabs](https://www.emacswiki.org/emacs/SmartTabs)。）

#### 分号（Semicolons）

在语句末尾打印分号。

有效选项：

- `true` - 在每个语句末尾添加分号。
- `false` - 只在[可能引入 ASI 故障](https://prettier.io/docs/rationale#semicolons)的行开头添加分号。

| 默认值 | CLI 覆盖     | API 覆盖       |
| ------ | ------------ | -------------- |
| `true` | `--no-semi`  | `semi: <bool>` |

#### 引号（Quotes）

使用单引号而不是双引号。

注意：

- JSX 引号忽略此选项——参见 [jsx-single-quote](#jsx-引号jsx-quotes)。
- 如果一种引号的数量超过另一种，将使用较少使用的那种引号来格式化字符串——例如：`"I'm double quoted"` 得到 `"I'm double quoted"`，而 `"This \"example\" is single quoted"` 得到 `'This "example" is single quoted'`。

更多信息请参见[字符串的设计原理](https://prettier.io/docs/rationale#strings)。

| 默认值  | CLI 覆盖         | API 覆盖              |
| ------- | ---------------- | --------------------- |
| `false` | `--single-quote` | `singleQuote: <bool>` |

#### 属性引号（Quote Props）

更改对象中的属性何时加引号。

有效选项：

- `"as-needed"` - 仅在需要的地方为对象属性添加引号。
- `"consistent"` - 如果对象中至少有一个属性需要引号，则为所有属性加引号。
- `"preserve"` - 尊重对象属性中引号的输入用法。

| 默认值        | CLI 覆盖                                                              | API 覆盖                                                              |
| ------------- | --------------------------------------------------------------------- | --------------------------------------------------------------------- |
| `"as-needed"` | <code>--quote-props \<as-needed&#124;consistent&#124;preserve></code> | <code>quoteProps: "\<as-needed&#124;consistent&#124;preserve>"</code> |

请注意，Prettier 从不在 Angular 表达式、TypeScript 和 Flow 中去除数字属性名的引号，因为字符串键和数字键之间的区别在这些语言中很重要。参见：[Angular](https://codesandbox.io/s/hungry-morse-foj87?file=/src/app/app.component.html)、[TypeScript](https://www.typescriptlang.org/play?#code/DYUwLgBAhhC8EG8IEYBcKA0EBM7sQF8AoUSAIzkQgHJlr1ktrt6dCiiATEAY2CgBOICKWhR0AaxABPAPYAzCGGkAHEAugBuLr35CR4CGTKSZG5Wo1ltRKDHjHtQA)、[Flow](https://flow.org/try/#0PQKgBAAgZgNg9gdzCYAoVBjOA7AzgFzAA8wBeMAb1TDAAYAuMARlQF8g)。Prettier 也不会为 Vue 去除数字属性的引号（参见相关的 [issue](https://github.com/prettier/prettier/issues/10127)）。

#### JSX 引号（JSX Quotes）

在 JSX 中使用单引号而不是双引号。

| 默认值  | CLI 覆盖             | API 覆盖                 |
| ------- | -------------------- | ------------------------ |
| `false` | `--jsx-single-quote` | `jsxSingleQuote: <bool>` |

#### 尾随逗号（Trailing Commas）

_默认值在 v3.0.0 中从 `es5` 更改为 `all`_

在多行的、以逗号分隔的语法结构中尽可能打印尾随逗号。（例如，单行数组永远不会获得尾随逗号。）

有效选项：

- `"all"` - 尽可能使用尾随逗号（包括[函数参数和调用](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Trailing_commas#Trailing_commas_in_functions)）。以这种方式格式化的 JavaScript 代码需要支持 ES2017 的引擎（Node.js 8+ 或现代浏览器）或[向下编译](https://babeljs.io/docs/index)才能运行。这也启用了 TypeScript 类型参数中的尾随逗号（自 2018 年 1 月发布的 TypeScript 2.7 起支持）。
- `"es5"` - 在 ES5 中有效的地方使用尾随逗号（对象、数组等）。TypeScript 和 Flow 的类型参数中使用尾随逗号。
- `"none"` - 不使用尾随逗号。

| 默认值  | CLI 覆盖                                                    | API 覆盖                                                    |
| ------- | ----------------------------------------------------------- | ----------------------------------------------------------- |
| `"all"` | <code>--trailing-comma \<all&#124;es5&#124;none></code>     | <code>trailingComma: "\<all&#124;es5&#124;none>"</code>     |

#### 括号间距（Bracket Spacing）

在对象字面量的括号之间打印空格。

有效选项：

- `true` - 示例：`{ foo: bar }`。
- `false` - 示例：`{foo: bar}`。

| 默认值 | CLI 覆盖               | API 覆盖                 |
| ------ | ---------------------- | ------------------------ |
| `true` | `--no-bracket-spacing` | `bracketSpacing: <bool>` |

#### 对象换行（Object Wrap）

_最早在 v3.5.0 可用_

配置 Prettier 在对象字面量可以放在一行或跨多行时如何换行。

默认情况下，如果第一个属性之前有换行符，Prettier 会将对象格式化为多行。作者可以使用这种启发式方法根据上下文提高可读性，尽管它有一些缺点。参见[多行对象](https://prettier.io/docs/rationale#multi-line-objects)。

有效选项：

- `"preserve"` - 如果左花括号和第一个属性之间有换行符，则保持多行。
- `"collapse"` - 尽可能放在一行上。

| 默认值       | CLI 覆盖                                             | API 覆盖                                             |
| ------------ | ---------------------------------------------------- | ---------------------------------------------------- |
| `"preserve"` | <code>--object-wrap \<preserve&#124;collapse></code> | <code>objectWrap: "\<preserve&#124;collapse>"</code> |

#### 括号行（Bracket Line）

将多行 HTML（HTML、JSX、Vue、Angular）元素的 `>` 放在最后一行的末尾，而不是单独放在下一行（不适用于自闭合元素）。

有效选项：

- `true` - 示例：

<!-- prettier-ignore -->
```html
<button
  className="prettier-class"
  id="prettier-id"
  onClick={this.handleClick}>
  Click Here
</button>
```

- `false` - 示例：

<!-- prettier-ignore -->
```html
<button
  className="prettier-class"
  id="prettier-id"
  onClick={this.handleClick}
>
  Click Here
</button>
```

| 默认值  | CLI 覆盖              | API 覆盖                  |
| ------- | --------------------- | ------------------------- |
| `false` | `--bracket-same-line` | `bracketSameLine: <bool>` |

#### [已废弃] JSX 括号（[Deprecated] JSX Brackets）

:::danger

此选项已在 v2.4.0 中废弃，请改用 --bracket-same-line。

:::

将多行 JSX 元素的 `>` 放在最后一行的末尾，而不是单独放在下一行（不适用于自闭合元素）。

有效选项：

- `true` - 示例：

<!-- prettier-ignore -->
```jsx
<button
  className="prettier-class"
  id="prettier-id"
  onClick={this.handleClick}>
  Click Here
</button>
```

- `false` - 示例：

<!-- prettier-ignore -->
```jsx
<button
  className="prettier-class"
  id="prettier-id"
  onClick={this.handleClick}
>
  Click Here
</button>
```

| 默认值  | CLI 覆盖                  | API 覆盖                     |
| ------- | ------------------------- | ---------------------------- |
| `false` | `--jsx-bracket-same-line` | `jsxBracketSameLine: <bool>` |

#### 箭头函数括号（Arrow Function Parentheses）

_最早在 v1.9.0 可用，默认值在 v2.0.0 中从 `avoid` 更改为 `always`_

为单个箭头函数参数加上括号。

有效选项：

- `"always"` - 始终包含括号。示例：`(x) => x`
- `"avoid"` - 尽可能省略括号。示例：`x => x`

| 默认值     | CLI 覆盖                                         | API 覆盖                                         |
| ---------- | ------------------------------------------------ | ------------------------------------------------ |
| `"always"` | <code>--arrow-parens \<always&#124;avoid></code> | <code>arrowParens: "\<always&#124;avoid>"</code> |

乍一看，避免括号似乎是更好的选择，因为视觉噪音更少。
然而，当 Prettier 移除括号时，添加类型注解、额外参数或默认值以及进行其他更改变得更加困难。
在编辑真实代码库时，一致使用括号能提供更好的开发者体验，这就是该选项默认值的理由。

#### 范围（Range）

只格式化文件的一部分。

这两个选项可用于格式化从给定字符偏移量开始和结束的代码（分别为包含和不包含）。范围将扩展：

- 向后扩展到包含所选语句的第一行的开头。
- 向前扩展到所选语句的末尾。

| 默认值     | CLI 覆盖              | API 覆盖            |
| ---------- | --------------------- | ------------------- |
| `0`        | `--range-start <int>` | `rangeStart: <int>` |
| `Infinity` | `--range-end <int>`   | `rangeEnd: <int>`   |

#### 解析器（Parser）

指定使用哪个解析器。

Prettier 会自动从输入文件路径推断解析器，所以你不应该需要更改此设置。

`babel` 和 `flow` 解析器支持相同的 JavaScript 特性集（包括 Flow 类型注解）。它们在某些边缘情况下可能有所不同，如果你遇到这种情况，可以尝试用 `flow` 代替 `babel`。`typescript` 和 `babel-ts` 的情况几乎相同。`babel-ts` 可能支持 TypeScript 尚不支持的 JavaScript 特性（提案），但它对无效代码的容错性较低，也没有 `typescript` 解析器那样经过实战检验。

有效选项：

- `"babel"`（通过 [@babel/parser](https://github.com/babel/babel/tree/main/packages/babel-parser)）_在 v1.16.0 之前名为 `"babylon"`_
- `"babel-flow"`（与 `"babel"` 相同，但显式启用 Flow 解析以避免歧义）_最早在 v1.16.0 可用_
- `"babel-ts"`（类似于 `"typescript"`，但使用 Babel 及其 TypeScript 插件）_最早在 v2.0.0 可用_
- `"flow"`（通过 [flow-parser](https://github.com/facebook/flow/tree/master/src/parser)）
- `"typescript"`（通过 [@typescript-eslint/typescript-estree](https://github.com/typescript-eslint/typescript-eslint)）_最早在 v1.4.0 可用_
- `"espree"`（通过 [espree](https://github.com/eslint/espree)）_最早在 v2.2.0 可用_
- `"meriyah"`（通过 [meriyah](https://github.com/meriyah/meriyah)）_最早在 v2.2.0 可用_
- `"acorn"`（通过 [acorn](https://github.com/acornjs/acorn)）_最早在 v2.6.0 可用_
- `"css"`（通过 [postcss](https://github.com/postcss/postcss)）_最早在 v1.7.1 可用_
- `"scss"`（通过 [postcss-scss](https://github.com/postcss/postcss-scss)）_最早在 v1.7.1 可用_
- `"less"`（通过 [postcss-less](https://github.com/shellscape/postcss-less)）_最早在 v1.7.1 可用_
- `"json"`（通过 [@babel/parser parseExpression](https://babeljs.io/docs/babel-parser#babelparserparseexpressioncode-options)）_最早在 v1.5.0 可用_
- `"json5"`（与 `"json"` 相同的解析器，但以 [json5](https://json5.org/) 输出）_最早在 v1.13.0 可用_
- `"jsonc"`（与 `"json"` 相同的解析器，但以 "JSON with Comments" 输出）_最早在 v3.2.0 可用_
- `"json-stringify"`（像 `JSON.parse()` 一样解析，但不那么严格，空白类似于 `JSON.stringify()`）_最早在 v1.13.0 可用_
- `"graphql"`（通过 [graphql/language](https://github.com/graphql/graphql-js/tree/main/src/language)）_最早在 v1.5.0 可用_
- `"markdown"`（通过 [micromark](https://github.com/micromark/micromark)）_最早在 v1.8.0 可用_
- `"mdx"`（通过 [remark-parse](https://github.com/wooorm/remark/tree/main/packages/remark-parse) 和 [remark-mdx](https://github.com/mdx-js/mdx/tree/main/packages/remark-mdx)）_最早在 v1.15.0 可用_
- `"html"`（通过 [angular-html-parser](https://github.com/ikatyang/angular-html-parser/tree/master/packages/angular-html-parser)）_最早在 1.15.0 可用_
- `"vue"`（与 `"html"` 相同的解析器，但还格式化 vue 特定语法）_最早在 1.10.0 可用_
- `"angular"`（与 `"html"` 相同的解析器，但通过 [angular-estree-parser](https://github.com/ikatyang/angular-estree-parser) 还格式化 angular 特定语法）_最早在 1.15.0 可用_
- `"lwc"`（与 `"html"` 相同的解析器，但还格式化不带引号模板属性的 LWC 特定语法）_最早在 1.17.0 可用_
- `"mjml"`（与 `"html"` 相同的解析器，但还格式化 MJML 特定语法）_最早在 3.6.0 可用_
- `"glimmer"`（Ember / Handlebars，通过 [@glimmer/syntax](https://github.com/glimmerjs/glimmer-vm/tree/main/packages/%40glimmer/syntax)）_最早在 1.10.0 可用_
- `"yaml"`（通过 [yaml](https://github.com/eemeli/yaml) 和 [yaml-unist-parser](https://github.com/ikatyang/yaml-unist-parser)）_最早在 1.14.0 可用_

| 默认值 | CLI 覆盖            | API 覆盖             |
| ------ | ------------------- | -------------------- |
| 无     | `--parser <string>` | `parser: "<string>"` |

注意：在 v1.13.0 之前，默认值是 `"babylon"`。

注意：自定义解析器 API 已在 v3.0.0 中移除。请改用[插件](https://prettier.io/docs/plugins)（[如何迁移](https://prettier.io/docs/api#custom-parser-api-removed)）。

<a name="filepath"></a>

#### 文件路径（File Path）

指定用于推断使用哪个解析器的文件名。

例如，以下命令将使用 CSS 解析器：

```bash
cat foo | prettier --stdin-filepath foo.css
```

此选项仅在 CLI 和 API 中有用。在配置文件中使用它没有意义。

| 默认值 | CLI 覆盖                    | API 覆盖               |
| ------ | --------------------------- | ---------------------- |
| 无     | `--stdin-filepath <string>` | `filepath: "<string>"` |

#### 要求 Pragma（Require Pragma）

_最早在 v1.7.0 可用_

Prettier 可以限制自己只格式化文件顶部包含特殊注释（称为 pragma）的文件。这在逐步将大型、未格式化的代码库迁移到 Prettier 时非常有用。

当提供 `--require-pragma` 时，第一个注释为以下内容的文件将被格式化：

```js
/**
 * @prettier
 */
```

或

```js
/**
 * @format
 */
```

| 默认值  | CLI 覆盖           | API 覆盖                |
| ------- | ------------------ | ----------------------- |
| `false` | `--require-pragma` | `requirePragma: <bool>` |

#### 插入 Pragma（Insert Pragma）

_最早在 v1.8.0 可用_

Prettier 可以在文件顶部插入一个特殊的 `@format` 标记，表明该文件已用 Prettier 格式化。这与 `--require-pragma` 选项配合使用效果良好。如果文件顶部已经有一个 docblock，此选项将为其添加一个带有 `@format` 标记的新行。

请注意，"配合"并不意味着"同时"。当两个选项同时使用时，`--require-pragma` 优先，因此 `--insert-pragma` 被忽略。其设计思路是：在大型代码库中逐步采用 Prettier 的过程中，参与迁移过程的开发者使用 `--insert-pragma`，而团队的其他成员和自动化工具使用 `--require-pragma`，只处理已完成迁移的文件。该功能的灵感来自 Facebook 的[采用策略](https://prettier.io/blog/2017/05/03/1.3.0.html#facebook-adoption-update)。

| 默认值  | CLI 覆盖          | API 覆盖               |
| ------- | ----------------- | ---------------------- |
| `false` | `--insert-pragma` | `insertPragma: <bool>` |

#### 检查忽略 Pragma（Check Ignore Pragma）

_最早在 v3.6.0 可用_

Prettier 可以允许单个文件通过在文件顶部包含特殊注释（称为 pragma）来选择退出格式化。

检查这些标记会在格式化时产生少量的前期开销，因此默认未启用。

当提供 `--check-ignore-pragma` 时，第一个注释为以下内容的文件将**不会**被格式化：

```js
/**
 * @noprettier
 */
```

或

```js
/**
 * @noformat
 */
```

| 默认值  | CLI 覆盖                | API 覆盖                    |
| ------- | ----------------------- | --------------------------- |
| `false` | `--check-ignore-pragma` | `checkIgnorePragma: <bool>` |

#### 散文换行（Prose Wrap）

_最早在 v1.8.2 可用_

默认情况下，Prettier 不会更改 markdown 文本中的换行，因为某些服务使用对换行敏感的渲染器，例如 GitHub 评论和 BitBucket。要让 Prettier 将散文换行到打印宽度，将此选项更改为 "always"。如果你想让 Prettier 强制所有散文块都在一行上，并依赖编辑器/查看器的软换行，可以使用 `"never"`。

有效选项：

- `"always"` - 将散文换行到 `printWidth`。
- `"never"` - 将每个散文块放在一行上。
- `"preserve"` - 按原样保留现有的换行。_最早在 v1.9.0 可用_

例如，给定这个 Markdown 段落：

```md
The quick brown
fox jumps over the lazy dog.
```

使用 `printWidth: 20`，Prettier 将其格式化为：

- `"always"`

  ```md
  The quick brown fox
  jumps over the lazy
  dog.
  ```

- `"never"`

  ```md
  The quick brown fox jumps over the lazy dog.
  ```

- `"preserve"`

  ```md
  The quick brown
  fox jumps over the lazy dog.
  ```

| 默认值       | CLI 覆盖                                                     | API 覆盖                                                     |
| ------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `"preserve"` | <code>--prose-wrap \<always&#124;never&#124;preserve></code> | <code>proseWrap: "\<always&#124;never&#124;preserve>"</code> |

#### HTML 空白敏感性（HTML Whitespace Sensitivity）

_最早在 v1.15.0 可用。Handlebars 最早在 2.3.0 可用_

指定 HTML、Vue、Angular 和 Handlebars 的全局空白敏感性。更多信息请参见[空白敏感格式化](https://prettier.io/blog/2018/11/07/1.15.0.html#whitespace-sensitive-formatting)。

有效选项：

- `"css"` - 遵循 CSS `display` 属性的默认值。对于 Handlebars，与 `strict` 相同处理。
- `"strict"` - 所有标签周围的空白（或缺失）都被视为重要的。
- `"ignore"` - 所有标签周围的空白（或缺失）都被视为不重要的。

| 默认值  | CLI 覆盖                                                                  | API 覆盖                                                                 |
| ------- | ------------------------------------------------------------------------- | ------------------------------------------------------------------------ |
| `"css"` | <code>--html-whitespace-sensitivity \<css&#124;strict&#124;ignore></code> | <code>htmlWhitespaceSensitivity: "\<css&#124;strict&#124;ignore>"</code> |

#### Vue 文件 script 和 style 标签缩进（Vue files script and style tags indentation）

_最早在 v1.19.0 可用_

是否缩进 Vue 文件中 `<script>` 和 `<style>` 标签内的代码。

有效选项：

- `false` - 不缩进 Vue 文件中的 script 和 style 标签。
- `true` - 缩进 Vue 文件中的 script 和 style 标签。

| 默认值  | CLI 覆盖                        | API 覆盖                          |
| ------- | ------------------------------- | --------------------------------- |
| `false` | `--vue-indent-script-and-style` | `vueIndentScriptAndStyle: <bool>` |

#### 行尾（End of Line）

_最早在 v1.15.0 可用，默认值在 v2.0.0 中从 `auto` 更改为 `lf`_

由于历史原因，文本文件中存在两种常见的行尾形式。
即 `\n`（或 `LF`，_Line Feed_）和 `\r\n`（或 `CRLF`，_Carriage Return + Line Feed_）。
前者在 Linux 和 macOS 上常见，而后者在 Windows 上盛行。
解释原因的细节[可以在 Wikipedia 上找到](https://en.wikipedia.org/wiki/Newline)。

当来自不同操作系统的人们在一个项目上协作时，共享的 git 仓库很容易出现混合的行尾。
Windows 用户也可能意外地将之前提交文件的行尾从 `LF` 改为 `CRLF`。
这样做会产生大量的 `git diff`，从而使文件的逐行历史（`git blame`）更难查看。

如果你想确保你的整个 git 仓库在 Prettier 覆盖的文件中只包含 Linux 风格的行尾：

1. 确保 Prettier 的 `endOfLine` 选项设置为 `lf`（自 v2.0.0 起这是默认值）
1. 配置一个将运行 Prettier 的 [pre-commit hook](https://prettier.io/docs/precommit)
1. 配置 Prettier 在你的 CI 流水线中使用 [`--check` 标志](https://prettier.io/docs/cli#--check)运行。如果你使用 Travis CI，在 `.travis.yml` 中将 [`autocrlf` 选项](https://docs.travis-ci.com/user/customizing-the-build#git-end-of-line-conversion-control)设置为 `input`。
1. 向仓库的 `.gitattributes` 文件添加 `* text=auto eol=lf`。
   你可能需要在此更改后要求 Windows 用户重新克隆你的仓库，以确保 git 在检出时没有将 `LF` 转换为 `CRLF`。

所有操作系统中的所有现代文本编辑器都能够正确显示使用 `\n`（`LF`）的行尾。
然而，旧版本的 Windows 记事本会在视觉上把这些行挤成一行，因为它只能处理 `\r\n`（`CRLF`）。

有效选项：

- `"lf"` – 仅 Line Feed（`\n`），在 Linux、macOS 以及 git 仓库内部常见
- `"crlf"` - Carriage Return + Line Feed 字符（`\r\n`），在 Windows 上常见
- `"cr"` - 仅 Carriage Return 字符（`\r`），极少使用
- `"auto"` - 保持现有的行尾
  （一个文件内的混合值通过查看第一行之后使用的内容来规范化）

| 默认值  | CLI 覆盖                                                     | API 覆盖                                                    |
| ------- | ------------------------------------------------------------ | ----------------------------------------------------------- |
| `"lf"`  | <code>--end-of-line \<lf&#124;crlf&#124;cr&#124;auto></code> | <code>endOfLine: "\<lf&#124;crlf&#124;cr&#124;auto>"</code> |

在 [`.editorconfig` 文件](https://editorconfig.org/)中设置 `end_of_line` 将配置 Prettier 的行尾使用，除非被覆盖。

#### 嵌入式语言格式化（Embedded Language Formatting）

_最早在 v2.1.0 可用_

控制 Prettier 是否格式化文件中嵌入的带引号的代码。

当 Prettier 识别出你似乎在另一个文件中的字符串里放置了它知道如何格式化的代码时——比如在 JavaScript 中带有名为 `html` 标签的模板字面量，或 Markdown 中的代码块——它默认会尝试格式化该代码。

有时这种行为是不可取的，特别是在你可能无意将字符串解释为代码的情况下。此选项允许你在默认行为（`auto`）和完全禁用此功能（`off`）之间切换。

有效选项：

- `"auto"` – 如果 Prettier 能自动识别嵌入式代码，则格式化它。
- `"off"` - 从不自动格式化嵌入式代码。

| 默认值   | CLI 覆盖                                                     | API 覆盖                                                    |
| -------- | ------------------------------------------------------------ | ----------------------------------------------------------- |
| `"auto"` | <code>--embedded-language-formatting=\<off&#124;auto></code> | <code>embeddedLanguageFormatting: "\<off&#124;auto>"</code> |

#### 每行单个属性（Single Attribute Per Line）

_最早在 v2.6.0 可用_

在 HTML、Vue 和 JSX 中强制每行单个属性。

有效选项：

- `false` - 不强制每行单个属性。
- `true` - 强制每行单个属性。

| 默认值  | CLI 覆盖                      | API 覆盖                         |
| ------- | ----------------------------- | -------------------------------- |
| `false` | `--single-attribute-per-line` | `singleAttributePerLine: <bool>` |
