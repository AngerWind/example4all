<template>
  <button @click="n++">点我n加1</button>
  {{ n }}

  <input type="text" v-model.number="n">
  <!--
      v-model的原理: 上面代码会等效于如下代码
          - v-bind:value用于将n从数据绑定到页面上
          - @input监听输入框的修改, $event表示输入事件, $event.target.value当前input框的值
              并且在回调中修改n为$event.target.value
          这样就实现了双向绑定
  -->
  <input type="text" v-bind:value="n" @input="n = $event.target.value">

  <div><span>----------------------------------------------------------</span></div>

  <!-- 自定义组件也可以实现双向绑定 -->
  <ThreeInput v-model="weight" v-model:age="age" v-model:name="name"></ThreeInput>

  <!--  上面的代码会转换为如下的形式
           - 意思就是 父组件给子组件传递了props, 名为modelValue, name, age
               你通过v-model直接传递的是modelValue属性, 这个是固定了
               v-model:age传递的就是age属性
            - 父组件还会监听子组件的 update:modelValue, update:age, update:name 三个自定义事件
        所以你只要在
           - 在BeautifulInput组件中通过 props 来接受并展示属性
           - 在BeautifulInput组件中发布自定义事件来修改属性
   -->
  <!--  !!!!! 因为ThreeInput是自定义组件, 所以在监听子组件自定义事件的时候, $event就是子组件传递过来的属性-->
  <!--  !!!!! 而input是原生组件, 所有监听他的click的时候, $event就是原生的 Event事件对象 -->
  <!--
  <ThreeInput
      v-bind:modelValue="weight"
      @update:modelValue="weight = $event"

      v-bind:age="age"
      @update:age="age = $event"

      v-bind:name="name"
      @update:name="name = $event">
  </ThreeInput>
  -->
</template>

<script setup lang="ts">
import {ref} from "vue";
import ThreeInput from "./components/ThreeInput.vue";

let n = ref(0)

let weight = ref(100)
let name = ref("zhangsan")
let age = ref(18)
</script>


<style scoped>

</style>
