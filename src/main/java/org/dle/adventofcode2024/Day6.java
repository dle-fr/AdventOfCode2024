package org.dle.adventofcode2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Day6 {

    long part1(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);
        char[][] map = strings.stream().map(String::toCharArray).toArray(char[][]::new);

        Guard guard = findGuard(map);
        while (true) {
            while(!canWalk(guard, map)) { // Turn in the right direction
                guard = calcNewDirection(guard, map);
            }

            guard = walk(guard, map); // Advance one case

            if (nextMoveOut(guard, map)) { // Can get out of map
                return nbCasesVisited(map);
            }
        }
    }

    // It's getting out of hands... 16s on a 7800X3D on all cores...
    long part2(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);
        char[][] map = strings.stream().map(String::toCharArray).toArray(char[][]::new);

        Guard guard = findGuard(map);

        CompletableFuture<Long>[] threads = new CompletableFuture[map.length];

        for (int y = 0; y < map.length; y++) {
            int finalY = y;
            char[][] tMap = Arrays.stream(map).map(char[]::clone).toArray(char[][]::new); // Copy map for this thread
            threads[y] = CompletableFuture.supplyAsync(() -> {
                long tNbStuckPos = 0;
                for (int x = 0; x < tMap[finalY].length; x++) {
                    char prevChar = tMap[finalY][x];
                    if (prevChar != '#') { // Minor perf improvement
                        tMap[finalY][x] = '#';
                        if (isStuckInLoop(guard, tMap)) {
                            tNbStuckPos++;
                        }
                        tMap[finalY][x] = prevChar; // Reset for next try
                    }
                }
                return tNbStuckPos;
            });
        }

        return Stream.of(threads)
                .map(CompletableFuture::join)
                .reduce(0L, Long::sum);
    }

    private record Guard(int x, int y, int dirX, int dirY) {};

    private Guard findGuard(char[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                String dir = "" + map[y][x];
                if ("<>^v".contains(dir)) {
                    switch (dir) {
                        case "<": return new Guard(x, y, -1, 0);
                        case ">": return new Guard(x, y, 1, 0);
                        case "^": return new Guard(x, y, 0, -1);
                        case "v": return new Guard(x, y, 0, 1);
                        default: break;
                    }
                }
            }
        }
        throw new RuntimeException("Guard not found!");
    }

    private boolean canWalk(Guard guard, char[][] map) {
        if (nextMoveOut(guard, map)) {
            return true;
        }
        return map[guard.y + guard.dirY][guard.x + guard.dirX] != '#';
    }

    // Returns guard facing right direction
    private Guard calcNewDirection(Guard guard, char[][] map) {
        if (guard.dirX == -1) return new Guard(guard.x, guard.y, 0, -1);
        else if (guard.dirX == 1) return new Guard(guard.x, guard.y, 0, 1);
        else if (guard.dirY == -1) return new Guard(guard.x, guard.y, 1, 0);
        else if (guard.dirY == 1) return new Guard(guard.x, guard.y, -1, 0);
        return null;
    }

    private Guard walk(Guard guard, char[][] map) {
        map[guard.y][guard.x] = 'X';
        return new Guard(guard.x + guard.dirX, guard.y + guard.dirY, guard.dirX, guard.dirY);
    }

    private boolean nextMoveOut(Guard guard, char[][] map) {
        int newX = guard.x + guard.dirX;
        int newY = guard.y + guard.dirY;
        return newX < 0 || newX >= map[0].length || newY < 0 || newY >= map.length;
    }

    private long nbCasesVisited(char[][] map) {
        return Arrays.stream(map)
                .map(c -> IntStream.range(0, c.length).mapToObj(i -> c[i]))
                .flatMap(cs -> cs.map(c -> c == 'X' ? 1 : 0))
                .reduce(0, Integer::sum) + 1;
    }

    private boolean isStuckInLoop(Guard guard, char[][] map) {
        List<Guard> allGuardPos = new ArrayList<>();
        while (true) {
            allGuardPos.add(guard); // Add this position to all positions visited

            while(!canWalk(guard, map)) { // Turn in the right direction
                guard = calcNewDirection(guard, map);
            }

            guard = walk(guard, map); // Advance one case

            if (nextMoveOut(guard, map)) { // Can get out of map
                return false;
            }
            if (allGuardPos.contains(guard)) { // Already been here in same direction: we're stuck
                return true;
            }
        }
    }
}