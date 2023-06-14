/**
 *
 * 因为使用的是vue2的版本, 所以安装的时候要使用vuex3的版本, 如果是vue3对应vuex4
 *
 *
 * 该文件用于创建vuex中最核心的store
 * 在vuex中必须定义actions, mutations, store三个属性
 *
 * actions: 一系列操作store的动作
 * mutations: 也是一系列操作store的动作
 *      与actions的区别是, mutations中可以真正操作store, 而actions是通过mutations间接来操作store的
 *      可以在mutations中写一些比较细化的操作, 然后在actions中将mutations操作组合起来形成一个业务
 * store: 共享的数据
 *
 * 用户通过vuex来调用dispatch方法来触发actions中的动作
 * 然后在actions中调用commit来触发mutations中的动作
 * 在mutations中真正修改store, 然后重新渲染模板
 *
 * vuex中还有一个非必须的属性getter, 类似vue中的computed
 */

//引入Vuex
import Vuex from 'vuex';
import Vue from "vue";

// 注册VueX插件
Vue.use(Vuex);

//定义一系列操作
const actions = {
    // 该方法会在$store.dispatch("increment", xxx)的时候被触发
    increment(context, value){
        // 调用commit来触发mutations中的函数
        context.commit('INCREMENT', value);

        // context.dispatch()继续调用actions
        // context.state  获取state做判断等等
    },
    decrement(context, value){
        context.commit('DECREMENT', value);
    },
    incrementIfOdd(context, value){
        if(context.state.sum % 2) {
            console.log('@')
            context.commit('INCREMENT',value);
            // context.state.sum += 1;//这样可以实现但是记住本次对状态的改变开发者工具将无法捕获，因为开发者工具是对mutations对话的
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

//准备state数据
const state = {
    sum: 0,
}
// 类似vue中的computed
const getters = {
    bigSum(state){
        return state.sum * 10;
    }
}

//创建并暴露store
export default new Vuex.Store({
    actions,
    mutations,
    state,
    getters
});







