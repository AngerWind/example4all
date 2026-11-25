<template>
  <div class="count">
    <h3>sum: {{ countStore.sum }}</h3>
    <h3>token: {{ countStore.token }}</h3>
    <button @click="countStore.sum += 1">sum+1</button>
    <button @click="countStore.token = 'abc' + Math.random().toFixed(3)">换个token</button>
  </div>
</template>

<script setup lang="ts" name="Count">
  import {useCountStore} from '../store/count'

  const countStore = useCountStore()

</script>

<style scoped>
</style>
