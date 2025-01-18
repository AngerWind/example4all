
//引入Vuex
import Vuex from 'vuex';
import Vue from "vue";
import count from './count';
import person from './person';

Vue.use(Vuex);

/**
 * 模块化主要用于, 如果你有多组需要管理的状态, 那么就需要使用模块化来区分不同的状态
 */
export default new Vuex.Store({
    // 使用模块化
    modules:{
        count, // 这里放一组 {state, action, getters, mutations }
        person // 这里放另外一组 {state, action, getters, mutations }
    }
});







