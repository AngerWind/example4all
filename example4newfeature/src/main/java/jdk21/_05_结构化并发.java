package jdk21;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public class _05_结构化并发 {

    public static void main(String[] args) {

        // 类似与CompletableFuture, 用于控制多个任务全部成功就成功, 只要一个成功就成功就成功

        try (
                var scope = new StructuredTaskScope.ShutdownOnFailure();
        ) {
            StructuredTaskScope.Subtask<String> fork = scope.fork(() -> "新鲜的大腰子考好了"); // 任务1
            StructuredTaskScope.Subtask<String> fork1 = scope.fork(() -> "奶茶做好了"); // 任务2

            scope.join().throwIfFailed(); // 等待两个任务完成, 如果其中一个任务失败, 那么其他的任务也失败

            System.out.println(fork.get());
            System.out.println(fork1.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
