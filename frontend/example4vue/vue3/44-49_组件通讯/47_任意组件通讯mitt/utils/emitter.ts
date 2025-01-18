
// npm i mitt
import mitt from "mitt"

/**
 * 创建一个全局的 发布订阅器
 *   通过emitter.on("xxx", (args)=> { }) 来监听一个事件
 *   通过emitter.emit("xxx", args) 来发布一个事件
 *   通过emitter.off("xxx") 取消一个事件, 之后的emit都会无效
 *   通过emitter.all.clear()来取消所有事件, 之后接受不到任何事件了, 也不会触发任何事件了
 */
const emitter = mitt()

// 导出供其他组件使用
export default emitter