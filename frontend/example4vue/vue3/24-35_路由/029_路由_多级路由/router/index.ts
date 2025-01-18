
import {createRouter,createWebHistory,createWebHashHistory} from 'vue-router'

import Home from '@/pages/Home.vue'
import News from '@/pages/News.vue'
import Detail from '@/pages/Detail.vue'


const router = createRouter({
  history:createWebHistory(),
  routes:[
    {
      name:'zhuye',
      path:'/home',
      component:Home
    },
    {
      name:'xinwen',
      path:'/news',
      component:News,
      children:[ // 通过children来定义二级路由
        {
          path:'detail',
          component:Detail
        }
      ]
    },
  ]
})

// 暴露出去router
export default router
