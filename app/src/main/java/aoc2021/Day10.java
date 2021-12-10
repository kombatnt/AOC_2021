package aoc2021;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Stack;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class Day10 {

    public static void main(final String[] args) {

        // Read the input.
        final List<String> lines;
        try {
            lines = IOUtils
                    .readLines(Day10.class.getClassLoader().getResourceAsStream("Day10.txt"), StandardCharsets.UTF_8);
        } catch (final IOException e) {
            System.err.println("Failed to read data file.");
            return;
        }

        long result = part1(lines);

        System.out.println("Part 1 answer: " + result);

        result = part2(lines);

        System.out.println("Part 2 answer: " + result);

    }

    private static long part1(final List<String> lines) {
        return lines.stream().map(Day10::scoreLineContainingIllegalCharacters).reduce(0L, Long::sum);
    }

    private static long part2(final List<String> lines) {

        final List<Long> scores = lines.stream()
                .filter(Day10::containsNoIllegalCharacters)
                .filter(Day10::isIncomplete)
                .map(Day10::generateClosingTags)
                .map(Day10::scoreClosingTags)
                .sorted()
                .collect(toList());

        return scores.get(scores.size() / 2);
    }

    private static boolean containsNoIllegalCharacters(final String line) {
        return scoreLineContainingIllegalCharacters(line) == 0;
    }

    private static String generateClosingTags(final String line) {
        final Stack<Character> tokenStack = new Stack<>();
        for (final char symbol : line.toCharArray()) {
            switch (symbol) {
            case '(':
            case '[':
            case '{':
            case '<':
                tokenStack.push(symbol);
                continue;

            case ')':
            case ']':
            case '}':
            case '>':
                final char lastOpenCharacter = tokenStack.pop();
                if (!isMatchingPair(lastOpenCharacter, symbol))
                    throw new IllegalArgumentException();
            }
        }

        final StringBuilder closingTags = new StringBuilder();
        while (!tokenStack.isEmpty())
            closingTags.append(getMatchingClosingTag(tokenStack.pop()));
        return closingTags.toString();
    }

    private static boolean isIncomplete(final String line) {
        return StringUtils.isNotEmpty(generateClosingTags(line));
    }

    private static long scoreLineContainingIllegalCharacters(final String line) {

        final Stack<Character> tokenStack = new Stack<>();
        for (final char symbol : line.toCharArray()) {
            switch (symbol) {
            case '(':
            case '[':
            case '{':
            case '<':
                tokenStack.push(symbol);
                continue;

            case ')':
            case ']':
            case '}':
            case '>':
                final char lastOpenCharacter = tokenStack.pop();
                if (!isMatchingPair(lastOpenCharacter, symbol))
                    return getValueOfIllegalCharacter(symbol);
            }
        }
        return 0L;
    }

    private static long scoreClosingTags(final String tags) {

        long score = 0;
        for (final char symbol : tags.toCharArray()) {
            score *= 5;
            switch (symbol) {
            case ')':
                score += 1;
                break;
            case ']':
                score += 2;
                break;
            case '}':
                score += 3;
                break;
            case '>':
                score += 4;
                break;
            default:
                throw new IllegalArgumentException("'" + symbol + "'");
            }
        }
        return score;
    }

    private static boolean isMatchingPair(final char open, final char close) {
        switch (open) {
        case '(':
            return close == ')';
        case '[':
            return close == ']';
        case '{':
            return close == '}';
        case '<':
            return close == '>';
        default:
            return false;
        }
    }

    private static char getMatchingClosingTag(final char open) {
        switch (open) {
        case '(':
            return ')';
        case '[':
            return ']';
        case '{':
            return '}';
        case '<':
            return '>';
        default:
            throw new IllegalArgumentException();
        }

    }

    private static long getValueOfIllegalCharacter(final char illegalCharacter) {
        switch (illegalCharacter) {
        case ')':
            return 3L;
        case ']':
            return 57L;
        case '}':
            return 1197L;
        case '>':
            return 25137L;
        default:
            throw new IllegalArgumentException();
        }
    }
}
