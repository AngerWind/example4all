<template>
   <div class="container">
     <Category title="游戏">
       <!-- 通过scope来接收组件传过来的数据, 是一个对象, 里面是传过来的具体数据 -->
       <!-- 通过template来指定插槽的结构 -->
       <template scope="{games}">
         <ul>
           <li v-for="(g , index) in games" :key="index">{{ g }}</li>
         </ul>
       </template>
     </Category>

     <Category title="游戏">
       <template scope="{games}">
         <!--这里data为插槽给你传递的对象包含你所需要的数据-->
         <ol>
           <li  v-for="(g , index) in games" :key="index">{{ g }}</li>
         </ol>
       </template>
     </Category>

     <Category title="游戏">
       <template slot-scope="{games}">
         <h4 v-for="(g , index) in games" :key="index">{{ g }}</h4>
       </template>
     </Category>
   </div>
</template>

<script>
import Category from "./components/Category";
export default {
  name: "App",
  components:{
    Category
  },
}
</script>
<style lang="css" scoped>
   .container, .foot{
     display: flex;
     justify-content: space-around;
   }
</style>



