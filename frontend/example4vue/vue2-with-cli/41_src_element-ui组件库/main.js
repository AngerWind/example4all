//引入Vue
import Vue from "vue";
//引入App
import App from './App';

// 安装element ui
// npm i element-ui -S

/*
完整引入element的所有内容

import ElementUI from 'element-ui'; //引入element-ui组件库
import 'element-ui/lib/theme-chalk/index.css'; //引入element全部样式
Vue.use(ElementUI); //使用element ui插件库

使用这种方式, ElementUI里面的一百多个组件都可以随便使用, 样式也可以随便使用
缺点是页面打包后的体积有7M多, 大的吓人
*/

/**
 * 想要按需引入的话, 那么要安装一个插件
 * npm install babel-plugin-component -D
 *
 * 然后在 babel.config.js 中添加如下内容
 *     {
 *   "presets": [["es2015", { "modules": false }]],
 *   "plugins": [
 *     [
 *       "component",
 *       {
 *         "libraryName": "element-ui",
 *         "styleLibraryName": "theme-chalk"
 *       }
 *     ]
 *   ]
 * }
 */
// 按需导入使用到的组件, 然后注册
import {Button, Input, Row, DatePicker} from 'element-ui';

Vue.use(Button);
Vue.use(Input);
Vue.use(Row);
Vue.use(DatePicker);

//关闭Vue的生产提示
Vue.config.productionTip = false;

new Vue({
    el: '#app',
    render: h => h(App),
});



