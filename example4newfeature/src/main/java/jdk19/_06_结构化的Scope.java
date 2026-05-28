package jdk19;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;

public class _06_结构化的Scope {

    public static final ScopedValue<String> USER = ScopedValue.newInstance();
    public static final ScopedValue<Integer> AGE = ScopedValue.newInstance();

    public static void main(String[] args) {
        ScopedValue.where(USER, "zhangsan").where(AGE, 18).run(() -> {

            System.out.println(Thread.currentThread().getName());

            // 创建一个Scope, Scope.fork()会创建一个新的虚拟线程来执行代码块
            // 但是虚拟线程中的Scope和当前代码块同属于一个Scope
            try(var scope = new StructuredTaskScope<Object>()) {

                StructuredTaskScope.Subtask<String> subtask = scope.fork(() -> {
                    System.out.println(Thread.currentThread().getName());

                    // 这里面还是和外面再同一个Scope中
                    return readUser();
                });
                StructuredTaskScope.Subtask<Integer> subtask1 = scope.fork(() -> {
                    System.out.println(Thread.currentThread().getName());

                    // 这里还是和外面再同一个Scope中
                    return readAge();
                });

                scope.join(); // 等待所有的task完成

                switch (subtask.state()) {
                    case FAILED -> {
                        Throwable exception = subtask.exception();
                    }
                    case SUCCESS -> {
                        String user = subtask.get();
                        System.out.println(user);
                    }
                    case UNAVAILABLE ->
                        System.out.println("user task 尚未完成");
                }
                switch (subtask1.state()) {
                    case FAILED -> {
                        Throwable exception = subtask1.exception();
                    }
                    case SUCCESS -> {
                        Integer age = subtask1.get();
                        System.out.println(age);
                    }
                    case UNAVAILABLE ->
                        System.out.println("age task 尚未完成");
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
    }

    public static String readUser() {
        return USER.get();
    }
    public static int readAge() {
        return AGE.get();
    }

    public static void main1(String[] args) {
        // StructuredTaskScope还有两个子类, 一个是ShutdownOnFailure, 一个是ShutdownOnSuccess
        // ShutdownOnFailure: 只要有一个task失败, 那么所有的task都会被取消
        // ShutdownOnSuccess: 只要有一个task成功, 那么所有的task都会被取消
        ScopedValue.where(USER, "zhangsan").where(AGE, 18).run(() -> {

            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                // 使用了 scope.fork 来创建两个并行的任务
                // 每个任务都在执行上下文中获取 VALUE 的值，并对其进行操作
                StructuredTaskScope.Subtask<String> user = scope.fork(USER::get);
                StructuredTaskScope.Subtask<Integer> order = scope.fork(AGE::get);

                // join() 方法等待所有范围内的任务完成
                // throwIfFailed() 方法会检查所有任务的结果，如果任何任务失败，则会抛出异常
                scope.join().throwIfFailed();

            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        ScopedValue.where(USER, "zhangsan").where(AGE, 18).run(() -> {

            try (var scope = new StructuredTaskScope.ShutdownOnSuccess<Object>()) {
                // 使用了 scope.fork 来创建两个并行的任务
                // 每个任务都在执行上下文中获取 VALUE 的值，并对其进行操作
                StructuredTaskScope.Subtask<String> user = scope.fork(USER::get);
                StructuredTaskScope.Subtask<Integer> order = scope.fork(AGE::get);

                // join() 方法等待所有范围内的任务完成
                // throwIfFailed() 方法会检查所有任务的结果，如果任何任务失败，则会抛出异常
                scope.join();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
