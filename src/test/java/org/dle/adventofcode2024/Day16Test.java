package org.dle.adventofcode2024;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day16Test {

    private static final Supplier<Day16> clazz = Day16::new;
    private static final List<String> TEST_MAP = List.of("""
            ###############
            #.......#....E#
            #.#.###.#.###.#
            #.....#.#...#.#
            #.###.#####.#.#
            #.#.#.......#.#
            #.#.#####.###.#
            #...........#.#
            ###.#.#####.#.#
            #...#.....#.#.#
            #.#.#.###.#.#.#
            #.....#...#.#.#
            #.###.#.#.#.#.#
            #S..#.....#...#
            ###############
            """.split("\n"));

    private static final List<String> TEST_MAP_2 = List.of("""
            #################
            #...#...#...#..E#
            #.#.#.#.#.#.#.#.#
            #.#.#.#...#...#.#
            #.#.#.#.###.#.#.#
            #...#.#.#.....#.#
            #.#.#.#.#.#####.#
            #.#...#.#.#.....#
            #.#.#####.#.###.#
            #.#.#.......#...#
            #.#.###.#####.###
            #.#.#...#.....#.#
            #.#.#.#####.###.#
            #.#.#.........#.#
            #.#.#.#########.#
            #S#.............#
            #################
            """.split("\n"));

    @Test
    void part1_test() {
        assertEquals(7036, clazz.get().part1(TEST_MAP));
        assertEquals(11048, clazz.get().part1(TEST_MAP_2));
        assertEquals(130536, clazz.get().part1(null));
    }

    @Test
    void part2_test() {
        assertEquals(45, clazz.get().part2(TEST_MAP));
        assertEquals(64, clazz.get().part2(TEST_MAP_2));
        assertEquals(1024, clazz.get().part2(null));
    }
}
