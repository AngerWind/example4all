<template>
  <div>
    <div class="row">
      <Banner/>
    </div>
    <div class="row">
      <div class="col-xs-2 col-xs-offset-2">
        <div class="list-group">

          <!-- 可以在路径中添加 queryString 来传递query参数 -->
          <router-link class="list-group-item" active-class="active"
                       :to="`/about?message=${aboutMessage.name}&age=${aboutMessage.age}`">About
          </router-link>

          <!-- 也可以使用如下这种方式来传递 query 参数 -->
          <router-link class="list-group-item" active-class="active"
                       :to="{
                          path: '/home',
                          query: {
                            name: homeMessage.name,
                            age: homeMessage.age
                          }
                       }">
            Home
          </router-link>
        </div>
      </div>
      <div class="col-xs-6">
        <div class="panel">
          <div class="panel-body">
            <!--router-view确定视图的位置-->
            <router-view>
            </router-view>
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
  data() {
    return {
      aboutMessage: {
        name: "zhangsan",
        age: 19
      },
      homeMessage: {
        name: "lisi",
        age: 19
      },
    }
  }
}
</script>
<style lang="css" scoped>

</style>



