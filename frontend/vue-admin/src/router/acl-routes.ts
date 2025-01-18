import { RouteRecordRaw } from 'vue-router'

const aclRouter: RouteRecordRaw = {
  path: '/acl',
  redirect: '/acl/role',
  meta: {
    menu: {
      title: '权限管理',
      icon: 'Lock',
    },
  },
  children: [
    {
      path: 'role', // 角色管理
      // alias: [''], // 别名, 也就是说访问/acl和/acl/role都会访问到role组件
      component: () => import('@/views/acl/role/index.vue'),
      name: 'role',
      meta: {
        menu: {
          title: '角色管理',
          icon: 'Avatar',
        },
      },
    },
    {
      path: 'user', // 用户管理
      component: () => import('@/views/acl/user/index.vue'),
      name: 'user',
      meta: {
        menu: {
          title: '用户管理',
          icon: 'User',
        },
      },
    },
    {
      path: 'permission', // 菜单管理
      component: () => import('@/views/acl/permission/index.vue'),
      name: 'permission',
      meta: {
        menu: {
          title: '菜单管理',
          icon: 'List',
        },
      },
    },
  ],
}
export default aclRouter
