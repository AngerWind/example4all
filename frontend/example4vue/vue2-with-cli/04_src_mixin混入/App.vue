<template>
  <div >
    <h2 @click="showName">姓名:{{  name }}</h2>
    <h2>性别: {{ sex }}</h2>
  </div>
</template>

<script>
import {  mixin,  shareData } from "../mixin";
export default {
  name: "App",
  data(){
    console.log(this);
    return {
      name: '张三',
      sex: '男',
      x:666 // 如果mixins中的配置项与当前配置项冲突(method中有相同的方法, 或者data返回相同的数据), 以当前为准, 除了mount
    }
  },
  mounted() {
    console.log('你好啊啊！！！！')  //但对于生命周期钩子是都会保存的(混合的钩子比你配置的钩子先跑)
  },
  mixins:[ mixin, shareData ] // 指定需要混合的配置项, 相当于当前组件的配置项继承mixins中的配置项
}
</script>

