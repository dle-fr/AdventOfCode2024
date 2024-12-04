package org.dle.adventofcode2024;

import java.util.List;

class Day4 {

    long part1(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);
        char[][] chars = strings.stream().map(String::toCharArray).toArray(char[][]::new);
        long res = 0;
        for (int y = 0; y < chars.length; y++) {
            for (int x = 0; x < chars[y].length; x++) {
                if (chars[y][x] == 'X') {
                    res += nbWordsFromThisPlace(x, y, chars);
                }
            }
        }
        return res;
    }

    long part2(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(this);
        char[][] chars = strings.stream().map(String::toCharArray).toArray(char[][]::new);
        long res = 0;
        // No need to search for 'A' on the borders of the matrix
        for (int y = 1; y < chars.length - 1; y++) {
            for (int x = 1; x < chars[y].length - 1; x++) {
                if (chars[y][x] == 'A') {
                    res += nbCrossesFromThisPlace(x, y, chars);
                }
            }
        }
        return res;
    }

    // Nb XMAS in 9 directions from this x,y place
    private long nbWordsFromThisPlace(int x, int y, char[][] input) {
        long nbWords = 0;

        // Clock wise, starting up right
        nbWords += addIfFound(x, y, 1, -1, input);
        nbWords += addIfFound(x, y, 1, 0, input);
        nbWords += addIfFound(x, y, 1, 1, input);
        nbWords += addIfFound(x, y, 0, 1, input);
        nbWords += addIfFound(x, y, -1, 1, input);
        nbWords += addIfFound(x, y, -1, 0, input);
        nbWords += addIfFound(x, y, -1, -1, input);
        nbWords += addIfFound(x, y, 0, -1, input);

        return nbWords;
    }

    // Nb Crosses in 2 diagonals from this x,y place (so, result is 0 or 1)
    private long nbCrossesFromThisPlace(int x, int y, char[][] input) {
        String diag1 = new String(new char[] {input[y - 1][x - 1], input[y][x], input[y + 1][x + 1]});
        String diag2 = new String(new char[] {input[y + 1][x - 1], input[y][x], input[y - 1][x + 1]});
        return ("MAS".equals(diag1) || "SAM".equals(diag1)) && ("MAS".equals(diag2) || "SAM".equals(diag2)) ? 1 : 0;
    }

    private long addIfFound(int x0, int y0, int dirX, int dirY, char[][] input) {
        StringBuilder res = new StringBuilder();
        int currX = x0, currY = y0;
        do {
            res.append(input[currY][currX]);
            currX += dirX;
            currY += dirY;
        } while (currX < input[0].length && currX >= 0 && currY < input.length && currY >= 0);
        return res.indexOf("XMAS") == 0 ? 1 : 0;
    }
}
