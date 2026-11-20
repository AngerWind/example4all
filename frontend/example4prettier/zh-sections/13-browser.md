### 浏览器（Browser）

> 原文链接：https://prettier.io/docs/browser

使用 Prettier 的 **standalone（独立）**版本在浏览器中运行 Prettier。此版本不依赖 Node.js。它只格式化代码，不支持配置文件、忽略文件、CLI 用法或插件的自动加载。

独立版本以以下形式提供：

- ES modules：`standalone.mjs`，从 3.0 版本开始（在版本 2 中为 `esm/standalone.mjs`）。
- UMD：`standalone.js`，从 1.13 版本开始。

Prettier 的 `package.json` 中的 [`browser` 字段](https://github.com/defunctzombie/package-browser-field-spec)指向 `standalone.js`。这就是为什么你可以直接 `import` 或 `require` `prettier` 模块来访问 Prettier 的 API，并且只要使用 webpack 或其他支持 `browser` 字段的打包器，你的代码就可以同时兼容 Node 和浏览器。这对[插件](https://prettier.io/docs/plugins)来说尤其方便。

#### `prettier.format(code, options)`

必需选项：

- **[`parser`](https://prettier.io/docs/options#parser)（或 [`filepath`](https://prettier.io/docs/options#file-path)）**：必须指定这些选项之一，Prettier 才能知道使用哪个解析器。

- **`plugins`**：与[基于 Node.js 的 API](https://prettier.io/docs/api#prettierformatsource-options) 中的 `format` 函数不同，此函数不会自动加载插件。`plugins` 选项是必需的，因为 Prettier 包中包含的所有解析器都以插件形式提供（出于文件大小的考虑）。这些插件是 [https://unpkg.com/browse/prettier/plugins](https://unpkg.com/browse/prettier/plugins) 中的文件。请注意，打印 JavaScript、TypeScript、Flow 或 JSON 时应加载 `estree` 插件。

  你需要加载你要使用的插件，并使用 `plugins` 选项将它们传给 `prettier.format`。

示例见下文。

#### 用法（Usage）

##### 全局方式（Global）

```html
<script src="https://unpkg.com/prettier/standalone.js"></script>
<script src="https://unpkg.com/prettier/plugins/graphql.js"></script>
<script>
  (async () => {
    const formatted = await prettier.format("type Query { hello: String }", {
      parser: "graphql",
      plugins: prettierPlugins,
    });
  })();
</script>
```

请注意，Prettier 的 `package.json` 中的 [`unpkg` 字段](https://unpkg.com/#examples)指向 `standalone.js`，这就是为什么也可以使用 `https://unpkg.com/prettier` 代替 `https://unpkg.com/prettier/standalone.js`。

##### ES Modules

```html
<script type="module">
  import * as prettier from "https://unpkg.com/prettier/standalone.mjs";
  import * as prettierPluginGraphql from "https://unpkg.com/prettier/plugins/graphql.mjs";

  const formatted = await prettier.format("type Query { hello: String }", {
    parser: "graphql",
    plugins: [prettierPluginGraphql],
  });
</script>
```

##### AMD

```js
define([
  "https://unpkg.com/prettier/standalone.js",
  "https://unpkg.com/prettier/plugins/graphql.js",
], async (prettier, ...plugins) => {
  const formatted = await prettier.format("type Query { hello: String }", {
    parser: "graphql",
    plugins,
  });
});
```

##### CommonJS

```js
const prettier = require("prettier/standalone");
const prettierPluginGraphql = require("prettier/plugins/graphql");

(async () => {
  const formatted = await prettier.format("type Query { hello: String }", {
    parser: "graphql",
    plugins: [prettierPluginGraphql],
  });
})();
```

此语法不一定能在浏览器中工作，但可以在用 browserify、Rollup、webpack 或其他打包器打包代码时使用。

##### Worker

```js
// Module worker
import * as prettier from "https://unpkg.com/prettier/standalone.mjs";
import * as prettierPluginGraphql from "https://unpkg.com/prettier/plugins/graphql.mjs";

const formatted = await prettier.format("type Query { hello: String }", {
  parser: "graphql",
  plugins: [prettierPluginGraphql],
});
```

```js
// Classic worker
importScripts(
  "https://unpkg.com/prettier/standalone.js",
  "https://unpkg.com/prettier/plugins/graphql.js",
);

(async () => {
  const formatted = await prettier.format("type Query { hello: String }", {
    parser: "graphql",
    plugins: prettierPlugins,
  });
})();
```

#### 用于嵌入式代码的解析器插件（Parser plugins for embedded code）

如果你想格式化[嵌入式代码](https://prettier.io/docs/options#embedded-language-formatting)，还需要加载相关的插件。例如：

```html
<script type="module">
  import * as prettier from "https://unpkg.com/prettier/standalone.mjs";
  import * as prettierPluginBabel from "https://unpkg.com/prettier/plugins/babel.mjs";
  import * as prettierPluginEstree from "https://unpkg.com/prettier/plugins/estree.mjs";

  console.log(
    await prettier.format("const html=/* HTML */ `<DIV> </DIV>`", {
      parser: "babel",
      plugins: [prettierPluginBabel, prettierPluginEstree],
    }),
  );
  // 输出：const html = /* HTML */ `<DIV> </DIV>`;
</script>
```

嵌入在 JavaScript 中的 HTML 代码保持未格式化状态，因为 `html` 解析器没有被加载。正确用法：

```html
<script type="module">
  import * as prettier from "https://unpkg.com/prettier/standalone.mjs";
  import * as prettierPluginBabel from "https://unpkg.com/prettier/plugins/babel.mjs";
  import * as prettierPluginEstree from "https://unpkg.com/prettier/plugins/estree.mjs";
  import * as prettierPluginHtml from "https://unpkg.com/prettier/plugins/html.mjs";

  console.log(
    await prettier.format("const html=/* HTML */ `<DIV> </DIV>`", {
      parser: "babel",
      plugins: [prettierPluginBabel, prettierPluginEstree, prettierPluginHtml],
    }),
  );
  // 输出：const html = /* HTML */ `<div></div>`;
</script>
```
