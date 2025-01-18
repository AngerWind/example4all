// 引入不再是Vue构造函数了，引入的是一个名为createApp的工厂函数
// 注意在这里无法像vue2那样去引入一个Vue构造函数了
import {createApp} from 'vue'
import App from './App.vue'

// 根据App组件来创建应用, 并挂载到#app上
let app = createApp(App)
app.mount('#app'); // app类似于vue2中vm,但app比vm更轻


// //vue2写法
// const vm = new Vue({
//     render: h=> h(App)
// });
// vm.$mount('#app')
