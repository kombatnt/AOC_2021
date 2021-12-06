package aoc2021;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;

public class Day6B {

    private static final short FIRST_GEN_NUM_DAYS = 7;

    public static void main(String[] args) {

        // Read the input.
        final List<Short> school;
        try {
            school = IOUtils
                    .readLines(Day6B.class.getClassLoader().getResourceAsStream("Day6.txt"), StandardCharsets.UTF_8)
                    .stream().flatMap(line -> Stream.of(line.split(",")))
                    .map(Short::parseShort)
                    .collect(toList());
        } catch (IOException e) {
            System.err.println("Failed to read data file [" + args[0] + "].");
            return;
        }

        System.out.println("Starting with " + school.size() + " lantern fish. Ages: " + school);
        
        long[] ageCounts = new long[FIRST_GEN_NUM_DAYS + 2];
        for (int i = 0 ; i < ageCounts.length ; i++)
            ageCounts[i] = 0;
        school.forEach(age -> ageCounts[age]++);
        
        int day = 1;
        long numSpawning;
        for (; day <= 256; day++) {

            // Count how many are ready to spawn (i.e., age is 0),
            // and decrement the ages.
            numSpawning = ageCounts[0];
            
            // Decrement everybody else's age (basically shoving everyone
            // down the array by 1 slot).
            for (int i = 1 ; i < ageCounts.length ; i++)
                ageCounts[i-1] = ageCounts[i];
            
            // Reset the spawners to 6, being careful to keep
            // the ones already there (counting down from 8).
            ageCounts[FIRST_GEN_NUM_DAYS - 1] = ageCounts[FIRST_GEN_NUM_DAYS - 1] + numSpawning;
            
            // Add the new ones at position 8.
            ageCounts[FIRST_GEN_NUM_DAYS + 1] = numSpawning;
            
        }
        
        System.out.println("After " + (day - 1) + " days, there are " + Arrays.stream(ageCounts).sum() + " fish.");

    }

}
