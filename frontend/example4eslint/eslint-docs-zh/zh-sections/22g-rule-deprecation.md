### 规则废弃（Rule Deprecation）

> 原文链接：https://eslint.org/docs/latest/extend/rule-deprecation

规则废弃元数据描述一条规则是否已废弃，以及在有替代规则时如何替代它。
旧格式使用两个顶层的[规则 meta](https://eslint.org/docs/latest/extend/custom-rules#rule-structure) 属性 `deprecated`（仅为布尔值）和 `replacedBy`。
在新格式中，`deprecated` 是一个布尔值或 `DeprecatedInfo` 类型的对象，而 `replacedBy` 应定义在 `deprecated` 内部，而不是顶层。

#### ◆ DeprecatedInfo 类型（◆ DeprecatedInfo type）

此类型表示规则废弃的一般信息。
每个属性都是可选的。

- `message`（`string`）<br>
  可展示给用户的一般性消息。可以包含此规则被废弃的原因或如何替代该规则。
- `url`（`string`）<br>
  包含关于此规则废弃更多信息的 URL。
- `replacedBy`（`ReplacedByInfo[]`）<br>
  关于该规则可用替代方案的信息。
  可以是空数组，以明确表示没有替代方案。
- `deprecatedSince`（`string`）<br>
  废弃该规则的版本的 [Semver](https://semver.org/)。应为表示特定版本的完整 semver 字符串，不带前导 `"v"`，例如 `"3.12.0"`。
- `availableUntil`（`string | null`）<br>
  可能移除该规则的版本的 [Semver](https://semver.org/)，例如下一个主版本。应为表示特定版本的完整 semver 字符串，不带前导 `"v"`，例如 `"4.0.0"`。
  特殊值 `null` 表示该规则不再变更，但会继续保留可用。

#### ◆ ReplacedByInfo 类型（◆ ReplacedByInfo type）

此类型描述一条规则的单个可能的替代方案。
每个属性都是可选的。

- `message`（`string`）<br>
  关于此规则替代方案的一般性消息。
- `url`（`string`）<br>
  包含关于此规则替代方案更多信息的 URL。
- `plugin`（`ExternalSpecifier`）<br>
  指定哪个插件拥有替代规则。
  名称应为包名；如果替代规则是 ESLint 核心规则，则应为 "eslint"。
  如果替代规则在同一个插件中，则应省略此属性。
- `rule`（`ExternalSpecifier`）<br>
  指定替代规则。
  如果插件只包含一条规则，或与该规则同名，则可以省略。

##### ◆ ExternalSpecifier 类型（◆ ExternalSpecifier type）

此类型表示一个外部资源。
每个属性都是可选的。

- `name`（`string`）<br>
  对于 `plugin` 是包名，对于 `rule` 是规则 ID。
- `url`（`string`）<br>
  指向插件/规则文档的 URL。
