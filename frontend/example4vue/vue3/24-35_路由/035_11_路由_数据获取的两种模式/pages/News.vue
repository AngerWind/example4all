<template>
  <div class="news">
    <h3>新闻: 导航完成前获取数据</h3>
    <!-- 模式二: 导航完成前获取, 数据拿到后才进入页面, 没有loading过程 -->
    <p>获取到的数据: {{ data }}</p>
  </div>
</template>

<!-- beforeRouteEnter只能写在选项式API中 -->
<script lang="ts">
import {fetchData} from '@/pages/mock'

export default {
  // 模式二(导航完成前获取): 在进入这个路由组件之前, 先把数据请求完
  // 数据放到路由的meta里, 组件再从$route.meta中取
  // 优点: 进入页面时数据已经就绪, 没有loading闪烁
  // 缺点: 数据没拿到之前页面不切换, 用户会在旧页面停留
  async beforeRouteEnter(to, from) {
    const data = await fetchData('news的数据')
    to.meta.data = data
  },
}
</script>

<script setup lang="ts" name="News">
  import {computed} from 'vue'
  import {useRoute} from 'vue-router'

  const route = useRoute()
  // 从meta中拿到beforeRouteEnter放进去的数据
  const data = computed(() => route.meta.data)

</script>

<style scoped>
  .news {
    padding: 20px;
  }
</style>
