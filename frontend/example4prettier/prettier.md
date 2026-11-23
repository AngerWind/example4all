# Prettier的理念

Prettier是一个代码格式化工具, 他和eslint在理念上有着非常的不同

- 在eslint中, 他是由非常多的规则进行组成的, 你可以对不同的规则进行开关, 也可以定制他们的告警等级, 这样就可以拥有非常定制化的配置了

- 但是在prettier中, 他的理念是尽量少的, 或者不提供配置(少量的配置也只是因为历史原因),  你使用了Prettier, 那么就使用了一种固定的代码格式, 没有什么可以配置的

  他这样做的目标是为了结束代码格式的争论, 有的人喜欢双引号字符串, 有的喜欢单引号, 这些都是非常定制化的东西, 但是在使用Prettier之后, 这些就没什么好争论的了, 一切按照Prettier的来就好了, 也没什么可以配置的

具体的文档可以查看https://prettier.io/docs/option-philosophy



# 安装

首先在项目中安装Prettier

~~~shell
npm install --save-dev --save-exact prettier@3.6.2
yarn add --dev --exact prettier@3.6.2
pnpm add --save-dev --save-exact prettier@3.6.2
bun add --dev --exact prettier@3.6.2
deno install --dev --save-exact prettier@3.6.2
~~~

> 注意, prettier每个版本的代码风格都有可能有细微的差异, 所以在install的时候, 不要用latest来进行安装, 这样你不知道安装的是什么版本
>
> 并且在安装的时候添加`--save-exact`项目, 这个选项是在生成`devDependencies`的时候, 不要生成`^3.6.2`这样的版本号, 这样的版本号是浮动的, 这会导致团队的每个成员的版本号都不一样, 代码格式也不一样, 而是应该使用`3.6.2`这样精准的版本号



然后创建一个Prettier的配置文件`.prettierrc`文件, 这样编辑器和其他的工具就知道正在使用Prettier了

~~~shell
# 通过node执行代码来生成文件
node --eval "fs.writeFileSync('.prettierrc','{}\n')"
~~~

然后再创建一个`.prettierignore`文件, 这样可以告诉Prettier, 哪些是不需要格式化的文件

~~~shell
node --eval "fs.writeFileSync('.prettierignore','# Ignore artifacts:\nbuild\ncoverage\n')"
~~~

安装prettier之后, 你可以使用如下的命令来执行prettier来进行代码格式化

~~~shell
npx prettier . --write # 格式化所有文件
yarn exec prettier . --write
pnpm exec prettier . --write
bunx prettier . --write
deno x prettier . --write

prettier --write app/ # 指定目录
prettier --write app/components/Button.js # 指定特定的文件
prettier --write "app/**/*.test.js" # 使用通配符匹配文件
~~~



你也可以将prettier集成到CICD中, 比如git action

~~~yaml
name: Code Format Check
on:
  push:
  pull_request:
jobs:
  prettier:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with:
          node-version: 22
      - run: npm ci
      - run: npx prettier . --check
~~~

`npx prettier . --check` 这个命令会检查代码的格式是否有问题, 而不是重新格式化代码

如果代码格式有问题的话, 那么这个命令的退出状态码非零





# 配置

## 配置文件

你可以通过以下方式配置 Prettier（**按照优先级从高到低排列**）：

