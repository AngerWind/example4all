<template>
  <div class="app">
    <h2>多个组件共享同一个 Store</h2>
    <p>在两个组件中分别操作，观察两边的 sum 始终同步。</p>

    <div class="components">
      <CountA />
      <CountB />
    </div>
  </div>
</template>

<script setup lang="ts" name="App">
import CountA from './components/CountA.vue'
import CountB from './components/CountB.vue'
</script>

<style scoped>
.app {
  padding: 20px;
  text-align: center;
}

.components {
  display: flex;
  gap: 20px;
  justify-content: center;
  margin-top: 20px;
}
</style>
