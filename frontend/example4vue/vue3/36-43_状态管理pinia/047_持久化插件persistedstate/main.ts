import {createApp} from 'vue'
import App from './App.vue'
import {createPinia} from 'pinia'
// 持久化插件, 需要先安装: npm install pinia-plugin-persistedstate
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const app = createApp(App)
const pinia = createPinia()
// 安装持久化插件
// 之后state中的数据会自动同步到localStorage, 刷新页面后自动恢复, 不会丢失
pinia.use(piniaPluginPersistedstate)
app.use(pinia)
app.mount('#app')
