

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
  ]
})

// 全局前置守卫: 一律拦截/news, 让push返回NavigationFailure, 配合App.vue的演示
router.beforeEach((to,from) => {
  if(to.path === '/news'){
    return false
  }
})

// 暴露出去router
export default router
