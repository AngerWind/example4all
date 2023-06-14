//vm和vc都可以用
export default {
    // 只需要通过Vue.use就能获得很多过滤器, 全局指令, 全局混入
    install(Vue){

        //全局过滤器
        Vue.filter('mySlice', function (val){
            return val.slice(0,4);
        });

        //全局指令
        Vue.directive('fbind', {
            bind(el, binding){
                // console.log('bind')
                el.value = binding.value;
            },
            //指令被插入页面时
            inserted(el, binding){
                // console.log('inserted')
                el.focus();
            },
            //指令所在模版被重新解析时
            update(el, binding){
                // console.log('update');
                el.value = binding.value;
            }
        });


        //全局混入
        Vue.mixin({
            data(){
                return {
                    x:1,
                    y:2
                }
            }
        });

        //给vue原型上添加一个方法 vc/vm都可以使用
        Vue.prototype.hello = function (){
            alert('hello')
        }
    }
}
