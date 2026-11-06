通过create-vue创建项目(官方推荐), 底层实际上是使用vite来创建项目
~~~js
## 1.创建命令
##   npm create vue@latest就等效于 npx create-vue@latest, 等效于npm exec create-vue@latest
##   实际上他会**临时**下载create-vue@latest, 然后执行package.json中bin指定的脚本文件, 并传递参数
npm create vue@latest

## 2.具体配置
## 配置项目名称
√ Project name: vue3_test
## 是否添加TypeScript支持
√ Add TypeScript?  Yes
## 是否添加JSX支持
√ Add JSX Support?  No
## 是否添加路由环境
√ Add Vue Router for Single Page Application development?  No
## 是否添加pinia环境
√ Add Pinia for state management?  No
## 是否添加单元测试
√ Add Vitest for Unit Testing?  No
## 是否添加端到端测试方案
√ Add an End-to-End Testing Solution? » No
## 是否添加ESLint语法检查
√ Add ESLint for code quality?  Yes
## 是否添加Prettiert代码格式化
√ Add Prettier for code formatting?  No
~~~

直接使用vite来创建项目
```shell
# 等效于npm exec create-vite@latest my-vue-app -- --template vue
# my-vue-app是项目名称, --template vue是传递给package.json中的bin指定的脚本的参数
npm create vite@latest my-vue-app -- --template vue
cd my-vue-app
npm install
npm run dev
```

通过vue-cli创建项目, (不推荐, vue-cli已经进入维护模式)
~~~js
## 查看@vue/cli版本，确保@vue/cli版本在4.5.0以上
vue --version

## 安装或者升级你的@vue/cli 
npm install -g @vue/cli

## 执行创建命令
vue create vue_test

##  随后选择3.x
##  Choose a version of Vue.js that you want to start the project with (Use arrow keys)
##  > 3.x
##    2.x

## 启动
cd vue_test
npm run serve
~~~
