### Pre-commit Hook（Pre-commit Hook）

> 原文链接：https://prettier.io/docs/precommit

你可以将 Prettier 与 pre-commit 工具一起使用。这可以在你提交之前，重新格式化那些通过 `git add` 标记为"已暂存（staged）"的文件。

#### 方案 1：[lint-staged](https://github.com/okonet/lint-staged)

**适用场景：**当你想将 Prettier 与其他代码质量工具（如 ESLint、Stylelint 等）一起使用时，或需要支持部分暂存的文件（`git add --patch`）时很有用。

_在继续之前，请确保 Prettier 已安装并在你的 [`devDependencies`](https://docs.npmjs.com/specifying-dependencies-and-devdependencies-in-a-package-json-file) 中。_

```bash
npx mrm@2 lint-staged
```

这将安装 [husky](https://github.com/typicode/husky) 和 [lint-staged](https://github.com/okonet/lint-staged)，然后向项目的 `package.json` 添加一个配置，在 pre-commit hook 中自动格式化支持的文件。

更多信息请阅读 [lint-staged](https://github.com/okonet/lint-staged#configuration) 仓库。

#### 方案 2：[pretty-quick](https://github.com/prettier/pretty-quick)

**适用场景：**非常适合你想对变更/暂存的文件进行整个文件格式化的场景。

与 [simple-git-hooks](https://github.com/toplenboren/simple-git-hooks) 一起安装：

```bash
# npm
npm install --save-dev simple-git-hooks pretty-quick
node --eval "fs.writeFileSync('.simple-git-hooks.json',JSON.stringify({'pre-commit':'npx pretty-quick --staged'},undefined,2)+'\n')"
npx simple-git-hooks

# yarn
yarn add --dev simple-git-hooks pretty-quick
node --eval "fs.writeFileSync('.simple-git-hooks.json',JSON.stringify({'pre-commit':'yarn pretty-quick --staged'},undefined,2)+'\n')"
yarn simple-git-hooks

# pnpm
pnpm add --save-dev simple-git-hooks pretty-quick
node --eval "fs.writeFileSync('.simple-git-hooks.json',JSON.stringify({'pre-commit':'pnpm pretty-quick --staged'},undefined,2)+'\n')"
pnpm simple-git-hooks

# bun
bun add --dev simple-git-hooks pretty-quick
node --eval "fs.writeFileSync('.simple-git-hooks.json',JSON.stringify({'pre-commit':'bun pretty-quick --staged'},undefined,2)+'\n')"
bun simple-git-hooks

# deno
deno install --dev simple-git-hooks pretty-quick
deno eval "Deno.writeTextFileSync('.simple-git-hooks.json',JSON.stringify({'pre-commit':'deno x pretty-quick --staged'},undefined,2)+'\n')"
deno x simple-git-hooks
```

更多信息请阅读 [pretty-quick](https://github.com/prettier/pretty-quick) 仓库。

#### 方案 3：[Husky.Net](https://github.com/alirezanet/Husky.Net)

**适用场景：**一个 dotnet 方案，用于将 Prettier 与其他代码质量工具（如 dotnet-format、ESLint、Stylelint 等）一起使用。它支持多种文件状态（staged - last-commit、git-files 等）。

```bash
dotnet new tool-manifest
dotnet tool install husky
dotnet husky install
dotnet husky add pre-commit
```

安装后，你可以将 prettier 任务添加到 `task-runner.json` 中：

```json title="task-runner.json"
{
  "command": "npx",
  "args": ["prettier", "--ignore-unknown", "--write", "${staged}"],
  "pathMode": "absolute"
}
```

#### 方案 4：[git-format-staged](https://github.com/hallettj/git-format-staged)

**适用场景：**非常适合你想格式化部分暂存文件，而其他方案都不太适合你的项目的场景。

Git-format-staged 用于运行任何可以通过 stdin 接收文件内容的格式化器。它与其他格式化部分暂存文件的工具的工作方式不同：它直接对 git 对象数据库中的对象应用格式化器，然后将更改合并回工作树。这个过程提供了几个保证：

1. 提交中的更改总是被格式化的。
2. 未暂存的更改在任何情况下都不会在格式化过程中被暂存。
3. 如果已格式化并暂存的更改与未暂存的更改之间存在冲突，你的工作树文件将保持不变——你的工作不会被覆盖，也没有需要清理的 stash。
4. 未暂存的更改不会被格式化。

Git-format-staged 需要 Python v3 或 v2.7。Python 通常在 Linux 和 macOS 上预装，但在 Windows 上没有。将 git-format-staged 与 [husky](https://github.com/typicode/husky) 一起使用：

```bash
# npm
npx husky init
npm install --save-dev git-format-staged
node --eval "fs.writeFileSync('.husky/pre-commit', 'git-format-staged -f \'prettier --ignore-unknown --stdin --stdin-filepath \"{}\"\' .\n')"

# yarn
yarn husky init
yarn add --dev git-format-staged
node --eval "fs.writeFileSync('.husky/pre-commit', 'git-format-staged -f \'prettier --ignore-unknown --stdin --stdin-filepath \"{}\"\' .\n')"

# pnpm
pnpm exec husky init
pnpm add --save-dev git-format-staged
node --eval "fs.writeFileSync('.husky/pre-commit', 'git-format-staged -f \'prettier --ignore-unknown --stdin --stdin-filepath \"{}\"\' .\n')"

# bun
bunx husky init
bun add --dev git-format-staged
bun --eval "fs.writeFileSync('.husky/pre-commit', 'git-format-staged -f \'prettier --ignore-unknown --stdin --stdin-filepath \"{}\"\' .\n')"

# deno
deno x husky init
deno install --dev git-format-staged
deno eval "Deno.writeTextFileSync('.husky/pre-commit', 'git-format-staged -f \'prettier --ignore-unknown --stdin --stdin-filepath \"{}\"\' .\n')"
```

根据你的项目添加或移除文件扩展名。请注意，无论你列出哪些扩展名，格式化都会遵循项目中任何 `.prettierignore` 文件。

要了解 git-format-staged 的工作原理，请阅读[部分暂存文件的自动代码格式化](https://www.olioapps.com/blog/automatic-code-formatting/)。

#### 方案 5：[Lefthook](https://lefthook.dev/)

**适用场景：**一个快速的、多语言的 Git hooks 管理器，内置并行化。如果你想要一个单一工具，能在同一个仓库中同时运行 Prettier 和其他语言的 hooks，它很有用。

在仓库根目录的 `lefthook.yml` 文件中添加一个 Prettier 任务：

```yaml title="lefthook.yml"
pre-commit:
  jobs:
    - name: prettier
      run: npx prettier --ignore-unknown --write '{staged_files}'
      stage_fixed: true
```

`stage_fixed: true` 会重新暂存 Prettier 修改过的文件，使提交的是格式化后的版本。

然后安装 Lefthook 并运行 `lefthook install`，让 Lefthook 将配置的任务接入 `.git/hooks`：

```bash
# npm
npm install --save-dev lefthook
npx lefthook install

# yarn
yarn add --dev lefthook
yarn lefthook install

# pnpm
pnpm add --save-dev lefthook
pnpm lefthook install

# bun
bun add --dev lefthook
bunx lefthook install

# deno
deno install --dev lefthook
deno x lefthook install
```

每次更改 `lefthook.yml` 后重新运行 `lefthook install`。更多信息请阅读 [Lefthook](https://lefthook.dev/) 网站。

#### 方案 6：Shell 脚本（Shell script）

或者，你可以将此脚本保存为 `.git/hooks/pre-commit` 并赋予它执行权限：

```sh title=".git/hooks/pre-commit"
#!/bin/sh
FILES=$(git diff --cached --name-only --diff-filter=ACMR | sed 's| |\\ |g')
[ -z "$FILES" ] && exit 0

# 格式化所有选中的文件
echo "$FILES" | xargs ./node_modules/.bin/prettier --ignore-unknown --write

# 将修改/格式化后的文件重新添加到暂存区
echo "$FILES" | xargs git add

# 如果格式化移除了所有暂存的更改，则中止提交。
if git diff --cached --quiet; then
  echo "Prettier formatting removed all staged changes. Commit aborted."
  exit 1
fi

exit 0
```

如果提交后 git 报告你格式化过的文件仍被修改，你可能需要添加一个[更新 git 索引的 post-commit 脚本](https://github.com/prettier/prettier/issues/2978#issuecomment-334408427)。

将类似以下内容添加到 `.git/hooks/post-commit`：

```sh title=".git/hooks/post-commit"
#!/bin/sh
git update-index -g
```
