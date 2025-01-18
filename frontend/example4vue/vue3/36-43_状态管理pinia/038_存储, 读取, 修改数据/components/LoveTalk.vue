<template>
  <div class="talk">
    <button @click="getLoveTalk">获取一句土味情话</button>
    <ul>
      <li v-for="talk in talkStore.talkList" :key="talk.id">{{ talk.title }}</li>
    </ul>
  </div>
</template>

<script setup lang="ts" name="LoveTalk">
import {useTalkStore} from '@/store/loveTalk'

// 获取store
const talkStore = useTalkStore()

// 读取状态
console.log(talkStore.talkList[0].id, talkStore.talkList[0].title)

// 修改数据的三种方式
// 方式1, 直接修改
function change() {
  talkStore.talkList[0].title = "哈哈哈"
}

// 方式2, 通过$patch方法, 这个方法接受一个对象, 这个对象可以覆盖store中的数据的状态, 适合要修改多个属性时使用
function change1() {
  talkStore.$patch({
    taskList: [
      {id: 'ftrfasdf01', title: '今天你有点怪，哪里怪？怪好看的！'},
      {id: 'ftrfasdf02', title: '草莓、蓝莓、蔓越莓，今天想我了没？'},
      {id: 'ftrfasdf03', title: '心里给你留了一块地，我的死心塌地'}
    ]
  })
}

// 方式3, 在count中定义actions, 然后在这里调用actions来修改状态
function change2() {
  talkStore.getATalk() // 调用actions来修改数据
}

</script>

<style scoped>
.talk {
  background-color: orange;
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0 0 10px;
}
</style>