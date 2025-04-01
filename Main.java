package application;

import java.util.*;

public class Main {
	static int length = 32;
	static int pop_size = 70;
	static int top_number_of_try = 78;
	static double rate = 0.058;

	public static void main(String[] args) {
		String passcode = newPassword();
		System.out.println("Randomly generated passcode: " + passcode.toString());
		int generationsTaken = Genetic_Algorithm(passcode);
		System.out.println("Number of Generations Checked: " + generationsTaken);
	}

	public static int Genetic_Algorithm(String passcode) {

		List<String> pop = get_first_pop(pop_size);
		int bestFitness = 0;
		String bestSequence = "";
		int ratio = 0;
		long startTime = System.nanoTime();

		for (int generation = 1; generation < top_number_of_try + 1; generation++) {
			List<String> halfPop = half_Pop(pop, passcode);
			List<String> newPop = new ArrayList<>();

			while (newPop.size() < pop_size) {
				String firstParent = halfPop.get(new Random().nextInt(halfPop.size()));
				String secondParent = halfPop.get(new Random().nextInt(halfPop.size()));
				String offspring = crossOver(firstParent, secondParent);
				newPop.add(mutate(offspring, rate));
			}

			pop = newPop;

			for (String individual : pop) {
				int fitness = fitness(individual, passcode);
				if (fitness > bestFitness) {
					bestFitness = fitness;
					bestSequence = individual;
					ratio = (100 * bestFitness) / 32;
				}
			}

			System.out.println("Generation " + generation + ": Best Fitness = " + ratio + " % ");

			if (bestFitness == length) {
				System.out.println("Passcode found!");
				long endTime = System.nanoTime();
				long duration = (endTime - startTime) / 1_000_000;
				System.out.printf("Time taken: %d milliseconds%n", duration);

				return generation;
			}
		}
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1_000_000;
		System.out.printf("Time taken: %d milliseconds%n", duration);
		System.out.println("Failed to find password !!! ");
		return top_number_of_try;
	}

	public static String newPassword() {
		Random ra = new Random();
		StringBuilder passcode = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			passcode.append(ra.nextInt(99) % 2);
		}
		return passcode.toString();
	}

	public static List<String> get_first_pop(int size) {
		List<String> pop = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			pop.add(newPassword());
		}
		return pop;
	}


	public static int fitness(String individual, String passcode) {
		int numOfFitness = 0;
		for (int i = 0; i < length; i++) {
			if (individual.charAt(i) == passcode.charAt(i)) {
				numOfFitness++;
			}
		}
		return numOfFitness;
	}

	public static List<String> half_Pop(List<String> pop, String passcode) {
		List<String> fitnessList = new ArrayList<>();
		List<Integer> score = new ArrayList<>();

		for (String onePop : pop) {
			int fitness = fitness(onePop, passcode);
			fitnessList.add(onePop);
			score.add(fitness);
		}

		for (int i = 0; i < score.size() - 1; i++) {
			for (int j = 0; j < score.size() - i - 1; j++) {
				if (score.get(j) < score.get(j + 1)) {
					int temp = score.get(j);
					score.set(j, score.get(j + 1));
					score.set(j + 1, temp);

					String temp_One_Pop = fitnessList.get(j);
					fitnessList.set(j, fitnessList.get(j + 1));
					fitnessList.set(j + 1, temp_One_Pop);
				}
			}
		}

		List<String> chosenPopulation = new ArrayList<>();
		for (int i = 0; i < pop.size() / 2; i++) {
			chosenPopulation.add(fitnessList.get(i));
		}

		return chosenPopulation;
	}

	public static String crossOver(String chose1, String chose2) {
		Random ra = new Random();
		int cross = ra.nextInt(length - 2) + 2;
		StringBuilder offspring = new StringBuilder(length);

		for (int i = 0; i < cross; i++) {
			offspring.append(chose1.charAt(i));
		}
		for (int i = cross; i < length; i++) {
			offspring.append(chose2.charAt(i));
		}
		return offspring.toString();
	}

	public static String mutate(String offspring, double mutationRate) {
		Random ra = new Random();
		StringBuilder mutated = new StringBuilder(offspring);
		for (int i = 0; i < length; i++) {
			if (ra.nextDouble() < mutationRate) {
				if (offspring.charAt(i) == '0') {
					mutated.setCharAt(i, '1');
				} else {
					mutated.setCharAt(i, '0');
				}
			}
		}
		return mutated.toString();
	}

}