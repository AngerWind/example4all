<script setup lang="ts">
// 接受父组件透传过来的爷爷组件的 props
let props = defineProps(["name", "age", "x", "y"]);
console.log(props.name, props.age, props.x, props.y)

</script>

<template>
  <!--  模板中不需要使用 props.name, 可以直接使用 -->
  {{ name }} -- {{ age }}
</template>

<style scoped>

</style>