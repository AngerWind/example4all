## 使用（Usage）

### 安装（Install）

> 原文链接：https://prettier.io/docs/install

首先，在本地安装 Prettier：

```bash
# npm
npm install --save-dev --save-exact prettier

# yarn
yarn add --dev --exact prettier

# pnpm
pnpm add --save-dev --save-exact prettier

# bun
bun add --dev --exact prettier

# deno
deno install --dev --save-exact prettier
```

然后，创建一个空的配置文件，让编辑器和其他工具知道你在使用 Prettier：

```bash
node --eval "fs.writeFileSync('.prettierrc','{}\n')"
```

接下来，创建一个 [.prettierignore](https://prettier.io/docs/ignore) 文件，让 Prettier CLI 和编辑器知道哪些文件*不*格式化。下面是一个示例：

```bash
node --eval "fs.writeFileSync('.prettierignore','# Ignore artifacts:\nbuild\ncoverage\n')"
```

:::tip

如果 Prettier 运行的目录中存在 .gitignore，它会遵循其中指定的规则。你也可以基于 .eslintignore（如果你有的话）来编写 .prettierignore。

:::

:::tip[另一个提示]

如果你的项目还没准备好格式化某些文件（比如 HTML 文件），可以添加 `*.html`。

:::

现在，用 Prettier 格式化所有文件：

```bash
# npm
npx prettier . --write

# yarn
yarn exec prettier . --write

# pnpm
pnpm exec prettier . --write

# bun
bunx prettier . --write

# deno
deno x prettier . --write
```

:::info

`npx` 是什么？`npx` 随 `npm` 一起提供，让你可以运行本地安装的工具。为简洁起见，本文其余部分会省略 `npx` 部分！（yarn/pnpm/bun/deno 对应 `yarn exec`、`pnpm exec`、`bunx`、`deno x`。）

:::

:::warning

如果你忘记先安装 Prettier，`npx` 会临时下载最新版本。这对使用 Prettier 来说不是个好主意，因为我们每个版本都会改变代码的格式化方式！在你的 `package.json` 中锁定 Prettier 的版本很重要。而且也更快。

:::

`prettier --write .` 非常适合格式化所有内容，但对于大项目可能需要一点时间。你可以运行 `prettier --write app/` 格式化某个目录，或 `prettier --write app/components/Button.js` 格式化某个文件。或者使用 _glob_，如 `prettier --write "app/**/*.test.js"` 格式化目录中的所有测试（支持的 glob 语法见 [fast-glob](https://github.com/mrmlnc/fast-glob#pattern-syntax)）。

如果你搭建了 CI，将以下命令作为其中一部分运行，确保每个人都运行了 Prettier。这可以避免合并冲突和其他协作问题！

```bash
npx prettier . --check
```

`--check` 类似于 `--write`，但只检查文件是否已经格式化，而不是覆盖它们。`prettier --write` 和 `prettier --check` 是运行 Prettier 最常见的方式。

#### 设置你的编辑器（Set up your editor）

从命令行格式化是入门的好方式，但要从 Prettier 获得最大收益，应该在编辑器中运行它——通过快捷键或在保存文件时自动运行。当编码时某行长到屏幕放不下时，只需按一下键，看着它神奇地被换成多行！或者当你粘贴一些代码导致缩进全乱时，让 Prettier 在你不离开编辑器的情况下为你修复。

如何设置编辑器请参见[编辑器集成（Editor Integration）](https://prettier.io/docs/editors)。如果你的编辑器不支持 Prettier，可以改为[用文件监视器运行 Prettier](https://prettier.io/docs/watching-files)。

:::note

不要跳过常规的本地安装！编辑器插件会读取你本地版本的 Prettier，确保你在每个项目中使用正确的版本。（你不会希望编辑器因为使用了比项目更新的 Prettier 版本而意外造成大量变更！）

而能够从命令行运行 Prettier 仍然是一个很好的后备方案，也是 CI 搭建所必需的。

:::

#### ESLint（及其他 linter）

如果你使用 ESLint，安装 [eslint-config-prettier](https://github.com/prettier/eslint-config-prettier#installation)，让 ESLint 和 Prettier 友好相处。它会关闭所有不必要或可能与 Prettier 冲突的 ESLint 规则。

（要了解更多关于格式化与 linting 的区别，请参见 [Prettier 与 Linters 的对比](https://prettier.io/docs/comparison)；要更深入地了解如何配置 linter，请参见[与 Linters 集成](https://prettier.io/docs/integrating-with-linters)；如需更多集成可能性，请参见[相关项目](https://prettier.io/docs/related-projects)。）

#### Git hooks

除了从命令行运行 Prettier（`prettier --write`）、在 CI 中检查格式化、在编辑器中运行 Prettier 之外，许多人还喜欢把 Prettier 作为 pre-commit hook 运行。这可以确保你所有的提交都是格式化过的，无需等待 CI 构建完成。

例如，你可以执行以下操作，让 Prettier 在每次提交前运行：

1. 安装 [husky](https://github.com/typicode/husky) 和 [lint-staged](https://github.com/okonet/lint-staged)：

```bash
# npm
npm install --save-dev husky lint-staged
npx husky init
node --eval "fs.writeFileSync('.husky/pre-commit','npx lint-staged\n')"

# yarn（Yarn 2 用户请见 https://typicode.github.io/husky/#/?id=yarn-2）
yarn add --dev husky lint-staged
npx husky init
node --eval "fs.writeFileSync('.husky/pre-commit','yarn lint-staged\n')"

# pnpm
pnpm add --save-dev husky lint-staged
pnpm exec husky init
node --eval "fs.writeFileSync('.husky/pre-commit','pnpm exec lint-staged\n')"

# bun
bun add --dev husky lint-staged
bunx husky init
bun --eval "fs.writeFileSync('.husky/pre-commit','bunx lint-staged\n')"

# deno
deno install --dev husky lint-staged
deno x husky init
deno eval "Deno.writeTextFileSync('.husky/pre-commit','deno x lint-staged\n')"
```

2. 将以下内容添加到你的 `package.json`：

```json
{
  "lint-staged": {
    "**/*": "prettier --write --ignore-unknown"
  }
}
```

:::note

如果你使用 ESLint，确保 lint-staged 在 Prettier *之前*运行它，而不是之后。

:::

更多信息请参见 [Pre-commit Hook](https://prettier.io/docs/precommit)。

#### 总结（Summary）

总结一下，我们学会了：

- 在项目中本地安装精确版本的 Prettier。这确保项目中的每个人都获得完全相同的 Prettier 版本。即使是 Prettier 的补丁版本也可能导致略微不同的格式化结果，你不会希望不同的团队成员使用不同的版本，把彼此的修改来回反复格式化。
- 添加 `.prettierrc`，让你的编辑器知道你在使用 Prettier。
- 添加 `.prettierignore`，让你的编辑器知道哪些文件*不要*碰，同时让你能够运行 `prettier --write .` 格式化整个项目（而不会破坏你不想要的文件，或在生成的文件上出错）。
- 在 CI 中运行 `prettier --check .`，确保你的项目保持格式化状态。
- 在编辑器中运行 Prettier 以获得最佳体验。
- 使用 [eslint-config-prettier](https://github.com/prettier/eslint-config-prettier) 让 Prettier 和 ESLint 友好共处。
- 设置 pre-commit hook，确保每次提交都是格式化过的。
