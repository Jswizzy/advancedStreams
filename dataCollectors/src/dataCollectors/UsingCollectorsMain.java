package dataCollectors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public class UsingCollectorsMain {
    public static void main(String[] args) {

        Path shakespearePath = Paths.get("./files/words.shakespeare.txt");
        Path ospdPath = Paths.get("./files/ospd.txt");

        try (Stream<String> ospd = Files.lines(ospdPath);
             Stream<String> shakespeare = Files.lines(shakespearePath)) {

            Set<String> scrabbleWords = ospd.collect(Collectors.toSet());
            Set<String> shakespeareWords = shakespeare.collect(Collectors.toSet());

            System.out.println("Scrabble: " + scrabbleWords.size());
            System.out.println("Shakespeare: " + shakespeareWords.size());

            // i would make a class for this in a real program
            final Map<Character, Integer> scrabbleScore = ofEntries(
                    entry('a', 1),
                    entry('b', 3),
                    entry('c', 3),
                    entry('d', 2),
                    entry('e', 1),
                    entry('f', 4),
                    entry('g', 2),
                    entry('h', 4),
                    entry('i', 1),
                    entry('j', 8),
                    entry('k', 5),
                    entry('l', 1),
                    entry('m', 3),
                    entry('n', 1),
                    entry('o', 1),
                    entry('p', 3),
                    entry('q', 10),
                    entry('r', 1),
                    entry('s', 1),
                    entry('t', 1),
                    entry('u', 1),
                    entry('v', 4),
                    entry('w', 4),
                    entry('x', 8),
                    entry('y', 4),
                    entry('z', 10)
            );

            // i would make a class for this in a real program
            final Map<Character, Integer> scrabbleLetterCounts = ofEntries(
                    entry('a', 9),
                    entry('b', 2),
                    entry('c', 2),
                    entry('d', 1),
                    entry('e', 12),
                    entry('f', 2),
                    entry('g', 3),
                    entry('h', 2),
                    entry('i', 9),
                    entry('j', 1),
                    entry('k', 1),
                    entry('l', 4),
                    entry('m', 2),
                    entry('n', 6),
                    entry('o', 8),
                    entry('p', 2),
                    entry('q', 1),
                    entry('r', 6),
                    entry('s', 4),
                    entry('t', 6),
                    entry('u', 4),
                    entry('v', 6),
                    entry('w', 2),
                    entry('x', 1),
                    entry('y', 2),
                    entry('z', 1)
            );

            Function<String, Integer> score =
                    word -> word.toLowerCase().chars()
                            .map(letter -> scrabbleScore.getOrDefault((char) letter, 0))
                            .sum();

            Map<Integer, List<String>> summaryStatistics = shakespeareWords.stream()
                    .filter(scrabbleWords::contains)
                    .collect(Collectors.groupingBy(
                            score
                            )
                    );

            summaryStatistics.entrySet().stream()
                    .sorted(
                            Comparator.comparing(entry -> -entry.getKey()))
                    .limit(3)
                    .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));

            System.out.println("# histogramWordsByScore = " + summaryStatistics.size());

            Function<String, Map<Integer, Long>> histoWord =
                    word -> word.chars().boxed()
                            .collect(Collectors.groupingBy(
                                    letter -> letter,
                                    Collectors.counting()
                                    )
                            );

            Function<String, Long> nBlanks =
                    word -> histoWord.apply(word)
                            .entrySet()
                            .stream()
                            .mapToLong(
                                    entry ->
                                            Long.max(
                                                    entry.getValue() - (long) scrabbleLetterCounts.getOrDefault((char) entry.getKey().intValue(), 0), 0L
                                            )
                            )
                            .sum();

            System.out.println("Number of blanks for whizzing: " + nBlanks.apply("whizzing"));

            Function<String, Integer> score2 =
                    word -> histoWord.apply(word)
                            .entrySet()
                            .stream()
                            .mapToInt(
                                    entry ->
                                            scrabbleScore.getOrDefault((char) entry.getKey().intValue(), 0) *
                                            Integer.min(
                                                    entry.getValue().intValue(),
                                                    scrabbleLetterCounts.getOrDefault((char) entry.getKey().intValue(), 0)
                                            )
                            )
                            .sum();

            System.out.println("Score for whizzing: " + score.apply("whizzing"));
            System.out.println("Score2 for whizzing: " + score2.apply("whizzing"));

            //Map<Integer, List<String>> histoWordsByScore2 =
                    shakespeareWords.stream()
                    .filter(scrabbleWords::contains)
                    .filter(word -> nBlanks.apply(word) <= 2)
                    .collect(
                            Collectors.groupingBy(
                                    score2
                            )
                    )
                    .entrySet()
                    .stream()
                    .sorted(
                            Comparator.comparing(entry -> -entry.getKey())
                    )
                    .limit(3)
                    .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
