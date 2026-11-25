<template>
  <!-- 外部链接: 渲染成普通的<a>, 新窗口打开 -->
  <a v-if="isExternal" :href="to" target="_blank" rel="noopener">
    <slot></slot>
  </a>
  <!--
    内部链接: 复用RouterLink的能力
    custom属性表示不使用RouterLink默认的<a>渲染, 而是通过v-slot把渲染权交给我们
    v-slot可以拿到: href(解析后的路径), isActive(是否激活), navigate(跳转方法)等
  -->
  <RouterLink v-else :to="to" custom v-slot="{ href, isActive, navigate }">
    <a :href="href" :class="{'active': isActive}" @click="navigate">
      <slot></slot>
    </a>
  </RouterLink>
</template>

<script setup lang="ts" name="AppLink">
  import {computed} from 'vue'
  import {RouterLink} from 'vue-router'

  // 扩展RouterLink的思路: 包一层自己的组件, 加上"外链直接打开、内链走路由"的判断
  // 实际项目中还可以在这里统一加图标、埋点、权限判断等
  const props = defineProps<{
    to: string
  }>()

  // 判断是不是外部链接(以http开头)
  const isExternal = computed(() => typeof props.to === 'string' && props.to.startsWith('http'))

</script>

<style scoped>
</style>
