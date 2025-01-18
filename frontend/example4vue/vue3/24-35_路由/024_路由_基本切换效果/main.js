"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
// 引入createApp用于创建应用
var vue_1 = require("vue");
// 引入App根组件
var App_vue_1 = require("./App.vue");
// 引入路由器
var router_1 = require("./router");
// 创建一个应用
var app = (0, vue_1.createApp)(App_vue_1.default);
// 使用路由器
app.use(router_1.default);
// 挂载整个应用到app容器中
app.mount('#app');
