<template>
  <h4>{{ person }}</h4>
  <h2>姓名:{{ name }}</h2>
  <h2>年龄:{{ age }}</h2>
  <h2>薪资:{{ salary }}K</h2>
  <button @click="name = name + '~'">修改姓名</button>
  <button @click="age++">增长年龄</button>
  <button @click="salary++">增长薪资</button>
</template>

<script>
import { ref, reactive, toRef, toRefs} from 'vue';
export default {
  name: 'Demo',
  setup(){
    let person = reactive({
      name: '张三',
      age: 18,
      job:{
        j1:{
          salary: 20
        }
      }
    });

    /**
     * 为了方便在template中使用属性, 而不用每次都带上person前缀, 可以吧person中的属性也暴露出去
     * 但是千万不能像下面这种写法
     * 因为JS是传值的类型, 暴露出去的值和person中的属性已经没有关系了
     */
    // return {
    //   person,
    //   name: person.name
    //   age: ref(person.age),
    //   salary: ref(person.job.j1.salary)
    // };
    /**
     * 正确的做法是把person的属性包装成一个refImpl, 然后暴露出去
     * toRef将一个属性包装成refImpl, toRefs包装一堆属性, 然后结构赋值
     */
    return {
      person,
      // name: toRef(person, "name"),
      // age: toRef(person, "age"),
      // salary: toRef(person.job.j1, "salary")
      ...toRefs(person),
      salary: toRef(person.job.j1, 'salary')  //toRef可以与toRefs连用,更加方便
    };



  }
}
</script>

<style>
</style>

