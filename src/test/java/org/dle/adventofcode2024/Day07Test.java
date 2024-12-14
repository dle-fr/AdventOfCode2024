package org.dle.adventofcode2024;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day07Test {

    private static final Supplier<Day07> clazz = Day07::new;
    private static final List<String> TEST_MAP = List.of("""
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
            """.split("\n"));

    @Test
    void part1_test() {
        assertEquals(3749, clazz.get().part1(TEST_MAP));
        assertEquals(5702958180383L, clazz.get().part1(null));
    }

    @Test
    void part2_test() {
        assertEquals(11387, clazz.get().part2(TEST_MAP));
        assertEquals(92612386119138L, clazz.get().part2(null));
    }
}
