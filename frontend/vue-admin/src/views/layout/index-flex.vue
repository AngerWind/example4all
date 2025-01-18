<script setup lang="ts">
import logo from '@/views/layout/aside/logo/index.vue'
import { logo_path, logo_title } from '@/settings/logo_settings.ts'
</script>

<template>
  <div class="container">
    <div class="slider">
      <logo :src="logo_path" :title="logo_title" />
    </div>
    <div class="tabbar">456</div>
    <div class="main">
      789
      <!--<p style="height: 10000px; background-color: lightgray">我是一个段落</p>-->
    </div>
  </div>
</template>

<style scoped lang="scss">
$slider-width: 18vw;
$tabbar-height: 100px;
.container {
  width: 100vw;
  height: 100vh;
  background-color: red;

  .slider {
    max-width: 500px;
    width: $slider-width;
    height: 100vh;
    background-color: green;
  }

  .tabbar {
    width: calc(100% - $slider-width);
    height: $tabbar-height;
    background-color: #00f;
    position: fixed;
    // todo 如果注释掉下面两行, 这个tabbar案例来说应该出现在左上角, 但是实际上却是直接消失找不到了
    left: $slider-width;
    top: 0;
  }

  .main {
    position: absolute;
    // todo 老师这里没有减去2*padding, 但是效果却是刚刚好, 为什么?
    width: calc(100% - $slider-width - 40px);
    height: calc(100vh - $tabbar-height - 40px);
    background-color: #ff0;
    left: $slider-width;
    top: $tabbar-height;
    overflow: auto;
    // todo 案例来说添加上padding后width和height应该将去2*padding, 才能刚刚好填满, 否则会多出一截来, 但是老师的效果却是刚刚好
    padding: 20px;
  }
}
</style>
