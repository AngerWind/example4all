// typings.d.ts or router.ts
import 'vue-router'

type MenuBasic = {
  title: string
  icon: string
}
declare module 'vue-router' {
  // 添加一个新的路由元信息的类型声明
  interface RouteMeta {
    menu?: MenuBasic
  }
}

export { type MenuBasic }
