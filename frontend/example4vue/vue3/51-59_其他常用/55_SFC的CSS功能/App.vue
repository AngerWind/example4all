<template>
  <div class="app">
    <h2 class="title">SFC的CSS功能</h2>
    <button @click="themeColor = themeColor === 'red' ? 'blue' : 'red'">切换主题色</button>
    <Child/>
  </div>
</template>

<script setup lang="ts" name="App">
  import {ref} from 'vue'
  import Child from './components/Child.vue'

  // 这个响应式数据会被下面的v-bind()用在CSS里
  const themeColor = ref('red')

</script>

<style scoped>
  /*
    scoped: 样式只作用于当前组件
    原理: 编译后会给模板中的元素加上data-v-xxx属性, 选择器也会带上这个属性选择器,
          所以不会"污染"到其他组件
  */
  .title {
    color: green;
  }

  /*
    CSS中的v-bind(): 把CSS的值和组件的响应式数据绑定
    themeColor变化时, 样式自动更新, 不用再写:class动态类名
  */
  .app {
    border: 3px solid v-bind(themeColor);
    padding: 10px;
  }

  /*
    :deep(): 深度选择器
    scoped下, 父组件的样式默认影响不到子组件内部的元素
    想从父组件改子组件内部的样式(比如改第三方组件库的样式), 就用:deep()
  */
  :deep(.child-title) {
    color: v-bind(themeColor);
  }
</style>
