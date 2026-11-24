

import {createRouter,createWebHistory,createWebHashHistory} from 'vue-router'

import Home from '@/pages/Home.vue'
import News from '@/pages/News.vue'


const router = createRouter({
  history:createWebHistory(),
  routes:[
    {
      name:'zhuye',
      path:'/home',
      component:Home,
      // meta: 路由元信息, 可以在路由上挂一些自定义的数据, 在路由守卫中通过to.meta拿到
      meta:{
        isAuth:false,
        title:'首页'
      }
    },
    {
      name:'xinwen',
      path:'/news',
      component:News,
      meta:{
        isAuth:true, // 标记这个路由需要鉴权
        title:'新闻'
      },
      // 路由独享守卫: 只作用于这一条路由, 在进入/news之前被调用
      // 注意它没有"独享的后置守卫", 后置只能用全局的afterEach
      beforeEnter(to,from){
        console.log('新闻路由的独享守卫beforeEnter',to,from)
        if(to.meta.isAuth){
          if(localStorage.getItem('school') === 'atguigu'){
            // Vue3的守卫中: 不写返回值或返回true, 表示放行
          }else{
            alert('学校名不对, 无权限查看!')
            // 返回false表示取消本次导航
            return false
          }
        }
      }
    },
  ]
})

// 全局前置路由守卫: 初始化和每一次路由切换之前被调用
// to: 要去哪个路由, from: 从哪个路由来
// Vue3中推荐用返回值控制导航, 不再用next():
//   返回undefined/true  -> 放行
//   返回false           -> 取消导航
//   返回'/home'或{name:'zhuye'} -> 重定向到别处
router.beforeEach((to,from) => {
  console.log('全局前置守卫',to,from)
  if(to.meta.isAuth){
    // 代表需要鉴权
    if(localStorage.getItem('school') === 'atguigu'){
      // 放行
    }else{
      alert('无权限!')
      return false
    }
  }
})

// 全局后置路由守卫: 初始化和每一次路由切换之后被调用
// 它不能拦截导航(导航已经完成了), 一般用来做埋点、改标题之类的收尾工作
router.afterEach((to,from) => {
  console.log('全局后置守卫',to,from)
  // 每切换一个路由, 就把页面的document.title改成路由meta中配置的title
  document.title = (to.meta.title as string) || 'vue3_test'
})

// 暴露出去router
export default router
