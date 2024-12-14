package org.dle.adventofcode2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

class AoCUtils {

    static List<String> readFile(Object o) {
        try {
            return Files.readAllLines(Paths.get(o.getClass().getResource("/" + o.getClass().getSimpleName() + "Input.txt").toURI()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Chars separated by a space
    static List<Integer> stringWithSpacesToInts(String string) {
        return Arrays.stream(string.split(" "))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .toList();
    }

    // String of ints (no separation, single digit)
    static List<Integer> stringToInts(String string) {
        return Arrays.stream(string.split("")).map(Integer::parseInt).toList();
    }

    static List<Long> toLongs(String[] array) {
        return Arrays.stream(array).map(Long::parseLong).toList();
    }
}
