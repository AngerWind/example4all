<template>
  <div class="news">
    <span>这里是新闻</span>
    <!--
      验证滚动行为:
      1. 在home页把内容滚动到中部, 点击"新闻", 新闻页会回到顶部(scrollBehavior中返回了top:0)
      2. 再点击浏览器的前进/后退按钮, 页面会恢复之前记住的滚动位置(savedPosition)
    -->
    <p v-for="i in 50" :key="i">这是第{{ i }}条新闻</p>
  </div>
</template>

<script setup lang="ts" name="News">

</script>

<style scoped>
</style>
