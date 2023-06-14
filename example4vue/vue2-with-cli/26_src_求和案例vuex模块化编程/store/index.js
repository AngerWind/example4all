/**
 * 该文件用于创建vuex中最核心的store
 */

//引入Vuex
import Vuex from 'vuex';
import Vue from "vue";
import count from './count';
import person from './person';

Vue.use(Vuex);


//创建并暴露store
export default new Vuex.Store({
    // 使用模块化
    modules:{
        count,
        person
    }
});







