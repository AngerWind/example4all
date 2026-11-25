import { createApp } from 'vue'
import App from './App.vue'

const app = createApp(App)

// ==================== 全局自定义指令 ====================
// 用app.directive注册, 注册后任何组件中都能用
//
// 指令一共有7个生命周期钩子(全局指令和局部指令的钩子完全一样, 只是注册方式不同):
//
//   执行时机(以v-if控制元素为例):
//     条件变true:  created → beforeMount → [插入DOM] → mounted
//     数据变化:    beforeUpdate → [DOM更新] → updated
//     条件变false: beforeUnmount → [移除DOM] → unmounted
//
// 每个钩子的参数都一样:
//   el:        指令绑定的真实DOM元素, 直接操作它
//   binding:   绑定对象, 常用属性:
//                binding.value      指令的值, 如v-demo="n"中的n
//                binding.oldValue   更新前的旧值(只在beforeUpdate/updated中有意义)
//                binding.arg        指令的参数, 如v-demo:foo中的foo
//                binding.modifiers  指令的修饰符, 如v-demo.stop → {stop:true}
//   vnode:     绑定元素的虚拟节点
//   prevVnode: 更新前的虚拟节点(只在beforeUpdate/updated中有值)
app.directive('demo', {
  // 1. created: 在绑定元素的属性或事件监听器应用之前调用
  //    此时元素还没插入DOM, 一般用得少
  created(el, binding, vnode, prevVnode) {
    console.log('demo-created', el, binding)
  },

  // 2. beforeMount: 元素第一次绑定到指令时调用, 元素即将插入DOM
  //    此时DOM里还没有这个元素, 不适合操作DOM位置/尺寸
  beforeMount(el, binding, vnode, prevVnode) {
    console.log('demo-beforeMount', el, binding)
  },

  // 3. mounted: 元素插入DOM之后调用, 最常用
  //    适合聚焦、读取尺寸、初始化第三方DOM库
  mounted(el, binding, vnode, prevVnode) {
    console.log('demo-mounted', el, binding)
  },

  // 4. beforeUpdate: 所在组件的VNode更新之前调用
  //    此时binding.value已经是新值, 但DOM还没更新
  beforeUpdate(el, binding, vnode, prevVnode) {
    console.log('demo-beforeUpdate', el, binding)
  },

  // 5. updated: 所在组件的VNode及子组件全部更新之后调用, 常用
  //    指令值变化后要同步DOM状态时用它(比如下面的v-big更新字体大小)
  updated(el, binding, vnode, prevVnode) {
    console.log('demo-updated', el, binding)
  },

  // 6. beforeUnmount: 元素即将从DOM中卸载之前调用
  beforeUnmount(el, binding, vnode, prevVnode) {
    console.log('demo-beforeUnmount', el, binding)
  },

  // 7. unmounted: 元素卸载完成、指令解绑时调用
  //    适合做清理: 移除事件监听、销毁定时器/第三方实例
  unmounted(el, binding, vnode, prevVnode) {
    console.log('demo-unmounted', el, binding)
  },
})

// 全局指令v-focus: 元素插入DOM后自动聚焦
app.directive('focus', {
  mounted(el) {
    el.focus()
  }
})

// 全局指令v-big: 把元素的字体放大10倍
// 简写形式: 如果mounted和updated的逻辑一样, 可以直接写成一个函数
// 这个函数会同时作为mounted和updated执行
// 完整写法是:
// app.directive('big', {
//   mounted(el, binding) { el.style.fontSize = binding.value * 10 + 'px' },
//   updated(el, binding) { el.style.fontSize = binding.value * 10 + 'px' }
// })
app.directive('big', (el, binding) => {
  // binding.value就是指令表达式算出来的值: v-big="n"中的n
  el.style.fontSize = binding.value * 10 + 'px'
})

app.mount('#app')
