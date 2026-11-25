<template>
  <div class="app">
    <h2>环境变量与打包</h2>
    <h3>应用名: {{ appName }}</h3>
    <h3>接口地址: {{ baseUrl }}</h3>
    <h3>当前模式: {{ mode }}</h3>
    <h3>是否开发环境: {{ isDev }}</h3>
    <!--
      原理:
      1. 项目根目录放.env / .env.development / .env.production等文件
      2. Vite启动/打包时, 根据当前模式(development/production)加载对应的文件
         .env所有环境都加载, .env.development只在dev时加载且覆盖.env的同名变量
      3. 代码中通过import.meta.env.VITE_xxx读取(只有VITE_前缀的变量才会暴露, 防止密钥泄露到前端)
      4. 打包: npm run build, 产物在dist目录, 是静态文件, 丢到nginx/对象存储即可
    -->
  </div>
</template>

<script setup lang="ts" name="App">
  // import.meta.env上除了自定义变量, 还有几个内置变量:
  //   MODE: 当前模式(development/production)
  //   DEV/PROD: 是否开发/生产环境(布尔值)
  //   BASE_URL: 应用的基础路径
  const appName = import.meta.env.VITE_APP_NAME
  const baseUrl = import.meta.env.VITE_API_BASE_URL
  const mode = import.meta.env.MODE
  const isDev = import.meta.env.DEV

</script>

<style scoped>
</style>
