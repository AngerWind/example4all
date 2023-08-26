<template>
   <div class="demo">
     <h2>学校名称:{{  name }}</h2>
     <h2>学校地址: {{ address }}</h2>
   </div>
</template>

<script>
export default {
  name: "School",
  data(){
    console.log(this);
    return {
       name: 'wust university',
       address: '武汉科技大学'
    }
  },
}
</script>

<!--添加scoped表示当前样式只负责当前文件的样式, 当前组件导入到其他的组件中使用的时候不起作用-->
<style scoped>
  .demo{
    background: skyblue;
  }
</style>


