import Vue from "vue";
import App from "./App.vue";

Vue.config.productionTip = false;

new Vue({
  render: (h) => h(App), // 相当于 template: '<App/>'
}).$mount("#app");
