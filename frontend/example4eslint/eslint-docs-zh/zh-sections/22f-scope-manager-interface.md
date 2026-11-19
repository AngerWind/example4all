### 作用域管理器（ScopeManager）

> 原文链接：https://eslint.org/docs/latest/extend/scope-manager-interface

本文档基于 [eslint-scope](https://github.com/eslint/js/tree/main/packages/eslint-scope)（[escope](https://github.com/estools/escope) 的一个 fork）的实现编写，并废弃了一些 ESLint 未使用的成员。

---

#### ScopeManager 接口（ScopeManager interface）

`ScopeManager` 对象包含所有变量作用域。

##### 字段（Fields）

###### scopes

- **类型：** `Scope[]`
- **描述：** 所有作用域。

###### globalScope

- **类型：** `Scope`
- **描述：** 根作用域。

##### 方法（Methods）

###### acquire(node, inner = false)

- **参数：**
    - `node`（`ASTNode`）…… 要获取其作用域的 AST 节点。
    - `inner`（`boolean`）…… 如果节点有多个作用域，通常返回最外层的作用域。如果 `inner` 为 `true`，则返回最内层的作用域。默认为 `false`。
- **返回类型：** `Scope | null`
- **描述：** 获取给定 AST 节点的作用域。所获作用域的 `block` 属性即为该节点。此方法永远不会返回 `function-expression-name` 作用域。如果该节点没有自己的作用域，则返回 `null`。

###### addGlobals(names)

- **参数：**
    - `names`（`string[]`）…… 要添加到全局作用域的变量名。
- **返回类型：** `undefined`
- **描述：** 向全局作用域添加变量并解析对它们的引用。此方法由 ESLint 核心使用，绝不应在规则中使用。

###### getDeclaredVariables(node)

- **参数：**
    - `node`（`ASTNode`）…… 要获取其变量的 AST 节点。
- **返回类型：** `Variable[]`
- **描述：** 获取给定 AST 节点定义的变量。所获变量的 `def[].node`/`def[].parent` 属性即为该节点。如果该节点未定义任何变量，则返回空数组。

##### 已废弃成员（Deprecated members）

这些成员已定义但在 ESLint 中未使用。

###### isModule()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此程序是模块，则为 `true`。

###### isImpliedStrict()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此程序隐式处于严格模式，则为 `true`。即 `options.impliedStrict === true`。

###### isStrictModeSupported()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此程序支持严格模式，则为 `true`。即 `options.ecmaVersion >= 5`。

###### acquireAll(node)

- **参数：**
    - `node`（`ASTNode`）…… 要获取其作用域的 AST 节点。
- **返回类型：** `Scope[] | null`
- **描述：** 获取给定 AST 节点的作用域。所获作用域的 `block` 属性即为该节点。如果该节点没有自己的作用域，则返回 `null`。

---

#### Scope 接口（Scope interface）

`Scope` 对象包含作用域中的所有变量和引用。

##### 字段（Fields）

###### type

- **类型：** `string`
- **描述：** 此作用域的类型。为 `"block"`、`"catch"`、`"class"`、`"class-field-initializer"`、`"class-static-block"`、`"for"`、`"function"`、`"function-expression-name"`、`"global"`、`"module"`、`"switch"`、`"with"` 之一。

###### isStrict

- **类型：** `boolean`
- **描述：** 如果此作用域处于严格模式，则为 `true`。

###### upper

- **类型：** `Scope | null`
- **描述：** 父作用域。如果这是全局作用域，则此属性为 `null`。

###### childScopes

- **类型：** `Scope[]`
- **描述：** 子作用域数组。不包括孙级作用域。

###### variableScope

- **类型：** `Scope`
- **描述：** `type` 为 `"class-field-initializer"`、`"class-static-block"`、`"function"`、`"global"` 或 `"module"` 之一的最近祖先。对于上述作用域本身，这是自引用。

> 这表示最内层的封闭函数或顶层作用域。类字段初始化器和类静态块是隐式函数。历史上，这是承载由 `var` 声明定义的变量的作用域，因此得名 `variableScope`。

###### block

- **类型：** `ASTNode`
- **描述：** 创建此作用域的 AST 节点。

###### variables

- **类型：** `Variable[]`
- **描述：** 在此作用域上定义的所有变量的数组。不包括在子作用域中定义的变量。

###### set

- **类型：** `Map<string, Variable>`
- **描述：** 从变量名到变量对象的映射。

###### references

- **类型：** `Reference[]`
- **描述：** 此作用域上所有引用的数组。不包括子作用域中的引用。

###### through

- **类型：** `Reference[]`
- **描述：** 在此作用域中无法解析的引用数组。

###### functionExpressionScope

- **类型：** `boolean`
- **描述：** 如果此作用域是 `"function-expression-name"` 作用域，则为 `true`。

###### implicit

此字段仅存在于根 `Scope` 对象（全局作用域）上。它提供关于隐式全局变量的信息。隐式全局变量是指既非内置也非显式声明，而是在非严格代码中通过给未声明变量赋值而隐式创建的变量。这些变量的 `Variable` 对象不会出现在根 `Scope` 对象的 `variables` 和 `set` 字段中。

`implicit` 字段的值是一个有两个属性的对象。

**variables**

- **类型：** `Variable[]`
- **描述：** 所有隐式全局变量的数组。

**set**

- **类型：** `Map<string, Variable>`
- **描述：** 隐式全局变量从变量名到变量对象的映射。

::: tip
在表示隐式全局变量的 `Variable` 对象中，`references` 始终为空数组。你可以在根 `Scope` 对象（全局作用域）的 `through` 字段中，与其他未解析的引用一起找到对这些变量的引用。
:::

##### 已废弃成员（Deprecated members）

这些成员已定义但在 ESLint 中未使用。

###### taints

- **类型：** `Map<string, boolean>`
- **描述：** 从变量名到 `tainted` 标志的映射。

###### dynamic

- **类型：** `boolean`
- **描述：** 如果此作用域是动态的，则为 `true`。即此作用域的类型是 `"global"` 或 `"with"`。

###### directCallToEvalScope

- **类型：** `boolean`
- **描述：** 如果此作用域包含 `eval()` 调用，则为 `true`。

###### thisFound

- **类型：** `boolean`
- **描述：** 如果此作用域包含 `this`，则为 `true`。

###### resolve(node)

- **参数：**
    - `node`（`ASTNode`）…… 要获取其引用对象的 AST 节点。节点类型必须是 `"Identifier"`。
- **返回类型：** `Reference | null`
- **描述：** 返回 `this.references.find(r => r.identifier === node)`。

###### isStatic()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 返回 `!this.dynamic`。

###### isArgumentsMaterialized()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果这是一个使用了 `arguments` 变量的 `"function"` 作用域，则为 `true`。

###### isThisMaterialized()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 返回 `this.thisFound`。

###### isUsedName(name)

- **参数：**
    - `name`（`string`）…… 要检查的名称。
- **返回类型：** `boolean`
- **描述：** 如果给定名称用于变量名或引用名，则为 `true`。

---

#### Variable 接口（Variable interface）

`Variable` 对象是变量的信息。

##### 字段（Fields）

###### name

- **类型：** `string`
- **描述：** 此变量的名称。

###### scope

- **类型：** `Scope`
- **描述：** 定义此变量的作用域。

###### identifiers

- **类型：** `ASTNode[]`
- **描述：** 定义此变量的 `Identifier` 节点数组。如果此变量被重复声明，此数组包含两个或更多节点。

###### references

- **类型：** `Reference[]`
- **描述：** 此变量的引用数组。

###### defs

- **类型：** `Definition[]`
- **描述：** 此变量的定义数组。

##### 已废弃成员（Deprecated members）

这些成员已定义但在 ESLint 中未使用。

###### tainted

- **类型：** `boolean`
- **描述：** `tainted` 标志。（始终为 `false`）

###### stack

- **类型：** `boolean`
- **描述：** `stack` 标志。（不确定其含义。）

---

#### Reference 接口（Reference interface）

`Reference` 对象是引用的信息。

##### 字段（Fields）

###### identifier

- **类型：** `ASTNode`
- **描述：** 此引用的 `Identifier` 或 `JSXIdentifier` 节点。

###### from

- **类型：** `Scope`
- **描述：** 此引用所在的 `Scope` 对象。

###### resolved

- **类型：** `Variable | null`
- **描述：** 此引用指向的 `Variable` 对象。如果未定义这样的变量，则为 `null`。

###### writeExpr

- **类型：** `ASTNode | null`
- **描述：** 右侧的 ASTNode 对象。

###### init

- **类型：** `boolean`
- **描述：** 如果此写入引用是变量初始化器或默认值，则为 `true`。

##### 方法（Methods）

###### isWrite()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此引用是写入，则为 `true`。

###### isRead()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此引用是读取，则为 `true`。

###### isWriteOnly()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此引用是写入但不读取，则为 `true`。

###### isReadOnly()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此引用是读取但不写入，则为 `true`。

###### isReadWrite()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此引用既读取又写入，则为 `true`。

##### 已废弃成员（Deprecated members）

这些成员已定义但在 ESLint 中未使用。

###### tainted

- **类型：** `boolean`
- **描述：** `tainted` 标志。（始终为 `false`）

###### flag

- **类型：** `number`
- **描述：** `1` 表示读取，`2` 表示写入，`3` 表示读取/写入。

###### partial

- **类型：** `boolean`
- **描述：** `partial` 标志。

###### isStatic()

- **参数：**
- **返回类型：** `boolean`
- **描述：** 如果此引用是静态解析的，则为 `true`。

---

#### Definition 接口（Definition interface）

`Definition` 对象是变量定义的信息。

##### 字段（Fields）

###### type

- **类型：** `string`
- **描述：** 此定义的类型。为 `"CatchClause"`、`"ClassName"`、`"FunctionName"`、`"ImplicitGlobalVariable"`、`"ImportBinding"`、`"Parameter"` 和 `"Variable"` 之一。

###### name

- **类型：** `ASTNode`
- **描述：** 此定义的 `Identifier` 节点。

###### node

- **类型：** `ASTNode`
- **描述：** 名称的封闭节点。

| type                       | node                                                                       |
| :------------------------- | :------------------------------------------------------------------------- |
| `"CatchClause"`            | `CatchClause`                                                              |
| `"ClassName"`              | `ClassDeclaration` 或 `ClassExpression`                                    |
| `"FunctionName"`           | `FunctionDeclaration` 或 `FunctionExpression`                              |
| `"ImplicitGlobalVariable"` | `AssignmentExpression` 或 `ForInStatement` 或 `ForOfStatement`             |
| `"ImportBinding"`          | `ImportSpecifier`、`ImportDefaultSpecifier` 或 `ImportNamespaceSpecifier`  |
| `"Parameter"`              | `FunctionDeclaration`、`FunctionExpression` 或 `ArrowFunctionExpression`   |
| `"Variable"`               | `VariableDeclarator`                                                       |

###### parent

- **类型：** `ASTNode | undefined | null`
- **描述：** 名称的封闭语句节点。

| type                       | parent                |
| :------------------------- | :-------------------- |
| `"CatchClause"`            | `null`                |
| `"ClassName"`              | `null`                |
| `"FunctionName"`           | `null`                |
| `"ImplicitGlobalVariable"` | `null`                |
| `"ImportBinding"`          | `ImportDeclaration`   |
| `"Parameter"`              | `null`                |
| `"Variable"`               | `VariableDeclaration` |

##### 已废弃成员（Deprecated members）

这些成员已定义但在 ESLint 中未使用。

###### index

- **类型：** `number | undefined | null`
- **描述：** 在声明语句中的索引。

###### kind

- **类型：** `string | undefined | null`
- **描述：** 声明语句的种类。
