<script setup lang="ts">
import Aside from '@/views/layout/aside/index.vue'
import Header from '@/views/layout/header/index.vue'
import Main from '@/views/layout/main/index.vue'
import { useFoldStore } from '@/store/modules/flod.ts'
import { onMounted } from 'vue'
import { reqUserInfo } from '@/api/user'
import { useUserStore } from '@/store/modules/user.ts'

const foldState = useFoldStore() // 在css中使用了, 别删除
onMounted(async () => {
  const data = await reqUserInfo()
  const userStore = useUserStore()
  userStore.username = data.name
  userStore.avatar = data.avatar
})
</script>

<template>
  <div>
    <el-container>
      <!--侧边栏-->
      <el-aside>
        <Aside></Aside>
      </el-aside>
      <el-container>
        <!--header-->
        <el-header>
          <Header></Header>
        </el-header>
        <!--main-->
        <el-main>
          <Main></Main>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped lang="scss">
.el-aside {
  height: 100vh;
  background-color: $menu-background-color;
  width: v-bind("foldState.fold ? '85px' : '260px'");
  transition: all 0.3s; // 侧边栏折叠时, 宽度变化的动画
  color: $menu-text-color;
  overflow: hidden;
}

.el-header {
  height: 6vh;
  background-color: $header-background-color;
  color: #001529;
}

.el-main {
  background-color: $main-background-color;
  overflow: auto;
}
</style>
