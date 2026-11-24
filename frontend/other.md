## .editorconfig的作用

`.editorconfig` 是一个用来统一代码编辑器风格配置的文件。它的作用是让不同开发者、不同编辑器/IDE
  在打开同一个项目时，自动遵守同一套基础格式规则，而不依赖各自的编辑器默认设置。

这个文件是`ini`格式的, 典型的配置如下

~~~ini
root = true

[*]
indent_style = space        # 用空格缩进
indent_size = 2             # 缩进宽度 2
tab_width = 2               # tab的宽度为2
end_of_line = lf            # 换行符用 LF
charset = utf-8             # 文件编码 UTF-8
trim_trailing_whitespace = true   # 删除行尾空格
insert_final_newline = true       # 文件末尾保留一个空行

[*.{js,json}]
indent_size = 2             # js/json 文件缩进用 2

[*.md]
trim_trailing_whitespace = false  # markdown 保留行尾空格
~~~

另外, 如果你的项目中有这个配置文件的话, 并且有Prettier的话, 那么Prettier 会自动读取 `.editorconfig` 的部分设置作为兜底。



编辑器在格式化代码的时候, 会从这个文件所在目录开始，逐级向上（父目录、祖父目录……直到磁盘根目录）查找所有 .editorconfig
  文件，然后把找到的规则合并：

  - 不同的子文件夹可以有不同的`.editorconfig`配置
  - 离文件越近的` .editorconfig` 优先级越高；
  - 但如果上层目录里还有` .editorconfig`，它们的规则也会合并进来（补充没被覆盖的项）。

一般会在项目的根目录中的`.editorconfig`中添加`root = true`, 作用是告诉编辑器: 不要再像上查找`.editorconfig`文件了, 到这里就终止吧, 没有写的规则就使用编辑器默认的



## commitlint + husky 的用法

commitlint 是一个校验 git 提交信息(commit message)格式的工具, 用来强制团队遵守统一的提交规范, 通常配合的是 [Conventional Commits](https://www.conventionalcommits.org/zh-hans/) 规范, 要求提交信息长这样:

~~~txt
type(scope): subject

例如:
feat(user): 添加手机号登录
fix: 修复订单金额计算错误
docs: 更新 README
~~~

常见的 type 有: `feat`(新功能)、`fix`(修bug)、`docs`(文档)、`style`(格式)、`refactor`(重构)、`perf`(性能)、`test`(测试)、`chore`(杂项)

### 使用步骤

1. 安装 commitlint 和它的规则集

   ~~~shell
   npm install --save-dev @commitlint/cli @commitlint/config-conventional
   ~~~

   - `@commitlint/cli`: commitlint 的命令行工具本体
   - `@commitlint/config-conventional`: Conventional Commits 的规则集, 定义了 type 枚举、subject 不能为空等规则

2. 添加 commitlint 的配置文件 `commitlint.config.js`

   ~~~js
   export default {
     extends: ['@commitlint/config-conventional'],
   };
   ~~~

   也可以在这里覆盖规则, 比如:

   ~~~js
   export default {
     extends: ['@commitlint/config-conventional'],
     rules: {
       'type-enum': [2, 'always', ['feat', 'fix', 'docs', 'style', 'refactor', 'perf', 'test', 'chore']],
       'subject-max-length': [2, 'always', 72],
     },
   };
   ~~~

   规则等级: `0`=关闭, `1`=警告(不阻塞), `2`=错误(阻塞提交)

3. 安装 husky, 并激活 git hooks

   ~~~shell
   npm install --save-dev husky
   npx husky init
   ~~~

   `husky init` 会创建 `.husky` 目录, 并在 package.json 中添加 `"prepare": "husky"` 脚本

4. 添加 `commit-msg` 钩子, 让 husky 在提交时调用 commitlint

   ~~~shell
   echo 'npx --no -- commitlint --edit $1' > .husky/commit-msg
   ~~~

   也可以直接手动创建 `.husky/commit-msg` 文件, 内容就一行:

   ~~~shell
   npx --no -- commitlint --edit $1
   ~~~

5. 验证效果

   ~~~shell
   git commit -m "随便写点什么"   # ❌ 被拦截, 报 type 不能为空
   git commit -m "fix: 修复xxx"   # ✅ 通过
   ~~~

### 原理

1. git 本身提供了一系列的钩子(hooks), 比如 `pre-commit`(提交前)、`commit-msg`(提交信息写入时)、`pre-push`(推送前), 这些钩子本质是 `.git/hooks/` 目录下的脚本文件, git 在对应时机执行它们, **脚本退出码非零时, git 就中止这次操作**

2. husky 的作用就是接管这些钩子: 安装时通过 `git config core.hooksPath .husky` 把钩子目录从 `.git/hooks` 指向项目内的 `.husky` 目录, 这样钩子脚本就可以被 git 版本控制、随项目共享给团队所有成员

   别人 clone 项目后执行 `npm install`, 会触发 package.json 中的 `prepare` 生命周期脚本, 自动执行 `husky` 命令重新设置 `core.hooksPath`, 完成本地激活

3. `commit-msg` 钩子的特点: git 在调用它时, 会把**存放本次提交信息的临时文件路径**作为第一个参数(`$1`)传进来。所以钩子里的 `commitlint --edit $1` 就是读取这个临时文件中的提交信息, 然后用配置文件里的规则逐条校验

4. 校验失败时 commitlint 退出码非零 → git 中止提交 → 终端打印出违反了哪些规则(比如 `subject may not be empty`、`type may not be empty`), 开发者改好提交信息再重新提交

### 为什么要规范化提交信息

1. 提交历史可读, 一眼看出每次提交是功能还是修复
2. 是自动化的原料: `semantic-release`、`standard-version` 等工具可以根据 `feat`/`fix`/`BREAKING CHANGE` 自动计算版本号、自动发版、自动生成 CHANGELOG
3. 靠工具拦截, 而不是靠口头约定, 新人也不会写歪

### 配套的交互式工具(可选)

如果担心团队成员记不住 type 枚举, 可以用 commitizen 提供交互式引导, 把写提交信息变成做选择题:

~~~shell
npm install --save-dev commitizen cz-conventional-changelog
npx git-cz   # 代替 git commit, 通过交互问答生成规范的提交信息
~~~

