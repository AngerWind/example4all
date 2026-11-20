## 编辑器（Editors）

### 编辑器集成（Editor Integration）

> 原文链接：https://prettier.io/docs/editors

要充分发挥 Prettier 的作用，建议在你的编辑器中运行它。

如果你的编辑器不支持 Prettier，可以改为[用文件监视器运行 Prettier](https://prettier.io/docs/watching-files)。

**注意！**在每个项目中本地[安装](https://prettier.io/docs/install) Prettier 很重要，这样每个项目才能获得正确的 Prettier 版本。

#### Visual Studio Code

可以使用扩展侧边栏安装 `prettier-vscode`——它叫 "Prettier - Code formatter"。[查看其仓库了解配置和快捷键](https://github.com/prettier/prettier-vscode)。

如果你想开关格式化器，安装 [`vscode-status-bar-format-toggle`](https://marketplace.visualstudio.com/items?itemName=tombonnike.vscode-status-bar-format-toggle)。

#### Emacs

查看 [prettier-emacs](https://github.com/prettier/prettier-emacs) 仓库，或 [prettier.el](https://github.com/jscheid/prettier.el)。[Apheleia](https://github.com/raxod502/apheleia) 包支持多种代码格式化器，包括 Prettier。

#### Vim

[vim-prettier](https://github.com/prettier/vim-prettier) 是 Prettier 专用的 Vim 插件。[Neoformat](https://github.com/sbdchd/neoformat)、[ALE](https://github.com/w0rp/ale) 和 [coc-prettier](https://github.com/neoclide/coc-prettier) 是支持 Prettier 的多语言 Vim linter/formatter 插件。

更多细节请参见 [Vim 设置指南](https://prettier.io/docs/vim)。

#### Helix

可以在你的 [Helix 语言配置](https://docs.helix-editor.com/languages.html#language-configuration)中指定格式化器，它将优先于任何语言服务器。

更多细节请参见 [Helix Formatter Configurations](https://github.com/helix-editor/helix/wiki/Formatter-Configurations#prettier) 页面。

#### Sublime Text

Sublime Text 通过 Package Control 和 [JsPrettier](https://packagecontrol.io/packages/JsPrettier) 插件提供支持。

#### JetBrains WebStorm、PHPStorm、PyCharm……

请参见 [WebStorm 设置指南](https://prettier.io/docs/webstorm)。

#### Visual Studio

安装 [JavaScript Prettier extension](https://github.com/madskristensen/JavaScriptPrettier)。

#### Espresso

Espresso 用户可以安装 [espresso-prettier](https://github.com/eablokker/espresso-prettier) 插件。
