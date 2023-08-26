<template>
  <div>
    <button @click="getStudents">获取学生信息</button>
    <button @click="getCars">获取汽车信息</button>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: "App",
  methods:{
     getStudents(){
       axios.get('/api/students') //请求前端服务器, 前端服务器把请求转发给后端
       .then(res => console.log(res.data))
       .catch(e => console.log(`msg: ${e.message}, stack: ${e.stack}`));
    },
    getCars(){
      axios.get('/demo/cars') //同理
          .then(res => console.log(res.data))
          .catch(e => console.log(`msg: ${e.message}, stack: ${e.stack}`));
    }
  }
}
</script>



