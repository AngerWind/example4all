<template>
  <div>

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
}
</script>
<style lang="css" scoped>

</style>



