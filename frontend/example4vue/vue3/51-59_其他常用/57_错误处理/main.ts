import { createApp } from 'vue'
import App from './App.vue'

const app = createApp(App)

// 全局错误处理: 应用内任何组件抛出的、没被捕获的错误都会走到这里
// 实际项目中可以在这里统一上报错误日志(sentry等), 避免错误直接抛到控制台、页面白屏
app.config.errorHandler = (err, instance, info) => {
  console.log('全局捕获到错误:', err)
  console.log('错误来源组件:', instance)
  console.log('错误信息(生命周期/事件名):', info)
}

app.mount('#app')
