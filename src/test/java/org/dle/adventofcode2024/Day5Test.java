package org.dle.adventofcode2024;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day5Test {

    @Test
    void part1_test() {
        long res = new Day5().part1(null);
        System.out.println(res);
        assertEquals(0, res);
    }

    @Test
    void part2_test() {
        long res = new Day5().part2(null);
        System.out.println(res);
        assertEquals(0, res);
    }

    @Test
    void part1_test_example() {
        long res = new Day5().part1(Arrays.asList());
        System.out.println(res);
        assertEquals(0, res);
    }

    @Test
    void part2_test_example() {
        long res = new Day5().part2(Arrays.asList());
        System.out.println(res);
        assertEquals(0, res);
    }
}
