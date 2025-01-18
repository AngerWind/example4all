<template>
  <!--vue3的组件模版结构可以没有根标签-->
  <h1>我是app组件</h1>
  <h1>我叫{{ person.name }}, {{ person.age }}岁</h1>
  <button @click="test">测试触发一次demo的自定义事件</button>
</template>

<script>
import { reactive } from 'vue';
export default {
  name: 'Demo',
  props: ['msg', 'school'], // 定义要接受的参数
  emits:['hello'], // 定义当前组件会触发的事件类型

  // setup可以接受两个参数, props和context
  // context可以用来触发自定义事件
  setup(props, context){
    // props参数, 可以接受上面props中定义的参数
    console.log(props) // {"msg": "hello", "school": "wust"}

    function test(){
      context.emit('hello', 666); // 触发自定义事件
    }

    //返回一个对象
    return {
      person,
      test
    }
  }
}
</script>

<style>
</style>
