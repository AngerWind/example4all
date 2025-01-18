<template>

<!-- 监听来自子组件的自定义事件: send-toy 和 send-hello -->
<Child @send-toy="sendToy" @send-hello="sendHello"></Child>

  <ul>
    <li v-for="toy in toyList">
      {{toy.name}} -- {{toy.age}}
    </li>
  </ul>
</template>

<script setup lang="ts">
import Child from "./components/Child.vue";
import {reactive} from "vue";

type Toy = { name: string; age: number }
// 因为使用的是ts, 所以要加类似注释
let toyList = reactive<Toy[]>([]);
function sendToy(toy:Toy){
  toyList.push(toy)
}

function sendHello(msg: string){
  console.log(msg)
}

</script>



<style scoped>

</style>
