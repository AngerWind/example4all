import router from '@/router'

// 导入进度条插件和样式
// @ts-expect-error 没有类型声明文件
import NProgress from 'nprogress'
NProgress.configure({ showSpinner: false }) // 进度条加载的时候不需要转圈
import 'nprogress/nprogress.css'
import { Router } from 'vue-router'

export default function install(router: Router) {
  router.beforeEach((to, from, next) => {
    NProgress.start() // 开启进度条
    next()
  })
  router.afterEach(() => {
    NProgress.done() // 关闭进度条
  })
}
