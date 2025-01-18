<template>
  <div>
    <button @click="isShow = true">点我弹个窗</button>
    <!-- teleport组件表示传送, 他主要解决的问题是, 如果下面的div通过v-if来控制展示 -->
    <!-- 当div展示的时候, 他会把父元素撑大, 当不展示的时候, 父元素又会缩小, 这样不美观 -->
    <!-- 所以我们就可以使用 teleport 组件将他包裹一层, 然后将他传送到别的元素内部的最后面 -->

    <!-- 将内部的div传送到body里面, 作为body的最后一个子节点, 也可以使用to="#app"来传送到id为app的元素内部, 并作为他的最后一个子节点-->
    <teleport to="body">
      <div class="mask" v-if="isShow">
        <div class="dialog">
          <h3>我是一个弹窗</h3>
          <h4>一些内容</h4>
          <h4>一些内容</h4>
          <h4>一些内容</h4>
          <h4>一些内容</h4>
          <button @click="isShow = false">关闭弹窗</button>
        </div>
      </div>
    </teleport>
  </div>
</template>

<script>
import { ref } from "vue";
export default {
  name: "Dialog",
  setup(){
    let isShow = ref(false);
    return {
      isShow
    }
  }
}
</script>

<style scoped>
  .mask{
    position: absolute;
    top:0;
    bottom: 0;
    left: 0;
    right:0;
    background: rgba(0,0,0,.5);
    text-align: center;
  }
  .dialog{
    position: absolute;
    top: 50%;
    left:50%;
    transform: translate(-50%, -50%);
    width: 300px;
    height: 300px;
    background: #42b983;
  }
</style>
