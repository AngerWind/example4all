<template>
  <div class="app">
    <h2>Transition(单个元素/组件的过渡)</h2>
    <button @click="isShow = !isShow">切换显示</button>
    <!--
      Transition: 给"单个"元素的进入/离开加过渡
      name="hello"对应CSS中的hello-enter-active等类名
      appear表示页面首次渲染时也播放入场动画
    -->
    <Transition name="hello" appear>
      <h3 v-show="isShow">你好啊</h3>
    </Transition>

    <h2>TransitionGroup(列表的过渡)</h2>
    <input v-model="keyword" placeholder="输入名字, 回车添加" @keyup.enter="addPerson">
    <!--
      TransitionGroup: 给v-for列表的每一项加过渡(新增/删除/排序变化时都有动画)
      tag="ul"表示渲染成ul标签(默认渲染成Fragment没有标签)
      注意: 列表的每一项必须有唯一的key
    -->
    <TransitionGroup name="list" tag="ul">
      <li v-for="p in personList" :key="p.id">{{ p.name }}</li>
    </TransitionGroup>
  </div>
</template>

<script setup lang="ts" name="App">
  import {ref,reactive} from 'vue'
  import {nanoid} from 'nanoid'

  const isShow = ref(true)

  const keyword = ref('')
  const personList = reactive([
    {id:nanoid(),name:'张三'},
    {id:nanoid(),name:'李四'},
  ])
  function addPerson(){
    if(!keyword.value.trim()) return
    personList.unshift({id:nanoid(),name:keyword.value})
    keyword.value = ''
  }

</script>

<style scoped>
  /* Transition的类名(name="hello") */
  .hello-enter-active,
  .hello-leave-active {
    transition: all 0.5s ease;
  }
  .hello-enter-from,
  .hello-leave-to {
    opacity: 0;
    transform: translateX(-100%);
  }

  /* TransitionGroup的类名(name="list") */
  .list-enter-active,
  .list-leave-active {
    transition: all 0.5s ease;
  }
  .list-enter-from,
  .list-leave-to {
    opacity: 0;
    transform: translateX(30px);
  }
  /* 让正在离开的元素脱离文档流, 其他元素才能平滑移动 */
  .list-leave-active {
    position: absolute;
  }
  .list-move {
    transition: transform 0.5s ease;
  }
</style>
