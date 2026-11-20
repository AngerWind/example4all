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
