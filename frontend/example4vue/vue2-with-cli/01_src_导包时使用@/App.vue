<template>
  <!-- 必须要有一个根元素 -->
  <div>
    <School></School>
    <Student></Student>
  </div>
</template>

<script>
// 在导入文件的时候, 可以使用@符号来表示src目录
import School from "@/components/MySchool";
import Student from "@/components/MyStudent";

export default {
  name: "App",
  components: {
    Student,
    School,
  },
};
</script>
