## 扩展 ESLint 的方式（Ways to Extend ESLint）

> 原文链接：https://eslint.org/docs/latest/extend/ways-to-extend

ESLint 是高度可插拔和可配置的。你可以通过多种方式扩展 ESLint 的功能。

本页介绍扩展 ESLint 的各种方式，以及这些扩展如何协同组合在一起。

### 插件（Plugins）

插件让你可以向项目添加自己的 ESLint 自定义规则和自定义处理器。你可以将插件发布为 npm 模块。

插件很有用，因为你的项目可能需要一些核心 `eslint` 包中没有包含的 ESLint 配置。例如，如果你在使用像 [React](https://react.dev/) 这样的前端 JavaScript 库，或像 [Vue](https://vuejs.org/) 这样的框架，这些工具的一些特性需要 ESLint 核心规则范围之外的自定义规则。

插件通常会搭配一个 ESLint 配置，将插件的一组功能应用到项目中。你也可以在插件中包含配置。

例如，[`eslint-plugin-react`](https://www.npmjs.com/package/eslint-plugin-react) 是一个 ESLint 插件，包含专门针对 React 项目的规则。这些规则包括强制统一使用 React 组件生命周期方法、要求渲染动态列表时使用 key prop 等。

要了解更多关于创建可包含在插件中的扩展的信息，请参阅以下文档：

- [自定义规则（Custom Rules）](https://eslint.org/docs/latest/extend/custom-rules)
- [自定义处理器（Custom Processors）](https://eslint.org/docs/latest/extend/custom-processors)
- [插件中的配置（Configs in Plugins）](https://eslint.org/docs/latest/extend/plugins#configs-in-plugins)

要了解更多关于将这些扩展打包成插件的信息，请参阅[创建插件（Plugins）](https://eslint.org/docs/latest/extend/plugins)。

### 可共享配置（Shareable Configs）

ESLint 可共享配置是预定义的 ESLint 配置，你可以在自己的项目中使用。它们把规则和其他配置打包在一个 npm 包中。凡是可以放在配置文件中的内容，都可以放进可共享配置中。

你可以独立发布可共享配置，也可以将其作为插件的一部分发布。

例如，一个流行的可共享配置是 [`eslint-config-airbnb`](https://www.npmjs.com/package/eslint-config-airbnb)，除了一些[解析器选项](https://eslint.org/docs/latest/use/configure/language-options#specifying-parser-options)之外，它还包含各种规则。这是一套为匹配 [Airbnb JavaScript 风格指南](https://github.com/airbnb/javascript)而设计的 ESLint 规则集。通过使用 `eslint-config-airbnb` 可共享配置，你可以在项目中自动强制执行 Airbnb 风格指南，而无需手动配置每条规则。

要了解更多关于创建可共享配置的信息，请参阅[共享配置（Share Configurations）](https://eslint.org/docs/latest/extend/shareable-configs)。

### 自定义格式化器（Custom Formatters）

自定义格式化器接收 ESLint 的 lint 结果，并以你定义的格式输出结果。自定义格式化器让你以最适合自己需求的格式展示 lint 结果，无论是特定的文件格式、某种展示风格，还是为特定工具优化的格式。只有当[内置格式化器](https://eslint.org/docs/latest/use/formatters/)不能满足你的使用场景时，你才需要创建自定义格式化器。

例如，自定义格式化器 [eslint-formatter-gitlab](https://www.npmjs.com/package/eslint-formatter-gitlab) 可用于在 [GitLab](https://about.gitlab.com/) 代码质量报告中展示 ESLint 结果。

要了解更多关于创建自定义格式化器的信息，请参阅[自定义格式化器（Custom Formatters）](https://eslint.org/docs/latest/extend/custom-formatters)。

### 自定义解析器（Custom Parsers）

ESLint 自定义解析器是一种扩展 ESLint 的方式，使其支持对代码中的新语言特性或自定义语法进行 lint。解析器负责接收你的代码，并将其转换为 ESLint 可以分析和检查的抽象语法树（AST）。

ESLint 自带一个内置的 JavaScript 解析器（Espree），但自定义解析器允许你对其他语言进行 lint，或扩展内置解析器的 lint 能力。

例如，自定义解析器 [@typescript-eslint/parser](https://typescript-eslint.io/packages/parser) 扩展了 ESLint，使其能够检查 TypeScript 代码。

自定义解析器也可以包含在插件中。

要了解更多关于创建自定义解析器的信息，请参阅[自定义解析器（Custom Parsers）](https://eslint.org/docs/latest/extend/custom-parsers)。
