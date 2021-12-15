package aoc2021;

import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class Day14 {

    public static void main(final String[] args) {

        // Read the input.
        final List<String> lines;
        try {
            lines = IOUtils
                    .readLines(Day14.class.getClassLoader().getResourceAsStream("Day14.txt"), StandardCharsets.UTF_8);
        } catch (final IOException e) {
            System.err.println("Failed to read data file.");
            return;
        }

        final String polymerTemplate = lines.get(0);

        final Map<String, String> pairInsertionRules = lines.stream()
                .skip(1L)
                .filter(StringUtils::isNotBlank)
                .collect(toMap(line -> line.substring(0, line.indexOf("->") - 1),
                        line -> line.substring(line.indexOf("->") + "->".length() + 1)));

        System.out.println(pairInsertionRules);

        long result;

        result = part1(polymerTemplate, pairInsertionRules);

        System.out.println("Part 1 answer: " + result);

        result = part2(polymerTemplate, pairInsertionRules);

        System.out.println("Part 2 answer: " + result);

    }

    private static long part1(final String polymerTemplate, final Map<String, String> pairInsertionRules) {

        final String finalFormula = process(polymerTemplate, pairInsertionRules, 10);
        System.out.println("Part 1: After 10 loops: " + finalFormula);
        return finalFormula.chars()
                .mapToObj(i -> (char) i)
                .distinct()
                .map(c -> (long) StringUtils.countMatches(finalFormula, c))
                .max(Long::compareTo)
                .orElseThrow(IllegalArgumentException::new) -
                finalFormula.chars()
                        .mapToObj(i -> (char) i)
                        .distinct()
                        .map(c -> (long) StringUtils.countMatches(finalFormula, c))
                        .min(Long::compareTo)
                        .orElseThrow(IllegalArgumentException::new);
    }

    private static long part2(final String polymerTemplate, final Map<String, String> pairInsertionRules) {

        // Compute all possible pair mappings.
        final Map<String, String> pairMap = pairInsertionRules.keySet().stream().collect(toMap(key -> key,
                key -> process(key, pairInsertionRules, 10)));

        // Now, expand the actual polymerTemplate into its resulting formula.
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < polymerTemplate.length() - 1; i++) {
            final String pair = polymerTemplate.substring(i, i + 2);
            final String currentResult = pairMap.get(pair);
            // System.out.println("Expanding [" + pair + "] to " + currentResult);
            result.append(currentResult.substring(i > 0 ? 1 : 0));
        }

        System.out.println("Part 2: After 10 loops: " + result.toString());

        return 0L;
    }

    private static String process(final String polymerTemplate,
            final Map<String, String> pairInsertionRules, final int iterations) {
        String result = polymerTemplate;
        for (int i = 0; i < iterations; i++) {
            result = performPairInsertion(result, pairInsertionRules);
        }
        return result;
    }

    private static String performPairInsertion(final String polymerTemplate,
            final Map<String, String> pairInsertionRules) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < polymerTemplate.length() - 1; i++) {
            final String pair = polymerTemplate.substring(i, i + 2);
            if (i == 0)
                result.append(polymerTemplate.charAt(i));
            if (pairInsertionRules.containsKey(pair))
                result.append(pairInsertionRules.get(pair));
            result.append(polymerTemplate.charAt(i + 1));
        }
        return result.toString();
    }

}
