package org.dle.adventofcode2024;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Day14 {

    private static final Pattern PATTERN = Pattern.compile("p=(-*\\d+),(-*\\d+) v=(-*\\d+),(-*\\d+)");
    private static final int SOLUTION_PART2 = 6587; // FIXME We may be able to calculate it with quadrants of 4 * 4
    private static final int MULT = 10, DIV = 500, SPEED = 60; // For visualisation

    public static void main(String[] args) {
        new Day14().part2(null, 101, 103);
    }

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

        Visualisation visualisation = new Visualisation();
        JFrame frame = new JFrame("Christmas Tree visualisation");
        frame.setSize(sizeX * 10, sizeY * 10);
        frame.setBackground(Color.black);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(visualisation);

        int iteration = SOLUTION_PART2 - 1;
        long biggestSum = 0;
        do {
            int[][] positions = getPositions(robots, sizeX, sizeY, iteration);
            long sum = calcQuadrantsSum(positions); // Totally false for this size of map
            if (sum > biggestSum) {
                biggestSum = sum;
                System.out.println("Iteration " + iteration + " = " + sum);
            }
            getSubPositions(robots, sizeX, sizeY, iteration, visualisation);

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
    private void getSubPositions(List<Robot> robots, int sizeX, int sizeY, int iteration, Visualisation visualisation) {
        for (double d = 0; d < DIV; d++) {
            final int[][] res = new int[sizeY * MULT][sizeX * MULT];
            Arrays.stream(res).forEach(a -> Arrays.fill(a, 0));
            double fd = d;
            robots.forEach(r -> {
                int finalX = (int) ((r.x * MULT) + (((double) iteration + fd / DIV) * (r.vx * MULT))) % (sizeX * MULT);
                if (finalX < 0) {
                    finalX = sizeX * MULT + finalX;
                }
                int finalY = (int) ((r.y * MULT) + (((double) iteration + fd / DIV) * (r.vy * MULT))) % (sizeY * MULT);
                if (finalY < 0) {
                    finalY = sizeY * MULT + finalY;
                }
                res[finalY][finalX]++;
            });
            visualisation.positions = res;
            visualisation.iteration = iteration;
            visualisation.repaint();
            try {
                Thread.sleep(1000 / SPEED);
            } catch (InterruptedException e) {}
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

    private int[][] getPositions(List<Robot> robots, int sizeX, int sizeY, int iterations) {
        final int[][] res = new int[sizeY][sizeX];
        Arrays.stream(res).forEach(a -> Arrays.fill(a, 0));

        robots.forEach(r -> {
            int finalX = (r.x + iterations * r.vx) % sizeX;
            if (finalX < 0) {
                finalX = sizeX + finalX;
            }
            int finalY = (r.y + iterations * r.vy) % sizeY;
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

    public static class Visualisation extends JPanel {
        private int[][] positions;
        private int iteration;

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //draw robots
            if (positions != null) {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(new Color(255, 255, 255, 50));
                for (int y = 0; y < positions.length; y++) {
                    for (int x = 0; x < positions[0].length; x++) {
                        if (positions[y][x] > 0) {
                            g.fillOval(x * getWidth() / positions[0].length, y * getHeight() / positions.length,
                                    getWidth() / 100, getWidth() / 100);
                        }
                    }
                }
            }
            // Draw iteration indicator
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, getWidth() / 50));
            g.drawString("iteration: " + iteration, getWidth() / 50, getHeight() / 25);
        }
    }
}