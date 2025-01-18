<template>
  <div id="root">
    {{n}}
    <button @click="add">点我n加一</button>
    <input type="text" v-model="n" ref="input">
  </div>
</template>

<script>

export default {
  name: "App",
  data() {
    return {
      n: 1
    }
  },
  methods: {
    add() {
      n++
      // 这个方法会在模板重新渲染后被调用
      this.$nextTick(() => {
        // 模板渲染后, 重新让input获取焦点
        this.$refs.input.focus()
      })
    }
  }
}
</script>

<style>

</style>


