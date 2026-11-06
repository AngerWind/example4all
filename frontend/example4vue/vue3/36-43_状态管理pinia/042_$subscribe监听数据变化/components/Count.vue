<template>
  <div class="count">
    <h2>当前求和为：{{ sum }}，放大10倍后：{{ bigSum }}</h2>
    <h3>欢迎来到:{{ school }}，坐落于：{{ address }}，大写：{{ upperSchool }}</h3>
    <select v-model.number="n">
      <option value="1">1</option>
      <option value="2">2</option>
      <option value="3">3</option>
    </select>
  </div>
</template>

<script setup lang="ts" name="Count">
  import { ref,reactive,toRefs } from "vue";
  import {storeToRefs} from 'pinia'

  import {useCountStore} from '@/store/count'
  const countStore = useCountStore()
  // 解构
  let {sum, school, address, upperSchool, bigSum} = storeToRefs(countStore)

  let n = ref(0)

  // 你可以通过$subscribe来订阅数据的变化, 类似于watch
  // 回调函数在数据变化的时候会被调用
  // mutate没什么用, state可以获取到变化后的数据
  countStore.$subscribe((mutate, state) => {
    console.log(`数据发生了改变, 变化后的sum为: ${state.sum}`)
  })
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