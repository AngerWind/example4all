import {defineStore} from 'pinia'
import {nanoid} from 'nanoid'

// actions中发异步请求是实际项目的主要用法:
//   组件只负责调用action, 请求、loading、错误处理、修改state都收拢在store里
export const useTalkStore = defineStore('talk',{
  state(){
    return {
      talkList:[] as {id:string,title:string}[],
      loading:false
    }
  },
  actions:{
    // action可以声明成async, 在里面await异步操作, 完成后用this修改state
    async getATalk(){
      try {
        this.loading = true
        // 假装这里是axios发请求, 用定时器模拟500毫秒的接口耗时
        const title:string = await new Promise((resolve) => {
          setTimeout(() => resolve('你上辈子一定是碳酸饮料吧, 为什么我一看到你就开心得冒泡'), 500)
        })
        // 真实项目中是: let {data:{content:title}} = await axios.get('https://api.xxx.com')
        this.talkList.unshift({id:nanoid(),title})
      } finally {
        this.loading = false
      }
    }
  }
})
