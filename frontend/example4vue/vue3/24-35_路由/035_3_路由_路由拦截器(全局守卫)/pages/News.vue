<template>
  <div class="news">
    <span>这里是新闻</span>
    <!--
      此页面需要权限才能访问(meta.isAuth为true),
      在控制台执行 localStorage.setItem('school','atguigu') 后再点击新闻, 就可以进入了
    -->
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
