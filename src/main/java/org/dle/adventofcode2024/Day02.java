package org.dle.adventofcode2024;

import java.util.ArrayList;
import java.util.List;

class Day02 {

    int part1() {
        return resolve(false);
    }

    int part2() {
        return resolve(true);
    }

    private int resolve(boolean withDampener) {
        List<String> strings = AoCUtils.readFile(this);
        int safe = 0;
        for (String s : strings) {
            List<Integer> split = AoCUtils.stringWithSpacesToInts(s);
            boolean isSafe = withDampener ? isSafeWithDampener(split) : isSafe(split);
            if (isSafe) {
                safe++;
            }
        }
        return safe;
    }

    private boolean isSafe(List<Integer> list) {
        Integer lastDirection = null;

        for (int i = 1; i < list.size(); i++) {
            int diff = list.get(i) - list.get(i - 1);
            int direction = Integer.compare(diff, 0);

            // Direction must not be equals to 0 (2 identical values in a row)
            boolean isSafe = Math.abs(diff) <= 3 && (lastDirection == null || lastDirection == direction && direction != 0);
            if (!isSafe) {
                return false;
            }
            lastDirection = direction;
        }
        return true;
    }

    public boolean isSafeWithDampener(List<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            List<Integer> dampenedList = new ArrayList<>(list);
            dampenedList.remove(i);
            boolean isSafe = isSafe(dampenedList);
            if (isSafe) {
                return true;
            }
        }
        return false;
    }
}
