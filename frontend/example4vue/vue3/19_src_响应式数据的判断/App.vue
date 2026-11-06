<template>
  <div class="app">
  </div>
</template>

<script>
import {reactive, readonly, ref, toRefs, isRef, isReactive, isReadonly, isProxy} from "vue";
export default {
  name: 'App',
  setup(){

    let car = reactive({
      name: '宝马',
      price: 40
    });

    let sum = ref(0);

    let car2 = readonly(car); ///readonly依然返回代理类型的对象只不过它不能再改而已

    // isRef(value)：判断 value 是否是一个 ref 对象
    // isReactive(value)：判断 value 是否是一个响应式代理对象
    // isReadonly(value)：判断 value 是否是一个只读代理对象
    // isProxy(value)：判断 value 是否是由 reactive()、readonly() 等创建的代理对象，ref 对象不属于此类
    // 下面的判断结果依次为：true、true、true、true、true、false
    console.log(isRef(sum), isReactive(car), isReadonly(car2), isProxy(car), isProxy(car2), isProxy(sum));

  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.app{
  background: grey;
  padding: 10px;
}
</style>
