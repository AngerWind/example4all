<template>
  <div class="app">
    <h2>nextTick: 等DOM更新之后再执行代码</h2>

    <div v-if="!isEdit">
      <span @click="showEdit">{{ content }}</span>
    </div>
    <input ref="inputRef" v-else v-model="content" @blur="isEdit = false">
    <p>点上面这行字, 会变成输入框并且自动聚焦</p>
  </div>
</template>

<script setup lang="ts" name="App">
  import {ref,nextTick} from 'vue'

  const content = ref('点我编辑')
  const isEdit = ref(false)
  const inputRef = ref<HTMLInputElement>()

  async function showEdit(){
    isEdit.value = true
    // 此时如果直接写inputRef.value.focus()会报错:
    // 因为修改isEdit后, DOM是异步更新的, input元素还没被创建出来
    //
    // nextTick会在"下一次DOM更新完成之后"再执行回调/resolve
    // 所以等它之后, 就能拿到渲染出来的input了
    await nextTick()
    inputRef.value?.focus()
  }

</script>

<style scoped>
</style>
