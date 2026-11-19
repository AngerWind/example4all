### 代码路径分析（Code Path Analysis）

> 原文链接：https://eslint.org/docs/latest/extend/code-path-analysis

::: tip
本页适用于 JS [语言](https://eslint.org/docs/latest/extend/languages)的 ESLint 规则。这包括使用替代解析器处理 TypeScript 和 Vue 等 JS 语法扩展的规则，但不包括 CSS 或 Markdown 等不相关语言的规则。
:::

ESLint 规则可以访问一个用于分析代码路径的 API。
代码路径表示执行在程序中可能经过的控制流图。
它由代码路径段（code path segment）组成，这些段在 `if`、`while`、`return`、`continue` 等分支结构和其他控制流语句处分叉和汇合。

下面是一个非常基础的程序的代码路径图：

```js
if (someCondition()) {
	foo();
}
bar();
```

![代码路径示例](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-simple-if-branch.svg)

::: tip
你可以使用 [Code Explorer](https://explorer.eslint.org) 查看任何 JavaScript 代码的代码路径图。
:::

#### 对象（Objects）

一个程序/文件包含多个代码路径：一个用于全局/顶层执行，另外每个函数、类字段初始化器和类静态块各有一个。
代码路径分析 API 中有两个基本对象：`CodePath` 和 `CodePathSegment`。

##### `CodePath`

`CodePath` 对象描述程序中的一个控制流图。它有以下属性：

- `id`（`string`）- 唯一标识代码路径的字符串。规则可以使用 `id` 作为键，为每个代码路径保存额外信息。
- `origin`（`string`）- 在程序中创建此代码路径的构造。可以是 `"program"`、`"function"`、`"class-field-initializer"` 或 `"class-static-block"`。
- `initialSegment`（`CodePathSegment`）- 代码路径的初始段。
- `finalSegments`（`CodePathSegment[]`）- 代码路径的最终段，包括返回（returned）和抛出（thrown）的段。
- `returnedSegments`（`CodePathSegment[]`）- 代码路径的最终段，只包括返回的段。
- `thrownSegments`（`CodePathSegment[]`）- 代码路径的最终段，只包括抛出的段。
- `upper`（`CodePath | null`）- 包含此代码路径的函数/类/全局作用域的代码路径。
- `childCodePaths`（`CodePath[]`）- 此代码路径包含的函数/类的代码路径。

`CodePath` 有以下方法：

- `traverseSegments(optionsOrCallback, callback)` - 以深度优先的顺序，从初始段到最终段遍历此代码路径中所有可达的段。你可以选择性地传递一个带有 `first`（`CodePathSegment`）和 `last`（`CodePathSegment`）属性的选项对象来限制遍历范围。回调接收一个 `CodePathSegment` 和一个控制器对象，并以 `this` 设置为当前 `CodePath` 的方式调用。控制器有一个 `skip()` 方法用于跳过当前分支中的后续段，以及一个 `break()` 方法用于停止遍历。

##### `CodePathSegment`

`CodePathSegment` 是代码路径中没有分支的一部分。在程序的每个分支点，一个 `CodePathSegment` 结束，并包含指向下一个可能的 `CodePathSegment` 的链接，形成有向图（一般来说可以包含环，例如存在 `while` 循环时）。`CodePathSegment` 还包含指向前序可能段的链接，用于程序分支汇合的情况（例如 `if` 块之后）。

`CodePathSegment` 有以下属性：

- `id`（`string`）- 唯一标识代码路径段的字符串。规则可以使用 `id` 作为键，为每个段保存额外信息。
- `nextSegments`（`CodePathSegment[]`）- 下一个（可达的）段。如果是分叉，则有两个或更多。如果是最终段，则为空。
- `prevSegments`（`CodePathSegment[]`）- 前一个（可达的）段。如果是汇合，则有两个或更多。如果是初始段，则为空。
- `allNextSegments`（`CodePathSegment[]`）- 下一个段，包括可达和不可达的段。
- `allPrevSegments`（`CodePathSegment[]`）- 前一个段，包括可达和不可达的段。
- `reachable`（`boolean`）- 指示该段是否可达的标志。在 `return`、`throw`、`break` 或 `continue` 之后的段中，此值为 `false`。

#### 访问代码路径（Accessing Code Paths）

要在规则中使用代码路径分析，你可以在规则的 `create()` 方法导出的对象（即包含 AST 节点访问器的同一个对象）中定义代码路径事件（详见下文）的事件处理器。所有访问器（包括代码路径访问器）都在对源代码的单次遍历中按源代码顺序调用。这意味着多个代码路径可能同时处于打开状态，需要手动跟踪（见下文示例["跟踪当前段位置"](#跟踪当前段位置track-current-segment-position)）。

```js
export default {
	meta: {
		// ...
	},
	create(context) {
		return {
			/**
			 * 在开始遍历代码路径时调用。
			 *
			 * @param {CodePath} codePath - 新的代码路径。
			 * @param {ASTNode} node - 当前节点。
			 * @returns {void}
			 */
			onCodePathStart(codePath, node) {
				// 对 codePath 做些什么
			},

			/**
			 * 在结束遍历代码路径时调用。
			 *
			 * @param {CodePath} codePath - 已完成的代码路径。
			 * @param {ASTNode} node - 当前节点。
			 * @returns {void}
			 */
			onCodePathEnd(codePath, node) {
				// 对 codePath 做些什么
			},

			/**
			 * 在进入一个可达的代码路径段时调用。
			 *
			 * @param {CodePathSegment} segment - 新的代码路径段。
			 * @param {ASTNode} node - 当前节点。
			 * @returns {void}
			 */
			onCodePathSegmentStart(segment, node) {
				// 对 segment 做些什么
			},

			/**
			 * 在退出一个可达的代码路径段时调用。
			 *
			 * @param {CodePathSegment} segment - 离开的代码路径段。
			 * @param {ASTNode} node - 当前节点。
			 * @returns {void}
			 */
			onCodePathSegmentEnd(segment, node) {
				// 对 segment 做些什么
			},

			/**
			 * 在进入一个不可达的代码路径段时调用。
			 *
			 * @param {CodePathSegment} segment - 进入的代码路径段。
			 * @param {ASTNode} node - 当前节点。
			 * @returns {void}
			 */
			onUnreachableCodePathSegmentStart(segment, node) {
				// 对 segment 做些什么
			},

			/**
			 * 在退出一个不可达的代码路径段时调用。
			 *
			 * @param {CodePathSegment} segment - 退出的代码路径段。
			 * @param {ASTNode} node - 当前节点。
			 * @returns {void}
			 */
			onUnreachableCodePathSegmentEnd(segment, node) {
				// 对 segment 做些什么
			},

			/**
			 * 在即将退出一个可达段（`fromSegment`）之前调用，
			 * 此时离开该段的控制流可以循环回到源代码中更早的
			 * 位置（`toSegment` 的起点），例如在循环体末尾或
			 * `continue;` 语句处。请注意，在某些情况下
			 * `fromSegment === toSegment`，例如简单的
			 * `do...while` 循环或测试/更新表达式为空的
			 * `for (;;)` 循环。
			 *
			 * 此事件总是发生在 `fromSegment` 的
			 * `onCodePathSegmentStart` 和 `onCodePathSegmentEnd` 之间。
			 * 而 `toSegment` 的 `onCodePathSegmentStart` 必定已经被调用，
			 * 其 `onCodePathSegmentEnd` 当且仅当
			 * `toSegment !== fromSegment` 时已经被调用。
			 *
			 * @param {CodePathSegment} fromSegment - 循环起始的代码路径段。
			 * @param {CodePathSegment} toSegment - 循环去往的代码路径段。
			 * @param {ASTNode} node - 当前节点。
			 * @returns {void}
			 */
			onCodePathSegmentLoop(fromSegment, toSegment, node) {
				// 对 segment 做些什么
			},

			// AST 节点访问器……
		};
	},
};
```

::: important

在旧版 ESLint（ESLint v9 之前）中，代码路径和代码路径段是在遍历过程中构建的，因此当事件被调用时，事件载荷中的代码路径和段只被部分初始化。

然而，自 ESLint v9 起，ESLint 会在规则运行之前对 AST 执行一次完整的预扫描，以构建完整的代码路径信息（[参见迁移指南](https://eslint.org/docs/latest/use/migrate-to-9.0.0#-code-paths-are-now-precalculated)）。这意味着当任何 `onCodePath*` 事件触发时，它接收的 `CodePath` 和 `CodePathSegment` 对象已经包含了所有数据（例如 `childCodePaths`、`nextSegments`、`prevSegments`），即使对于规则所见的遍历中尚未访问的代码部分也是如此。你不需要等待稍后的事件（如 `onCodePathEnd`）就能安全地读取这些属性。

:::

::: warning

`CodePath` 和 `CodePathSegment` 对象是可变的，并且在所有代码路径事件和所有规则之间共享，因此规则应注意永远不要修改它们。

:::

##### 示例：简单程序（Example: Simple Program）

考虑以下简单但并不平凡的程序：

```js
function foo(x) {
	if (x) {
		setTimeout(() => {
			console.log("logging x after 100 ms", x);
		}, 100);
	}
}

function bar(y) {
	if (typeof y === "string") {
		console.log(y);
	} else {
		console.log("not a string");
	}
}

if (Math.random() < 0.5) {
	foo();
} else {
	bar();
}
```

它有 4 个代码路径：

1. 顶层代码（`origin: "program"`）：

    ![简单程序代码路径 1](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-simpleprogram-codepath1.svg)

1. 函数 `foo`（`origin: "function"`）：

    ![简单程序代码路径 2](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-simpleprogram-codepath2.svg)

1. 在 `foo` 内部定义的箭头函数回调（`origin: "function"`）：

    ![简单程序代码路径 3](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-simpleprogram-codepath3.svg)

1. 函数 `bar`（`origin: "function"`）：

    ![简单程序代码路径 4](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-simpleprogram-codepath4.svg)

其代码路径事件按以下顺序调用（缩进表示 start/end 对之间的嵌套）：

```text
onCodePathStart                  s1   (top level)
  onCodePathSegmentStart         s1_1 (before the `if`, up through its test)
    onCodePathStart              s2   (foo)
      onCodePathSegmentStart     s2_1 (before `if (x)`, up through its test)
      onCodePathSegmentEnd       s2_1
      onCodePathSegmentStart     s2_2 (`if (x)` true branch: the `setTimeout(...)` call)
        onCodePathStart          s3   (arrow function callback)
          onCodePathSegmentStart s3_1 (the whole callback body)
          onCodePathSegmentEnd   s3_1
        onCodePathEnd            s3
      onCodePathSegmentEnd       s2_2
      onCodePathSegmentStart     s2_3 (after `if (x)`; falls off the end of `foo`)
      onCodePathSegmentEnd       s2_3
    onCodePathEnd                s2
    onCodePathStart              s4   (bar)
      onCodePathSegmentStart     s4_1 (before the `if`, up through its test)
      onCodePathSegmentEnd       s4_1
      onCodePathSegmentStart     s4_2 (`if` true branch: `console.log(y)`)
      onCodePathSegmentEnd       s4_2
      onCodePathSegmentStart     s4_3 (`else` branch: `console.log("not a string")`)
      onCodePathSegmentEnd       s4_3
      onCodePathSegmentStart     s4_4 (after the `if`/`else`, where the branches join; falls off the end of `bar`)
      onCodePathSegmentEnd       s4_4
    onCodePathEnd                s4
  onCodePathSegmentEnd           s1_1
  onCodePathSegmentStart         s1_2 (top-level `if` true branch: the `foo();` call)
  onCodePathSegmentEnd           s1_2
  onCodePathSegmentStart         s1_3 (top-level `else` branch: the `bar();` call)
  onCodePathSegmentEnd           s1_3
  onCodePathSegmentStart         s1_4 (after the top-level `if`/`else`, where the branches join)
  onCodePathSegmentEnd           s1_4
onCodePathEnd                    s1
```

##### 关于 `onCodePathSegmentLoop`（About `onCodePathSegmentLoop`）

每当遍历到达控制流循环回到之前已进入的某个段的位置时，就会触发此事件。它主要在循环末尾触发。

考虑以下 `while` 循环的代码路径：

```js
while (a) {
	a = foo();
}
bar();
```

![循环事件示例](https://eslint.org/docs/latest/assets/images/code-path-analysis/loop-event-example-while.svg)

此程序的事件序列如下所示（缩进表示 start/end 对之间的嵌套）：

```text
onCodePathStart           (program)
  onCodePathSegmentStart  (before the loop)
  onCodePathSegmentEnd    (before the loop)
  onCodePathSegmentStart  (test: `a`)
  onCodePathSegmentEnd    (test: `a`)
  onCodePathSegmentStart  (body: `a = foo();`)
    onCodePathSegmentLoop (body -> test, highlighted above in red)
  onCodePathSegmentEnd    (body)
  onCodePathSegmentStart  (after the loop: `bar();`)
  onCodePathSegmentEnd    (after the loop)
onCodePathEnd             (program)
```

请注意，`onCodePathSegmentLoop` 在 `body` 段的 `onCodePathSegmentEnd` *之前*触发：它表示控制流从 `body` 末尾的某个点循环回到 `test`。因为 `test` 在之前的步骤中已经被完整访问过，所以它的 `onCodePathSegmentStart`/`onCodePathSegmentEnd` 对不会再次触发。

`onCodePathSegmentLoop` 事件可用于分析循环中的控制流。例如，参见 [`no-unreachable-loop`](https://eslint.org/docs/latest/rules/no-unreachable-loop) 的实现。

#### 使用示例（Usage Examples）

##### 跟踪当前段位置（Track current segment position）

要跟踪当前代码路径段的位置，你可以定义一个这样的规则：

```js
export default {
	meta: {
		// ...
	},
	create(context) {
		// 跟踪我们当前所在的代码路径
		let currentCodePath;

		// 一个 Set，跟踪我们在当前代码路径中已遍历的段
		let currentSegments;

		// 栈，跟踪所有打开路径的当前段 Set。
		const allCurrentSegments = [];

		return {
			onCodePathStart(codePath) {
				currentCodePath = codePath;
				allCurrentSegments.push(currentSegments);
				currentSegments = new Set();
			},

			onCodePathEnd(codePath) {
				currentCodePath = codePath.upper;
				currentSegments = allCurrentSegments.pop();
			},

			onCodePathSegmentStart(segment) {
				currentSegments.add(segment);
			},

			onCodePathSegmentEnd(segment) {
				currentSegments.delete(segment);
			},

			onUnreachableCodePathSegmentStart(segment) {
				currentSegments.add(segment);
			},

			onUnreachableCodePathSegmentEnd(segment) {
				currentSegments.delete(segment);
			},
		};
	},
};
```

在此示例中，`currentCodePath` 变量用于访问当前正在遍历的代码路径，`currentSegments` 变量跟踪该代码路径中到该时点为止已遍历的段。请注意，`currentSegments` 以空集开始，也以空集结束，随着遍历的进行不断更新。

跟踪当前段位置有助于分析通向特定节点的代码路径，如下一个示例所示。

##### 查找不可达节点（Find an unreachable node）

要查找不可达节点，跟踪当前段位置，然后使用节点访问器检查是否有任何段是可达的。例如，下面的规则查找任何不可达的 `ExpressionStatement`。

```js
function areAnySegmentsReachable(segments) {
	for (const segment of segments) {
		if (segment.reachable) {
			return true;
		}
	}

	return false;
}

export default {
	meta: {
		// ...
	},
	create(context) {
		// 跟踪我们当前所在的代码路径
		let currentCodePath;

		// 一个 Set，跟踪我们在当前代码路径中已遍历的段
		let currentSegments;

		// 栈，跟踪所有打开路径的当前段 Set。
		const allCurrentSegments = [];

		return {
			onCodePathStart(codePath) {
				currentCodePath = codePath;
				allCurrentSegments.push(currentSegments);
				currentSegments = new Set();
			},

			onCodePathEnd(codePath) {
				currentCodePath = codePath.upper;
				currentSegments = allCurrentSegments.pop();
			},

			onCodePathSegmentStart(segment) {
				currentSegments.add(segment);
			},

			onCodePathSegmentEnd(segment) {
				currentSegments.delete(segment);
			},

			onUnreachableCodePathSegmentStart(segment) {
				currentSegments.add(segment);
			},

			onUnreachableCodePathSegmentEnd(segment) {
				currentSegments.delete(segment);
			},

			ExpressionStatement(node) {
				// 检查通向此节点的所有代码路径段
				if (!areAnySegmentsReachable(currentSegments)) {
					context.report({ message: "Unreachable!", node });
				}
			},
		};
	},
};
```

另请参阅：
[no-unreachable](https://github.com/eslint/eslint/blob/HEAD/lib/rules/no-unreachable.js)、
[no-fallthrough](https://github.com/eslint/eslint/blob/HEAD/lib/rules/no-fallthrough.js)、
[consistent-return](https://github.com/eslint/eslint/blob/HEAD/lib/rules/consistent-return.js)

##### 检查函数是否在每条路径中都被调用（Check if a function is called in every path）

此示例检查参数 `cb` 是否在每条路径中都被调用。

```js
function getCbParameter(codePath, node, context) {
	if (codePath.origin !== "function") {
		return null;
	}

	return (
		context.sourceCode
			.getDeclaredVariables(node)
			.find(
				variable =>
					variable.name === "cb" &&
					variable.defs.some(def => def.type === "Parameter"),
			) ?? null
	);
}

function hasPathWithoutCall(codePath, segmentsWithCall) {
	const visited = new Set();
	const stack = [codePath.initialSegment];

	while (stack.length > 0) {
		const segment = stack.pop();

		if (visited.has(segment) || segmentsWithCall.has(segment)) {
			continue;
		}
		visited.add(segment);

		if (codePath.finalSegments.includes(segment)) {
			return true;
		}

		stack.push(...segment.nextSegments);
	}

	return false;
}

export default {
	meta: {
		// ...
	},
	create(context) {
		let codePathInfo = null;
		const codePathInfoStack = [];

		return {
			onCodePathStart(codePath, node) {
				codePathInfoStack.push(codePathInfo);

				codePathInfo = {
					cbVariable: getCbParameter(codePath, node, context),
					currentSegments: new Set(),
					segmentsWithCall: new Set(),
				};
			},

			onCodePathEnd(codePath, node) {
				const { cbVariable, segmentsWithCall } = codePathInfo;

				codePathInfo = codePathInfoStack.pop();

				if (!cbVariable) {
					return;
				}

				if (hasPathWithoutCall(codePath, segmentsWithCall)) {
					context.report({
						message: "`cb` should be called in every path.",
						node,
					});
				}
			},

			onCodePathSegmentStart(segment) {
				codePathInfo.currentSegments.add(segment);
			},

			onCodePathSegmentEnd(segment) {
				codePathInfo.currentSegments.delete(segment);
			},

			CallExpression(node) {
				const { cbVariable } = codePathInfo;

				if (
					cbVariable?.references.some(
						reference => reference.identifier === node.callee,
					)
				) {
					for (const segment of codePathInfo.currentSegments) {
						codePathInfo.segmentsWithCall.add(segment);
					}
				}
			},
		};
	},
};
```

另请参阅：
[constructor-super](https://github.com/eslint/eslint/blob/HEAD/lib/rules/constructor-super.js)、
[no-this-before-super](https://github.com/eslint/eslint/blob/HEAD/lib/rules/no-this-before-super.js)

#### 局限性（Limitations）

ESLint 的代码路径分析是对 JavaScript 程序实际运行时可能性的近似，而不是精确模型。

例如，在现代 JavaScript 中，几乎任何东西在技术上都可能抛出异常，包括函数调用、属性访问，甚至[引用已声明的标识符](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/let#temporal_dead_zone_tdz)。
然而，在 `try` 块之外，代码路径分析假定所有这些操作都会成功，而不是对每个可能的抛出点建模。有关 `try` 构造中控制流如何建模的详细信息，请参见下面的 `TryStatement` 示例。

另一个例子是，代码路径分析将所有函数调用视为不透明的，即使能清楚地看出正在调用哪个函数。请参见下面带函数调用的示例。

#### 代码路径示例（Code Path Examples）

##### Hello World

```js
console.log("Hello world!");
```

![Hello World](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-hello-world.svg)

##### `IfStatement`

```js
if (a) {
	foo();
} else {
	bar();
}
```

![`IfStatement`](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-ifstatement.svg)

##### `IfStatement`（链式）

```js
if (a) {
	foo();
} else if (b) {
	bar();
} else if (c) {
	baz();
}
```

![`IfStatement`（链式）](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-ifstatement-chain.svg)

##### `LogicalExpression`

请注意，由于短路行为，`&&` 和 `||` 是分支结构。

```js
const foo = a && b;
```

![`LogicalExpression`](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-logicalexpression.svg)

##### `SwitchStatement`

```js
switch (a) {
	case 0:
		foo();
		break;

	case 1:
	case 2:
		bar();
	// 贯穿（fallthrough）

	case 3:
		baz();
		break;
}
```

![`SwitchStatement`](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-switchstatement.svg)

##### `SwitchStatement`（带 `default`）

```js
switch (a) {
	case 0:
		foo();
		break;

	case 1:
	case 2:
		bar();
	// 贯穿（fallthrough）

	case 3:
		baz();
		break;

	default:
		quux();
		break;
}
```

![`SwitchStatement`（带 `default`）](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-switchstatement-has-default.svg)

##### `TryStatement`（try-catch）

```js
try {
	foo();
	if (a) {
		throw new Error();
	}
	bar();
} catch (err) {
	baz(err);
}
last();
```

代码路径分析会在以下位置创建从 `try` 块到 `catch` 块的路径：

- `throw` 语句。
- `try` 块中第一个可能抛出的节点（例如函数调用）。
- `try` 块的末尾。

![`TryStatement`（try-catch）](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-trystatement-try-catch.svg)

##### `TryStatement`（try-finally）

```js
try {
	foo();
	bar();
} finally {
	baz();
}
last();
```

如果没有 `catch` 块，`finally` 块有两个当前段：正常路径和离开路径。请注意，即使 `try` 块不包含 `throw` 或 `return`，离开路径在这里也存在，因为像 `foo()` 这样的函数调用被视为可能抛出。

如果你在此代码上运行上面的"查找不可达节点"示例，遍历 `finally` 块时 `currentSegments.size` 将是 `2`。

![`TryStatement`（try-finally）](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-trystatement-try-finally.svg)

##### `TryStatement`（try-catch-finally）

```js
try {
	foo();
	bar();
} catch (err) {
	baz(err);
} finally {
	quux();
}
last();
```

![`TryStatement`（try-catch-finally）](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-trystatement-try-catch-finally.svg)

##### `WhileStatement`

```js
while (a) {
	foo();
	if (b) {
		continue;
	}
	bar();
}
```

![`WhileStatement`](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-whilestatement.svg)

##### `DoWhileStatement`

```js
do {
	foo();
	bar();
} while (a);
```

![`DoWhileStatement`](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-dowhilestatement.svg)

##### `ForStatement`

```js
for (let i = 0; i < 10; ++i) {
	foo();
	if (b) {
		break;
	}
	bar();
}
```

![`ForStatement`](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-forstatement.svg)

##### `ForStatement`（无限循环）

```js
for (;;) {
	foo();
}
bar();
```

![`ForStatement`（无限循环）](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-forstatement-infinite-loop.svg)

##### `ForInStatement`

```js
for (let key in obj) {
	foo(key);
}
```

![`ForInStatement`](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-forinstatement.svg)

##### 当有函数时（When there is a function）

```js
function foo(a) {
	if (a) {
		return;
	}
	bar();
}

foo(false);
```

此程序由两个代码路径组成：

1. 全局代码路径

    ![当有函数时——全局代码路径](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-when-there-is-a-function-1.svg)

1. 函数 `foo()` 的代码路径

    ![当有函数时——函数代码路径](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-when-there-is-a-function-2.svg)

请注意，从包含对 `foo()` 调用的代码路径段到包含 `foo()` 实现的代码路径段之间没有链接。这是因为在代码路径分析中，函数调用被视为不透明的，即使从源代码中可以明确看出正在调用哪个函数。

##### 生成器函数中的 `YieldExpression`（`YieldExpression` in generator functions）

```js
function* generator(flag) {
	yield 1;
	if (flag) {
		foo();
	}
	bar();
}
```

在生成器函数中，`yield` 可以创建三个代码路径：

- 到下一段。
- 到抛出路径。
- 到函数末尾。

![生成器函数中的 `YieldExpression`](https://eslint.org/docs/latest/assets/images/code-path-analysis/example-yieldExpression-in-generator-functions.svg)
