<template>

  <Demo @hello="showHelloMsg" msg="hello" school="wust">
    <template v-slot:qwe>
      没事
    </template>
    <template v-slot:asd>
      love you
    </template>
  </Demo>

  <!-- 直接使用setup中的数据和方法 -->
  <span @click="showHelloMsg">{{a}}</span>
</template>

<!--
  vue3中提供了setup语法糖,  你可以在<script>标签上标注一个 setup
  然后在script中编写的代码就等于在setup()中编写的代码, 同时也不需要再setup中return一个对象, 然后才可以在模板中调用属性和方法了
-->
<script setup lang="js">
import { ref, reactive } from 'vue';
import Demo from "./components/Demo"; // 使用到的组件不再需要再components中注册了, 可以直接在模板中使用

let a = ref(0) // 不需要在setup(){}将属性return出去了, 可以直接在模板中使用这个属性
function showHelloMsg(value) { // 可以在模板中直接使用这个方法
  alert(`你好,${value}`);
}
</script>

<!-- 不使用setup语法糖的形式 -->
<!--
<script>
import Demo from "./components/Demo";
export default {
  name: 'App',
  components: {Demo},
  setup(props, context){
    return {

    }
  }
}
</script>
-->

<style>

</style>
