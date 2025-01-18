//该文件专门用于创建整个应用的路由器

import VueRouter from "vue-router";
import About from "../pages/About.vue";
import Home from "../pages/Home.vue";


//创建并默认暴露一个路由器
export default new VueRouter({
   routes:[
       {
           path:'/about', // 访问about路径的时候, 切换到About组件
           component: About
       },
       {
           path:'/home',
           component: Home
       }
   ]
});

