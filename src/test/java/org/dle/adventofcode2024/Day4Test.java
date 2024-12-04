package org.dle.adventofcode2024;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day4Test {

    @Test
    void part1_test() {
        long res = new Day4().part1(null);
        System.out.println(res);
        assertEquals(2458, res);
    }

    @Test
    void part2_test() {
        long res = new Day4().part2(null);
        System.out.println(res);
        assertEquals(1945, res);
    }

    @Test
    void part1_test_example() {
        long res = new Day4().part1(Arrays.asList(
                "MMMSXXMASM",
                "MSAMXMSMSA",
                "AMXSXMAAMM",
                "MSAMASMSMX",
                "XMASAMXAMM",
                "XXAMMXXAMA",
                "SMSMSASXSS",
                "SAXAMASAAA",
                "MAMMMXMMMM",
                "MXMXAXMASX"
        ));
        System.out.println(res);
        assertEquals(18, res);
    }

    @Test
    void part2_test_example() {
        long res = new Day4().part2(Arrays.asList(
                "MMMSXXMASM",
                "MSAMXMSMSA",
                "AMXSXMAAMM",
                "MSAMASMSMX",
                "XMASAMXAMM",
                "XXAMMXXAMA",
                "SMSMSASXSS",
                "SAXAMASAAA",
                "MAMMMXMMMM",
                "MXMXAXMASX"
        ));
        System.out.println(res);
        assertEquals(9, res);
    }
}
