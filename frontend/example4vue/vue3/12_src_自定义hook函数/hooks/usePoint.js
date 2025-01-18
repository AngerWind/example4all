
import { reactive, onMounted, onBeforeUnmount } from "vue";

/**
 * 这里有一个功能, 就是用户点击鼠标的时候, 在组件中展示鼠标的位置
 * 由于多个组件都可能有这个功能, 那么我们就可以把这个功能提取出来, 作为一个hook
 */
// 注意这里要export出去的是一个函数, 函数调用后一个对象, 对象中有数据和操作数据的方法
// 如果多个组件调用多次函数, 就会返回多个对象, 这样每个组件的数据是相互独立的, 互不干扰
export default function usePoint(){
    // 定义鼠标的位置的属性
    let point = reactive({
        x: 0,
        y: 0
    });

    const savePoint = event => {
        console.log(event.pageX, event.pageY);
        point.x = event.pageX;
        point.y = event.pageY;
    };

    // 当组件挂载的时候, 添加一个监听器
    onMounted(() => {
        window.addEventListener('click', savePoint)
    });

    // 当组件卸载的时候, 取消监听器
    onBeforeUnmount(() => {
        window.removeEventListener('click', savePoint);
    });

    // 返回鼠标位置的数据
    return {
        point,
        // 还可以放入一些有用的方法, 但是这个功能不需要
    }
}
