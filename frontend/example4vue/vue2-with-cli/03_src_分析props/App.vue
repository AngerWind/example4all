<template>
   <div>

      <!-- 给子组件传递参数, 如果直接sex="男", 那么原样传递, 如果使用 :age="12 + 1", 那么会计算表达式-->
      <Student name="panyue" sex="男" :age='12'/>
      <hr/>
   </div>
</template>

<script>
import Student from "./components/Student";

export default {
  name: "App",
  components:{
    Student,
  },
}
</script>

