<template>
  <div>

    <div class="row">
      <div class="col-xs-2 col-xs-offset-2">
        <div class="list-group">

          <!--
              浏览器的历史记录是一个stack的形式

              当用户跳转到一个页面的时候, 就往stack中压入新页面的地址, 并且将指针指向栈顶的新页面的地址
              当用户前进后退的时候, 实际上就是移动stack中的指针, 这种模式被称为push模式, 即每访问一个新的页面, 都往其中push一个新记录

              浏览器还有一种模式, 为replace, 即用户每访问一个新的页面, 就将栈顶的最新记录 !!!替换!!! 为新页面的地址
              在这种模式下, 会导致页面无法前进和后退, 因为每一次都是替换, 不会生成历史记录, 指针无法移动

              其实这两种模式可以混合使用, 是push还是replace取决于你点击的router-link中是否有replace这个属性
              如果有的话就是replace模式, 否则就是默认的push模式
          -->

          <!-- 这里跳转到 about不会产生历史记录, 跳转到home 就会产生历史记录 -->
          <router-link class="list-group-item" replace active-class="active" :to="{name: 'about'}">About</router-link>
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



