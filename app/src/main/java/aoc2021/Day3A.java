package aoc2021;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class Day3A {

	public static void main(String[] args) {

		final List<String> rawInput;
		try {
			rawInput = IOUtils.readLines(Day3A.class.getClassLoader().getResourceAsStream("Day3.txt"),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("Failed to read data file [" + args[0] + "].");
			return;
		}
		int numBits = rawInput.get(0).length();

		System.out.println("Found " + rawInput.size() + " data points.");
		System.out.println("The data are each " + numBits + " bits in length.");

		final List<Integer> input = rawInput.stream().map(line -> Integer.parseInt(line, 2)).collect(toList());

		int gammaRate = 0;
		for (int position = 0; position < numBits; position++) {

			final int order = position;
			
			// For each position, count how many of the lines of
			// input have the corresponding bit set.
			long numBitsSet = input.stream()
					.filter(num -> (num & Math.round(Math.pow(2, order))) == Math.round(Math.pow(2, order))).count();
			
			if (numBitsSet > input.size() / 2)
				gammaRate += Math.round(Math.pow(2, order));
		}

		int mask = 0;
		for (int order = 0; order < numBits; order++)
			mask += Math.round(Math.pow(2, order));
		
		System.out.println("Gamma rate: " + gammaRate + ", Epsilon rate: " + (~gammaRate & mask));
		System.out.println("Product is " + gammaRate * (~gammaRate & mask));
	}
}
