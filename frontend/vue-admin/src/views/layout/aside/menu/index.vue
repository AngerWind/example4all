<script setup lang="ts">
import MenuItemList from '@/views/layout/aside/menu/MenuItemList.vue'
import { MenuItem } from '@/views/layout/aside/menu/types'
import { useRoute } from 'vue-router'

// 在js中使用scss文件中定义的变量
import style from '@/styles/js.module.scss'
import { useFoldStore } from '@/store/modules/flod.ts'

defineProps<{
  menuList: MenuItem[]
}>()

const $route = useRoute()
const foldState = useFoldStore()
</script>

<template>
  <!--必须要使用el-menu包着MenuItemList, 然后在MenuItemList中递归出列表-->
  <!--如果直接在这个组件中使用el-menu并且当前组件进行递归, 产生的子目录将不会缩进, 因为子目录也被el-menu包裹住, 会被当成一级目录-->
  <!--router表示开启路由模式, 在点击相应的item之后, 会将item的index作为路径进行跳转-->
  <!--collapse 表示当前菜单是否折叠, 在折叠状态下, 鼠标悬浮在图标上, 会弹窗提示框以显示二级菜单 -->
  <!--default-active表示加载时, 默认激活的菜单的index, 考虑到刷新时要按照路径激活对应的菜单-->
  <el-menu
    :collapse="foldState.fold"
    :collapse-transition="false"
    :default-active="$route.path"
    :router="true"
    :background-color="style['menu-background-color']"
    text-color="#fff"
  >
    <MenuItemList :menuList="menuList" />
  </el-menu>
</template>

<style scoped lang="scss">
.el-menu {
  border-right: 0; // 消除el-menu自带的边框
  width: 100%;
  height: 100%;
}
</style>
