<script setup lang="ts">
import {
  ArrowDown,
  FullScreen,
  Refresh,
  Setting,
} from '@element-plus/icons-vue'
import { useUserStore } from '@/store/modules/user.ts'
import { useRoute, useRouter } from 'vue-router'
import { reqLogout } from '@/api/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const $router = useRouter()
const $route = useRoute()

async function logout() {
  // 1. 调用后端接口, 清除token有效期
  await reqLogout()
  // 2. 清除本地的token
  userStore.updateToken('')
  // 3. 跳转到登录页面
  if ($route.path !== '/login') {
    await $router.push('/login')
  }
}
</script>

<template>
  <div class="setting">
    <el-button size="large" type="primary" :icon="Refresh" circle />
    <el-button size="large" type="success" :icon="FullScreen" circle />
    <el-button size="large" type="info" :icon="Setting" circle />
    <el-button size="large" type="info" circle>
      <el-avatar :src="userStore.avatar" />
    </el-button>
    <el-dropdown>
      <span class="el-dropdown-link">
        {{ userStore.username }}
        <el-icon class="el-icon--right">
          <arrow-down />
        </el-icon>
      </span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="logout()">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<style scoped lang="scss">
.setting {
  display: flex;
  align-items: center;

  .el-dropdown {
    padding-left: 12px;
  }
}
</style>
