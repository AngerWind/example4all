<template>
  <!-- 必须要有一个根元素 -->
  <div>
    <School></School>
    <Student></Student>
  </div>
</template>

<script>
import School from "./components/MySchool";
import Student from "./components/MyStudent";

export default {
  name: "App",
  //汇总所有的组件
  components: {
    Student,
    School,
  },
};
</script>
