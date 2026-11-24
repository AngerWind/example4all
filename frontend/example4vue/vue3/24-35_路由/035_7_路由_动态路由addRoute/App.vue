<template>
  <div class="app">
    <!-- 导航区 -->
    <div class="navigate">
      <RouterLink to="/home" active-class="active">首页</RouterLink>
      <RouterLink to="/news" active-class="active">新闻</RouterLink>
      <!-- admin路由默认不存在, 点这里会显示"路由不存在" -->
      <RouterLink to="/admin" active-class="active">后台</RouterLink>
    </div>
    <!-- 操作区 -->
    <div class="operate">
      <button @click="addAdminRoute">动态添加admin路由</button>
      <button @click="removeAdminRoute">移除admin路由</button>
      <button @click="showAllRoutes">控制台查看所有路由</button>
    </div>
    <!-- 展示区 -->
    <div class="main-content">
      <RouterView></RouterView>
    </div>
  </div>
</template>

<script lang="ts" setup name="App">
  import {RouterView,RouterLink,useRouter} from 'vue-router'
  import Admin from '@/pages/Admin.vue'

  const router = useRouter()

  // 动态路由: 在运行时向路由器中添加/删除路由
  // 典型场景: 后台管理系统中, 登录后根据用户权限动态生成可访问的路由
  function addAdminRoute(){
    // addRoute返回一个"删除该路由"的函数
    router.addRoute({
      name:'admin',
      path:'/admin',
      component:Admin
    })
    alert('admin路由添加成功, 再点击上方"后台"试试')
  }

  function removeAdminRoute(){
    // 通过路由的name来移除
    router.removeRoute('admin')
    alert('admin路由已移除')
  }

  function showAllRoutes(){
    // getRoutes可以拿到当前路由器中的所有路由
    console.log(router.getRoutes())
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
  .operate {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-top: 20px;
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
