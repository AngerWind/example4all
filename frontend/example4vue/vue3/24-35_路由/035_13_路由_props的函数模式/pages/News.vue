<template>
  <div class="news">
    <h3>新闻详情</h3>
    <!-- 组件不需要再写$route.params.xxx, 直接用props -->
    <p>id(params): {{ id }}</p>
    <p>keyword(query): {{ keyword }}</p>
  </div>
</template>

<script setup lang="ts" name="News">
  // props的函数模式(见router/index.ts):
  // 路由把params和query处理好后, 以props的形式传给组件
  // 组件只要声明接收即可, 和$route解耦, 组件单独测试时也更方便(直接传props就行)
  defineProps<{
    id:string,
    keyword:string
  }>()

</script>

<style scoped>
  .news {
    padding: 20px;
  }
</style>
