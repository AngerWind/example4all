//创建vm
import App from "./App";
//在main.js中绑定root元素
new Vue({
  el: "#root",
  template: `<App></App>`,
  components: {
    App,
  },
});
