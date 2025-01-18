<template>
  <div class="son">
    <h2>son(孙)</h2>
    <Dialog/>
  </div>
</template>

<script>
import Dialog from "./Dialog";
export default {
  name: "Son",
  components: {Dialog}
}
</script>

<style scoped>
//不要删除这里的样式
  .son{
    background: orange;
    padding: 10px;
  }
</style>
