<script setup lang="ts">
import Breadcrumb from '@/views/layout/header/breadcrumb/index.vue'
import Setting from '@/views/layout/header/setting/index.vue'
</script>

<template>
  <div class="header">
    <Breadcrumb class="header-left"></Breadcrumb>
    <Setting class="header-right"></Setting>
  </div>
</template>

<style scoped lang="scss">
.header {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: space-between;
}
</style>
