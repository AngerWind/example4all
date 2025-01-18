export default [
  {
    path: '/login',
    component: () => import('@/views/login/index.vue'),
    name: 'login', // 命名路由
  },
  {
    path: '/', // 登录成功后重定向到首页
    component: () => import('@/views/home/index.vue'),
    name: 'home',
  },
  {
    path: '/404',
    component: () => import('@/views/404/index.vue'),
    name: '404',
  },
  {
    path: '/:pathMatch(.*)*', // 匹配所有路径
    redirect: '/404', // 重定向到404页面
    name: 'Any',
  },
]
