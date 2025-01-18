<template>
  <!-- 通过这种方式, 将所有子组件不接受的 props 传递给孙子组件, 实现父孙之间的参数传递 -->
  <!-- 然后可以在孙子组件中通过 defineProps 来接受这些参数 -->
  <GrandSon v-bind="$attrs"></GrandSon>
</template>

<script setup lang="ts">
import {onMounted, useAttrs} from "vue";
import GrandSon from "@/components/GrandSon.vue";

// 接受父组件传递过来的 props
// let props = defineProps(["name", "age", "x", "y"]);

// 如果你不接受父组件传递过来的 props, 那么他会出现在 $attrs属性里面
onMounted(() => {
  const attrs = useAttrs() // 获取所有的attrs
  console.log(attrs.name, attrs.age, attrs.x, attrs.y) // zhangsan 18 100 200
})
</script>


<style scoped>

</style>