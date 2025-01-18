<template>
  <!--之前说了, 在vue3中, template可以有多个根标签-->
  <!--实际上在编译之后, 如果template有多个根标签, 那么会用一个Fragment组件来包裹 -->
  <!-- 我们可以在vue3的开发插件中看到, 组件名边上会显示一个fragment字样 -->
  <img src="img.png"/>

  <h1>haha</h1>
  <h2>hahah</h2>
</template>

<script>


export default {
  name: 'App',
}
</script>

<style>

</style>
