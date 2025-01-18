"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var vue_router_1 = require("vue-router");
var Home_vue_1 = require("@/pages/Home.vue");
var News_vue_1 = require("@/pages/News.vue");
var router = (0, vue_router_1.createRouter)({
    history: (0, vue_router_1.createWebHistory)(),
    routes: [
        {
            name: 'zhuye',
            path: '/home',
            component: Home_vue_1.default
        },
        {
            name: 'xinwen',
            path: '/news',
            component: News_vue_1.default,
        },
        {
            path: '/',
            redirect: '/home' // 重定向, 已访问/, 就自动跳转到/home, 这样一开始就可以进行默认的路由了
        }
    ]
});
// 暴露出去router
exports.default = router;
