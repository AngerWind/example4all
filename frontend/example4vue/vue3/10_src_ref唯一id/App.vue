<template>
  <!-- 指定元素dom的ref -->
  <h2 ref="title2">你好</h2>

  <!-- 指定组件的ref -->
  <Person ref="ren"/>

  <button @click="showRef">测试</button>
</template>

<script lang="ts" setup name="App">
  import Person from './components/Person.vue'
  import {ref} from 'vue'

  // 通过ref()函数来获取被ref标识的唯一id的元素
  let title2 = ref() // 因为title2.value是原生标签, 所以获取到的是真实的dom
  let ren = ref() // 因为Person是一个vue中的组件, 所以ren.value获取到的是VueComponent实例

  function showRef(){
    console.log(title2.value) // 原生dom
    console.log(ren.value) // VueComponent实例

    // 在vue2中, 有ref属性的组件会被放到this.$refs里面, 并且你可以随便使用组件在data, method, computed中定义的属性和方法
    // 但是在vue3中, 你必须在子组件中通过defineExpose()函数定义要暴露的属性和方法, 否则父组件通过ref()获取到VueComponent实例, 也无法子组件调用属性和方法

    // this.$refs.ren.printMessage() // vue2中可以随便调用属性和方法, 无需任何处理
    ren.printMessage() // vue3中子组件必须调用defineExpose({printMessage}) 来暴露printMessage属性
    console.log(ren.value.a, ren.value.b)
  }
</script>
