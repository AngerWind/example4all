import express from 'express';
import bodyParser from 'body-parser';
import {url, port, fileUpload, download, fileDownload} from "./config.js"
import cors from "cors"
import { fileURLToPath } from "url";

import multer from "multer"
import path from "path";

// 延迟函数
const delay = (ms) => new Promise((resolve) => setTimeout(resolve, ms));


const app = express();

// 使用 bodyParser 中间件解析 JSON 请求体
app.use(bodyParser.json());
app.use(cors()); // 解决跨域

// 设置文件上传时, 文件保存的目录和文件名
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, './tmp/'); // 存储到项目下的 'tmp' 文件夹
    },
    filename: (req, file, cb) => {
        cb(null, file.originalname); // 使用原文件名保存
    }
});
const upload = multer({storage: storage});

app.all(url, async (req, res) => {
    console.log('Request Method:', req.method);
    console.log('Query String:', req.query);
    console.log('Headers:', req.headers);
    console.log("Body", req.body)
    console.log("----------------------------------------------------")

    await delay(1000); // 延迟返回数据

    res.json({
        method: req.method,
        query: req.query,
        headers: req.headers,
        body: req.body
    });
});

// 文件上传接口, 在调用这个接口的时候, 会自动保存文件到指定的位置, 所以在接口中不需要处理文件的保存
app.post(fileUpload, upload.any(), function (req, res) {
    // 上传的文件在req.files中
    req.files.forEach((file, index) => {
        console.log(file.fieldname, file.originalname)
    })
    res.send({
        message: '文件上传成功'
    });
})


// 文件下载接口
app.get(`${fileDownload}/:filename`, (req, res) => {
    const __filename = fileURLToPath(import.meta.url);
    const __dirname = path.dirname(__filename); // 获取当前文件的目录
    const fileDirectory = path.join(__dirname, 'files'); // 获取当前目录下的files文件夹
    const filename = req.params.filename; // 从 URL 中获取文件名
    const filePath = path.join(fileDirectory, filename); // 构造文件路径

    // 检查文件是否存在
    res.sendFile(filePath, (err) => {
        if (err) {
            res.status(404).send('文件未找到');
        }
    });
});

// 启动服务器
app.listen(port, () => {
    console.log(`Server is running on http://localhost:${port}`);
});
