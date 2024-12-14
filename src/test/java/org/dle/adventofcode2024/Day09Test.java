package org.dle.adventofcode2024;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day09Test {

    private static final Supplier<Day09> clazz = Day09::new;
    private static final List<String> TEST_MAP = List.of("2333133121414131402");

    @Test
    void part1_test() {
        assertEquals(1928, clazz.get().part1(TEST_MAP));
        assertEquals(6299243228569L, clazz.get().part1(null));
    }

    @Test
    void part2_test() {
        assertEquals(2858, clazz.get().part2(TEST_MAP));
        assertEquals(6326952672104L, clazz.get().part2(null));
    }
}
