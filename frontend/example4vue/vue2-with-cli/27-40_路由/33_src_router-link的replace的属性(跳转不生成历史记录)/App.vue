<template>
  <div>

    <div class="row">
      <div class="col-xs-2 col-xs-offset-2">
        <div class="list-group">

          <!--
              浏览器的历史记录可以简单理解为一个stack，指针指向当前正在浏览的记录。
              push：新增一条记录，是router-link默认的跳转方式。
              replace：替换当前记录，不新增记录，也不会清空其他历史记录。

              案例一：第二次跳转使用push

              1. 打开A页面
                 stack: [A]
                 当前记录: A

              2. 第一次跳转，使用push从A跳转到B
                 stack: [A, B]
                 当前记录: B

              3. 第二次跳转，使用push从B跳转到C
                 stack: [A, B, C]
                 当前记录: C

              4. 点击浏览器后退
                 C仍然在栈中，指针从C移动到B，所以回到B页面。


              案例二：第二次跳转使用replace

              1. 打开A页面
                 stack: [A]
                 当前记录: A

              2. 第一次跳转，使用push从A跳转到B
                 stack: [A, B]
                 当前记录: B

              3. 第二次跳转，使用replace从B跳转到C
                 C会替换当前的B，而不是新增一条记录。
                 stack: [A, C]
                 当前记录: C

              4. 点击浏览器后退
                 指针从C移动到A，所以回到A页面。
                 B已经被C替换，无法再通过浏览器后退回到B。
          -->

          <!-- 跳转到About不会新增历史记录；跳转到Home会新增一条历史记录 -->
          <router-link class="list-group-item" replace active-class="active" :to="{name: 'regard'}">About</router-link>
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



