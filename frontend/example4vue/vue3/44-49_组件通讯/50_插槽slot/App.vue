<template>
  <div class="app">
    <h2>插槽slot: 父组件往子组件的指定位置传递"结构"(html)</h2>

    <!-- 1. 默认插槽: 子组件没有给插槽起名字, 父组件传递的结构直接放进<slot>的位置 -->
    <Child>
      <p>这是父组件传递的默认插槽内容</p>
    </Child>

    <!-- 2. 具名插槽: 子组件的<slot name="xxx">起了名字, 父组件用<template v-slot:xxx>指定往哪个插槽放 -->
    <Child>
      <!-- v-slot:header也可以简写成#header -->
      <template #header>
        <h3>这是header插槽的内容</h3>
      </template>
      <template #footer>
        <h3>这是footer插槽的内容</h3>
      </template>
    </Child>

    <!-- 3. 作用域插槽: 数据在子组件里, 但是展示的结构由父组件决定 -->
    <!-- 子组件通过<slot :list="xxx">把数据传给插槽, 父组件用v-slot="xxx"接收 -->
    <Child>
      <template #game="scope">
        <ul>
          <li v-for="g in scope.games" :key="g.id">{{ g.name }}</li>
        </ul>
      </template>
    </Child>

    <!-- 4. 作用域插槽传递多个数据: 子组件<slot>上绑定了几个属性, 这里就能解构出几个 -->
    <Child>
      <!-- 用解构一次性接收多个数据 -->
      <template #game2="{ games, year, hotGame }">
        <p>游戏数量: {{ games.length }}</p>
        <p>年份: {{ year }}</p>
        <p>最火的游戏: {{ hotGame }}</p>
        <!-- 也可以像上面那样写成#game2="scope", 再用scope.year逐个取 -->
      </template>
    </Child>
  </div>
</template>

<script setup lang="ts" name="App">
  import Child from './components/Child.vue'

</script>

<style scoped>
</style>
