package org.dle.adventofcode2024;

import java.util.*;

class Day12 {

    long part1(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);
        return calcCost(strings, false);
    }

    long part2(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);
        return calcCost(strings, true);
    }

    private long calcCost(List<String> strings, boolean straightLines) {
        GardenMap gardenMap = new GardenMap(strings);
        int region = 0, xMin = 0, yMin = 0;
        do {
            if (gardenMap.regions[yMin][xMin] == -1) { // If this new location hasn't been visited,
                gardenMap.regions[yMin][xMin] = region++; // increment region number and set it.
            }

            for(int y = yMin; y < gardenMap.ySize(); y++) { // Loop on submap starting at xMin, yMin
                for (int x = xMin; x < gardenMap.xSize(); x++) {
                    if (gardenMap.regions[y][x] > -1) { // if location hasn't been visited,
                        visitLocation(x+1, y, x, y, gardenMap); // Visit right location
                        visitLocation(x, y+1, x, y, gardenMap); // Visit bottom location
                    }
                }
            }
            // Increment xMin and yMin logic
            xMin++;
            if(xMin % gardenMap.xSize() == 0) {
                yMin++;
                xMin = 0;
            }
        } while (gardenMap.isInbounds(xMin, yMin)); // increment start position until we're in garden bounds
//        Arrays.stream(gardenMap.regions).map(Arrays::toString).forEach(System.out::println);

        return straightLines ? calcRegionCostPart2(gardenMap, region) : calcRegionCostPart1(gardenMap, region);
    }

    private void visitLocation(int x, int y, int currX, int currY, GardenMap gardenMap) {
        if (!gardenMap.isInbounds(x, y) || gardenMap.regions[y][x] > -1) { // Out of bounds or already visited, nothing to do
            return;
        }

        if (gardenMap.value(x, y) == gardenMap.value(currX, currY)) { // Neighbor is of the same type
            gardenMap.regions[y][x] = gardenMap.regions[currY][currX]; // Set the same region
            visitLocation(x, y+1, x, y, gardenMap); // Recursive loop on all 4 neighbors
            visitLocation(x+1, y, x, y, gardenMap); // To propagate recursively the region
            visitLocation(x, y-1, x, y, gardenMap); // If neighbor is already visited or out of bound,
            visitLocation(x-1, y, x, y, gardenMap); // it won't propagate any further
        }
    }

    private long calcRegionCostPart1(GardenMap gardenMap, int maxRegion) {
        long cost = 0L;
        for (int r = 0; r < maxRegion; r++) {
            long regionSize = 0L, regionSides = 0L;
            for (int y = 0; y < gardenMap.ySize(); y++) {
                for (int x = 0; x < gardenMap.xSize(); x++) {
                    if (gardenMap.regions[y][x] == r) { // Same region
                        regionSize++;
                        regionSides += 4; // Start sides at 4, and decrease for each neighbor of same region
                        regionSides -= removeSide(x, y, x + 1, y, gardenMap);
                        regionSides -= removeSide(x, y, x - 1, y, gardenMap);
                        regionSides -= removeSide(x, y, x, y + 1, gardenMap);
                        regionSides -= removeSide(x, y, x, y - 1, gardenMap);
                    }
                }
            }
            cost += regionSize * regionSides;
        }
        return cost;
    }

    private long calcRegionCostPart2(GardenMap gardenMap, int maxRegion) {
        long cost = 0L;
        for (int r = 0; r < maxRegion; r++) {
            int finalR = r;
            long regionSize = Arrays.stream(gardenMap.regions).flatMapToInt(Arrays::stream).filter(reg -> reg == finalR).count();

            int xSides = nbSides(gardenMap, false, r); // Right and left sides
            int ySides = nbSides(gardenMap, true, r); // Top and bottom sides

            // System.out.println(r + " -> x=" + xSides + " y=" + ySides + " size=" + regionSize + " cost=" + regionSize * (xSides + ySides));
            cost += regionSize * (xSides + ySides);
        }
        return cost;
    }

    // Vertical (or horizontal) just change the scan order of table (by rows or by column)
    private int nbSides(GardenMap gardenMap, boolean vertical, int region) {
        var mapIn = new HashMap<Integer, List<Integer>>(); // Maps of dir1-indexes (y in case of horizontal scan, x in vertical scan)
        var mapOut = new HashMap<Integer, List<Integer>>(); // where we found a starting (in) or ending (out) region
        for (int dir1 = 0; dir1 < (vertical ? gardenMap.xSize() : gardenMap.ySize()); dir1++) {
            for (int dir2 = 0; dir2 < (vertical ? gardenMap.ySize() : gardenMap.xSize()); dir2++) {
                boolean inRegion = gardenMap.regions[dir1][dir2] == region; // In current region
                // calculate if previous (left or top) next (right or bottom) location is also in region
                boolean prevInRegion = gardenMap.isInbounds(dir2 - 1, dir1) && gardenMap.regions[dir1][dir2 - 1] == region;
                boolean nextInRegion = gardenMap.isInbounds(dir2 + 1, dir1) && gardenMap.regions[dir1][dir2 + 1] == region;
                if (inRegion && !prevInRegion) { // We detect a starting region
                    if (!mapIn.containsKey(dir2)) mapIn.put(dir2, new ArrayList<>());
                    mapIn.get(dir2).add(dir1); // We save index of the other direction (y in case of horizontal)
                }
                if (inRegion && !nextInRegion) { // We detect an ending region
                    if (!mapOut.containsKey(dir2)) mapOut.put(dir2, new ArrayList<>());
                    mapOut.get(dir2).add(dir1); // We save index of the other direction (y in case of horizontal)
                }
            }
        }
        int nbIn = mapIn.values().stream().map(this::calcNbSides).reduce(0, Integer::sum);
        int nbOut = mapOut.values().stream().map(this::calcNbSides).reduce(0, Integer::sum);
        return nbIn + nbOut;
    }

    private int calcNbSides(List<Integer> index) {
        int nbSides = 1;
        for (int i = 1; i < index.size(); i++) { // For all indexes, we calculate the number of intervals
            if (index.get(i) - index.get(i - 1) > 1) { // To only count as one following indexes
                nbSides++;
            }
        }
        return nbSides;
    }

    private long removeSide(int x, int y, int newX, int newY, GardenMap gardenMap) { // Return 1 to remove this side, 0 otherwise
        return gardenMap.isInbounds(newX, newY) && gardenMap.regions[y][x] == gardenMap.regions[newY][newX] ? 1 : 0;
    }

    private static class GardenMap { // Easier way to manipulate coordinates
        private final List<String> map;
        final int[][] regions;
        final boolean[][] visited;

        GardenMap(List<String> map) {
            this.map = map;
            regions = new int[map.size()][map.getFirst().length()];
            Arrays.stream(regions).forEach(a -> Arrays.fill(a, -1));
            visited = new boolean[map.size()][map.getFirst().length()];
        }

        char value(int x, int y) {
            return map.get(y).charAt(x);
        }

        int ySize() {
            return map.size();
        }

        int xSize() {
            return map.getFirst().length();
        }

        boolean isInbounds(int x, int y) {
            return x >= 0 && x < xSize() && y >= 0 && y < ySize();
        }
    }
}