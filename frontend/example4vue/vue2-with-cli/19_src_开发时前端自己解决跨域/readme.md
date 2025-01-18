1. 跨域问题的产生

    跨域就是我们访问的前端是 http://localhost:5000
    
    但是在我们前段的网页给后端 http://localhost:8888 发送了请求

    只要前端和后端的 协议/域名/端口 其中三个有一个不一致, 就会产生跨域问题

    请求发送出去了, 后端也接受到了, 也返回给浏览器, 但是浏览器就是JS代码相应的结果

2. 解决办法

   1. 后端配置跨域
   2. 使用nginx方向代理
   3. 在开发的时候, 我们使用vue-cli脚手架启动一个前端, 这样我们就可以在本地访问网页了

        同时我们也可以配置vue-cli, 将不属于前端的请求, 转发到我们后端的服务器去
        
        这样我们就可以通过js请求前端vue-cli的地址, 然后vue-cli将请求转发给后端, 这样就解决了跨域的问题
        
        现在假设我们前段地址为localhost:4000, 后端的地址为localhost5000

        配置需要再项目的 `vue.config.js`中进行

        方式1

        ~~~js
        module.exports = {
            devServer: {
                proxy: "http://localhost:5000" // 将vue-cli不能处理的请求, 转发到后端的5000端口去
            } 
        }
        ~~~
      
        方式2

        ~~~js
        module.exports = {
            devServer: {
                proxy: {
                    "/atguigu": { // 转发所有/atguigu开始的请求
                        target: "http://localhost:5000", // 转发的地址
                        // 在转发的时候, 修改地址, key是一个真正表达式, value是要修改的值
                        pathRewrite: {"^/atguigu": ""},  // 如果请求路径为/atguigu/student, 那么他会修改为/student
                        ws: true, // 是否转发webScoket 请求
                        // 用于控制请求头中的host字段的值, 这个字段表示请求来自哪里
                        // 如果为false, 那么vue-cli在转发请求的时候, 会如实报告自己来源为localhost4000
                        // 如果为true, 那么在转发的时候, 他会将host修改为要转发的后端的host, 即localhost5000
                        // 这样的话就能够绕过一些限制
                        changeOrigin: true
                    },
                   "/heima": { // 可以将多个不同路径的请求, 转发给不同的后端
                       target: "http://localhost:8888"
                    }
                }
            }
        }
        ~~~
      配置了之后, 记得要修改JS代码中, 将后端的地址, 修改为vue-cli的地址
        