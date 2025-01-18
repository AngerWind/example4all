// 创建user相关的共享状态
import { defineStore } from 'pinia'

export const useUserStore = defineStore('User', {
  // 存储状态
  state: () => {
    return {
      token: localStorage.getItem('token'),
      avatar: '',
      username: '',
    }
  },
  // 保存逻辑的地方, 可以写异步方法
  actions: {
    updateToken(token: string) {
      // 修改共享状态中的token
      this.token = token
      // 保存token在localStorage中
      localStorage.setItem('token', token)

      // 将token放在cookie中也可以, 每次通过cookie= "xxx"都是添加了一个cookie, 要删除一个cookie可以通过设置expires为一个过去的时间
      // 读取document.cookie会返回一个分号隔开的如下形式的字符串: "c1=v1; c2=v2"
      // document.cookie = `token=${token}; path=/; expires=${new Date(
      //   new Date().getTime() + 3600 * 24 * 2, // 两天后过期
      // )}`
    },
    clear() {
      this.token = ''
      localStorage.removeItem('token')
      this.avatar = ''
      this.username = ''
    },
  },
  // 类似computed的属性
  getters: {},
})
