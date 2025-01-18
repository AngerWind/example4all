<template>
   <ul>
     <li>消息编号:{{ id }}</li>
     <li>消息标题:{{ title }}</li>
   </ul>
</template>

<script>
export default {
  name: "Detail",
  // 在这里接受路由的参数
  props: ['id', 'title', "a", "b"]
}
</script>

<style scoped>

</style>
