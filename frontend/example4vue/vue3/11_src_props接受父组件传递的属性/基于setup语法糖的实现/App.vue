<template>
  <!-- 给子组件传递属性 -->
  <Person :personList="personList" :a="a"/>

</template>

<script lang="ts" setup name="App">
  import Person from './components/Person.vue'
  import {ref, reactive} from 'vue'

  let a = ref(0)

  let personList = reactive([
    {id:'asudfysafd01',name:'张三',age:18},
    {id:'asudfysafd02',name:'李四',age:20},
    {id:'asudfysaf)d03',name:'王五',age:22}
  ])

</script>
