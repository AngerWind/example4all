<template>
  <ul class="news-list">
    <li>编号：{{ params.id }}</li>
    <li>标题：{{ params.title }}</li>
    <li>内容：{{ params.content }}</li>
  </ul>
</template>

<script setup lang="ts" name="About">
  import {useRoute} from 'vue-router'
  import {toRefs} from 'vue'

  // 使用useRoute来获取当前VueComponent上面的Route
  let route = useRoute()

  // 可以通过 route.params.id 来使用参数
  console.log(route.params.id)


  // 你也可以从route上展开params属性, 通过toRefs函数
  let {params} = toRefs(route)

  console.log(route)
</script>

<style scoped>
  .news-list {
    list-style: none;
    padding-left: 20px;
  }

  .news-list>li {
    line-height: 30px;
  }
</style>