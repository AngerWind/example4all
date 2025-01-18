<template>

</template>

<script>
import { ref, onBeforeMount, onMounted, onBeforeUpdate, onUpdated, onBeforeUnmount, onUnmounted } from 'vue';
export default {
  name: 'App',
  setup(){
    let sum = ref(0);

    // 在vue2中, 生命周期有: beforeCreate, created, beforeMounted, mounted, beforeUpdate, updated, beforeDestroy, destroyed
    // 在vue3中, 生命周期有: setup, beforeCreate, created, beforeMounted, mounted, beforeUpdate, updated, beforeUnmount, unmounted
    // vue3中的生命周期钩子, 可以使用组合式api配置, 也可以使用配置式api配置

    // 通过组合式api的形式去使用生命周期钩子
    // 在组合式api中, 没有办法配置beforeCreate()和created(), setup()就相当于beforeCreate()和created()

    console.log('-----setup----') // setup会最先调用
    onBeforeMount(() => {  console.log('----beforeMount----'); });
    onMounted(() => {  console.log('-----mounted-----'); });
    onBeforeUpdate(() => {  console.log('-----beforeUpdate-----') });
    onUpdated(() => { console.log('-----updated-----'); });
    onBeforeUnmount(() => { console.log('-----beforeUnmount----'); });
    onUnmounted(() => { console.log('-----unmounted----'); })


    //返回一个对象
    return {
    }
  },
  //使用配置项的形式使用生命周期钩子
  // beforeCreate() {
  //   console.log('----beforeCreate!!----');
  // },
  // created() {
  //   console.log('----created!!----');
  // },
  // beforeMount() {
  //   console.log('----beforeMount----');
  // },
  // mounted() {
  //   console.log('-----mounted-----');
  // },
  // beforeUpdate() {
  //   console.log('-----beforeUpdate-----')
  // },
  // updated() {
  //   console.log('-----updated-----');
  // },
  // beforeUnmount() {
  //   console.log('-----beforeUnmount----');
  // },
  // unmounted() {
  //   console.log('-----unmounted----');
  // }
}
</script>

<style>

</style>
