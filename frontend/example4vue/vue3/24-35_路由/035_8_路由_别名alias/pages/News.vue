<template>
  <div class="news">
    <span>这里是新闻</span>
    <p>当前路径: {{ $route.path }}</p>
  </div>
</template>

<script setup lang="ts" name="News">

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
