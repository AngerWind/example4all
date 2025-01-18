"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var vue_router_1 = require("vue-router");
var Detail_vue_1 = require("@/pages/Detail.vue");
var router = (0, vue_router_1.createRouter)({
    history: (0, vue_router_1.createWebHistory)(),
    routes: [
        {
            name: 'xiang',
            path: 'detail',
            component: Detail_vue_1.default,
            // 第一种写法：将路由收到的所有 params 参数作为props传给路由组件
            // 但是缺点是 query 参数不会作为 props 进行传递
            // props:true,
            // 第二种写法：函数写法
            // props会接受到一个route参数, 这个参数就是路由到的组件的route
            props: function (route) {
                // 可以通过route.query.xxx 和 route.params.xxx 来获取query参数和params参数
                return {
                    // 可以在这里解析route, 然后返回一个对象
                    // 这个对象的属性会作为 props 参数传递给路由组件
                    id: route.params.id,
                    title: route.params.title,
                    content: route.params.content // 解析params参数, 并通过props传递给路由组件
                };
            }
            // 第三种写法：对象写法，直接返回死数据, 作为 props 传递给路由组件 (不使用!!!!)
            // props:{
            //   id:100,
            //   title:200,
            //   content:300
            // }
        }
    ]
});
// 暴露出去router
exports.default = router;
