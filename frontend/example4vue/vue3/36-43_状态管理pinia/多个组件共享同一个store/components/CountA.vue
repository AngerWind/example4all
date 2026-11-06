<template>
  <div class="card component-a">
    <h3>组件 A</h3>
    <h2>当前求和：{{ countStore.sum }}</h2>
    <button @click="countStore.increment(1)">组件 A：加 1</button>
  </div>
</template>

<script setup lang="ts" name="CountA">
import { useCountStore } from '@/store/count'

// 第一次使用 count Store 时，Pinia 创建 Store 并初始化 state。
const countStore = useCountStore()
</script>

<style scoped>
.card {
  min-width: 260px;
  padding: 20px;
  border-radius: 10px;
}

.component-a {
  background-color: skyblue;
}

button {
  padding: 6px 12px;
  cursor: pointer;
}
</style>
