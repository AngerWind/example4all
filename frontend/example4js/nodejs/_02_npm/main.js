/**
 * 1. 先使用npm init对文件夹进行模块初始化
 *      在初始化的过程中, npm会对当前模块的一些信息进行询问(名字, 版本, 作者, 描述, github地址, 开源协议, 程序入口)  
 *      初始化完成之后会在当前文件夹下面生成package.json文件, 记录上面询问的东西
 *                
 * 2. 使用npm i md5下载md5包
 *      下载完成之后, 在当前文件夹下面出现一个node_modules的文件夹, 这个文件夹保存着下载下来的包
 *      同时package.json也出现了dependencies信息, 记录着当前模块所依赖的第三方模块和版本
 * 
 * 3. npm config ls 查看全局缓存路径, 全局安装路径
 * 
 * 
 * npm相关命令:
 *      npm init
 *      npm install/uninstall/update 模块[@版本号]  [-g], 安装/删除/更新模块
 *          不加-g只操作当前目录下的模块, 添加-g操作全局中的模块, 使用全局安装的模块不会出现在package.json中
 *          install选项等效i选项
 *      npm install 模块[@版本号] [--save-dev]
 *          --save-dev表示当前安装的模块是dev环境下使用的, 模块将在package.json的devDependencies中出现
 *      npm list [-g],  不加-g列举当前目录下的模块, 添加-g列举全局目录下的模块
 *      npm info 模块, 列举模块的详细信息
 *      npm info 模块 version, 获取模块的最新版本
 *      npm outdated, 检测模块是否过时
 * 
 *      npm install [-g], 按照当前目录下的package.json安装依赖到当前[全局]目录下
 */

/**
 * 在nodejs中, 每个模块都有版本号, 一般是三个数字   主版本号.副版本号.补丁号(majro.minor.patch)
 * 
 * 范围指定: 使用< <= > >=
 *      >=1.2.7 <1.3 表示只能使用大于等于1.2.7小于1.3.0的版本
 *      =1.2.7, 1.2.7  表示只能使用1.2.7版本
 * 横杆: 
 *      1.2.3 - 2.3.4 表示 >=1.2.3 <=2.3.4
 *      1.2 - 2.3.4 表示 >=1.2.0 <=2.3.4
 *      1.2.3 - 2.3 表示 >=1.2.3 <2.4.0
 *      1.2.3 - 2 表示 >=1.2.3 <3.0.0
 * 波浪号:
 *      如果minor指定了, 那么只能修改patch, 如果没有指定minor那么可以修改minor
 *      ~1.2.3 表示 >=1.2.3 <1.3.0
 *      ~1.2 表示 >=1.2.0 <1.3.0
 *      ~0.2 表示 >=0.2.0 <0.3.0
 *      ~1 表示 >=1.0.0 <2.0.0
 *      ~0表示 >=0.0.0 <1.0.0
 * 尖括号:
 *      表示固定左侧第一个非零数字
 *      ^1.2.3 := >=1.2.3 <2.0.0
 *      ^0.2.3 := >=0.2.3 <0.3.0
 *      ^0.0.3 := >=0.0.3 <0.0.4
 */

/**
 * npm修改镜像仓库
 *      1. 手动切换为淘宝镜像
 *          npm config set registry http://registry.npm.taobao.org
 *      2. 通过nrm工具切换
 *          - 全局安装nrm:   npm install -g nrm
 *          - 查看nrm版本:   nrm -V
 *          - 查看当前有哪些可用的镜像:  nrm ls
 *          - 自动切换的仓库: nrm use 镜像名
 *          - 获取当前使用的镜像地址:  npm config get registry
 *      3. 使用cnpm安装第三方模块, cnpm的区别在于cnpm默认从淘宝镜像下载模块, 其他的与npm一致
 *          - 安装cnpm:    npm install -g cnpm --registry=https://registry.npm.taobao.org
 *          - 使用cnpm安装模块: cnpm install 模块名
 * 
 *      4. 使用node中的yarn模块安装第三方模块: 省略...百度
 */

// 导入安装的包
let md5 = require("md5")