<template>
  <h2>当前的y是:{{ x.y }}</h2>
  <button @click="x = {y : 888}">点我替换x</button>
  <button @click="x.y++">点我y+1</button>
  <hr/>
  <h4>{{ person }}</h4>
  <h2>姓名:{{ name }}</h2>
  <h2>年龄:{{ age }}</h2>
  <h2>薪资:{{ job.j1.salary }}K</h2>
  <button @click="name = name + '~'">修改姓名</button>
  <button @click="age++">增长年龄</button>
  <button @click="job.j1.salary++">增长薪资</button>
</template>

<script>
import {ref,reactive, toRefs, shallowReactive, shallowRef} from 'vue';
export default {
  name: 'App',
  setup(){
    /**
     * shallowReactive只考虑对象类型的第一层数据响应式
     * 即只有name, age, job(=直接修改, 添加属性, 修改属性), job.j1(=直接修改)
     * 对j1添加属性, 修改属性是没有响应式的
     */
    let person = shallowReactive({
      name: '张三',
      age: 18,
      job:{
        j1:{
          salary: 20
        }
      }
    });

    /**
     * 对于shallowRef, 如果传递的是基本类型, 那么和ref没有区别
     * 如果传递的是对象, 那么返回的refImpl不是响应式的, 即底层不会通过Proxy来代理他
     */
        // x不是响应式的
    let x = shallowRef({ y: 0 });


    /**
     * 总结: 这两个方法是用来优化性能的, 可以说没什么用!!!!!!!!!!!!!!!!!!!!
     * 如果数据有很深的结构, 但是只变化第一层, 那么使用shallowReactive
     * 如果一个数据, 每次都是替换他, 而不修改他, 那么使用shallowRef
     */
    return {
      person,
      ...toRefs(person),
      x,
    };

  }
}
</script>

<style>
</style>

