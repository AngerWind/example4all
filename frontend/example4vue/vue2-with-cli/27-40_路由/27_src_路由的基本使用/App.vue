<template>
  <div>
    <div class="row">
      <div class="col-xs-offset-2 col-xs-8">
        <div class="page-header"><h2>Vue Router Demo</h2></div>
      </div>
    </div>
    <div class="row">
      <div class="col-xs-2 col-xs-offset-2">
        <div class="list-group">
          <!-- 原始使用a标签跳转多个页面,多页面应用 -->
          <!--<a.js class="list-group-item active" href="./about.html">About</a.js>-->
          <!--<a.js class="list-group-item" href="./home.html">Home</a.js>-->


          <!--vue中借助router=link标签实现路由的切换-->
          <!--router-link最后会被变成href标签, active-class表示该标签激活时的class, to表示路由的路径-->
          <router-link class="list-group-item" active-class="active" to="/about">About</router-link>
          <router-link class="list-group-item" active-class="active" to="/home">Home</router-link>
        </div>
      </div>
      <div class="col-xs-6">
        <div class="panel">
          <div class="panel-body">

            <!--router-view表示路由组件的展示位置-->
            <!-- !!!!!!!!!!! 在挂载页面的时候, 会新创建一个VueComponent实例, 如果切走了, 那么会销毁这个VueComponent实例 !!!!!!!!!!!!!!! -->
            <router-view> </router-view>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "App",
}
</script>
<style lang="css" scoped>

</style>



