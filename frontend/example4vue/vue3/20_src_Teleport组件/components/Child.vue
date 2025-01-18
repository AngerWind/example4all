<template>
  <div class="child">
    <h2>child(子)</h2>
    <Son/>
  </div>
</template>

<script>
import Son from "./Son";
export default {
  name: "Child",
  components: {Son},
  setup() {
  }
}
</script>

<style scoped>
//不要删除这里的样式
   .child{
     background: skyblue;
     padding: 10px;
   }
</style>
