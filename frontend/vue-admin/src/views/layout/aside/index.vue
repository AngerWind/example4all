<template>
  <div class="aside">
    <Logo :src="logo_path" :title="logo_title" class="logo" />
    <el-scrollbar class="scrollbar">
      <Menu :menuList="menuData" />
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import { logo_path, logo_title } from '@/settings/logo_settings.ts'
import { menuData } from '@/router/routes.ts'
import Menu from '@/views/layout/aside/menu/index.vue'
import Logo from '@/views/layout/aside/logo/index.vue'
</script>

<style scoped>
.aside {
  width: 100%;
  height: 100%;
}
</style>
