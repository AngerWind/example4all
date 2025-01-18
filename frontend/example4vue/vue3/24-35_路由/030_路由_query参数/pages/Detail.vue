<template>
  <ul class="news-list">
    <li>编号：{{ query.id }}</li>
    <li>标题：{{ query.title }}</li>
    <li>内容：{{ query.content }}</li>
  </ul>
</template>

<script setup lang="ts" name="About">
  import {toRefs} from 'vue'
  import {useRoute} from 'vue-router'

  // 使用useRoute来获取当前VueComponent上面的Route
  let route = useRoute()

  // 可以通过 route.query.id 来使用参数
  console.log(route.query.id)


  // 你也可以从route上展开query属性, 通过toRefs函数
  let {query} = toRefs(route)


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