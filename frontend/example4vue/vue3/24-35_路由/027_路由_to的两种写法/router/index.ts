


import {createRouter,createWebHistory,createWebHashHistory} from 'vue-router'

import Home from '@/pages/Home.vue'

import About from '@/pages/About.vue'


const router = createRouter({
  history:createWebHashHistory(),
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
