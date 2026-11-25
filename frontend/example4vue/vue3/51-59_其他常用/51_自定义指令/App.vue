<template>
  <div class="app">
    <h2>自定义指令</h2>

    <!-- 使用全局指令v-demo(在main.ts中注册): 打开控制台, 切换下面的v-if, 观察7个钩子的执行顺序 -->
    <p v-if="show" v-demo="n">我在演示指令的生命周期</p>
    <button @click="show = !show">v-if切换元素的创建/销毁</button>

    <!-- 使用全局指令v-focus(在main.ts中注册): 页面加载后这个输入框自动聚焦 -->
    <input type="text" v-focus>

    <!-- 使用全局指令v-big: n是3, 字体显示为30px -->
    <h3 v-big="n">这个标题的字体会被放大</h3>
    <button @click="n += 1">n+1(当前n={{ n }}, 触发指令的beforeUpdate/updated钩子)</button>

    <!-- 使用局部指令v-red(在本组件的script中定义): 文字变红 -->
    <h3 v-red>这个标题是红色的(局部指令)</h3>

    <!--
      自定义指令什么时候用?
      当要"直接操作DOM"的场景: 聚焦、复制到剪贴板、权限控制(没权限直接remove元素)、防重复点击等
      常见指令: v-focus / v-copy / v-permission / v-debounce
    -->
  </div>
</template>

<script setup lang="ts" name="App">
  import {ref} from 'vue'

  const n = ref(3)
  const show = ref(true)

  // ==================== 局部自定义指令 ====================
  // 在<script setup>中, 定义一个v开头、驼峰命名的变量即可, 模板中用v-red引用(驼峰vRed对应v-red)
  //
  // 局部指令和全局指令的7个生命周期钩子完全一样:
  //   created / beforeMount / mounted / beforeUpdate / updated / beforeUnmount / unmounted
  // 区别只在注册方式和作用范围:
  //   全局: app.directive('red', {...})注册在main.ts中, 所有组件可用
  //   局部: 定义在组件内部, 只有当前组件能用
  const vRed = {
    // 元素插入DOM后把文字变红
    mounted(el: HTMLElement) {
      console.log('vRed-mounted(局部指令的钩子和全局的一样)')
      el.style.color = 'red'
    },
    // 元素卸载时执行清理
    unmounted(el: HTMLElement) {
      console.log('vRed-unmounted')
    }
  }

</script>

<style scoped>
</style>
