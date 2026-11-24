

import {createRouter,createWebHistory,createWebHashHistory} from 'vue-router'

// 路由懒加载: 不要在一开始就用import静态引入所有路由组件
// import Home from '@/pages/Home.vue'
// 而是写成动态导入() => import('...')的形式
//
// 原理:
//   静态import: 所有路由组件会被打包进同一个js文件, 首屏就要下载全部代码, 页面越多首屏越慢
//   动态import: vite/webpack会把每个动态导入的模块单独打成一个js文件(代码分割),
//               只有真正访问这个路由时才去下载对应的文件, 首屏体积变小
//
// 可以混用: 首屏就要展示的组件静态导入, 其他路由懒加载
import Home from '@/pages/Home.vue'


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
      // 懒加载写法: component的值是一个返回import()的函数
      component:() => import('@/pages/News.vue')
    },
  ]
})

// 暴露出去router
export default router
