/**
 *
 * 因为使用的是vue2的版本, 所以安装的时候要使用vuex3的版本, 如果是vue3对应vuex4
 *
 * 要想实现vuex管理共享状态, 我们需要实现三个东西
 * 1. state: 需要共享的状态
 * 2. actions: 一个操作state的动作
 * 3. mutations: 精细化的操作state的动作
 *
 *     mutations和actions的区别在于:
 *         mutations是真正操作store的地方, 而action需要借助mutations来修改store
 *         所以可以在mutations写一些简单的操作, 然后在actions中将多个mutations组合起来, 完成一个业务
 *
 * vuex管理状态的过程
 * 1. 配置vuex插件到vue中
 * 2. 之后就可以在组件中通过this.$store.dispatch("action", args...) 来触发一个定义好的action
 * 3. 然后在actions中, 会接受到一个context, 你可以多次调用context.commit("mutations", args...) 来触发一系列的mutations
 * 4. 在mutations中, 真正的修改state
 * 5. 重新渲染页面内容
 *
 * vuex中还有一个非必须的属性getter, 类似vue中的computed
 */

// 引入Vuex
// npm install vuex@3
import Vuex from 'vuex';
import Vue from "vue";

// 注册Vuex插件, 之后就可以在new Vue({})的时候, 传入一个Vuex.Store对象了
// 同时在任何的组件中, 都可以使用this.$store.dispatch("action", args) 来调用一个action 了
Vue.use(Vuex);

// 定义需要管理的状态
const state = {
    sum: 0,
}
// 类似vue中的computed, 之后可以使用this.$store.getters.bigSum 来调用
const getters = {
    bigSum(state){
        return state.sum * 10;
    }
}

//定义一系列操作
const actions = {
    // 该方法会在$store.dispatch("increment", xxx)的时候被触发
    increment(context, value){
        // 调用commit来触发mutations中的函数
        context.commit('INCREMENT', value);

        // 还可以接着调用 context.dispatch()继续调用actions
        // 也可以接着调用context.commit() 来修改状态

        // 可以通过context.state来获取管理的state, 进行一些判断
        // 但是不要在actions 中通过 context.state 来直接修改状态, 虽然也是可以的,
        // 但是本次对状态的改变开发者工具将无法捕获，因为开发者工具是对mutations对话的
        // if (context.state.sum > 0) {
        //     context.state.sum += 1
        // }
    },
    // 该方法会在 $store.dispatch("decrement", args)的时候被触发
    decrement(context, value){
        context.commit('DECREMENT', value);
    },
    incrementIfOdd(context, value){
        if(context.state.sum % 2) {
            console.log('@')
            // 触发mutations中的INCREMENT
            context.commit('INCREMENT',value);
        }
    },
    incrementWait(context, value){
        setTimeout(() => {
            context.commit('INCREMENT', value);
        },500)
    },
}

//创建mutations(本质也是对象) 用于修改数据(state)
const mutations = {
    // 收到state和要操作数value
    INCREMENT(state, value) {
        state.sum += value;
    },
    DECREMENT(state, value) {
        state.sum -= value;
    },
}



//创建一个Vuex.Store并暴露
export default new Vuex.Store({
    actions,
    mutations,
    state,
    getters
});







