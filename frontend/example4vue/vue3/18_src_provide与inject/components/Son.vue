<template>
  <div class="son">
    <h2>son(孙)</h2>
    <h3>从app组件获取的数据:{{ car.name }} --- {{ car.price }}</h3>
  </div>
</template>

<script>
import { inject } from "vue";
export default {
  name: "Son",
  setup(){
    let car = inject('car'); // // 通过inject来接受祖先组件通过provide传递过来的key="car"的数据
    console.log(car); //响应式数据
    return {
      car
    }
  }
}
</script>

<style scoped>
  .son{
    background: orange;
    padding: 10px;
  }
</style>
