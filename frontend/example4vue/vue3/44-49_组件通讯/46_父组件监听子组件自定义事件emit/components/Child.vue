<template>
  <button @click="triggerSendToy">点我发送一个send-hello自定义事件</button>
  <button @click="triggerSendHello">点我发送一个send-hello自定义事件</button>
</template>

<script setup lang="ts">

// 定义子组件会发送的所有自定义事件
// 在自定义事件的时候, 多个参数使用横杆连接
const emit = defineEmits(["send-toy", "send-hello"])

function triggerSendToy() {
  // 发送send-toy事件, 并携带一个参数
  emit("send-toy", {name: "zhangsan", age: 18})
}

function triggerSendHello() {
  // 发送一个 send-hello 自定义事件, 并携带一个参数
  emit("send-hello", "hello world")
}
</script>


<style scoped>

</style>