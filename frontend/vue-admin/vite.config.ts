import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import * as path from 'path'
// 引入svg需要用到的插件
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import { viteMockServe } from 'vite-plugin-mock'

// https://vitejs.dev/config/
export default defineConfig(({ command, mode }) => {
  // 第一个参数为mode, 表示当前环境
  // 第二个参数为项目根目录, 指定.env文件的位置
  let env = loadEnv(mode, process.cwd())
  const config = {
    plugins: [
      vue(),
      // svg插件
      createSvgIconsPlugin({
        // 指定svg文件存放的位置
        iconDirs: [path.resolve(process.cwd(), 'src/assets/icons')],
        // Specify symbolId format
        symbolId: 'icon-[dir]-[name]',
      }),
      viteMockServe({
        localEnabled: command === 'serve', // 保证开发阶段可以使用mock接口
      }),
    ],
    resolve: {
      alias: {
        '@': path.resolve('./src'), // 相对路径别名配置，使用 @ 代替 ./src
      },
    },
    css: {
      preprocessorOptions: {
        scss: {
          javascriptEnabled: true,
          additionalData: '@import "./src/styles/variable.module.scss";', // 指定sass变量位置
        },
      },
    },
    server: {
      // 设置请求转发
      proxy: {
        [env.VITE_APP_BASE_API + '/admin/acl']: {
          // 获取环境变量中的base-url, 然后针对其进行转发
          target: 'http://139.198.104.58:8212', // 转发的目标服务器
          changeOrigin: true, // 是否将主机头的源更改为目标URL, 用以解决跨域问题
          rewrite: (path) =>
            path.replace(new RegExp(`^${env.VITE_APP_BASE_API}`), ''), // 在转发请求的时候重写请求, 消除掉base-url
        },
        // ^开头的会被当做正则表达式  .*表示匹配任意字符
        [`^${env.VITE_APP_BASE_API}/admin/.*`]: {
          // 获取环境变量中的base-url, 然后针对其进行转发
          target: 'http://39.98.123.211:8510', // 转发的目标服务器
          changeOrigin: true, // 是否将主机头的源更改为目标URL, 用以解决跨域问题
          rewrite: (path) =>
            path.replace(new RegExp(`^${env.VITE_APP_BASE_API}`), ''), // 在转发请求的时候重写请求, 消除掉base-url
        },
        // 这里是官方文档的例子
        // // http://localhost:5173/api/bar -> http://jsonplaceholder.typicode.com/bar
        // '/api': {  // 针对base-url为/api的请求进行转发
        //   target: 'http://jsonplaceholder.typicode.com', // 转发的目标服务器
        //   changeOrigin: true, // 是否将主机头的源更改为目标URL, 用以解决跨域问题
        //   rewrite: (path) => path.replace(/^\/api/, ''), // 在转发请求的时候重写请求
        // },
      },
    },
  }
  return config
})
