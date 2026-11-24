

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
  ],
  // scrollBehavior: 滚动行为, 路由切换时控制页面滚动到哪里
  // to: 要去的路由, from: 离开的路由
  // savedPosition: 只有通过浏览器前进/后退按钮切换路由时才有值, 表示之前记住的滚动位置
  scrollBehavior(to,from,savedPosition){
    if(savedPosition){
      // 浏览器前进/后退: 恢复之前的滚动位置
      return savedPosition
    }else if(to.hash){
      // 带锚点的跳转(如 /home#section1): 滚动到对应元素
      return {
        el:to.hash,
        behavior:'smooth' // 平滑滚动
      }
    }else{
      // 普通的路由切换: 回到顶部
      return {
        top:0
      }
    }
  }
})

// 暴露出去router
export default router
