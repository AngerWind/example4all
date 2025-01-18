<template>

  <!-- 向子组件发送 参数 -->
  <!-- v-bind="{x:100, y:200}" 等效于 :x=100 :y=200 -->
  <Child :name="name" age="18" v-bind="{x:100, y:200}"></Child>
</template>

<script setup lang="ts">

import Child from "./components/Child.vue";
import {ref} from "vue"

let name = ref("zhangsan")
</script>



<style scoped>

</style>
