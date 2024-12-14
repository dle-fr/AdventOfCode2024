package org.dle.adventofcode2024;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day10Test {

    private static final Supplier<Day10> clazz = Day10::new;
    private static final List<String> TEST_MAP = List.of("""
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732
            """.split("\n"));

    @Test
    void part1_test() {
        assertEquals(36, clazz.get().part1(TEST_MAP));
        assertEquals(682, clazz.get().part1(null));
    }

    @Test
    void part2_test() {
        assertEquals(81, clazz.get().part2(TEST_MAP));
        assertEquals(1511, clazz.get().part2(null));
    }
}
