package org.dle.adventofcode2024;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Day1 {

    int part1() {
        Pair lists = loadLists();

        Collections.sort(lists.list1);
        Collections.sort(lists.list2);

        int diffSum = 0;
        for (int i = 0; i < lists.list1.size(); i++) {
            diffSum += Math.abs(lists.list1.get(i) - lists.list2.get(i));
        }
        return diffSum;
    }

    int part2() {
        Pair lists = loadLists();

        int similarity = 0;
        for (Integer i : lists.list1) {
            similarity += (int) (lists.list2.stream().filter(i::equals).count() * i);
        }
        return similarity;
    }

    private Pair loadLists() {
        Pair pair = new Pair(new ArrayList<>(), new ArrayList<>());
        List<String> strings = AoCUtils.readFile(this);

        for (String s : strings) {
            List<Integer> split = AoCUtils.integerListFromString(s);
            pair.list1.add(split.get(0));
            pair.list2.add(split.get(1));
        }
        return pair;
    }

    private record Pair(List<Integer> list1, List<Integer> list2){}
}
