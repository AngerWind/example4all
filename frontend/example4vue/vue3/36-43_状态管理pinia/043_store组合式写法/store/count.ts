import {defineStore} from 'pinia'

// 选项式API

// export const useCountStore = defineStore('count',{
//   // actions里面放置的是一个一个的方法，用于响应组件中的“动作”
//   actions:{
//     increment(value:number){
//       console.log('increment被调用了',value)
//       if( this.sum < 10){
//         // 修改数据（this是当前的store）
//         this.sum += value
//       }
//     }
//   },
//   // 真正存储数据的地方
//   state(){
//     return {
//       sum:3,
//       school:'atguigu',
//       address:'宏福科技园'
//     }
//   },
//   getters:{
//     bigSum:state => state.sum * 10,
//     upperSchool():string{
//       return this.school.toUpperCase()
//     }
//   }
// })

// 组合式API
import {ref, computed} from "vue"
export const useCountStore = defineStore("count", () => {
  let sum = ref(0) // 创建一个数据
  let school = ref("尚硅谷")
  let address = ref("北京")

  // 定义getter
  let bigSum = computed(() => {
    return school.toUpperCase();
  })
  let upperSchool = computed(() => sum * 10)

  // 定义一个actions
  function add() {
    sum.value ++
  }

  // 把数据交出去
  return {
    sum, add, bigSum, upperSchool, address
  }
})