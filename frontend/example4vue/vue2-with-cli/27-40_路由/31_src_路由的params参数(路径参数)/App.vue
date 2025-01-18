<template>
  <div>
    <div class="row">
      <div class="col-xs-2 col-xs-offset-2">
        <div class="list-group">

          <!-- 通过路径传递参数 -->
          <router-link class="list-group-item" active-class="active"
                       :to="`/about/${aboutMessage.id}/${aboutMessage.title}`">About
          </router-link>
          <router-link class="list-group-item" active-class="active"
                       :to="{
                          name: 'home', // 这里只能指定name了, 不能指定path
                          params: { // parmas里面传递的参数不能是对象和数组, 因为这里是路径参数
                            id: homeMessage.id,
                            title: homeMessage.title
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

export default {
  name: "App",
  data() {
    return {
      aboutMessage: {
        id: "001",
        title: "about page"
      },
      homeMessage: {
        id: "002",
        title: "home page"
      },
    }
  }
}
</script>
<style lang="css" scoped>

</style>



