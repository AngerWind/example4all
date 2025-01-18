<template>
  <div class="logo">
    <img :src="prop.src" alt="{{prop.title}}" class="logo-image" />
    <!--菜单折叠后不显示字体-->
    <p v-show="!foldState.fold" class="logo-title">{{ prop.title }}</p>
  </div>
</template>

<script setup lang="ts">
// 接收父组件传递过来的参数
import { useFoldStore } from '@/store/modules/flod.ts'

const prop = defineProps({
  src: {
    // logo图片地址
    type: String,
    required: true,
  },
  title: {
    // logo标题
    type: String,
    required: true,
  },
})

const foldState = useFoldStore()
</script>

<style scoped lang="scss">
.logo {
  height: $logo-height;
  width: calc(100% - 24px);
  display: flex;
  align-items: center;
  flex-wrap: nowrap;
  white-space: nowrap; // 宽度不够的时候文字不换行
  padding: 5px 12px;
  &-image {
    width: 60px;
    height: 60px;
  }
  &-title {
    font-size: 25px;
    font-weight: bold;
    margin-left: 10px;
  }
}
</style>
