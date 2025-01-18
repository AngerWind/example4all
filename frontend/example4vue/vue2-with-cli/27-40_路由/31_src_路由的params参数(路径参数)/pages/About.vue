<template>
  <h2>我是About的内容</h2>

  <!-- 通过 $route.params 来获取 路径参数 -->
  {{$route.params.id}}
  {{$route.params.title}}
</template>

<script>
export default {
  name: "About",
}
</script>

<style scoped>

</style>
