<template>
   <div class="demo">
     <h2 class="title">姓名:{{  name }}</h2>
     <h2>性别: {{ sex }}</h2>
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
}
</script>

<!--添加scoped表示当前样式只负责当前文件的样式, 当前组件导入到其他的组件中使用的时候不起作用-->
<style scoped>
  .demo{
    background: orange;
  }
</style>
