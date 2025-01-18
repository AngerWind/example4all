import { defineStore } from 'pinia'

// 定义左侧菜单栏的折叠状态
export const useFoldStore = defineStore('Fold', {
  state: () => ({
    fold: false,
  }),
  actions: {
    change() {
      this.fold = !this.fold
    },
  },
})
