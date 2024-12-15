package org.dle.adventofcode2024;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Day13 {

    private static final Pattern X_MATCHER = Pattern.compile(".*X[\\+=](.*),.*");
    private static final Pattern Y_MATCHER = Pattern.compile(".*Y[\\+=](.*)");

    long part1(List<String> input) {
        return calcToken(input, 0);
    }

    long part2(List<String> input) {
        return calcToken(input, 10000000000000L);
    }

    private long calcToken(List<String> input, long error) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);
        List<Machine> machines = new ArrayList<>();
        for(int i = 0; i < strings.size(); i += 4) {
            long ax = getLong(X_MATCHER, strings.get(i));
            long ay = getLong(Y_MATCHER, strings.get(i));
            long bx = getLong(X_MATCHER, strings.get(i+1));
            long by = getLong(Y_MATCHER, strings.get(i+1));
            long prizex = getLong(X_MATCHER, strings.get(i+2));
            long prizey = getLong(Y_MATCHER, strings.get(i+2));
            machines.add(new Machine(ax, ay, bx, by, prizex + error, prizey + error));
        }

        return machines.stream().map(m -> error == 0 ? calcTokensPart1(m) : calcTokensPart2(m, error)).reduce(0L, Long::sum);
    }

    private long getLong(Pattern pattern, String s) {
        Matcher matcher = pattern.matcher(s);
        matcher.find();
        return Long.parseLong(matcher.group(1));
    }

    private long calcTokensPart1(Machine machine) {
        long nbTokens = Long.MAX_VALUE;
        for (int a = 0; a < 100; a++) {
            for (int b = 0; b < 100; b++) {
                if (a * machine.ax + b * machine.bx() == machine.prizex
                        && a * machine.ay + b * machine.by() == machine.prizey) {
                    if (nbTokens > (a * 3 + b)) {
                        nbTokens = a * 3 + b;
                    }
                }
            }
        }
        return nbTokens < Long.MAX_VALUE ? nbTokens : 0;
    }

    // No idea at the moment
    private long calcTokensPart2(Machine machine, long error) {
        long nbTokens = Long.MAX_VALUE;

        long minA = ((error / machine.ax()) / 10) * 10;
        long minB = ((error / machine.bx()) / 10) * 10;

        int finalA = 0, finalB = 0;

        for (int a = 0; a < 100; a++) {
            for (int b = 0; b < 100; b++) {
                long fullA = a + minA;
                long fullB = b + minB;
                long x = fullA * machine.ax + fullB * machine.bx();
                long y = fullA * machine.ay + fullB * machine.by();
//                System.out.println("x=" + x + " y=" + y + " prizex=" + machine.prizex + " prizey=" + machine.prizey);
                if (x == machine.prizex && y == machine.prizey) {
                    if (nbTokens > (fullA * 3 + fullB)) {
                        finalA = a; finalB = b;
                        nbTokens = fullA * 3 + fullB;
                    }
                }
            }
        }

        nbTokens = nbTokens < Long.MAX_VALUE ? nbTokens : 0;
        System.out.println("minA=" + minA + " minB=" + minB + " a=" + finalA + " b=" + finalB + " => tokens " + nbTokens);
        return nbTokens;
    }

    private record Machine(long ax, long ay, long bx, long by, long prizex, long prizey){};
}