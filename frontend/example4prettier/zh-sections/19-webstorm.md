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
