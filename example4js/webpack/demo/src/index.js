import { sayH1 } from "./m1";
import { sayH2 } from "./m2";
import "./style/index.css"; // 因为webpack中使用了css-loader, 所以这里引入不会报错, css-loader会将css存放在js字符串中
import picture from "./assert/resource/R.jpg"; // 对于图片资源, 直接导入, 返回的是图片路径

sayH1();
sayH2();

document.body.insertAdjacentHTML("beforeend", `<img src=${picture}></img>`); // 使用图片

// babel会将这个转换为function的形式
document.body.onclick = () => {
  alert("你点我干嘛!!!");
};
