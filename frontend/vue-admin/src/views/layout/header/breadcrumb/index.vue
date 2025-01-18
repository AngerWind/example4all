<script setup lang="ts">
import { ArrowRight, Expand, Fold } from '@element-plus/icons-vue'
import { useFoldStore } from '@/store/modules/flod.ts'
import { useRoute, useRouter } from 'vue-router'
const foldStore = useFoldStore()
const $route = useRoute()
const $router = useRouter()
</script>

<template>
  <div class="breadcrumb">
    <el-icon size="35" @click="foldStore.change()">
      <component :is="foldStore.fold ? Expand : Fold"></component>
    </el-icon>
    <el-breadcrumb :separator-icon="ArrowRight">
      <template v-for="match in $route.matched">
        <el-breadcrumb-item v-if="match.meta.menu" :to="{ path: match.path }">
          <el-icon>
            <component :is="match.meta.menu?.icon"></component>
          </el-icon>
          {{ match.meta.menu?.title }}
        </el-breadcrumb-item>
      </template>
    </el-breadcrumb>
  </div>
</template>

<style scoped lang="scss">
.breadcrumb {
  display: flex;
  align-items: center;
  .el-icon {
    margin-left: 10px;
  }
  .el-breadcrumb {
    margin-left: 20px;
  }
}
</style>
