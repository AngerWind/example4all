<template>
  <div>
    <h2>我是Home的内容</h2>
    {{$route.params.id}}
    {{$route.params.title}}
  </div>
</template>

<script>
export default {
  name: "Home",
}
</script>

<style scoped>
</style>
