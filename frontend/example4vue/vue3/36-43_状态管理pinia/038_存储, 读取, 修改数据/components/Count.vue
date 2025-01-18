<template>
  <div class="count">
    <h2>当前求和为：{{ countStore.sum }}</h2>
    <select v-model.number="n">
      <option value="1">1</option>
      <option value="2">2</option>
      <option value="3">3</option>
    </select>
    <button @click="add">加</button>
    <button @click="minus">减</button>
  </div>
</template>

<script setup lang="ts" name="Count">
  import {useCountStore} from '@/store/count'

  // 获取状态
  const countStore = useCountStore()
  // 读取状态
  console.log(countStore.sum)

  // 修改数据的三种方式
  // 方式1, 直接修改
  function add() {
    countStore.sum += 1
  }

  // 方式2, 通过$patch方法, 这个方法接受一个对象, 这个对象可以覆盖store中的数据的状态, 适合要修改多个属性时使用
  function minus(){
    countStore.$patch({
      sum: 10 // 这里指定sum的值, 来覆盖countStore中的sum
    })
  }

  // 方式3, 在count中定义actions, 然后在这里调用actions来修改状态
  function add1(){
    countStore.increment() // 调用actions来修改数据
  }

</script>

<style scoped>
  .count {
    background-color: skyblue;
    padding: 10px;
    border-radius: 10px;
    box-shadow: 0 0 10px;
  }
  select,button {
    margin: 0 5px;
    height: 25px;
  }
</style>