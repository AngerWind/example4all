<template>
  <!-- vue3的组件模版结构可以没有根标签, 但是vue2的template必须要有一个根标签 -->
  <button @click="printMessage1">调用vue3的printMessage1</button>
  <button @click="printMessage">调用vue2的printMessage</button>
</template>

<script>
export default {
  name: 'App',
  // 还可以根据vue2的语法来配置data, method, computed等等
  // 但是不推荐配置这些东西了
  data(){
    return{
      name: "zhangsan",
      age: 18
    }
  },
  methods: {
    printMessage(){
      console.log(this.name, this.age)
      console.log(this.name1, this.age1) // 在vue2的代码里面, 可以访问vue3写的数据
    }
  },
  setup(){

    // vue3推荐所有的东西都写在setup方法里面, 包括data, method, computed
    let name1 = "lisi"
    let age1 = 17

    function printMessage1(){
      console.log(this.name, this.age)
      console.log(this.name1, this.age1) // 在vue3的代码里面, 无法访问vue2的数据
    }

    // 返回一个对象, 这个对象里面的内容可以直接在模板中调用
    return {
      name1,
      age1,
      printMessage1
    }
  }
}
</script>

<style>

</style>
