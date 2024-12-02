package org.dle.adventofcode2024;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day1Test {

    @Test
    void part1_test() {
        int res = new Day1().part1();
        System.out.println(res);
        assertEquals(2166959, res);
    }

    @Test
    void part2_test() {
        int res = new Day1().part2();
        System.out.println(res);
        assertEquals(23741109, res);
    }
}
