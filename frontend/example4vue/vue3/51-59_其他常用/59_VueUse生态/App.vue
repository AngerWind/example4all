<template>
  <div class="app">
    <h2>VueUse: 现成的组合式函数库</h2>

    <h3>useMouse(鼠标位置): x={{ x }}, y={{ y }}</h3>

    <h3>
      useLocalStorage(自动同步到localStorage的ref):
      <input v-model="name">
    </h3>

    <h3>useNow(实时时间): {{ now }}</h3>

    <h3>
      useDark(暗黑模式):
      <button @click="toggleDark()">当前是{{ isDark ? '暗' : '亮' }}, 点我切换</button>
    </h3>
  </div>
</template>

<script setup lang="ts" name="App">
  // VueUse: 一个收集了大量常用组合式函数(hooks)的第三方库
  // 安装: npm install @vueuse/core
  // 我们自己写自定义hook(12章)做的事情, 它基本都做好了, 直接拿来用
  import {useMouse,useLocalStorage,useNow,useDark,useToggle} from '@vueuse/core'

  // useMouse: 实时返回鼠标的x/y坐标, 是响应式的
  const {x,y} = useMouse()

  // useLocalStorage: 创建一个ref, 自动和localStorage双向同步, 刷新不丢
  const name = useLocalStorage('my-name','默认值')

  // useNow: 返回当前时间的响应式ref
  const now = useNow()

  // useDark: 检测/切换暗黑模式, 自动给html加dark类
  const isDark = useDark()
  const toggleDark = useToggle(isDark)

</script>

<style scoped>
</style>
