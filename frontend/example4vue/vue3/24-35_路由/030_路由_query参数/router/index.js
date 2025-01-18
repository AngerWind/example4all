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
            component: Detail_vue_1.default
        }
    ]
});
// 暴露出去router
exports.default = router;
