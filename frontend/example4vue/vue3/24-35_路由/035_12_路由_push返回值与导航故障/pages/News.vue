<template>
  <div class="news">
    <span>这里是新闻(正常情况进不来, 会被全局守卫拦截)</span>
  </div>
</template>

<script setup lang="ts" name="News">

</script>

<style scoped>
  .news {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
  }
</style>
