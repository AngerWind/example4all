<template>
  <h1>当前求和为:{{ sum }}</h1>
  <button @click="sum++">点我加一</button>
  <hr/>
  <h2>当前的信息为:{{ msg }}</h2>
  <button @click="msg += '!'">修改信息</button>
  <hr/>
  <h2>姓名:{{ person.name }}</h2>
  <h2>年龄:{{ person.age }}</h2>
  <h2>薪资:{{ person.job.j1.salary }}K</h2>
  <button @click="person.name = person.name + '~'">修改姓名</button>
  <button @click="person.age++">增长年龄</button>
  <button @click="person.job.j1.salary++">增长薪资</button>
</template>

<script>
import {ref, reactive, watch} from 'vue';

export default {
  name: 'App',
  // vue2中的watch写法
  // watch:{
  //   // 简单写法
  //   // sum(newValue,oldValue){ // 指定要监视sum
  //   //   console.log('sum的值发生变化了');
  //   //   console.log(`newValue:${newValue}, oldValue:${oldValue}`);
  //   // }
  //
  //   //完整写法
  //   sum:{
  //     deep: true, //深度监视
  //     immediate: true, //一开始就监视一下
  //     handler(nv,ov){
  //         console.log('sum的值发生变化了');
  //         console.log(`newValue:${nv}, oldValue:${ov}`);
  //     }
  //   }
  // },
  setup() {

    let sum = ref(0);
    let msg = ref('你好');
    let student = ref({
      name: '张三',
      age: 18,
      job: {
        j1: {
          salary: 20
        }
      }
    })
    let person = reactive({
      name: '张三',
      age: 18,
      job: {
        j1: {
          salary: 20
        }
      }
    })

    //情况一: 监视ref所定义的基础数据类型
    // watch(sum, (nv, ov) => {
    //   console.log('sum的值发生变化了');
    //   console.log(`newValue:${nv}, oldValue:${ov}`);
    // }, {
    //   //监视的配置
    //   immediate: true //一上来就更新
    // });

    //情况二:监视ref所定义的多个基础数据类型
    // watch([sum, msg], (nv, ov) => {
    //   //此时nv和ov都是被监视属性值的数组
    //   // console.log(Array.isArray(ov)); //true
    //   console.log('sum的值或者msg的值发生变化了');
    //   console.log(`newValue:${nv}, oldValue:${ov}`);
    // },{
    //   immediate: true
    // });

    // 监视ref定义的对象类型, 只能监视value的变化, value的属性的变化监视不到, 换句话说, 只能监视一层
    // 解决办法是: 1: 监视student.value, 2. 开启深度监视
    // watch(student, (nv, ov) => {
    //   console.log(`newValue:${nv}, oldValue:${ov}`);
    // }, {
    //   deep: true // 必须开深度监视
    // })
    //
    // watch(student.value, (nv, ov) => {
    //   console.log(`newValue:${nv}, oldValue:${ov}`);
    // })

    /**
     * 情况三:监视reactive所定义的一个响应式数据
     *
     * 坑:1.此处无法获取正确的ov(oldValue), 因为你监视的person是一个对象, 那么给你的oldValue和newValue都是同一个
     *    2.强制开启了深度监视, 配置了也无效!!!!!!!!!!!!!!
     *
     * 一般生产中就写这种, 因为oldValue也不是必须的, 如果oldValue是必须的, 把它提出去变成一个ref对象进行了
     */
    // watch(person, (nv, ov) => {
    //   console.log('person变化了');
    //   console.log(nv, ov);
    // }, {
    //   deep: false // 此处的deep配置是无效的, 强制深度监视
    // });

    //情况四：监视reactive所定义的对象的一个属性
    // watch(() => person.age, (nv, ov) => {
    //   console.log('person的age变了', nv, ov);
    // })

    //情况五: 监视reactive所定义的对象的多个属性
    // watch([() => person.age, () => person.name], (nv, ov) => {
    //   //此时nv和ov都是数组
    //   console.log('person的age或name发生改变了',nv, ov);
    // });

    /**
     * 情况六: 监视对象中的对象
     * 仍然获取不到oldValue, oldValue和newValue一样
     *
     * 监视对象中的对象, 必须开启deep才能进行深度监视, 要不然不会开启深度监视!!!!!!!!!
     */
    // watch(() => person.job, (nv, ov) => {
    //     //这里依然无法拿到正确的ov，因为依然监视的是对象
    //   console.log('person的job信息发生改变了',nv, ov);
    // }, {
    //   // 这里必须要添加deep才能进行深度监视
    //   deep: true
    // })

    return {
      sum,
      msg,
      person
    }
  }
}
</script>

<style>
</style>
