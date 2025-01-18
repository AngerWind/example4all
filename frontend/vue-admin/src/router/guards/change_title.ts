// 后置路由守卫
import router from '@/router'
import { Router } from 'vue-router'

export default function install(router: Router) {
  router.afterEach((to, from) => {
    // to: 将要访问的路由
    // from: 从哪个路由跳转而来
    document.title = to.meta.menu
      ? to.meta.menu.title
      : import.meta.env.VITE_APP_TITLE // 设置页面标题
  })
}
