<template>
  <div class="news">
    <span>这里是新闻</span>
    <!--
      验证缓存效果: 在输入框里输入内容, 切到首页再切回来
      不加keep-alive: 组件被销毁重建, 输入的内容丢失
      加了keep-alive: 组件只是失活, 输入的内容还在
    -->
    <input type="text" v-model="keyword" placeholder="输入点什么, 再切换路由试试">
  </div>
</template>

<script setup lang="ts" name="News">
  import {ref} from 'vue'

  const keyword = ref('')

</script>

<style scoped>
  .news {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
    gap: 20px;
  }
</style>
