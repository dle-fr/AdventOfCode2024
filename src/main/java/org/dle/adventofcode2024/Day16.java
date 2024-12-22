package org.dle.adventofcode2024;

import lombok.Setter;
import lombok.experimental.Accessors;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

class Day16 extends AoCVisual.Drawable<Day16.DrawData> {

    final static Coordinates RIGHT = new Coordinates(1, 0);
    final static Coordinates DOWN = new Coordinates(0, 1);
    final static Coordinates LEFT = new Coordinates(-1, 0);
    final static Coordinates UP = new Coordinates(0, -1);
    final static List<Coordinates> C_LIST = Arrays.asList(RIGHT, DOWN, LEFT, UP); // Not optimal but necessary for part2?
    final static Map<Coordinates, Coordinates> TO_RIGHT = Map.of(
            RIGHT, DOWN,
            DOWN, LEFT,
            LEFT, UP,
            UP, RIGHT);
    final static Map<Coordinates, Coordinates> TO_LEFT = Map.of(
            RIGHT, UP,
            UP, LEFT,
            LEFT, DOWN,
            DOWN, RIGHT);

    long part1(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(new Day16());
        return calcFinishingPaths(parseInput(strings), new AoCVisual<>("Maze", this), true)
                .stream().map(p -> p.score).min(Long::compare).orElse(0L);
    }

    long part2(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(new Day16());
        List<Path> paths = calcFinishingPaths(parseInput(strings), new AoCVisual<>("Maze", this), false);
        return calcBestPlaces(paths);
    }

    private long calcBestPlaces(List<Path> paths) {
        List<Path> best = paths.stream().collect(groupingBy(p -> p.score, TreeMap::new, toList())).firstEntry().getValue();
        return best.stream().map(this::getFullPath).flatMap(Collection::stream).distinct().count();
    }

    private List<Path> calcFinishingPaths(Maze maze, AoCVisual<DrawData> mazeViz, boolean draw) {
        Path start = new Path().coordinates(maze.start).straightDir(RIGHT);
        LinkedList<Path> paths = new LinkedList<>();
        paths.add(start);
        List<Path> finishedPaths = new ArrayList<>();

        while (!paths.isEmpty()) {
            Path toVisit = paths.remove();
            Coordinates straight = new Coordinates(toVisit.straightDir.x, toVisit.straightDir.y);
            Path path = calcPath(toVisit, straight, 1, maze);
            if (path.active) paths.add(path);
            if (path.finished) finishedPaths.add(path);
            path = calcPath(toVisit, TO_LEFT.get(straight), 1001, maze); // 1OOO for turning and 1 for walking
            if (path.active) paths.add(path);
            if (path.finished) finishedPaths.add(path);
            path = calcPath(toVisit, TO_RIGHT.get(straight), 1001, maze); // 1OOO for turning and 1 for walking
            if (path.active) paths.add(path);
            if (path.finished) finishedPaths.add(path);
            toVisit.active = false;

            if (draw) {
                long unfinishedScore = paths.stream().map(p -> p.score).min(Long::compare).orElse(0L);
                long finishedScore = finishedPaths.stream().map(p -> p.score).min(Long::compare).orElse(0L);
                List<Path> all = new ArrayList<>(paths);
                all.addAll(finishedPaths);
                long waitTime = paths.isEmpty() ? 3000 : 20 - paths.size();
                mazeViz.drawAndWait(new DrawData(maze, all),
                        "Current lowest score: " + finishedScore
                                + " (unfinished: " + unfinishedScore + ")"
                                + " nbPaths: " + paths.size(), waitTime);
            }
        }
        return finishedPaths;
    }

    private List<Coordinates> getFullPath(Path path) {
        List<Coordinates> res = new ArrayList<>();
        Path curr = path;
        while (curr != null) {
            res.add(curr.coordinates);
            curr = curr.parent;
        }
        return res;
    }

    public record DrawData(Maze maze, List<Path> leafs) {}

