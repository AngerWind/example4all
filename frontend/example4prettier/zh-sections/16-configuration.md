### 配置文件（Configuration File）

> 原文链接：https://prettier.io/docs/configuration

你可以通过以下方式配置 Prettier（按优先级顺序）：

- `package.json` 或 [`package.yaml`](https://github.com/pnpm/pnpm/pull/1799) 文件中的 `"prettier"` 键。
- 用 JSON 或 YAML 编写的 `.prettierrc` 文件。
- `.prettierrc.json`、`.prettierrc.yml`、`.prettierrc.yaml` 或 `.prettierrc.json5` 文件。
- 使用 `export default` 或 `module.exports` 导出对象的 `.prettierrc.js`、`prettier.config.js`、`.prettierrc.ts` 或 `prettier.config.ts` 文件（取决于你 `package.json` 中的 [`type`](https://nodejs.org/api/packages.html#type) 值）。
- 使用 `export default` 导出对象的 `.prettierrc.mjs`、`prettier.config.mjs`、`.prettierrc.mts` 或 `prettier.config.mts` 文件。
- 使用 `module.exports` 导出对象的 `.prettierrc.cjs`、`prettier.config.cjs`、`.prettierrc.cts` 或 `prettier.config.cts` 文件。
- `.prettierrc.toml` 文件。

:::info

TypeScript 配置文件的支持需要[额外设置](#typescript-配置文件typescript-configuration-files)

:::

配置文件将从被格式化文件的位置开始解析，并向上搜索文件树，直到找到（或未找到）配置文件。

Prettier 有意不支持任何形式的全局配置。这是为了确保当项目被复制到另一台计算机时，Prettier 的行为保持不变。否则，Prettier 将无法保证团队中的每个人都得到同样一致的结果。

你可以在配置文件中使用的选项与 [API 选项](https://prettier.io/docs/options)相同。

#### TypeScript 配置文件（TypeScript Configuration Files）

TypeScript 支持需要 Node.js>=22.6.0，并且在 Node.js v24.3.0 之前需要 `--experimental-strip-types` 来运行 Node.js。

```sh
node --experimental-strip-types node_modules/prettier/bin/prettier.cjs . --write
```

或

```sh
NODE_OPTIONS="--experimental-strip-types" prettier . --write
```

#### 基本配置（Basic Configuration）

JSON：

```json title=".prettierrc.json or .prettierrc"
{
  "trailingComma": "es5",
  "tabWidth": 4,
  "semi": false,
  "singleQuote": true
}
```

JS（ES Modules）：

```js title="prettier.config.mjs, .prettierrc.mjs, prettier.config.js, or .prettierrc.js"
/**
 * @see https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
const config = {
  trailingComma: "es5",
  tabWidth: 4,
  semi: false,
  singleQuote: true,
};

export default config;
```

JS（CommonJS）：

```js title="prettier.config.cjs, .prettierrc.cjs, prettier.config.js, or .prettierrc.js"
/**
 * @see https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
const config = {
  trailingComma: "es5",
  tabWidth: 4,
  semi: false,
  singleQuote: true,
};

module.exports = config;
```

TypeScript（ES Modules）：

```ts title="prettier.config.mts, .prettierrc.mts, prettier.config.ts, or .prettierrc.ts"
import { type Config } from "prettier";

const config: Config = {
  trailingComma: "none",
};

export default config;
```

TypeScript（CommonJS）：

```ts title="prettier.config.cts, .prettierrc.cts, prettier.config.ts, or .prettierrc.ts"
import { type Config } from "prettier";

const config: Config = {
  trailingComma: "none",
};

module.exports = config;
```

YAML：

```yaml title=".prettierrc, .prettierrc.yml, or .prettierrc.yaml"
trailingComma: "es5"
tabWidth: 4
semi: false
singleQuote: true
```

TOML：

```toml title=".prettierrc.toml"
trailingComma = "es5"
tabWidth = 4
semi = false
singleQuote = true
```

#### 配置覆盖（Configuration Overrides）

overrides 让你可以为特定的文件扩展名、文件夹和特定文件使用不同的配置。

JSON：

```json title=".prettierrc"
{
  "semi": false,
  "overrides": [
    {
      "files": ["*.test.js"],
      "options": {
        "semi": true
      }
    },
    {
      "files": ["*.html", "legacy/**/*.js"],
      "options": {
        "tabWidth": 4
      }
    }
  ]
}
```

YAML：

```yaml title=".prettierrc"
semi: false
overrides:
  - files:
      - "*.test.js"
    options:
      semi: true
  - files:
      - "*.html"
      - "legacy/**/*.js"
    options:
      tabWidth: 4
```

每个 override 都必须有 `files`，可以是字符串或字符串数组。可以选择性地提供 `excludeFiles` 来为给定规则排除文件，也可以是字符串或字符串数组。

`files` 模式是相对于包含配置文件的目录（而不是当前工作目录）来匹配文件路径的，所以 `legacy/**/*.js` 只匹配该配置文件旁边的 `legacy/` 目录内的文件。不带 `/` 的模式（如 `*.html`）只匹配文件名本身，因此在任何深度都适用。

#### 设置 [parser](https://prettier.io/docs/options#parser) 选项（Setting the parser option）

默认情况下，Prettier 根据输入文件的扩展名自动推断使用哪个解析器。结合 `overrides`，你可以教 Prettier 如何解析它不认识文件。

例如，要让 Prettier 格式化它自己的 `.prettierrc` 文件，你可以这样做：

```json title=".prettierrc"
{
  "overrides": [
    {
      "files": [".prettierrc"],
      "options": {
        "parser": "json"
      }
    }
  ]
}
```

你也可以为 .js 文件切换到 `flow` 解析器，而不是默认的 `babel`：

```json title=".prettierrc"
{
  "overrides": [
    {
      "files": ["*.js"],
      "options": {
        "parser": "flow"
      }
    }
  ]
}
```

**注意：***永远不要*把 `parser` 选项放在配置的顶层。*只能*在 `overrides` 内使用它。否则你实际上禁用了 Prettier 基于文件扩展名的自动解析器推断。这会迫使 Prettier 对*所有*类型的文件使用你指定的解析器——即使这不合理，比如试图把 CSS 文件当作 JavaScript 来解析。

#### 配置 Schema（Configuration Schema）

如果你想要一个 JSON schema 来验证你的配置，可以在这里找到：[https://www.schemastore.org/prettierrc.json](https://www.schemastore.org/prettierrc.json)。

#### EditorConfig

如果你的项目中有 [`.editorconfig` 文件](https://editorconfig.org/)，Prettier 将解析它并将其属性转换为相应的 Prettier 配置。此配置将被 `.prettierrc` 等覆盖。

:::note

与 EditorConfig 规范不同，`.editorconfig` 文件的搜索会在项目根目录停止，不会继续向上。

:::

下面是对不同属性如何映射到 Prettier 行为的带注释说明：

```ini title=".editorconfig"
# 阻止编辑器在父目录中查找 .editorconfig 文件
# root = true

[*]
# 不可配置的 Prettier 行为
charset = utf-8
insert_final_newline = true
# 注意：Prettier 不会修剪模板字符串内的行尾空白，但你的编辑器可能会。
# trim_trailing_whitespace = true

# 可配置的 Prettier 行为
#（如果你的 Prettier 配置不同，请更改这些）
end_of_line = lf
indent_style = space
indent_size = 2
max_line_length = 80
```

如果你使用默认选项，这是一个可以直接复制粘贴的 `.editorconfig` 文件：

```ini title=".editorconfig"
[*]
charset = utf-8
insert_final_newline = true
end_of_line = lf
indent_style = space
indent_size = 2
max_line_length = 80
```
