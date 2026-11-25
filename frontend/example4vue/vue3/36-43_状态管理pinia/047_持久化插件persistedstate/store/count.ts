import {defineStore} from 'pinia'

export const useCountStore = defineStore('count',{
  state(){
    return {
      sum:6,
      // token这种敏感数据不想持久化, 可以在persist里排除掉
      token:''
    }
  },
  // persist: true表示这个store的所有state都持久化(默认存localStorage, key默认是store的id)
  // persist: true,
  persist:{
    // pick: 只持久化指定的字段
    pick:['sum'],
    // 也可以自定义存储位置和key
    // storage: sessionStorage,
    // key: 'my-count',
  }
})
