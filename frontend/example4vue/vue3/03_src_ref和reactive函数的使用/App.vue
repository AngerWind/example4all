<template>

  <!--在模板中, 不需要 .value 就可以调用ref定义的对象 -->
  <h1>我是app组件</h1>
  <h1>我叫{{ name }}, {{ age }}岁</h1>
  <h3>职位:{{ job.type }}</h3>
  <h3>薪水:{{ job.salary }}</h3>
  <button @click="changeInfo">修改人的信息</button>
</template>

<script>
import { ref, reactive } from 'vue';
export default {
  name: 'App',
  setup(){
    /**
     * 对于在vue2中通过data定义的数据, 在vue3中需要通过 ref 和 reactive 这两个函数来定义了
     *
     * ref可以定义基础类型和对象类型, 在调用的时候需要通过 .value 来调用
     * reactive只能定义对象类型, 不能定义基础类型, 在调用属性的时候, 可以直接调用
     *
     * ref和reactive定义的对象, 如果有多级结构, 那么都有响应式
     */
    let name = ref('py'); // ref 定义基础类型
    let age = ref(21);
    let job = ref({  // ref 定义对象类型
      type: 'frontend developer',
      salary: '30'
    });
    let job2 = reactive({ // reactive 定义对象类型
      type: 'frontend developer',
      salary: '30'
    });
    let hobby = reactive(["抽样", "喝酒"])

    function changeInfo(){
      name.value = '李四'; // ref 定义的需要通过 .value 来调用
      age.value = 42;
      job.value.type = 'UI developer';
      job2.type = "Java Developer" // reactive 定义的不需要通过 .value 来调用

    }


    //返回一个对象
    return {
      name,
      age,
      job,
      job2,
      changeInfo
    }
  }
}
</script>

<style>
</style>
