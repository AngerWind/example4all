<template>
<!--  模板中可以直接使用, 而不需要通过 props.name 来使用 -->
{{name}} -- {{age}}
</template>

<script setup lang="ts">
import {onMounted} from "vue";

// 接受父组件传递过来的 props
let props = defineProps(["name", "age", "x", "y"]);
onMounted(() => {
  // 接受过来的 props 是不能修改的
  console.log(`${props.name}, ${props.age}`)
})
</script>


<style scoped>

</style>