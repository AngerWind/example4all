<template>
  <div class="child">
    <!-- 具名插槽header, 父组件可以用#header往这里放结构 -->
    <slot name="header"></slot>

    <!-- 默认插槽(没有name), 父组件写在组件标签里、又没指定插槽名的内容会放在这里 -->
    <!-- 标签里的文字是"默认内容", 父组件没传递结构时才显示 -->
    <slot>默认内容(父组件没有传递结构时显示)</slot>

    <slot name="footer"></slot>

    <!-- 作用域插槽: 数据games在子组件中, 通过:games把数据传给插槽的使用者(父组件) -->
    <slot name="game" :games="games"></slot>

    <!-- 作用域插槽可以传递多个数据, 只要在<slot>上多绑定几个属性即可 -->
    <!-- 父组件那边用解构接收: v-slot="{ games, year, hotGame }" -->
    <slot name="game2" :games="games" :year="year" :hotGame="hotGame"></slot>
  </div>
</template>

<script setup lang="ts" name="Child">
  import {reactive,ref} from 'vue'

  // 数据在子组件这边维护
  const games = reactive([
    {id:'g01',name:'英雄联盟'},
    {id:'g02',name:'王者荣耀'},
    {id:'g03',name:'原神'},
  ])

  const year = ref(2026)
  const hotGame = ref('黑神话:悟空')

</script>

<style scoped>
  .child {
    background-color: skyblue;
    border-radius: 10px;
    padding: 10px;
    margin-bottom: 20px;
  }
</style>
