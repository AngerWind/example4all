
import {createRouter,createWebHistory,createWebHashHistory} from 'vue-router'

import Home from '@/pages/Home.vue'
import News from '@/pages/News.vue'
import About from '@/pages/About.vue'
import Detail from '@/pages/Detail.vue'


const router = createRouter({
  history:createWebHistory(),
  routes:[
    {
      name:'xiang',
      // 使用路径参数的时候, 必须要在path中定义参数, 否则匹配不到
      // content后面添加一个?, 表示可传可不传
      path:'detail/:id/:title/:content?',
      component:Detail
    }
  ]
})

// 暴露出去router
export default router
