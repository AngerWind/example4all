// 对axios二次封装: 使用请求和响应拦截器
import axios from 'axios'
// @ts-expect-error 没有类型声明文件
import { ElMessage } from 'element-plus'

// 创建axios实例, 并且设置基础路径和超时时间
const request = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API as string, // 基础路径
  timeout: 5000, // 请求超时时间
})
console.log(import.meta.env.VITE_APP_BASE_API)

// 设置请求拦截器
request.interceptors.request.use((config) => {
  // 必须要返回config, 该对象有header属性, 可以设置请求头
  config.headers.token = '123456'
  return config
})

// 设置响应拦截器, 传入两个函数, 一个是成功的回调, 一个是失败的回调
request.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    // 失败回调, 处理http网络错误
    let message = ''
    const states = error.response.states
    switch (states) {
      case 401:
        message = '登录过期, 请重新登录'
        break
      case 403:
        message = '没有权限, 请联系管理员'
        break
      case 404:
        message = '请求资源不存在'
        break
      case 500:
        message = '服务器内部错误'
        break
      default:
        message = '未知错误'
        break
    }
    // 提示错误信息
    ElMessage({
      type: 'error',
      message,
    })
    // 返回一个失败的promise对象
    return Promise.reject(error)
  },
)

export default request
