<template>
  <div class="app">
    <h2>app(祖) {{ name }}---{{ price }}</h2>
    <Child/>
  </div>
</template>

<script>
import Child from "./components/Child";
import {reactive, toRefs, provide} from "vue";
export default {
  name: 'App',
  components: {Child},
  setup(){
    let car = reactive({
      name: 'benz',
      price: 40
    });

    // !!! provide 负责在祖先组件提供数据，inject 负责在任意后代组件接收数据，适合跨层级共享数据，可以避免多层 props 传递。

    // 给自己的 **后代** 组件传递一个key="car", value=car的数据
    // 之后Child和Son组件都可以通过inject来获取到这个kv了
    provide('car', car);
    return {
      ...toRefs(car)
    }
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
