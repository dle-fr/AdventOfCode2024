package org.dle.adventofcode2024;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Day03 {

    private static final Pattern MUL_PATTERN = Pattern.compile("(mul\\(\\d+,\\d+)\\)");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");
    private static final String DONT = "don't()";
    private static final String DO = "do()";

    long part1() {
        List<String> strings = AoCUtils.readFile(this);
        long res = 0;
        for (String s : strings) {
            res += calcOneLine(s, false);
        }
        return res;
    }

    long part2() {
        List<String> strings = AoCUtils.readFile(this);
        // Lines must be joined to manage don't() and do() properly
        String oneLine = StringUtils.join(strings);
        return calcOneLine(oneLine, true);
    }

    long calcOneLine(String line, boolean removeDisabledParts) {
        String cleanedLine = removeDisabledParts ? removeDisabledParts(line) : line;
        Matcher matcher = MUL_PATTERN.matcher(cleanedLine);
        long res = 0;
        while (matcher.find()) {
            res += calcOneMatch(matcher.group());
        }
        return res;
    }

    private String removeDisabledParts(String line) {
        String res = line;
        while (res.contains(DONT)) {
            if (res.indexOf(DONT) > res.indexOf(DO) && res.contains(DO)) {
                res = StringUtils.replaceOnce(res, DO, StringUtils.EMPTY);
            } else {
                res = res.substring(0, res.indexOf(DONT))
                        + (res.contains(DO) ? res.substring(res.indexOf(DO) + DO.length()) : StringUtils.EMPTY);
            }
        }
        return res;
    }

    private long calcOneMatch(String match) {
        long res = 1;
        Matcher matcher = NUMBER_PATTERN.matcher(match);
        while (matcher.find()) {
            res *= Long.parseLong(matcher.group());
        }
        return res;
    }
}
