<script setup lang="ts"></script>

<template>
  <!--给router-view添加过场动画-->
  <!-- 原理是: router-view这个组件在渲染的时候, 内部会给slot传递当前要渲染的组件
      我们可以通过v-slot来接收这个组件, 然后在内部给他添加上一层过渡的效果 -->
  <router-view v-slot="{ Component }">
    <component :is="Component" />
  </router-view>
</template>

<style scoped lang="scss"></style>
