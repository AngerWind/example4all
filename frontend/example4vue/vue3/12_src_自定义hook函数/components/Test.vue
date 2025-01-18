<template>
   <h2>我是test组件</h2>
   <h3>{{ point }}</h3>
</template>

<script>
export default {
  name: "Test",
}
</script>

<script setup>

import usePoint from "../hooks/usePoint";
const {point} = usePoint(); // 复用一个功能

</script>

<style scoped>

</style>
