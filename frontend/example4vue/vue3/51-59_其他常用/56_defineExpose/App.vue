<template>
  <div class="app">
    <h2>defineExpose: 父组件通过ref调用子组件暴露的方法</h2>
    <button @click="callChild">点我让子组件的count+1</button>
    <button @click="getChildData">点我获取子组件的数据</button>
    <!-- 通过ref拿到子组件实例 -->
    <Child ref="childRef"/>
  </div>
</template>

<script setup lang="ts" name="App">
  import {ref} from 'vue'
  import Child from './components/Child.vue'

  const childRef = ref<InstanceType<typeof Child>>()

  function callChild(){
    // 调用子组件通过defineExpose暴露出来的方法
    childRef.value?.increment()
  }

  function getChildData(){
    // 读取子组件暴露出来的数据
    alert(`子组件的count是: ${childRef.value?.count}`)
  }

</script>

<style scoped>
</style>
