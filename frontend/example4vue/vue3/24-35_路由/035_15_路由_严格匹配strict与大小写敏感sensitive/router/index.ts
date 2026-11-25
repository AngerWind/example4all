

import {createRouter,createWebHistory,createWebHashHistory} from 'vue-router'

import Home from '@/pages/Home.vue'
import NotFound from '@/pages/NotFound.vue'


const router = createRouter({
  history:createWebHistory(),
  // strict: 严格模式, 默认为false
  //   false时 /home 和 /home/ 都能匹配到path:'/home'
  //   true时尾斜杠会被区别对待, /home/匹配不上path:'/home'
  strict:true,
  // sensitive: 大小写敏感, 默认为false
  //   false时 /HOME 也能匹配到path:'/home'
  //   true时大小写必须完全一致, /HOME匹配不上path:'/home'
  sensitive:true,
  routes:[
    {
      name:'zhuye',
      path:'/home',
      component:Home
    },
    {
      // 配合404路由, 验证被strict/sensitive拒掉的那些路径
      name:'notfound',
      path:'/:pathMatch(.*)*',
      component:NotFound
    },
  ]
})

// 暴露出去router
export default router
