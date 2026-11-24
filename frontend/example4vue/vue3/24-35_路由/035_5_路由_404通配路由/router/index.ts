

import {createRouter,createWebHistory,createWebHashHistory} from 'vue-router'

import Home from '@/pages/Home.vue'
import News from '@/pages/News.vue'
import NotFound from '@/pages/NotFound.vue'


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
      // 404通配路由: 当上面的路由都匹配不上时, 兜底匹配这条
      // path中的/:pathMatch(.*)*是正则写法, 表示匹配任意路径(包括多级路径)
      // 注意1: vue-router4中不能再像vue2那样写path:'*', 必须用:pathMatch(.*)*
      // 注意2: 通配路由要放在routes的最后, 避免把正常的路由也拦截了
      name:'notfound',
      path:'/:pathMatch(.*)*',
      component:NotFound
    },
  ]
})

// 暴露出去router
export default router
