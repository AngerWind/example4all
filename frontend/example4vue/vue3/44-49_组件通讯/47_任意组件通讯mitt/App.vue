<template>
  <Child ></Child>
  <ul>
    <li v-for="toy in toyList">
      {{toy.name}} -- {{toy.age}}
    </li>
  </ul>
</template>

<script setup lang="ts">
import Child from "./components/Child.vue";
import {reactive} from "vue";
import emitter from "./utils/emitter.ts";

type Toy = { name: string; age: number }
// 因为使用的是ts, 所以要加类似注释
let toyList = reactive<Toy[]>([]);

// ts真的是烦死了
// 通过on来监听一个消息
emitter.on("send-toy", (toy) => {
  toyList.push(toy as Toy)
})
emitter.on("send-hello", (msg) => {
  console.log(msg)
})


</script>



<style scoped>

</style>
