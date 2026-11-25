import {defineStore} from 'pinia'

export const useCountStore = defineStore('count',{
  state(){
    return {
      sum:6,
      school:'atguigu',
    }
  },
  // getters: store的计算属性
  // 当多个组件都需要"对state做同样加工"时, 把加工逻辑收拢到getters里, 不用每个组件各写一遍
  // state变化时, getters会自动重新计算
  getters:{
    // 写法1: 接收state参数, 通过state读取数据(可以使用箭头函数)
    bigSum:(state):number => state.sum * 10,

    // 写法2: 不接收state, 通过this读取数据(必须用普通函数, 箭头函数没有自己的this)
    upperSchool():string{
      return this.school.toUpperCase()
    },

    // getters里面也可以用其他的getters(通过this)
    bigBigSum():number{
      return this.bigSum * 10
    }
  }
})
