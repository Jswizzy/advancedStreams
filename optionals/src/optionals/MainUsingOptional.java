package optionals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainUsingOptional {
    public static void main(String[] args) {

//        List<Double> result = new ArrayList<>();

//        ThreadLocalRandom.current()
//                .doubles(10_000)
//                .boxed().parallel()
//                .forEach(
//                        d -> NewMath.inv(d)
//                                .ifPresent(
//                                        inv -> NewMath.sqrt(inv)
//                                                .ifPresent(
//                                                        sqrt -> result.add(sqrt)
//                                                )
//                                )
//                );
//
//        System.out.println("# of threads = " + result.size());

        Function<Double, Stream<Double>> flatMapper =
                d -> NewMath.inv(d)
                        .flatMap(NewMath::sqrt).stream();

        List<Double> correctResults =
                ThreadLocalRandom.current()
                        .doubles(10_000).parallel()
                        .map(d -> d * 20 - 10) // -10---10
                        .boxed()
                        .flatMap(flatMapper)
                        .collect(Collectors.toList());

        System.out.println("# of threads = " + correctResults.size());

    }
}
