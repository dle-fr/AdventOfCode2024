package org.dle.adventofcode2024;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day6Test {

    private static final Supplier<Day6> clazz = Day6::new;
    private static final List<String> TEST_MAP = List.of("""
           ....#.....
           .........#
           ..........
           ..#.......
           .......#..
           ..........
           .#..^.....
           ........#.
           #.........
           ......#...""".split("\n"));

    @Test
    void part1_test() {
        assertEquals(41, clazz.get().part1(TEST_MAP));
        assertEquals(4890, clazz.get().part1(null));
    }

    @Test
    void part2_test() {
        assertEquals(6, clazz.get().part2(TEST_MAP));
        assertEquals(1995, clazz.get().part2(null));
    }
}
