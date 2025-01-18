<template>
   <h1>当前求和为:{{ sum }}</h1>
   <button @click="add">点我加一</button>
   <hr/>
   <h2>当前点击时鼠标的坐标为x:{{ point.x }}, y:{{ point.y }}</h2>
</template>


<script setup>

import usePoint from "../hooks/usePoint";
import useAdd from "../hooks/useAdd";

const {sum, add} = useAdd() // 复用add功能
const point = usePoint(); // 复用鼠标位置显示功能


</script>

<script>
export default {
  name: "Demo"
}
</script>

<style>
</style>
