

//引入Vuex
import Vuex from 'vuex';
import Vue from "vue";

// 注册VueX插件
Vue.use(Vuex);

//定义一系列操作
const actions = {

    increment(context, value){

        context.commit('INCREMENT', value);
    },
    decrement(context, value){
        context.commit('DECREMENT', value);
    },
    incrementIfOdd(context, value){
        if(context.state.sum % 2) {
            console.log('@')
            context.commit('INCREMENT',value);
        }
    },
    incrementWait(context, value){
        setTimeout(() => {
            context.commit('INCREMENT', value);
        },500)
    },
}


const mutations = {
    // 收到state和要操作数value
    INCREMENT(state, value) {
        state.sum += value;
    },
    DECREMENT(state, value) {
        state.sum -= value;
    },
}

//准备getters用于加工state，将其共享于各个组件当中
const getters = {
    bigSum(state){
        return state.sum * 10;
    }
}

//准备state(数据) 存储数据
//类似于各个组件里的computed(计算属性),只不过它是共享的
const state = {
    sum: 0,
    school: 'Wust',
    subject: 'Computer Science',
}


//创建并暴露store
export default new Vuex.Store({
    actions,
    mutations,
    state,
    getters
});







