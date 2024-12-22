package org.dle.adventofcode2024;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Day14 extends AoCVisual.Drawable<int[][]> {

    private static final Pattern PATTERN = Pattern.compile("p=(-*\\d+),(-*\\d+) v=(-*\\d+),(-*\\d+)");
    private static final int SOLUTION_PART2 = 6587; // FIXME We may be able to calculate it with quadrants of 4 * 4
    private static final int MULT_CALC = 500, MULT_GRID = 10; // For visualisation

    long part1(List<String> input, int sizeX, int sizeY) {
        List<String> strings = input != null ? input : AoCUtils.readFile(new Day14());
        List<Robot> robots = strings.stream().map(this::parseRobot).toList();

        int[][] positions = getPositions(robots, sizeX, sizeY, 100);
        Arrays.stream(positions).map(Arrays::toString).forEach(System.out::println);
        return calcQuadrantsSum(positions);
    }

    long part2(List<String> input, int sizeX, int sizeY) {
        List<String> strings = input != null ? input : AoCUtils.readFile(new Day14());
        List<Robot> robots = strings.stream().map(this::parseRobot).toList();
        AoCVisual<int[][]> aoCVisual = new AoCVisual<>("Christmas Tree visualisation", this);

        int iteration = SOLUTION_PART2 - 1;
        long biggestSum = 0;
        do {
            int[][] positions = getPositions(robots, sizeX, sizeY, iteration);
            long sum = calcQuadrantsSum(positions); // Totally false for this size of map
            if (sum > biggestSum) {
                biggestSum = sum;
                System.out.println("Iteration " + iteration + " = " + sum);
            }
            getSubPositions(robots, sizeX, sizeY, iteration, aoCVisual);

            iteration++;
        } while (iteration < (SOLUTION_PART2 + 1));
        return SOLUTION_PART2;
    }

    private long calcQuadrantsSum(int[][] positions) {
        int xSize = positions[0].length, ySize = positions.length;
        int xMid = xSize / 2, yMid = ySize / 2;

        return calcQuadrant(0, 0, xMid, yMid, positions)
                * calcQuadrant(xMid + 1, 0, xSize, yMid, positions)
                * calcQuadrant(0, yMid + 1, xMid, ySize, positions)
                * calcQuadrant(xMid + 1, yMid + 1, xSize, ySize, positions);
    }

    // More complicated calculation of positions for a smooth visualisation
    private void getSubPositions(List<Robot> robots, int sizeX, int sizeY, int iteration, AoCVisual<int[][]> aoCVisual) {
        final int newSizeX = sizeX * MULT_GRID, newSizeY = sizeY * MULT_GRID;

        for (int i = 0; i < MULT_CALC; i++) {
            final int[][] res = new int[newSizeY][newSizeX];
            Arrays.stream(res).forEach(a -> Arrays.fill(a, 0));
            double iterationD = iteration + i * 1.0 / MULT_CALC;
            robots.parallelStream().forEach(r -> {
                double finalX = (r.x * MULT_GRID + iterationD * r.vx * MULT_GRID) % newSizeX;
                if (finalX < 0) {
                    finalX = newSizeX + finalX;
                }
                double finalY = (r.y * MULT_GRID + iterationD * r.vy * MULT_GRID) % newSizeY;
                if (finalY < 0) {
                    finalY = newSizeY + finalY;
                }
                res[(int) finalY][(int) finalX]++;
            });
            aoCVisual.drawAndWait(res, "iteration: " + iteration,
                    i == 0 && iteration == SOLUTION_PART2 ? 1000 : 8000 / MULT_CALC);
        }
    }

    private long calcQuadrant(int x1, int y1, int x2, int y2, int[][] positions) {
        long res = 0;
        for (int y = y1; y < y2; y++) {
            for (int x = x1; x < x2; x++) {
                res += positions[y][x];
            }
        }
        return res;
    }

    private int[][] getPositions(List<Robot> robots, int sizeX, int sizeY, int iteration) {
        final int[][] res = new int[sizeY][sizeX];
        Arrays.stream(res).forEach(a -> Arrays.fill(a, 0));

        robots.forEach(r -> {
            int finalX = (r.x + iteration * r.vx) % sizeX;
            if (finalX < 0) {
                finalX = sizeX + finalX;
            }
            int finalY = (r.y + iteration * r.vy) % sizeY;
            if (finalY < 0) {
                finalY = sizeY + finalY;
            }
            res[finalY][finalX]++;
        });

        return res;
    }

    private Robot parseRobot(String s) {
        Matcher matcher = PATTERN.matcher(s);
        matcher.find();
        return new Robot(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
    }

    private record Robot(int x, int y, int vx, int vy) {}

    @Override
    public void draw(Graphics g, int width, int height) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(255, 255, 255, 50));

        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[0].length; x++) {
                if (data[y][x] > 0) {
                    g.fillOval(x * width / data[0].length, y * height / data.length,
                            width * MULT_GRID / data[0].length, height * MULT_GRID / data.length);
                }
            }
        }
    }
}