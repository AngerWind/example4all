import { defineStore } from 'pinia'

export const useCountStore = defineStore('count', {
  state() {
    return {
      sum: 6
    }
  },
  actions: {
    increment(value: number) {
      this.sum += value
    },
    decrement(value: number) {
      this.sum -= value
    },
    reset() {
      this.sum = 6
    }
  }
})
