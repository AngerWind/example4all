<template>
  <div>
  </div>
</template>

<script>

export default {
  name: "App",
  components: {},
}
</script>

<style scoped>
/*
  如果不加scoped, 那么这里写的样式会影响整个页面
  如果添加了scoped, 那么vue在编译的时候, 会给当前组件的所有元素都加上一个相同的data-v-xxx属性 (子组件只有最外一层会添加)
  同时给当前组件的所有样式, 都添加一个 [data-v-xxx]属性选择器
 */
.title {
  color: red;
}
</style>


