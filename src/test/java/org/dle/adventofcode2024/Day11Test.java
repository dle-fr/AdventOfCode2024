package org.dle.adventofcode2024;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day11Test {

    private static final Supplier<Day11> clazz = Day11::new;
    private static final List<String> TEST_VALUE = List.of("125 17");

    @Test
    void part1_test() {
        assertEquals(22, clazz.get().part1(TEST_VALUE, 6));
        assertEquals(55312, clazz.get().part1(TEST_VALUE, 25));
        assertEquals(184927, clazz.get().part1(null, 25));
    }

    @Test
    void part2_test() {
//        assertEquals(-1, clazz.get().part2(null));
    }
}
