### 自定义规则教程（Custom Rule Tutorial）

> 原文链接：https://eslint.org/docs/latest/extend/custom-rule-tutorial

本教程介绍如何为 ESLint 创建自定义规则，并通过插件进行分发。

你可以创建自定义规则来验证代码是否满足某个预期，并确定当不满足预期时该做什么。插件将自定义规则和其他配置打包，让你可以轻松地在不同项目中共享和复用它们。

要了解更多关于自定义规则和插件的信息，请参阅以下文档：

- [自定义规则（Custom Rules）](https://eslint.org/docs/latest/extend/custom-rules)
- [创建插件（Plugins）](https://eslint.org/docs/latest/extend/plugins)

#### 为什么要创建自定义规则？（Why Create a Custom Rule?）

当 ESLint [内置规则](https://eslint.org/docs/latest/rules/)和社区发布的自定义规则无法满足你的需求时，就创建自定义规则。你可能创建自定义规则来强制执行公司或项目的最佳实践、防止特定 bug 重现，或确保遵守风格指南。

在创建并非特定于你的公司或项目的自定义规则之前，值得先在网上搜索一下，看看是否有人发布了带有能解决你用例的自定义规则的插件。很可能该规则已经存在。

#### 前提条件（Prerequisites）

在开始之前，请确保你的开发环境中已安装以下内容：

- [Node.js](https://nodejs.org/en/download/)
- [npm](https://www.npmjs.com/)

本教程还假定你对 ESLint 和 ESLint 规则有基本的了解。

#### 自定义规则（The Custom Rule）

本教程中的自定义规则要求所有名为 `foo` 的 `const` 变量必须被赋值为字符串字面量 `"bar"`。规则定义在 `enforce-foo-bar.js` 文件中。该规则还会建议将任何赋给 `const foo` 的其他值替换为 `"bar"`。

例如，假设你有以下 `foo.js` 文件：

```javascript
// foo.js

const foo = "baz123";
```

使用该规则运行 ESLint 会将 `"baz123"` 标记为变量 `foo` 的错误值。如果 ESLint 以自动修复模式运行，ESLint 会将文件修复为包含以下内容：

```javascript
// foo.js

const foo = "bar";
```

#### 第 1 步：搭建项目（Step 1: Set up Your Project）

首先，为你的自定义规则创建一个新项目。创建一个新目录，在其中初始化一个新的 npm 项目，并为自定义规则创建一个新文件：

```shell
mkdir eslint-custom-rule-example # 创建目录
cd eslint-custom-rule-example # 进入目录
npm init -y # 初始化新的 npm 项目
touch enforce-foo-bar.js # 创建文件 enforce-foo-bar.js
```

#### 第 2 步：搭出规则文件的骨架（Step 2: Stub Out the Rule File）

在 `enforce-foo-bar.js` 文件中，为 `enforce-foo-bar` 自定义规则添加一些脚手架。同时，添加一个包含规则基本信息的 `meta` 对象。

```javascript
// enforce-foo-bar.js

module.exports = {
	meta: {
		// TODO: 添加元数据
	},
	create(context) {
		return {
			// TODO: 添加回调函数
		};
	},
};
```

#### 第 3 步：添加规则元数据（Step 3: Add Rule Metadata）

在编写规则之前，先向规则对象添加一些元数据。ESLint 在运行规则时会使用这些信息。

首先导出一个带有 `meta` 属性的对象，其中包含规则的元数据，例如规则类型、文档和可修复性。在这个例子中，规则类型是 "problem"，描述是 "Enforce that a variable named `foo` can only be assigned a value of 'bar'."，并且规则可以通过修改代码进行修复。

因为这条规则针对 JavaScript，我们还添加了 `languages: ["js/js"]`，声明它只适用于内置的 JavaScript 语言。如果这条规则对非 JavaScript 语言启用，ESLint 会抛出错误。

```javascript
// enforce-foo-bar.js

module.exports = {
	meta: {
		type: "problem",
		docs: {
			description:
				"Enforce that a variable named `foo` can only be assigned a value of 'bar'.",
		},
		fixable: "code",
		schema: [],
		languages: ["js/js"],
	},
	create(context) {
		return {
			// TODO: 添加回调函数
		};
	},
};
```

要了解更多关于规则元数据的信息，请参阅[规则结构（Rule Structure）](https://eslint.org/docs/latest/extend/custom-rules#rule-structure)。

#### 第 4 步：添加规则访问器方法（Step 4: Add Rule Visitor Methods）

定义规则的 `create` 函数，它接受一个 `context` 对象并返回一个对象，该对象为你想处理的每种语法节点类型各有一个属性。在这个例子中，你想处理 `VariableDeclarator` 节点。
你可以选择任何 [ESTree 节点类型](https://github.com/estree/estree)或[选择器](https://eslint.org/docs/latest/extend/selectors)。

::: tip
你可以使用 [Code Explorer](https://explorer.eslint.org) 查看任何 JavaScript 代码的 AST。这有助于确定你想要针对的节点类型。
:::

在 `VariableDeclarator` 访问器方法内部，检查节点是否代表一个 `const` 变量声明、其名称是否为 `foo`，以及它是否未被赋值为字符串 `"bar"`。你通过求值传递给 `VariableDeclaration` 方法的 `node` 来完成这些检查。

如果 `const foo` 声明被赋值为 `"bar"`，那么规则什么都不做。如果 `const foo` **没有**被赋值为 `"bar"`，那么 `context.report()` 会向 ESLint 报告一个错误。错误报告包含关于错误以及如何修复它的信息。

```javascript
// enforce-foo-bar.js
module.exports = {
    meta: {
        type: "problem",
        docs: {
            description: "Enforce that a variable named `foo` can only be assigned a value of 'bar'."
        },
        fixable: "code",
        schema: [],
        languages: ["js/js"]
    },
    create(context) {
        return {

            // 对每个变量声明符执行函数中的操作
            VariableDeclarator(node) {

                // 检查是否为 `const` 变量声明
                if (node.parent.kind === "const") {

                    // 检查变量名是否为 `foo`
                    if (node.id.type === "Identifier" && node.id.name === "foo") {

                        // 检查变量的值是否为 "bar"
                        if (node.init && node.init.type === "Literal" && node.init.value !== "bar") {

                            /*
                             * 向 ESLint 报告错误。错误消息使用
                             * 消息占位符将错误的值包含在错误消息中。
                             * 还包含一个 `fix(fixer)` 函数，将任何赋给
                             * `const foo` 的值替换为 "bar"。
                             */
                            context.report({
                                node,
                                message: 'Value other than "bar" assigned to `const foo`. Unexpected value: {{ notBar }}.',
                                data: {
                                    notBar: node.init.value
                                },
                                fix(fixer) {
                                    return fixer.replaceText(node.init, '"bar"');
                                }
                            });
                        }
                    }
                }
            }
        };
    }
};
```

#### 第 5 步：搭建测试（Step 5: Set up Testing）

规则编写完成后，你可以测试它，确保它按预期工作。

ESLint 提供了内置的 [`RuleTester`](https://eslint.org/docs/latest/integrate/nodejs-api#ruletester) 类来测试规则。你不需要使用第三方测试库来测试 ESLint 规则，但 `RuleTester` 可以与 Mocha 和 Jest 等工具无缝配合。

接下来，创建测试文件 `enforce-foo-bar.test.js`：

```shell
touch enforce-foo-bar.test.js
```

你将在测试文件中使用 `eslint` 包。将其安装为开发依赖：

```shell
npm install --save-dev eslint
```

并在你的 `package.json` 文件中添加一个测试脚本来运行测试：

```javascript
// package.json
{
    // ...其他配置
    "scripts": {
        "test": "node enforce-foo-bar.test.js"
    },
    // ...其他配置
}
```

#### 第 6 步：编写测试（Step 6: Write the Test）

要使用 `RuleTester` 编写测试，将该类和你的自定义规则导入 `enforce-foo-bar.test.js` 文件。

`RuleTester#run()` 方法针对有效和无效测试用例测试规则。如果规则未能通过任何测试场景，此方法会抛出错误。
`RuleTester` 要求至少存在一个有效和一个无效的测试场景。

```javascript
// enforce-foo-bar.test.js
const { RuleTester } = require("eslint");
const fooBarRule = require("./enforce-foo-bar");

const ruleTester = new RuleTester({
	// 必须至少使用 ecmaVersion 2015，
	// 因为 `const` 变量是在那时引入的。
	languageOptions: { ecmaVersion: 2015 },
});

// 如果 ruleTester.run() 中的测试未通过，则抛出错误
ruleTester.run(
	"enforce-foo-bar", // 规则名称
	fooBarRule, // 规则代码
	{
		// 检查
		// 'valid' 检查应该通过的用例
		valid: [
			{
				code: "const foo = 'bar';",
			},
		],
		// 'invalid' 检查不应通过的用例
		invalid: [
			{
				code: "const foo = 'baz';",
				output: 'const foo = "bar";',
				errors: 1,
			},
		],
	},
);

console.log("All tests passed!");
```

使用以下命令运行测试：

```shell
npm test
```

如果测试通过，你应该在控制台中看到以下内容：

```shell
All tests passed!
```

#### 第 7 步：将自定义规则打包进插件（Step 7: Bundle the Custom Rule in a Plugin）

现在你已经编写了自定义规则并验证了它可以工作，你可以将它包含在插件中。使用插件，你可以在 npm 包中分享该规则，以便在其他项目中使用。

创建插件文件：

```shell
touch eslint-plugin-example.js
```

现在编写插件代码。插件只是导出的 JavaScript 对象。要在插件中包含规则，将其包含在插件的 `rules` 对象中，该对象包含规则名称及其源代码的键值对。

要了解更多关于创建插件的信息，请参阅[创建插件（Create Plugins）](https://eslint.org/docs/latest/extend/plugins)。

```javascript
// eslint-plugin-example.js

const fooBarRule = require("./enforce-foo-bar");
const plugin = { rules: { "enforce-foo-bar": fooBarRule } };
module.exports = plugin;
```

#### 第 8 步：在本地使用插件（Step 8: Use the Plugin Locally）

你可以使用本地定义的插件在项目中执行自定义规则。要使用本地插件，在 ESLint 配置文件的 `plugins` 属性中指定插件的路径。

你可能想在以下场景之一中使用本地定义的插件：

- 你想在发布到 npm 之前测试插件。
- 你想使用一个插件，但不想将其发布到 npm。

在将插件添加到项目之前，先使用 [flat 配置文件](https://eslint.org/docs/latest/use/configure/configuration-files) `eslint.config.js` 为你的项目创建 ESLint 配置：

```shell
touch eslint.config.js
```

然后，将以下代码添加到 `eslint.config.js`：

```javascript
// eslint.config.js
"use strict";

// 导入 `defineConfig` 辅助函数
const { defineConfig } = require("eslint/config");
// 在本地导入 ESLint 插件
const eslintPluginExample = require("./eslint-plugin-example");

module.exports = defineConfig([
	{
		files: ["**/*.js"],
		languageOptions: {
			sourceType: "commonjs",
			ecmaVersion: "latest",
		},
		// 使用本地定义的 eslint-plugin-example 插件
		plugins: { example: eslintPluginExample },
		rules: {
			"example/enforce-foo-bar": "error",
		},
	},
]);
```

在测试规则之前，你必须创建一个用于测试规则的文件。

创建文件 `example.js`：

```shell
touch example.js
```

将以下代码添加到 `example.js`：

```javascript
// example.js

function correctFooBar() {
	const foo = "bar";
}

function incorrectFoo() {
	const foo = "baz"; // 问题！
}
```

现在你可以用本地定义的插件测试自定义规则了。

对 `example.js` 运行 ESLint：

```shell
npx eslint example.js
```

这会在终端中产生以下输出：

```text
/<path-to-directory>/eslint-custom-rule-example/example.js
  8:11  error  Value other than "bar" assigned to `const foo`. Unexpected value: baz  example/enforce-foo-bar

✖ 1 problem (1 error, 0 warnings)
  1 error and 0 warnings potentially fixable with the `--fix` option.
```

#### 第 9 步：发布插件（Step 9: Publish the Plugin）

要将包含规则的插件发布到 npm，你需要配置 `package.json`。在相应字段中添加以下内容：

1. `"name"`：包的唯一名称。npm 上不能有其他包使用相同的名称。
1. `"main"`：插件文件的相对路径。按照此示例，路径为 `"eslint-plugin-example.js"`。
1. `"description"`：在 npm 上可查看的包的描述。
1. `"peerDependencies"`：添加 `"eslint": ">=10.0.0"` 作为 peer dependency。使用该插件需要任何大于或等于该版本的版本。将 `eslint` 声明为 peer dependency 要求用户在插件之外单独将该包添加到项目中。
1. `"keywords"`：包含标准关键字 `["eslint", "eslintplugin", "eslint-plugin"]`，使包易于查找。你也可以添加任何其他可能与你的插件相关的关键字。

一个完整的、带注释的插件 `package.json` 文件示例：

```javascript
// package.json
{
  // npm 包名称。
  // 添加你自己的包名。eslint-plugin-example 已被占用！
  "name": "eslint-plugin-example",
  "version": "1.0.0",
  "description": "ESLint plugin for enforce-foo-bar rule.",
  "main": "eslint-plugin-example.js", // 插件入口点
  "scripts": {
    "test": "node enforce-foo-bar.test.js"
  },
  // 添加 eslint>=10.0.0 作为 peer dependency。
  "peerDependencies": {
    "eslint": ">=10.0.0"
  },
  // 添加这些标准关键字，让插件易于查找！
  "keywords": [
    "eslint",
    "eslintplugin",
    "eslint-plugin"
  ],
  "author": "",
  "license": "ISC",
  "devDependencies": {
    "eslint": "^10.0.0"
  }
}
```

要发布该包，运行 `npm publish` 并按照 CLI 提示操作。

你应该能看到该包已在 npm 上线！

#### 第 10 步：使用已发布的自定义规则（Step 10: Use the Published Custom Rule）

接下来，你可以使用已发布的插件。

在你的项目中运行以下命令下载该包：

```shell
# 在此处添加你的包名
npm install --save-dev eslint-plugin-example
```

更新 `eslint.config.js` 以使用打包版本的插件：

```javascript
// eslint.config.js
"use strict";

// 导入从 npm 下载的插件
const eslintPluginExample = require("eslint-plugin-example");

// ...其余配置
```

现在你可以测试自定义规则了。

使用下载的插件，对你在第 8 步中创建的 `example.js` 文件运行 ESLint：

```shell
npx eslint example.js
```

这会在终端中产生以下输出：

```text
/<path-to-directory>/eslint-custom-rule-example/example.js
  8:11  error  Value other than "bar" assigned to `const foo`. Unexpected value: baz  example/enforce-foo-bar

✖ 1 problem (1 error, 0 warnings)
  1 error and 0 warnings potentially fixable with the `--fix` option.
```

如上面的消息所示，你实际上可以使用 `--fix` 标志修复该问题，将变量赋值更正为 `"bar"`。

带 `--fix` 标志再次运行 ESLint：

```shell
npx eslint example.js --fix
```

运行时终端中没有错误输出，但你可以在 `example.js` 中看到修复已被应用。你应该看到以下内容：

```javascript
// example.js

// ...文件其余部分

function incorrectFoo() {
	const foo = "bar"; // 已修复！
}
```

#### 总结（Summary）

在本教程中，你创建了一个自定义规则，要求所有名为 `foo` 的 `const` 变量必须被赋值为字符串 `"bar"`，并建议将任何赋给 `const foo` 的其他值替换为 `"bar"`。你还将该规则添加到插件中，并在 npm 上发布了该插件。

通过这样做，你学到了以下实践，可以将它们应用于创建其他自定义规则和插件：

1. 创建自定义 ESLint 规则
1. 测试自定义规则
1. 将规则打包进插件
1. 发布插件
1. 使用插件中的规则

#### 查看教程代码（View the Tutorial Code）

你可以查看[本教程的带注释源代码](https://github.com/eslint/eslint/tree/main/docs/_examples/custom-rule-tutorial-code)。
