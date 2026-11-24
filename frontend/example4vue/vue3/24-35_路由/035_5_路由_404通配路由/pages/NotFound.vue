<template>
  <div class="not-found">
    <h2>404</h2>
    <p>页面不存在, 您访问的路径是: {{ $route.path }}</p>
    <RouterLink to="/home">返回首页</RouterLink>
  </div>
</template>

<script setup lang="ts" name="NotFound">
  import {RouterLink} from 'vue-router'

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
