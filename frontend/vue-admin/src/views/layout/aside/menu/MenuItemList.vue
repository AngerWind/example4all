<script setup lang="ts">
import { MenuItem } from '@/views/layout/aside/menu/types'

defineProps<{
  menuList: MenuItem[]
}>()
</script>
<!--<script lang="ts">-->
<!--// export default {-->
<!--//   name: 'MenuItemList', // 要想递归使用当前组件, 组件必须有name属性-->
<!--// }-->
<!--</script>-->
<template>
  <template v-for="(item, index) in menuList" :key="item.path">
    <template v-if="item.subMenu && item.subMenu.length > 0">
      <!--有子路由-->
      <!-- popper-class弹出框应用的class -->
      <el-sub-menu :index="item.path" popper-class="popper">
        <template #title>
          <!--对于el-sub-menu, icon要放在template里面, 否则菜单向左折叠的时候会显示不出图标 -->
          <el-icon>
            <component :is="item.icon" />
          </el-icon>
          <span>{{ item.title }}</span>
        </template>
        <!-- 递归调用Menu彩蛋, 生成子菜单 -->
        <!-- 递归组件需要一个名字, 当前组件的名字默认就是当前文件名, 但是老师的名字是index,
          所以他使用index是可以的, 已经经过实验了, 同时因为他的index这个名字太过离谱,
          所以可以通过另外一个<script>标签来重新定义名字, 如上面所示-->
        <MenuItemList :menuList="item.subMenu"></MenuItemList>
      </el-sub-menu>
    </template>
    <template v-else>
      <!--没有子路由-->
      <el-menu-item :index="item.path">
        <!-- 对于el-menu-item, el-icon需要放在template的外面 -->
        <el-icon>
          <component :is="item.icon" />
        </el-icon>
        <template #title>
          <span>{{ item.title }}</span>
        </template>
      </el-menu-item>
    </template>
  </template>
</template>

<style scoped lang="scss"></style>

<style lang="scss">
// 必须在全局css中定义, 因为弹出框不在app组件中
.popper .el-menu-item {
  background-color: red; // 设置弹出框的背景颜色
}
.popper.el-popper {
  border: 0; // 缩略的时候弹出的提示框会有一个边框, 这里消除掉
}
</style>
