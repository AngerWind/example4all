<template>
  <h2>我是Home的内容</h2>
</template>

<script>

/**
 * 被路由到的组件, 他的this上面都会多出来两个属性
 * - $route: 路由到当前组件上的一些信息, 比如路由的参数
 * - $router: 全局唯一的路由器
 */

export default {
  name: "Home"
}
</script>

<style scoped>
</style>
