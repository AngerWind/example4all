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
        给路由切换加过渡动效:
        1. 通过RouterView的v-slot拿到当前路由组件Component
        2. 用<Transition>包裹<component :is="Component">
        3. name="fade"对应CSS中的fade-enter-active等6个类名
        4. mode="out-in"表示旧组件先过渡完, 新组件再进入, 避免两个组件同时存在
      -->
      <RouterView v-slot="{ Component }">
        <Transition name="fade" mode="out-in">
          <component :is="Component"></component>
        </Transition>
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

  /* 过渡动效的类名, fade对应Transition的name */
  /* 进入的起点、离开的终点 */
  .fade-enter-from,
  .fade-leave-to {
    opacity: 0;
    transform: translateX(20px);
  }
  /* 进入和离开的过程 */
  .fade-enter-active,
  .fade-leave-active {
    transition: all 0.3s ease;
  }
</style>
