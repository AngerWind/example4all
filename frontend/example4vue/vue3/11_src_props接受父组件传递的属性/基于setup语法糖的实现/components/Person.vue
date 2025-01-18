<template>
  <div class="person">
    <ul>
      <li v-for="p in list" :key="p.id">
        {{ p.name }} -- {{ p.age }}
      </li>
    </ul>
    <span>{{ a }}</span>
  </div>
</template>

<script lang="ts" setup name="Person">

import {withDefaults} from 'vue'

// 接受父组件传递过来的list和a属性, 然后就可以模板中通过list和a来使用了
// defineProps(['list', "a"])

// 接受父组件传递过来的list, a属性, 然后就可以模板中使用了
// 同时将list和a保存为一个对象, 赋值给x, 然后就可以在setup中使用了
// let props = defineProps(['list', "a"])
// console.log(props.list, props.a)

//  接受属性, 如果父组件没有指定属性, 那么就使用默认值
let x = withDefaults(defineProps(), // defineProps不要再传参数了
    {
      // 这里指定要接受参数和默认值, 必须是一个函数, 然后返回默认值
      list: () => [{id: 'ausydgyu01', name: '康师傅·王麻子·特仑苏', age: 19}],
      a: () => 10
    }
)


</script>

<style scoped>
.person {
  background-color: skyblue;
  box-shadow: 0 0 10px;
  border-radius: 10px;
  padding: 20px;
}

button {
  margin: 0 5px;
}

li {
  font-size: 20px;
}
</style>