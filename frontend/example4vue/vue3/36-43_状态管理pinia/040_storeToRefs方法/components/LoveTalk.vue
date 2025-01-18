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
import {storeToRefs} from "pinia"

// 获取store
const talkStore = useTalkStore()
// 读取状态
console.log(talkStore.talkList[0].id, talkStore.talkList[0].title)

// 如果你觉得每次读取状态 都要通过talkStore.xxx来去的话, 太麻烦了
// 那么你可以通过storeToRefs()函数, 将这些属性结构出来
let {talkList} = storeToRefs(talkStore)


</script>

<style scoped>
.talk {
  background-color: orange;
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0 0 10px;
}
</style>