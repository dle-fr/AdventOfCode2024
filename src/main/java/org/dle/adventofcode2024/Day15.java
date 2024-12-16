package org.dle.adventofcode2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class Day15 {

    private static final List<String> BOXES = Arrays.asList("O", "[", "]");

    long part1(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(new Day15());
        ParsedInput parsedInput = parseInput(strings, false);
        Coordinates robot = parsedInput.robot;
        for (Coordinates instruction : parsedInput.instructions) {
//            System.out.println(parsedInput.warehouse.toStringWithRobot(robot, instruction));
            robot = executeInstruction(parsedInput.warehouse, robot, instruction);
        }
//        System.out.println("\n" + parsedInput.warehouse.toString());
        return calculateCoordsSum(parsedInput.warehouse);
    }

    long part2(List<String> input) {
        List<String> strings = input != null ? input : AoCUtils.readFile(new Day15());
        ParsedInput parsedInput = parseInput(strings, true);
        Coordinates robot = parsedInput.robot;
        for (Coordinates instruction : parsedInput.instructions) {
            System.out.println(parsedInput.warehouse.toStringWithRobot(robot, instruction));
            robot = executeInstruction(parsedInput.warehouse, robot, instruction);
        }
        System.out.println("\n" + parsedInput.warehouse.toString());
        return calculateCoordsSum(parsedInput.warehouse);
    }

    private ParsedInput parseInput(List<String> strings, boolean expand) {
        List<String> map = new ArrayList<>();
        List<Coordinates> instructions = new LinkedList<>();
        Coordinates robot = null;
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i).trim();
            if (!s.isBlank()) {
                if (s.startsWith("#")) {
                    if (s.contains("@")) {
                        robot = new Coordinates(s.indexOf("@") * (expand ? 2 : 1), i, 1);
                        s = s.replace("@", ".");
                    }
                    if (expand) {
                        map.add(expandLine(s));
                    } else {
                        map.add(s);
                    }
                } else {
                    instructions.addAll(Arrays.stream(s.split("")).map(this::toInstruction).toList());
                }
            }
        }
        return new ParsedInput(new Warehouse(map), robot, instructions);
    }

    private String expandLine(String s) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            String c = s.substring(i, i + 1);
            switch (c) {
                case "#" -> res.append("##");
                case "." -> res.append("..");
                case "O" -> res.append("[]");
            }
        }
        return res.toString();
    }

    private Coordinates toInstruction(String s) {
        Coordinates instruction;
        switch (s) {
            case ">" -> instruction = new Coordinates(1, 0, 1);
            case "<" -> instruction = new Coordinates(-1, 0, 1);
            case "v" -> instruction = new Coordinates(0, 1, 1);
            case "^" -> instruction = new Coordinates(0, -1, 1);
            default -> throw new RuntimeException(s);
        }
        return instruction;
    }

    private Coordinates executeInstruction(Warehouse warehouse, Coordinates robot, Coordinates inst) {
        Coordinates target = new Coordinates(robot.x + inst.x, robot.y + inst.y, 1);
        if (warehouse.get(target).equals(".") || BOXES.contains(warehouse.get(target)) && moveBoxes(target, inst, warehouse)) {
            return target;
        }
        return robot;
    }

    private boolean moveBoxes(Coordinates box, Coordinates inst, Warehouse warehouse) {
        Coordinates theBox = new Coordinates(box.x, box.y, box.size);
        if (warehouse.outOfBounds(theBox)|| warehouse.get(box).contains("#")) {
            return false;
        }
        if (warehouse.get(box).equals("[")) {
            theBox = new Coordinates(box.x, box.y, 2);
        }
        if (warehouse.get(box).equals("]")) {
            theBox = new Coordinates(box.x - 1, box.y, 2);
        }
        Coordinates target = new Coordinates(theBox.x + inst.x, theBox.y + inst.y, theBox.size);

        if (warehouse.canMove(theBox, inst) || moveBoxes(target, inst, warehouse)) {
            warehouse.move(theBox, inst);
            return true;
        }
        return false;
    }

    private long calculateCoordsSum(Warehouse warehouse) {
        long res = 0;
        for (int y = 0; y < warehouse.map.size(); y++) {
            for (int x = 0; x < warehouse.map.getFirst().length(); x++) {
                Coordinates coordinates = new Coordinates(x, y, 1);
                if (warehouse.get(coordinates).equals("O") || warehouse.get(coordinates).equals("[")) {
                    res += y * 100L + x;
                }
            }
        }
        return res;
    }

    private record Warehouse(List<String> map) {

        String get(Coordinates c) {
            return map.get(c.y).substring(c.x, c.x + c.size);
        }

        void set(Coordinates c, String value) {
            String newV = map.get(c.y).substring(0, c.x) + value + map.get(c.y).substring(c.x + c.size);
            map.set(c.y, newV);
        }

        boolean canMove(Coordinates box, Coordinates inst) {
            Coordinates target = new Coordinates(box.x + inst.x, box.y + inst.y, box.size);
            if (outOfBounds(target) || get(box).contains("#")) {
                return false;
            }
            String targetS = get(target);
            return inst.y != 0 && targetS.equals(".".repeat(box.size()))
                    || inst.x < 0 && targetS.startsWith(".")
                    || inst.x > 0 && targetS.endsWith(".");
        }

        void move(Coordinates box, Coordinates inst) {
            String boxS = get(box); // Bug when moving some [] boxes
            set(box, ".".repeat(box.size));
            set(new Coordinates(box.x + inst.x, box.y + inst.y, box.size), boxS);
        }

        private boolean outOfBounds(Coordinates target) {
            return target.x < 0 || target.x + target.size >= map.getFirst().length() || target.y < 0 || target.y >= map.size();
        }

        public String toString() {
            return String.join("\n", map);
        }
        public String toStringWithRobot(Coordinates robot, Coordinates inst) {
            List<String> copy = new ArrayList<>(map);
            String dir = "";
            if (inst.x == 0 && inst.y == 1) dir = "v";
            if (inst.x == 0 && inst.y == -1) dir = "^";
            if (inst.x == 1 && inst.y == 0) dir = ">";
            if (inst.x == -1 && inst.y == 0) dir = "<";
            copy.set(robot.y, copy.get(robot.y).substring(0, robot.x) + dir + copy.get(robot.y).substring(robot.x + 1));
            return String.join("\n", copy);
        }
    }

    private record Coordinates(int x, int y, int size) {}

    private record ParsedInput(Warehouse warehouse, Coordinates robot, List<Coordinates> instructions) {}
}