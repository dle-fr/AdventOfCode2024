package org.dle.adventofcode2024;

import java.util.*;
import java.util.function.BiPredicate;

class Day5 {

    long part1(List<String> input) {
        return filterFixAndReduce((update, rules) -> getBadIndex(update, rules) == -1, input); // searching for good updates only
    }

    long part2(List<String> input) {
        return filterFixAndReduce((update, rules) -> getBadIndex(update, rules) >= 0, input); // searching for bad updates only
    }

    // Split input, filter good or bad updates, fix the order if necessary, get middle update value, sum
    private long filterFixAndReduce(BiPredicate<List<Long>, List<List<Long>>> filterPredicate, List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);

        List<List<Long>> rules = strings.stream().filter(s -> s.contains("|")).map(s -> toLongs(s.split("\\|"))).toList();
        List<List<Long>> updates = strings.stream().filter(s -> s.contains(",")).map(s-> toLongs(s.split(","))).toList();

        return updates.parallelStream()
                .filter(u -> filterPredicate.test(u, rules))
                .map(u -> fixOrder(u, rules))
                .map(u -> u.get(Math.floorDiv(u.size(), 2)))
                .reduce(0L, Long::sum);
    }

    private List<Long> toLongs(String[] split) {
        return Arrays.stream(split).map(Long::parseLong).toList();
    }

    // Should return a correct ordered update list, according to rules
    private List<Long> fixOrder(List<Long> update, List<List<Long>> rules) {
        List<Long> res = new ArrayList<>(update);
        while(true) { // yeah, but in theory no possible ∞ loop
            int i = getBadIndex(res, rules);
            if (i >= 0) { // we have a bad index, let's swap the element with previous one (element is too far)
                Long l = res.get(i);
                res.set(i, res.get(i - 1));
                res.set(i - 1, l);
            } else {
                return res; // 0 -> order is good!
            }
        }
    }

    // Returns -1 if correct order, otherwise, returns the index of the first wrong element (which is too far in list)
    private int getBadIndex(List<Long> update, List<List<Long>> rules) {
        // Searching for next numbers that are in the smallest rule part
        for (int i = 0; i < update.size() - 1; i++) {
            List<Long> subList = update.subList(i + 1, update.size());
            int finalI = i; // necessary for stream filter (kinda "final")
            // Loop on 2nd rules values equal to value at current update index, searching for 1st rules values
            // present after current index, which would indicate a bad order
            Optional<Integer> index = rules.parallelStream()
                    .filter(r -> Objects.equals(r.get(1), update.get(finalI)))
                    .map(r -> r.get(0))
                    .map(subList::indexOf)
                    .filter(ind -> ind > -1)
                    .findAny();
            if (index.isPresent()) {
                return index.get() + i + 1; // index + i (offset from initial list) + 1 (sublist started at index + 1)
            }
        }
        return -1;
    }
} // Nice