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

    static List<Integer> integerListFromString(String string) {
        return Arrays.stream(string.split(" "))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .toList();
    }

    static List<Long> toLongs(String[] split) {
        return Arrays.stream(split).map(Long::parseLong).toList();
    }
}
