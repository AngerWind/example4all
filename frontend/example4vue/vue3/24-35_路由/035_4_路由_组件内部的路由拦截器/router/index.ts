

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
      meta:{
        isAuth:true // 标记需要鉴权, 在News组件内的beforeRouteEnter中校验
      }
    },
  ]
})

// 暴露出去router
export default router
