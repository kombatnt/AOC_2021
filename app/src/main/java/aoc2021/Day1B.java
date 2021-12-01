package aoc2021;

import java.util.ArrayList;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class Day1B {

	public static void main(String[] args) {

		final List<Integer> depths;
		try {
			depths = IOUtils
					.readLines(Day1B.class.getClassLoader().getResourceAsStream("Day1.txt"), StandardCharsets.UTF_8)
					.stream().filter(NumberUtils::isParsable).map(Integer::parseInt).collect(toList());
		} catch (IOException e) {
			System.err.println("Failed to read data file [" + args[0] + "].");
			return;
		}

		System.out.println("Found " + depths.size() + " depth measurements.");

		Map<Integer, List<Integer>> measurementGroups = new HashMap<>();

		for (int i = 0; i < depths.size(); i++) {
			measurementGroups.computeIfAbsent(i, ArrayList<Integer>::new).add(depths.get(i));
			if (i - 1 >= 0)
				measurementGroups.get(i - 1).add(depths.get(i));
			if (i - 2 >= 0)
				measurementGroups.get(i - 2).add(depths.get(i));
		}

		final List<Integer> tuples;
		tuples = measurementGroups.values().stream()
				.filter(l -> l.size() >= 3)
				.map(l -> l.stream().reduce(0, Integer::sum))
				.collect(toList());

		int numIncreases = 0;
		for (int i = 1; i < tuples.size(); i++)
			if (tuples.get(i) > tuples.get(i - 1))
				numIncreases++;

		System.out.println("The depth increased " + numIncreases + " times.");
	}
}
