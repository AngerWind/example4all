# 配置 ESLint（Configure ESLint）

> 原文链接：https://eslint.org/docs/latest/use/configure/

ESLint 被设计为可以灵活配置以适应你的使用场景。你可以关闭所有规则，只运行基本的语法检查；也可以混合搭配内置规则和你的自定义规则，以满足项目的需求。配置 ESLint 有两种主要方式：

1. **配置注释（Configuration Comments）** - 使用 JavaScript 注释将配置信息直接嵌入到文件中。
2. **配置文件（Configuration Files）** - 使用 JavaScript 文件为整个目录及其所有子目录指定配置信息。这可以是一个 [`eslint.config.js`](https://eslint.org/docs/latest/use/configure/configuration-files) 文件（ESLint 会自动查找并读取它），或者你也可以在[命令行](https://eslint.org/docs/latest/use/command-line-interface)上指定配置文件。

以下是你可以在 ESLint 中配置的一些选项：

- [**全局变量（Globals）**](https://eslint.org/docs/latest/use/configure/language-options#specifying-globals) - 你的脚本在执行期间访问的额外全局变量。
- [**规则（Rules）**](https://eslint.org/docs/latest/use/configure/rules) - 启用哪些规则以及使用什么错误级别。
- [**插件（Plugins）**](https://eslint.org/docs/latest/use/configure/plugins) - 哪些第三方插件为 ESLint 定义了额外的规则、语言、配置等。

所有这些选项让你能够细粒度地控制 ESLint 如何处理你的代码。

## 目录（Table of Contents）

[**配置文件（Configuration Files）**](https://eslint.org/docs/latest/use/configure/configuration-files)

- [配置文件格式](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file)
- [配置对象](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-objects)
- [配置共享设置](https://eslint.org/docs/latest/use/configure/configuration-files#configuring-shared-settings)
- [配置文件解析](https://eslint.org/docs/latest/use/configure/configuration-files#configuration-file-resolution)

[**配置语言选项（Configure Language Options）**](https://eslint.org/docs/latest/use/configure/language-options)

- [指定 JavaScript 选项](https://eslint.org/docs/latest/use/configure/language-options#specifying-javascript-options)
- [指定全局变量](https://eslint.org/docs/latest/use/configure/language-options#specifying-globals)

[**配置规则（Configure Rules）**](https://eslint.org/docs/latest/use/configure/rules)

- [配置规则](https://eslint.org/docs/latest/use/configure/rules)
- [禁用规则](https://eslint.org/docs/latest/use/configure/rules#disabling-rules)

[**配置插件（Configure Plugins）**](https://eslint.org/docs/latest/use/configure/plugins)

- [配置插件](https://eslint.org/docs/latest/use/configure/plugins#configuring-plugins)
- [指定处理器](https://eslint.org/docs/latest/use/configure/plugins#specifying-a-processor)

[**配置解析器（Configure a Parser）**](https://eslint.org/docs/latest/use/configure/parser)

- [配置自定义解析器](https://eslint.org/docs/latest/use/configure/parser#configuring-a-custom-parser)

[**忽略文件（Ignore Files）**](https://eslint.org/docs/latest/use/configure/ignore)

- [忽略文件](https://eslint.org/docs/latest/use/configure/ignore#ignoring-files)
- [忽略目录](https://eslint.org/docs/latest/use/configure/ignore#ignoring-directories)
- [取消忽略文件和目录](https://eslint.org/docs/latest/use/configure/ignore#unignoring-files-and-directories)
- [已忽略文件警告](https://eslint.org/docs/latest/use/configure/ignore#ignored-file-warnings)
