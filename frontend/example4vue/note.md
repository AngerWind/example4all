#### 深度选择器与scoped

先说说scoped, 该属性的作用是保证在style中定义的css只在当前组件中起效

原理是:  

1. 对于当前组件中的所有标签, 都添加上一个data-v-xxx的属性
2. 在\<style scoped>\<style scoped>中定义的css选择器, 在被编译后都会被添加上data-v-xxx这样一个属性选择器

以下面的代码演示

~~~vue
// App.vue
<template>
  <div class="app">
    这里是app.vue
    <HelloWorld class="hello"/>
  </div>
</template>
<style scoped>
.app {
  background-color: bisque;
  .hello { color: green; }
  .xx { color: gray; }
}
</style>

// HelloWorld.vue
<template>
<div class="hw">
  这里是hello world
  <div class="xx">
    这里是hw中的xxx
  </div>
</div>
</template>
<style scoped>
.hw { color: red; }
.xx { color: blue; }
</style>
~~~

![image-20230625233536262](img/note/image-20230625233536262.png)

可以看到app.vue中的所有组件都被赋予了一个data-v-7a7a37b1这个属性, 同时在app.vue中\<script scoped>\<script scoped>定义的所有css都被添加上了一个属性选择器, 这样就可以保证在scoped中定义的css只在当前组件中生效



**但是特别需要注意的是, 对于app.vue中的子组件, 比如这里的helloworld组件, 只有第一层会带上这个data-v-xx的属性,  helloworld组件中的xxx这一层并没有带上app.vue中的属性选择器**

这样就导致我们定义在helloworld组件上的css选择器只能够影响其第一层, 而无法影响其内部的样式

如果要影响其内部的样式, 比如我想在app.vue中定义xxx的字体颜色, 有两种办法

1. 使用全局样式, 具体做法就是在app.vue中再添加一个\<style>标签, 但是这个标签不要添加scoped关键字

2. 使用深度选择器, 具体做法就是

   ~~~vue
   // App.vue
   <template>
     <div class="app">
       这里是app.vue
       <HelloWorld class="hello"/>
     </div>
   </template>
   <style scoped>
   .app {
     background-color: bisque;
     .hello { color: green; }
     :deep(.xx) { color: gray; } // 使用深度选择器
   }
   </style>
   ~~~

   ![image-20230625234617809](img/note/image-20230625234617809.png)

深度选择器的作用就是 **将原来的属性选择器不要放在后面, 而是放在前面, 表示后代选择器**



#### 递归使用组件

在vue中, 可以递归使用组件, 在使用的时候, **文件名就是组件名**, 但是也可以给组件另外一个名字, 做法如下



**另起一个script标签**, 内容如下:

~~~vue
<script lang="ts"> // 这里注意不要setup关键字
 export default {
   name: 'MenuItemList', // 要想递归使用当前组件, 组件必须有name属性
 }
</script>
~~~



#### 在js中使用scss中定义的全局变量

https://juejin.cn/post/7155377433951240229

https://blog.csdn.net/hp9999/article/details/126338799