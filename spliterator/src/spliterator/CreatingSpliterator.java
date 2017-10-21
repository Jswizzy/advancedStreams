package spliterator;

import model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CreatingSpliterator {

    public static void main(String[] args) {

        System.out.println(Paths.get(".").toAbsolutePath());
        Path path = Paths.get("files/people.txt");

        try (Stream<String> lines = Files.lines(path);) {

            Spliterator<String> lineSpliterator = lines.spliterator();
            Spliterator<Person> personSpliterator = new PersonSpliterator(lineSpliterator);

            Stream<Person> people = StreamSupport.stream(personSpliterator, false);
            people.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
