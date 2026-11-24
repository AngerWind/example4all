<template>
  <div class="news">
    <span>这里是新闻</span>
    <!--
      验证懒加载效果: 打开浏览器Network面板, 清空记录后再点击"新闻"
      可以看到此时才单独下载News组件对应的js文件, 而不是一开始就打进了主包
    -->
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
