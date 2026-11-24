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
        缓存路由组件: 让路由切走之后组件不被销毁, 切回来的时候还是原来的状态
        1. Vue3中keep-alive不能像以前那样直接包裹RouterView,
           需要通过v-slot拿到当前路由对应的组件Component, 再用<component :is>渲染
        2. include的值是要缓存的组件名(组件的name), 比如下面的"News"
           如果不写include, 则所有经过这个RouterView的组件都会被缓存
        3. 要缓存多个组件就写成数组: :include="['News','Home']"
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
