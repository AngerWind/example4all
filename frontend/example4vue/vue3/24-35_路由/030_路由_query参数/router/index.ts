
import {createRouter,createWebHistory,createWebHashHistory} from 'vue-router'

import Detail from '@/pages/Detail.vue'


const router = createRouter({
  history:createWebHistory(),
  routes:[
    {
      name:'xiang',
      path:'detail',
      component:Detail
    }
  ]
})

// 暴露出去router
export default router
