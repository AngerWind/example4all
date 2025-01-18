import { createApp } from 'vue'
import App from './App.vue'

// 引入element插件
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 引入svg插件
import 'virtual:svg-icons-register'

// 这里也可以使用@ts-ignore, 表示忽略当行语法检查
// 而@ts-expect-error表示下面行会报错, 并且忽略他. 如果下面行没有办法, 反而会打包不成功
//@ts-expect-error 忽略当前文件ts类型的检测否则有红色提示(打包会失败), 因为zh-cn.mjs没有类型声明文件
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

const app = createApp(App)

// 使用element插件
app.use(ElementPlus, {
  locale: zhCn, // element组件内容使用中文
})

// 输出vite的环境变量
console.log(import.meta.env)

// 引入全局样式
import '@/styles/index.scss'

// 配置路由
import router from '@/router/index.ts'
app.use(router)

app.mount('#app')
