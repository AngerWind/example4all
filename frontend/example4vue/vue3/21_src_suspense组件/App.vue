<template>
  <div class="app">
    <h2>app</h2>

    <!-- 异步组件需要使用Suspense包裹 -->
    <Suspense>
      <!-- 异步组件准备好时, 展示的内容 -->
      <template v-slot:default>
        <Child/>
      </template>
      <template v-slot:fallback>
        <!--退路-->
        <h3>loading...</h3>
      </template>
    </Suspense>
  </div>
</template>

<script>
/**
 * 在vue3中, 可以使用import来静态导入组件, 也可以使用defineAsyncComponent(() => import('xxx'))来异步导入一个组件
 * 动态导入和静态导入的区别在于
 *     - 如果是静态导入, 那么需要子组件完全的准备好, 才会显示当前的template
 *     - 如果是动态导入, 那么可以先展示template, 没准备好的子组件先让他慢慢准备, 准备好了再展示
 */

// import Child from "./components/Child"; 静态引入组件

import { defineAsyncComponent } from "vue";
const Child = defineAsyncComponent(() => import('./components/Child')); // 异步导入组件


export default {
  name: 'App',
  components: {Child},
}
</script>

<style>

</style>
