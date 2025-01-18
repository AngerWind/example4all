// 通过vue-router插件实现模板路由配置

import { createRouter, createWebHashHistory } from 'vue-router'
import routes from '@/router/routes.ts'

const router = createRouter({
  // 路由模式: hash
  history: createWebHashHistory(),
  routes: routes,
  // 路由跳转后滚动到顶部
  scrollBehavior() {
    return { top: 0, left: 0 }
  },
})

export default router
