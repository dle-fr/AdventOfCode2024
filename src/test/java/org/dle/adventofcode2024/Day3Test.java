package org.dle.adventofcode2024;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Day3Test {

    @Test
    void part1_test() {
        long res = new Day3().part1();
        System.out.println(res);
        assertEquals(167090022, res);
    }

    @Test
    void part2_test() {
        long res = new Day3().part2();
        System.out.println(res);
        assertEquals(89823704, res);
    }

    @Test
    void part1_test_example() {
        long res = new Day3().calcOneLine("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))", false);
        System.out.println(res);
        assertEquals(161, res);
    }

    @Test
    void part2_test_example() {
        long res = new Day3().calcOneLine("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))", true);
        System.out.println(res);
        assertEquals(48, res);
    }
}
