import {createApp} from 'vue'
import App from './App.vue'

// npm i pinia 安装pinia


// 第一步：引入pinia
import {createPinia} from 'pinia'

const app = createApp(App)
// 第二步：创建pinia
const pinia = createPinia()
// 第三步：安装pinia
app.use(pinia)
app.mount('#app')