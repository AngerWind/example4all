<script setup lang="ts">

import {inject} from "vue";

// 在后代组件中接收祖先传递过来的属性, 如果没有就使用默认值
// let car = inject("car", {car: "宝马", price: 100})
// let money = inject("money", 199)

// 也可以不要默认值, 没有就是undefined
let car = inject("car")
let money = inject("money")
</script>

<template>
{{car.brand}} -- {{car.price}} -- {{money}}
</template>

<style scoped>

</style>