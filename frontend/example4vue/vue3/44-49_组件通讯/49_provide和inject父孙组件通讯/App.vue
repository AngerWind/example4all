<template>
  <Child></Child>
</template>

<script setup lang="ts">
import {provide, reactive, ref} from "vue";
import Child from "../备份/44_props父子组件通讯/components/Child.vue";

let money = ref(100)
let car = reactive({
  brand: "奔驰",
  price: 100
})

// 向后代提供数据
provide("money", money)
provide("car", car)
</script>


<style scoped>

</style>