1. 在 `package.json` 文件或 [`package.yaml`](https://github.com/pnpm/pnpm/pull/1799?utm_source=chatgpt.com) 文件中配置一个 `"prettier"` 键。

2. 使用 JSON 或 YAML 格式编写 `.prettierrc` 文件。

3. 使用以下文件之一进行配置：

   - `.prettierrc.json`
   - `.prettierrc.yml`
   - `.prettierrc.yaml`
   - `.prettierrc.json5`

4. 使用以下 JavaScript 或 TypeScript 文件进行配置：

   - `.prettierrc.js`
   - `prettier.config.js`
   - `.prettierrc.ts`
   - `prettier.config.ts`

   这些文件需要导出一个对象，可以使用 `export default` 或 `module.exports`，具体取决于 `package.json` 中的 `type` 字段。

5. 使用以下 ES Module 格式的文件进行配置：

   - `.prettierrc.mjs`
   - `prettier.config.mjs`
   - `.prettierrc.mts`
   - `prettier.config.mts`

   这些文件使用 `export default` 导出一个配置对象。

6. 使用以下 CommonJS 格式的文件进行配置：

   - `.prettierrc.cjs`
   - `prettier.config.cjs`
   - `.prettierrc.cts`
   - `prettier.config.cts`

   这些文件使用 `module.exports` 导出一个配置对象。

7. 使用 `.prettierrc.toml` 文件进行配置。



> prettier会从要格式化的文件的位置开始, 向上查找配置文件, 直到找到, 所以**不同的子目录下可以有不同的配置文件**

> TypeScript 支持需要 Node.js 版本 22.6.0，而 Node.js 版本 24.3.0 之前需要使用 `--experimental-strip-types` 参数才能运行 Node.js。
>
> ```shell
> node --experimental-strip-types node_modules/prettier/bin/prettier.cjs . --write
> ```
>
> 或者
>
> ```shell
> NODE_OPTIONS="--experimental-strip-types" prettier . --write
> ```





## 基本配置

你可以使用一下的格式来对prettier进行配置

1. .prettierrc.json或者.prettierrc

   ~~~json
   {
     // 这里是全局配置, 针对所有文件
     "trailingComma": "es5",
     "tabWidth": 4,
     "semi": false,
     "singleQuote": true,
     // 这里是对特定的文件进行单独配置
     "overrides": [
       { 
         
         "files": ["**/*/*.js", "dist/**/*"], // 指定特定的文件
         "excludeFiles":["src/**/*.html", "dist/**/*.html"] // 指定要忽略的文件
         "options": {  // 指定这些文件应该使用的配置
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
   ~~~

2. prettier.config.mjs, .prettierrc.mjs, prettier.config.js, or .prettierrc.js

   ~~~js
   const config = {
     trailingComma: "es5",
     tabWidth: 4,
     semi: false,
     singleQuote: true,
   };
   
   export default config;
   ~~~

3. prettier.config.cjs、.prettierrc.cjs、prettier.config.js 或 .prettierrc.js

   ~~~js
   const config = {
     trailingComma: "es5",
     tabWidth: 4,
     semi: false,
     singleQuote: true,
   };
   
   module.exports = config;
   ~~~

4. prettier.config.mts、.prettierrc.mts、prettier.config.ts 或 .prettierrc.ts


   ~~~ts
   import { type Config } from "prettier";
   
   const config: Config = {
     trailingComma: "none",
   };
   
   export default config;
   ~~~

5. prettier.config.cts、.prettierrc.cts、prettier.config.ts 或 .prettierrc.ts

   ~~~ts
   import { type Config } from "prettier";
   
   const config: Config = {
     trailingComma: "none",
   };
   
   module.exports = config;
   ~~~

6. .prettierrc、.prettierrc.yml 或 .prettierrc.yaml

   ~~~yaml
   trailingComma: "es5"
   tabWidth: 4
   semi: false
   singleQuote: true
   ~~~

7. .prettierrc.toml

   ~~~toml
   trailingComma = "es5"
   tabWidth = 4
   semi = false
   singleQuote = true
   ~~~

   

## 可共享配置

可共享配置就是将你的Prettier配置, 打包到一个npm包中, 然后别人只要install你的包, 然后就可以直接使用你的配置来配置Prettier了

1. 我们创建一个新的npm包, 名为`@username/prettier-config`

   这个包的结构如下

   ~~~txt
   prettier-config/
   ├── index.js
   └── package.json
   ~~~

   package.sjon的内容如下:

   ~~~json
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
       "prettier": "3.10.1"
     }
   }
   ~~~

2. 之后编写index.js,  这个文件的主要内容是导出一个Prettier配置对象

   ~~~js
   const config = {
     trailingComma: "es5",
     tabWidth: 4,
     singleQuote: true,
   };
   
   export default config;
   ~~~

3. 将这个包发布到npm上面

   ~~~shell
   npm publish
   ~~~

4. 要使用这个包, 执行如下的命令

   ~~~shell
   npm install --save-dev @username/prettier-config
   ~~~

5. 在配置文件中使用这个npm包中的Prettier配置

   ~~~json
   import usernamePrettierConfig from "@username/prettier-config";
   
   const config = {
     ...usernamePrettierConfig, // 获取配置对象, 然后扩展出所有的属性
     semi: false, // 这里可以覆盖npm包中的配置, 进行自己的定制化
   };
   
   export default config;
   ~~~





## 所有配置选项

1. 行的宽度, 超过宽度换行

   ~~~js
   const config = {
     printWidth: 80 // 默认值80
   };
   export default config;
   ~~~

2. 每个缩进的空格的个数

   ~~~js
   const config = {
     tabWidth: 2 // 默认值2
   };
   export default config;
   ~~~

3. 使用tab, 而不是空格缩进

   ~~~js
   const config = {
     useTabs: false // 默认值false
   };
   export default config;
   ~~~

   

# 忽略文件

你可以在项目的根目录下面创建一个`.prettierignore`文件, 告诉prettier哪些文件是不需要格式化的

如果项目中已经存在 `.eslintignore`, 你可以将`.eslintignore`文件中的内容直接复制到`.prettierignore`中, 这样不需要从0开始配置这个文件, 而且他们的格式都是一样的, 这样ESLint 和 Prettier 就可以使用相同的文件忽略范围。

~~~.prettierignore
# Ignore artifacts:
build
coverage

# Ignore all HTML files:
**/*.html
~~~



默认的情况下, prettier会自动的忽略如下文件

~~~txt
**/.git
**/.svn
**/.hg
**/node_modules
~~~



如果你的项目根目录下面有`.gitignore`文件, 那么这个文件中指定的文件, prettier也不会格式化



## 内联注释

如果你不想在`.prettierignore`中添加全局的文件忽略, 只想要忽略特定文件中的部分内容, 那么你可以使用注释来指定忽略

1. js

   ~~~js
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
   ~~~

   格式话之后变成

   ~~~js
   matrix(1, 0, 0, 0, 1, 0, 0, 0, 1);
   
   // prettier-ignore
   matrix(
     1, 0, 0,
     0, 1, 0,
     0, 0, 1,
   );
   ~~~

2. jsx

   ~~~jsx
   <div>
     {/* prettier-ignore */}
     <span     ugly  format=''   />
   </div>
   ~~~

3. html

   ~~~xml
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
   ~~~

4. css

   ~~~css
   /* prettier-ignore */
   .my    ugly rule
   {
   
   }
   ~~~

5. markdown

   ~~~xml
   <!-- prettier-ignore -->
   Do   not    format   this
   ~~~

   markdown也支持范围忽略, 这个功能只要是用来忽略自动生成的内容的, 比如说toc

   ~~~markdown
   <!-- prettier-ignore-start -->
   <!-- SOMETHING AUTO-GENERATED BY TOOLS - START -->
   
   | MY | AWESOME | AUTO-GENERATED | TABLE |
   |-|-|-|-|
   | a | b | c | d |
   
   <!-- SOMETHING AUTO-GENERATED BY TOOLS - END -->
   <!-- prettier-ignore-end -->
   ~~~

6. yaml

   ~~~yaml
   # prettier-ignore
   key  : value
   ~~~



# Prettier和ESLinter

Linter的翻译是代码检查器, 常见的比如ESLint, 他有两个大类的功能

- 代码风格的格式化, 比如最大长度, 禁止tab等等
- 代码质量检查, 比如禁止未使用的变量, 不使用隐式的全局变量等等

而Prettier的作用是进行代码风格的格式化, 相当于ESLint和Prettier他们的功能是有交集的, 所以在这里推荐:

<font color=red>使用Prettier做代码格式化, 使用eslint做代码质量检查, 然后使用[eslint-config-prettier](https://github.com/prettier/eslint-config-prettier)来关闭eslint中可能会与prettier冲突的规则, eslint只专心做代码格式化, 这个插件是prettier官方写的</font>



安装eslint, 并添加eslint的配置文件

```shell
npm init @eslint/config@latest
```



安装prettier, eslint-config-prettier

~~~shell
npm install -D prettier eslint-config-prettier
~~~

添加`.prettierrc, .prettierignore`文件



在`eslint.config.mjs`文件中添加配置

~~~js
import js from '@eslint/js';
import globals from 'globals';
import prettier from 'eslint-config-prettier';

export default [
  // 忽略不需要检查的目录
  {
    ignores: ['node_modules/**', 'dist/**', 'build/**', 'coverage/**'],
  },

  // ESLint 官方推荐规则
  js.configs.recommended,

  // JavaScript 项目的基础配置
  {
    files: ['**/*.js', '**/*.mjs'],
    languageOptions: {
      ecmaVersion: 'latest',
      sourceType: 'module',
      globals: {
        ...globals.node,
        ...globals.browser,
      },
    },
  },

  // 必须放在其他规则配置之后
  prettier,
];
~~~

这里最重要的是最后这一项：

```
prettier
```

它实际上就是 `eslint-config-prettier` 导出的配置，会关闭与 Prettier 冲突的 ESLint 格式规则。

他必须放在最后面, 因为 ESLint 配置是按顺序合并的。把它放在最后，才能覆盖前面其他配置中可能启用的格式规则。



# Prettier和Git 



如果你想要在git commit之前, 运行eslint和prettier, 那么可以按照如下的内容来

1. 首先确保你已经正确的安装了prettier, 并且将其添加到了`devDependencies`中

2. 执行如下命令

   ~~~shell
   # 这个命令是执行mrm的2版本, 然后传入了一个lint-staged的命令
   npx mrm@2 lint-staged
   ~~~

3. 上面的命令执行之后会有如下的改动

   1. 会帮你自动安装`husky`和`lint-staged`, 并配置好git的`pre-commit`

      > 注意查看devDependencies中有没有husky和lint-staged, 如果没有的话要自己安装一下, 正常情况下是会有的
   
   2. 会生成一个`.husky`的文件夹, 其中有一个`pre-commit`的文件
   
      ![image-20261122173247556](img/prettier/image-20261122173247556.png)
   
   3. 在package.json中添加如下的内容
   
      ![image-20261122173321863](img/prettier/image-20261122173321863.png)



husky和lint-staged的原理在于

1. 安装的时候, 会执行`git config core.hooksPath`命令, 将hook的地址指向`.husky`目录, 这样在`git commit`的时候就可以调用到husky中的hook了, 并且这个`.husky`目录还是被git版本控制的, 可以被共享

2. 当别人拿到你的项目的时候, 会执行`npm install`这个命令来安装依赖, 那么就会触发package.json中的`prepare`这个命令, 这是npm的一个生命周期的hook, 在`npm install`之后执行

   这个`prepare`命令会执行`husky install`命令, 其实也就是执行`git config core.hooksPath`命令, 将hook的地址指向`.husky`目录, 这样别人的项目中, git的hook也被husky接管了

3. 然后每次git commit之前, 都会执行pre-commit这个hook, 然后执行其中的`npx lint-staged`这个命令

   这个命令会读取package.json中的lint-staged配置对象

   ~~~txt
     "lint-staged": {
       # 获取暂存区中的文件, 过滤出js文件, 然后执行eslit
       "*.js": "eslint --cache --fix", 
       # 获取暂存区中的文件, 过滤出js,css,md文件, 然后执行eslit
       "*.{js,css,md}": "prettier --write" 
     }
   ~~~

   命令的执行时从上到下的, 所以js文件先被eslint, 然后prettier格式化

   最后husky会将修改后的文件重新调用`git add`添加回暂存区, 然后再执行`git commit`





# API

你可以在代码中调用prettier

Prettier 提供了 JavaScript API，因此你不仅可以通过命令行 `npx prettier` 格式化代码，还可以在自己的 Node.js 程序中直接调用 Prettier。

下面给你一个完整案例：读取 JavaScript 文件，调用 Prettier 格式化，然后将格式化结果写回文件。

1. 安装 Prettier

   ~~~shell
   npm install prettier
   ~~~

2. 编写调用代码

   创建 `format.js`：

   ```js
   import * as prettier from 'prettier';
   import fs from 'node:fs/promises';
   
   async function formatFile(filePath) {
     // 1. 读取文件内容
     const source = await fs.readFile(filePath, 'utf-8');
   
     // 2. 获取 Prettier 配置
     const options = await prettier.resolveConfig(filePath);
   
     // 3. 调用 Prettier 格式化
     const formatted = await prettier.format(source, {
       ...options,
       filepath: filePath,
     });
   
     // 4. 将格式化结果写回文件
     await fs.writeFile(filePath, formatted, 'utf-8');
   
     console.log(`${filePath} 格式化完成`);
   }
   
   formatFile('./example.js');
   ```

3. 准备一个待格式化文件

      `example.js`：

      ```js
      const user={name:"Tom",age:18}
      function sayHello(user){console.log("Hello,"+user.name)}
      sayHello(user)
      ```

4. 执行：

      ```shell
      node format.js
      ```

      格式化后：

      ```js
      const user = { name: 'Tom', age: 18 };
      function sayHello(user) {
        console.log('Hello,' + user.name);
      }
      sayHello(user);
      ```

      

# Prettier和Git Action

你可以通过git action将prettier配置到CI/CD中, 详情查看https://prettier.io/docs/ci



# Prettier集成IDE

## vscode

vscode中有prettier对应的插件https://marketplace.visualstudio.com/items?itemName=esbenp.prettier-vscode

### 默认的解析器

1. 你可以将prettier设置为vscode的默认格式化器

   ~~~json
   {
     "editor.defaultFormatter": "esbenp.prettier-vscode"
   }
   ~~~

2. 你也可以为单独的语言设置prettier作为格式化器

   ~~~json
   {
     "editor.defaultFormatter": "<another formatter>",
     "[javascript]": {
       "editor.defaultFormatter": "esbenp.prettier-vscode"
     },
     "[typescript]": {
       "editor.defaultFormatter": "esbenp.prettier-vscode"
     }
   }
   ~~~

   

### 插件使用的prettier

默认情况下, vscode会使用项目中的node_modules中的prettier来进行格式化

如果当前项目中没有的话, 并且设置了`prettier.resolveGlobalModules` 为 `true` 时, 还会查找全局的prettier来格式化代码

否则的话使用插件自带的prettier3.x来进行格式化代码



### 插使用的配置文件

插件默认使用项目中的prettier配置文件来作为配置, 但是你也可以直接通过 [prettier.configPath](https://github.com/prettier/prettier-vscode#prettierconfigpath)来指定配置文件的位置, 然后忽略项目中的配置文件



## webstorm

1. 现在webstorm上面安装prettier插件

   ![image-20261122192910081](img/prettier/image-20261122192910081.png)

2. 确保项目已经安装了prettier

3. 配置prettier

![image-20261122192820610](img/prettier/image-20261122192820610.png)

webstorm可以自动识别项目中的prettier的如下配置文件

- **.prettierrc.json**, **.prettierrc.yml**, **.prettierrc.yaml**, or **.prettierrc.json5**
- **.prettierrc.js**, **prettier.config.js**, **.prettierrc.mjs**, **prettier.config.mjs**, **.prettierrc.cjs**, **prettier.config.cjs**, **.prettierrc.toml**
- **.prettierrc.ts**, **prettier.config.ts**, **.prettierrc.mts**, **prettier.config.mts**, **.prettierrc.cts**, **prettier.config.cts**

然后也会识别`.prettierignore`配置文件

在格式化的时候, webstorm会使用项目中的node_modules中的prettier来进行格式化