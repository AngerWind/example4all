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
// console.log(import.meta.env)

// 引入全局样式, 包括css中的变量
import '@/styles/index.scss'

// 全局注册所有element-plus的图标, 之后再组件中可以直接使用, 而不用import
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 配置pinia
import { store } from '@/store/index.ts'
app.use(store)

// 配置路由
import router from '@/router/index.ts'
app.use(router)

// 全局的错误处理
app.config.errorHandler = (err, instance, info) => {
  // todo 在组件的setup上面报错应该怎么办, 这里好像没有用
  console.error(err, instance, info)
}

app.mount('#app')
