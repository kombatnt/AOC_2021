package aoc2021;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class Day1A {

	public static void main(String[] args) {

		final List<Integer> depths;
		try {
			depths = IOUtils
					.readLines(Day1A.class.getClassLoader().getResourceAsStream("Day1.txt"), StandardCharsets.UTF_8)
					.stream().filter(NumberUtils::isParsable).map(Integer::parseInt).collect(toList());
		} catch (IOException e) {
			System.err.println("Failed to read data file [" + args[0] + "].");
			return;
		}

		System.out.println("Found " + depths.size() + " depth measurements.");

		int numIncreases = 0;
		for (int i = 1; i < depths.size(); i++)
			if (depths.get(i) > depths.get(i - 1))
				numIncreases++;
		
		System.out.println("The depth increased " + numIncreases + " times.");
	}
}
