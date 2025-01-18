//该文件专门用于创建整个应用的路由器

import VueRouter from "vue-router";
import About from "../pages/About.vue";
import Home from "../pages/Home.vue";


/**
 * 当我们点击路由的时候, 比如我们跳转到about的时候, 发现浏览器地址是 localhost:8080/#/about
 * 其中 #/about 被称为 hash
 * 当我们刷新浏览器的时候, 浏览器重新请求localhost:8080/#/about的时候, 是不会将 #/about 也一起发送给服务器的
 * 这种方式被称为hash模式
 * 使用这种模式, 连接会有一点丑陋, 但是兼容性比较好
 *
 * 还有另外一种history模式, 在这种模式下, 比如我们页面一开始的地址为 localhost:8080,
 * 当我们点击about时, 浏览器地址为 localhost:8080/about, 不会出现 # 号
 * 这种模式的地址比较美观, 但是兼容性略微差一点
 *
 * 其中history模式还有一个致命的缺点, 就是当我们重新刷新浏览器的时候, 浏览器会访问 localhost:8080/about,
 * 这就会导致服务器无法返回正确的资源, 因为我们只有访问 localhost:8080 才会返回正确的页面
 * 但是访问 localhost:8080的话, 又会丢失路由的信息
 *
 * 解决的办法是需要配置服务器来正确的识别路径 (java和nginx的解决办法不知道, 老师没讲)
 *
 *
 * 总结: 别他妈的用history模式, 自讨没趣
 */
export default new VueRouter({
    mode: "hash", // 默认为hash
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

