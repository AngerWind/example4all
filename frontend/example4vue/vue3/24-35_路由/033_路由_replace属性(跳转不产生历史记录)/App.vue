<template>
  <div class="app">
    <Header/>

    <div class="navigate">

      <!-- 通过replace来指定使用replace模式 -->
      <!-- 如果不指定replace模式, 那么就会使用push模式 -->
      <!-- 关于replace和push的差别, 可以查看vue2中的讲解 -->
      <RouterLink replace to="/home" active-class="active">首页</RouterLink>
      <RouterLink replace :to="{name:'xinwen'}" active-class="active">新闻</RouterLink>

    </div>

    <div class="main-content">
      <RouterView></RouterView>
    </div>
  </div>
</template>

<script lang="ts" setup name="App">
  import {RouterView,RouterLink} from 'vue-router'
  import Header from './components/Header.vue'

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
    width: 90px;
    height: 40px;
    line-height: 40px;
    border-radius: 10px;
    background-color: gray;
    text-decoration: none;
    color: white;
    font-size: 18px;
    letter-spacing: 5px;
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
