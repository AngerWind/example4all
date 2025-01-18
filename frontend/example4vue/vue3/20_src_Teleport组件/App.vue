<template>
  <div class="app">
    <h2>app</h2>
    <Child/>
  </div>
</template>

<script>
import Child from "./components/Child";
import {reactive, toRefs, provide} from "vue";
export default {
  name: 'App',
  components: {Child},
}
</script>

<style>
//不要删除这里的样式
.app{
  background: grey;
  padding: 10px;
}
</style>
