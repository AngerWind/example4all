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

  // !!! ref()的作用主要是父组件 用来获取子组件的实例, 之后父组件就可以调用子组件的方法和属性了

  // 通过ref()函数来获取 被ref属性唯一标识的元素
  // 这里不用传递对应的id来获取, 因为这些变量写在setup函数中, 这些变量会被语法糖自动return, 类似 return {"title2": title2, "ren": ren}
  // 这样vue就可以通过变量名来将对应的变量进行赋值了


  // ref() 函数返回 Ref对象, 如果ref标识的组件是原生dom, 那么他的value就是正式的Element实例
  // 如果ref标识的组件的vue组件, 那么他的value就是VueComponent实例
  let title2 = ref() // 所以要因为title2.value是原生标签, 所以获取到的是真实的dom
  let ren = ref() // 因为Person是一个vue中的组件, 所以ren.value获取到的是VueComponent实例


  // !!!注意你不能再这里直接调用ren.value
  // ren.value.printMessage()  // 这里会报错, 因为根据上面的原理, ref()被调用的时候, 实际上他是返回了的是RefImpl{value: undefined},
  // 因为只有setup结束之后, title2和ren被return之后, 只有执行到onMounted的时候, 才会进行赋值

  function showRef(){
    console.log(title2.value) // 原生dom
    console.log(ren.value) // VueComponent实例

    // 在vue2中, 有ref属性的组件会被放到this.$refs里面, 并且你可以随便使用组件在data, method, computed中定义的属性和方法
    // 但是在vue3中, 你必须在子组件中通过defineExpose()函数定义要暴露的属性和方法, 否则父组件通过ref()获取到VueComponent实例, 也无法子组件调用属性和方法

    // this.$refs.ren.printMessage() // vue2中可以随便调用属性和方法, 无需任何处理
    // vue3中子组件必须调用defineExpose({printMessage}) 来暴露printMessage属性, 然后父组件才可以进行调用
    ren.value.printMessage()  // 父组件调用子组件的方法
    console.log(ren.value.a, ren.value.b) // 父组件调用子组件的属性
  }
</script>
