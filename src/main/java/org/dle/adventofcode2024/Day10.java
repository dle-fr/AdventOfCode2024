package org.dle.adventofcode2024;

import java.util.*;
import java.util.stream.IntStream;

class Day10 {

    long part1(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);
        HeightMap map = new HeightMap(strings.stream().map(AoCUtils::stringToInts).toList());

        return IntStream.range(0, map.ySize()).parallel()
                .mapToObj(y -> scoreTrailsByLine(y, map, true))
                .reduce(0L, Long::sum);
    }

    long part2(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);
        HeightMap map = new HeightMap(strings.stream().map(AoCUtils::stringToInts).toList());

        return IntStream.range(0, map.ySize()).parallel()
                .mapToObj(y -> scoreTrailsByLine(y, map, false))
                .reduce(0L, Long::sum);
    }

    private long scoreTrailsByLine(int y, HeightMap map, boolean distinct) {
        return IntStream.range(0, map.xSize())
                .filter(x -> map.get(x, y) == 0) // Filter only coordinates with height 0
                .mapToObj(x -> reachableLocations(List.of(new Location(x, y, 0)), map, distinct))
                .map(List::size) // Score of a location is number of complete trails
                .reduce(0, Integer::sum);
    }

    private List<Location> reachableLocations(List<Location> current, HeightMap map, boolean distinct) {
        List<Location> next = current;
        List<Location> reachable;
        do {
            reachable = next.stream().map(l -> reachableLocations(l, map)).flatMap(List::stream).toList();
            if (!reachable.isEmpty()) { // If there's still reachable locations, trail them
                next = reachableLocations(reachable, map, distinct); // loop
            }
        } while (!reachable.isEmpty()); // No more reachable locations

        if (distinct) {
            return next.stream().filter(l -> l.height == 9).distinct().toList(); // return only distinct 9-height locations
        } else {
            return next.stream().filter(l -> l.height == 9).toList(); // return all 9-height locations (all trails)
        }
    }

    private List<Location> reachableLocations(Location current, HeightMap map) {
        List<Location> locations = new ArrayList<>();
        addIfReachable(locations, new Location(current.x - 1, current.y, current.height + 1), map);
        addIfReachable(locations, new Location(current.x + 1, current.y, current.height + 1), map);
        addIfReachable(locations, new Location(current.x, current.y - 1, current.height + 1), map);
        addIfReachable(locations, new Location(current.x, current.y + 1, current.height + 1), map);
        return locations; // list of locations (max 4) reachable (height + 1) from current location
    }

    private void addIfReachable(List<Location> locations, Location newlocation, HeightMap map) {
        if (map.isInbounds(newlocation) && map.get(newlocation.x, newlocation.y) == newlocation.height) {
            locations.add(newlocation);
        }
    }

    private record Location(int x, int y, int height) {};

    private record HeightMap(List<List<Integer>> map) { // Easier way to manipulate coordinates
        int get(int x, int y) {
            return map.get(y).get(x);
        }

        int ySize() {
            return map.size();
        }

        int xSize() {
            return map.getFirst().size();
        }

        boolean isInbounds(Location l) {
            return l.x >= 0 && l.x < xSize() && l.y >= 0 && l.y < ySize();
        }
    }
}