package aoc2021;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class Day2A {

	public static void main(String[] args) {

		final List<String> commands;
		try {
			commands = IOUtils.readLines(Day2A.class.getClassLoader().getResourceAsStream("Day2.txt"),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("Failed to read data file [" + args[0] + "].");
			return;
		}

		System.out.println("Found " + commands.size() + " depth measurements.");

		int depth = 0;
		int distance = 0;
		for (String command : commands) {
			String[] pair = command.split("\\s+");
			int magnitude = Integer.parseInt(pair[1]);
			switch (pair[0]) {
			case "forward":
				distance += magnitude;
				break;
			case "up":
				depth -= magnitude;
				break;
			case "down":
				depth += magnitude;
				break;
			}
		}

		System.out.println("Depth: " + depth + ", Distance: " + distance);
		System.out.println("Product is " + depth * distance);
	}
}
