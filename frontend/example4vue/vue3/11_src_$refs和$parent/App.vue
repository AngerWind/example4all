<template>
  <!-- 指定元素dom的ref -->
  <h2 ref="title2">你好</h2>

  <!-- 指定组件的ref -->
  <Person ref="ren"/>

  <!-- 默认中可以直接使用$refs 来获取所有带ref的子组件 -->
  {{$refs.ref}}
</template>

<script lang="ts" setup name="App">
  import Person from './components/Person.vue'
  import {ref, getCurrentInstance, onMounted} from 'vue'

  let color = ref("red") // 定义要暴露的属性
  let weight = ref(100)
  defineExpose({color, weight}) // 暴露属性, 给子组件使用

  // 当然你也可以通过如下的方式, 来获取所有的定义了ref的子组件
  onMounted(() => {
    // 只能在onMounted中使用, 因为在setup执行的时候, 子组件还没有创建
    console.log(getCurrentInstance().ctx.$refs.title)
    console.log(getCurrentInstance().ctx.$refs.ren.printMessage)
  })


</script>
