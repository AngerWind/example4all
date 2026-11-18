# 核心概念（Core Concepts）

> 原文链接：https://eslint.org/docs/latest/use/core-concepts/

本页提供 ESLint 一些核心概念的高层概览。

## 什么是 ESLint？（What is ESLint?）

ESLint 是一个可配置的 JavaScript linter。它帮助你发现并修复 JavaScript 代码中的问题。问题可以是任何东西，从潜在的运行时 bug，到未遵循最佳实践，再到风格问题。

## 规则（Rules）

规则是 ESLint 的核心构建块。规则验证你的代码是否满足某个预期，以及当不满足预期时该做什么。规则还可以包含特定于该规则的额外配置选项。

例如，[`semi`](https://eslint.org/docs/latest/rules/semi) 规则允许你指定 JavaScript 语句是否应以分号（`;`）结尾。你可以将该规则设置为始终要求分号，或要求语句永远不以分号结尾。

ESLint 包含数百条你可以使用的内置规则。你也可以创建自定义规则，或通过[插件](#插件)使用其他人创建的规则。

更多信息请参阅[规则](https://eslint.org/docs/latest/rules/)。

### 规则修复（Rule Fixes）

规则可以选择性地为它们发现的违规提供修复。修复可以安全地纠正违规，而不改变应用程序逻辑。

修复可以通过 [`--fix` 命令行选项](https://eslint.org/docs/latest/use/command-line-interface#--fix)和编辑器扩展自动应用。

可能提供修复的规则在[规则](https://eslint.org/docs/latest/rules/)页面中标记为 🔧。

### 规则建议（Rule Suggestions）

规则可以选择性地在提供修复之外（或代替修复）提供建议。建议与修复有两个方面的不同：

1. 建议可能改变应用程序逻辑，因此不能自动应用。
1. 建议不能通过 ESLint CLI 应用，只能通过编辑器集成使用。

可能提供建议的规则在[规则](https://eslint.org/docs/latest/rules/)页面中标记为 💡。

## 配置文件（Configuration Files）

ESLint 配置文件是你在项目中放置 ESLint 配置的地方。配置中可以包含内置规则及其执行方式、带自定义规则的插件、可共享配置、希望规则应用于哪些文件等等。

更多信息请参阅[配置文件](https://eslint.org/docs/latest/use/configure/configuration-files)。

## 可共享配置（Shareable Configurations）

可共享配置是通过 npm 共享的 ESLint 配置。

可共享配置通常用于使用 ESLint 的内置规则来强制执行风格指南。例如，可共享配置 [eslint-config-airbnb-base](https://www.npmjs.com/package/eslint-config-airbnb-base) 实现了流行的 Airbnb JavaScript 风格指南。

更多信息请参阅[使用可共享配置包](https://eslint.org/docs/latest/use/configure/configuration-files#using-a-shareable-configuration-package)。

## 插件（Plugins）

ESLint 插件是一个 npm 模块，可以包含一组 ESLint 规则、配置、处理器和语言。插件通常包含自定义规则。插件可用于强制执行风格指南，并支持 JavaScript 扩展（如 [TypeScript](https://www.typescriptlang.org)）、库（如 [React](https://react.dev)）和框架（如 [Angular](https://angular.dev)）。

插件的一个流行用例是强制执行某个框架的最佳实践。例如，[@angular-eslint/eslint-plugin](https://www.npmjs.com/package/@angular-eslint/eslint-plugin) 包含使用 Angular 框架的最佳实践。

更多信息请参阅[配置插件](https://eslint.org/docs/latest/use/configure/plugins)。

## 解析器（Parsers）

ESLint 解析器将代码转换为 ESLint 可以求值的抽象语法树（AST）。默认情况下，ESLint 使用内置的 [Espree](https://github.com/eslint/js/tree/main/packages/espree) 解析器，它与标准的 JavaScript 运行时和版本兼容。

自定义解析器让 ESLint 能够解析非标准的 JavaScript 语法。自定义解析器通常作为可共享配置或插件的一部分被包含，因此你不必直接使用它们。

例如，[@typescript-eslint/parser](https://www.npmjs.com/package/@typescript-eslint/parser) 是 [typescript-eslint](https://github.com/typescript-eslint/typescript-eslint) 项目中包含的自定义解析器，它让 ESLint 能够解析 TypeScript 代码。

## 自定义处理器（Custom Processors）

ESLint 处理器从其他类型的文件中提取 JavaScript 代码，然后让 ESLint 检查这些 JavaScript 代码。或者，你可以使用处理器在用 ESLint 解析 JavaScript 代码之前对其进行处理。

例如，[@eslint/markdown](https://github.com/eslint/markdown) 包含一个自定义处理器，让你可以检查 Markdown 代码块中的 JavaScript 代码。

## 格式化器（Formatters）

ESLint 格式化器控制 CLI 中 lint 结果的展示外观。

更多信息请参阅[格式化器](https://eslint.org/docs/latest/use/formatters/)。

## 集成（Integrations）

让 ESLint 成为如此有用的工具的原因之一是围绕它的集成生态系统。例如，许多代码编辑器都有 ESLint 扩展，可以在你工作时直接在文件中显示代码的 ESLint 结果，这样你就不需要使用 ESLint CLI 来查看 lint 结果。

更多信息请参阅[集成](https://eslint.org/docs/latest/use/integrations)。

## CLI 与 Node.js API（CLI & Node.js API）

ESLint CLI 是一个命令行接口，让你可以从终端执行 lint。CLI 有多种选项可以传递给它的命令。

ESLint Node.js API 让你可以从 Node.js 代码中以编程方式使用 ESLint。该 API 在开发插件、集成和其他与 ESLint 相关的工具时很有用。

除非你以某种方式扩展 ESLint，否则应该使用 CLI。

更多信息请参阅[命令行接口](https://eslint.org/docs/latest/use/command-line-interface)和 [Node.js API](https://eslint.org/docs/latest/integrate/nodejs-api)。
