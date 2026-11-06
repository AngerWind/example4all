<template>
  <div class="app">
    <h2 class="title">Vue路由测试</h2>
    <!-- 导航区 -->
    <div class="navigate">

      <!-- 注意, 这里已经从vue2的 router-link 标签变成了 RouterLink 标签了 -->
      <!-- 通过to来指定路由的路径, 然后会通过路径去匹配对应的组件 -->
      <RouterLink to="/home" active-class="xiaozhupeiqi">首页</RouterLink>
      <RouterLink to="/news" active-class="xiaozhupeiqi">新闻</RouterLink>
      <RouterLink to="/about" active-class="xiaozhupeiqi">关于</RouterLink>

      <!-- RouterLink最后实际上会被转换为a标签, 所以可以在RouterLink上面添加class, 然后在css中添加类选择器,
           或者也可以直接使用a标签来选中RouterLink 来给RouterLink加样式
            也可以直接在RouterLink上面直接加style来设置css -->
      <!-- active-class这个属性, 会在标签被点击的时候, 加上class这个属性上 -->

      <!-- <RouterLink to="/home" class="nav-link aa bb" active-class="nav-link-active" > 首页 </RouterLink> -->
      <!-- 转换后为 -->
      <!-- <a href="/home" class="nav-link aa bb nav-link-active">首页</a> -->

    </div>
    <!-- 展示区, RouterView最后渲染是不固定的, 所以推荐在外面包一层 -->
    <div class="main-content">

      <!-- 注意, 这里已经从 vue2 的 router-view 标签变为了 RouterView 标签了 -->
      <!-- 一般情况下都不会给RouterView加样式, 如果要在外部加样式的话, 都是在外面包的这一层中写样式, 然后组件自己的样式就卸载组件自己上面 -->
      <!-- 如果在RouterView上面强行加class 也不是不行, 但是渲染之后他会把class加在子组件的第一个根节点上面 -->
      <RouterView></RouterView>
    </div>
  </div>
</template>

<script lang="ts" setup name="App">
// 注意, 这里需要导入RouterView和RouterLink组件
// 在vue2中是不需要导入的, 可以直接使用
import {RouterView, RouterLink} from 'vue-router'

</script>

<style>
/* App */
.title {
  text-align: center;
  word-spacing: 5px;
  margin: 30px 0;
  height: 70px;
  line-height: 70px;
  background-image: linear-gradient(45deg, gray, white);
  border-radius: 10px;
  box-shadow: 0 0 2px;
  font-size: 30px;
}

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

.navigate a.xiaozhupeiqi {
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
