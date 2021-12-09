package aoc2021;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class Day9 {

    public static class Coordinates {
        int row;
        int col;

        public Coordinates(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + col;
            result = prime * result + row;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Coordinates other = (Coordinates) obj;
            if (col != other.col)
                return false;
            if (row != other.row)
                return false;
            return true;
        }
    }

    public static void main(String[] args) {

        // Read the input.
        final List<String> lines;
        try {
            lines = IOUtils
                    .readLines(Day9.class.getClassLoader().getResourceAsStream("Day9.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Failed to read data file.");
            return;
        }

        int[][] map = new int[lines.size()][lines.get(0).length()];
        for (int line = 0; line < lines.size(); line++)
            map[line] = lines.get(line).chars().mapToObj(c -> (char) c - '0').mapToInt(i -> i).toArray();

        long result = part1(map);

        System.out.println("Part 1 answer: " + result);

        result = part2(map);

        System.out.println("Part 2 answer: " + result);

    }

    private static long part1(int[][] terrainMap) {

        List<Integer> lowPoints = new ArrayList<>();
        for (int row = 0; row < terrainMap.length; row++)
            for (int col = 0; col < terrainMap[row].length; col++)
                if (isLowPoint(terrainMap, row, col))
                    lowPoints.add(terrainMap[row][col]);

        return lowPoints.stream().map(i -> i + 1).reduce(0, Integer::sum);
    }

    private static long part2(int[][] terrainMap) {

        List<Long> basinSizes = new ArrayList<>();
        List<Coordinates> visited = new ArrayList<>();
        for (int row = 0; row < terrainMap.length; row++)
            for (int col = 0; col < terrainMap[row].length; col++)
                if (isLowPoint(terrainMap, row, col)) {
                    // Found a low point. Compute the number of points in this basin.
                    long basinSize = basinSize(terrainMap, row, col, visited);
                    basinSizes.add(basinSize);
                    System.out.println("Size of basin at row " + row + " and column " + col + " is " + basinSize);
                }

        return basinSizes.stream().sorted(Collections.reverseOrder()).limit(3).reduce(1L, (a, b) -> a * b);
    }

    private static long basinSize(int[][] terrainMap, int row, int col, List<Coordinates> visited) {

        long basinSize = 1;
        visited.add(new Coordinates(row, col));
        if (canGoUp(terrainMap, row, col) && !visited.contains(new Coordinates(row - 1, col)))
            basinSize += basinSize(terrainMap, row - 1, col, visited);
        if (canGoDown(terrainMap, row, col) && !visited.contains(new Coordinates(row + 1, col)))
            basinSize += basinSize(terrainMap, row + 1, col, visited);
        if (canGoLeft(terrainMap, row, col) && !visited.contains(new Coordinates(row, col - 1)))
            basinSize += basinSize(terrainMap, row, col - 1, visited);
        if (canGoRight(terrainMap, row, col) && !visited.contains(new Coordinates(row, col + 1)))
            basinSize += basinSize(terrainMap, row, col + 1, visited);

        return basinSize;

    }

    private static boolean canGoUp(int[][] terrainMap, int row, int col) {
        return row > 0 && terrainMap[row - 1][col] != 9;
    }

    private static boolean canGoDown(int[][] terrainMap, int row, int col) {
        return row < terrainMap.length - 1 && terrainMap[row + 1][col] != 9;
    }

    private static boolean canGoLeft(int[][] terrainMap, int row, int col) {
        return col > 0 && terrainMap[row][col - 1] != 9;
    }

    private static boolean canGoRight(int[][] terrainMap, int row, int col) {
        return col < terrainMap[row].length - 1 && terrainMap[row][col + 1] != 9;
    }

    private static boolean isLowPoint(int[][] terrainMap, int row, int col) {

        // Up
        if (row > 0 && terrainMap[row - 1][col] <= terrainMap[row][col])
            return false;

        // Down
        if (row < terrainMap.length - 1 && terrainMap[row + 1][col] <= terrainMap[row][col])
            return false;

        // Left
        if (col > 0 && terrainMap[row][col - 1] <= terrainMap[row][col])
            return false;

        // Right
        if (col < terrainMap[row].length - 1 && terrainMap[row][col + 1] <= terrainMap[row][col])
            return false;

        return true;
    }
}
