package paralleStreams;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ParallelStreams {

    public static void main(String[] args) {

        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "2");

        List<String> strings = new ArrayList<>();

        Stream.iterate("+", s -> s + "+")
                .parallel()
                .limit(1000)
//                .peek(s -> System.out.println(s + " processed in the thread " + Thread.currentThread().getName()))
                .forEach(strings::add);

        System.out.println("# " + strings.size());
    }
}
