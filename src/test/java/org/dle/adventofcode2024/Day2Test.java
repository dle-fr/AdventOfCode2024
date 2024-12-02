package org.dle.adventofcode2024;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class Day2Test {

    @Test
    void part1_test() {
        int res = new Day2().part1();
        System.out.println(res);
        assertEquals(287, res);
    }

    @Test
    void part2_test() {
        int res = new Day2().part2();
        System.out.println(res);
        assertEquals(354, res);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "49 49 52 49 49,false"
    })
    void part2_test_weird_case(String value, String safe) {
        assertEquals("true".equals(safe), new Day2().isSafeWithDampener(AoCUtils.integerListFromString(value)));
    }
}
