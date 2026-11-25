<template>
  <div class="child">
    <h3>我是子组件</h3>
    <button @click="makeError">点我制造一个错误</button>
  </div>
</template>

<script setup lang="ts" name="Child">
  import {ref} from 'vue'

  const user = ref<any>(null)

  function makeError(){
    // user是null, 访问.name会抛TypeError
    // 这个错误会被App的onErrorCaptured捕获(也可以放开让它冒泡到全局errorHandler)
    console.log(user.value.name)
  }

</script>

<style scoped>
</style>
