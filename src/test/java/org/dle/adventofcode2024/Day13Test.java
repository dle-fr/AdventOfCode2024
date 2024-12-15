package org.dle.adventofcode2024;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day13Test {

    private static final Supplier<Day13> clazz = Day13::new;
    private static final List<String> TEST_LIST_1 = List.of("""
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400
            
            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176
            
            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450
            
            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279
            """.split("\n"));

    private static final List<String> TEST_LIST_1_EXTRACT = List.of("""
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400
            """.split("\n"));
    @Test
    void part1_test() {
        assertEquals(280, clazz.get().part1(TEST_LIST_1_EXTRACT));
        assertEquals(480, clazz.get().part1(TEST_LIST_1));
        assertEquals(35729, clazz.get().part1(null));
    }

    @Test
    void part2_test() {
        // We don't even know which result to expect from example :(
        assertEquals(-1, clazz.get().part2(TEST_LIST_1));
//        assertEquals(0, clazz.get().part2(null));
    }
}
