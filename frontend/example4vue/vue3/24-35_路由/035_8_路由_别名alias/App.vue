<template>
  <div class="app">
    <!-- 导航区 -->
    <div class="navigate">
      <!-- /和/home/index都是/home的别名, 都能访问到首页 -->
      <RouterLink to="/home" active-class="active">首页</RouterLink>
      <RouterLink to="/home/index" active-class="active">首页(别名)</RouterLink>
      <RouterLink to="/news" active-class="active">新闻</RouterLink>
      <RouterLink to="/xw" active-class="active">新闻(别名)</RouterLink>
    </div>
    <!-- 展示区 -->
    <div class="main-content">
      <RouterView></RouterView>
    </div>
  </div>
</template>

<script lang="ts" setup name="App">
  import {RouterView,RouterLink} from 'vue-router'

</script>

<style>
    /* App */
  .navigate {
    display: flex;
    justify-content: space-around;
    margin: 0 50px;
  }
  .navigate a {
    display: block;
    text-align: center;
    width: 130px;
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
