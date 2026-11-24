

import {createRouter,createWebHistory,createWebHashHistory} from 'vue-router'

import Home from '@/pages/Home.vue'
import News from '@/pages/News.vue'


const router = createRouter({
  history:createWebHistory(),
  routes:[
    {
      name:'zhuye',
      path:'/home',
      // alias: 路由别名, 让同一个路由可以通过多个路径访问
      // 访问/home/index时, 浏览器地址栏显示的是/home/index, 匹配的依然是这条路由
      //
      // 别名和重定向的区别:
      //   别名(alias):    地址栏不变, 直接匹配原路由渲染对应组件
      //   重定向(redirect): 地址栏会变成目标路径, 相当于"跳转"过去
      alias:'/home/index',
      component:Home
    },
    {
      name:'xinwen',
      path:'/news',
      // 多个别名可以写成数组
      alias:['/xw','/xinwen'],
      component:News,
    },
  ]
})

// 暴露出去router
export default router
