## 关于（About）

### Prettier 是什么？（What is Prettier?）

> 原文链接：https://prettier.io/docs/

Prettier 是一个有主见的（opinionated）代码格式化工具，支持：

- JavaScript（包括实验性特性）
- [JSX](https://facebook.github.io/jsx/)
- [Angular](https://angular.dev/)
- [Vue](https://vuejs.org/)
- [Flow](https://flow.org/)
- [TypeScript](https://www.typescriptlang.org/)
- CSS、[Less](https://lesscss.org/) 和 [SCSS](https://sass-lang.com)
- [HTML](https://en.wikipedia.org/wiki/HTML)
- [Ember/Handlebars](https://emberjs.com/)
- [JSON](https://json.org/)
- [GraphQL](https://graphql.org/)
- [Markdown](https://commonmark.org/)，包括 [GFM](https://github.github.com/gfm/) 和 [MDX v1](https://mdxjs.com/)
- [YAML](https://yaml.org/)
- [Lightning Web Components (LWC)](https://developer.salesforce.com/developer-centers/lightning-web-components)
- [MJML](https://mjml.io/)

它会移除所有原始样式[\*](#脚注footnotes)，并确保输出的所有代码都符合一致的风格。（参见这篇[博客文章](https://jlongster.com/A-Prettier-Formatter)）

Prettier 接收你的代码，并在考虑行长的前提下从头重新打印。

例如，以下面的代码为例：

<!-- prettier-ignore -->
```js
foo(arg1, arg2, arg3, arg4);
```

它能放在一行里，所以会保持原样。然而，我们都遇到过这种情况：

<!-- prettier-ignore -->
```js
foo(reallyLongArg(), omgSoManyParameters(), IShouldRefactorThis(), isThereSeriouslyAnotherOne());
```

突然间，之前调用函数的格式行不通了，因为太长了。Prettier 会为你完成这件繁琐的工作，把它重新打印成这样：

```js
foo(
  reallyLongArg(),
  omgSoManyParameters(),
  IShouldRefactorThis(),
  isThereSeriouslyAnotherOne(),
);
```

Prettier 在你的整个代码库中强制执行一致的代码**风格**（即不影响 AST 的代码格式化），因为它通过解析丢弃原始样式[\*](#脚注footnotes)，并用自己考虑最大行长的规则重新打印解析后的 AST，在必要时换行。

如果你想了解更多，这两个会议演讲是很好的入门介绍：

[![A Prettier Printer by James Long on React Conf 2017](https://prettier.io/images/youtube-cover/a-prettier-printer-by-james-long-on-react-conf-2017.png)](https://www.youtube.com/watch?v=hkfBvpEfWdA)

[![JavaScript Code Formatting by Christopher Chedeau on React London 2017](https://prettier.io/images/youtube-cover/javascript-code-formatting-by-christopher-chedeau-on-react-london-2017.png)](https://www.youtube.com/watch?v=0Q4kUNx85_4)

#### 脚注（Footnotes）

\* _实际上，在切实可行的情况下会保留一些原始样式——参见[空行](https://prettier.io/docs/rationale#empty-lines)和[多行对象](https://prettier.io/docs/rationale#multi-line-objects)。_
