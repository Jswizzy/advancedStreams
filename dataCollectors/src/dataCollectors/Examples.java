package dataCollectors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Examples {
    public static void main(String[] args) {

        List<String> peopleNames = List.of("Bob", "Sam", "William");

//        List<String> result = new ArrayList<>();

//        peopleNames.stream()
//                .filter(s -> !s.isEmpty())
//                .forEach(result::add);

        //cannot access concurrently with external array

        List<String> result = peopleNames.stream().parallel()
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        result.forEach(System.out::println);


    }
}
