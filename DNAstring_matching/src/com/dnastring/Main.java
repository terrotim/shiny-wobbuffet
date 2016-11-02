package com.dnastring;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		int numDnaString = 20;
		int dnaLength = 100;
		ArrayList<String> dnaDatabase = new ArrayList<String>();
		ArrayList<String> expectedPatterns = new ArrayList<String>();
		String gaPattern = new String();
		String emPattern = new String();
		
		//expectedPatterns.add("AAAAAAAAAAAA");
		//expectedPatterns.add("CATCATCATCAT");
		expectedPatterns.add("TTGACATATAAT");
		//expectedPatterns.add("TTGACACATCATGGGGGGCATCATTATAAT");
		Initializer initializer = new Initializer(numDnaString, dnaLength, expectedPatterns);
		dnaDatabase = initializer.generateRandomDnaDatabase();
		
		int generations = 1000;
		int population = 100;
		int mutation = 1;
		int minimumPatternLength = 12;
		
		int numMotif = expectedPatterns.size();
		GeneticAlgorithm ga = new GeneticAlgorithm(dnaDatabase, generations, population, mutation, minimumPatternLength);
		gaPattern = ga.discoverPattern(numMotif);
		System.out.println("Genetic Algorithm predicts " + gaPattern);
		ExpectationMaximization em = new ExpectationMaximization(dnaDatabase, minimumPatternLength, dnaLength, generations);
		emPattern = em.discoverPattern();
		System.out.println("Expectation Maximization predicts " + emPattern);
	}

}
