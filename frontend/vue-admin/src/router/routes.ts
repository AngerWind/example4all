import { RouteRecordRaw } from 'vue-router'
import { type MenuBasic } from '@/router/type-extends'
import aclRoutes from '@/router/acl-routes.ts'
import product from '@/router/product.ts'

const route: RouteRecordRaw[] = [
  {
    path: '/login',
    component: () => import('@/views/login/index.vue'),
    name: 'login', // 命名路由
  },
  {
    path: '/', // 登录成功后跳转到首页
    component: () => import('@/views/layout/index.vue'),
    name: 'index',
    redirect: '/home',
    children: [
      {
        path: '/home',
        // alias: [''], // 别名, 也就是说访问/和/home都会访问到home组件,
        component: () => import('@/views/home/index.vue'),
        name: 'home',
        props: {
          index: 10,
        },
        meta: {
          menu: {
            title: '首页',
            icon: 'HomeFilled',
          },
        },
      },
      aclRoutes,
      product,
    ],
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
  {
    // 数据大屏路由
    path: '/screen',
    component: () => import('@/views/screen/index.vue'),
    name: 'screen',
    meta: {
      menu: {
        title: '数据大屏',
        icon: 'Platform',
      },
    },
  },
]
export default route

type MenuItem = {
  path: string
  subMenu?: MenuItem[]
} & MenuBasic

function getMenuData(routes: RouteRecordRaw[]): MenuItem[] {
  for (const route of routes) {
    if (route.meta?.menu) {
      const menu: MenuItem = {
        path: route.path,
        ...route.meta.menu,
      }
      if (route.children) {
        menu.subMenu = getMenuData(route.children)
      }
    }
  }
  return []
}

export const menuData: MenuItem[] = [
  {
    title: '数据大屏',
    icon: 'Platform',
    path: '/screen',
  },
  {
    title: '首页',
    icon: 'HomeFilled',
    path: '/home',
  },
  {
    path: '/acl',
    title: '权限管理',
    icon: 'Lock',
    subMenu: [
      {
        path: '/acl/role', // 角色管理
        title: '角色管理',
        icon: 'Avatar',
      },
      {
        path: '/acl/user', // 用户管理
        title: '用户管理',
        icon: 'User',
      },
      {
        path: '/acl/permission', // 菜单管理
        title: '菜单管理',
        icon: 'List',
      },
    ],
  },
  {
    path: '/product',
    title: '商品管理',
    icon: 'Goods',
    subMenu: [
      {
        path: '/product/attr', // 属性管理
        title: '属性管理',
        icon: 'Management',
      },
      {
        path: '/product/trademark', // 品牌管理
        title: '品牌管理',
        icon: 'ShoppingCart',
      },
      {
        path: '/product/spu', // spu管理
        title: 'spu管理',
        icon: 'SetUp',
      },
      {
        path: '/product/sku', // sku管理
        title: 'sku管理',
        icon: 'ScaleToOriginal',
      },
    ],
  },
]
