<template>
  <div>
    <h2>我是Home的内容</h2>
    <div>
      <ul class="nav nav-tabs">
        <li>
          <!--路由按钮-->
          <router-link class="list-group-item" active-class="active" to="/home/news">News</router-link>
        </li>
        <li>
          <router-link class="list-group-item" active-class="active" to="/home/message">Message</router-link>
        </li>
      </ul>
      <!--路由组件的展示位置-->
      <router-view>
      </router-view>
    </div>
  </div>
</template>

<script>
export default {
  name: "Home",
  // 路由页面被切换了, vc实例就会被销毁
  // 如果重新路由到组件, 又会创建一个新的vc实例, 进行重新的挂载
  mounted() {
    console.log('Home组件挂载完毕', this);
    window.homeRoute = this.$route;
    window.homeRouter = this.$router;
  },
  beforeDestroy() {
    console.log('Home组件将要被销毁');
  }
}
</script>

<style scoped>
</style>
