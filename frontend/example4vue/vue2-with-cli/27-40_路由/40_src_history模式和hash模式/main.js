//引入Vue
import Vue from "vue";
//引入App
import App from './App';

//引入vue-router
import VueRouter from "vue-router";

// 导入创建的router
import router from './router';

//关闭Vue的生产提示
Vue.config.productionTip = false;

//应用vue-router插件
Vue.use(VueRouter);

new Vue({
    el: '#app',
    render: h => h(App),
    router // 使用了VueRouter之后, 就可以传入一个router属性
});



