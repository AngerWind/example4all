<template>
  <div>
    <div class="row">
      <Banner/>
    </div>
    <div class="row">
      <div class="col-xs-2 col-xs-offset-2">
        <div class="list-group">

          <router-link class="list-group-item" active-class="active" :to="{name: 'about'}">About</router-link>
          <router-link class="list-group-item" active-class="active" to="/home">Home</router-link>

        </div>
      </div>
      <div class="col-xs-6">
        <div class="panel">
          <div class="panel-body">

            <!--include的值代表要缓存的组件，比如下面代表在App组件中要缓存Home组件(组件名, 就是export里面的那个name定义的组件名)-->
            <!--如果要缓存多个路由组件就改写为:include="['About', 'Home']"-->
            <keep-alive include="Home">
              <router-view>
              </router-view>
            </keep-alive>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Banner from "./components/Banner";
export default {
  name: "App",
  components: {Banner},
}
</script>
<style lang="css" scoped>

</style>



