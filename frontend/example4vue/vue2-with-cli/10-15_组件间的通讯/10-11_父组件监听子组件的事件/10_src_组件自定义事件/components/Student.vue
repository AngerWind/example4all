<template>
   <div class="student">
     <h2>姓名:{{  name }}</h2>
     <h2>性别: {{ sex }}</h2>

     <button @click="sendStudentName">把学生名传递给app</button>
     <button @click="unbind">解绑自定义(personalEvent)事件</button>
     <button @click="death">销毁当前student组件的实例对象</button>
   </div>
</template>

<script>

export default {
  name: "Student",
  data(){
    console.log(this);
    return {
       name: '张三',
       sex: '男',
    }
  },
  methods:{

    sendStudentName(){
      // 在当前VueComponent身上触发一个 personalEvent 事件, 并携带相应的信息
      this.$emit('personalEvent',this.name,666,777,888);
    },
    unbind(){
      //解绑事件, 解绑之后, 父组件就没办法监听这个事件了

      this.$off('personalEvent'); //这种写法只能解绑一种自定义事件
      //this.$off([ 'personalEvent', 'demo' ]);// 解绑多个事件，参数为包含多个事件名的数组
      // this.$off(); //比较暴力，有几个自定义事件就全给你解绑了
    },
    death(){
      this.$destroy(); //销毁当前组件实例，销毁后所有该实例的自定义事件都不奏效了
    }
  }
}
</script>

<style scoped>
  .student{
    background: orange;
    padding: 5px;
    margin-bottom: 10px;
  }
</style>
