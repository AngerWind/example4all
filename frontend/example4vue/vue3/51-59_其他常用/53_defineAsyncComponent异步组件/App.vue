<template>
  <div class="app">
    <h2>异步组件defineAsyncComponent</h2>
    <button @click="showChild = true">加载Child组件</button>
    <!--
      Child是异步组件:
      不点按钮时, Child的js文件根本没有下载;
      点了按钮要渲染它时, 才去下载对应的js并渲染(可以打开Network面板验证)
      打包时Child会被单独打成一个js文件(代码分割)
    -->
    <Child v-if="showChild"/>
  </div>
</template>

<script setup lang="ts" name="App">
  import {ref,defineAsyncComponent} from 'vue'

  // 静态引入: Child的代码会被打进主包, 不管用不用得到
  // import Child from './components/Child.vue'

  // defineAsyncComponent: 定义一个异步组件, 参数是返回import()的函数
  // 适合: 弹窗、复杂编辑器、图表库这种"不是首屏必需、体积又大"的组件
  const Child = defineAsyncComponent(() => import('./components/Child.vue'))

  const showChild = ref(false)

</script>

<style scoped>
</style>
