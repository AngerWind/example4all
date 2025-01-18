/**
 * 通过这种方式, 实际上引入的是 vue/dist/vue.runtime.ems.js文件
 * 这个文件是一个runtime版本的vue, 也就是残缺版的, 没有模板解析器, 所有new Vue({})里面不能配置template字段
 *
 * 这里使用残缺版的vue的原因是, 整个模板解析器占完整版vue文件大小的三分之1, 有100多kb
 * 为了节省空间, 使用了残缺版的vue
 *
 * 此时有两种解决办法:
 *    1. 引入一个完整版的vue文件, 然后通过template来指定要渲染的内容
 *        import Vue from 'vue/dist/vue';
 *    2. 通过 render来指定要渲染的内容
 */
import Vue from "vue";
import App from "./App.vue";

new Vue({
  el: "#app",
  /**
   * 实际上 render 是一个函数, 用于指定需要渲染的内容
   * 完整版的写法是:
   *    render: function(createElement){
   *        return createElement("h1", "哈哈") // 渲染一个 <h1>haha</h1> 到页面上
   *    }
   * 简略版的写法就是
   *    render: (h) => h(App) // 渲染一个 <App></App> 到页面上, 等效于 template: "<App></App>"
   */
  render: (h) => h(App),
}).$mount("#app");
