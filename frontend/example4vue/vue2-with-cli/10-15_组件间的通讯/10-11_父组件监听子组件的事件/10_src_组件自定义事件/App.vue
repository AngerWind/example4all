<template>
  <div class="app">
    <h1>{{ msg }},学生姓名是:{{ studentName }}</h1>


    <!-- 通过@xxx 来监听子组件上的自定义事件 -->
    <!--<Student @personalEvent="getStudentName" />-->

    <!--第二种写法使用ref绑定事件, 然后通过$refs来监听子组件的自定义时间  -->
    <Student ref="student" />

    <!-- 如果要监听Student原生事件, 比如click, 必须要使用.native修饰符,
         否则vue会以为你监听的是一个自定义的名为click的事件 -->
    <!-- vue3已弃用 .native -->
    <Student @click.native="show"/>

  </div>
</template>

<script>
import Student from "./components/Student";

export default {
  name: "App",
  components: {
    School,
    Student,
  },
  data() {
    return {
      msg: 'hello',
      studentName: ''
    }
  },
  methods: {
    // 接受到的参数, 就是子组件发送过来的参数
    getStudentName(name, ...params) {
      console.log(`app收到了学生名, ${name}`);
      this.studentName = name;
      console.log(`剩余参数,${params}`);
    },
    demo() {
      console.log('demo事件被触发了');
    },
    show() {
      console.log(`123`);
    }
  },
  mounted() {
    // 通过$refs拿到组件实例, 绑定事件, 第二种方式, 注意这里使用的是function的形式
    // this.$refs.student.$on('personalEvent', this.getStudentName);
    // this.$refs.student.$once('personalEvent', this.getStudentName); // 监听一次


    // 注意这里回调要写成剪头函数，this参数当前app组件, 否则的话是事件的触发者Student组件的this
    this.$refs.student.$on('personalEvent', (name) => {
      console.log(this);
      console.log(name);
      this.studentName = name;
    });
  }
}
</script>

<style>
/*
全局的样式是不需要加scoped
全局共享
*/
.app {
  background: gray;
  padding: 5px;
}
</style>


