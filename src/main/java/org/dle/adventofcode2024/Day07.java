package org.dle.adventofcode2024;

import java.util.List;

import static org.dle.adventofcode2024.AoCUtils.toLongs;

class Day07 {

    long part1(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);
        return strings.parallelStream().map(s -> getCalibrationResult(s, false)).reduce(0L, Long::sum);
    }

    long part2(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);
        return strings.parallelStream().map(s -> getCalibrationResult(s, true)).reduce(0L, Long::sum);
    }

    // Returns the calibration value if equation can be solved, 0 otherwise.
    private long getCalibrationResult(String s, boolean withConcat) {
        long res = Long.parseLong(s.split(":")[0]);
        List<Long> values = toLongs(s.split(":")[1].trim().split(" "));

        if (values.size() == 1) {
            return res == values.get(0) ? res : 0;
        }

        for (long l = 0; l < (Math.pow(2, values.size() - 1)); l++) { // 2 ^ (values.size() - 1) => number of combinations of + and *
            // Converting current combination to a string representation where 0 = + and 1 = *
            String operators = String.format("%" + (values.size() - 1) + "s", Long.toBinaryString(l)).replace(" ", "0");
            if (operatorsMatches(operators, values, res)) {
                return res;
            }

            if (withConcat) { // Replacing operators by concat operator and retry
                for (long i = 0; i < (Math.pow(2, operators.length())); i++) {
                    String concatMask = String.format("%" + operators.length() + "s", Long.toBinaryString(i)).replace(" ", "0");
                    String operatorsWithConcat = applyConcatMask(operators, concatMask);
                    if (operatorsMatches(operatorsWithConcat, values, res)) {
                        return res;
                    }
                }
            }
        }
        return 0;
    }

    private boolean operatorsMatches(String operators, List<Long> values, Long res) {
        long currRes = values.get(0);
        for (int i = 0; i < operators.length(); i++) { // Looping on all operators to execute operations
            switch (operators.charAt(i)) {
                case '0' -> currRes += values.get(i + 1);
                case '1' -> currRes *= values.get(i + 1);
                case '|' -> currRes = Long.parseLong("" + currRes + values.get(i + 1));
            }
        }
        return currRes == res;
    }

    private String applyConcatMask(String operators, String concatMask) {
        char[] res = operators.toCharArray();
        for (int i = 0; i < concatMask.length(); i++) { // 0 => keep original operator, 1 => replace by concat
            res[i] = concatMask.charAt(i) == '1' ? '|' : res[i];
        }
        return String.valueOf(res);
    }
}