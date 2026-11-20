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
