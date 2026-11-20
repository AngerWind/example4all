### 与 Linters 集成（Integrating with Linters）

> 原文链接：https://prettier.io/docs/integrating-with-linters

Linter 通常不仅包含代码质量规则，还包含风格规则。在使用 Prettier 时，大多数风格规则都是不必要的，但更糟的是——它们可能与 Prettier 冲突！如 [Prettier 与 Linters 的对比](https://prettier.io/docs/comparison)中所述：用 Prettier 处理代码格式化问题，用 linter 处理代码质量问题。

幸运的是，使用这些预制的配置可以很容易地关闭与 Prettier 冲突或不必要的规则：

- [eslint-config-prettier](https://github.com/prettier/eslint-config-prettier)

请查看上述链接，了解如何安装和设置的说明。

#### 注意事项（Notes）

在网上同时搜索 Prettier 和你的 linter 时，你可能会找到更多相关项目。这些**一般不推荐**，但在某些情况下可能有用。

首先，有些插件可以让你像运行 linter 规则一样运行 Prettier：

- [eslint-plugin-prettier](https://github.com/prettier/eslint-plugin-prettier)
- [stylelint-prettier](https://github.com/prettier/stylelint-prettier)

这些插件在 Prettier 刚出现时特别有用。通过在 linter 内部运行 Prettier，你不需要搭建任何新的基础设施，还可以复用 linter 的编辑器集成。但如今你可以运行 `prettier --check .`，而且大多数编辑器都支持 Prettier。

这些插件的缺点是：

- 你的编辑器中会出现大量红色波浪线，这很烦人。Prettier 应该让你忘记格式化——而不是让它总在你面前晃！
- 它们比直接运行 Prettier 慢。
- 它们又增加了一层可能出问题的间接层。

最后，有些工具会先运行 `prettier`，然后立即通过运行例如 `eslint --fix` 来 lint 文件：

- [prettier-eslint](https://github.com/prettier/prettier-eslint)
- [prettier-stylelint](https://github.com/hugomrdias/prettier-stylelint)

如果 Prettier 输出的某些方面让你完全无法接受，这些工具就很有用——你可以让例如 `eslint --fix` 为你修复。缺点是这些工具比只运行 Prettier 慢得多。
