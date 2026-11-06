<template>
  <div class="card component-b">
    <h3>组件 B</h3>
    <h2>当前求和：{{ sum }}</h2>
    <button @click="countStore.decrement(1)">组件 B：减 1</button>
    <button @click="countStore.reset()">重置</button>
  </div>
</template>

<script setup lang="ts" name="CountB">
import { storeToRefs } from 'pinia'
import { useCountStore } from '@/store/count'

// 再次调用 useCountStore()，得到的是组件 A 正在使用的同一个 Store。
const countStore = useCountStore()

// sum 仍然关联 Store 中的状态，不是复制出一份独立数据。
// 在模板中会自动解包；如果在 script 中修改，需要写 sum.value。
const { sum } = storeToRefs(countStore)
</script>

<style scoped>
.card {
  min-width: 260px;
  padding: 20px;
  border-radius: 10px;
}

.component-b {
  background-color: orange;
}

button {
  margin: 0 5px;
  padding: 6px 12px;
  cursor: pointer;
}
</style>
