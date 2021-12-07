package aoc2021;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;

public class Day7 {

    public static void main(String[] args) {

        // Read the input.
        final List<Integer> initialPositions;
        try {
            initialPositions = IOUtils
                    .readLines(Day7.class.getClassLoader().getResourceAsStream("Day7.txt"), StandardCharsets.UTF_8)
                    .stream().flatMap(line -> Stream.of(line.split(",")))
                    .map(Integer::parseInt)
                    .collect(toList());
        } catch (IOException e) {
            System.err.println("Failed to read data file [" + args[0] + "].");
            return;
        }

        long result = part1(new ArrayList<>(initialPositions));

        System.out.println("Part 1 answer: " + result);
        
        result = part2(new ArrayList<>(initialPositions));
        
        System.out.println("Part 2 answer: " + result);
    }

    private static long part1(List<Integer> initialPositions) {

        long median = computeMedian(initialPositions);

        System.out.println("Median is " + median);
        
        // Compute the sum of the absolute values between
        // each position and the median.
        long fuel = initialPositions.stream().map(val -> Math.abs(median - val)).reduce(0L, Long::sum);

        return fuel;
    }
    
    private static long part2(List<Integer> initialPositions) {
        
        long minimumFuelUsed = Long.MAX_VALUE;
        long totalFuelUsedMovingToCurrentPosition;
        for (int targetPosition = 0 ; targetPosition < Collections.max(initialPositions) ; targetPosition++) {
            totalFuelUsedMovingToCurrentPosition = part2FuelRequiredToMoveToPosition(initialPositions, targetPosition);
            if (totalFuelUsedMovingToCurrentPosition < minimumFuelUsed)
                minimumFuelUsed = totalFuelUsedMovingToCurrentPosition;
        }
        
        return minimumFuelUsed;
    }

    private static final long computeMedian(List<Integer> initialPositions) {

        initialPositions.sort(Integer::compare);
        double median;
        if (initialPositions.size() % 2 == 0)
            median = (((double) initialPositions.get(initialPositions.size() / 2) +
                    (double) initialPositions.get((initialPositions.size() / 2) - 1)) / 2);
        else
            median = (double) initialPositions.get(initialPositions.size() / 2);

        return Math.round(median);
    }
    
    
    private static final long part2FuelRequiredToMoveToPosition(List<Integer> initialPositions, int targetPosition) {
        long fuelRequired = 0;
        for (int currentInitialPosition : initialPositions) {
            int currentFuelRequirement = IntStream.range(0, Math.abs(currentInitialPosition - targetPosition) + 1).reduce(0, Integer::sum);
            fuelRequired += currentFuelRequirement;
        }
        return fuelRequired;
    }
    
}
