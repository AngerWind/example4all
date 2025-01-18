<script setup lang="ts">
const props = defineProps<{ index: number }>()
</script>

<template>
  hello
  <template v-if="index > 0">
    <index :index="index - 1" />
  </template>
</template>

<style scoped lang="scss"></style>
