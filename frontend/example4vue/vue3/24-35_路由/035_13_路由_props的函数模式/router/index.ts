

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
      path:'/news/:id', // :id是params参数
      component:News,
      // props的三种写法回顾:
      //   1. 对象模式: props:{id:'666'}  -> 写死的数据, 用得少
      //   2. 布尔模式: props:true        -> 只把params参数以props传给组件, query不传
      //   3. 函数模式: props:(route)=>{...} -> 最灵活, params/query都能自由组装成props
      // 函数模式接收当前路由对象route, 返回的对象会作为props传给组件
      props:(route) => {
        return {
          id:route.params.id,
          keyword:route.query.keyword
        }
      }
    },
  ]
})

// 暴露出去router
export default router
