<template>
  <div class="home">
    <span>这里是home</span>
    <p>当前路径: {{ $route.path }}</p>
  </div>
</template>

<script setup lang="ts" name="Home">

</script>

<style scoped>
  .home {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
  }
</style>
