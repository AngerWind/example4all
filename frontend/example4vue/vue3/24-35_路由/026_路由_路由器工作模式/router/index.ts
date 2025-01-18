
import {createRouter,createWebHistory,createWebHashHistory} from 'vue-router'

import Home from '@/pages/Home.vue'
import About from '@/pages/About.vue'


const router = createRouter({
  // 路由有两种工作模式, 一种是hash模式, 一种是history模式
  // 使用比较多的是history模式, 因为url比较漂亮, 但是如果直接使用的话刷新的时候则会报错404
  // 开发的时候需要配置代理服务器
  // 上线的时候需要nginx的配合
  // 具体可以看vue2中的讲解
  history:createWebHistory(), // 使用history模式, 通过createWebHashHistory()来指定hash模式
  routes:[
    {
      path:'/home',
      component:Home
    },
    {
      path:'/about',
      component:About
    },
  ]
})

// 暴露出去router
export default router
