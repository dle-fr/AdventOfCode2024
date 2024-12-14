package org.dle.adventofcode2024;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day05Test {

    @Test
    void part1_test() {
        long res = new Day05().part1(null);
        assertEquals(6951, res);
    }

    @Test
    void part2_test() {
        long res = new Day05().part2(null);
        assertEquals(4121, res);
    }

    private static final List<String> TEST_LIST = List.of("""
            47|53
            97|13
            97|61
            97|47
            75|29
            61|13
            75|53
            29|13
            97|29
            53|29
            61|53
            97|53
            61|29
            47|13
            75|47
            97|75
            47|61
            75|61
            47|29
            75|13
            53|13
            
            75,47,61,53,29
            97,61,53,29,13
            75,29,13
            75,97,47,61,53
            61,13,29
            97,13,75,29,47""".split("\n"));

    @Test
    void part1_test_example() {
        long res = new Day05().part1(TEST_LIST);
        assertEquals(143, res);
    }

    @Test
    void part2_test_example() {
        long res = new Day05().part2(TEST_LIST);
        assertEquals(123, res);
    }
}
