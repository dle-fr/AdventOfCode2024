package org.dle.adventofcode2024;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Day9 {

    // Cache of dots clusters min index by cluster size, to avoid iterating
    // on the whole map again each time and only start at last known min index
    private final Map<Integer, Integer> dotClustersCache = new HashMap<>();

    long part1(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);
        List<Integer> expandedMap = calculateExpandedMap(strings.getFirst());

        do {
            int dotIndex = expandedMap.indexOf(-1);
            expandedMap.set(dotIndex, expandedMap.getLast());
            do {
                expandedMap.removeLast();
            } while (expandedMap.getLast() == -1);
        } while (expandedMap.contains(-1));

        return calculateFinalMap(expandedMap);
    }

    long part2(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);
        List<Integer> expandedMap = calculateExpandedMap(strings.getFirst());

        for (int id = expandedMap.getLast(); id >= 0; id--) { // Starting from the last id
            int idIndex = expandedMap.indexOf(id);
            int nbBlocks = expandedMap.lastIndexOf(id) - idIndex + 1; // Nb blocks of the current file id

            int dotClusterIndex = -1; // Index of first dots cluster big enough
            int x = dotClustersCache.getOrDefault(nbBlocks, 0); // Get cache value if exists
            do {
                if (IntStream.range(x, x + nbBlocks).parallel() // All indices of the search
                        .mapToObj(expandedMap::get)
                        .filter(s -> s == -1) // Only keeping "dots"
                        .count() == nbBlocks) { // if current range is entirely made of dots and is big enough
                    dotClusterIndex = x;
                    dotClustersCache.put(nbBlocks, x); // Putting this index as the new min index for this block size
                }
                x++;
            } while (x < idIndex && dotClusterIndex == -1);

            switchFileId(dotClusterIndex, nbBlocks, id, idIndex, expandedMap);
        }

        return calculateFinalMap(expandedMap);
    }

    private List<Integer> calculateExpandedMap(String input) {
        return IntStream.range(0, input.length())
                .mapToObj(i -> toExpendedStrings(i, input))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<Integer> toExpendedStrings(int i, String inputMap) {
        int charInt = Integer.parseInt("" + inputMap.charAt(i));
        if (charInt == 0) { // O value is not kept
            return new ArrayList<>();
        } else if (i % 2 == 0) { // Every even index
            return IntStream.range(0, charInt).mapToObj(x -> i / 2).toList();
        } else { // Every odd index is "dots" (-1)
            return IntStream.range(0, charInt).mapToObj(x -> -1).toList(); // dots converted to -1
        }
    }

    private void switchFileId(int indexTarget, int nbBlocks, int id, int indexSource, List<Integer> map) {
        if (indexTarget > -1) {
            IntStream.range(0, nbBlocks).parallel()
                    .forEach(i -> {
                        map.set(indexTarget + i, id);
                        map.set(indexSource + i, -1);
                    });
        }
    }

    private long calculateFinalMap(List<Integer> expandedMap) {
        return IntStream.range(0, expandedMap.size())
                .mapToLong(i -> expandedMap.get(i) == -1 ? 0L : (long) i * expandedMap.get(i))
                .reduce(0L, Long::sum);
    }
}