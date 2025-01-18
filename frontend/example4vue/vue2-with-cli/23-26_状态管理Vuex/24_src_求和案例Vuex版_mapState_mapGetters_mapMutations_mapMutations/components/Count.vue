<template>
  <div>
    <h1>当前求和为: {{ $store.store.sum }}</h1>

    <h3>当前求和放大3倍为:{{ $store.getters.bigSum }}</h3>
    <h3>我在{{ school }}, 学习{{ subject }}</h3>
    <select v-model.number="n">
      <!--让所有的value全部绑定为数字-->
      <option value="1">1</option>
      <option value="2">2</option>
      <option value="3">3</option>
    </select>
    <button @click="increment(n)">+</button>
    <button @click="decrement(n)">-</button>
    <button @click="incrementIfOdd(n)">当前求和为奇数再加</button>
    <button @click="incrementWait(n)">等一等再加</button>
  </div>
</template>

<script>
import { mapState, mapGetters, mapMutations, mapActions} from 'vuex';
export default {
  //计数组件
  name: "Count",
  data(){
    return {
      n: 1,// 代表用户在select框开始的时候选择的数字
    }
  },
  computed:{
    /**
     * 通过this.$store.store.xxx来获取state
     * 通过this.$store.dispatch("action", args...) 来调用action, 这样太过麻烦
     *
     * vuex提供了四个函数 mapState, mapGetter, mapMutations, mapActions
     * 可以使用这四个函数, 来展开state, getters, mutations, actions, 然后放到计算属性中, 这样我们就可以通过计算属性来更方便的调用action, mutations了
     */

    /**
     * mapState
     */
    // 自己生成计算属性
    // sum: function () {return this.$store.store.sum},

    // 通过mapState生成计算属性, 然后结构赋值, 类似sum: function(){return this.$store.store.sum}
    // key是生成的计算属性的名字, value是要绑定的store中的属性
    // ... mapState({
    //   sum:'sum',
    //   school: 'school',
    //   subject: 'subject'
    // }),

    // 如果生成key与store中属性相同, 直接传递数组, 之后就可以直接使用 this.sum 来获取$store.store中的sum 了
    ... mapState(['sum', 'school', 'subject']),

    /**
     * mapGetter
     */
    //借助mapGetters从getters中生成计算属性,对象写法
    // ...mapGetters({ bigSum: 'bigSum' }),
    //借助mapGetters从getters中生成计算属性,数组写法
    ...mapGetters(['bigSum']),

  },
  methods:{

    //借助mapMutations生成对应方法，方法会调用commit去联系mutations，对象写法
    // value是mutations的方法名
    ...mapMutations({
      increment: 'INCREMENT',
      decrement: 'DECREMENT',
    }),

    //借助数组写法生成方法,但注意你生成的方法名和mutations里对应的方法名将会一样的
    // ...mapMutations(['increment', 'decrement']),

    /**
     * mapActions
     */
    // incrementOdd(){
    //   this.$store.dispatch('incrementIfOdd', this.n);
    // },
    // incrementWait(){
    //   this.$store.dispatch('incrementWait', this.n);
    // }

    //借助mapMutations生成对应方法，方法会调用dispatch去联系actions，对象写法
    // ...mapActions({
    //   incrementIfOdd: 'incrementIfOdd',
    //   incrementWait: 'incrementWait',
    // }),

    ...mapActions(['incrementWait', 'incrementIfOdd']), //数组写法,同上
  },
}
</script>

<style scoped>
   button{
     margin-left: 5px;
   }
</style>
