<template>
  <div class="app">

    <button @click="change">点我进行编程式路由</button>
    <!-- 导航区 -->
    <div class="navigate">
      <RouterLink replace to="/home" active-class="active">首页</RouterLink>
      <RouterLink replace :to="{name:'xinwen'}" active-class="active">新闻</RouterLink>
      <RouterLink replace :to="{path:'/about'}" active-class="active">关于</RouterLink>
    </div>
    <!-- 展示区 -->
    <div class="main-content">
      <RouterView></RouterView>
    </div>
  </div>
</template>

<script lang="ts" setup name="App">
  import {RouterView,RouterLink, useRouter} from 'vue-router'

  // 获取项目的路由器
  const router = useRouter()

  // 通过代码来进行路由
  function change(){
    router.push({ // 通过push模式来路由, 你也可以通过调用replace方法来进行replace方式的路由
      name: "xinwen",
      query:{

      },
      params: {

      }
    })
  }

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
