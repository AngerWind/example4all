<template>
  <!-- vue3的组件模版结构可以没有根标签, 但是vue2的template必须要有一个根标签 -->
  <!-- vue3会自动的在外面包裹一个Fragment组件 -->
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
    // !!!! 在setup里面, 没有办法调用this, 是undefined, 因为所有需要调用的数据都在setup中, 不再需要this

    // vue3推荐所有的东西都写在setup方法里面, 包括data, method, computed
    let name1 = "lisi"
    let age1 = 17

    function printMessage1(){
      console.log(this.name, this.age)

      // 在vue3的代码里面, 无法访问vue2的数据, 因为在生命周期中, setup被最先调用, 这个时候data(){}都还没有执行
      console.log(this.name1, this.age1)
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
