package aoc2021;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;

public class Day6A {

    private static final int FIRST_GEN_NUM_DAYS = 6;

    private static final class Lanternfish {
        private int gestationDaysLeft;

        private Lanternfish(final int gestationDaysLeft) {
            this.gestationDaysLeft = gestationDaysLeft;
        }

        public static Lanternfish of(final int gestationDaysLeft) {
            return new Lanternfish(gestationDaysLeft);
        }

        public void gestate() {
            this.gestationDaysLeft--;
        }

        public boolean isReadyToSpawn() {
            return this.gestationDaysLeft == 0;
        }

        public boolean isNotReadyToSpawn() {
            return !this.isReadyToSpawn();
        }

        public Lanternfish spawn() {
            this.gestationDaysLeft = FIRST_GEN_NUM_DAYS;
            return Lanternfish.of(FIRST_GEN_NUM_DAYS + 2);
        }

        @Override
        public String toString() {
            return Integer.toString(this.gestationDaysLeft);
        }
    }

    public static void main(String[] args) {

        // Read the input.
        final List<Lanternfish> school;
        try {
            school = IOUtils
                    .readLines(Day6A.class.getClassLoader().getResourceAsStream("Day6.txt"), StandardCharsets.UTF_8)
                    .stream().flatMap(line -> Stream.of(line.split(",")))
                    .map(Integer::parseInt)
                    .map(initialAge -> Lanternfish.of(initialAge))
                    .collect(toList());
        } catch (IOException e) {
            System.err.println("Failed to read data file [" + args[0] + "].");
            return;
        }

        System.out.println("Creating " + school.size() + " lantern fish.");

        System.out.println("Initial state: " + school.stream().map(Lanternfish::toString).collect(joining(",")));

        int day = 1;
        for (; day <= 80; day++) {

            // Gather up the set of fish whose timers are already at 0,
            // so we can spawn them and reset their timers.
            List<Lanternfish> readyToSpawn = school.stream().filter(Lanternfish::isReadyToSpawn).collect(toList());

            // For any fish who are NOT ready to spawn, process
            // another day of gestation.
            school.stream().filter(Lanternfish::isNotReadyToSpawn).forEach(Lanternfish::gestate);

            // Now process the ones that are spawning, and add their spawn
            // to the school.
            school.addAll(readyToSpawn.stream().map(Lanternfish::spawn).collect(toList()));
        }
        
        System.out.println("After " + day + " days, there are " + school.size() + " fish.");

    }

}
