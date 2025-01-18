<template>
  <!--vue3的组件模版结构可以没有根标签-->
  <Demo @hello="showHelloMsg" msg="hello" school="wust">
    <template v-slot:qwe>
      没事
    </template>
    <template v-slot:asd>
      love you
    </template>
  </Demo>
</template>

<script>
import Demo from "./components/Demo";
export default {
  name: 'App',
  components: {Demo},
  setup(props, context){
    // setup函数在生命周期中是最先执行的, 早于beforeCreate
    // 所以vue3中定义的数据, 在vue2的代码中可以使用, 但是在setup中不能使用data(){}中定义的属性

    // setup中的this是undefined, 因为在vue3中, 所有的数据都应该定义在setup中, 所以也就不需要通过this来调用数据了

    return {
      showHelloMsg(value){
        alert(`你好,${value}`);
      }
    }
  }
}
</script>

<style>

</style>
