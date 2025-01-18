import {ref} from "vue"

// 必须export一个函数, 然后函数里面返回数据和控制数据的方法
// 这样做是为了防止多次export的时候, 不同组件的数据和方法能够相互隔离
export default function () {
    let sum = ref(0);

    function add(){
        sum.value ++
    }

    return {
        sum,add
    }
};