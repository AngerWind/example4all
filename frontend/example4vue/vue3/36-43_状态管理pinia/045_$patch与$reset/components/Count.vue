<template>
  <div class="count">
    <h3>学校: {{ countStore.school }}, 地址: {{ countStore.address }}, sum: {{ countStore.sum }}</h3>
    <button @click="changeByPatch">$patch对象形式: 同时改学校和地址</button>
    <button @click="changeByPatchFn">$patch函数形式: 修改数组</button>
    <button @click="resetStore">$reset: 恢复初始值</button>
    <h4>爱好列表: {{ countStore.hobbies }}</h4>
  </div>
</template>

<script setup lang="ts" name="Count">
  import {useCountStore} from '../store/count'

  const countStore = useCountStore()

  // $patch: 批量修改state中的多个数据
  // 直接一个个赋值也行, 但是$patch在devtools里只产生一次变更记录, 且写法更聚合
  function changeByPatch(){
    // 对象形式: 传入要修改的键值对
    countStore.$patch({
      school:'尚硅谷',
      address:'北京',
      sum:888
    })
  }

  function changeByPatchFn(){
    // 函数形式: 拿到state, 可以做更复杂的修改(比如数组的增删)
    countStore.$patch((state) => {
      state.hobbies.push('打游戏')
    })
  }

  // $reset: 把state恢复到初始值(state()函数重新执行一遍)
  function resetStore(){
    countStore.$reset()
  }

</script>

<style scoped>
</style>
