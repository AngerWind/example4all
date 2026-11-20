### Vim 设置（Vim Setup）

> 原文链接：https://prettier.io/docs/vim

Vim 用户可以安装 Prettier 专用的 [vim-prettier](https://github.com/prettier/vim-prettier)，或支持 Prettier 的通用 lint/format 引擎 [Neoformat](https://github.com/sbdchd/neoformat) 或 [ALE](https://github.com/dense-analysis/ale)。

#### [vim-prettier](https://github.com/prettier/vim-prettier)

安装和使用说明请参见 [vim-prettier](https://github.com/prettier/vim-prettier) 的 README。

#### [Neoformat](https://github.com/sbdchd/neoformat)

安装 Neoformat 的最佳方式是使用你最喜欢的 Vim 插件管理器，例如 [vim-plug](https://github.com/junegunn/vim-plug)：

```vim
Plug 'sbdchd/neoformat'
```

为了让 Neoformat 使用项目本地的 Prettier 版本（即使用 `node_modules/.bin/prettier` 而不是在 `$PATH` 上查找 `prettier`），你必须设置 `neoformat_try_node_exe` 选项：

```vim
let g:neoformat_try_node_exe = 1
```

在支持的文件中运行 `:Neoformat` 或 `:Neoformat prettier` 即可运行 Prettier。

要让 Neoformat 在保存时运行 Prettier：

```vim
autocmd BufWritePre *.js Neoformat
```

你也可以通过为其他事件设置 `autocmd`，让 Vim 更频繁地格式化你的代码。这里有几个有用的：

- `TextChanged`：在 Normal 模式下对文本进行更改后
- `InsertLeave`：离开 Insert 模式时

例如，你可以让格式化在上面两个事件以及 `BufWritePre` 时一起发生，像这样：

```vim
autocmd BufWritePre,TextChanged,InsertLeave *.js Neoformat
```

详情请参见 Vim 中的 `:help autocmd-events`。

建议使用[配置文件](https://prettier.io/docs/configuration)，但你也可以在你的 `.vimrc` 中添加选项：

```vim
autocmd FileType javascript setlocal formatprg=prettier\ --single-quote\ --trailing-comma\ es5
" 可用时使用 formatprg
let g:neoformat_try_formatprg = 1
```

Prettier 选项中的每个空格都应该用 `\` 转义。

#### [ALE](https://github.com/dense-analysis/ale)

ALE 需要 Vim 8 或 Neovim，因为 ALE 使用了 Vim 8 和 Neovim 都提供的异步能力。

安装 ALE 的最佳方式是使用你最喜欢的 Vim 插件管理器，例如 [vim-plug](https://github.com/junegunn/vim-plug)：

```vim
Plug 'dense-analysis/ale'
```

你可以在 [ALE 仓库](https://github.com/dense-analysis/ale#3-installation)中找到更多说明。

ALE 会先尝试使用本地安装的 Prettier，然后才查找全局安装。

为你使用的语言启用 Prettier fixer：

```vim
let g:ale_fixers = {
\   'javascript': ['prettier'],
\   'css': ['prettier'],
\}
```

ALE 同时支持 _linters_ 和 _fixers_。如果你不指定要运行哪些 _linters_，**所有支持语言的所有可用工具都会被运行**，你可能会得到一个格式正确但带有一堆 lint 错误的文件。要禁用此行为，你可以告诉 ALE 只运行你显式配置的 linter（更多信息见 [FAQ](https://github.com/dense-analysis/ale/blob/ed8104b6ab10f63c78e49b60d2468ae2656250e9/README.md#faq-disable-linters)）：

```vim
let g:ale_linters_explicit = 1
```

然后你可以在 JavaScript 或 CSS 文件中运行 `:ALEFix` 来运行 Prettier。

要让 ALE 在保存时运行 Prettier：

```vim
let g:ale_fix_on_save = 1
```

建议使用[配置文件](https://prettier.io/docs/configuration)，但你也可以在你的 `.vimrc` 中添加选项：

```vim
let g:ale_javascript_prettier_options = '--single-quote --trailing-comma all'
```

#### [coc-prettier](https://github.com/neoclide/coc-prettier)

[coc.nvim](https://github.com/neoclide/coc.nvim) 的 Prettier 扩展，需要 neovim 或 vim8.1。
用你最喜欢的插件管理器安装 coc.nvim，例如 [vim-plug](https://github.com/junegunn/vim-plug)：

```vim
Plug 'neoclide/coc.nvim', {'branch': 'release'}
```

并通过命令安装 coc-prettier：

```vim
CocInstall coc-prettier
```

在你的 `init.vim` 或 `.vimrc` 中设置 `Prettier` 命令：

```vim
command! -nargs=0 Prettier :call CocAction('runCommand', 'prettier.formatFile')
```

为你想保存时格式化的语言更新 `coc-settings.json`：

```json
{
  "coc.preferences.formatOnSaveFiletypes": ["css", "markdown"]
}
```

[coc-prettier](https://github.com/neoclide/coc-prettier) 与 [prettier-vscode](https://github.com/prettier/prettier-vscode) 有相同的配置，通过 `:CocConfig` 打开 `coc-settings.json` 可获得自动补全支持。

#### 手动运行（Running manually）

如果你想要真正精简的方式，可以创建一个自定义键绑定。在这个例子中，`gp`（助记："get pretty"）用于在当前活动缓冲区中运行 prettier（带选项）：

```vim
nnoremap gp :silent %!prettier --stdin-filepath %<CR>
```

请注意，如果你的代码中有语法错误，整个缓冲区将被替换为错误消息。你需要按 `u` 来恢复你的代码。

这种方法的另一个缺点是光标位置不会被保留。
