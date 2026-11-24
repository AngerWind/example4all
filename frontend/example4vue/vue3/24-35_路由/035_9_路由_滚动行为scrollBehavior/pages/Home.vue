<template>
  <div class="home">
    <span>这里是home</span>
    <!-- 制造很长的内容, 让展示区可以滚动 -->
    <p v-for="i in 50" :key="i">这是第{{ i }}行内容</p>
  </div>
</template>

<script setup lang="ts" name="Home">

</script>

<style scoped>
</style>
