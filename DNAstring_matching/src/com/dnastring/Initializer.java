package com.dnastring;

import java.util.ArrayList;
import java.util.Random;

public class Initializer {
	private Random random = new Random();
	private int databaseSize;
	private int dnaLength;
	private String alphabet = "ACTG";
	private ArrayList<String> expectedPatterns = new ArrayList<String>();
	
	public Initializer(){
		setDatabaseSize(10);
		setDnaLength(10);
	}
	
	public Initializer(int databaseSize, int dnaLength, ArrayList<String> patterns) {
		setDatabaseSize(databaseSize);
		setDnaLength(dnaLength);
		setExpectedPatterns(patterns);
	}

	public ArrayList<String> generateRandomDnaDatabase() {
		ArrayList<String> population = new ArrayList<String>();
		for(int i=0;i<databaseSize;i++){
			String temp = new String();
			for(int j = 0; j<dnaLength;j++){
				temp = temp + alphabet.charAt(random.nextInt(alphabet.length()));
			}
			temp = insertPattern(temp);
			population.add(temp);
		}
		return population;
	}

	private String insertPattern(String temp) {
		String selectedPattern = expectedPatterns.get(random.nextInt(expectedPatterns.size()));
		int patternLength = selectedPattern.length();
		int patternLocation = random.nextInt(dnaLength-patternLength);
		char c[] = temp.toCharArray();
		for(int i = 0;i<patternLength;i++){
			c[patternLocation+i] = selectedPattern.charAt(i);
		}
		return new String(c);
	}

	public int getDatabaseSize() {
		return databaseSize;
	}

	public void setDatabaseSize(int size) {
		this.databaseSize = size;
	}

	public int getDnaLength() {
		return dnaLength;
	}

	public void setDnaLength(int dnaLength) {
		this.dnaLength = dnaLength;
	}

	public ArrayList<String> getExpectedPatterns() {
		return expectedPatterns;
	}

	public void setExpectedPatterns(ArrayList<String> expectedPatterns) {
		this.expectedPatterns = expectedPatterns;
	}

}
