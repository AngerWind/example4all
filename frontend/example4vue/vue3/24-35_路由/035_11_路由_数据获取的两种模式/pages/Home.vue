<template>
  <div class="home">
    <h3>首页: 导航完成后获取数据</h3>
    <!-- 模式一: 导航完成后获取, 页面先出来, 数据加载中显示loading -->
    <p v-if="loading">数据加载中...</p>
    <p v-else>获取到的数据: {{ data }}</p>
  </div>
</template>

<script setup lang="ts" name="Home">
  import {ref,onMounted} from 'vue'
  import {fetchData} from '@/pages/mock'

  const loading = ref(true)
  const data = ref('')

  // 模式一(导航完成后获取): 组件挂载后再发请求
  // 优点: 页面切换快, 用户马上看到页面骨架/loading
  // 缺点: 有一段loading时间
  onMounted(async () => {
    data.value = await fetchData('home的数据')
    loading.value = false
  })

</script>

<style scoped>
  .home {
    padding: 20px;
  }
</style>
