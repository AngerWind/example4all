package jdk19;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

import java.util.function.Consumer;


public class _03_Continuation实现生成器 {

    public static void main(String[] args) {
        var generator = new Generator<String>(source -> {
            source.yield ("A");
            source.yield ("B");
            source.yield ("C");
        });

        while (generator.hasNext()){
            System.out.println(generator.next());
            System.out.println("Do something else");
        }
    }

    public static class Generator<T> {
        private final ContinuationScope scope;
        private final Continuation continuation;
        private final Source source;

        public class Source{
            private T value;

            public void yield(T value){
                this.value = value;
                Continuation.yield(scope);
            }

            private T getValue(){
                return value;
            }
        }
        public T next() {
            var t = source.getValue();
            continuation.run();
            return t;
        }
        public boolean hasNext() {
            return !continuation.isDone();
        }

        public Generator(Consumer<Source> consumer){
            scope = new ContinuationScope("Generator");
            source = new Source();
            continuation = new Continuation(scope, () -> {
                consumer.accept(source);
            });
            continuation.run();
        }
    }

}
