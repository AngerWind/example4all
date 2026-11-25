<template>
  <div class="app">
    <h2>错误处理: errorHandler + onErrorCaptured</h2>
    <p v-if="hasError" style="color:red">子组件出错了, 已捕获(页面没有崩)</p>
    <Child v-else/>
  </div>
</template>

<script setup lang="ts" name="App">
  import {ref,onErrorCaptured} from 'vue'
  import Child from './components/Child.vue'

  const hasError = ref(false)

  // onErrorCaptured: 捕获"后代组件"抛出的错误(本组件自己的错误捕获不到)
  // 返回false可以阻止错误继续向上传播(不再触发全局的errorHandler)
  // 常见用途: 给某个区域做局部的错误兜底UI
  onErrorCaptured((err) => {
    console.log('App捕获到后代组件的错误:', err)
    hasError.value = true
    return false
  })

</script>

<style scoped>
</style>
