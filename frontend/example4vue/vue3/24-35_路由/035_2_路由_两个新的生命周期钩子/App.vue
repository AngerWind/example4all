<template>
  <div class="app">
    <!-- 导航区 -->
    <div class="navigate">
      <RouterLink to="/home" active-class="active">首页</RouterLink>
      <RouterLink to="/news" active-class="active">新闻</RouterLink>
    </div>
    <!-- 展示区 -->
    <div class="main-content">
      <!--
        activated/deactivated(激活/失活)这两个生命周期钩子,
        只有在组件被keep-alive缓存的前提下才有意义,
        所以这里依然要用keep-alive缓存News组件
      -->
      <RouterView v-slot="{ Component }">
        <keep-alive include="News">
          <component :is="Component"></component>
        </keep-alive>
      </RouterView>
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
