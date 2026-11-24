<template>
  <div class="news">
    <span>这里是新闻</span>
  </div>
</template>

<!-- 普通的script块, 和script setup可以共存 -->
<script lang="ts">
export default {
  // beforeRouteEnter没有组合式API的等价写法,
  // 因为它是"进入组件之前"调用的, 此时组件实例都还没创建, setup自然也没执行,
  // 所以只能写在选项式API中
  // 注意: 只有"通过路由规则进入该组件"才会调用, 组件作为普通子组件被引用时不会调用
  beforeRouteEnter(to, from) {
    console.log('News---beforeRouteEnter', to, from)
    if (to.meta.isAuth) {
      if (localStorage.getItem('school') === 'atguigu') {
        // 返回true或不返回值, 表示放行
      } else {
        alert('无权限!')
        return false // 返回false, 取消本次导航
      }
    }
  },
}
</script>

<script setup lang="ts" name="News">
  import {onBeforeRouteUpdate,onBeforeRouteLeave} from 'vue-router'

  // beforeRouteUpdate的组合式写法: 当前路由改变, 但该组件被复用时调用
  // 比如从 /news/1 跳到 /news/2, 路由变了但News组件不重建
  onBeforeRouteUpdate((to, from) => {
    console.log('News---onBeforeRouteUpdate', to, from)
  })

  // beforeRouteLeave的组合式写法: 通过路由规则离开该组件时调用
  // 返回false可以阻止离开(比如表单没保存时提示用户)
  onBeforeRouteLeave((to, from) => {
    console.log('News---onBeforeRouteLeave', to, from)
    const answer = window.confirm('确定要离开新闻页面吗?')
    if (!answer) {
      return false // 取消本次导航, 留在当前页
    }
  })

</script>

<style scoped>
  .news {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
  }
</style>
