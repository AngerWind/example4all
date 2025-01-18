import { Router } from 'vue-router'
import { useUserStore } from '@/store/modules/user.ts'

export default function install(router: Router) {
  // 前置路由守卫
  router.beforeEach((to, from, next) => {
    // to: 将要访问的路由
    // from: 从哪个路由跳转而来
    // next: 放行函数
    const userStore = useUserStore()
    if (userStore.token) {
      if (to.path === '/login') {
        // 有token后不能访问登录页面
        next({ path: '/' })
      } else {
        next()
      }
    } else {
      if (to.path === '/login') {
        next()
      } else {
        // 没有token不能访问其他页面
        next({ path: '/login', query: { redirect: to.fullPath } })
      }
    }
  })
}
