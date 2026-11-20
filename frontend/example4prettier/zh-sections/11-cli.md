### CLI（CLI）

> 原文链接：https://prettier.io/docs/cli

使用 `prettier` 命令从命令行运行 Prettier。

```bash
prettier [options] [file/dir/glob ...]
```

:::note

要运行你本地安装的 Prettier 版本，在命令前加上 `npx`、`yarn exec`、`pnpm exec`、`bunx` 或 `deno x`，即 `npx prettier --help`、`yarn exec prettier --help`、`pnpm exec prettier --help`、`bunx prettier --help` 或 `deno x prettier --help`。

:::

要就地格式化文件，使用 `--write`。（注意：这会覆盖你的文件！）

在实践中，可能像这样：

```bash
prettier . --write
```

此命令格式化当前目录及其子目录中 Prettier 支持的所有文件。

建议始终确保 `prettier --write .` 只格式化你项目中想要的内容。使用 [`.prettierignore`](https://prettier.io/docs/ignore) 文件来忽略不应格式化的内容。

一个更复杂的例子：

```bash
prettier docs package.json "{app,__{tests,mocks}__}/**/*.js" --write --single-quote --trailing-comma all
```

:::warning

别忘了 glob 周围的**引号**！引号确保由 Prettier CLI 而不是你的 shell 来展开 glob，这对跨平台使用很重要。

:::

:::note

对于 `--single-quote` 和 `--trailing-comma` 这样的格式化选项，最好使用[配置文件](https://prettier.io/docs/configuration)而不是作为 CLI 标志传递。这样 Prettier CLI、[编辑器集成](https://prettier.io/docs/editors)和其他工具都能知道你使用了哪些选项。

:::

#### 文件模式（File patterns）

给定一个路径/模式列表，Prettier CLI 首先将其中每一项视为字面路径。

- 如果路径指向一个已存在的文件，Prettier CLI 直接处理该文件，不会将路径解析为 glob 模式。

- 如果路径指向一个已存在的目录，Prettier CLI 递归查找该目录中支持的文件。此解析过程基于 Prettier 及其[插件](https://prettier.io/docs/plugins)与支持的语言关联的文件扩展名和众所周知的文件名。

- 否则，该条目将使用 [`fast-glob` 模块的 glob 语法](https://github.com/mrmlnc/fast-glob#pattern-syntax)解析为 glob 模式。

Prettier CLI 会忽略位于 `node_modules` 目录中的文件。要退出此行为，使用 `--with-node-modules` 标志。

Prettier CLI 在展开参数时不会跟随符号链接。

要转义 glob 中的特殊字符，可以使用两种转义语法之一：`prettier "\[my-dir]/*.js"` 或 `prettier "[[]my-dir]/*.js"`。两者都匹配名为 `[my-dir]` 的目录中的所有 JS 文件，但后者更可取，因为前者在 Windows 上不起作用（Windows 中反斜杠被视为路径分隔符）。

#### `--check`

当你想检查文件是否已格式化时，可以带 `--check` 标志（或 `-c`）运行 Prettier。
这将输出一条人类友好的消息和未格式化文件的列表（如果有）。

```bash
prettier . --check
```

所有文件都已格式化时的控制台输出：

```console
Checking formatting...
All matched files use Prettier code style!
```

部分文件需要重新格式化时的控制台输出：

```console
Checking formatting...
[warn] src/fileA.js
[warn] src/fileB.js
[warn] Code style issues found in 2 files. Run Prettier with --write to fix.
```

第二种情况下该命令将返回退出码 `1`，这在 CI 流水线中很有用。
人类友好的状态消息帮助项目贡献者对可能的问题做出反应。
为了尽量减少 `prettier --check` 发现未格式化文件的次数，你可能会对在你的仓库中配置 [pre-commit hook](https://prettier.io/docs/precommit) 感兴趣。
应用这种做法将最大限度地减少 CI 因代码格式化问题而失败的次数。

如果你需要将未格式化文件列表通过管道传给另一个命令，可以使用 [`--list-different`](#--list-different) 标志代替 `--check`。

##### 退出码（Exit codes）

| 代码 | 含义                     |
| ---- | ------------------------ |
| `0`  | 一切格式化正常           |
| `1`  | 有内容未正确格式化       |
| `2`  | Prettier 出了问题        |

#### `--debug-check`

如果你担心 Prettier 会改变代码的正确性，在命令中添加 `--debug-check`。这将使 Prettier 在检测到代码正确性可能已改变时打印错误消息。请注意，`--write` 不能与 `--debug-check` 一起使用。

#### `--find-config-path` 和 `--config`

如果你反复使用 `prettier` 格式化单个文件，当 Prettier 尝试查找[配置文件](https://prettier.io/docs/configuration)时会产生一点性能开销。为了跳过这一步，你可以让 Prettier 找一次配置文件，之后复用它。

```console
$ prettier --find-config-path path/to/file.js
path/to/.prettierrc
```

这将提供配置文件的路径，你可以将其传给 `--config`：

```bash
prettier path/to/file.js --write --config path/to/.prettierrc
```

如果你的配置文件位于 Prettier 找不到的地方（例如 `config/` 目录），你也可以使用 `--config`。

如果你没有配置文件，或者想忽略已存在的配置文件，可以改为传递 `--no-config`。

#### `--ignore-path`

包含描述要忽略文件的模式的文件路径。默认情况下，Prettier 查找 `./.gitignore` 和 `./.prettierignore`。

通过提供多个 `--ignore-path` 选项可以接受多个值：`prettier --ignore-path=foo-ignore --ignore-path=bar-ignore`。指定 `--ignore-path` 是覆盖默认值而不是追加，所以要添加新的忽略文件同时保留 `./.gitignore` 和 `./.prettierignore`，请指定 `prettier --ignore-path=.gitignore --ignore-path=.prettierignore --ignore-path=foo-ignore`。

#### `--list-different`

另一个有用的标志是 `--list-different`（或 `-l`），它打印与 Prettier 格式化结果不同的文件的文件名。如果存在差异，脚本会报错退出，这在 CI 场景中很有用。

```bash
prettier . --single-quote --list-different
```

你也可以使用 [`--check`](#--check) 标志，它的工作方式与 `--list-different` 相同，但还会向 stdout 打印一条人类友好的摘要消息。

#### `--no-config`

不查找配置文件。将使用默认设置。

#### `--config-precedence`

定义配置文件应如何与 CLI 选项组合求值。

**cli-override（默认）**

CLI 选项优先于配置文件

**file-override**

配置文件优先于 CLI 选项

**prefer-file**

如果找到配置文件，则对其求值并忽略其他 CLI 选项。如果未找到配置文件，CLI 选项将正常求值。

此选项为编辑器集成提供了支持：用户定义他们的默认配置，但希望尊重项目特定的配置。

#### `--no-editorconfig`

解析配置时不考虑 `.editorconfig`。详情请参见 [`prettier.resolveConfig` 文档](https://prettier.io/docs/api)。

#### `--with-node-modules`

Prettier CLI 会忽略位于 `node_modules` 目录中的文件。要退出此行为，使用 `--with-node-modules` 标志。

#### `--write`

就地重写所有处理过的文件。这类似于 `eslint --fix` 的工作流程。你也可以使用 `-w` 别名。

#### `--log-level`

更改 CLI 的日志级别。有效选项为：

- `error`
- `warn`
- `log`（默认）
- `debug`
- `silent`

#### `--stdin-filepath`

Prettier CLI 将视为 stdin 的文件路径。例如：

_abc.css_

```css
.name {
  display: none;
}
```

_shell_

```console
$ cat abc.css | prettier --stdin-filepath abc.css
.name {
  display: none;
}
```

#### `--ignore-unknown`

使用 `--ignore-unknown`（或 `-u`），prettier 将忽略模式匹配到的未知文件。

```bash
prettier "**/*" --write --ignore-unknown
```

#### `--no-error-on-unmatched-pattern`

当模式不匹配任何文件时阻止报错。

#### `--cache`

如果启用此选项，以下值将用作缓存键，只有当其中之一发生变化时才格式化文件。

- Prettier 版本
- 选项
- Node.js 版本
-（如果 `--cache-strategy` 为 `metadata`）文件元数据，如时间戳
-（如果 `--cache-strategy` 为 `content`）文件内容

```bash
prettier . --write --cache
```

不带 `--cache` 运行 Prettier 将删除缓存。

此外，由于缓存文件存储在 `./node_modules/.cache/prettier/.prettier-cache`，你可以使用 `rm ./node_modules/.cache/prettier/.prettier-cache` 手动删除它。

:::warning

插件的版本和实现不用作缓存键。我们建议在更新插件时删除缓存。

:::

#### `--cache-location`

`--cache` 标志使用的缓存文件位置的路径。如果你不显式指定 `--cache-location`，Prettier 将缓存文件保存在 `./node_modules/.cache/prettier/.prettier-cache`。

如果传入的是文件路径，则使用该文件作为缓存文件。

```bash
prettier . --write --cache --cache-location=path/to/cache-file
```

#### `--cache-strategy`

缓存用于检测已更改文件的策略。可以是 `metadata` 或 `content`。

一般来说，`metadata` 更快。但是，`content` 对于文件内容未变但时间戳被更新的情况很有用。例如，在 `git clone` 等 git 操作期间可能发生这种情况，因为 git 不跟踪文件修改时间。

如果未指定策略，将使用 `content`。

```bash
prettier . --write --cache --cache-strategy metadata
```
