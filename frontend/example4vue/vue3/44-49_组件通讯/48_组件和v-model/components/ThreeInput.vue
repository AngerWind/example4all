<script setup lang="ts">
// 接受父组件传递过来的 props
const props = defineProps(["modelValue", "name", "age"])
// 自定义事件
const emit = defineEmits(["update:modelValue", "update:name", "update:age"])
</script>

<template>
  <!--
      当内部的input输入的时候, 就会触发  emit('update:name', $event.target.value),
      $event.target.value表示输入后输入框的值
      emit表示发送一个自定义事件, 这样父组件就会接受到这个事件,
      然后执行 name = $event 来修改name属性, 这样这里的props也会跟着改变, 然后又经过v-bind, 所以input的内容也会跟着改变
      这就实现了双向绑定
  -->

  modelValue属性: {{modelValue}}<input type="text" v-bind:value="modelValue"  @input="emit('update:modelValue', $event.target.value)"/> <br>
  name属性: {{name}}<input type="text" v-bind:value="name" @input="emit('update:name', $event.target.value)"/> <br>
  age属性: {{age}}<input type="text" v-bind:value="age" @input="emit('update:age', $event.target.value)"/> <br>

</template>

<style scoped>

</style>