const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");
require("./db/db") // 加载db

const app = express();
const port = 5000;

app.use(cors()); // 跨域相关
app.use(express.json()); //
app.use(bodyParser.json()); // 当context-type为application/json的时候将request body转换为json格式
app.use(bodyParser.urlencoded({ extended: true })); // 当context-type为application/x-www-form-urlencoded将请求体转换为url格式


const userRouter = require("./router/user")
app.use(userRouter.path, userRouter.router); // 定义router


app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`);
});
