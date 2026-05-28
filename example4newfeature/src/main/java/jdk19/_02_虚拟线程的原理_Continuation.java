package jdk19;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

public class _02_虚拟线程的原理_Continuation {

    public static void main(String[] args) {
        var cont = getContinuations();
        cont.run();
        System.out.println("Do something");
        cont.run();
        System.out.println("Do something");
        cont.run();
        cont.run(); // 这里会保存, 因为cont已经执行完毕了
    }

    /**
     * 使用Continuation这些类, 必须添加 --add-exports java.base/jdk.internal.vm=ALL-UNNAMED参数
     * 因为这些API只会在java内部使用
     *
     * Continuation的作用主要就是实现JS中的yield功能,
     *
     * 在new Continuation的时候, 会在heap上面创建一个Continuation对象空间,
     * 调用Continuation.run()之后, 将在系统线程的栈空间中开始调用方法
     *
     * 调用Continuation.yield后, 意味着虚拟线程主动的放弃了cpu的执行权
     * 系统线程会将虚拟线程的call stack 复制到Continuation的堆空间中
     * 在调用run的时候, 又将call stack 复制到系统线程的栈空间中, 继续调用
     */
    private static Continuation getContinuations() {
        var scope = new ContinuationScope("Demo");
        return new Continuation(scope, () -> {
            System.out.println("A");
            Continuation.yield(scope); // 等效于js中的 yield
            System.out.println("B");
            Continuation.yield(scope);
            System.out.println("C");
        });
    }
}
