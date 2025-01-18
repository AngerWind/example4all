状态管理主要用在 如果多个组件都依赖同一组状态的情况下, 那么就需要再多个组件中共享状态, 所以需要一个东西来管理这些共享状态,  这里主要使用vuex来实现



1. 因为这里使用的是vue2, 所以在安装vuex的时候, 需要安装vuex3的版本

   对于vue3, 安装vuex4的版本

   ~~~shell
   npm i vuex@3
   ~~~

2. 需要再main.js中, 配置vuex插件

   ~~~js
   import Vuex from "vuex"
   Vue.use(Vuex)
   
   new Vue {
       el: "#app"
   }
   ~~~

   