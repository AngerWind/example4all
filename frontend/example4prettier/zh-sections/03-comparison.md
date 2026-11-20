### Prettier 与 Linters 的对比（Prettier vs. Linters）

> 原文链接：https://prettier.io/docs/comparison

#### 它与 ESLint/TSLint/stylelint 等相比如何？（How does it compare to ESLint/TSLint/stylelint, etc.?）

Linter 有两类规则：

**格式化规则（Formatting rules）**：例如 [max-len](https://eslint.org/docs/rules/max-len)、[no-mixed-spaces-and-tabs](https://eslint.org/docs/rules/no-mixed-spaces-and-tabs)、[keyword-spacing](https://eslint.org/docs/rules/keyword-spacing)、[comma-style](https://eslint.org/docs/rules/comma-style)……

Prettier 让这一整类规则都变得不再必要！Prettier 会以一致的方式从头重新打印整个程序，所以程序员再也不可能在这方面犯错 :)

Prettier 不是基于规则的工具：它没有可以启用或禁用的单条格式化规则。它改为从头重新打印你的整个程序。

**代码质量规则（Code-quality rules）**：例如 [no-unused-vars](https://eslint.org/docs/rules/no-unused-vars)、[no-extra-bind](https://eslint.org/docs/rules/no-extra-bind)、[no-implicit-globals](https://eslint.org/docs/rules/no-implicit-globals)、[prefer-promise-reject-errors](https://eslint.org/docs/rules/prefer-promise-reject-errors)……

Prettier 对这类规则毫无帮助。它们也是 linter 提供的最重要的规则，因为它们很可能捕获代码中真正的 bug！

换句话说：**用 Prettier 做格式化，用 linter 抓 bug！**
