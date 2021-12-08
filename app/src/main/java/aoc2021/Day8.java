package aoc2021;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.collections4.SetUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class Day8 {

    public static void main(String[] args) {

        // Read the input.
        final List<String> lines;
        try {
            lines = IOUtils
                    .readLines(Day8.class.getClassLoader().getResourceAsStream("Day8.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Failed to read data file.");
            return;
        }

        long result = part1(new ArrayList<>(lines));

        System.out.println("Part 1 answer: " + result);

        result = part2(new ArrayList<>(lines));

        System.out.println("Part 2 answer: " + result);

    }

    private static long part1(List<String> lines) {

        long numMatches = 0;
        for (String line : lines) {
            List<String> signalPatterns = Arrays.asList(line.split("\\|")[0].split("\\s+"));
            Map<Set<Character>, Integer> segmentMapper = generateBasicSegmentMap(signalPatterns);

            // Now read the 4-digit output value from this line.
            List<String> output = Arrays.asList(line.split("\\|")[1].split("\\s+"));

            // Turn each chunk into a set of characters.
            List<Set<Character>> charactersInOutput = output.stream().map(Day8::asSet).collect(toList());

            // How many of the sets of characters do we have mappings for?
            numMatches += charactersInOutput.stream().filter(segmentMapper::containsKey).count();
        }

        return numMatches;
    }

    private static long part2(List<String> lines) {

        long total = 0;
        int lineNumber = 1;
        for (String line : lines) {
            List<String> signalPatterns = Arrays.asList(line.split("\\|")[0].split("\\s+"));
            System.out.println("Processing line " + lineNumber++ + ". Patterns: " + signalPatterns);
            Map<Set<Character>, Integer> segmentMapper = generateCompleteSegmentMap(signalPatterns);

            // Now read the 4-digit output value from this line.
            List<String> output = Arrays.asList(line.split("\\|")[1].split("\\s+")).stream()
                    .filter(StringUtils::isNotBlank).collect(toList());

            // Turn each chunk into a set of characters.
            List<Set<Character>> charactersInOutput = output.stream().map(Day8::asSet).collect(toList());

            // Map each set of characters to their corresponding digit,
            // using our segment map.
            total += Long.parseLong(charactersInOutput.stream().map(chars -> segmentMapper.get(chars))
                    .map(Object::toString).collect(joining("")));
        }

        return total;
    }

    private static Map<Set<Character>, Integer> generateBasicSegmentMap(final List<String> signalPatterns) {

        Map<Set<Character>, Integer> segmentMap = new HashMap<>();

        // Find the signal pattern representing "1"
        segmentMap.put(asSet(signalPatterns.stream().filter(pattern -> pattern.length() == 2)
                .findAny().orElseThrow()), 1);

        // Find the signal pattern representing "4"
        segmentMap.put(asSet(signalPatterns.stream().filter(pattern -> pattern.length() == 4)
                .findAny().orElseThrow()), 4);

        // Find the signal pattern representing "7"
        segmentMap.put(asSet(signalPatterns.stream().filter(pattern -> pattern.length() == 3)
                .findAny().orElseThrow()), 7);

        // Find the signal pattern representing "8"
        segmentMap.put(asSet(signalPatterns.stream().filter(pattern -> pattern.length() == 7)
                .findAny().orElseThrow()), 8);

        return segmentMap;
    }

    private static Map<Set<Character>, Integer> generateCompleteSegmentMap(final List<String> signalPatterns) {

        Set<Character> oneCharacters = asSet(signalPatterns.stream().filter(pattern -> pattern.length() == 2)
                .findAny().orElseThrow());
        Set<Character> fourCharacters = asSet(signalPatterns.stream().filter(pattern -> pattern.length() == 4)
                .findAny().orElseThrow());
        Set<Character> sevenCharacters = asSet(signalPatterns.stream().filter(pattern -> pattern.length() == 3)
                .findAny().orElseThrow());
        Set<Character> eightCharacters = asSet(signalPatterns.stream().filter(pattern -> pattern.length() == 7)
                .findAny().orElseThrow());

        // We can solve for b, d, c, and f by comparing the 1, 4, and 7.

        // The segments that's in the 7 set, but not the 1 or 4 is the first
        // segment, our 'a'.
        Set<Character> aChar = SetUtils.difference(sevenCharacters, SetUtils.union(oneCharacters, fourCharacters));

        // The 2 segments that are in 4, but not 7 or 1 will be our 'b' and 'd'
        // segments.
        Set<Character> bdChars = SetUtils.difference(fourCharacters, SetUtils.union(sevenCharacters, oneCharacters));

        // The other 2 segments that are in 4, but are not our 'b' and 'd' segments,
        // are our 'c' and 'f'.
        Set<Character> cfChars = SetUtils.difference(fourCharacters, bdChars);

        // The 2 segments that aren't in any of our numbers so far
        // must be our 'e' and 'g'.
        Set<Character> egChars = SetUtils.difference(eightCharacters,
                SetUtils.union(fourCharacters, SetUtils.union(sevenCharacters, oneCharacters)));

        // Which numbers have 5 segments?
        // 2, 3, 5

        // The only 5-segment number that has both of our b/d segments is 5.
        Set<Character> fiveCharacters = signalPatterns.stream()
                .filter(pattern -> pattern.length() == 5)
                .map(Day8::asSet)
                .filter(characters -> characters.containsAll(bdChars))
                .findAny()
                .orElseThrow();

        // The only 5-segment number that has both of our 'c' and 'f' segments is 3.
        Set<Character> threeCharacters = signalPatterns.stream()
                .filter(pattern -> pattern.length() == 5)
                .map(Day8::asSet)
                .filter(characters -> characters.containsAll(cfChars))
                .findAny()
                .orElseThrow();

        // The only 5-segment number left is 2.
        Set<Character> twoCharacters = signalPatterns.stream()
                .filter(pattern -> pattern.length() == 5)
                .map(Day8::asSet)
                .filter(characters -> !characters.equals(fiveCharacters) && !characters.equals(threeCharacters))
                .findAny()
                .orElseThrow();

        // Which numbers have 6 segments?
        // 0, 6, 9

        // 6 and 9 both have all the same segments as 5, plus one more.
        // Therefore, the one that does NOT meet that condition must be zero.
        Set<Character> zeroCharacters = signalPatterns.stream()
                .filter(pattern -> pattern.length() == 6)
                .map(Day8::asSet)
                .filter(characters -> !characters.containsAll(fiveCharacters))
                .findAny()
                .orElseThrow();

        // 9 has all the same segments as 3, plus one more.
        Set<Character> nineCharacters = signalPatterns.stream()
                .filter(pattern -> pattern.length() == 6)
                .map(Day8::asSet)
                .filter(characters -> characters.containsAll(threeCharacters))
                .findAny()
                .orElseThrow();

        // That just leaves 6, which is the one that differs from the other two.
        Set<Character> sixCharacters = signalPatterns.stream()
                .filter(pattern -> pattern.length() == 6)
                .map(Day8::asSet)
                .filter(characters -> !characters.equals(zeroCharacters) && !characters.equals(nineCharacters))
                .findAny()
                .orElseThrow();

        // Now let's build up our map, using the sets of segments as keys.
        Map<Set<Character>, Integer> segmentMap = new HashMap<>();
        segmentMap.put(zeroCharacters, 0);
        segmentMap.put(oneCharacters, 1);
        segmentMap.put(twoCharacters, 2);
        segmentMap.put(threeCharacters, 3);
        segmentMap.put(fourCharacters, 4);
        segmentMap.put(fiveCharacters, 5);
        segmentMap.put(sixCharacters, 6);
        segmentMap.put(sevenCharacters, 7);
        segmentMap.put(eightCharacters, 8);
        segmentMap.put(nineCharacters, 9);
        return segmentMap;
    }

    private static Set<Character> asSet(final String string) {
        Set<Character> characters = new HashSet<>();
        for (char c : string.toCharArray())
            characters.add(c);
        return characters;
    }
}
