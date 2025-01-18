


import {createRouter,createWebHistory,createWebHashHistory} from 'vue-router'
import Home from '@/pages/Home.vue'
import About from '@/pages/About.vue'


const router = createRouter({
  history:createWebHashHistory(),
  routes:[
    {
      name:'zhuye', // 可以对路由定义一个名字, 然后在 to 的时候指定name
      path:'/home',
      component:Home
    },
    {
      name:'guanyu',
      path:'/about',
      component:About
    },
  ]
})

// 暴露出去router
export default router
