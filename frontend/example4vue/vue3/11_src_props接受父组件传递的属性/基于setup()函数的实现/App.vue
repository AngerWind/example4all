<template>

  <!-- 通过属性给子组件传递参数 -->
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
