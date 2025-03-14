const path = require("path");
const HTMLPlugin = require("html-webpack-plugin");

module.exports = {
  mode: "production", // 设置打包的模式，production表示生产模式, 会混淆文件,   development 开发模式, 尽量保留源码

  entry: "./src/index.js", // 用来指定打包时的主文件 默认 ./src/index.js
  // entry: ["./src/a.js.js", "./src/b.js"], // 可以打包多个, 打包后的多个文件js合并成一个js文件
  // entry: {                             // 可以是对象, 每个文件打包成一个单独的文件
  //     a1: "./src/a.js.js", // 打包a.js生成a1.js
  //     b1: "./src/b.js" // 打包b.js生成b1.js
  // },

  output: {
    path: path.resolve(__dirname, "dist"), // 指定打包的目录，必须要绝对路径, 默认dist
    filename: "bundle.js", // 打包后的文件名
    // filename:"[name]-[id]-[hash].js", // 对于打包后生成多个js文件的(entry为对象), 要使用这种形式表示打包后的js文件名, 如果是上面那种会报错, [name]表示原先js文件名, [id]随机生成, [hash]哈希, 更多查看文档
    // clean: true // 在生成文件之前是否清理打包路径下的文件, 默认为false, 这个选项用于清理上次打包的js文件
  },

  // webpack默认情况下，只会处理js文件，如果我们希望它可以处理其他类型的文件，则要为其引入loader
  // 使用npm install -D css-loader 安装css-loader, 之后可以在js中import css文件
  module: {
    rules: [
      {
        // 指定需要处理的文件, 这里指定以.css结尾的文件
        test: /\.css$/i,
        // 这里指定处理的loader, css-loader只负责将css打包进js, 但是css不生效, 要使用css生效, 还要style-loader
        // css-loader必须在style-loader后面, 因为loader是从后往前执行的
        // npm install -D style-loader css-loader
        use: ["style-loader", "css-loader"],
      },
      {
        test: /\.(jpg|png|gif)$/i,
        type: "asset/resource", // 对于图片, 只需要路径即可, 所以不需要安装loader, 所以通过指定type来处理(不是很清楚?????)
      },

      // babel是一个能够将高版本js语法转换为低版本js语法的工具, 同时也可以与webpack一起使用
      // 安装 npm install -D babel-loader @babel/core @babel/preset-env
      // 其中@babel/core是核心组件, babel-loader将babel和webpack连接起来
      // babel中也是通过一条条的规则将高版本语法和低版本语法对应, @babel/preset-env就是内置的规则, 如果不安装的话就要手写
      {
        test: /\.m?js$/, // 指定babel处理的文件, 以.js或者.mjs结尾的文件
        exclude: /(node_modules|bower_components)/, // 排除的文件
        use: {
          loader: "babel-loader", // 指定loader
          // babel的配置
          options: {
            presets: ["@babel/preset-env"],
            // 如果要指定babel-loader转换后的语法能够兼容哪些浏览器, 可以在package.json中设置"browserslist"这个属性
            // 这个属性的具体设置规则可以参看  https://github.com/browserslist/browserslist
          },
        },
      },
    ],
  },
  // 插件
  plugins: [
    // html-webpack-plugin会自动生成一个html文件, 并将打包后的js文件引入到其中
    new HTMLPlugin({
      //   title: "hello world", // 设置自动生成的html文件的title
      template: "./src/index.html", // 指定自动生成的html的模板
    }),
  ],
  // 因为webpack打包后的文件是乱七八糟的, 我们在浏览器上面无法调试, 要使用这个工具
  // 这个工具只有在mode: development下才有效
  // 添加这个工具后再浏览器中选中`source`, 下面有一个项目同名的文件夹, 其中有源码, 并且可以在源码上打断点
  devtool: "inline-source-map",
};