    private Path calcPath(Path curr, Coordinates dir, long newScore, Maze maze) {
        Coordinates target = new Coordinates(curr.coordinates.x + dir.x, curr.coordinates.y + dir.y);
        Path res = new Path().coordinates(target).parent(curr).score(curr.score + newScore).finished(target.x == maze.end.x && target.y == maze.end.y);
        if (maze.outOfBounds(target) || "#".equals(maze.get(target))) {
            res.active(false);
            return res;
        }
        if (maze.scores[target.y][target.x][C_LIST.indexOf(dir)] > 0 && maze.scores[target.y][target.x][C_LIST.indexOf(dir)] < res.score) {
            res.active(false);
            return res;
        } else {
            maze.scores[target.y][target.x][C_LIST.indexOf(dir)] = res.score;
        }
        res.straightDir(dir).active(!res.finished);
        return res;
    }

    private Maze parseInput(List<String> strings) {
        List<String> maze = new ArrayList<>();
        Coordinates start = null;
        Coordinates end = null;
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i).trim();
            if (s.contains("S")) {
                start = new Coordinates(s.indexOf("S"), i);
                s = s.replace("S", ".");
            }
            if (s.contains("E")) {
                end = new Coordinates(s.indexOf("E"), i);
                s = s.replace("E", ".");
            }
            maze.add(s);
        }
        return new Maze(maze, new long[maze.size()][maze.getFirst().length()][4], start, end);
    }

    @Override
    void draw(Graphics g, int width, int height) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        for (int x = 0; x < data.maze.width(); x++) {
            for (int y = 0; y < data.maze.height(); y++) {
                if ("#".equals(data.maze.get(new Coordinates(x, y)))) {
                    g.fillRect(x * width / data.maze.width(), y * height / data.maze.height(),
                            width / data.maze.width() + 1, height / data.maze.height() + 1);
                }
            }
        }
        data.leafs.parallelStream().filter(p -> !p.finished && p.active).forEach(path -> {
            g.setColor(new Color(128, 128, 128));
            getFullPath(path).forEach(c -> g.fillRect(c.x * width / data.maze.width(), c.y * height / data.maze.height(),
                    width / data.maze.width() + 1, height / data.maze.height() + 1));
        });
        data.leafs.stream().filter(p -> p.finished).forEach(path -> {
            g.setColor(new Color(64, 64, 64));
            getFullPath(path).forEach(c -> g.fillRect(c.x * width / data.maze.width(), c.y * height / data.maze.height(),
                    width / data.maze.width() + 1, height / data.maze.height() + 1));
        });
        data.leafs.stream().filter(p -> p.finished).min((p1, p2) -> Math.toIntExact(p1.score - p2.score)).ifPresent(path -> {
            g.setColor(new Color(64, 128, 128));
            getFullPath(path).forEach(c -> g.fillRect(c.x * width / data.maze.width(), c.y * height / data.maze.height(),
                    width / data.maze.width() + 1, height / data.maze.height() + 1));
        });
        g.setColor(Color.GREEN);
        g.fillRect(data.maze.end.x * width / data.maze.width(), data.maze.end.y * height / data.maze.height(),
                width / data.maze.width() + 1, height / data.maze.height() + 1);
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    private static class Path {
        Coordinates coordinates;
        Path parent; // To backtrack path
        Coordinates straightDir;
        long score = 0;
        boolean active = true; // false if no straight / right / left path or score is higher than the smallest finished score
        boolean finished = false;
    }

    record Maze(List<String> maze, long[][][] scores, Coordinates start, Coordinates end) {
        String get(Coordinates c) {
            return maze.get(c.y).substring(c.x, c.x + 1);
        }

        int width() {
            return maze.getFirst().length();
        }

        int height() {
            return maze.size();
        }

        private boolean outOfBounds(Coordinates c) {
            return c.x < 0 || c.x >= width() || c.y < 0 || c.y >= height();
        }
    }

    private record Coordinates(int x, int y) {}
}