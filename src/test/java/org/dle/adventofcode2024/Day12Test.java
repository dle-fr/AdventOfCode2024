package org.dle.adventofcode2024;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day12Test {

    private static final Supplier<Day12> clazz = Day12::new;
    private static final List<String> TEST_MAP_1 = List.of("""
            AAAA
            BBCD
            BBCC
            EEEC
            """.split("\n"));

    private static final List<String> TEST_MAP_2 = List.of("""
            OOOOO
            OXOXO
            OOOOO
            OXOXO
            OOOOO
            """.split("\n"));

    private static final List<String> TEST_MAP_3 = List.of("""
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
            """.split("\n"));

    private static final List<String> TEST_MAP_3_EXTRACT = List.of("""
            CFF
            CCF
            FFF
            """.split("\n"));

    @Test
    void part1_test() {
        assertEquals(140, clazz.get().part1(TEST_MAP_1));
        assertEquals(772, clazz.get().part1(TEST_MAP_2));
        assertEquals(1930, clazz.get().part1(TEST_MAP_3));
        assertEquals(108, clazz.get().part1(TEST_MAP_3_EXTRACT));
        assertEquals(1415378, clazz.get().part1(null));
    }

    @Test
    void part2_test() {
        assertEquals(66, clazz.get().part2(TEST_MAP_3_EXTRACT));
        assertEquals(80, clazz.get().part2(TEST_MAP_1));
        assertEquals(436, clazz.get().part2(TEST_MAP_2));
        assertEquals(1206, clazz.get().part2(TEST_MAP_3));
        assertEquals(862714, clazz.get().part2(null));
    }
}
