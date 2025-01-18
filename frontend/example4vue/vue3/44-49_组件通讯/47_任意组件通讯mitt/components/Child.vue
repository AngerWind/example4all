<template>
  <button @click="triggerSendToy">点我发送一个send-hello自定义事件</button>
  <button @click="triggerSendHello">点我发送一个send-hello自定义事件</button>
</template>

<script setup lang="ts">

import emitter from "@/utils/emitter.ts";

function triggerSendToy() {
  // 发送一个 send-toy 类型的消息, 并携带参数
  emitter.emit( "send-toy", {name: "zhangsan", age: 18})
}

function triggerSendHello() {
  // 发送一个 send-hello 类型的消息, 并携带参数
  emitter.emit("send-hello", "hello world")
}
</script>


<style scoped>

</style>