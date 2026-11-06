
import {createRouter,createWebHistory,createWebHashHistory} from 'vue-router'
import Detail from '@/pages/Detail.vue'


const router = createRouter({
  history:createWebHistory(),
  routes:[
    {
      name:'xiang',
      path:'detail',
      component:Detail,

      /**
       * props其实实现的功能就是params和query的功能, 但是为什么要加这个功能呢?
       * 主要是用于解耦, 如果使用params和query的话, 那么子路由组件只能从路由组件中获取对应的参数信息, 如果他被作为普通组件, 那么是没有办法获取到参数信息的
       *
       * 而使用props的话, 那么子路由组件只需要定义需要接受的参数, 而不用管这个参数是从哪里来的, 可以是params和query参数, 也可以是从其他的地方来的
       * 这样就可以解耦了, 而且测试也更方便了, 主要给他对应的参数就ok了
       *
       * 所以数据的传递就从
       *     url -> params/query -> 组件router
       * 变成了
       *     url -> params/query -> 路由props  -> 组件props  (即使将query和params传递到了props中, 也还可以在子组件中从router中接受参数, 两者互不影响 )
       */

      // 第一种写法：将路由收到的所有 params 参数作为props传给路由组件
      // 但是缺点是 query 参数不会作为 props 进行传递
      // props:true,


      // 第二种写法：手动从query/params参数中提取数据, 然后将特定的数据指定为props传递到子组件中
      props(route){
        // props会接受到一个route参数, 这个参数就是路由到的组件的route
        // 可以通过route.query.xxx 和 route.params.xxx 来获取query参数和params参数
        return {
          // 可以在这里解析route, 然后返回一个对象
          // 这个对象的属性会作为 props 参数传递给路由组件
          id: route.params.id,
          title: route.params.title,
          content: route.params.content // 解析params参数, 并通过props传递给路由组件
        }
      }

      // 第三种写法：对象写法，直接返回死数据, 作为 props 传递给路由组件 (不使用!!!!)
      // props:{
      //   id:100,
      //   title:200,
      //   content:300
      // }
    }
  ]
})

// 暴露出去router
export default router
