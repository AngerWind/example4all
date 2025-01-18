
import {createRouter,createWebHistory} from 'vue-router'

import Home from '@/components/Home.vue'
import News from '@/components/News.vue'
import About from '@/components/About.vue'


// 注意, 这里不再是vue2中的 new VueRouter({}) 了
const router = createRouter({
  history:createWebHistory(), // 注意这里必须要指定路由的工作模式!!!!, 但是在vue2中这比赛必须的
  routes:[ //一个一个的路由规则
    {
      path:'/home',
      component:Home
    },
    {
      path:'/news',
      component:News
    },
    {
      path:'/about',
      component:About
    },
  ]
})

// 暴露出去router
export default router
