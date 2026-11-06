//该文件专门用于创建整个应用的路由器

import VueRouter from "vue-router";
import About from "../pages/About.vue";
import Home from "../pages/Home.vue";


/**
 * 当我们点击路由的时候, 比如我们跳转到about的时候, 发现浏览器地址是 localhost:8080/#/about
 * 其中 #/about 被称为 hash, 用来标识前端路由
 *
 * Hash 模式利用了浏览器的一个特点： 修改 # 后面的内容不会导致浏览器向服务器请求一个新页面，也不会造成整页刷新。
 * 所以 Vue Router 会检测 hash 的变化，找到 /news 对应的组件，然后更新 RouterView
 *
 * 当我们刷新浏览器, 或者直接访问 localhost:8080/#/about 的时候, 浏览器重新请求localhost:8080/#/about的时候, 不会将 #/about 也一起发送给服务器的
 * 而是直接访问 localhost:8080/, 等服务器返回 index.html 后，Vue 应用启动，Vue Router 再读取 #/news 并显示新闻组件。
 * 因此，Hash 模式通常不需要服务器配置  **路由回退**。
 *
 * 优点
 *   - 部署简单。
 *   - 刷新或直接访问路由，一般不会出现服务器 404。
 *   - 适合无法修改服务器配置的静态托管环境。

 *   缺点
 *   - URL 中有 #，不够简洁。
 *   - 路由路径不是服务器真正识别的路径。
 *   - 对 SEO 和需要规范 URL 的场景不够友好。
 *   - 路由 hash 与页面锚点都使用 #，特殊场景下处理不够直观。
 */

/**
 * 还有另外一种history模式, 在这种模式下, 比如我们页面一开始的地址为 localhost:8080,
 * 当我们点击about时, 浏览器地址为 localhost:8080/about, 不会出现 # 号
 * 此时 Vue Router会阻止 <a> 标签默认的整夜跳转, 然后使用history api 把地址修改为 /about, 然后找到 /about对应的路由组件, 更新RouterView, 显示对应的页面
 * 整个过程中不会重新加载html页面
 *
 * 当用户点击前进, 后退按钮的时候, Vue Router 会监听 popstate 事件, 再根据新的 url 来更新组件
 *
 * history模式下只需要注意的是 **刷新和直接访问** 的时候
 * 假设用户在页面内从 /home 切换到 /news，此时只是前端修改 URL，没有向服务器请求 /news，所以没有问题。
 * 但如果用户：
 * - 在 /news 页面刷新浏览器；
 * - 直接输入 http://example.com/news
 * - 把这个地址发给其他人访问；
 * 浏览器就会真实地向服务器发送：
 *     GET /news
 * 如果服务器上不存在 news 文件或目录，并且没有进行特殊配置，服务器就会返回：
 *     404 Not Found
 *
 * 解决的办法就是服务器需要配置路由回退
 * 对于单页应用，服务器应该把前端路由请求回退到 index.html。
 * 以 Nginx 为例：
 *   server {
 *     listen 80;
 *     server_name example.com;
 *
 *     root /usr/share/nginx/html;
 *     index index.html;
 *
 *     location / {
 *       try_files $uri $uri/ /index.html;
 *     }
 *   }
 *
 *   这段配置表示：
 *   1. 请求的是真实存在的 JS、CSS、图片等文件时，直接返回文件。
 *   2. 请求 /news 等前端路由、找不到实际资源时，返回 index.html。
 *   3. Vue 应用启动后，Vue Router 读取 /news，显示对应组件。
 *   开发时 Vite 会自动处理这种回退，所以你在本地直接访问 /home 通常没有问题；部署到生产服务器时才需要特别关注这项配置。
 *
 *   优点
 *   - URL 简洁，没有 #。
 *   - URL 更符合常规网站的路径形式。
 *   - 更适合对链接形式、SEO 有要求的项目。
 *
 *   缺点
 *   - 部署时需要服务器配合。
 *   - 服务器未配置回退时，刷新或直接访问子路由可能返回 404。
 */

/**
 * 应该怎么选择？
 * 一般建议：
 *   - 本地开发, vite自动处理回退, 优先使用History模式
 *   - 能够配置生产服务器的路由回退的：优先使用 History 模式, 保证url优雅
 *   - 无法配置服务器，只希望部署后直接运行：使用 Hash 模式。
 *   - 静态的网页, 比如文档等等, 优先使用hash模式
 */

export default new VueRouter({
    mode: "hash", // 默认为hash
   routes:[
       {
           path:'/about', // 访问about路径的时候, 切换到About组件
           component: About
       },
       {
           path:'/home',
           component: Home
       }
   ]
});

