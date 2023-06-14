<template>
  <div>
    <h1>当前求和为: {{ $store.store.sum }}</h1>
    <!--让其收集到的数据全是number类型的 number修饰符-->
    <h3>当前求和放大3倍为:{{ $store.getters.bigSum }}</h3>
    <select v-model.number="n">
      <!--让所有的value全部绑定为数字-->
      <option value="1">1</option>
      <option value="2">2</option>
      <option value="3">3</option>
    </select>
    <button @click="increment(n)">+</button>
    <button @click="decrement(n)">-</button>
    <button @click="incrementIfOdd(n)">当前求和为奇数再加</button>
    <button @click="incrementWait(n)">等一等再加</button>
  </div>
</template>

<script>
export default {
  //计数组件
  name: "Count",
  data(){
    return {
      n: 1,// 代表用户在select框开始的时候选择的数字
    }
  },
  methods:{
    increment(){
      // 直接调用mutations中的操作
      this.$store.commit('INCREMENT', this.n);
    },
    decrement(){
      this.$store.commit('DECREMENT', this.n);
    },
    incrementIfOdd(){
      // 调用actions中的操作
      this.$store.dispatch('incrementIfOdd', this.n);
    },
    incrementWait(){
      this.$store.dispatch('incrementWait', this.n);
    }
  },
}
</script>

<style scoped>
   button{
     margin-left: 5px;
   }
</style>
