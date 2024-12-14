package org.dle.adventofcode2024;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day08Test {

    private static final Supplier<Day08> clazz = Day08::new;
    private static final List<String> TEST_MAP = List.of("""
           ............
           ........0...
           .....0......
           .......0....
           ....0.......
           ......A.....
           ............
           ............
           ........A...
           .........A..
           ............
           ............
           """.split("\n"));

    @Test
    void part1_test() {
        assertEquals(14, clazz.get().part1(TEST_MAP));
        assertEquals(379, clazz.get().part1(null));
    }

    @Test
    void part2_test() {
        assertEquals(34, clazz.get().part2(TEST_MAP));
        assertEquals(1339, clazz.get().part2(null));
    }
}
