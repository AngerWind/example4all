<template>
  <div class="app">
    <!-- 导航区 -->
    <div class="navigate">
      <RouterLink to="/home" active-class="active">首页</RouterLink>
      <RouterLink to="/news" active-class="active">新闻(被守卫拦截)</RouterLink>
    </div>
    <!-- 操作区 -->
    <div class="operate">
      <button @click="goNews">push跳新闻(被拦截)</button>
      <button @click="goNewsTwice">连续push两次相同路由(重复导航)</button>
    </div>
    <!-- 展示区 -->
    <div class="main-content">
      <RouterView></RouterView>
    </div>
  </div>
</template>

<script lang="ts" setup name="App">
  import {RouterView,RouterLink,useRouter,isNavigationFailure,NavigationFailureType} from 'vue-router'

  const router = useRouter()

  // router.push返回一个Promise:
  //   导航成功时, resolve的值是undefined
  //   导航失败时(被守卫拦截、重复导航等), resolve的值是一个NavigationFailure对象
  // 用isNavigationFailure()可以判断返回的是不是导航故障
  // 用NavigationFailureType可以区分是哪种故障
  async function goNews(){
    const failure = await router.push('/news')
    if(isNavigationFailure(failure)){
      // 类型2(aborted): 导航被守卫中止(守卫返回了false)
      console.log('导航被守卫拦截了', failure, NavigationFailureType[failure.type])
      alert('导航被守卫拦截了(NavigationFailureType.aborted)')
    }else{
      console.log('导航成功')
    }
  }

  async function goNewsTwice(){
    await router.push('/home')
    // 已经在/home了, 再push一次/home, 触发"重复导航"
    const failure = await router.push('/home')
    if(isNavigationFailure(failure)){
      // 类型16(duplicated): 重复导航(目标地址和当前地址一样)
      console.log('重复导航', NavigationFailureType.duplicated)
      alert('重复导航(NavigationFailureType.duplicated)')
    }
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
    width: 180px;
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
