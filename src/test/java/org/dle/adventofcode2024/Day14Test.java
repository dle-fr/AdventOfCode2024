package org.dle.adventofcode2024;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day14Test {

    private static final Supplier<Day14> clazz = Day14::new;
    private static final List<String> TEST_LIST = List.of("""
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3
            """.split("\n"));

    @Test
    void part1_test() {
        assertEquals(12, clazz.get().part1(TEST_LIST, 11, 7));
        assertEquals(211692000, clazz.get().part1(null, 101, 103));
    }

    @Test
    void part2_test() {
        assertEquals(6587, clazz.get().part2(null, 101, 103));
    }
}
