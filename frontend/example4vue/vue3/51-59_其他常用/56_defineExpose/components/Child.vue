<template>
  <div class="child">
    <h3>我是子组件, count: {{ count }}</h3>
    <h3>secret: 这个数据没有暴露, 父组件拿不到</h3>
  </div>
</template>

<script setup lang="ts" name="Child">
  import {ref} from 'vue'

  const count = ref(0)
  const secret = ref('这是子组件的秘密')

  function increment(){
    count.value += 1
  }

  // <script setup>中的变量和方法默认是"私有"的, 父组件通过ref拿到组件实例也访问不到
  // defineExpose显式指定"哪些东西允许父组件访问"
  defineExpose({
    count,
    increment
    // secret没有暴露, 父组件通过ref拿不到它
  })

</script>

<style scoped>
  .child {
    background-color: skyblue;
    border-radius: 10px;
    padding: 10px;
  }
</style>
