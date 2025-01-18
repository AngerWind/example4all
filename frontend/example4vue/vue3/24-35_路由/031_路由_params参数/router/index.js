"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var vue_router_1 = require("vue-router");
var Detail_vue_1 = require("@/pages/Detail.vue");
var router = (0, vue_router_1.createRouter)({
    history: (0, vue_router_1.createWebHistory)(),
    routes: [
        {
            name: 'xiang',
            // 使用路径参数的时候, 必须要在path中定义参数, 否则匹配不到
            // content后面添加一个?, 表示可传可不传
            path: 'detail/:id/:title/:content?',
            component: Detail_vue_1.default
        }
    ]
});
// 暴露出去router
exports.default = router;
