<template>
  <div class="app">
    <!-- 导航区: 全部换成自定义的AppLink -->
    <div class="navigate">
      <AppLink to="/home" active-class="active">首页</AppLink>
      <AppLink to="/news" active-class="active">新闻</AppLink>
      <!-- to是外部链接时, AppLink会自动渲染成普通的<a>新窗口打开, 而不是用路由跳转 -->
      <AppLink to="https://cn.vuejs.org">Vue官网(外链)</AppLink>
    </div>
    <!-- 展示区 -->
    <div class="main-content">
      <RouterView></RouterView>
    </div>
  </div>
</template>

<script lang="ts" setup name="App">
  import {RouterView} from 'vue-router'
  import AppLink from '@/components/AppLink.vue'

</script>

<style>
    /* App */
  .navigate {
    display: flex;
    justify-content: space-around;
    margin: 0 100px;
  }
  .navigate a {
    display: block;
    text-align: center;
    width: 160px;
    height: 40px;
    line-height: 40px;
    border-radius: 10px;
    background-color: gray;
    text-decoration: none;
    color: white;
    font-size: 18px;
  }
  .navigate a.active {
    background-color: #64967E;
    color: #ffc268;
    font-weight: 900;
    text-shadow: 0 0 1px black;
    font-family: 微软雅黑;
  }
  .main-content {
    margin: 0 auto;
    margin-top: 30px;
    border-radius: 10px;
    width: 90%;
    height: 400px;
    border: 1px solid;
  }
</style>
