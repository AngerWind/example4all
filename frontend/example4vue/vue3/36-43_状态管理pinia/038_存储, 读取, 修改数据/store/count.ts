import {defineStore} from 'pinia'

export const useCountStore = defineStore('count',{
  // actions里面放置的是一个一个的方法，用于响应组件中的“动作”
  actions:{
    increment(value){
      console.log('increment被调用了',value)
      if( this.sum < 10){
        // 修改数据（this是当前的store）
        this.sum += value
      }
    }
  },
  // 真正存储数据的地方
  state(){
    return {
      sum:6,
      school:'atguigu',
      address:'宏福科技园'
    }
  },
  // 还可以定义一个getter, 类似于计算属性
  getters:{
    // 可以接受一个state, 通过这个state来获取定义的数据和方法
    bigSum: function(state){
      return state.sum * 10
    },
    // 或者你也可以不接受state, 直接通过this来调用, 这个this就是state
    upperSchool: function(){
      return this.school.toUpperCase()
    }
  }
})