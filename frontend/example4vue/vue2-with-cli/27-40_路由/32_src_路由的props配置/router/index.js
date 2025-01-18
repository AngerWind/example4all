//该文件专门用于创建整个应用的路由器

import VueRouter from "vue-router";
import About from "@/pages/About";
import Home from '@/pages/Home';
import News from "@/pages/News";
import Message from "@/pages/Message";
import Detail from "@/pages/Detail";

//创建并默认暴露一个路由器
export default new VueRouter({
   routes:[
       {
           name: 'about',
           path:'/about',
           component: About
       },
       {
           path:'/home',
           component: Home,
           children:[
               {
                   path: 'news',
                   component: News
               },
               {
                   path: 'message',
                   component: Message,
                   children:[
                       {
                           name: 'particulars',
                           path: 'detail/:id/:title',
                           component: Detail,
                           /**
                            * 使用props的理由是, 在使用query参数和路径参数的时候, 一直要this.$route.params.xxx和this.$route.query.xxx
                            * 特别的不方便, 所以你可以将路径参数和query参数, 转换为第三节中讲到的 props 参数, 然后就可以直接使用props参数了
                            */

                           //props的第一种写法值为对象,该对象的所有key-value都会以props的形式传给detail组件(死数据), 不会这样用
                           // props:{
                           //     a.js:1,
                           //     b:'hello'
                           // }

                           // props的第二种写法，值为布尔值,
                           // 如果布尔值为真，就会把该路由组件收到的所有params参数以props的形式传递给detail组件
                           // (注意如果是query参数不会奏效的)
                           // props: true

                           // props的第三种写法,值为函数
                           // 该函数可以接收到 $route对象, 然后你就可以将query参数和路径参数提取出来, 通过 props 参数的形式传入进去,
                           // 然后就可以直接通过props参数来使用了
                           props: function($route){
                               return {
                                   id: $route.query.id,
                                   title: $route.query.title,
                               }
                           }

                           // !!!!! 记得在Detail中定义要接受的props参数 !!!!
                       }
                   ],
               }
           ]
       }
   ]
});

