//该文件专门用于创建整个应用的路由器

import VueRouter from "vue-router";
import About from "@/pages/About";
import Home from '@/pages/Home';
import News from "@/pages/News";
import Message from "@/pages/Message";
import Detail from "@/pages/Detail";

//创建并默认暴露一个路由器
let router = new VueRouter({
    routes: [
        {
            name: 'about',
            path: '/about',
            component: About,
            // meta:路由元信息，可以配置一些自定义的元信息
            meta: {
                isAuth: true,
                title: 'about'
            },
        },
        {
            path: '/home',
            component: Home,
            name: "name",
            meta: {
                isAuth: false,
                title: 'home'
            }
        }
    ]
});

export default router

