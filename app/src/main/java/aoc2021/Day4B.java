package aoc2021;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class Day4B {

	private static final class BoardSquare {
		private boolean called;
		private final int value;

		private BoardSquare(final boolean called, final int value) {
			this.called = called;
			this.value = value;
		}

		public static final BoardSquare of(final int value) {
			return new BoardSquare(false, value);
		}

		public boolean isCalled() {
			return called;
		}

		public int getValue() {
			return value;
		}

		public void setCalled(boolean called) {
			this.called = called;
		}

		@Override
		public String toString() {
			return this.value + ": " + (this.called ? "Called" : "Not called");
		}

	}

	public static void main(String[] args) {

		final List<String> rawInput;
		try {
			rawInput = IOUtils.readLines(Day4B.class.getClassLoader().getResourceAsStream("Day4.txt"),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("Failed to read data file [" + args[0] + "].");
			return;
		}

		// Read the numbers being called from the first line.
		final List<Integer> numbersCalled = Stream.of(rawInput.get(0).split(",")).map(Integer::parseInt)
				.collect(toList());

		System.out.println("Numbers to be called: " + numbersCalled);

		// Each Bingo board will be a 2-dimensional array of number/flag pairs.
		final List<BoardSquare[][]> boards = new ArrayList<>();

		// Read the boards.
		int line = 1;
		while (line < rawInput.size()) {

			// Advance to the next non-blank line.
			while (StringUtils.isBlank(rawInput.get(line)) && line < rawInput.size())
				line++;

			if (line >= rawInput.size())
				break;

			// Read the next 5 lines, turn each one into a row of board data.
			BoardSquare[][] currentBoard = new BoardSquare[5][5];
			for (int boardRow = 0; boardRow < 5; boardRow++) {
				currentBoard[boardRow] = Stream.of(rawInput.get(line++).split("\\s+"))
						.filter(StringUtils::isNotBlank)
						.map(Integer::parseInt)
						.map(BoardSquare::of)
						.collect(toList()).toArray(new BoardSquare[0]);
			}

			boards.add(currentBoard);
		}

		System.out.println("Read in " + boards.size() + " boards.");

		// Now call the numbers one by one, and update the boards.
		int calledNumberIndex = 0;
		BoardSquare[][] winningBoard = null;
		for (; calledNumberIndex < numbersCalled.size(); calledNumberIndex++) {

			final int numberCalled = numbersCalled.get(calledNumberIndex);

			System.out.println("Calling number " + numberCalled);

			boards.forEach(board -> callNumber(numberCalled, board));

			// Remove any boards that now have Bingo.
			boards.removeIf(Day4B::hasBingo);
			
			// Are we down to 1 left yet?
			if (boards.size() > 1)
				continue;

			winningBoard = boards.get(0);

			break;
		}
		
		// Need to keep going until this last board has Bingo.
		int numberCalled = 0;
		while (!hasBingo(winningBoard)) {
			numberCalled = numbersCalled.get(calledNumberIndex++);

			System.out.println("Calling number " + numberCalled);
			
			callNumber(numberCalled, winningBoard);
		}

		System.out.println("Last remaining board to win is \n" + toString(winningBoard));
		
		// Sum up all the numbers that WEREN'T called on that board.
		int sum = 0;
		for (int row = 0; row < winningBoard.length; row++)
			for (int col = 0; col < winningBoard[row].length; col++)
				if (!winningBoard[row][col].isCalled())
					sum += winningBoard[row][col].getValue();

		System.out.println("Sum of all uncalled numbers on the winning board is " + sum);
		System.out.println("Sum " + sum + " multiplied by the winning number (" + numberCalled
				+ ") is " + sum * numberCalled);
	}

	private static void callNumber(final int number, final BoardSquare[][] board) {
		for (int row = 0; row < board.length; row++)
			for (int col = 0; col < board[row].length; col++)
				if (board[row][col].getValue() == number)
					board[row][col].setCalled(true);
	}

	private static boolean hasBingo(final BoardSquare[][] board) {

		// Check rows.
		for (int row = 0; row < board.length; row++)
			if (Stream.of(board[row]).allMatch(BoardSquare::isCalled))
				return true;

		// Check columns.
		int column = 0;
		while (column < board[0].length) {
			boolean hasBingo = true;
			for (int row = 0; row < board.length; row++)
				if (!board[row][column].isCalled())
					hasBingo = false;
			if (hasBingo)
				return true;
			column++;
		}

		return false;
	}

	private static String toString(final BoardSquare[][] board) {
		StringBuilder result = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++)
				result.append(String.format("%3s", board[row][column].getValue()));
			result.append("\n");
		}
		return result.toString();
	}
}
