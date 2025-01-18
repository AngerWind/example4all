<template>
  <div class="child">
    <h2>child(子)</h2>
    <Son/>
  </div>
</template>

<script>
import Son from "./Son";
import { inject } from "vue";

export default {
  name: "Child",
  components: {Son},
  setup() {
    console.log('@@@@ Car ', inject('car')) // 通过inject来接受父组件通过provide传递过来的key="car"的数据
  }
}
</script>

<style scoped>
   .child{
     background: skyblue;
     padding: 10px;
   }
</style>
