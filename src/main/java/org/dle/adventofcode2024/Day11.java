package org.dle.adventofcode2024;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class Day11 {

    private final static Map<Long, List<Long>> cache = new ConcurrentHashMap<>(); // Not sure if really useful

    long part1(List<String> input, int blinks) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);
        List<Long> stones = AoCUtils.toLongs(strings.getFirst().split(" "));

        return stones.stream()
                .map(s -> blink(List.of(s), blinks))
                .mapToLong(Collection::size)
                .sum();
    }

    // Optimization needed
    long part2(List<String> input) {
        return part1(input, 75);
    }

    private List<Long> blink(List<Long> stones, int blinks) {
        System.out.println(blinks + " => " + stones.size());
        List<Long> nextStones = stones;
        if (blinks > 0) {
            nextStones = stones.stream().parallel().map(this::blink).flatMap(Collection::stream).toList();
            return blink(nextStones, --blinks);
        }
        return nextStones;
    }

    private List<Long> blink(Long stone) { // One blink of one stone
        if (cache.containsKey(stone)) {
            return new ArrayList<>(cache.get(stone));
        }
        int length = ("" + stone).length();
        List<Long> res;
        if (stone == 0) {
            res = List.of(1L);
        } else if (length % 2 == 0) {
            long left = Long.parseLong(("" + stone).substring(0, length / 2));
            long right = Long.parseLong(("" + stone).substring(length / 2));
            res = List.of(left, right);
        } else {
            res = List.of(stone * 2024);
        }
        cache.put(stone, res);
//        System.out.println(stone + " => " + res);
        return res;
    }
}