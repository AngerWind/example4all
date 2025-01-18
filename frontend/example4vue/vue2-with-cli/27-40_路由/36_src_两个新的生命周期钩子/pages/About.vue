<template>
  <h2>我是About的内容</h2>
</template>

<script>
export default {
  name: "About",
  //激活(路由组件独有的两个钩子)
  activated() {
    console.log('About组件被激活');
  },
  //失活
  deactivated() {
    console.log('About组件失活了');
  }
}
</script>

<style scoped>

</style>
