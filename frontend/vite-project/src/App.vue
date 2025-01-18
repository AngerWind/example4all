<script setup lang="ts">
import HelloWorld from './components/HelloWorld.vue'
</script>

<template>
  <div class="app">
    这里是app.vue
    <HelloWorld class="hello"/>
  </div>

</template>

<style scoped>
.app {
  background-color: bisque;
  .hello {
    color: green;
  }
  :deep(.xx){
    color: gray;
  }
}
</style>
