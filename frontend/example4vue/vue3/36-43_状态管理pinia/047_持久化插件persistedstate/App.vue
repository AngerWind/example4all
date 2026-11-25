<template>
  <div class="app">
    <h2>pinia数据持久化(刷新不丢失)</h2>
    <Count/>
    <!-- 验证: sum加几下, 然后刷新浏览器, sum还在; 打开devtools的Application->Local Storage可以看到存储的数据 -->
  </div>
</template>

<script setup lang="ts" name="App">
  import Count from './components/Count.vue'

</script>

<style scoped>
</style>
