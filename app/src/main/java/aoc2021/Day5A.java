package aoc2021;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class Day5A {

	private static final class Point {
		private final int x;
		private final int y;

		private Point(final int x, final int y) {
			this.x = x;
			this.y = y;
		}

		public static Point of(final int x, final int y) {
			return new Point(x, y);
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		@Override
		public String toString() {
			return "(" + this.x + ", " + this.y + ")";
		}
		
		public int max() {
			return Math.max(this.x, this.y);
		}
	}

	private static final class Line {
		private final Point start;
		private final Point end;

		private Line(final Point start, final Point end) {
			this.start = start;
			this.end = end;
		}

		public static final Line of(final Point start, final Point end) {
			return new Line(start, end);
		}

		@Override
		public String toString() {
			return this.start + " -> " + this.end;
		}

		public Point getStart() {
			return start;
		}

		public Point getEnd() {
			return end;
		}
		
		public int max() {
			return Math.max(start.max(), end.max());
		}

	}

	public static void main(String[] args) {

		final List<String> rawInput;
		try {
			rawInput = IOUtils.readLines(Day5A.class.getClassLoader().getResourceAsStream("Day5.txt"),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("Failed to read data file [" + args[0] + "].");
			return;
		}

		// Read the input.
		final List<String> input;
		try {
			input = IOUtils.readLines(Day2A.class.getClassLoader().getResourceAsStream("Day5.txt"),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("Failed to read data file [" + args[0] + "].");
			return;
		}

		// Parse each line of input data into a line segment.
		final List<Line> lines = new ArrayList<>();
		for (String currentInputLine : input) {
			String[] coordinatePairs = currentInputLine.split("\\s*->\\s*");
			lines.add(Line.of(
					Point.of(Integer.parseInt(coordinatePairs[0].split(",")[0]),
							Integer.parseInt(coordinatePairs[0].split(",")[1])),
					Point.of(Integer.parseInt(coordinatePairs[1].split(",")[0]),
							Integer.parseInt(coordinatePairs[1].split(",")[1]))));
		}

		System.out.println("Generated " + lines.size() + " lines: \n" + lines);

		// What size grid do we need? What's the biggest value for any x or y value?
		int gridSize = lines.stream().map(Line::max).max(Integer::compare).get() + 1;
		
		// Initialize the grid with 0 counts.
		int[][] grid = new int[gridSize][gridSize];
		for (int x = 0; x < grid.length; x++)
			for (int y = 0; y < grid[x].length; y++)
				grid[x][y] = 0;

		System.out.println("Generated 2-dimensional grid with size " + gridSize);
		
		lines.stream().filter(Day5A::isHorizontalOrVertical).forEach(line -> updateGridWithLine(grid, line));

		System.out.println("Grid after processing all lines:\n" + toString(grid));

		// Count how many times at least 2 lines overlap.
		int numOverlaps = 0;
		for (int y = 0; y < grid[0].length; y++)
			for (int x = 0; x < grid.length; x++)
				if (grid[x][y] >= 2)
					numOverlaps++;
		
		System.out.println("There are " + numOverlaps + " points at which lines overlap.");
	}

	private static boolean isHorizontalOrVertical(final Line line) {
		return isHorizontal(line) || isVertical(line);
	}

	private static boolean isHorizontal(final Line line) {
		return line.getStart().getY() == line.getEnd().getY();
	}

	private static boolean isVertical(final Line line) {
		return line.getStart().getX() == line.getEnd().getX();
	}

	private static void updateGridWithLine(final int[][] grid, final Line line) {

		System.out.println("Updating grid with line " + line);

		if (isHorizontal(line)) {
			int y = line.getStart().getY();
			int x = Math.min(line.getStart().getX(), line.getEnd().getX());
			int endX = Math.max(line.getStart().getX(), line.getEnd().getX());
			while (x <= endX)
				grid[x++][y]++;
		}

		if (isVertical(line)) {
			int x = line.getStart().getX();
			int y = Math.min(line.getStart().getY(), line.getEnd().getY());
			int endY = Math.max(line.getStart().getY(), line.getEnd().getY());
			while (y <= endY)
				grid[x][y++]++;
		}
	}

	private static final String toString(final int[][] grid) {
		StringBuilder result = new StringBuilder();
		for (int y = 0; y < grid[0].length; y++) {
			for (int x = 0; x < grid.length; x++)
				result.append(String.format("%2s", grid[x][y]));
			result.append("\n");
		}
		return result.toString();
	}
}
