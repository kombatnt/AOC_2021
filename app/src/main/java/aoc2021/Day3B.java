package aoc2021;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class Day3B {

	public static void main(String[] args) {

		final List<String> rawInput;
		try {
			rawInput = IOUtils.readLines(Day3B.class.getClassLoader().getResourceAsStream("Day3.txt"),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("Failed to read data file [" + args[0] + "].");
			return;
		}
		int numBits = rawInput.get(0).length();

		System.out.println("Found " + rawInput.size() + " data points.");
		System.out.println("The data are each " + numBits + " bits in length.");

		final List<Integer> input = rawInput.stream().map(line -> Integer.parseInt(line, 2)).collect(toList());
		final List<Integer> oxygenDataset = new ArrayList<>(input);
		final List<Integer> co2Dataset = new ArrayList<>(input);

		int oxygenGeneratorRating = -1;
		int co2ScrubberRating = -1;
		List<Integer> oxygenRemovalCandidates;
		List<Integer> co2RemovalCandidates;
		for (int position = numBits; position >= 0; position--) {

			if (oxygenGeneratorRating >= 0 && co2ScrubberRating >= 0)
				break;

			long mask = Math.round(Math.pow(2, position - 1));

			if (oxygenDataset.size() == 1) {
				oxygenGeneratorRating = oxygenDataset.get(0);
			} else {
				long numBitsSetInOxygenDataset = oxygenDataset.stream().filter(num -> (num & mask) == mask).count();
				if (numBitsSetInOxygenDataset >= (double) oxygenDataset.size() / 2.0) {
					oxygenRemovalCandidates = oxygenDataset.stream().filter(num -> (~num & mask) > 0).collect(toList());
					oxygenDataset.removeAll(oxygenRemovalCandidates);
				} else {
					oxygenRemovalCandidates = oxygenDataset.stream().filter(num -> (num & mask) > 0).collect(toList());
					oxygenDataset.removeAll(oxygenRemovalCandidates);
				}
			}

			if (co2Dataset.size() == 1) {
				co2ScrubberRating = co2Dataset.get(0);
			} else {
				long numBitsSetInCo2Dataset = co2Dataset.stream().filter(num -> (num & mask) == mask).count();
				if (numBitsSetInCo2Dataset < (double) co2Dataset.size() / 2.0) {
					co2RemovalCandidates = co2Dataset.stream().filter(num -> (~num & mask) > 0).collect(toList());
					co2Dataset.removeAll(co2RemovalCandidates);
				} else {
					co2RemovalCandidates = co2Dataset.stream().filter(num -> (num & mask) > 0).collect(toList());
					co2Dataset.removeAll(co2RemovalCandidates);
				}
			}

		}

		System.out.println(
				"Oxygen generator rating: " + oxygenGeneratorRating + ", CO2 scrubber rating: " + co2ScrubberRating);
		System.out.println("Product is " + oxygenGeneratorRating * co2ScrubberRating);
	}

}
