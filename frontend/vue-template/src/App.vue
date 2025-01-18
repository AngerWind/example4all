<template>
  <div>
    <el-button type="primary" size="default" :icon="Plus">主要按钮</el-button>
    <el-button type="success" size="small" :icon="Edit">编辑按钮</el-button>
    <el-button type="danger" size="default" :icon="Delete">删除按钮</el-button>

    <!-- svg测试 -->
    <!-- 放在svg的容器 -->
    <svg style="width: 30px; height: 100px">
      <!--xlink:href指定使用哪个图标, 属性值是#icon-图标名字-->
      <use xlink:href="#icon-eps" fill="yellow"></use>
    </svg>

    <h1>color</h1>

    <!-- 路由的位置 -->
    <router-view></router-view>
  </div>
</template>

<script setup lang="ts">
// 引入图标组件库
import { Delete, Edit, Plus } from '@element-plus/icons-vue'

import request from '@/utils/request.ts'
// mock数据测试
request({
  url: '/user/login',
  method: 'post',
  data: {
    username: 'admin',
    password: '111111',
  },
})
</script>

<style scoped lang="scss">
div {
  h1 {
    color: $color;
  }
}
</style>
