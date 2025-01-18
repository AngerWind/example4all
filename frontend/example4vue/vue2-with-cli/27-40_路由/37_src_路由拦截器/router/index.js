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
            // 针对单个组件的拦截器
            // 只有在跳转到about组件的时候, 才会执行这个方法
            beforeEnter(to,from,next){
                const { isAuth } = to.meta;
                if(isAuth){
                    // 代表需要鉴权
                    if(localStorage.getItem('school') === 'wust1')
                        next(); // 执行跳转
                    else
                        alert('无权限');
                }else{
                    next();
                }
            }
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

// 全局前置路由守卫
// 初始化和在每一次路由切换之前被调用
router.beforeEach((to, from, next) => {
    // console.log(to, from);
    console.log('前置路由守卫');
    const {isAuth} = to.meta;
    if (isAuth) {
        if (localStorage.getItem("name") !== "zhangsan") {
            alert("name不是zhangsan, 不能跳转")
        } else {
            next(); // next()表示执行跳转
        }
    } else {
        next();
    }
});

//全局后置路由守卫
//初始化和在每一次路由切换之后被调用
router.afterEach((to, from) => {
    console.log('后置路由守卫', to, from);
    //点击每一个路由都切换下页面的document.title
    const {title} = to.meta;
    document.title = title || 'vue-advance';
})


export default router

