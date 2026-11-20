## 杂项（Misc）

### 技术细节（Technical Details）

> 原文链接：https://prettier.io/docs/technical-details

这个 printer 是 [recast](https://github.com/benjamn/recast) 的 printer 的一个 fork，其算法被替换为 Wadler 在 "[A prettier printer](https://homepages.inf.ed.ac.uk/wadler/papers/prettier/prettier.pdf)" 中描述的算法。可能仍有一些来自 recast 的遗留代码需要清理。

基本思想是：printer 接收一个 AST 并返回输出的中间表示（IR），然后 printer 使用它来生成字符串。这样做的好处是 printer 可以"测量" IR，判断输出是否能放在一行上，如果不能就换行。

这意味着打印 AST 的大部分逻辑涉及生成由某些命令组成的输出的抽象表示。例如，`["(", line, arg, line, ")"]` 将表示左括号、参数和右括号的连接。但如果它放不下一行，printer 可以在指定 `line` 的地方换行。

[Playground](https://prettier.io/playground) 有一个特殊的模式用于探索 Prettier 的中间表示是如何打印的。要进入该模式，打开侧边栏（"Show options" 按钮）并将 `parser` 选项设置为特殊值 `doc-explorer`。

更多（粗略的）细节可以在 [commands.md](https://github.com/prettier/prettier/blob/main/commands.md) 中找到。
