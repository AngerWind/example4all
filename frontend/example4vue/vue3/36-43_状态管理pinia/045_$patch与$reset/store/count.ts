import {defineStore} from 'pinia'

export const useCountStore = defineStore('count',{
  state(){
    return {
      sum:6,
      school:'atguigu',
      address:'宏福科技园',
      hobbies:['学习','吃饭']
    }
  }
})
