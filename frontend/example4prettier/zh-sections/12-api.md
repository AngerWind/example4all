### API（API）

> 原文链接：https://prettier.io/docs/api

如果你想以编程方式运行 Prettier，请查看本页。

```js
import * as prettier from "prettier";
```

我们的公共 API 都是异步的。如果出于某种原因你必须使用同步版本，可以尝试 [`@prettier/sync`](https://github.com/prettier/prettier-synchronized)。

#### `prettier.format(source, options)`

`format` 用于使用 Prettier 格式化文本。必须根据你要格式化的语言设置 `options.parser`（参见[可用解析器列表](https://prettier.io/docs/options#parser)）。或者，可以指定 `options.filepath`，让 Prettier 从文件扩展名推断解析器。可以提供其他[选项](https://prettier.io/docs/options)来覆盖默认值。

```js
await prettier.format("foo ( );", { semi: false, parser: "babel" });
// -> 'foo()\n'
```

#### `prettier.check(source [, options])`

`check` 检查文件是否已使用给定选项通过 Prettier 格式化，并返回 `Promise<boolean>`。这类似于 CLI 中的 `--check` 或 `--list-different` 参数，适用于在 CI 场景中运行 Prettier。

#### `prettier.formatWithCursor(source [, options])`

`formatWithCursor` 既格式化代码，又将光标位置从未格式化的代码转换到格式化后的代码。这对编辑器集成很有用，可以防止格式化代码时光标移动。

应提供 `cursorOffset` 选项来指定光标位置。

```js
await prettier.formatWithCursor(" 1", { cursorOffset: 2, parser: "babel" });
// -> { formatted: '1;\n', cursorOffset: 1 }
```

#### `prettier.resolveConfig(fileUrlOrPath [, options])`

`resolveConfig` 可用于解析给定源文件的配置，将其路径或 url 作为第一个参数传递。配置搜索将从文件所在目录开始，并继续向上搜索目录。如果你不想搜索，也可以直接通过 `options.config` 传入配置文件的路径。返回一个 Promise，解析为：

- 一个选项对象（如果找到了[配置文件](https://prettier.io/docs/configuration)）。
- `null`（如果未找到文件）。

如果解析配置文件时出错，Promise 将被拒绝。

如果 `options.useCache` 为 `false`，所有缓存将被绕过。

```js
const text = await fs.readFile(filePath, "utf8");
const options = await prettier.resolveConfig(filePath);
const formatted = await prettier.format(text, {
  ...options,
  filepath: filePath,
});
```

如果 `options.editorconfig` 为 `true` 且你的项目中有 [`.editorconfig` 文件](https://editorconfig.org/)，Prettier 将解析它并将其属性转换为相应的 Prettier 配置。此配置将被 `.prettierrc` 等覆盖。目前支持以下 EditorConfig 属性：

- `end_of_line`
- `indent_style`
- `indent_size`/`tab_width`
- `max_line_length`

#### `prettier.resolveConfigFile([fileUrlOrPath])`

`resolveConfigFile` 可用于查找解析配置时（即调用 `resolveConfig` 时）将使用的 Prettier 配置文件的路径。返回一个 Promise，解析为：

- 配置文件的路径。
- `null`（如果未找到文件）。

如果解析配置文件时出错，Promise 将被拒绝。

搜索从 `process.cwd()` 开始，如果提供了 `fileUrlOrPath`，则从其所在目录开始。

```js
const configFile = await prettier.resolveConfigFile(filePath);
// 你得到了配置文件的路径
```

#### `prettier.clearConfigCache()`

当 Prettier 加载配置文件和插件时，会缓存文件系统结构以提高性能。此函数将清除缓存。一般只有编辑器集成才需要它——它们知道自上次格式化以来文件系统已发生变化。

#### `prettier.getFileInfo(fileUrlOrPath [, options])`

`getFileInfo` 可供编辑器扩展用来决定某个特定文件是否需要格式化。此方法返回一个 Promise，解析为具有以下属性的对象：

```ts
{
  ignored: boolean;
  inferredParser: string | null;
}
```

如果 `fileUrlOrPath` 的类型不是 `string` 或 `URL`，Promise 将被拒绝。

设置 `options.ignorePath`（`string | URL | (string | URL)[]`）和 `options.withNodeModules`（`boolean`）会影响 `ignored` 的值（默认为 `false`）。

如果给定的 `fileUrlOrPath` 被忽略，`inferredParser` 始终为 `null`。

在 `options.plugins`（`(string | URL | Plugin)[]`）中提供[插件](https://prettier.io/docs/plugins)路径，有助于为 Prettier 核心不支持的文件提取 `inferredParser`。

将 `options.resolveConfig`（`boolean`，默认 `true`）设置为 `false` 时，Prettier 将不会搜索配置文件。如果此函数仅用于检查文件是否被忽略，这会很有用。

#### `prettier.getSupportInfo()`

返回一个 Promise，解析为一个表示 Prettier 支持的选项、解析器、语言和文件类型的对象。

支持信息如下所示：

```ts
{
  languages: Array<{
    name: string;
    parsers: string[];
    group?: string;
    tmScope?: string;
    aceMode?: string;
    codemirrorMode?: string;
    codemirrorMimeType?: string;
    aliases?: string[];
    extensions?: string[];
    filenames?: string[];
    linguistLanguageId?: number;
    vscodeLanguageIds?: string[];
    isSupported?(options: { filepath: string }): boolean;
  }>;
}
```

:::note

Prettier 无法确保 `filepath` 存在于磁盘上。
从 API（如 `prettier.format()`）使用时，Prettier 也无法确保它是有效路径。

:::

<a name="custom-parser-api"></a>

#### 自定义解析器 API（已移除）（Custom Parser API (removed)）

_在 v3.0.0 中移除（由插件 API 取代）_

在[插件](https://prettier.io/docs/plugins)出现之前，Prettier 有一个类似但更受限的特性，叫做自定义解析器。它在 v3.0.0 中被移除，因为其功能是插件 API 的子集。如果你用过它，请查看下面关于如何迁移的示例。

❌ 自定义解析器 API（已移除）：

```js
import { format } from "prettier";

format("lodash ( )", {
  parser(text, { babel }) {
    const ast = babel(text);
    ast.program.body[0].expression.callee.name = "_";
    return ast;
  },
});
// -> "_();\n"
```

✔️ 插件 API：

```js
import { format } from "prettier";
import * as prettierPluginBabel from "prettier/plugins/babel";

const myCustomPlugin = {
  parsers: {
    "my-custom-parser": {
      async parse(text) {
        const ast = await prettierPluginBabel.parsers.babel.parse(text);
        ast.program.body[0].expression.callee.name = "_";
        return ast;
      },
      astFormat: "estree",
    },
  },
};

await format("lodash ( )", {
  parser: "my-custom-parser",
  plugins: [myCustomPlugin],
});
// -> "_();\n"
```

:::note

总的来说，不建议用这种方式做 codemod。Prettier 使用 AST 节点的位置数据来做许多事情，比如保留空行和附加注释。当 AST 在解析后被修改时，位置数据往往会失同步，这可能导致不可预测的结果。如果你需要 codemod，请考虑使用 [jscodeshift](https://github.com/facebook/jscodeshift)。

:::

作为已移除的自定义解析器 API 的一部分，之前可以通过 `--parser` 选项传递导出 `parse` 函数的模块路径。请改用 `--plugin` CLI 选项或 `plugins` API 选项来[加载插件](https://prettier.io/docs/plugins#using-plugins)。
