<template>
  <div class="count">
    <h3>sum的值是: {{ countStore.sum }}</h3>
    <!-- getters和state一样, 直接通过store实例使用 -->
    <h3>sum放大10倍(bigSum): {{ countStore.bigSum }}</h3>
    <h3>大写的学校名(upperSchool): {{ countStore.upperSchool }}</h3>
    <h3>拿别的getter(bigBigSum): {{ countStore.bigBigSum }}</h3>
    <button @click="countStore.sum += 1">点我sum+1, 看getters自动变</button>
  </div>
</template>

<script setup lang="ts" name="Count">
  import {useCountStore} from '../store/count'

  const countStore = useCountStore()

</script>

<style scoped>
</style>
