### 插件（Plugins）

> 原文链接：https://prettier.io/docs/plugins

插件是为 Prettier 添加新语言或格式化规则的方式。Prettier 自己对所有语言的实现都是使用插件 API 编写的。核心的 `prettier` 包内置了 JavaScript 和其他 Web 相关的语言。对于其他语言，你需要安装插件。

#### 使用插件（Using Plugins）

你可以通过以下方式加载插件：

- [CLI](https://prettier.io/docs/cli)，通过 `--plugin`：

  ```bash
  prettier --write main.foo --plugin=prettier-plugin-foo
  ```

  :::tip

  你可以多次设置 `--plugin` 选项。

  :::

- [API](https://prettier.io/docs/api)，通过 `plugins` 选项：

  ```js
  import * as prettierPluginFoo from "prettier-plugin-foo";

  await prettier.format("code", {
    parser: "foo",
    plugins: [prettierPluginFoo],
  });
  ```

- [配置文件](https://prettier.io/docs/configuration)：

  ```js title="prettier.config.mjs"
  import * as prettierPluginFoo from "prettier-plugin-foo";

  /**
   * @see https://prettier.io/docs/configuration
   * @type {import("prettier").Config}
   */
  const config = {
    plugins: [prettierPluginFoo],
  };

  export default config;
  ```

传给 `plugins` 的字符串最终会传给 [`import()` 表达式](https://nodejs.org/api/esm.html#import-expressions)，所以你可以提供模块/包名、路径，或 `import()` 接受的任何其他内容。

#### 官方插件（Official Plugins）

- [`@prettier/plugin-php`](https://github.com/prettier/plugin-php)
- [`@prettier/plugin-pug`](https://github.com/prettier/plugin-pug)，作者 [**@Shinigami92**](https://github.com/Shinigami92)
- [`@prettier/plugin-ruby`](https://github.com/prettier/plugin-ruby)
- [`@prettier/plugin-xml`](https://github.com/prettier/plugin-xml)

#### 社区插件（Community Plugins）

- [`@htnabe/prettier-plugin-go-template`](https://github.com/htnabe/prettier-plugin-go-template)，作者 [**@htnabe**](https://github.com/htnabe)
- [`@htnabe/prettier-plugin-hugo-post`](https://github.com/htnabe/prettier-plugin-hugo-post)，作者 [**@htnabe**](https://github.com/htnabe)
- [`@poliklot/prettier-plugin-handlebars`](https://github.com/Poliklot/prettier-plugin-handlebars)，作者 [**@Poliklot**](https://github.com/Poliklot)
- [`prettier-plugin-apex`](https://github.com/dangmai/prettier-plugin-apex)，作者 [**@dangmai**](https://github.com/dangmai)
- [`prettier-plugin-astro`](https://github.com/withastro/prettier-plugin-astro)，作者 [**@withastro contributors**](https://github.com/withastro/prettier-plugin-astro/graphs/contributors)
- [`prettier-plugin-bigcommerce-stencil`](https://github.com/phoenix128/prettier-plugin-bigcommerce-stencil)，作者 [**@phoenix128**](https://github.com/phoenix128)
- [`prettier-plugin-elm`](https://github.com/gicentre/prettier-plugin-elm)，作者 [**@giCentre**](https://github.com/gicentre)
- [`prettier-plugin-erb`](https://github.com/adamzapasnik/prettier-plugin-erb)，作者 [**@adamzapasnik**](https://github.com/adamzapasnik)
- [`prettier-plugin-gherkin`](https://github.com/mapado/prettier-plugin-gherkin)，作者 [**@mapado**](https://github.com/mapado)
- [`prettier-plugin-glsl`](https://github.com/NaridaL/glsl-language-toolkit/tree/main/packages/prettier-plugin-glsl)，作者 [**@NaridaL**](https://github.com/NaridaL)
- [`prettier-plugin-java`](https://github.com/jhipster/prettier-java)，作者 [**@JHipster**](https://github.com/jhipster)
- [`prettier-plugin-jinja-template`](https://github.com/davidodenwald/prettier-plugin-jinja-template)，作者 [**@davidodenwald**](https://github.com/davidodenwald)
- [`prettier-plugin-jte`](https://github.com/sgruendel/prettier-plugin-jte)，作者 [**@sgruendel**](https://github.com/sgruendel)
- [`prettier-plugin-kotlin`](https://github.com/Angry-Potato/prettier-plugin-kotlin)，作者 [**@Angry-Potato**](https://github.com/Angry-Potato)
- [`prettier-plugin-markdown-compact-tables`](https://github.com/cudoment/prettier-plugin-markdown-compact-tables)，作者 [**@cudoment**](https://github.com/cudoment)
- [`prettier-plugin-markdown-html`](https://github.com/porada/prettier-plugin-markdown-html)，作者 [**@porada**](https://github.com/porada)
- [`prettier-plugin-marko`](https://github.com/marko-js/prettier)，作者 [**@marko-js**](https://github.com/marko-js)
- [`prettier-plugin-motoko`](https://github.com/dfinity/prettier-plugin-motoko)，作者 [**@dfinity**](https://github.com/dfinity)
- [`prettier-plugin-mustache`](https://github.com/Poliklot/prettier-plugin-mustache)，作者 [**@Poliklot**](https://github.com/Poliklot)
- [`prettier-plugin-nginx`](https://github.com/jxddk/prettier-plugin-nginx)，作者 [**@jxddk**](https://github.com/jxddk)
- [`prettier-plugin-nunjucks`](https://github.com/Poliklot/prettier-plugin-nunjucks)，作者 [**@Poliklot**](https://github.com/Poliklot)
- [`prettier-plugin-prisma`](https://github.com/umidbekk/prettier-plugin-prisma)，作者 [**@umidbekk**](https://github.com/umidbekk)
- [`prettier-plugin-properties`](https://github.com/eemeli/prettier-plugin-properties)，作者 [**@eemeli**](https://github.com/eemeli)
- [`prettier-plugin-powershell`](https://github.com/Nick2bad4u/Prettier-Plugin-Powershell)，作者 [**@Nick2bad4u**](https://github.com/Nick2bad4u)
- [`prettier-plugin-rust`](https://github.com/jinxdash/prettier-plugin-rust)，作者 [**@jinxdash**](https://github.com/jinxdash)
- [`prettier-plugin-sh`](https://github.com/un-ts/prettier/tree/master/packages/sh)，作者 [**@JounQin**](https://github.com/JounQin)
- [`prettier-plugin-sql`](https://github.com/un-ts/prettier/tree/master/packages/sql)，作者 [**@JounQin**](https://github.com/JounQin)
- [`prettier-plugin-sql-cst`](https://github.com/nene/prettier-plugin-sql-cst)，作者 [**@nene**](https://github.com/nene)
- [`prettier-plugin-solidity`](https://github.com/prettier-solidity/prettier-plugin-solidity)，作者 [**@mattiaerre**](https://github.com/mattiaerre)
- [`prettier-plugin-svelte`](https://github.com/sveltejs/prettier-plugin-svelte)，作者 [**@sveltejs**](https://github.com/sveltejs)
- [`prettier-plugin-toml`](https://github.com/un-ts/prettier/tree/master/packages/toml)，作者 [**@JounQin**](https://github.com/JounQin) 和 [**@so1ve**](https://github.com/so1ve)
- [`prettier-plugin-xquery`](https://github.com/drrataplan/prettier-plugin-xquery)，作者 [**@DrRataplan**](https://github.com/drrataplan)
- [`prettier-plugin-yaml`](https://github.com/porada/prettier-plugin-yaml)，作者 [**@porada**](https://github.com/porada)

#### 开发插件（Developing Plugins）

Prettier 插件是普通的 JavaScript 模块，具有以下五个导出，或以带有以下属性的默认导出形式：

- `languages`
- `parsers`
- `printers`
- `options`
- `defaultOptions`

##### `languages`

languages 是你的插件将为 Prettier 贡献的语言定义数组。它可以包含 [`prettier.getSupportInfo()`](https://prettier.io/docs/api#prettiergetsupportinfo) 中指定的所有字段。

它**必须**包含 `name` 和 `parsers`。

```js
export const languages = [
  {
    // 语言名称
    name: "InterpretedDanceScript",
    // 能解析此语言的解析器。
    // 可以是内置解析器，也可以是你通过此插件贡献的解析器。
    parsers: ["dance-parse"],
  },
];
```

##### `parsers`

parsers 将字符串形式的代码转换为 [AST](https://en.wikipedia.org/wiki/Abstract_syntax_tree)。

键必须与 `languages` 中 `parsers` 数组里的名称匹配。值包含一个 parse 函数、一个 AST 格式名称，以及两个位置提取函数（`locStart` 和 `locEnd`）。

```js
export const parsers = {
  "dance-parse": {
    parse,
    // 解析器产生的 AST 的名称。
    astFormat: "dance-ast",
    hasPragma,
    hasIgnorePragma,
    locStart,
    locEnd,
    preprocess,
  },
};
```

`parse` 函数的签名是：

```ts
function parse(text: string, options: object): Promise<AST> | AST;
```

位置提取函数（`locStart` 和 `locEnd`）返回给定 AST 节点的起始和结束位置：

```ts
function locStart(node: object): number;
```

_（可选）_ pragma 检测函数（`hasPragma`）应返回文本是否包含 pragma 注释。

```ts
function hasPragma(text: string): boolean;
```

_（可选）_ "忽略 pragma"检测函数（`hasIgnorePragma`）应返回文本是否包含表示不应格式化该文本的 pragma。

```ts
function hasIgnorePragma(text: string): boolean;
```

_（可选）_ preprocess 函数可以在传入 `parse` 函数之前处理输入文本。

```ts
function preprocess(text: string, options: object): string | Promise<string>;
```

_对异步 preprocess 的支持最早在 v3.7.0 中添加_

##### `printers`

printers 将 AST 转换为 Prettier 的中间表示，也称为 Doc。

键必须与解析器产生的 `astFormat` 匹配。值包含一个带有 `print` 函数的对象。所有其他属性（`embed`、`preprocess` 等）都是可选的。

```js
export const printers = {
  "dance-ast": {
    print,
    embed,
    preprocess,
    getVisitorKeys,
    insertPragma,
    canAttachComment,
    isBlockComment,
    printComment,
    getCommentChildNodes,
    hasPrettierIgnore,
    printPrettierIgnored,
    handleComments: {
      ownLine,
      endOfLine,
      remaining,
    },
  },
};
```

###### 打印过程（The printing process）

Prettier 使用一种称为 Doc 的中间表示，然后 Prettier 将其转换为字符串（基于 `printWidth` 等选项）。*printer* 的工作是接收 `parsers[<parser name>].parse` 生成的 AST 并返回一个 Doc。Doc 使用[构建器命令](https://github.com/prettier/prettier/blob/main/commands.md)构造：

```js
import * as prettier from "prettier";

const { join, line, ifBreak, group } = prettier.doc.builders;
```

打印过程由以下步骤组成：

1. **AST 预处理**（可选）。参见 [`preprocess`](#可选-preprocess)。
2. **注释附加**（可选）。参见[在 printer 中处理注释](#在-printer-中处理注释handling-comments-in-a-printer)。
3. **处理嵌入式语言**（可选）。如果定义了 [`embed`](#可选-embed) 方法，则以深度优先的方式为每个节点调用它。出于性能考虑，递归本身是同步的，但 `embed` 可以返回异步函数，这些函数可以调用其他解析器和 printer 来为 CSS-in-JS 等嵌入式语法组合 doc。这些返回的函数会排队并在下一步之前顺序执行。
4. **递归打印**。从 AST 递归构造 doc。从根节点开始：
   - 如果第 3 步中有与当前节点关联的嵌入式语言 doc，则使用该 doc。
   - 否则，调用 `print(path, options, print): Doc` 方法。它为当前节点组合一个 doc，通常通过 `print` 回调打印子节点。

###### `print`

插件 printer 的大部分工作将在其 `print` 函数中进行，其签名为：

```ts
function print(
  // 要打印的 AST 节点的路径
  path: AstPath,
  options: object,
  // 递归打印子节点
  print: (selector?: string | number | Array<string | number> | AstPath) => Doc,
): Doc;
```

`print` 函数接收以下参数：

- **`path`**：一个对象，可用于访问 AST 中的节点。它是一个栈状数据结构，维护递归的当前状态。它被称为"path"，因为它表示从 AST 根到当前节点的路径。当前节点由 `path.node` 返回。
- **`options`**：一个持久对象，包含全局选项，插件可以修改它来存储上下文数据。
- **`print`**：用于打印子节点的回调。此函数包含核心打印逻辑，由一系列步骤组成，其实现由插件提供。特别地，它调用 printer 的 `print` 函数并将自身传给它。因此，这两个 `print` 函数——一个来自核心，一个来自插件——在递归下降 AST 时相互调用。

下面是一个简化的示例，展示 `print` 的典型实现：

```js
import * as prettier from "prettier";

const { group, indent, join, line, softline } = prettier.doc.builders;

function print(path, options, print) {
  const node = path.node;

  switch (node.type) {
    case "list":
      return group([
        "(",
        indent([softline, join(line, path.map(print, "elements"))]),
        softline,
        ")",
      ]);

    case "pair":
      return group([
        "(",
        indent([softline, print("left"), line, ". ", print("right")]),
        softline,
        ")",
      ]);

    case "symbol":
      return node.name;
  }

  throw new Error(`Unknown node type: ${node.type}`);
}
```

查看 [prettier-python 的 printer](https://github.com/prettier/prettier-python/blob/034ba8a9551f3fa22cead41b323be0b28d06d13b/src/printer.js#L174)，了解可以实现的功能示例。

###### （可选）`embed`

printer 可以有 `embed` 方法，用于在一种语言内打印另一种语言。典型的例子是打印 CSS-in-JS 或 Markdown 中的代码块。签名为：

```ts
function embed(
  // 当前 AST 节点的路径
  path: AstPath,
  // 当前选项
  options: Options,
):
  | ((
      // 使用不同的解析器解析并打印传入的文本。
      // 你应设置 `options.parser` 来指定使用哪个解析器。
      textToDoc: (text: string, options: Options) => Promise<Doc>,
      // 用当前 printer 打印当前节点或其后代节点
      print: (
        selector?: string | number | Array<string | number> | AstPath,
      ) => Doc,
      // 以下两个参数是为了方便而传入的。
      // 它们与传给 `embed` 的 `path` 和 `options` 相同。
      path: AstPath,
      options: Options,
    ) => Promise<Doc | undefined> | Doc | undefined)
  | Doc
  | undefined;
```

`embed` 方法与 `print` 方法类似，都将 AST 节点映射为 doc，但与 `print` 不同的是，它有能力通过返回异步函数来做异步工作。该函数的第一个参数——`textToDoc` 异步函数——可用于使用不同的插件渲染 doc。

如果从 `embed` 返回的函数返回一个 doc 或一个解析为 doc 的 Promise，该 doc 将被用于打印，并且不会为此节点调用 `print` 方法。在极少数情况下，直接从 `embed` 同步返回 doc 也是可行的且可能方便，但此时 `textToDoc` 和 `print` 回调不可用。返回一个函数才能获得它们。

如果 `embed` 返回 `undefined`，或者它返回的函数返回 `undefined` 或解析为 `undefined` 的 Promise，该节点将使用 `print` 方法正常打印。如果返回的函数抛出错误或返回被拒绝的 Promise（例如发生解析错误），也会如此。如果你希望 Prettier 重新抛出这些错误，请将 `PRETTIER_DEBUG` 环境变量设置为非空值。

例如，一个包含嵌入式 JavaScript 节点的插件可能有以下 `embed` 方法：

```js
function embed(path, options) {
  const node = path.node;
  if (node.type === "javascript") {
    return async (textToDoc) => {
      return [
        "<script>",
        hardline,
        await textToDoc(node.javaScriptCode, { parser: "babel" }),
        hardline,
        "</script>",
      ];
    };
  }
}
```

如果 [`--embedded-language-formatting`](https://prettier.io/docs/options#embedded-language-formatting) 选项设置为 `off`，嵌入步骤将被完全跳过，`embed` 不会被调用，所有节点都使用 `print` 方法打印。

###### （可选）`preprocess`

`preprocess` 方法可以在将解析器产生的 AST 传入 `print` 方法之前对其进行处理。

```ts
function preprocess(ast: AST, options: Options): AST | Promise<AST>;
```

###### （可选）`getVisitorKeys`

如果插件使用注释附加或嵌入式语言，这个属性可能会派上用场。这些功能会从根开始遍历 AST，迭代每个节点的所有自有可枚举属性。如果 AST 有[环](<https://en.wikipedia.org/wiki/Cycle_(graph_theory)>)，这样的遍历会陷入无限循环。此外，节点可能包含非节点对象（例如位置数据），迭代它们是对资源的浪费。为了解决这些问题，printer 可以定义一个函数来返回应该被遍历的属性名。

其签名为：

```ts
function getVisitorKeys(node, nonTraversableKeys: Set<string>): string[];
```

默认的 `getVisitorKeys`：

```js
function getVisitorKeys(node, nonTraversableKeys) {
  return Object.keys(node).filter((key) => !nonTraversableKeys.has(key));
}
```

第二个参数 `nonTraversableKeys` 是一个包含常见键和 prettier 内部使用的键的集合。

如果你有完整的 visitor keys 列表：

```js
const visitorKeys = {
  Program: ["body"],
  Identifier: [],
  // ...
};

function getVisitorKeys(node /* , nonTraversableKeys*/) {
  // 对未知节点返回 `[]`，防止 Prettier 回退到使用 `Object.keys()`
  return visitorKeys[node.type] ?? [];
}
```

如果你只需要排除一小部分键：

```js
const ignoredKeys = new Set(["prev", "next", "range"]);

function getVisitorKeys(node, nonTraversableKeys) {
  return Object.keys(node).filter(
    (key) => !nonTraversableKeys.has(key) && !ignoredKeys.has(key),
  );
}
```

###### （可选）`insertPragma`

插件可以在 `insertPragma` 函数中实现当使用 `--insert-pragma` 选项时如何将 pragma 注释插入生成的代码。其签名为：

```ts
function insertPragma(text: string): string;
```

###### 在 printer 中处理注释（Handling comments in a printer）

注释通常不是语言 AST 的一部分，这对 pretty printer 来说是一个挑战。Prettier 插件可以在其 `print` 函数中自己打印注释，或者依赖 Prettier 的注释算法。

要使用 Prettier 的注释算法，请在 AST 根的 `comments` 属性中返回注释节点数组，并在你的 printer 上同时实现 `canAttachComment` 和 `printComment`。解析器的 `locStart` 和 `locEnd` 函数必须既适用于注释节点也适用于其他 AST 节点。给每个注释一个字符串 `value`，这样如果注释未被打印，Prettier 可以在错误消息中识别它。

Prettier 从 AST 根移除 `comments`，然后根据每个注释的位置将其附加到某个节点。这会改变 AST。Prettier 调用 `printComment` 来打印节点输出前后的前导和尾随注释。你的 printer 负责放置悬空注释（dangling comments）。

###### （可选）`getCommentChildNodes`

默认情况下，Prettier 递归搜索每个节点的所有对象属性（少数预定义属性除外）。可以提供此函数来覆盖该行为。其签名为：

```ts
function getCommentChildNodes(
  // 应返回其子节点的节点。
  node: AST,
  // 当前选项
  options: object,
): AST[] | undefined;
```

如果节点没有子节点则返回 `[]`，返回 `undefined` 则回退到默认行为。

##### （可选）`hasPrettierIgnore`

```ts
function hasPrettierIgnore(path: AstPath): boolean;
```

返回 AST 节点是否被标记为 `prettier-ignore`。

##### （可选）`printPrettierIgnored`

如果 AST 节点被标记为 `prettier-ignore`，默认情况下 Prettier 会直接截取文本而不调用 `print` 函数；但插件也可以通过添加此属性来处理 `prettier-ignore` 节点的打印。

此属性与 `print` 属性具有相同的签名。

_最早在 v3.7.0 可用_

###### （可选）`printComment`

使用 Prettier 的注释打印时，返回注释节点的 Doc。其签名为：

```ts
function printComment(
  // 当前注释节点的路径
  commentPath: AstPath,
  // 当前选项
  options: object,
): Doc;
```

###### （可选）`canAttachComment`

```ts
function canAttachComment(node: AST, ancestors: T[]): boolean;
```

此函数用于决定注释是否可以附加到特定的 AST 节点。默认情况下，会遍历*所有* AST 属性以搜索可以附加注释的节点。此函数用于阻止注释被附加到特定节点。一个典型的实现如下：

```js
function canAttachComment(node, [parent]) {
  return !(
    node.type === "comment" ||
    (parent?.type === "shorthand-property" &&
      parent.key === node &&
      parent.key !== parent.value)
  );
}
```

_第二个参数 `ancestors` 最早在 v3.7.0 中添加。_

###### （可选）`isBlockComment`

```ts
function isBlockComment(node: AST): boolean;
```

返回 AST 节点是否为块注释。

###### （可选）`handleComments`

`handleComments` 对象包含三个可选函数，每个函数的签名为：

```ts
(
  // 注释对应的 AST 节点
  comment: AST,
  // 完整源代码文本
  text: string,
  // 全局选项对象
  options: object,
  // AST
  ast: AST,
  // 此注释是否为最后一个注释
  isLastComment: boolean,
) => boolean;
```

这些函数覆盖 Prettier 的默认注释附加行为。手动附加注释后返回 `true`，或返回 `false` 让 Prettier 附加它。

Prettier 检查注释周围的换行符，忽略空格和制表符，并考虑相邻的注释。它会调用：

- `ownLine`：如果在注释之前找到换行符。
- `endOfLine`：如果在注释之后找到换行符但之前没有。
- `remaining`：其他情况，例如同一行上两个 token 之间的块注释。

调用这些函数时，Prettier 通过注释上的 `enclosingNode`、`precedingNode` 和 `followingNode` 提供附近的 AST 节点。其中一些或全部可能不存在，例如当文件只包含注释时。

###### 手动附加注释（Manually attaching a comment）

`handleComments` 回调根据注释在源文本中的位置选择。附加注释时，选择它相对于目标节点的打印方式：

- `prettier.util.addLeadingComment(node, comment)` 附加一个在节点之前打印的注释。
- `prettier.util.addTrailingComment(node, comment)` 附加一个在节点之后打印的注释。
- `prettier.util.addDanglingComment(node, comment)` 附加一个其位置由节点的 printer 决定的注释，例如空块内的注释。

例如，`ownLine` 回调可以将注释附加为 `punctuation` 节点（本示例虚构的节点类型）的尾随注释：

```js
import * as prettier from "prettier";

function ownLine(comment, text, options, ast, isLastComment) {
  const { precedingNode } = comment;
  if (precedingNode?.type === "punctuation") {
    return prettier.util.addTrailingComment(precedingNode, comment);
  }
  return false;
}
```

:::warning

`prettier.util.add{Trailing,Leading,Dangling}Comment()` 在 v3.10 之前返回 `void`；如果需要支持旧版本的 Prettier，请在函数调用后显式 `return true`。

```diff
- return prettier.util.addTrailingComment(precedingNode, comment);
+ prettier.util.addTrailingComment(precedingNode, comment);
+ return true;
```

或者

```diff
- return prettier.util.addTrailingComment(precedingNode, comment);
+ return prettier.util.addTrailingComment(precedingNode, comment) ?? true;
```

:::

带有注释的节点预期有一个 `comments` 属性，其中包含注释数组。每个注释预期具有以下属性：`leading`、`trailing`、`printed`。

上面的示例使用了 `prettier.util.addTrailingComment`，它会自动将 `comment.leading`/`trailing`/`printed` 设置为适当的值，并将注释添加到 AST 节点的 `comments` 数组中。

###### 打印悬空注释（Printing dangling comments）

打印节点时，处理其既没有设置 `leading` 也没有设置 `trailing` 的注释。将每个悬空注释的 Doc 添加到它所属的位置，并设置 `comment.printed = true`。例如，空块的 printer 可以像这样复用其 `printComment` 函数：

```js
import { doc } from "prettier";

const { hardline, indent, join } = doc.builders;

function printEmptyBlock(path, options) {
  const dangling = [];
  if (path.node.comments) {
    path.each((commentPath) => {
      const comment = commentPath.node;
      if (!comment.leading && !comment.trailing) {
        comment.printed = true;
        dangling.push(printComment(commentPath, options));
      }
    }, "comments");
  }
  return dangling.length === 0
    ? "{}"
    : ["{", indent([hardline, join(hardline, dangling)]), hardline, "}"];
}
```

`--debug-print-comments` CLI 标志可以帮助调试注释附加问题。它打印注释的详细列表，包括每个注释如何分类（`ownLine`/`endOfLine`/`remaining`、`leading`/`trailing`/`dangling`）以及附加到了哪个节点的信息。对于 Prettier 的内置语言，此信息也可在 Playground 上查看（Debug 部分的 'show comments' 复选框）。

##### `options`

`options` 是一个对象，包含你的插件支持的自定义选项。

示例：

```js
// ...插件实现

export const options = {
  openingBraceNewLine: {
    type: "boolean",
    category: "Global",
    default: true,
    description: "Move open brace for code blocks onto new line.",
  },
};
```

##### `defaultOptions`

如果你的插件需要对 Prettier 的某些核心选项使用不同的默认值，可以在 `defaultOptions` 中指定：

```js
// ...插件实现

export const defaultOptions = {
  tabWidth: 4,
};
```

##### 工具函数（Utility functions）

`prettier.util` 为插件提供以下有限的工具函数集：

```js
import * as prettier from "prettier";

const { getStringWidth } = prettier.util;

console.log(getStringWidth("prettier"));
// -> 8
```

```ts
type Quote = '"' | "'";
type SkipOptions = { backwards?: boolean };

function getMaxContinuousCount(text: string, searchString: string): number;

function getStringWidth(text: string): number;

function getAlignmentSize(
  text: string,
  tabWidth: number,
  startIndex?: number,
): number;

function getIndentSize(value: string, tabWidth: number): number;

function skip(
  characters: string | RegExp,
): (
  text: string,
  startIndex: number | false,
  options?: SkipOptions,
) => number | false;

function skipWhitespace(
  text: string,
  startIndex: number | false,
  options?: SkipOptions,
): number | false;

function skipSpaces(
  text: string,
  startIndex: number | false,
  options?: SkipOptions,
): number | false;

function skipToLineEnd(
  text: string,
  startIndex: number | false,
  options?: SkipOptions,
): number | false;

function skipEverythingButNewLine(
  text: string,
  startIndex: number | false,
  options?: SkipOptions,
): number | false;

function skipInlineComment(
  text: string,
  startIndex: number | false,
): number | false;

function skipTrailingComment(
  text: string,
  startIndex: number | false,
): number | false;

function skipNewline(
  text: string,
  startIndex: number | false,
  options?: SkipOptions,
): number | false;

function hasNewline(
  text: string,
  startIndex: number,
  options?: SkipOptions,
): boolean;

function hasNewlineInRange(
  text: string,
  startIndex: number,
  startIndex: number,
): boolean;

function hasSpaces(
  text: string,
  startIndex: number,
  options?: SkipOptions,
): boolean;

function getPreferredQuote(
  text: string,
  preferredQuoteOrPreferSingleQuote: Quote | boolean,
): Quote;

function makeString(
  rawText: string,
  enclosingQuote: Quote,
  unescapeUnnecessaryEscapes?: boolean,
): string;

function getNextNonSpaceNonCommentCharacter(
  text: string,
  startIndex: number,
): string;

function getNextNonSpaceNonCommentCharacterIndex(
  text: string,
  startIndex: number,
): number | false;

function isNextLineEmpty(text: string, startIndex: number): boolean;

function isPreviousLineEmpty(text: string, startIndex: number): boolean;
```

##### 教程（Tutorials）

- [如何为 Prettier 编写插件](https://medium.com/@fvictorio/how-to-write-a-plugin-for-prettier-a0d98c845e70)：教你如何为 TOML 编写一个非常基础的 Prettier 插件。

#### 测试插件（Testing Plugins）

由于插件可以使用相对路径解析，在开发插件时你可以这样做：

```js
import * as prettier from "prettier";

const code = "(add 1 2)";
await prettier.format(code, {
  parser: "lisp",
  plugins: ["./index.js"],
});
```

这将相对于当前工作目录解析插件。
