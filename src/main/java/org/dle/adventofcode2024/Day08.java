package org.dle.adventofcode2024;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Day08 {

    long part1(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);

        return IntStream.range(0, strings.size())
                .mapToObj(y -> calcLocations(strings.get(y), y, strings, false))
                .flatMap(Collection::stream)
                .distinct().count();
    }

    long part2(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);

        Set<Location> locations = IntStream.range(0, strings.size())
                .mapToObj(y -> calcLocations(strings.get(y), y, strings, true))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        List<String> outMap = new ArrayList<>(strings);
        for (Location l : locations) {
            char[] chars = outMap.get(l.y).toCharArray();
            chars[l.x] = '#';
            outMap.set(l.y, String.valueOf(chars));
        }
        return outMap.stream().map(s -> s.replace(".", "")).map(String::length).reduce(0, Integer::sum);
    }

    private Set<Location> calcLocations(String line, int y, List<String> input, boolean withHarmonics) {
        Set<Location> locations = new HashSet<>();

        for(int x = 0; x < line.length(); x++) {
            if (line.charAt(x) != '.') { // We're on an antenna case
                for (int yI = 0; yI < input.size(); yI++) { // Loop on all map
                    for (int xI = 0; xI < input.get(yI).length(); xI++) {
                        if (xI != x && yI != y // We check we're not on current location
                                && line.charAt(x) == input.get(yI).charAt(xI)) { // Same antenna type
                            boolean inBounds;
                            int mult = 1; // Harmonic multiplier (add one more distance from first case each turn)
                            do {
                                Location antinode = new Location((xI - x) * mult + xI, (yI - y) * mult + yI);
                                inBounds = antinode.y >= 0 && antinode.y < input.size() && antinode.x >= 0  && antinode.x < input.get(antinode.y).length();
                                if (inBounds) {
                                    locations.add(antinode);
                                }
                                mult++;
                            } while (withHarmonics && inBounds); // No harmonics required or antinode is out of map => we stop
                        }
                    }
                }
            }
        }
        return locations;
    }

    private record Location(int x, int y) {};
}