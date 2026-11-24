<template>
  <div class="news">
    <span>这里是新闻</span>
    <!-- 数字每100毫秒加1, 切走时停止, 切回来时继续 -->
    <h2>{{ sum }}</h2>
  </div>
</template>

<script setup lang="ts" name="News">
  import {ref,onActivated,onDeactivated} from 'vue'

  const sum = ref(0)
  let timer: number

  // 激活: 被keep-alive缓存的组件, 每次切入(包括第一次挂载)时调用
  // 对应选项式API中的activated
  onActivated(() => {
    console.log('News组件被激活了')
    timer = window.setInterval(() => {
      sum.value += 1
    }, 100)
  })

  // 失活: 被keep-alive缓存的组件, 每次切走时调用(组件并没有被销毁)
  // 对应选项式API中的deactivated
  // 定时器之类的副作用要在这里清理, 否则切走后定时器还在后台跑
  onDeactivated(() => {
    console.log('News组件失活了')
    clearInterval(timer)
  })

</script>

<style scoped>
  .news {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
  }
</style>
