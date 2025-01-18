//引入Vue
import Vue from "vue";
//引入App
import App from './App';

import { mixin, shareData } from "./mixin";

//关闭Vue的生产提示
Vue.config.productionTip = false;

/**
 * 如果多个组件有相同的data, method, 生命周期, 我们就可以把这个抽取出来, 作为一个mixin
 * 然后将组件的配置与mixin混合, 相当于继承这些通用的配置
 */

// 全局混合, 相当于给全局的vue实例添加了默认的配置项
// Vue.mixin(mixin);
// Vue.mixin(shareData);

new Vue({
    el:'#app',
    render: h => h(App)
});
