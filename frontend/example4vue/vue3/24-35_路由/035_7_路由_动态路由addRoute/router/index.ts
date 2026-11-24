

import {createRouter,createWebHistory,createWebHashHistory} from 'vue-router'

import Home from '@/pages/Home.vue'
import News from '@/pages/News.vue'
import NoRoute from '@/pages/NoRoute.vue'


const router = createRouter({
  history:createWebHistory(),
  // 初始路由里只有home和news, 没有admin
  // admin路由通过router.addRoute()在运行时动态添加(见App.vue)
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
      // 兜底路由: 未匹配到的路径(包括还没添加的/admin)都走这里
      name:'noroute',
      path:'/:pathMatch(.*)*',
      component:NoRoute
    },
  ]
})

// 暴露出去router
export default router
