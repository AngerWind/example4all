<template>
  <div class="person">
    这里是person的内容
  </div>
</template>

<script lang="ts" setup name="Person">
  import {ref} from 'vue'
  import {reactive} from 'vue'

  let a = ref(1)
  let b = reactive({
    name: "zhangsan",
    age: 18
  })
  function printMessage(){
    console.log(`a: ${a.value}, b: ${b.name}, ${b.age}`)
  }
  // 必须通过defineExpose, 才可以将属性放到VueComponent实例上
  // 然后父组件通过ref()来获取子组件的VueComponent实例, 然后才可以调用其中的属性
  defineExpose({a,b,printMessage})
</script>

<style scoped>

</style>