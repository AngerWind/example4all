<template>
  <div class="child">
    <h3 class="child-title">我是子组件Child</h3>
    <!-- 插槽内容由父组件传入, 样式的"归属"在父组件那边 -->
    <slot></slot>
  </div>
</template>

<script setup lang="ts" name="Child">

</script>

<style scoped>
  .child {
    background-color: #eee;
    padding: 10px;
    margin-top: 10px;
  }

  /*
    :slotted(): 让子组件可以给"插槽传进来的内容"设置样式
    因为插槽内容是父组件编译的, 只带父组件的data-v, 子组件普通选择器选不中它
  */
  :slotted(.slot-content) {
    font-size: 20px;
  }

  /*
    :global(): 全局选择器, 写了它就等于不用scoped, 样式全局生效(慎用)
  */
  /* :global(body) { background-color: #fafafa; } */
</style>
