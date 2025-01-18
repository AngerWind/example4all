/**
 * 该文件用于创建vuex中最核心的store
 */

//引入Vuex
import Vuex from 'vuex';
import Vue from "vue";


Vue.use(Vuex);


const actions = {
    incrementIfOdd(context, value){
        if(context.state.sum % 2) {
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
    //收到state和要操作数value
    INCREMENT(state, value) {
        state.sum += value;
    },
    DECREMENT(state, value) {
        state.sum -= value;
    },
    ADD_PERSON(state, value){
        state.personList.unshift(value);
    }
}

const state = {
    sum: 0,
    personList: [
        {id: "001", name: "zhangsan"}
    ]
}


//创建并暴露store
export default new Vuex.Store({
    actions,
    mutations,
    state,
});







