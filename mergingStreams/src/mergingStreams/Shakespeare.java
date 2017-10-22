package mergingStreams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Map.*;

public class Shakespeare {
    public static void main(String[] args) throws IOException {

        Set<String> shakespeareWords = Files.lines(Paths.get("files/words.shakespeare.txt"))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        Set<String> scrabbleWords = Files.lines(Paths.get("files/ospd.txt"))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        System.out.println("# words of Shakespeare: " + shakespeareWords.size());
        System.out.println("# words of Scrabble: " + scrabbleWords.size());

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

//        System.out.println("Score of the word hello: " +
//                "h".codePoints()
//                .map(scrabbleScore::get)
//                .sum()
//
//        );

        ToIntFunction<String> score =
                word -> word.chars()
                        .map(i -> scrabbleScore.getOrDefault((char) i, 0))
                        .sum();

        System.out.println("Score of hello: " + score.applyAsInt("hello"));

        Optional<String> bestWord = shakespeareWords.stream()
                .filter(scrabbleWords::contains)
                .max(Comparator.comparingInt(score));

        bestWord.ifPresent(s -> System.out.println("Best word: " + s + " " + score.applyAsInt(s)));

        IntSummaryStatistics intSummaryStatistics = shakespeareWords.stream().parallel()
                .filter(scrabbleWords::contains)
                .mapToInt(score)
                .summaryStatistics();

        System.out.println("Stats: " + intSummaryStatistics);

    }
}
