"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var vue_router_1 = require("vue-router");
var Home_vue_1 = require("@/pages/Home.vue");
var About_vue_1 = require("@/pages/About.vue");
var router = (0, vue_router_1.createRouter)({
    history: (0, vue_router_1.createWebHashHistory)(),
    routes: [
        {
            path: '/home',
            component: Home_vue_1.default
        },
        {
            path: '/about',
            component: About_vue_1.default
        },
    ]
});
// 暴露出去router
exports.default = router;
