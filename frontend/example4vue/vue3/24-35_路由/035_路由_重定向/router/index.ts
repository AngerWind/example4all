

import {createRouter,createWebHistory,createWebHashHistory} from 'vue-router'

import Home from '@/pages/Home.vue'
import News from '@/pages/News.vue'


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
    },
    {
      path:'/',
      redirect:'/home' // 重定向, 已访问/, 就自动跳转到/home, 这样一开始就可以进行默认的路由了
    }
  ]
})

// 暴露出去router
export default router
