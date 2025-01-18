//引入Vue
import Vue from "vue";
//引入App
import App from './App';



// 引入创建好的Vuex.Store
import store from './store';

//关闭Vue的生产提示
Vue.config.productionTip = false;

new Vue({
    el: '#app',
    store, // 传入Vuex.Store到Vue实例中
    render: h => h(App),
});


