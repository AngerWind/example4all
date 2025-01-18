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

          <button @click="pushShow">push查看</button>
          <button @click="replaceShow">replace查看</button>
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
  methods:{
    pushShow(){
      // 可以通过$router来进行路由的跳转
      this.$router.push({
        //这里与router-link的to属性配置形式是一样的
        name: 'about',
        query:{

        }
      })
    },
    replaceShow(){
      this.$router.replace({
        //这里与router-link的to属性配置形式是一样的
        name: 'particulars',
        query:{
        }
      })
    }
  }
}
</script>
<style lang="css" scoped>

</style>



