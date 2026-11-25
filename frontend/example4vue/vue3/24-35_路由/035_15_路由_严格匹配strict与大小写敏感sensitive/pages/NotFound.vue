<template>
  <div class="not-found">
    <h2>404</h2>
    <p>路径 {{ $route.path }} 没有匹配到路由</p>
    <p>因为路由器开启了strict和sensitive, 尾斜杠和大写都不会被宽容处理</p>
  </div>
</template>

<script setup lang="ts" name="NotFound">

</script>

<style scoped>
  .not-found {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
    gap: 10px;
  }
</style>
