## setup语法糖

在不使用setup, 我们可以这样来定义一个组件

~~~vue
<template>
  <!-- 使用子组件 -->
  <Demo></Demo>

  <!-- 使用setup中的数据和方法 -->
  <span @click="showHelloMsg">{{a}}</span>
</template>

<script>
import Demo from "./components/Demo"; // 导入子组件
export default {
  name: 'App',
  components: {Demo}, // 注册子组件
  setup(props, context){
    let a = ref(0)
    function showHelloMsg(){
        alert("你好");
    }
    return { // 返回需要在页面上使用的数据和方法
		a,
        showHelloMsg
    }
  }
}
</script> 
~~~

但是现在在vue3中, 提供了setup语法糖, 你可以在<script>标签上标注一个 setup, 然后在script中编写的代码就等于在setup()中编写的代码, 同时也不需要再setup中return一个对象, 然后才可以在模板中调用属性和方法了

~~~vue
<template>
  <!-- 使用子组件 -->
  <Demo></Demo>

  <!-- 使用setup中的数据和方法 -->
  <span @click="showHelloMsg">{{a}}</span>
</template>
<!-- 注意这里要加一个setup 属性 -->
<script setup> 
    
import { ref, reactive } from 'vue';
import Demo from "./components/Demo"; // 使用到的组件不再需要再components中注册了, 可以直接在模板中使用

let a = ref(0) // 不需要在setup(){}将属性return出去了, 可以直接在模板中使用这个属性
function showHelloMsg(value) { // 可以在模板中直接使用这个方法
  alert(`你好,${value}`);
}

</script>
~~~

**如果你还有一些代码不想写在setup语法糖中, 那么你可以另外再写一个不带setup的script标签, 两个script标签可以同时存在**

~~~vue
<template>

</template>

<script>
 // 一些不想写在setup中的代码 
import Demo from "./components/Demo"; 
export default {
  name: 'App',
  components: {Demo}, 
  data(){
      return {
          
      }
  },
  method: {
      
  }
}
</script> 
<script setup>
// 两个script标签可以同时存在
</script>
~~~





## setup语法糖产生的问题

https://blog.csdn.net/XianZhe_/article/details/134211764

有了setup语法糖, 那么就不需要使用如下的形式了

~~~vue
<script>
    export default {
        name: "xxx",
        setup(){
            // some code...
        }
    }
</script>
~~~

但是这也就导致了一个问题, **如果你想要定义组件名, 会没有地方定义**

有如下的几种解决方式

1. 再定义一个不带setup的script标签, 然后在其中定义name属性

   ~~~vue
   <template>
   
   </template>
   
   <script>
   export default {
     name: 'xxx' // 指定name属性
   }
   </script> 
   
   <script setup>
   // ....
   </script>
   ~~~

2. 使用[vite-plugin-vue-setup-extend](https://github.com/vbenjs/vite-plugin-vue-setup-extend)插件

   1. 安装插件

      ~~~shell
      npm i vite-plugin-vue-setup-extend -D
      ~~~

   2. 在 `vite.config.ts`中配置插件

      ~~~js
      import { defineConfig, Plugin } from 'vite'
      import vue from '@vitejs/plugin-vue'
      import vueSetupExtend from 'vite-plugin-vue-setup-extend'
      
      export default defineConfig({
        plugins: [
            vue(), 
            vueSetupExtend() // 配置这个插件
        ],
      })
      ~~~

   3. 然后就可以在`<script setup>`标签上添加一个name属性, 用来指定组件名了

      ~~~vue
      <template>
        <div>hello world {{ a }}</div>
      </template>
      
      <!-- 通过name属性来定义组件名 -->
      <script lang="ts" setup name="App">
        const a = 1
      </script>
      ~~~

3. 如果你是vue3.3之后的版本, 那么可以在setup中通过`defineOptions`这个函数来定义组件名

   ~~~vue
   <script lang="ts" setup>
     defineOptions({
       name: "my-component" // 设置组件名
     })
     
     /* 业务代码 */
   </script>
   ~~~

   

