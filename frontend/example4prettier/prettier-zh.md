# Prettier 中文文档

> 翻译自 Prettier 官方文档 https://prettier.io/docs/ ，章节层级对应官网侧边栏结构（About / Usage / Configuring Prettier / Editors / Misc），每篇标题下标注了原文链接，可与 en/ 目录下的英文原文对照。

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

```js
foo(arg1, arg2, arg3, arg4);
```

它能放在一行里，所以会保持原样。然而，我们都遇到过这种情况：

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


---

### 为什么选择 Prettier？（Why Prettier?）

> 原文链接：https://prettier.io/docs/why-prettier

#### 建立并强制执行风格指南（Building and enforcing a style guide）

到目前为止，采用 Prettier 最大的理由是终结所有关于风格的持续争论。[人们普遍认同为项目和团队制定统一的风格指南是有价值的](https://www.smashingmagazine.com/2012/10/why-coding-style-matters/)，但实现它的过程非常痛苦且没有成就感。人们对特定的代码写法很容易情绪化，而且没有人喜欢花时间写 nit（吹毛求疵的评审意见）或收到 nit。

那么为什么选择"Prettier 风格指南"而不是其他随便哪个风格指南？因为 Prettier 是唯一一个全自动的"风格指南"。即使 Prettier 不能 100% 按你想要的方式格式化所有代码，考虑到 Prettier 独特的优势，这点"牺牲"也是值得的，不是吗？

- "我们想释放脑力，终结关于风格的讨论。这些讨论虽然偶尔有益，但大部分时间是浪费。"
- "真的曾有一位工程师花了很大力气清理我们所有的代码，因为我们为三元表达式的风格争论了很长时间，而且写法一直不一致。这很蠢，但那是一场奇怪的、持续不断的'大辩论'，浪费了大量来回沟通。现在我们所有人都更容易达成一致：直接跑 Prettier，用它的风格。"
- "已经厌倦了告诉别人该怎么给他们的产品代码排版。"
- "我们的首要理由是停止浪费时间去争论风格细节。"
- "配置好 git hook 后，PR 里因 ESLint 规则导致的构建失败、或我事后不得不挑刺清理的风格问题变少了。"
- "我不希望任何人再对其他任何人吹毛求疵。"
- "这让我想起 Steve Jobs 以前每天穿同样的衣服，因为他要做无数个决定，不想为挑衣服这种琐事烦心。我觉得 Prettier 就是这样。"

#### 帮助新人（Helping Newcomers）

引入 Prettier 的通常是熟悉当前代码库和 JavaScript 的人，但从中受益最多的是代码库的新人。人们可能以为它只对编程经验非常有限的人有用，但我们看到它同样加快了有经验工程师入职后的上手速度——他们之前可能使用不同的编码风格——以及从其他编程语言转过来的开发者。

- "我使用 Prettier 的动机是：看起来我很会写 JavaScript。"
- "我总是把空格放错地方，现在再也不用担心了。"
- "当你是初学者时，会犯很多由语法导致的错误。有了 Prettier，你可以减少这些错误，节省大量时间专注于真正重要的事情。"
- "作为一名老师，我也会告诉我的学生安装 Prettier，帮助他们学习 JS 语法并保持文件可读。"

#### 编写代码（Writing code）

人们用上 Prettier 之后通常会发生的是：他们意识到自己实际上花了大量时间和精力在格式化代码上。有了 Prettier 编辑器集成，你只需按下那个神奇的快捷键，代码就格式化好了。这绝对是一种大开眼界的体验。

- "我想写代码，不想在格式化上耗费精力。"
- "它消除了我们日常工作中 5% 的烦心事——也就是格式化。"
- "都 2017 年了，当你碰巧加一个参数导致超过 80 列限制时，把一个调用拆成多行还是那么痛苦 :("

#### 易于采用（Easy to adopt）

我们非常努力地采用争议最小的编码风格，经过多轮修复各种边缘情况，并打磨了入门体验。当你准备把 Prettier 引入代码库时，不仅在技术上应该毫无痛苦，而且新格式化的代码库也不应引起大的争议，能被你的同事们顺利接受。

- "它开销很低。我们能把 Prettier 用到各种非常不同的仓库上，几乎不费什么力气。"
- "它基本没有 bug。如果在实施过程中出现大的样式问题，我们会犹豫要不要把它用到我们的 JS 代码库上。我很高兴事实并非如此。"
- "每个人都把它作为 pre-commit 脚本的一部分来运行，我们中还有几个人使用编辑器保存时格式化的扩展。"
- "它很快，对我们一个较大的 JS 代码库，Prettier 不到 13 秒就跑完了。"
- "Prettier 对我们最大的好处是能够一次性格式化整个代码库。"

#### 清理现有代码库（Clean up an existing codebase）

由于提出一套编码风格并强制执行是一项艰巨的任务，它经常被搁置，你最终只能在不一致的代码库上工作。在这种情况下运行 Prettier 是一个立竿见影的胜利——代码库现在统一了，更易读了，而且几乎没花什么时间。

- "看看代码 :) 我只需要恢复正常。"
- "我们接手了一个约 2000 个模块的 ES6 代码库，由全球团队的 20 个不同开发者历时 18 个月开发。没怎么调研就取得了这样的成果，感觉太值了。"

#### 赶上热潮（Ride the hype train）

纯粹的技术层面并不是人们在选择是否采用 Prettier 时唯一考虑的因素。谁构建了它、谁在使用它，以及它在社区中传播的速度，都有不可忽视的影响。

- "对我来说最神奇的是：1) 两个月前才发布。2) 似乎已经被每个主流 JS 项目采用。3) 7000 star，每月 10 万次 npm 下载。"
- "它和 React 及 React Native 是同一批人构建的。"
- "我喜欢参与热门的新事物。"
- "因为很快人们就会要求用它。"


---

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


---

### 选项理念（Option Philosophy）

> 原文链接：https://prettier.io/docs/option-philosophy

:::info

由于历史原因，Prettier 有一些选项。**但我们不会再增加了。**

继续阅读以了解更多。

:::

Prettier 不是一个试图以任何你想要的方式打印代码的大杂烩式代码格式化工具。它是*有主见的*。引用[为什么选择 Prettier？](https://prettier.io/docs/why-prettier)页面的话：

> 到目前为止，采用 Prettier 最大的理由是终结所有关于风格的持续争论。

然而，Prettier 的选项越多，就离上述目标越远。**关于风格的争论只会变成关于使用哪些 Prettier 选项的争论。** 格式化之战会以新的势头爆发："哪个选项值更好？为什么？我们选对了吗？"

而这并不是选项唯一的代价。要了解更多关于选项弊端的讨论，请参见[关于抵制增加配置的 issue](https://github.com/prettier/prettier/issues/40)，它获得的 👍 比任何选项请求 issue 都多。

那么为什么还会有选项呢？

- 有几个是在 Prettier 的婴儿期添加的，为的是让它能起步。🚀
- 有几个是在"强烈要求"之后添加的。🤔
- 有些是出于兼容性原因添加的。👍

比较容易说通的选项包括：

- `--trailing-comma=es5` 让你能在大多数环境中使用尾随逗号而无需转译（函数尾随逗号是 ES2017 才加入的）。
- `--prose-wrap` 对支持各种行为怪异的 Markdown 渲染器很重要。
- `--html-whitespace-sensitivity` 是由于 HTML 糟糕的空白规则而必需的。
- `--end-of-line` 让团队更容易把 CRLF 挡在 git 仓库之外。
- `--quote-props` 对 Google Closure Compiler 的高级用法很重要。

但事后看来，其他一些选项很难说通：`--arrow-parens`、`--jsx-single-quote`、`--bracket-same-line` 和 `--no-bracket-spacing` 不是我们乐意拥有的那类选项。它们在团队中引起了大量[鸡毛蒜皮之争（bike-shedding）](https://en.wikipedia.org/wiki/Law_of_triviality)，对此我们很抱歉。这些选项现在难以移除，作为历史遗留而存在，不应成为添加更多选项的理由（"既然*那些*选项存在，为什么这个不行？"）。

长期以来，我们把选项请求保持开放，以便让讨论充分展开并收集反馈。那些年里我们学到的是：需求真的很难衡量。Prettier 的使用量增长了很多。当年的"强烈要求"放到今天已经不算多了。GitHub 表情反应和 Twitter 投票变得不再具有代表性。那些沉默的用户呢？看起来"只再加一个"选项很容易。但我们该在哪里停下来？多少才算多？即使在添加了"那最后一个选项"之后，issue 列表里也总会有一个新的"头号 issue"。

然而，是时候停下来了。现在 Prettier 已经足够成熟，我们看到它被如此多的组织和项目采用，调研阶段已经结束。我们有足够的信心得出结论：Prettier 已经到了选项集合应该被"冻结"的时刻。**不再接受选项请求。** 我们感谢参与这段艰难旅程的每一个人。

请注意，由于选项请求不在 Prettier 的范围内，它们将不经讨论直接关闭。保留输入格式元素（如换行符）的请求也同样适用，因为那只不过是伪装起来的选项，带有"真正"选项的所有缺点。在某些情况下，由于技术必要性（如兼容性）可能无法避免添加选项，但对于与格式化相关的选项，这就是最终决定。


---

### 设计原理（Rationale）

> 原文链接：https://prettier.io/docs/rationale

Prettier 是一个有主见的代码格式化工具。本文档解释它的一些选择。

#### Prettier 关注什么（What Prettier is concerned about）

##### 正确性（Correctness）

Prettier 的第一要求是输出与格式化前行为完全相同的有效代码。请报告任何 Prettier 未遵循这些正确性规则的代码——那是需要修复的 bug！

##### 字符串（Strings）

双引号还是单引号？Prettier 选择产生转义数量最少的那一种。`"It's gettin' better!"`，而不是 `'It\'s gettin\' better!'`。在两者相等或字符串不含任何引号的情况下，Prettier 默认使用双引号（但可以通过 [singleQuote](https://prettier.io/docs/options#quotes) 选项更改）。

JSX 有自己单独的引号选项：[jsxSingleQuote](https://prettier.io/docs/options#jsx-quotes)。
JSX 源于 HTML，HTML 中属性的主流引号用法是双引号。浏览器开发者工具也遵循这一约定，总是用双引号显示 HTML，即使源代码使用单引号。单独的选项允许 JS 使用单引号而"HTML"（JSX）使用双引号。

Prettier 会保持你字符串的转义方式。例如，`"🙂"` 不会被格式化为 `"\uD83D\uDE42"`，反之亦然。

##### 空行（Empty lines）

事实证明，空行很难自动生成。Prettier 采取的方法是按空行在原始源代码中的样子保留它们。还有两条附加规则：

- Prettier 将多个连续空行合并为一个空行。
- 块（以及整个文件）开头和结尾的空行会被移除。（不过文件总是以单个换行符结尾。）

##### 多行对象（Multi-line objects）

默认情况下，Prettier 的打印算法会在表达式放得下时将它们打印在一行上。然而，对象在 JavaScript 中有许多不同的用途，有时让它们保持多行确实有助于可读性。例如参见[对象列表](https://github.com/prettier/prettier/issues/74#issue-199965534)、[嵌套配置](https://github.com/prettier/prettier/issues/88#issuecomment-275448346)、[样式表](https://github.com/prettier/prettier/issues/74#issuecomment-275262094)和[键控方法](https://github.com/prettier/prettier/pull/495#issuecomment-275745434)。我们没能为所有这些情况找到一条好规则，所以默认情况下，如果原始源代码中 `{` 和第一个键之间有换行符，Prettier 就保持对象为多行。因此，长的单行对象会被自动展开，但短的多行对象永远不会被折叠。

你可以用 [`objectWrap`](https://prettier.io/docs/options#object-wrap) 选项禁用这种条件行为。

**提示：**如果你有一个想合并成单行的多行对象：

<!-- prettier-ignore -->
```js
const user = {
  name: "John Doe",
  age: 30
};
```

……你只需要删除 `{` 后面的换行符：

<!-- prettier-ignore -->
```js
const user = {  name: "John Doe",
  age: 30
};
```

……然后运行 Prettier：

```js
const user = { name: "John Doe", age: 30 };
```

如果你想再变回多行，在 `{` 后面加一个换行符：

<!-- prettier-ignore -->
```js
const user = {
 name: "John Doe", age: 30 };
```

……然后运行 Prettier：

```js
const user = {
  name: "John Doe",
  age: 30,
};
```

:::note[关于格式化可逆性的说明]

对象字面量的这种半手动格式化实际上是一个变通办法，而不是一个特性。之所以这样实现，只是因为当时没有找到好的启发式规则而又急需修复。然而，作为一般策略，Prettier 避免这种*不可逆*的格式化，所以团队仍在寻找启发式规则，以便完全移除这种行为，或者至少减少应用它的场景。

**可逆**是什么意思？一旦对象字面量变成多行，Prettier 就不会把它折叠回去。如果在 Prettier 格式化过的代码中，我们给一个对象字面量添加一个属性，运行 Prettier，然后改变主意，删掉添加的属性，再运行一次 Prettier，最终得到的格式化结果可能与最初的不一致。这个无用的变更甚至可能被包含进某次提交中——而这正是 Prettier 旨在防止的那类情况。

:::

##### 装饰器（Decorators）

与对象一样，装饰器有许多不同的用途。有时把装饰器写在被装饰行*上方*有意义，有时让它们放在*同一*行更好。我们没能为此找到一条好规则，所以 Prettier 会按你写的样子保持装饰器的位置（如果它们放得下的话）。这并不理想，但是对这个难题的一个务实解法。

```ts
@Component({
  selector: "hero-button",
  template: `<button>{{ label }}</button>`,
})
class HeroButtonComponent {
  // 这些装饰器是内联书写的且放得下，所以保持
  // 内联。
  @Output() change = new EventEmitter();
  @Input() label: string;

  // 这些是多行书写的，所以保持多行。
  @readonly
  @nonenumerable
  NODE_TYPE: 2;
}
```

有一个例外：类。我们认为把类的装饰器内联从来都不合理，所以它们总是被移到单独的一行。

<!-- prettier-ignore -->
```ts
// 运行 Prettier 之前：
@observer class OrderLine {
  @observable price: number = 0;
}
```

```ts
// 运行 Prettier 之后：
@observer
class OrderLine {
  @observable price: number = 0;
}
```

注意：Prettier 1.14.x 及更早版本会尝试自动移动你的装饰器，所以如果你在代码上运行过旧版 Prettier，可能需要在这里那里手动合并一些装饰器以避免不一致：

```ts
@observer
class OrderLine {
  @observable price: number = 0;
  @observable
  amount: number = 0;
}
```

##### 模板字面量（Template literals）

模板字面量可以包含插值。决定是否适合在插值内插入换行，很遗憾取决于模板的语义内容——例如，在自然语言句子中间引入换行通常是不可取的。由于 Prettier 没有足够的信息来自行做出这个决定，它使用与对象类似的启发式规则：只有当插值内已经存在换行时，才会将插值表达式拆分到多行。

这意味着像下面这样的字面量不会被拆成多行，即使它超过了打印宽度：

<!-- prettier-ignore -->
```js
`this is a long message which contains an interpolation: ${format(data)} <- like this`;
```

如果你想让 Prettier 拆分一个插值，你需要确保 `${...}` 内的某处有一个换行。否则无论多长，它都会把所有内容保持在一行上。

团队不愿意以这种方式依赖原始格式，但这是我们目前拥有的最好启发式规则。

##### 分号（Semicolons）

这是关于使用 [noSemi](https://prettier.io/docs/options#semicolons) 选项的说明。

考虑这段代码：

<!-- prettier-ignore -->
```js
if (shouldAddLines) {
  [-1, 1].forEach(delta => addLine(delta * 20))
}
```

虽然上面的代码没有分号也能正常工作，但 Prettier 实际上会把它变成：

<!-- prettier-ignore -->
```js
if (shouldAddLines) {
  ;[-1, 1].forEach(delta => addLine(delta * 20))
}
```

这是为了帮助你避免错误。想象一下，如果 Prettier *没有*插入那个分号，然后又添加了这一行：

```diff
 if (shouldAddLines) {
+  console.log('Do we even get here??')
   [-1, 1].forEach(delta => addLine(delta * 20))
 }
```

糟糕！上面实际上意味着：

<!-- prettier-ignore -->
```js
if (shouldAddLines) {
  console.log('Do we even get here??')[-1, 1].forEach(delta => addLine(delta * 20))
}
```

在 `[` 前面加上分号，这类问题就永远不会发生。它让这一行独立于其他行，所以你可以移动和添加行，而不必考虑 ASI（自动分号插入）规则。

这种做法在使用无分号风格的 [standard](https://standardjs.com/rules.html#semicolons) 中也很常见。

请注意，如果你的程序当前存在与分号相关的 bug，Prettier *不会*为你自动修复这个 bug。请记住，Prettier 只重新格式化代码，不改变代码的行为。以这段有 bug 的代码为例，开发者忘记在 `(` 前放置分号：

<!-- prettier-ignore -->
```js
console.log('Running a background task')
(async () => {
  await doBackgroundWork()
})()
```

如果你把它喂给 Prettier，它不会改变这段代码的行为；相反，它会以一种展示这段代码运行时实际行为的方式重新格式化它：

```js
console.log("Running a background task")(async () => {
  await doBackgroundWork();
})();
```

##### 打印宽度（Print width）

[printWidth](https://prettier.io/docs/options#print-width) 选项对 Prettier 来说更像是一个指导方针，而不是硬性规则。它不是允许的最大行长度限制。它是一种告诉 Prettier 你大致希望行有多长的方式。Prettier 会生成更短和更长的行，但总体上会努力满足指定的打印宽度。

有一些边缘情况，比如非常长的字符串字面量、正则表达式、注释和变量名，它们无法跨行拆分（除非使用代码转换，而 [Prettier 不做代码转换](#prettier-不关注什么what-prettier-is-not-concerned-about)）。或者如果你把代码嵌套了 50 层，你的行当然主要都是缩进 :)

除此之外，还有一些 Prettier 故意超过打印宽度的情况。

###### Imports

Prettier 可以把长的 `import` 语句拆成几行：

```js
import {
  CollectionDashboard,
  DashboardPlaceholder,
} from "../components/collections/collection-dashboard/main";
```

下面的例子放不进打印宽度内，但 Prettier 还是把它打印在一行上：

```js
import { CollectionDashboard } from "../components/collections/collection-dashboard/main";
```

这可能让一些人感到意外，但我们这样做是因为把单个元素的 `import` 保持在一行是一个常见的请求。这同样适用于 `require` 调用。

###### 测试函数（Testing functions）

另一个常见请求是把冗长的测试描述保持在一行，即使太长了。在这种情况下，把参数换行也没有多大帮助。

```js
describe("NodeRegistry", () => {
  it("makes no request if there are no nodes to prefetch, even if the cache is stale", async () => {
    // 上面这行超过了打印宽度，但还是保持在了一行上。
  });
});
```

Prettier 对常见的测试框架函数（如 `describe`、`it` 和 `test`）有特殊处理。

##### JSX

涉及 JSX 时，Prettier 的打印方式与其他 JS 略有不同：

```jsx
function greet(user) {
  return user
    ? `Welcome back, ${user.name}!`
    : "Greetings, traveler! Sign up today!";
}

function Greet({ user }) {
  return (
    <div>
      {user ? (
        <p>Welcome back, {user.name}!</p>
      ) : (
        <p>Greetings, traveler! Sign up today!</p>
      )}
    </div>
  );
}
```

有两个原因。

首先，很多人已经用括号包裹他们的 JSX，尤其是在 `return` 语句中。Prettier 遵循这种常见风格。

其次，[另一种格式化方式让 JSX 更容易编辑](https://github.com/prettier/prettier/issues/2208)。留下分号很容易。与普通 JS 不同，JSX 中遗留的分号最终可能变成纯文本显示在你的页面上。

```jsx
<div>
  <p>Greetings, traveler! Sign up today!</p>; {/* <-- 糟糕！ */}
</div>
```

##### 注释（Comments）

当涉及注释的*内容*时，Prettier 实际上做不了什么。注释可以包含从散文到被注释掉的代码和 ASCII 图的所有内容。由于它们可以包含任何东西，Prettier 无法知道如何格式化或换行它们。所以它们保持原样。唯一的例外是 JSDoc 风格的注释（每行都以 `*` 开头的块注释），Prettier 可以修正它们的缩进。

然后是注释放在*哪里*的问题。事实证明这是一个非常难的问题。Prettier 尽力让你的注释大致保持在原来的位置，但这并不容易，因为注释几乎可以放在任何地方。

一般来说，把注释放在**单独的行上**而不是行尾，能获得最好的结果。优先使用 `// eslint-disable-next-line` 而不是 `// eslint-disable-line`。

请注意，像 `eslint-disable-next-line` 和 `$FlowFixMe` 这样的"魔法注释"，有时可能因为 Prettier 将表达式拆成多行而需要手动移动。

想象这段代码：

```js
// eslint-disable-next-line no-eval
const result = safeToEval ? eval(input) : fallback(input);
```

然后你需要添加另一个条件：

<!-- prettier-ignore -->
```js
// eslint-disable-next-line no-eval
const result = safeToEval && settings.allowNativeEval ? eval(input) : fallback(input);
```

Prettier 会把上面的代码变成：

```js
// eslint-disable-next-line no-eval
const result =
  safeToEval && settings.allowNativeEval ? eval(input) : fallback(input);
```

这意味着 `eslint-disable-next-line` 注释不再有效。在这种情况下，你需要移动注释：

```js
const result =
  // eslint-disable-next-line no-eval
  safeToEval && settings.allowNativeEval ? eval(input) : fallback(input);
```

如果可能，优先使用作用于行范围的注释（如 `eslint-disable` 和 `eslint-enable`）或语句级别的注释（如 `/* istanbul ignore next */`），它们更安全。可以使用 [`eslint-plugin-eslint-comments`](https://github.com/mysticatea/eslint-plugin-eslint-comments) 来禁止使用 `eslint-disable-line` 和 `eslint-disable-next-line` 注释。

#### 关于非标准语法的免责声明（Disclaimer about non-standard syntax）

Prettier 通常能够识别和格式化非标准语法，例如 ECMAScript 早期阶段的提案和任何规范都未定义的 Markdown 语法扩展。对这类语法的支持被视为尽力而为和实验性的。任何版本都可能引入不兼容性，不应被视为破坏性变更。

#### 关于机器生成文件的免责声明（Disclaimer about machine-generated files）

一些文件，如 `package.json` 或 `composer.lock`，是机器生成的，并由包管理器定期更新。如果 Prettier 对这些文件使用与其他文件相同的 JSON 格式化规则，它会经常与这些其他工具冲突。为了避免这种不便，Prettier 将对这类文件改用基于 `JSON.stringify` 的格式化器。你可能会注意到这些差异，比如垂直空白的移除，但这是预期行为。

#### Prettier *不*关注什么（What Prettier is _not_ concerned about）

Prettier 只*打印*代码。它不做转换。这是为了限制 Prettier 的范围。让我们专注于打印并把它做到真正好！

下面是一些超出 Prettier 范围的事情的例子：

- 把单引号或双引号字符串变成模板字面量，反之亦然。
- 使用 `+` 把长字符串字面量拆成适合打印宽度的部分。
- 在可选的地方添加/移除 `{}` 和 `return`。
- 把 `?:` 变成 `if`-`else` 语句。
- 排序/移动 imports、对象键、类成员、JSX 键、CSS 属性或任何其他内容。除了排序是*转换*而不仅仅是打印（如上所述）之外，排序还可能因为副作用而不安全（例如对 imports 而言），并且难以验证最重要的[正确性](#正确性correctness)目标。


---

## 使用（Usage）

### 安装（Install）

> 原文链接：https://prettier.io/docs/install

首先，在本地安装 Prettier：

```bash
# npm
npm install --save-dev --save-exact prettier

# yarn
yarn add --dev --exact prettier

# pnpm
pnpm add --save-dev --save-exact prettier

# bun
bun add --dev --exact prettier

# deno
deno install --dev --save-exact prettier
```

然后，创建一个空的配置文件，让编辑器和其他工具知道你在使用 Prettier：

```bash
node --eval "fs.writeFileSync('.prettierrc','{}\n')"
```

接下来，创建一个 [.prettierignore](https://prettier.io/docs/ignore) 文件，让 Prettier CLI 和编辑器知道哪些文件*不*格式化。下面是一个示例：

```bash
node --eval "fs.writeFileSync('.prettierignore','# Ignore artifacts:\nbuild\ncoverage\n')"
```

:::tip

如果 Prettier 运行的目录中存在 .gitignore，它会遵循其中指定的规则。你也可以基于 .eslintignore（如果你有的话）来编写 .prettierignore。

:::

:::tip[另一个提示]

如果你的项目还没准备好格式化某些文件（比如 HTML 文件），可以添加 `*.html`。

:::

现在，用 Prettier 格式化所有文件：

```bash
# npm
npx prettier . --write

# yarn
yarn exec prettier . --write

# pnpm
pnpm exec prettier . --write

# bun
bunx prettier . --write

# deno
deno x prettier . --write
```

:::info

`npx` 是什么？`npx` 随 `npm` 一起提供，让你可以运行本地安装的工具。为简洁起见，本文其余部分会省略 `npx` 部分！（yarn/pnpm/bun/deno 对应 `yarn exec`、`pnpm exec`、`bunx`、`deno x`。）

:::

:::warning

如果你忘记先安装 Prettier，`npx` 会临时下载最新版本。这对使用 Prettier 来说不是个好主意，因为我们每个版本都会改变代码的格式化方式！在你的 `package.json` 中锁定 Prettier 的版本很重要。而且也更快。

:::

`prettier --write .` 非常适合格式化所有内容，但对于大项目可能需要一点时间。你可以运行 `prettier --write app/` 格式化某个目录，或 `prettier --write app/components/Button.js` 格式化某个文件。或者使用 _glob_，如 `prettier --write "app/**/*.test.js"` 格式化目录中的所有测试（支持的 glob 语法见 [fast-glob](https://github.com/mrmlnc/fast-glob#pattern-syntax)）。

如果你搭建了 CI，将以下命令作为其中一部分运行，确保每个人都运行了 Prettier。这可以避免合并冲突和其他协作问题！

```bash
npx prettier . --check
```

`--check` 类似于 `--write`，但只检查文件是否已经格式化，而不是覆盖它们。`prettier --write` 和 `prettier --check` 是运行 Prettier 最常见的方式。

#### 设置你的编辑器（Set up your editor）

从命令行格式化是入门的好方式，但要从 Prettier 获得最大收益，应该在编辑器中运行它——通过快捷键或在保存文件时自动运行。当编码时某行长到屏幕放不下时，只需按一下键，看着它神奇地被换成多行！或者当你粘贴一些代码导致缩进全乱时，让 Prettier 在你不离开编辑器的情况下为你修复。

如何设置编辑器请参见[编辑器集成（Editor Integration）](https://prettier.io/docs/editors)。如果你的编辑器不支持 Prettier，可以改为[用文件监视器运行 Prettier](https://prettier.io/docs/watching-files)。

:::note

不要跳过常规的本地安装！编辑器插件会读取你本地版本的 Prettier，确保你在每个项目中使用正确的版本。（你不会希望编辑器因为使用了比项目更新的 Prettier 版本而意外造成大量变更！）

而能够从命令行运行 Prettier 仍然是一个很好的后备方案，也是 CI 搭建所必需的。

:::

#### ESLint（及其他 linter）

如果你使用 ESLint，安装 [eslint-config-prettier](https://github.com/prettier/eslint-config-prettier#installation)，让 ESLint 和 Prettier 友好相处。它会关闭所有不必要或可能与 Prettier 冲突的 ESLint 规则。

（要了解更多关于格式化与 linting 的区别，请参见 [Prettier 与 Linters 的对比](https://prettier.io/docs/comparison)；要更深入地了解如何配置 linter，请参见[与 Linters 集成](https://prettier.io/docs/integrating-with-linters)；如需更多集成可能性，请参见[相关项目](https://prettier.io/docs/related-projects)。）

#### Git hooks

除了从命令行运行 Prettier（`prettier --write`）、在 CI 中检查格式化、在编辑器中运行 Prettier 之外，许多人还喜欢把 Prettier 作为 pre-commit hook 运行。这可以确保你所有的提交都是格式化过的，无需等待 CI 构建完成。

例如，你可以执行以下操作，让 Prettier 在每次提交前运行：

1. 安装 [husky](https://github.com/typicode/husky) 和 [lint-staged](https://github.com/okonet/lint-staged)：

```bash
# npm
npm install --save-dev husky lint-staged
npx husky init
node --eval "fs.writeFileSync('.husky/pre-commit','npx lint-staged\n')"

# yarn（Yarn 2 用户请见 https://typicode.github.io/husky/#/?id=yarn-2）
yarn add --dev husky lint-staged
npx husky init
node --eval "fs.writeFileSync('.husky/pre-commit','yarn lint-staged\n')"

# pnpm
pnpm add --save-dev husky lint-staged
pnpm exec husky init
node --eval "fs.writeFileSync('.husky/pre-commit','pnpm exec lint-staged\n')"

# bun
bun add --dev husky lint-staged
bunx husky init
bun --eval "fs.writeFileSync('.husky/pre-commit','bunx lint-staged\n')"

# deno
deno install --dev husky lint-staged
deno x husky init
deno eval "Deno.writeTextFileSync('.husky/pre-commit','deno x lint-staged\n')"
```

2. 将以下内容添加到你的 `package.json`：

```json
{
  "lint-staged": {
    "**/*": "prettier --write --ignore-unknown"
  }
}
```

:::note

如果你使用 ESLint，确保 lint-staged 在 Prettier *之前*运行它，而不是之后。

:::

更多信息请参见 [Pre-commit Hook](https://prettier.io/docs/precommit)。

#### 总结（Summary）

总结一下，我们学会了：

- 在项目中本地安装精确版本的 Prettier。这确保项目中的每个人都获得完全相同的 Prettier 版本。即使是 Prettier 的补丁版本也可能导致略微不同的格式化结果，你不会希望不同的团队成员使用不同的版本，把彼此的修改来回反复格式化。
- 添加 `.prettierrc`，让你的编辑器知道你在使用 Prettier。
- 添加 `.prettierignore`，让你的编辑器知道哪些文件*不要*碰，同时让你能够运行 `prettier --write .` 格式化整个项目（而不会破坏你不想要的文件，或在生成的文件上出错）。
- 在 CI 中运行 `prettier --check .`，确保你的项目保持格式化状态。
- 在编辑器中运行 Prettier 以获得最佳体验。
- 使用 [eslint-config-prettier](https://github.com/prettier/eslint-config-prettier) 让 Prettier 和 ESLint 友好共处。
- 设置 pre-commit hook，确保每次提交都是格式化过的。


---

### 忽略代码（Ignoring Code）

> 原文链接：https://prettier.io/docs/ignore

使用 `.prettierignore` 完全忽略（即不重新格式化）某些文件和文件夹。

使用 "prettier-ignore" 注释忽略文件的某些部分。

#### 忽略文件：.prettierignore（Ignoring Files: .prettierignore）

要排除某些文件不被格式化，在项目根目录创建一个 `.prettierignore` 文件。`.prettierignore` 使用 [gitignore 语法](https://git-scm.com/docs/gitignore#_pattern_format)。

示例：

```text
# 忽略构建产物：
build
coverage

# 忽略所有 HTML 文件：
**/*.html
```

建议在你的项目中准备一个 `.prettierignore`！这样你就可以运行 `prettier --write .` 来确保所有内容都被格式化（而不会破坏你不想要的文件，或在生成的文件上出错）。而且——你的编辑器也会知道哪些文件*不要*格式化！

默认情况下，Prettier 忽略版本控制系统目录（".git"、".jj"、".sl"、".svn" 和 ".hg"）和 `node_modules` 中的文件（除非指定了 [`--with-node-modules` CLI 选项](https://prettier.io/docs/cli#--with-node-modules)）。如果 Prettier 运行的目录中存在 ".gitignore" 文件，它还会遵循其中指定的规则。

所以默认情况下是：

```text
**/.git
**/.svn
**/.hg
**/node_modules
```

如果提供了 [`--with-node-modules` CLI 选项](https://prettier.io/docs/cli#--with-node-modules)，则为：

```text
**/.git
**/.svn
**/.hg
```

（另请参见 [`--ignore-path` CLI 选项](https://prettier.io/docs/cli#--ignore-path)。）

#### JavaScript

JavaScript 注释 `// prettier-ignore` 会将抽象语法树中的下一个节点排除在格式化之外。

例如：

<!-- prettier-ignore -->
```js
matrix(
  1, 0, 0,
  0, 1, 0,
  0, 0, 1,
);

// prettier-ignore
matrix(
  1, 0, 0,
  0, 1, 0,
  0, 0, 1,
);
```

将被转换为：

```js
matrix(1, 0, 0, 0, 1, 0, 0, 0, 1);

// prettier-ignore
matrix(
  1, 0, 0,
  0, 1, 0,
  0, 0, 1,
);
```

#### JSX

```jsx
<div>
  {/* prettier-ignore */}
  <span     ugly  format=''   />
</div>
```

#### HTML

```html
<!-- prettier-ignore -->
<div         class="x"       >hello world</div            >

<!-- prettier-ignore-attribute -->
<div
  (mousedown)="       onStart    (    )         "
  (mouseup)="         onEnd      (    )         "
></div>

<!-- prettier-ignore-attribute (mouseup) -->
<div
  (mousedown)="onStart()"
  (mouseup)="         onEnd      (    )         "
></div>
```

#### CSS

```css
/* prettier-ignore */
.my    ugly rule
{

}
```

#### Markdown

```markdown
<!-- prettier-ignore -->
Do   not    format   this
```

##### 范围忽略（Range Ignore）

_在 v1.12.0+ 可用_

这种类型的忽略只允许在顶层使用，旨在禁用对自动生成内容的格式化，例如 [`all-contributors`](https://github.com/kentcdodds/all-contributors)、[`markdown-toc`](https://github.com/jonschlinkert/markdown-toc) 等。

```markdown
<!-- prettier-ignore-start -->
<!-- SOMETHING AUTO-GENERATED BY TOOLS - START -->

| MY | AWESOME | AUTO-GENERATED | TABLE |
|-|-|-|-|
| a | b | c | d |

<!-- SOMETHING AUTO-GENERATED BY TOOLS - END -->
<!-- prettier-ignore-end -->
```

**重要：**你必须在 `<!-- prettier-ignore-start -->` 和 `<!-- prettier-ignore-end -->` 之前留一个空行，Prettier 才能识别这些注释。

#### YAML

要忽略 YAML 文件的一部分，`# prettier-ignore` 应放在被忽略节点上方紧邻的一行：

```yaml
# prettier-ignore
key  : value
hello: world
```

#### GraphQL

```graphql
{
  # prettier-ignore
  addReaction(input:{superLongInputFieldName:"MDU6SXNzdWUyMzEzOTE1NTE=",content:HOORAY}) {
    reaction {content}
  }
}
```

#### Handlebars

<!-- prettier-ignore -->
```hbs
{{! prettier-ignore }}
<div>
  "hello! my parent was ignored"
  {{#my-crazy-component     "shall"     be="preserved"}}
    <This
      is  =  "also preserved as is"
    />
  {{/my-crazy-component}}
</div>
```

#### 命令行文件模式（Command Line File Patterns）

对于一次性命令，当你想排除某些文件而不将它们添加到 `.prettierignore` 时，否定模式会派上用场：

```bash
prettier . "!**/*.{js,jsx,vue}" --write
```

要了解更多高级 glob 语法，请参见 [fast-glob](https://prettier.io/docs/cli#file-patterns)。


---

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


---

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


---

### 插件（Plugins）

> 原文链接：https://prettier.io/docs/plugins

插件是为 Prettier 添加新语言或格式化规则的方式。Prettier 自己对所有语言的实现都是使用插件 API 编写的。核心的 `prettier` 包内置了 JavaScript 和其他 Web 相关的语言。对于其他语言，你需要安装插件。

#### 使用插件（Using Plugins）

你可以通过以下方式加载插件：

- [CLI](https://prettier.io/docs/cli)，通过 `--plugin`：

  ```bash
  prettier --write main.foo --plugin=prettier-plugin-foo
  ```

  :::tip

  你可以多次设置 `--plugin` 选项。

  :::

- [API](https://prettier.io/docs/api)，通过 `plugins` 选项：

  ```js
  import * as prettierPluginFoo from "prettier-plugin-foo";

  await prettier.format("code", {
    parser: "foo",
    plugins: [prettierPluginFoo],
  });
  ```

- [配置文件](https://prettier.io/docs/configuration)：

  ```js title="prettier.config.mjs"
  import * as prettierPluginFoo from "prettier-plugin-foo";

  /**
   * @see https://prettier.io/docs/configuration
   * @type {import("prettier").Config}
   */
  const config = {
    plugins: [prettierPluginFoo],
  };

  export default config;
  ```

传给 `plugins` 的字符串最终会传给 [`import()` 表达式](https://nodejs.org/api/esm.html#import-expressions)，所以你可以提供模块/包名、路径，或 `import()` 接受的任何其他内容。

#### 官方插件（Official Plugins）

- [`@prettier/plugin-php`](https://github.com/prettier/plugin-php)
- [`@prettier/plugin-pug`](https://github.com/prettier/plugin-pug)，作者 [**@Shinigami92**](https://github.com/Shinigami92)
- [`@prettier/plugin-ruby`](https://github.com/prettier/plugin-ruby)
- [`@prettier/plugin-xml`](https://github.com/prettier/plugin-xml)

#### 社区插件（Community Plugins）

- [`@htnabe/prettier-plugin-go-template`](https://github.com/htnabe/prettier-plugin-go-template)，作者 [**@htnabe**](https://github.com/htnabe)
- [`@htnabe/prettier-plugin-hugo-post`](https://github.com/htnabe/prettier-plugin-hugo-post)，作者 [**@htnabe**](https://github.com/htnabe)
- [`@poliklot/prettier-plugin-handlebars`](https://github.com/Poliklot/prettier-plugin-handlebars)，作者 [**@Poliklot**](https://github.com/Poliklot)
- [`prettier-plugin-apex`](https://github.com/dangmai/prettier-plugin-apex)，作者 [**@dangmai**](https://github.com/dangmai)
- [`prettier-plugin-astro`](https://github.com/withastro/prettier-plugin-astro)，作者 [**@withastro contributors**](https://github.com/withastro/prettier-plugin-astro/graphs/contributors)
- [`prettier-plugin-bigcommerce-stencil`](https://github.com/phoenix128/prettier-plugin-bigcommerce-stencil)，作者 [**@phoenix128**](https://github.com/phoenix128)
- [`prettier-plugin-elm`](https://github.com/gicentre/prettier-plugin-elm)，作者 [**@giCentre**](https://github.com/gicentre)
- [`prettier-plugin-erb`](https://github.com/adamzapasnik/prettier-plugin-erb)，作者 [**@adamzapasnik**](https://github.com/adamzapasnik)
- [`prettier-plugin-gherkin`](https://github.com/mapado/prettier-plugin-gherkin)，作者 [**@mapado**](https://github.com/mapado)
- [`prettier-plugin-glsl`](https://github.com/NaridaL/glsl-language-toolkit/tree/main/packages/prettier-plugin-glsl)，作者 [**@NaridaL**](https://github.com/NaridaL)
- [`prettier-plugin-java`](https://github.com/jhipster/prettier-java)，作者 [**@JHipster**](https://github.com/jhipster)
- [`prettier-plugin-jinja-template`](https://github.com/davidodenwald/prettier-plugin-jinja-template)，作者 [**@davidodenwald**](https://github.com/davidodenwald)
- [`prettier-plugin-jte`](https://github.com/sgruendel/prettier-plugin-jte)，作者 [**@sgruendel**](https://github.com/sgruendel)
- [`prettier-plugin-kotlin`](https://github.com/Angry-Potato/prettier-plugin-kotlin)，作者 [**@Angry-Potato**](https://github.com/Angry-Potato)
- [`prettier-plugin-markdown-compact-tables`](https://github.com/cudoment/prettier-plugin-markdown-compact-tables)，作者 [**@cudoment**](https://github.com/cudoment)
- [`prettier-plugin-markdown-html`](https://github.com/porada/prettier-plugin-markdown-html)，作者 [**@porada**](https://github.com/porada)
- [`prettier-plugin-marko`](https://github.com/marko-js/prettier)，作者 [**@marko-js**](https://github.com/marko-js)
- [`prettier-plugin-motoko`](https://github.com/dfinity/prettier-plugin-motoko)，作者 [**@dfinity**](https://github.com/dfinity)
- [`prettier-plugin-mustache`](https://github.com/Poliklot/prettier-plugin-mustache)，作者 [**@Poliklot**](https://github.com/Poliklot)
- [`prettier-plugin-nginx`](https://github.com/jxddk/prettier-plugin-nginx)，作者 [**@jxddk**](https://github.com/jxddk)
- [`prettier-plugin-nunjucks`](https://github.com/Poliklot/prettier-plugin-nunjucks)，作者 [**@Poliklot**](https://github.com/Poliklot)
- [`prettier-plugin-prisma`](https://github.com/umidbekk/prettier-plugin-prisma)，作者 [**@umidbekk**](https://github.com/umidbekk)
- [`prettier-plugin-properties`](https://github.com/eemeli/prettier-plugin-properties)，作者 [**@eemeli**](https://github.com/eemeli)
- [`prettier-plugin-powershell`](https://github.com/Nick2bad4u/Prettier-Plugin-Powershell)，作者 [**@Nick2bad4u**](https://github.com/Nick2bad4u)
- [`prettier-plugin-rust`](https://github.com/jinxdash/prettier-plugin-rust)，作者 [**@jinxdash**](https://github.com/jinxdash)
- [`prettier-plugin-sh`](https://github.com/un-ts/prettier/tree/master/packages/sh)，作者 [**@JounQin**](https://github.com/JounQin)
- [`prettier-plugin-sql`](https://github.com/un-ts/prettier/tree/master/packages/sql)，作者 [**@JounQin**](https://github.com/JounQin)
- [`prettier-plugin-sql-cst`](https://github.com/nene/prettier-plugin-sql-cst)，作者 [**@nene**](https://github.com/nene)
- [`prettier-plugin-solidity`](https://github.com/prettier-solidity/prettier-plugin-solidity)，作者 [**@mattiaerre**](https://github.com/mattiaerre)
- [`prettier-plugin-svelte`](https://github.com/sveltejs/prettier-plugin-svelte)，作者 [**@sveltejs**](https://github.com/sveltejs)
- [`prettier-plugin-toml`](https://github.com/un-ts/prettier/tree/master/packages/toml)，作者 [**@JounQin**](https://github.com/JounQin) 和 [**@so1ve**](https://github.com/so1ve)
- [`prettier-plugin-xquery`](https://github.com/drrataplan/prettier-plugin-xquery)，作者 [**@DrRataplan**](https://github.com/drrataplan)
- [`prettier-plugin-yaml`](https://github.com/porada/prettier-plugin-yaml)，作者 [**@porada**](https://github.com/porada)

#### 开发插件（Developing Plugins）

Prettier 插件是普通的 JavaScript 模块，具有以下五个导出，或以带有以下属性的默认导出形式：

- `languages`
- `parsers`
- `printers`
- `options`
- `defaultOptions`

##### `languages`

languages 是你的插件将为 Prettier 贡献的语言定义数组。它可以包含 [`prettier.getSupportInfo()`](https://prettier.io/docs/api#prettiergetsupportinfo) 中指定的所有字段。

它**必须**包含 `name` 和 `parsers`。

```js
export const languages = [
  {
    // 语言名称
    name: "InterpretedDanceScript",
    // 能解析此语言的解析器。
    // 可以是内置解析器，也可以是你通过此插件贡献的解析器。
    parsers: ["dance-parse"],
  },
];
```

##### `parsers`

parsers 将字符串形式的代码转换为 [AST](https://en.wikipedia.org/wiki/Abstract_syntax_tree)。

键必须与 `languages` 中 `parsers` 数组里的名称匹配。值包含一个 parse 函数、一个 AST 格式名称，以及两个位置提取函数（`locStart` 和 `locEnd`）。

```js
export const parsers = {
  "dance-parse": {
    parse,
    // 解析器产生的 AST 的名称。
    astFormat: "dance-ast",
    hasPragma,
    hasIgnorePragma,
    locStart,
    locEnd,
    preprocess,
  },
};
```

`parse` 函数的签名是：

```ts
function parse(text: string, options: object): Promise<AST> | AST;
```

位置提取函数（`locStart` 和 `locEnd`）返回给定 AST 节点的起始和结束位置：

```ts
function locStart(node: object): number;
```

_（可选）_ pragma 检测函数（`hasPragma`）应返回文本是否包含 pragma 注释。

```ts
function hasPragma(text: string): boolean;
```

_（可选）_ "忽略 pragma"检测函数（`hasIgnorePragma`）应返回文本是否包含表示不应格式化该文本的 pragma。

```ts
function hasIgnorePragma(text: string): boolean;
```

_（可选）_ preprocess 函数可以在传入 `parse` 函数之前处理输入文本。

```ts
function preprocess(text: string, options: object): string | Promise<string>;
```

_对异步 preprocess 的支持最早在 v3.7.0 中添加_

##### `printers`

printers 将 AST 转换为 Prettier 的中间表示，也称为 Doc。

键必须与解析器产生的 `astFormat` 匹配。值包含一个带有 `print` 函数的对象。所有其他属性（`embed`、`preprocess` 等）都是可选的。

```js
export const printers = {
  "dance-ast": {
    print,
    embed,
    preprocess,
    getVisitorKeys,
    insertPragma,
    canAttachComment,
    isBlockComment,
    printComment,
    getCommentChildNodes,
    hasPrettierIgnore,
    printPrettierIgnored,
    handleComments: {
      ownLine,
      endOfLine,
      remaining,
    },
  },
};
```

###### 打印过程（The printing process）

Prettier 使用一种称为 Doc 的中间表示，然后 Prettier 将其转换为字符串（基于 `printWidth` 等选项）。*printer* 的工作是接收 `parsers[<parser name>].parse` 生成的 AST 并返回一个 Doc。Doc 使用[构建器命令](https://github.com/prettier/prettier/blob/main/commands.md)构造：

```js
import * as prettier from "prettier";

const { join, line, ifBreak, group } = prettier.doc.builders;
```

打印过程由以下步骤组成：

1. **AST 预处理**（可选）。参见 [`preprocess`](#可选-preprocess)。
2. **注释附加**（可选）。参见[在 printer 中处理注释](#在-printer-中处理注释handling-comments-in-a-printer)。
3. **处理嵌入式语言**（可选）。如果定义了 [`embed`](#可选-embed) 方法，则以深度优先的方式为每个节点调用它。出于性能考虑，递归本身是同步的，但 `embed` 可以返回异步函数，这些函数可以调用其他解析器和 printer 来为 CSS-in-JS 等嵌入式语法组合 doc。这些返回的函数会排队并在下一步之前顺序执行。
4. **递归打印**。从 AST 递归构造 doc。从根节点开始：
   - 如果第 3 步中有与当前节点关联的嵌入式语言 doc，则使用该 doc。
   - 否则，调用 `print(path, options, print): Doc` 方法。它为当前节点组合一个 doc，通常通过 `print` 回调打印子节点。

###### `print`

插件 printer 的大部分工作将在其 `print` 函数中进行，其签名为：

```ts
function print(
  // 要打印的 AST 节点的路径
  path: AstPath,
  options: object,
  // 递归打印子节点
  print: (selector?: string | number | Array<string | number> | AstPath) => Doc,
): Doc;
```

`print` 函数接收以下参数：

- **`path`**：一个对象，可用于访问 AST 中的节点。它是一个栈状数据结构，维护递归的当前状态。它被称为"path"，因为它表示从 AST 根到当前节点的路径。当前节点由 `path.node` 返回。
- **`options`**：一个持久对象，包含全局选项，插件可以修改它来存储上下文数据。
- **`print`**：用于打印子节点的回调。此函数包含核心打印逻辑，由一系列步骤组成，其实现由插件提供。特别地，它调用 printer 的 `print` 函数并将自身传给它。因此，这两个 `print` 函数——一个来自核心，一个来自插件——在递归下降 AST 时相互调用。

下面是一个简化的示例，展示 `print` 的典型实现：

```js
import * as prettier from "prettier";

const { group, indent, join, line, softline } = prettier.doc.builders;

function print(path, options, print) {
  const node = path.node;

  switch (node.type) {
    case "list":
      return group([
        "(",
        indent([softline, join(line, path.map(print, "elements"))]),
        softline,
        ")",
      ]);

    case "pair":
      return group([
        "(",
        indent([softline, print("left"), line, ". ", print("right")]),
        softline,
        ")",
      ]);

    case "symbol":
      return node.name;
  }

  throw new Error(`Unknown node type: ${node.type}`);
}
```

查看 [prettier-python 的 printer](https://github.com/prettier/prettier-python/blob/034ba8a9551f3fa22cead41b323be0b28d06d13b/src/printer.js#L174)，了解可以实现的功能示例。

###### （可选）`embed`

printer 可以有 `embed` 方法，用于在一种语言内打印另一种语言。典型的例子是打印 CSS-in-JS 或 Markdown 中的代码块。签名为：

```ts
function embed(
  // 当前 AST 节点的路径
  path: AstPath,
  // 当前选项
  options: Options,
):
  | ((
      // 使用不同的解析器解析并打印传入的文本。
      // 你应设置 `options.parser` 来指定使用哪个解析器。
      textToDoc: (text: string, options: Options) => Promise<Doc>,
      // 用当前 printer 打印当前节点或其后代节点
      print: (
        selector?: string | number | Array<string | number> | AstPath,
      ) => Doc,
      // 以下两个参数是为了方便而传入的。
      // 它们与传给 `embed` 的 `path` 和 `options` 相同。
      path: AstPath,
      options: Options,
    ) => Promise<Doc | undefined> | Doc | undefined)
  | Doc
  | undefined;
```

`embed` 方法与 `print` 方法类似，都将 AST 节点映射为 doc，但与 `print` 不同的是，它有能力通过返回异步函数来做异步工作。该函数的第一个参数——`textToDoc` 异步函数——可用于使用不同的插件渲染 doc。

如果从 `embed` 返回的函数返回一个 doc 或一个解析为 doc 的 Promise，该 doc 将被用于打印，并且不会为此节点调用 `print` 方法。在极少数情况下，直接从 `embed` 同步返回 doc 也是可行的且可能方便，但此时 `textToDoc` 和 `print` 回调不可用。返回一个函数才能获得它们。

如果 `embed` 返回 `undefined`，或者它返回的函数返回 `undefined` 或解析为 `undefined` 的 Promise，该节点将使用 `print` 方法正常打印。如果返回的函数抛出错误或返回被拒绝的 Promise（例如发生解析错误），也会如此。如果你希望 Prettier 重新抛出这些错误，请将 `PRETTIER_DEBUG` 环境变量设置为非空值。

例如，一个包含嵌入式 JavaScript 节点的插件可能有以下 `embed` 方法：

```js
function embed(path, options) {
  const node = path.node;
  if (node.type === "javascript") {
    return async (textToDoc) => {
      return [
        "<script>",
        hardline,
        await textToDoc(node.javaScriptCode, { parser: "babel" }),
        hardline,
        "</script>",
      ];
    };
  }
}
```

如果 [`--embedded-language-formatting`](https://prettier.io/docs/options#embedded-language-formatting) 选项设置为 `off`，嵌入步骤将被完全跳过，`embed` 不会被调用，所有节点都使用 `print` 方法打印。

###### （可选）`preprocess`

`preprocess` 方法可以在将解析器产生的 AST 传入 `print` 方法之前对其进行处理。

```ts
function preprocess(ast: AST, options: Options): AST | Promise<AST>;
```

###### （可选）`getVisitorKeys`

如果插件使用注释附加或嵌入式语言，这个属性可能会派上用场。这些功能会从根开始遍历 AST，迭代每个节点的所有自有可枚举属性。如果 AST 有[环](<https://en.wikipedia.org/wiki/Cycle_(graph_theory)>)，这样的遍历会陷入无限循环。此外，节点可能包含非节点对象（例如位置数据），迭代它们是对资源的浪费。为了解决这些问题，printer 可以定义一个函数来返回应该被遍历的属性名。

其签名为：

```ts
function getVisitorKeys(node, nonTraversableKeys: Set<string>): string[];
```

默认的 `getVisitorKeys`：

```js
function getVisitorKeys(node, nonTraversableKeys) {
  return Object.keys(node).filter((key) => !nonTraversableKeys.has(key));
}
```

第二个参数 `nonTraversableKeys` 是一个包含常见键和 prettier 内部使用的键的集合。

如果你有完整的 visitor keys 列表：

```js
const visitorKeys = {
  Program: ["body"],
  Identifier: [],
  // ...
};

function getVisitorKeys(node /* , nonTraversableKeys*/) {
  // 对未知节点返回 `[]`，防止 Prettier 回退到使用 `Object.keys()`
  return visitorKeys[node.type] ?? [];
}
```

如果你只需要排除一小部分键：

```js
const ignoredKeys = new Set(["prev", "next", "range"]);

function getVisitorKeys(node, nonTraversableKeys) {
  return Object.keys(node).filter(
    (key) => !nonTraversableKeys.has(key) && !ignoredKeys.has(key),
  );
}
```

###### （可选）`insertPragma`

插件可以在 `insertPragma` 函数中实现当使用 `--insert-pragma` 选项时如何将 pragma 注释插入生成的代码。其签名为：

```ts
function insertPragma(text: string): string;
```

###### 在 printer 中处理注释（Handling comments in a printer）

注释通常不是语言 AST 的一部分，这对 pretty printer 来说是一个挑战。Prettier 插件可以在其 `print` 函数中自己打印注释，或者依赖 Prettier 的注释算法。

要使用 Prettier 的注释算法，请在 AST 根的 `comments` 属性中返回注释节点数组，并在你的 printer 上同时实现 `canAttachComment` 和 `printComment`。解析器的 `locStart` 和 `locEnd` 函数必须既适用于注释节点也适用于其他 AST 节点。给每个注释一个字符串 `value`，这样如果注释未被打印，Prettier 可以在错误消息中识别它。

Prettier 从 AST 根移除 `comments`，然后根据每个注释的位置将其附加到某个节点。这会改变 AST。Prettier 调用 `printComment` 来打印节点输出前后的前导和尾随注释。你的 printer 负责放置悬空注释（dangling comments）。

###### （可选）`getCommentChildNodes`

默认情况下，Prettier 递归搜索每个节点的所有对象属性（少数预定义属性除外）。可以提供此函数来覆盖该行为。其签名为：

```ts
function getCommentChildNodes(
  // 应返回其子节点的节点。
  node: AST,
  // 当前选项
  options: object,
): AST[] | undefined;
```

如果节点没有子节点则返回 `[]`，返回 `undefined` 则回退到默认行为。

##### （可选）`hasPrettierIgnore`

```ts
function hasPrettierIgnore(path: AstPath): boolean;
```

返回 AST 节点是否被标记为 `prettier-ignore`。

##### （可选）`printPrettierIgnored`

如果 AST 节点被标记为 `prettier-ignore`，默认情况下 Prettier 会直接截取文本而不调用 `print` 函数；但插件也可以通过添加此属性来处理 `prettier-ignore` 节点的打印。

此属性与 `print` 属性具有相同的签名。

_最早在 v3.7.0 可用_

###### （可选）`printComment`

使用 Prettier 的注释打印时，返回注释节点的 Doc。其签名为：

```ts
function printComment(
  // 当前注释节点的路径
  commentPath: AstPath,
  // 当前选项
  options: object,
): Doc;
```

###### （可选）`canAttachComment`

```ts
function canAttachComment(node: AST, ancestors: T[]): boolean;
```

此函数用于决定注释是否可以附加到特定的 AST 节点。默认情况下，会遍历*所有* AST 属性以搜索可以附加注释的节点。此函数用于阻止注释被附加到特定节点。一个典型的实现如下：

```js
function canAttachComment(node, [parent]) {
  return !(
    node.type === "comment" ||
    (parent?.type === "shorthand-property" &&
      parent.key === node &&
      parent.key !== parent.value)
  );
}
```

_第二个参数 `ancestors` 最早在 v3.7.0 中添加。_

###### （可选）`isBlockComment`

```ts
function isBlockComment(node: AST): boolean;
```

返回 AST 节点是否为块注释。

###### （可选）`handleComments`

`handleComments` 对象包含三个可选函数，每个函数的签名为：

```ts
(
  // 注释对应的 AST 节点
  comment: AST,
  // 完整源代码文本
  text: string,
  // 全局选项对象
  options: object,
  // AST
  ast: AST,
  // 此注释是否为最后一个注释
  isLastComment: boolean,
) => boolean;
```

这些函数覆盖 Prettier 的默认注释附加行为。手动附加注释后返回 `true`，或返回 `false` 让 Prettier 附加它。

Prettier 检查注释周围的换行符，忽略空格和制表符，并考虑相邻的注释。它会调用：

- `ownLine`：如果在注释之前找到换行符。
- `endOfLine`：如果在注释之后找到换行符但之前没有。
- `remaining`：其他情况，例如同一行上两个 token 之间的块注释。

调用这些函数时，Prettier 通过注释上的 `enclosingNode`、`precedingNode` 和 `followingNode` 提供附近的 AST 节点。其中一些或全部可能不存在，例如当文件只包含注释时。

###### 手动附加注释（Manually attaching a comment）

`handleComments` 回调根据注释在源文本中的位置选择。附加注释时，选择它相对于目标节点的打印方式：

- `prettier.util.addLeadingComment(node, comment)` 附加一个在节点之前打印的注释。
- `prettier.util.addTrailingComment(node, comment)` 附加一个在节点之后打印的注释。
- `prettier.util.addDanglingComment(node, comment)` 附加一个其位置由节点的 printer 决定的注释，例如空块内的注释。

例如，`ownLine` 回调可以将注释附加为 `punctuation` 节点（本示例虚构的节点类型）的尾随注释：

```js
import * as prettier from "prettier";

function ownLine(comment, text, options, ast, isLastComment) {
  const { precedingNode } = comment;
  if (precedingNode?.type === "punctuation") {
    return prettier.util.addTrailingComment(precedingNode, comment);
  }
  return false;
}
```

:::warning

`prettier.util.add{Trailing,Leading,Dangling}Comment()` 在 v3.10 之前返回 `void`；如果需要支持旧版本的 Prettier，请在函数调用后显式 `return true`。

```diff
- return prettier.util.addTrailingComment(precedingNode, comment);
+ prettier.util.addTrailingComment(precedingNode, comment);
+ return true;
```

或者

```diff
- return prettier.util.addTrailingComment(precedingNode, comment);
+ return prettier.util.addTrailingComment(precedingNode, comment) ?? true;
```

:::

带有注释的节点预期有一个 `comments` 属性，其中包含注释数组。每个注释预期具有以下属性：`leading`、`trailing`、`printed`。

上面的示例使用了 `prettier.util.addTrailingComment`，它会自动将 `comment.leading`/`trailing`/`printed` 设置为适当的值，并将注释添加到 AST 节点的 `comments` 数组中。

###### 打印悬空注释（Printing dangling comments）

打印节点时，处理其既没有设置 `leading` 也没有设置 `trailing` 的注释。将每个悬空注释的 Doc 添加到它所属的位置，并设置 `comment.printed = true`。例如，空块的 printer 可以像这样复用其 `printComment` 函数：

```js
import { doc } from "prettier";

const { hardline, indent, join } = doc.builders;

function printEmptyBlock(path, options) {
  const dangling = [];
  if (path.node.comments) {
    path.each((commentPath) => {
      const comment = commentPath.node;
      if (!comment.leading && !comment.trailing) {
        comment.printed = true;
        dangling.push(printComment(commentPath, options));
      }
    }, "comments");
  }
  return dangling.length === 0
    ? "{}"
    : ["{", indent([hardline, join(hardline, dangling)]), hardline, "}"];
}
```

`--debug-print-comments` CLI 标志可以帮助调试注释附加问题。它打印注释的详细列表，包括每个注释如何分类（`ownLine`/`endOfLine`/`remaining`、`leading`/`trailing`/`dangling`）以及附加到了哪个节点的信息。对于 Prettier 的内置语言，此信息也可在 Playground 上查看（Debug 部分的 'show comments' 复选框）。

##### `options`

`options` 是一个对象，包含你的插件支持的自定义选项。

示例：

```js
// ...插件实现

export const options = {
  openingBraceNewLine: {
    type: "boolean",
    category: "Global",
    default: true,
    description: "Move open brace for code blocks onto new line.",
  },
};
```

##### `defaultOptions`

如果你的插件需要对 Prettier 的某些核心选项使用不同的默认值，可以在 `defaultOptions` 中指定：

```js
// ...插件实现

export const defaultOptions = {
  tabWidth: 4,
};
```

##### 工具函数（Utility functions）

`prettier.util` 为插件提供以下有限的工具函数集：

```js
import * as prettier from "prettier";

const { getStringWidth } = prettier.util;

console.log(getStringWidth("prettier"));
// -> 8
```

```ts
type Quote = '"' | "'";
type SkipOptions = { backwards?: boolean };

function getMaxContinuousCount(text: string, searchString: string): number;

function getStringWidth(text: string): number;

function getAlignmentSize(
  text: string,
  tabWidth: number,
  startIndex?: number,
): number;

function getIndentSize(value: string, tabWidth: number): number;

function skip(
  characters: string | RegExp,
): (
  text: string,
  startIndex: number | false,
  options?: SkipOptions,
) => number | false;

function skipWhitespace(
  text: string,
  startIndex: number | false,
  options?: SkipOptions,
): number | false;

function skipSpaces(
  text: string,
  startIndex: number | false,
  options?: SkipOptions,
): number | false;

function skipToLineEnd(
  text: string,
  startIndex: number | false,
  options?: SkipOptions,
): number | false;

function skipEverythingButNewLine(
  text: string,
  startIndex: number | false,
  options?: SkipOptions,
): number | false;

function skipInlineComment(
  text: string,
  startIndex: number | false,
): number | false;

function skipTrailingComment(
  text: string,
  startIndex: number | false,
): number | false;

function skipNewline(
  text: string,
  startIndex: number | false,
  options?: SkipOptions,
): number | false;

function hasNewline(
  text: string,
  startIndex: number,
  options?: SkipOptions,
): boolean;

function hasNewlineInRange(
  text: string,
  startIndex: number,
  startIndex: number,
): boolean;

function hasSpaces(
  text: string,
  startIndex: number,
  options?: SkipOptions,
): boolean;

function getPreferredQuote(
  text: string,
  preferredQuoteOrPreferSingleQuote: Quote | boolean,
): Quote;

function makeString(
  rawText: string,
  enclosingQuote: Quote,
  unescapeUnnecessaryEscapes?: boolean,
): string;

function getNextNonSpaceNonCommentCharacter(
  text: string,
  startIndex: number,
): string;

function getNextNonSpaceNonCommentCharacterIndex(
  text: string,
  startIndex: number,
): number | false;

function isNextLineEmpty(text: string, startIndex: number): boolean;

function isPreviousLineEmpty(text: string, startIndex: number): boolean;
```

##### 教程（Tutorials）

- [如何为 Prettier 编写插件](https://medium.com/@fvictorio/how-to-write-a-plugin-for-prettier-a0d98c845e70)：教你如何为 TOML 编写一个非常基础的 Prettier 插件。

#### 测试插件（Testing Plugins）

由于插件可以使用相对路径解析，在开发插件时你可以这样做：

```js
import * as prettier from "prettier";

const code = "(add 1 2)";
await prettier.format(code, {
  parser: "lisp",
  plugins: ["./index.js"],
});
```

这将相对于当前工作目录解析插件。


---

### CLI（CLI）

> 原文链接：https://prettier.io/docs/cli

使用 `prettier` 命令从命令行运行 Prettier。

```bash
prettier [options] [file/dir/glob ...]
```

:::note

要运行你本地安装的 Prettier 版本，在命令前加上 `npx`、`yarn exec`、`pnpm exec`、`bunx` 或 `deno x`，即 `npx prettier --help`、`yarn exec prettier --help`、`pnpm exec prettier --help`、`bunx prettier --help` 或 `deno x prettier --help`。

:::

要就地格式化文件，使用 `--write`。（注意：这会覆盖你的文件！）

在实践中，可能像这样：

```bash
prettier . --write
```

此命令格式化当前目录及其子目录中 Prettier 支持的所有文件。

建议始终确保 `prettier --write .` 只格式化你项目中想要的内容。使用 [`.prettierignore`](https://prettier.io/docs/ignore) 文件来忽略不应格式化的内容。

一个更复杂的例子：

```bash
prettier docs package.json "{app,__{tests,mocks}__}/**/*.js" --write --single-quote --trailing-comma all
```

:::warning

别忘了 glob 周围的**引号**！引号确保由 Prettier CLI 而不是你的 shell 来展开 glob，这对跨平台使用很重要。

:::

:::note

对于 `--single-quote` 和 `--trailing-comma` 这样的格式化选项，最好使用[配置文件](https://prettier.io/docs/configuration)而不是作为 CLI 标志传递。这样 Prettier CLI、[编辑器集成](https://prettier.io/docs/editors)和其他工具都能知道你使用了哪些选项。

:::

#### 文件模式（File patterns）

给定一个路径/模式列表，Prettier CLI 首先将其中每一项视为字面路径。

- 如果路径指向一个已存在的文件，Prettier CLI 直接处理该文件，不会将路径解析为 glob 模式。

- 如果路径指向一个已存在的目录，Prettier CLI 递归查找该目录中支持的文件。此解析过程基于 Prettier 及其[插件](https://prettier.io/docs/plugins)与支持的语言关联的文件扩展名和众所周知的文件名。

- 否则，该条目将使用 [`fast-glob` 模块的 glob 语法](https://github.com/mrmlnc/fast-glob#pattern-syntax)解析为 glob 模式。

Prettier CLI 会忽略位于 `node_modules` 目录中的文件。要退出此行为，使用 `--with-node-modules` 标志。

Prettier CLI 在展开参数时不会跟随符号链接。

要转义 glob 中的特殊字符，可以使用两种转义语法之一：`prettier "\[my-dir]/*.js"` 或 `prettier "[[]my-dir]/*.js"`。两者都匹配名为 `[my-dir]` 的目录中的所有 JS 文件，但后者更可取，因为前者在 Windows 上不起作用（Windows 中反斜杠被视为路径分隔符）。

#### `--check`

当你想检查文件是否已格式化时，可以带 `--check` 标志（或 `-c`）运行 Prettier。
这将输出一条人类友好的消息和未格式化文件的列表（如果有）。

```bash
prettier . --check
```

所有文件都已格式化时的控制台输出：

```console
Checking formatting...
All matched files use Prettier code style!
```

部分文件需要重新格式化时的控制台输出：

```console
Checking formatting...
[warn] src/fileA.js
[warn] src/fileB.js
[warn] Code style issues found in 2 files. Run Prettier with --write to fix.
```

第二种情况下该命令将返回退出码 `1`，这在 CI 流水线中很有用。
人类友好的状态消息帮助项目贡献者对可能的问题做出反应。
为了尽量减少 `prettier --check` 发现未格式化文件的次数，你可能会对在你的仓库中配置 [pre-commit hook](https://prettier.io/docs/precommit) 感兴趣。
应用这种做法将最大限度地减少 CI 因代码格式化问题而失败的次数。

如果你需要将未格式化文件列表通过管道传给另一个命令，可以使用 [`--list-different`](#--list-different) 标志代替 `--check`。

##### 退出码（Exit codes）

| 代码 | 含义                     |
| ---- | ------------------------ |
| `0`  | 一切格式化正常           |
| `1`  | 有内容未正确格式化       |
| `2`  | Prettier 出了问题        |

#### `--debug-check`

如果你担心 Prettier 会改变代码的正确性，在命令中添加 `--debug-check`。这将使 Prettier 在检测到代码正确性可能已改变时打印错误消息。请注意，`--write` 不能与 `--debug-check` 一起使用。

#### `--find-config-path` 和 `--config`

如果你反复使用 `prettier` 格式化单个文件，当 Prettier 尝试查找[配置文件](https://prettier.io/docs/configuration)时会产生一点性能开销。为了跳过这一步，你可以让 Prettier 找一次配置文件，之后复用它。

```console
$ prettier --find-config-path path/to/file.js
path/to/.prettierrc
```

这将提供配置文件的路径，你可以将其传给 `--config`：

```bash
prettier path/to/file.js --write --config path/to/.prettierrc
```

如果你的配置文件位于 Prettier 找不到的地方（例如 `config/` 目录），你也可以使用 `--config`。

如果你没有配置文件，或者想忽略已存在的配置文件，可以改为传递 `--no-config`。

#### `--ignore-path`

包含描述要忽略文件的模式的文件路径。默认情况下，Prettier 查找 `./.gitignore` 和 `./.prettierignore`。

通过提供多个 `--ignore-path` 选项可以接受多个值：`prettier --ignore-path=foo-ignore --ignore-path=bar-ignore`。指定 `--ignore-path` 是覆盖默认值而不是追加，所以要添加新的忽略文件同时保留 `./.gitignore` 和 `./.prettierignore`，请指定 `prettier --ignore-path=.gitignore --ignore-path=.prettierignore --ignore-path=foo-ignore`。

#### `--list-different`

另一个有用的标志是 `--list-different`（或 `-l`），它打印与 Prettier 格式化结果不同的文件的文件名。如果存在差异，脚本会报错退出，这在 CI 场景中很有用。

```bash
prettier . --single-quote --list-different
```

你也可以使用 [`--check`](#--check) 标志，它的工作方式与 `--list-different` 相同，但还会向 stdout 打印一条人类友好的摘要消息。

#### `--no-config`

不查找配置文件。将使用默认设置。

#### `--config-precedence`

定义配置文件应如何与 CLI 选项组合求值。

**cli-override（默认）**

CLI 选项优先于配置文件

**file-override**

配置文件优先于 CLI 选项

**prefer-file**

如果找到配置文件，则对其求值并忽略其他 CLI 选项。如果未找到配置文件，CLI 选项将正常求值。

此选项为编辑器集成提供了支持：用户定义他们的默认配置，但希望尊重项目特定的配置。

#### `--no-editorconfig`

解析配置时不考虑 `.editorconfig`。详情请参见 [`prettier.resolveConfig` 文档](https://prettier.io/docs/api)。

#### `--with-node-modules`

Prettier CLI 会忽略位于 `node_modules` 目录中的文件。要退出此行为，使用 `--with-node-modules` 标志。

#### `--write`

就地重写所有处理过的文件。这类似于 `eslint --fix` 的工作流程。你也可以使用 `-w` 别名。

#### `--log-level`

更改 CLI 的日志级别。有效选项为：

- `error`
- `warn`
- `log`（默认）
- `debug`
- `silent`

#### `--stdin-filepath`

Prettier CLI 将视为 stdin 的文件路径。例如：

_abc.css_

```css
.name {
  display: none;
}
```

_shell_

```console
$ cat abc.css | prettier --stdin-filepath abc.css
.name {
  display: none;
}
```

#### `--ignore-unknown`

使用 `--ignore-unknown`（或 `-u`），prettier 将忽略模式匹配到的未知文件。

```bash
prettier "**/*" --write --ignore-unknown
```

#### `--no-error-on-unmatched-pattern`

当模式不匹配任何文件时阻止报错。

#### `--cache`

如果启用此选项，以下值将用作缓存键，只有当其中之一发生变化时才格式化文件。

- Prettier 版本
- 选项
- Node.js 版本
-（如果 `--cache-strategy` 为 `metadata`）文件元数据，如时间戳
-（如果 `--cache-strategy` 为 `content`）文件内容

```bash
prettier . --write --cache
```

不带 `--cache` 运行 Prettier 将删除缓存。

此外，由于缓存文件存储在 `./node_modules/.cache/prettier/.prettier-cache`，你可以使用 `rm ./node_modules/.cache/prettier/.prettier-cache` 手动删除它。

:::warning

插件的版本和实现不用作缓存键。我们建议在更新插件时删除缓存。

:::

#### `--cache-location`

`--cache` 标志使用的缓存文件位置的路径。如果你不显式指定 `--cache-location`，Prettier 将缓存文件保存在 `./node_modules/.cache/prettier/.prettier-cache`。

如果传入的是文件路径，则使用该文件作为缓存文件。

```bash
prettier . --write --cache --cache-location=path/to/cache-file
```

#### `--cache-strategy`

缓存用于检测已更改文件的策略。可以是 `metadata` 或 `content`。

一般来说，`metadata` 更快。但是，`content` 对于文件内容未变但时间戳被更新的情况很有用。例如，在 `git clone` 等 git 操作期间可能发生这种情况，因为 git 不跟踪文件修改时间。

如果未指定策略，将使用 `content`。

```bash
prettier . --write --cache --cache-strategy metadata
```


---

### API（API）

> 原文链接：https://prettier.io/docs/api

如果你想以编程方式运行 Prettier，请查看本页。

```js
import * as prettier from "prettier";
```

我们的公共 API 都是异步的。如果出于某种原因你必须使用同步版本，可以尝试 [`@prettier/sync`](https://github.com/prettier/prettier-synchronized)。

#### `prettier.format(source, options)`

`format` 用于使用 Prettier 格式化文本。必须根据你要格式化的语言设置 `options.parser`（参见[可用解析器列表](https://prettier.io/docs/options#parser)）。或者，可以指定 `options.filepath`，让 Prettier 从文件扩展名推断解析器。可以提供其他[选项](https://prettier.io/docs/options)来覆盖默认值。

```js
await prettier.format("foo ( );", { semi: false, parser: "babel" });
// -> 'foo()\n'
```

#### `prettier.check(source [, options])`

`check` 检查文件是否已使用给定选项通过 Prettier 格式化，并返回 `Promise<boolean>`。这类似于 CLI 中的 `--check` 或 `--list-different` 参数，适用于在 CI 场景中运行 Prettier。

#### `prettier.formatWithCursor(source [, options])`

`formatWithCursor` 既格式化代码，又将光标位置从未格式化的代码转换到格式化后的代码。这对编辑器集成很有用，可以防止格式化代码时光标移动。

应提供 `cursorOffset` 选项来指定光标位置。

```js
await prettier.formatWithCursor(" 1", { cursorOffset: 2, parser: "babel" });
// -> { formatted: '1;\n', cursorOffset: 1 }
```

#### `prettier.resolveConfig(fileUrlOrPath [, options])`

`resolveConfig` 可用于解析给定源文件的配置，将其路径或 url 作为第一个参数传递。配置搜索将从文件所在目录开始，并继续向上搜索目录。如果你不想搜索，也可以直接通过 `options.config` 传入配置文件的路径。返回一个 Promise，解析为：

- 一个选项对象（如果找到了[配置文件](https://prettier.io/docs/configuration)）。
- `null`（如果未找到文件）。

如果解析配置文件时出错，Promise 将被拒绝。

如果 `options.useCache` 为 `false`，所有缓存将被绕过。

```js
const text = await fs.readFile(filePath, "utf8");
const options = await prettier.resolveConfig(filePath);
const formatted = await prettier.format(text, {
  ...options,
  filepath: filePath,
});
```

如果 `options.editorconfig` 为 `true` 且你的项目中有 [`.editorconfig` 文件](https://editorconfig.org/)，Prettier 将解析它并将其属性转换为相应的 Prettier 配置。此配置将被 `.prettierrc` 等覆盖。目前支持以下 EditorConfig 属性：

- `end_of_line`
- `indent_style`
- `indent_size`/`tab_width`
- `max_line_length`

#### `prettier.resolveConfigFile([fileUrlOrPath])`

`resolveConfigFile` 可用于查找解析配置时（即调用 `resolveConfig` 时）将使用的 Prettier 配置文件的路径。返回一个 Promise，解析为：

- 配置文件的路径。
- `null`（如果未找到文件）。

如果解析配置文件时出错，Promise 将被拒绝。

搜索从 `process.cwd()` 开始，如果提供了 `fileUrlOrPath`，则从其所在目录开始。

```js
const configFile = await prettier.resolveConfigFile(filePath);
// 你得到了配置文件的路径
```

#### `prettier.clearConfigCache()`

当 Prettier 加载配置文件和插件时，会缓存文件系统结构以提高性能。此函数将清除缓存。一般只有编辑器集成才需要它——它们知道自上次格式化以来文件系统已发生变化。

#### `prettier.getFileInfo(fileUrlOrPath [, options])`

`getFileInfo` 可供编辑器扩展用来决定某个特定文件是否需要格式化。此方法返回一个 Promise，解析为具有以下属性的对象：

```ts
{
  ignored: boolean;
  inferredParser: string | null;
}
```

如果 `fileUrlOrPath` 的类型不是 `string` 或 `URL`，Promise 将被拒绝。

设置 `options.ignorePath`（`string | URL | (string | URL)[]`）和 `options.withNodeModules`（`boolean`）会影响 `ignored` 的值（默认为 `false`）。

如果给定的 `fileUrlOrPath` 被忽略，`inferredParser` 始终为 `null`。

在 `options.plugins`（`(string | URL | Plugin)[]`）中提供[插件](https://prettier.io/docs/plugins)路径，有助于为 Prettier 核心不支持的文件提取 `inferredParser`。

将 `options.resolveConfig`（`boolean`，默认 `true`）设置为 `false` 时，Prettier 将不会搜索配置文件。如果此函数仅用于检查文件是否被忽略，这会很有用。

#### `prettier.getSupportInfo()`

返回一个 Promise，解析为一个表示 Prettier 支持的选项、解析器、语言和文件类型的对象。

支持信息如下所示：

```ts
{
  languages: Array<{
    name: string;
    parsers: string[];
    group?: string;
    tmScope?: string;
    aceMode?: string;
    codemirrorMode?: string;
    codemirrorMimeType?: string;
    aliases?: string[];
    extensions?: string[];
    filenames?: string[];
    linguistLanguageId?: number;
    vscodeLanguageIds?: string[];
    isSupported?(options: { filepath: string }): boolean;
  }>;
}
```

:::note

Prettier 无法确保 `filepath` 存在于磁盘上。
从 API（如 `prettier.format()`）使用时，Prettier 也无法确保它是有效路径。

:::

<a name="custom-parser-api"></a>

#### 自定义解析器 API（已移除）（Custom Parser API (removed)）

_在 v3.0.0 中移除（由插件 API 取代）_

在[插件](https://prettier.io/docs/plugins)出现之前，Prettier 有一个类似但更受限的特性，叫做自定义解析器。它在 v3.0.0 中被移除，因为其功能是插件 API 的子集。如果你用过它，请查看下面关于如何迁移的示例。

❌ 自定义解析器 API（已移除）：

```js
import { format } from "prettier";

format("lodash ( )", {
  parser(text, { babel }) {
    const ast = babel(text);
    ast.program.body[0].expression.callee.name = "_";
    return ast;
  },
});
// -> "_();\n"
```

✔️ 插件 API：

```js
import { format } from "prettier";
import * as prettierPluginBabel from "prettier/plugins/babel";

const myCustomPlugin = {
  parsers: {
    "my-custom-parser": {
      async parse(text) {
        const ast = await prettierPluginBabel.parsers.babel.parse(text);
        ast.program.body[0].expression.callee.name = "_";
        return ast;
      },
      astFormat: "estree",
    },
  },
};

await format("lodash ( )", {
  parser: "my-custom-parser",
  plugins: [myCustomPlugin],
});
// -> "_();\n"
```

:::note

总的来说，不建议用这种方式做 codemod。Prettier 使用 AST 节点的位置数据来做许多事情，比如保留空行和附加注释。当 AST 在解析后被修改时，位置数据往往会失同步，这可能导致不可预测的结果。如果你需要 codemod，请考虑使用 [jscodeshift](https://github.com/facebook/jscodeshift)。

:::

作为已移除的自定义解析器 API 的一部分，之前可以通过 `--parser` 选项传递导出 `parse` 函数的模块路径。请改用 `--plugin` CLI 选项或 `plugins` API 选项来[加载插件](https://prettier.io/docs/plugins#using-plugins)。


---

### 浏览器（Browser）

> 原文链接：https://prettier.io/docs/browser

使用 Prettier 的 **standalone（独立）**版本在浏览器中运行 Prettier。此版本不依赖 Node.js。它只格式化代码，不支持配置文件、忽略文件、CLI 用法或插件的自动加载。

独立版本以以下形式提供：

- ES modules：`standalone.mjs`，从 3.0 版本开始（在版本 2 中为 `esm/standalone.mjs`）。
- UMD：`standalone.js`，从 1.13 版本开始。

Prettier 的 `package.json` 中的 [`browser` 字段](https://github.com/defunctzombie/package-browser-field-spec)指向 `standalone.js`。这就是为什么你可以直接 `import` 或 `require` `prettier` 模块来访问 Prettier 的 API，并且只要使用 webpack 或其他支持 `browser` 字段的打包器，你的代码就可以同时兼容 Node 和浏览器。这对[插件](https://prettier.io/docs/plugins)来说尤其方便。

#### `prettier.format(code, options)`

必需选项：

- **[`parser`](https://prettier.io/docs/options#parser)（或 [`filepath`](https://prettier.io/docs/options#file-path)）**：必须指定这些选项之一，Prettier 才能知道使用哪个解析器。

- **`plugins`**：与[基于 Node.js 的 API](https://prettier.io/docs/api#prettierformatsource-options) 中的 `format` 函数不同，此函数不会自动加载插件。`plugins` 选项是必需的，因为 Prettier 包中包含的所有解析器都以插件形式提供（出于文件大小的考虑）。这些插件是 [https://unpkg.com/browse/prettier/plugins](https://unpkg.com/browse/prettier/plugins) 中的文件。请注意，打印 JavaScript、TypeScript、Flow 或 JSON 时应加载 `estree` 插件。

  你需要加载你要使用的插件，并使用 `plugins` 选项将它们传给 `prettier.format`。

示例见下文。

#### 用法（Usage）

##### 全局方式（Global）

```html
<script src="https://unpkg.com/prettier/standalone.js"></script>
<script src="https://unpkg.com/prettier/plugins/graphql.js"></script>
<script>
  (async () => {
    const formatted = await prettier.format("type Query { hello: String }", {
      parser: "graphql",
      plugins: prettierPlugins,
    });
  })();
</script>
```

请注意，Prettier 的 `package.json` 中的 [`unpkg` 字段](https://unpkg.com/#examples)指向 `standalone.js`，这就是为什么也可以使用 `https://unpkg.com/prettier` 代替 `https://unpkg.com/prettier/standalone.js`。

##### ES Modules

```html
<script type="module">
  import * as prettier from "https://unpkg.com/prettier/standalone.mjs";
  import * as prettierPluginGraphql from "https://unpkg.com/prettier/plugins/graphql.mjs";

  const formatted = await prettier.format("type Query { hello: String }", {
    parser: "graphql",
    plugins: [prettierPluginGraphql],
  });
</script>
```

##### AMD

```js
define([
  "https://unpkg.com/prettier/standalone.js",
  "https://unpkg.com/prettier/plugins/graphql.js",
], async (prettier, ...plugins) => {
  const formatted = await prettier.format("type Query { hello: String }", {
    parser: "graphql",
    plugins,
  });
});
```

##### CommonJS

```js
const prettier = require("prettier/standalone");
const prettierPluginGraphql = require("prettier/plugins/graphql");

(async () => {
  const formatted = await prettier.format("type Query { hello: String }", {
    parser: "graphql",
    plugins: [prettierPluginGraphql],
  });
})();
```

此语法不一定能在浏览器中工作，但可以在用 browserify、Rollup、webpack 或其他打包器打包代码时使用。

##### Worker

```js
// Module worker
import * as prettier from "https://unpkg.com/prettier/standalone.mjs";
import * as prettierPluginGraphql from "https://unpkg.com/prettier/plugins/graphql.mjs";

const formatted = await prettier.format("type Query { hello: String }", {
  parser: "graphql",
  plugins: [prettierPluginGraphql],
});
```

```js
// Classic worker
importScripts(
  "https://unpkg.com/prettier/standalone.js",
  "https://unpkg.com/prettier/plugins/graphql.js",
);

(async () => {
  const formatted = await prettier.format("type Query { hello: String }", {
    parser: "graphql",
    plugins: prettierPlugins,
  });
})();
```

#### 用于嵌入式代码的解析器插件（Parser plugins for embedded code）

如果你想格式化[嵌入式代码](https://prettier.io/docs/options#embedded-language-formatting)，还需要加载相关的插件。例如：

```html
<script type="module">
  import * as prettier from "https://unpkg.com/prettier/standalone.mjs";
  import * as prettierPluginBabel from "https://unpkg.com/prettier/plugins/babel.mjs";
  import * as prettierPluginEstree from "https://unpkg.com/prettier/plugins/estree.mjs";

  console.log(
    await prettier.format("const html=/* HTML */ `<DIV> </DIV>`", {
      parser: "babel",
      plugins: [prettierPluginBabel, prettierPluginEstree],
    }),
  );
  // 输出：const html = /* HTML */ `<DIV> </DIV>`;
</script>
```

嵌入在 JavaScript 中的 HTML 代码保持未格式化状态，因为 `html` 解析器没有被加载。正确用法：

```html
<script type="module">
  import * as prettier from "https://unpkg.com/prettier/standalone.mjs";
  import * as prettierPluginBabel from "https://unpkg.com/prettier/plugins/babel.mjs";
  import * as prettierPluginEstree from "https://unpkg.com/prettier/plugins/estree.mjs";
  import * as prettierPluginHtml from "https://unpkg.com/prettier/plugins/html.mjs";

  console.log(
    await prettier.format("const html=/* HTML */ `<DIV> </DIV>`", {
      parser: "babel",
      plugins: [prettierPluginBabel, prettierPluginEstree, prettierPluginHtml],
    }),
  );
  // 输出：const html = /* HTML */ `<div></div>`;
</script>
```


---

### 在 CI 上运行 Prettier（Run Prettier on CI）

> 原文链接：https://prettier.io/docs/ci

#### GitHub Actions

要在 GitHub Actions 中应用 Prettier 的自动修复，请执行以下操作：

1. 安装 [`autofix.ci`](https://github.com/apps/autofix-ci) GitHub App。
1. 确保你的仓库中安装了**锁定**版本的 Prettier。
1. 创建 `.github/workflows/prettier.yml`，内容如下：

   ```yaml title=".github/workflows/prettier.yml"
   name: autofix.ci
   on:
     pull_request:
     push:
   permissions: {}
   jobs:
     prettier:
       runs-on: ubuntu-latest
       steps:
         - uses: actions/checkout@v4
         - uses: actions/setup-node@v4
         - run: |
             yarn
             yarn prettier . --write
         - uses: autofix-ci/action@v1
           with:
             commit-message: "Apply Prettier format"
   ```

更多信息请参见 [autofix.ci](https://autofix.ci/) 网站。


---

## 配置 Prettier（Configuring Prettier）

### 选项（Options）

> 原文链接：https://prettier.io/docs/options

Prettier 附带少量格式化选项。

**要了解更多关于 Prettier 对选项的立场——请参见[选项理念](https://prettier.io/docs/option-philosophy)。**

如果你要更改任何选项，建议通过[配置文件](https://prettier.io/docs/configuration)进行。这样 Prettier CLI、[编辑器集成](https://prettier.io/docs/editors)和其他工具都能知道你使用了哪些选项。

#### 实验性三元表达式格式（Experimental Ternaries）

在 Prettier 的[新三元表达式格式](https://github.com/prettier/prettier/pull/13183)成为默认行为之前试用它。

有效选项：

- `true` - 使用 curious ternaries，问号放在条件之后。
- `false` - 保留三元表达式的默认行为；问号与结果表达式保持在同一行。

| 默认值  | CLI 覆盖                   | API 覆盖                          |
| ------- | -------------------------- | --------------------------------- |
| `false` | `--experimental-ternaries` | `experimentalTernaries: <bool>`   |

#### 实验性运算符位置（Experimental Operator Position）

有效选项：

- `"start"` - 当二元表达式换行时，将运算符打印在新行的开头。
- `"end"` - 默认行为；当二元表达式换行时，将运算符打印在前一行的末尾。

| 默认值  | CLI 覆盖                                                          | API 覆盖                                                         |
| ------- | ----------------------------------------------------------------- | ---------------------------------------------------------------- |
| `"end"` | <code>--experimental-operator-position \<start&#124;end></code>   | <code>experimentalOperatorPosition: "\<start&#124;end>"</code>   |

#### 打印宽度（Print Width）

指定 printer 换行的行长度。

:::warning

**为了可读性，我们不建议使用超过 80 个字符：**

在代码风格指南中，最大行长度规则通常设置为 100 或 120。然而，当人类编写代码时，他们不会努力让每一行都达到最大列数。开发者经常使用空白来拆分长行以提高可读性。在实践中，平均行长度往往远低于最大值。

Prettier 的 printWidth 选项的工作方式不同。它不是硬性的行长度上限。它是一种告诉 Prettier 你大致希望行有多长的方式。Prettier 会生成更短和更长的行，但总体上会努力满足指定的 printWidth。

请记住，计算机是笨的。你需要明确告诉它们该做什么，而人类可以自己做出（隐式的）判断，例如何时换行。

换句话说，不要试图把 printWidth 当成 ESLint 的 [max-len](https://eslint.org/docs/rules/max-len) 来用——它们不是一回事。max-len 只说明允许的最大行长度，而不说明通常首选的长度——后者才是 printWidth 所指定的。

:::

| 默认值 | CLI 覆盖              | API 覆盖            |
| ------ | --------------------- | ------------------- |
| `80`   | `--print-width <int>` | `printWidth: <int>` |

在 [`.editorconfig` 文件](https://editorconfig.org/)中设置 `max_line_length` 将配置 Prettier 的打印宽度，除非被覆盖。

（如果你在格式化 Markdown 时不想要换行，可以将 [Prose Wrap](#散文换行prose-wrap) 选项设置为禁用它。）

#### 制表符宽度（Tab Width）

指定每个缩进级别的空格数。

| 默认值 | CLI 覆盖            | API 覆盖          |
| ------ | ------------------- | ----------------- |
| `2`    | `--tab-width <int>` | `tabWidth: <int>` |

在 [`.editorconfig` 文件](https://editorconfig.org/)中设置 `indent_size` 或 `tab_width` 将配置 Prettier 的制表符宽度，除非被覆盖。

#### 制表符（Tabs）

用制表符而不是空格缩进行。

| 默认值  | CLI 覆盖     | API 覆盖          |
| ------- | ------------ | ----------------- |
| `false` | `--use-tabs` | `useTabs: <bool>` |

在 [`.editorconfig` 文件](https://editorconfig.org/)中设置 `indent_style` 将配置 Prettier 的制表符使用，除非被覆盖。

（制表符将用于*缩进*，但 Prettier 使用空格来*对齐*内容，例如在三元表达式中。这种行为被称为 [SmartTabs](https://www.emacswiki.org/emacs/SmartTabs)。）

#### 分号（Semicolons）

在语句末尾打印分号。

有效选项：

- `true` - 在每个语句末尾添加分号。
- `false` - 只在[可能引入 ASI 故障](https://prettier.io/docs/rationale#semicolons)的行开头添加分号。

| 默认值 | CLI 覆盖     | API 覆盖       |
| ------ | ------------ | -------------- |
| `true` | `--no-semi`  | `semi: <bool>` |

#### 引号（Quotes）

使用单引号而不是双引号。

注意：

- JSX 引号忽略此选项——参见 [jsx-single-quote](#jsx-引号jsx-quotes)。
- 如果一种引号的数量超过另一种，将使用较少使用的那种引号来格式化字符串——例如：`"I'm double quoted"` 得到 `"I'm double quoted"`，而 `"This \"example\" is single quoted"` 得到 `'This "example" is single quoted'`。

更多信息请参见[字符串的设计原理](https://prettier.io/docs/rationale#strings)。

| 默认值  | CLI 覆盖         | API 覆盖              |
| ------- | ---------------- | --------------------- |
| `false` | `--single-quote` | `singleQuote: <bool>` |

#### 属性引号（Quote Props）

更改对象中的属性何时加引号。

有效选项：

- `"as-needed"` - 仅在需要的地方为对象属性添加引号。
- `"consistent"` - 如果对象中至少有一个属性需要引号，则为所有属性加引号。
- `"preserve"` - 尊重对象属性中引号的输入用法。

| 默认值        | CLI 覆盖                                                              | API 覆盖                                                              |
| ------------- | --------------------------------------------------------------------- | --------------------------------------------------------------------- |
| `"as-needed"` | <code>--quote-props \<as-needed&#124;consistent&#124;preserve></code> | <code>quoteProps: "\<as-needed&#124;consistent&#124;preserve>"</code> |

请注意，Prettier 从不在 Angular 表达式、TypeScript 和 Flow 中去除数字属性名的引号，因为字符串键和数字键之间的区别在这些语言中很重要。参见：[Angular](https://codesandbox.io/s/hungry-morse-foj87?file=/src/app/app.component.html)、[TypeScript](https://www.typescriptlang.org/play?#code/DYUwLgBAhhC8EG8IEYBcKA0EBM7sQF8AoUSAIzkQgHJlr1ktrt6dCiiATEAY2CgBOICKWhR0AaxABPAPYAzCGGkAHEAugBuLr35CR4CGTKSZG5Wo1ltRKDHjHtQA)、[Flow](https://flow.org/try/#0PQKgBAAgZgNg9gdzCYAoVBjOA7AzgFzAA8wBeMAb1TDAAYAuMARlQF8g)。Prettier 也不会为 Vue 去除数字属性的引号（参见相关的 [issue](https://github.com/prettier/prettier/issues/10127)）。

#### JSX 引号（JSX Quotes）

在 JSX 中使用单引号而不是双引号。

| 默认值  | CLI 覆盖             | API 覆盖                 |
| ------- | -------------------- | ------------------------ |
| `false` | `--jsx-single-quote` | `jsxSingleQuote: <bool>` |

#### 尾随逗号（Trailing Commas）

_默认值在 v3.0.0 中从 `es5` 更改为 `all`_

在多行的、以逗号分隔的语法结构中尽可能打印尾随逗号。（例如，单行数组永远不会获得尾随逗号。）

有效选项：

- `"all"` - 尽可能使用尾随逗号（包括[函数参数和调用](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Trailing_commas#Trailing_commas_in_functions)）。以这种方式格式化的 JavaScript 代码需要支持 ES2017 的引擎（Node.js 8+ 或现代浏览器）或[向下编译](https://babeljs.io/docs/index)才能运行。这也启用了 TypeScript 类型参数中的尾随逗号（自 2018 年 1 月发布的 TypeScript 2.7 起支持）。
- `"es5"` - 在 ES5 中有效的地方使用尾随逗号（对象、数组等）。TypeScript 和 Flow 的类型参数中使用尾随逗号。
- `"none"` - 不使用尾随逗号。

| 默认值  | CLI 覆盖                                                    | API 覆盖                                                    |
| ------- | ----------------------------------------------------------- | ----------------------------------------------------------- |
| `"all"` | <code>--trailing-comma \<all&#124;es5&#124;none></code>     | <code>trailingComma: "\<all&#124;es5&#124;none>"</code>     |

#### 括号间距（Bracket Spacing）

在对象字面量的括号之间打印空格。

有效选项：

- `true` - 示例：`{ foo: bar }`。
- `false` - 示例：`{foo: bar}`。

| 默认值 | CLI 覆盖               | API 覆盖                 |
| ------ | ---------------------- | ------------------------ |
| `true` | `--no-bracket-spacing` | `bracketSpacing: <bool>` |

#### 对象换行（Object Wrap）

_最早在 v3.5.0 可用_

配置 Prettier 在对象字面量可以放在一行或跨多行时如何换行。

默认情况下，如果第一个属性之前有换行符，Prettier 会将对象格式化为多行。作者可以使用这种启发式方法根据上下文提高可读性，尽管它有一些缺点。参见[多行对象](https://prettier.io/docs/rationale#multi-line-objects)。

有效选项：

- `"preserve"` - 如果左花括号和第一个属性之间有换行符，则保持多行。
- `"collapse"` - 尽可能放在一行上。

| 默认值       | CLI 覆盖                                             | API 覆盖                                             |
| ------------ | ---------------------------------------------------- | ---------------------------------------------------- |
| `"preserve"` | <code>--object-wrap \<preserve&#124;collapse></code> | <code>objectWrap: "\<preserve&#124;collapse>"</code> |

#### 括号行（Bracket Line）

将多行 HTML（HTML、JSX、Vue、Angular）元素的 `>` 放在最后一行的末尾，而不是单独放在下一行（不适用于自闭合元素）。

有效选项：

- `true` - 示例：

<!-- prettier-ignore -->
```html
<button
  className="prettier-class"
  id="prettier-id"
  onClick={this.handleClick}>
  Click Here
</button>
```

- `false` - 示例：

<!-- prettier-ignore -->
```html
<button
  className="prettier-class"
  id="prettier-id"
  onClick={this.handleClick}
>
  Click Here
</button>
```

| 默认值  | CLI 覆盖              | API 覆盖                  |
| ------- | --------------------- | ------------------------- |
| `false` | `--bracket-same-line` | `bracketSameLine: <bool>` |

#### [已废弃] JSX 括号（[Deprecated] JSX Brackets）

:::danger

此选项已在 v2.4.0 中废弃，请改用 --bracket-same-line。

:::

将多行 JSX 元素的 `>` 放在最后一行的末尾，而不是单独放在下一行（不适用于自闭合元素）。

有效选项：

- `true` - 示例：

<!-- prettier-ignore -->
```jsx
<button
  className="prettier-class"
  id="prettier-id"
  onClick={this.handleClick}>
  Click Here
</button>
```

- `false` - 示例：

<!-- prettier-ignore -->
```jsx
<button
  className="prettier-class"
  id="prettier-id"
  onClick={this.handleClick}
>
  Click Here
</button>
```

| 默认值  | CLI 覆盖                  | API 覆盖                     |
| ------- | ------------------------- | ---------------------------- |
| `false` | `--jsx-bracket-same-line` | `jsxBracketSameLine: <bool>` |

#### 箭头函数括号（Arrow Function Parentheses）

_最早在 v1.9.0 可用，默认值在 v2.0.0 中从 `avoid` 更改为 `always`_

为单个箭头函数参数加上括号。

有效选项：

- `"always"` - 始终包含括号。示例：`(x) => x`
- `"avoid"` - 尽可能省略括号。示例：`x => x`

| 默认值     | CLI 覆盖                                         | API 覆盖                                         |
| ---------- | ------------------------------------------------ | ------------------------------------------------ |
| `"always"` | <code>--arrow-parens \<always&#124;avoid></code> | <code>arrowParens: "\<always&#124;avoid>"</code> |

乍一看，避免括号似乎是更好的选择，因为视觉噪音更少。
然而，当 Prettier 移除括号时，添加类型注解、额外参数或默认值以及进行其他更改变得更加困难。
在编辑真实代码库时，一致使用括号能提供更好的开发者体验，这就是该选项默认值的理由。

#### 范围（Range）

只格式化文件的一部分。

这两个选项可用于格式化从给定字符偏移量开始和结束的代码（分别为包含和不包含）。范围将扩展：

- 向后扩展到包含所选语句的第一行的开头。
- 向前扩展到所选语句的末尾。

| 默认值     | CLI 覆盖              | API 覆盖            |
| ---------- | --------------------- | ------------------- |
| `0`        | `--range-start <int>` | `rangeStart: <int>` |
| `Infinity` | `--range-end <int>`   | `rangeEnd: <int>`   |

#### 解析器（Parser）

指定使用哪个解析器。

Prettier 会自动从输入文件路径推断解析器，所以你不应该需要更改此设置。

`babel` 和 `flow` 解析器支持相同的 JavaScript 特性集（包括 Flow 类型注解）。它们在某些边缘情况下可能有所不同，如果你遇到这种情况，可以尝试用 `flow` 代替 `babel`。`typescript` 和 `babel-ts` 的情况几乎相同。`babel-ts` 可能支持 TypeScript 尚不支持的 JavaScript 特性（提案），但它对无效代码的容错性较低，也没有 `typescript` 解析器那样经过实战检验。

有效选项：

- `"babel"`（通过 [@babel/parser](https://github.com/babel/babel/tree/main/packages/babel-parser)）_在 v1.16.0 之前名为 `"babylon"`_
- `"babel-flow"`（与 `"babel"` 相同，但显式启用 Flow 解析以避免歧义）_最早在 v1.16.0 可用_
- `"babel-ts"`（类似于 `"typescript"`，但使用 Babel 及其 TypeScript 插件）_最早在 v2.0.0 可用_
- `"flow"`（通过 [flow-parser](https://github.com/facebook/flow/tree/master/src/parser)）
- `"typescript"`（通过 [@typescript-eslint/typescript-estree](https://github.com/typescript-eslint/typescript-eslint)）_最早在 v1.4.0 可用_
- `"espree"`（通过 [espree](https://github.com/eslint/espree)）_最早在 v2.2.0 可用_
- `"meriyah"`（通过 [meriyah](https://github.com/meriyah/meriyah)）_最早在 v2.2.0 可用_
- `"acorn"`（通过 [acorn](https://github.com/acornjs/acorn)）_最早在 v2.6.0 可用_
- `"css"`（通过 [postcss](https://github.com/postcss/postcss)）_最早在 v1.7.1 可用_
- `"scss"`（通过 [postcss-scss](https://github.com/postcss/postcss-scss)）_最早在 v1.7.1 可用_
- `"less"`（通过 [postcss-less](https://github.com/shellscape/postcss-less)）_最早在 v1.7.1 可用_
- `"json"`（通过 [@babel/parser parseExpression](https://babeljs.io/docs/babel-parser#babelparserparseexpressioncode-options)）_最早在 v1.5.0 可用_
- `"json5"`（与 `"json"` 相同的解析器，但以 [json5](https://json5.org/) 输出）_最早在 v1.13.0 可用_
- `"jsonc"`（与 `"json"` 相同的解析器，但以 "JSON with Comments" 输出）_最早在 v3.2.0 可用_
- `"json-stringify"`（像 `JSON.parse()` 一样解析，但不那么严格，空白类似于 `JSON.stringify()`）_最早在 v1.13.0 可用_
- `"graphql"`（通过 [graphql/language](https://github.com/graphql/graphql-js/tree/main/src/language)）_最早在 v1.5.0 可用_
- `"markdown"`（通过 [micromark](https://github.com/micromark/micromark)）_最早在 v1.8.0 可用_
- `"mdx"`（通过 [remark-parse](https://github.com/wooorm/remark/tree/main/packages/remark-parse) 和 [remark-mdx](https://github.com/mdx-js/mdx/tree/main/packages/remark-mdx)）_最早在 v1.15.0 可用_
- `"html"`（通过 [angular-html-parser](https://github.com/ikatyang/angular-html-parser/tree/master/packages/angular-html-parser)）_最早在 1.15.0 可用_
- `"vue"`（与 `"html"` 相同的解析器，但还格式化 vue 特定语法）_最早在 1.10.0 可用_
- `"angular"`（与 `"html"` 相同的解析器，但通过 [angular-estree-parser](https://github.com/ikatyang/angular-estree-parser) 还格式化 angular 特定语法）_最早在 1.15.0 可用_
- `"lwc"`（与 `"html"` 相同的解析器，但还格式化不带引号模板属性的 LWC 特定语法）_最早在 1.17.0 可用_
- `"mjml"`（与 `"html"` 相同的解析器，但还格式化 MJML 特定语法）_最早在 3.6.0 可用_
- `"glimmer"`（Ember / Handlebars，通过 [@glimmer/syntax](https://github.com/glimmerjs/glimmer-vm/tree/main/packages/%40glimmer/syntax)）_最早在 1.10.0 可用_
- `"yaml"`（通过 [yaml](https://github.com/eemeli/yaml) 和 [yaml-unist-parser](https://github.com/ikatyang/yaml-unist-parser)）_最早在 1.14.0 可用_

| 默认值 | CLI 覆盖            | API 覆盖             |
| ------ | ------------------- | -------------------- |
| 无     | `--parser <string>` | `parser: "<string>"` |

注意：在 v1.13.0 之前，默认值是 `"babylon"`。

注意：自定义解析器 API 已在 v3.0.0 中移除。请改用[插件](https://prettier.io/docs/plugins)（[如何迁移](https://prettier.io/docs/api#custom-parser-api-removed)）。

<a name="filepath"></a>

#### 文件路径（File Path）

指定用于推断使用哪个解析器的文件名。

例如，以下命令将使用 CSS 解析器：

```bash
cat foo | prettier --stdin-filepath foo.css
```

此选项仅在 CLI 和 API 中有用。在配置文件中使用它没有意义。

| 默认值 | CLI 覆盖                    | API 覆盖               |
| ------ | --------------------------- | ---------------------- |
| 无     | `--stdin-filepath <string>` | `filepath: "<string>"` |

#### 要求 Pragma（Require Pragma）

_最早在 v1.7.0 可用_

Prettier 可以限制自己只格式化文件顶部包含特殊注释（称为 pragma）的文件。这在逐步将大型、未格式化的代码库迁移到 Prettier 时非常有用。

当提供 `--require-pragma` 时，第一个注释为以下内容的文件将被格式化：

```js
/**
 * @prettier
 */
```

或

```js
/**
 * @format
 */
```

| 默认值  | CLI 覆盖           | API 覆盖                |
| ------- | ------------------ | ----------------------- |
| `false` | `--require-pragma` | `requirePragma: <bool>` |

#### 插入 Pragma（Insert Pragma）

_最早在 v1.8.0 可用_

Prettier 可以在文件顶部插入一个特殊的 `@format` 标记，表明该文件已用 Prettier 格式化。这与 `--require-pragma` 选项配合使用效果良好。如果文件顶部已经有一个 docblock，此选项将为其添加一个带有 `@format` 标记的新行。

请注意，"配合"并不意味着"同时"。当两个选项同时使用时，`--require-pragma` 优先，因此 `--insert-pragma` 被忽略。其设计思路是：在大型代码库中逐步采用 Prettier 的过程中，参与迁移过程的开发者使用 `--insert-pragma`，而团队的其他成员和自动化工具使用 `--require-pragma`，只处理已完成迁移的文件。该功能的灵感来自 Facebook 的[采用策略](https://prettier.io/blog/2017/05/03/1.3.0.html#facebook-adoption-update)。

| 默认值  | CLI 覆盖          | API 覆盖               |
| ------- | ----------------- | ---------------------- |
| `false` | `--insert-pragma` | `insertPragma: <bool>` |

#### 检查忽略 Pragma（Check Ignore Pragma）

_最早在 v3.6.0 可用_

Prettier 可以允许单个文件通过在文件顶部包含特殊注释（称为 pragma）来选择退出格式化。

检查这些标记会在格式化时产生少量的前期开销，因此默认未启用。

当提供 `--check-ignore-pragma` 时，第一个注释为以下内容的文件将**不会**被格式化：

```js
/**
 * @noprettier
 */
```

或

```js
/**
 * @noformat
 */
```

| 默认值  | CLI 覆盖                | API 覆盖                    |
| ------- | ----------------------- | --------------------------- |
| `false` | `--check-ignore-pragma` | `checkIgnorePragma: <bool>` |

#### 散文换行（Prose Wrap）

_最早在 v1.8.2 可用_

默认情况下，Prettier 不会更改 markdown 文本中的换行，因为某些服务使用对换行敏感的渲染器，例如 GitHub 评论和 BitBucket。要让 Prettier 将散文换行到打印宽度，将此选项更改为 "always"。如果你想让 Prettier 强制所有散文块都在一行上，并依赖编辑器/查看器的软换行，可以使用 `"never"`。

有效选项：

- `"always"` - 将散文换行到 `printWidth`。
- `"never"` - 将每个散文块放在一行上。
- `"preserve"` - 按原样保留现有的换行。_最早在 v1.9.0 可用_

例如，给定这个 Markdown 段落：

```md
The quick brown
fox jumps over the lazy dog.
```

使用 `printWidth: 20`，Prettier 将其格式化为：

- `"always"`

  ```md
  The quick brown fox
  jumps over the lazy
  dog.
  ```

- `"never"`

  ```md
  The quick brown fox jumps over the lazy dog.
  ```

- `"preserve"`

  ```md
  The quick brown
  fox jumps over the lazy dog.
  ```

| 默认值       | CLI 覆盖                                                     | API 覆盖                                                     |
| ------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `"preserve"` | <code>--prose-wrap \<always&#124;never&#124;preserve></code> | <code>proseWrap: "\<always&#124;never&#124;preserve>"</code> |

#### HTML 空白敏感性（HTML Whitespace Sensitivity）

_最早在 v1.15.0 可用。Handlebars 最早在 2.3.0 可用_

指定 HTML、Vue、Angular 和 Handlebars 的全局空白敏感性。更多信息请参见[空白敏感格式化](https://prettier.io/blog/2018/11/07/1.15.0.html#whitespace-sensitive-formatting)。

有效选项：

- `"css"` - 遵循 CSS `display` 属性的默认值。对于 Handlebars，与 `strict` 相同处理。
- `"strict"` - 所有标签周围的空白（或缺失）都被视为重要的。
- `"ignore"` - 所有标签周围的空白（或缺失）都被视为不重要的。

| 默认值  | CLI 覆盖                                                                  | API 覆盖                                                                 |
| ------- | ------------------------------------------------------------------------- | ------------------------------------------------------------------------ |
| `"css"` | <code>--html-whitespace-sensitivity \<css&#124;strict&#124;ignore></code> | <code>htmlWhitespaceSensitivity: "\<css&#124;strict&#124;ignore>"</code> |

#### Vue 文件 script 和 style 标签缩进（Vue files script and style tags indentation）

_最早在 v1.19.0 可用_

是否缩进 Vue 文件中 `<script>` 和 `<style>` 标签内的代码。

有效选项：

- `false` - 不缩进 Vue 文件中的 script 和 style 标签。
- `true` - 缩进 Vue 文件中的 script 和 style 标签。

| 默认值  | CLI 覆盖                        | API 覆盖                          |
| ------- | ------------------------------- | --------------------------------- |
| `false` | `--vue-indent-script-and-style` | `vueIndentScriptAndStyle: <bool>` |

#### 行尾（End of Line）

_最早在 v1.15.0 可用，默认值在 v2.0.0 中从 `auto` 更改为 `lf`_

由于历史原因，文本文件中存在两种常见的行尾形式。
即 `\n`（或 `LF`，_Line Feed_）和 `\r\n`（或 `CRLF`，_Carriage Return + Line Feed_）。
前者在 Linux 和 macOS 上常见，而后者在 Windows 上盛行。
解释原因的细节[可以在 Wikipedia 上找到](https://en.wikipedia.org/wiki/Newline)。

当来自不同操作系统的人们在一个项目上协作时，共享的 git 仓库很容易出现混合的行尾。
Windows 用户也可能意外地将之前提交文件的行尾从 `LF` 改为 `CRLF`。
这样做会产生大量的 `git diff`，从而使文件的逐行历史（`git blame`）更难查看。

如果你想确保你的整个 git 仓库在 Prettier 覆盖的文件中只包含 Linux 风格的行尾：

1. 确保 Prettier 的 `endOfLine` 选项设置为 `lf`（自 v2.0.0 起这是默认值）
1. 配置一个将运行 Prettier 的 [pre-commit hook](https://prettier.io/docs/precommit)
1. 配置 Prettier 在你的 CI 流水线中使用 [`--check` 标志](https://prettier.io/docs/cli#--check)运行。如果你使用 Travis CI，在 `.travis.yml` 中将 [`autocrlf` 选项](https://docs.travis-ci.com/user/customizing-the-build#git-end-of-line-conversion-control)设置为 `input`。
1. 向仓库的 `.gitattributes` 文件添加 `* text=auto eol=lf`。
   你可能需要在此更改后要求 Windows 用户重新克隆你的仓库，以确保 git 在检出时没有将 `LF` 转换为 `CRLF`。

所有操作系统中的所有现代文本编辑器都能够正确显示使用 `\n`（`LF`）的行尾。
然而，旧版本的 Windows 记事本会在视觉上把这些行挤成一行，因为它只能处理 `\r\n`（`CRLF`）。

有效选项：

- `"lf"` – 仅 Line Feed（`\n`），在 Linux、macOS 以及 git 仓库内部常见
- `"crlf"` - Carriage Return + Line Feed 字符（`\r\n`），在 Windows 上常见
- `"cr"` - 仅 Carriage Return 字符（`\r`），极少使用
- `"auto"` - 保持现有的行尾
  （一个文件内的混合值通过查看第一行之后使用的内容来规范化）

| 默认值  | CLI 覆盖                                                     | API 覆盖                                                    |
| ------- | ------------------------------------------------------------ | ----------------------------------------------------------- |
| `"lf"`  | <code>--end-of-line \<lf&#124;crlf&#124;cr&#124;auto></code> | <code>endOfLine: "\<lf&#124;crlf&#124;cr&#124;auto>"</code> |

在 [`.editorconfig` 文件](https://editorconfig.org/)中设置 `end_of_line` 将配置 Prettier 的行尾使用，除非被覆盖。

#### 嵌入式语言格式化（Embedded Language Formatting）

_最早在 v2.1.0 可用_

控制 Prettier 是否格式化文件中嵌入的带引号的代码。

当 Prettier 识别出你似乎在另一个文件中的字符串里放置了它知道如何格式化的代码时——比如在 JavaScript 中带有名为 `html` 标签的模板字面量，或 Markdown 中的代码块——它默认会尝试格式化该代码。

有时这种行为是不可取的，特别是在你可能无意将字符串解释为代码的情况下。此选项允许你在默认行为（`auto`）和完全禁用此功能（`off`）之间切换。

有效选项：

- `"auto"` – 如果 Prettier 能自动识别嵌入式代码，则格式化它。
- `"off"` - 从不自动格式化嵌入式代码。

| 默认值   | CLI 覆盖                                                     | API 覆盖                                                    |
| -------- | ------------------------------------------------------------ | ----------------------------------------------------------- |
| `"auto"` | <code>--embedded-language-formatting=\<off&#124;auto></code> | <code>embeddedLanguageFormatting: "\<off&#124;auto>"</code> |

#### 每行单个属性（Single Attribute Per Line）

_最早在 v2.6.0 可用_

在 HTML、Vue 和 JSX 中强制每行单个属性。

有效选项：

- `false` - 不强制每行单个属性。
- `true` - 强制每行单个属性。

| 默认值  | CLI 覆盖                      | API 覆盖                         |
| ------- | ----------------------------- | -------------------------------- |
| `false` | `--single-attribute-per-line` | `singleAttributePerLine: <bool>` |


---

### 配置文件（Configuration File）

> 原文链接：https://prettier.io/docs/configuration

你可以通过以下方式配置 Prettier（按优先级顺序）：

- `package.json` 或 [`package.yaml`](https://github.com/pnpm/pnpm/pull/1799) 文件中的 `"prettier"` 键。
- 用 JSON 或 YAML 编写的 `.prettierrc` 文件。
- `.prettierrc.json`、`.prettierrc.yml`、`.prettierrc.yaml` 或 `.prettierrc.json5` 文件。
- 使用 `export default` 或 `module.exports` 导出对象的 `.prettierrc.js`、`prettier.config.js`、`.prettierrc.ts` 或 `prettier.config.ts` 文件（取决于你 `package.json` 中的 [`type`](https://nodejs.org/api/packages.html#type) 值）。
- 使用 `export default` 导出对象的 `.prettierrc.mjs`、`prettier.config.mjs`、`.prettierrc.mts` 或 `prettier.config.mts` 文件。
- 使用 `module.exports` 导出对象的 `.prettierrc.cjs`、`prettier.config.cjs`、`.prettierrc.cts` 或 `prettier.config.cts` 文件。
- `.prettierrc.toml` 文件。

:::info

TypeScript 配置文件的支持需要[额外设置](#typescript-配置文件typescript-configuration-files)

:::

配置文件将从被格式化文件的位置开始解析，并向上搜索文件树，直到找到（或未找到）配置文件。

Prettier 有意不支持任何形式的全局配置。这是为了确保当项目被复制到另一台计算机时，Prettier 的行为保持不变。否则，Prettier 将无法保证团队中的每个人都得到同样一致的结果。

你可以在配置文件中使用的选项与 [API 选项](https://prettier.io/docs/options)相同。

#### TypeScript 配置文件（TypeScript Configuration Files）

TypeScript 支持需要 Node.js>=22.6.0，并且在 Node.js v24.3.0 之前需要 `--experimental-strip-types` 来运行 Node.js。

```sh
node --experimental-strip-types node_modules/prettier/bin/prettier.cjs . --write
```

或

```sh
NODE_OPTIONS="--experimental-strip-types" prettier . --write
```

#### 基本配置（Basic Configuration）

JSON：

```json title=".prettierrc.json or .prettierrc"
{
  "trailingComma": "es5",
  "tabWidth": 4,
  "semi": false,
  "singleQuote": true
}
```

JS（ES Modules）：

```js title="prettier.config.mjs, .prettierrc.mjs, prettier.config.js, or .prettierrc.js"
/**
 * @see https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
const config = {
  trailingComma: "es5",
  tabWidth: 4,
  semi: false,
  singleQuote: true,
};

export default config;
```

JS（CommonJS）：

```js title="prettier.config.cjs, .prettierrc.cjs, prettier.config.js, or .prettierrc.js"
/**
 * @see https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
const config = {
  trailingComma: "es5",
  tabWidth: 4,
  semi: false,
  singleQuote: true,
};

module.exports = config;
```

TypeScript（ES Modules）：

```ts title="prettier.config.mts, .prettierrc.mts, prettier.config.ts, or .prettierrc.ts"
import { type Config } from "prettier";

const config: Config = {
  trailingComma: "none",
};

export default config;
```

TypeScript（CommonJS）：

```ts title="prettier.config.cts, .prettierrc.cts, prettier.config.ts, or .prettierrc.ts"
import { type Config } from "prettier";

const config: Config = {
  trailingComma: "none",
};

module.exports = config;
```

YAML：

```yaml title=".prettierrc, .prettierrc.yml, or .prettierrc.yaml"
trailingComma: "es5"
tabWidth: 4
semi: false
singleQuote: true
```

TOML：

```toml title=".prettierrc.toml"
trailingComma = "es5"
tabWidth = 4
semi = false
singleQuote = true
```

#### 配置覆盖（Configuration Overrides）

overrides 让你可以为特定的文件扩展名、文件夹和特定文件使用不同的配置。

JSON：

```json title=".prettierrc"
{
  "semi": false,
  "overrides": [
    {
      "files": ["*.test.js"],
      "options": {
        "semi": true
      }
    },
    {
      "files": ["*.html", "legacy/**/*.js"],
      "options": {
        "tabWidth": 4
      }
    }
  ]
}
```

YAML：

```yaml title=".prettierrc"
semi: false
overrides:
  - files:
      - "*.test.js"
    options:
      semi: true
  - files:
      - "*.html"
      - "legacy/**/*.js"
    options:
      tabWidth: 4
```

每个 override 都必须有 `files`，可以是字符串或字符串数组。可以选择性地提供 `excludeFiles` 来为给定规则排除文件，也可以是字符串或字符串数组。

`files` 模式是相对于包含配置文件的目录（而不是当前工作目录）来匹配文件路径的，所以 `legacy/**/*.js` 只匹配该配置文件旁边的 `legacy/` 目录内的文件。不带 `/` 的模式（如 `*.html`）只匹配文件名本身，因此在任何深度都适用。

#### 设置 [parser](https://prettier.io/docs/options#parser) 选项（Setting the parser option）

默认情况下，Prettier 根据输入文件的扩展名自动推断使用哪个解析器。结合 `overrides`，你可以教 Prettier 如何解析它不认识文件。

例如，要让 Prettier 格式化它自己的 `.prettierrc` 文件，你可以这样做：

```json title=".prettierrc"
{
  "overrides": [
    {
      "files": [".prettierrc"],
      "options": {
        "parser": "json"
      }
    }
  ]
}
```

你也可以为 .js 文件切换到 `flow` 解析器，而不是默认的 `babel`：

```json title=".prettierrc"
{
  "overrides": [
    {
      "files": ["*.js"],
      "options": {
        "parser": "flow"
      }
    }
  ]
}
```

**注意：***永远不要*把 `parser` 选项放在配置的顶层。*只能*在 `overrides` 内使用它。否则你实际上禁用了 Prettier 基于文件扩展名的自动解析器推断。这会迫使 Prettier 对*所有*类型的文件使用你指定的解析器——即使这不合理，比如试图把 CSS 文件当作 JavaScript 来解析。

#### 配置 Schema（Configuration Schema）

如果你想要一个 JSON schema 来验证你的配置，可以在这里找到：[https://www.schemastore.org/prettierrc.json](https://www.schemastore.org/prettierrc.json)。

#### EditorConfig

如果你的项目中有 [`.editorconfig` 文件](https://editorconfig.org/)，Prettier 将解析它并将其属性转换为相应的 Prettier 配置。此配置将被 `.prettierrc` 等覆盖。

:::note

与 EditorConfig 规范不同，`.editorconfig` 文件的搜索会在项目根目录停止，不会继续向上。

:::

下面是对不同属性如何映射到 Prettier 行为的带注释说明：

```ini title=".editorconfig"
# 阻止编辑器在父目录中查找 .editorconfig 文件
# root = true

[*]
# 不可配置的 Prettier 行为
charset = utf-8
insert_final_newline = true
# 注意：Prettier 不会修剪模板字符串内的行尾空白，但你的编辑器可能会。
# trim_trailing_whitespace = true

# 可配置的 Prettier 行为
#（如果你的 Prettier 配置不同，请更改这些）
end_of_line = lf
indent_style = space
indent_size = 2
max_line_length = 80
```

如果你使用默认选项，这是一个可以直接复制粘贴的 `.editorconfig` 文件：

```ini title=".editorconfig"
[*]
charset = utf-8
insert_final_newline = true
end_of_line = lf
indent_style = space
indent_size = 2
max_line_length = 80
```


---

### 共享配置（Sharing configurations）

> 原文链接：https://prettier.io/docs/sharing-configurations

如果你有许多不同的项目，拥有一个可以在所有项目中使用的**共享配置**会很有帮助，而不必为每个项目复制粘贴相同的配置。

本页介绍如何创建、发布和使用可共享配置。

#### 创建可共享配置（Creating a Shareable Config）

可共享配置就是导出单个 [Prettier 配置文件](https://prettier.io/docs/configuration)的 [npm 包](https://docs.npmjs.com/about-packages-and-modules#about-packages)。

在开始之前，请确保你有：

- 一个用于发布包的 [npmjs.com](https://www.npmjs.com/) 账号
- 对[如何创建 Node.js 模块](https://docs.npmjs.com/creating-node-js-modules)的基本了解

首先，创建一个新包。我们建议创建一个名为 `@username/prettier-config` 的[作用域包](https://docs.npmjs.com/cli/v10/using-npm/scope)。

一个最小的包至少应该有两个文件：用于包配置的 `package.json` 和存放共享 Prettier 配置对象的 `index.js`：

```text
prettier-config/
├── index.js
└── package.json
```

示例 `package.json`：

```json title="package.json"
{
  "name": "@username/prettier-config",
  "version": "1.0.0",
  "description": "My personal Prettier config",
  "type": "module",
  "exports": "./index.js",
  "license": "MIT",
  "publishConfig": {
    "access": "public"
  },
  "peerDependencies": {
    "prettier": ">=3.0.0"
  },
  "devDependencies": {
    "prettier": "3.6.2"
  }
}
```

`index.js` 是你放置共享配置的地方。这个文件只是用相同的语法和相同的选项导出一个[常规的 Prettier 配置](https://prettier.io/docs/configuration)：

```js title="index.js"
/**
 * @see https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
const config = {
  trailingComma: "es5",
  tabWidth: 4,
  singleQuote: true,
};

export default config;
```

一个共享配置仓库的示例可在[这里](https://github.com/azz/prettier-config)找到。

#### 发布可共享配置（Publishing a Shareable Config）

准备好后，你可以[将你的包发布到 npm](https://docs.npmjs.com/creating-and-publishing-scoped-public-packages#publishing-scoped-public-packages)：

```bash
npm publish
```

#### 使用可共享配置（Using a Shareable Config）

你首先需要安装已发布的配置，例如：

```bash
# npm
npm install --save-dev @username/prettier-config

# yarn
yarn add --dev @username/prettier-config

# pnpm
pnpm add --save-dev @username/prettier-config

# bun
bun add --dev @username/prettier-config

# deno
deno install --dev @username/prettier-config
```

然后，你可以在你的 `package.json` 中引用它：

```json title="package.json"
{
  "name": "my-cool-library",
  "version": "1.0.0",
  "prettier": "@username/prettier-config"
}
```

如果你不想使用 `package.json`，可以使用任何受支持的扩展名来导出一个字符串，例如 `.prettierrc`：

```json title=".prettierrc"
"@company/prettier-config"
```

##### 扩展可共享配置（Extending a Shareable Config）

要*扩展*配置以覆盖共享配置中的某些属性，在 `prettier.config.mjs` 文件中导入该文件并导出修改后的内容，例如：

```js title="prettier.config.mjs"
import usernamePrettierConfig from "@username/prettier-config";

/**
 * @see https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
const config = {
  ...usernamePrettierConfig,
  semi: false,
};

export default config;
```

#### 其他示例（Other examples）

##### 在可共享配置中包含插件（Include Plugins in Shareable Configurations）

如果你想在共享配置中使用[插件](https://prettier.io/docs/plugins)，需要在配置文件的 `plugin` 数组中声明这些插件，并在 `package.json` 中将它们声明为 `dependencies`：

```js title="index.js"
import * as prettierPluginOxc from "@prettier/plugin-oxc";

/**
 * @see https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
const config = {
  singleQuote: true,
  plugins: [prettierPluginOxc],
};

export default config;
```

```diff title="package.json"
{
  "name": "@username/prettier-config",
  "version": "1.0.0",
  "description": "My personal Prettier config",
  "type": "module",
  "exports": "./index.js",
  "license": "MIT",
  "publishConfig": {
    "access": "public"
  },
+  "dependencies": {
+    "@prettier/plugin-oxc": "0.1.2"
+  },
  "peerDependencies": {
    "prettier": ">=3.0.0"
  },
  "devDependencies": {
    "prettier": "3.6.2"
  }
}
```

一个示例仓库可在[这里](https://github.com/kachkaev/routine-npm-packages/tree/bc3e658f88c0b41beb118c7a1b9b91ec647f8478/packages/prettier-config)找到。

**注意：**你可以使用 [`peerDependencies`](https://docs.npmjs.com/cli/v10/configuring-npm/package-json#peerdependencies) 代替 [`dependencies`](https://docs.npmjs.com/cli/v10/configuring-npm/package-json#dependencies)。要了解它们的区别，可以阅读 [Domenic Denicola 关于 peer dependencies 的这篇博客文章](https://nodejs.org/en/blog/npm/peer-dependencies)。


---

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


---

### WebStorm 设置（WebStorm Setup）

> 原文链接：https://prettier.io/docs/webstorm

#### JetBrains IDE（WebStorm、IntelliJ IDEA、PyCharm 等）

WebStorm 内置了对 Prettier 的支持。如果你使用的是其他 JetBrains IDE，如 IntelliJ IDEA、PhpStorm 或 PyCharm，请确保已在 _Preferences / Settings | Plugins_ 中安装并启用此[插件](https://plugins.jetbrains.com/plugin/10456-prettier)。

首先，你需要安装和配置 Prettier。你可以在[这里](https://www.jetbrains.com/help/webstorm/prettier.html#ws_prettier_install)找到操作方法。

完成后，你可以在 IDE 中做几件事。你可以使用 **Reformat with Prettier** 操作（macOS 上为 _Opt+Shift+Cmd+P_，Windows 和 Linux 上为 _Alt+Shift+Ctrl+P_）来格式化选中的代码、一个文件或整个目录。

你还可以配置 WebStorm 在保存时运行 Prettier（_Cmd+S/Ctrl+S_），或将其用作默认格式化器（_Opt+Cmd+L/Ctrl+Alt+L_）。为此，打开 _Preferences / Settings | Languages & Frameworks | JavaScript | Prettier_ 并勾选相应的复选框：**On save** 和/或 **On 'Reformat Code' action**。

![示例](https://prettier.io/images/webstorm/prettier-settings.png)

默认情况下，WebStorm 将对你在项目中编辑过的所有 _.js、.ts、.jsx_ 和 _.tsx_ 文件应用格式化。要将格式化应用于其他文件类型，或将格式化限制为仅位于特定目录中的文件，你可以使用 [glob 模式](https://github.com/isaacs/node-glob)自定义默认配置。

更多信息请参见 [WebStorm 在线帮助](https://www.jetbrains.com/help/webstorm/prettier.html)。


---

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


---

### 监视文件变化（Watching For Changes）

> 原文链接：https://prettier.io/docs/watching-files

你可以使用 [onchange](https://www.npmjs.com/package/onchange) 让 Prettier 从命令行监视文件变化。例如：

```bash
npx onchange "**/*" -- npx prettier --write --ignore-unknown {{changed}}
```

或者将以下内容添加到你的 `package.json`：

```json
{
  "scripts": {
    "prettier-watch": "onchange \"**/*\" -- prettier --write --ignore-unknown {{changed}}"
  }
}
```


---

## 杂项（Misc）

### 技术细节（Technical Details）

> 原文链接：https://prettier.io/docs/technical-details

这个 printer 是 [recast](https://github.com/benjamn/recast) 的 printer 的一个 fork，其算法被替换为 Wadler 在 "[A prettier printer](https://homepages.inf.ed.ac.uk/wadler/papers/prettier/prettier.pdf)" 中描述的算法。可能仍有一些来自 recast 的遗留代码需要清理。

基本思想是：printer 接收一个 AST 并返回输出的中间表示（IR），然后 printer 使用它来生成字符串。这样做的好处是 printer 可以"测量" IR，判断输出是否能放在一行上，如果不能就换行。

这意味着打印 AST 的大部分逻辑涉及生成由某些命令组成的输出的抽象表示。例如，`["(", line, arg, line, ")"]` 将表示左括号、参数和右括号的连接。但如果它放不下一行，printer 可以在指定 `line` 的地方换行。

[Playground](https://prettier.io/playground) 有一个特殊的模式用于探索 Prettier 的中间表示是如何打印的。要进入该模式，打开侧边栏（"Show options" 按钮）并将 `parser` 选项设置为特殊值 `doc-explorer`。

更多（粗略的）细节可以在 [commands.md](https://github.com/prettier/prettier/blob/main/commands.md) 中找到。


---

### 相关项目（Related Projects）

> 原文链接：https://prettier.io/docs/related-projects

#### ESLint 集成

- [eslint-config-prettier](https://github.com/prettier/eslint-config-prettier) 关闭所有不必要或可能与 Prettier 冲突的 ESLint 规则
- [eslint-plugin-prettier](https://github.com/prettier/eslint-plugin-prettier) 将 Prettier 作为 ESLint 规则运行，并将差异报告为单个 ESLint 问题
- [prettier-eslint](https://github.com/prettier/prettier-eslint) 将 `prettier` 的输出传给 `eslint --fix`
- [prettier-standard](https://github.com/sheerun/prettier-standard) 使用 `prettierx` 和 `prettier-eslint` 以 `standard` 规则格式化代码

#### stylelint 集成

- [stylelint-config-prettier](https://github.com/prettier/stylelint-config-prettier) 关闭所有不必要或可能与 Prettier 冲突的规则。
- [stylelint-prettier](https://github.com/prettier/stylelint-prettier) 将 Prettier 作为 stylelint 规则运行，并将差异报告为单个 stylelint 问题
- [prettier-stylelint](https://github.com/hugomrdias/prettier-stylelint) 将 `prettier` 的输出传给 `stylelint --fix`

#### Fork

- [prettierx](https://github.com/brodybits/prettierx) — 不那么有主见的 Prettier fork

#### 杂项（Misc）

- [parallel-prettier](https://github.com/microsoft/parallel-prettier) 是一个替代 CLI，可并行格式化文件以加速大型项目
- [prettier_d](https://github.com/josephfrazier/prettier_d.js) 将 Prettier 作为服务器运行，以避免 Node.js 启动延迟
- [pretty-quick](https://github.com/prettier/pretty-quick) 用 Prettier 格式化变更过的文件
- [rollup-plugin-prettier](https://github.com/mjeanroy/rollup-plugin-prettier) 允许你在 Rollup 中使用 Prettier
- [jest-runner-prettier](https://github.com/keplersj/jest-runner-prettier) 是作为 Jest runner 的 Prettier
- [prettier-chrome](https://github.com/u3u/prettier-chrome) 是在浏览器中运行 Prettier 的扩展
- [spotless](https://github.com/diffplug/spotless) 让你从 [gradle](https://github.com/diffplug/spotless/tree/main/plugin-gradle#prettier) 或 [maven](https://github.com/diffplug/spotless/tree/main/plugin-maven#prettier) 运行 prettier
- [csharpier](https://github.com/belav/csharpier) 是 Prettier 的 C# 移植版
- [Prettier](https://github.com/jaywcjlove/Prettier) 是基于 Prettier 的 Swift 版本
- [reviewdog-action-prettier](https://github.com/EPMatt/reviewdog-action-prettier) 在 GitHub Actions CI/CD 工作流中运行 Prettier
- [MegaLinter](https://megalinter.io/) 是一个开源的 CI linter 聚合器，开箱即用地运行 [Prettier](https://megalinter.io/latest/descriptors/javascript_prettier/)
- [monaco-prettier](https://github.com/remcohaszing/monaco-prettier) 将 Prettier 集成到 [Monaco editor](https://microsoft.github.io/monaco-editor/) 中


---

### 面向企业（For Enterprise）

> 原文链接：https://prettier.io/docs/for-enterprise

#### 作为 Tidelift 订阅的一部分提供

Tidelift 正在与 Prettier 以及数千个其他开源项目的维护者合作，为你用于构建应用程序的开源依赖提供商业支持和维护。节省时间、降低风险、改善代码健康，同时为你实际使用的依赖的维护者付费。

[了解更多](https://tidelift.com/subscription/pkg/npm-prettier?utm_source=npm-prettier&utm_medium=referral&utm_campaign=enterprise)
[请求演示](https://tidelift.com/subscription/request-a-demo?utm_source=npm-prettier&utm_medium=referral&utm_campaign=enterprise)

##### 企业级开源软件——为你托管

Tidelift 订阅是一个面向应用程序依赖的托管式开源订阅，覆盖 JavaScript、Python、Java、PHP、Ruby、.NET 等生态的数百万个开源项目。

你的订阅包括：

**安全更新**

Tidelift 的安全响应团队协调针对新的破坏性安全漏洞的补丁，并通过私有渠道立即发出警报，使你的软件供应链始终安全。

**许可证验证与赔偿**

Tidelift 验证许可证信息以便轻松执行策略，并增加知识产权赔偿，在出现问题时保护创建者和用户。你始终拥有 100% 最新的依赖物料清单，可与你的法律团队、客户或合作伙伴共享。

**维护与代码改进**

Tidelift 确保你依赖的软件在你需要的时间内持续可用。你托管的依赖会得到积极维护，必要时我们还会招募额外的维护者。

**包选择与版本指导**

我们帮助你从一开始就选择最好的开源包——然后在出现新问题时指导你完成更新，保持在最佳版本上。

**路线图参与**

与你所使用软件背后的创作者坐在一起。Tidelift 的参与维护者随着其软件被更多订阅者使用而获得更多收入，因此他们很想知道你的需求。

**工具与云集成**

Tidelift 与 GitHub、GitLab、BitBucket 等合作。我们支持所有云平台（以及其他部署目标）。

最终结果呢？你所期望的商业级软件的所有能力，覆盖你使用的全部开源软件。这意味着更少的时间纠结于晦涩的开源琐事，更多的时间用于构建你自己的应用程序——和你的业务。

[了解更多](https://tidelift.com/subscription/pkg/npm-prettier?utm_source=npm-prettier&utm_medium=referral&utm_campaign=enterprise)
[请求演示](https://tidelift.com/subscription/request-a-demo?utm_source=npm-prettier&utm_medium=referral&utm_campaign=enterprise)


---

