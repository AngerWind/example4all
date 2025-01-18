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
import {storeToRefs} from "pinia"

// 获取状态
const countStore = useCountStore()
// 读取状态
console.log(countStore.sum)

// 如果你觉得每次读取状态 都要通过countStore.xxx来去的话, 太麻烦了
// 那么你可以通过storeToRefs()函数, 将这些属性结构出来
let {sum, school, address} = storeToRefs(countStore)

function add() {
  sum += 1 // 直接修改
}
function minus() {
  sum -= 1
}

</script>

<style scoped>
.count {
  background-color: skyblue;
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0 0 10px;
}

select, button {
  margin: 0 5px;
  height: 25px;
}
</style>