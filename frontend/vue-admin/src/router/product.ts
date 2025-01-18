import { RouteRecordRaw } from 'vue-router'

const product: RouteRecordRaw = {
  path: '/product',
  redirect: '/product/attr',
  meta: {
    menu: {
      title: '商品管理',
      icon: 'el-icon-s-shop',
    },
  },
  children: [
    {
      path: 'attr', // 属性管理
      // alias: [''], // 别名, 也就是说访问/product和/product/attr都会访问到attr组件
      component: () => import('@/views/product/attr/index.vue'),
      name: 'attr',
      meta: {
        menu: {
          title: '属性管理',
          icon: 'Management',
        },
      },
    },
    {
      path: 'trademark', // 品牌管理
      component: () => import('@/views/product/trademark/index.vue'),
      name: 'trademark',
      meta: {
        menu: {
          title: '品牌管理',
          icon: 'ShoppingCart',
        },
      },
    },
    {
      path: 'spu', // spu管理
      component: () => import('@/views/product/spu/index.vue'),
      name: 'spu',
      meta: {
        menu: {
          title: 'spu管理',
          icon: 'SetUp',
        },
      },
    },
    {
      path: 'sku', // sku管理
      component: () => import('@/views/product/sku/index.vue'),
      name: 'sku',
      meta: {
        menu: {
          title: 'sku管理',
          icon: 'ScaleToOriginal',
        },
      },
    },
  ],
}
export default product
