<template>
  <div>
    <!-- 通过ref, 来讲该元素的dom添加到Vue实例或者VueComponent的$refs中 -->
    <h1 v-text="msg" ref="title"></h1>
    <button @click="showH">点我输出上方的dom元素</button>

    <!-- 通过ref, 来讲该组件对应的vc实例添加到vc的$refs中 -->
    <School ref="sch" />
  </div>
</template>

<script>
import School from "./components/School";

export default {
  name: "App",
  data() {
    return {
      msg: "欢迎学习Vue",
    };
  },
  methods: {
    showH(e) {
      console.log(this.$refs.title); // 拿到的是一个真实的dom元素
      console.log(this.$refs.sch); // 拿到的是表情对应的VueComponent实例
    },
  },
  components: {
    School,
  },
};
</script>
