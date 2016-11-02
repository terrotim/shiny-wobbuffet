package com.dnastring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class GeneticAlgorithm {
	private Random random = new Random();
	private ArrayList<String> dnaDatabase = new ArrayList<String>();
	private int generations, populationSize, mutation;
	private ArrayList<String> currentPopulation = new ArrayList<String>();
	private ArrayList<Integer> fitnessForPopulation = new ArrayList<Integer>();
	private int bestFitnessScore, minimumPatternLength;
	private int dnaPieceMotifMaxLocation, dnaPieceMotifMaxLength;
	private String bestFitnessString = new String();
	
	public GeneticAlgorithm(ArrayList<String> dnadatabase, int generations, int population, int mutation, int minLength) {
		this.setDnaDatabase(dnadatabase);
		this.setGenerations(generations);
		this.setPopulationSize(population);
		this.setMutation(mutation);
		this.setBestFitnessScore(Integer.MAX_VALUE);
		this.setMinimumPatternLength(minLength);
		generateRandomPopulation();
		determinePopulationFitnesses();
	}
	
	public String discoverPattern(int numMotif) {
		int currentGen = 0;
		ArrayList<String> winners = new ArrayList<String>();
		String result = new String();
		int resultScore;
		int changeCount = 0;
		int prevFitness;
		while (changeCount < dnaDatabase.get(0).length()*getMinimumPatternLength()*numMotif){
			currentGen++;
			System.out.println(currentGen + " : " + getBestFitnessScore());
			winners = getTournamentWinners();
			setCurrentPopulation(getNextGeneration(winners));
			prevFitness = getBestFitnessScore();
			determinePopulationFitnesses();
			if (prevFitness == getBestFitnessScore()){
				changeCount++;
			} else {
				changeCount = 0;
			}
		}
		result = getBestFitnessString();
		resultScore = getBestFitnessScore();
		return obtainCalculatedPattern(result);
	}

	private void determinePopulationFitnesses() {
		int populationsize = getPopulationSize();
		int fitness;
		ArrayList<Integer> fitnesses = new ArrayList<Integer>();
		
		for (int i = 0; i < populationsize; i++){
			fitness = determineFitness(currentPopulation.get(i));
			if(fitness < getBestFitnessScore()){
				setBestFitnessScore(fitness);
				setBestFitnessString(currentPopulation.get(i));
			}
			fitnesses.add(fitness);
		}
		this.setFitnessForPopulation(fitnesses);
	}

	private int determineFitness(String string) {
		int databaseSize = getDnaDatabase().size(); 
		ArrayList<String> database = getDnaDatabase();
		ArrayList<String> substringList = new ArrayList<String>();
		String dnaString = new String();
		String dnaLocation = new String();
		String dnaLength = new String();
		int dnaLocationVal, dnaLengthVal;
		
		for (int i = 0; i<databaseSize; i++){
			dnaString = string.substring((getDnaPieceMotifMaxLocation()+getDnaPieceMotifMaxLength())*i, (getDnaPieceMotifMaxLocation()+getDnaPieceMotifMaxLength())*i+(getDnaPieceMotifMaxLocation()+getDnaPieceMotifMaxLength())); //8 characters at a time
			
			dnaLocation = dnaString.substring(0,getDnaPieceMotifMaxLocation());
			dnaLength = dnaString.substring(getDnaPieceMotifMaxLocation(), getDnaPieceMotifMaxLocation()+getDnaPieceMotifMaxLength());
			dnaLocationVal = Integer.parseInt(dnaLocation, 2);
			dnaLengthVal = Integer.parseInt(dnaLength, 2);
			
			try {
				if (dnaLengthVal < getMinimumPatternLength()){
					substringList.add(""); 
				} else {
					substringList.add(database.get(i).substring(dnaLocationVal, dnaLocationVal+dnaLengthVal));
				}
			} catch(StringIndexOutOfBoundsException e) {
				substringList.add("");
			}
		}
		int fitnessScore = getTotalLevenshteinDistance(substringList);
		return fitnessScore;
	}

	private int getTotalLevenshteinDistance(ArrayList<String> substringList) {
		int totalDistance = 0;
		int listSize = substringList.size();
		
		for(int i = 0; i<listSize; i++){
			for(int j=i+1; j<listSize; j++){
				if (substringList.get(i).length()==0 || substringList.get(j).length()==0){
					totalDistance = (int) (totalDistance + Math.pow(2, getDnaPieceMotifMaxLength()));
				} else {
				totalDistance = totalDistance + getLevenshteinDistance(substringList.get(i), substringList.get(j));
				}
			}
		}		
		return totalDistance;
	}

	private int getLevenshteinDistance(String string, String string2) {
		int[] costs = new int [string2.length() + 1];
		for (int j=0; j < costs.length; j++){
			costs[j] = j;
		}
		for (int i = 1; i <= string.length(); i++){
			costs[0]=i;
			int nw = i - 1;
			for (int j = 1; j <= string2.length(); j++){
				int cj = Math.min(1 + Math.min(costs[j],  costs[j-1]), string.charAt(i-1)==string2.charAt(j-1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[string2.length()];
	}

	private void generateRandomPopulation() {
		ArrayList<String> initialPopulation = new ArrayList<String>();
		int databaseSize = getDnaDatabase().size(); // 30 DNA strings means 30 concatenations or iterations
		int dnaLength = getDnaDatabase().get(0).length(); // A DNA string that is 20 characters long will be represented as binary of length 5
		int longestPatternLength = this.getMinimumPatternLength(); 
		int dnaPieceMotifMaxLocation = Integer.toBinaryString(dnaLength).length();
		int dnaPieceMotifMaxLength = Integer.toBinaryString(longestPatternLength).length();
		setDnaPieceMotifMaxLocation(dnaPieceMotifMaxLocation);
		setDnaPieceMotifMaxLength(dnaPieceMotifMaxLength);
		int populationSize = getPopulationSize();
		
		for (int i = 0; i<populationSize; i++){
			String temp = new String();
			for (int j = 0; j<databaseSize; j++ ){
				for (int k = 0; k<dnaPieceMotifMaxLocation; k++){
					temp = temp + random.nextInt(2);
				}
				for (int k = 0; k<dnaPieceMotifMaxLength; k++){
					temp = temp + random.nextInt(2);
				}
			}
			initialPopulation.add(temp);
		}
		setCurrentPopulation(initialPopulation);
	}
	
	private String obtainCalculatedPattern(String result) {
		int databaseSize = getDnaDatabase().size(); 
		String dnaString = new String();
		String dnaLocation = new String();
		String dnaLength = new String();
		int dnaLocationVal, dnaLengthVal;
		String calculatedPattern = new String();
		ArrayList<String> patternsList = new ArrayList<String>();
		
		for (int i = 0; i<databaseSize; i++){
			dnaString = result.substring((getDnaPieceMotifMaxLocation()+getDnaPieceMotifMaxLength())*i, (getDnaPieceMotifMaxLocation()+getDnaPieceMotifMaxLength())*i+(getDnaPieceMotifMaxLocation()+getDnaPieceMotifMaxLength())); //8 characters at a time
			dnaLocation = dnaString.substring(0,getDnaPieceMotifMaxLocation());  
			dnaLength = dnaString.substring(getDnaPieceMotifMaxLocation(), getDnaPieceMotifMaxLocation()+getDnaPieceMotifMaxLength());
			dnaLocationVal = Integer.parseInt(dnaLocation, 2);
			dnaLengthVal = Integer.parseInt(dnaLength, 2);
			
			try {
				calculatedPattern = getDnaDatabase().get(i).substring(dnaLocationVal, dnaLocationVal+dnaLengthVal);
				if (calculatedPattern.length()>=getMinimumPatternLength()){
				patternsList.add(calculatedPattern);
				}
			} catch(StringIndexOutOfBoundsException e) {
			}
		}
		Map<String, Integer> patternChance = new TreeMap<String, Integer>();
		for (String pattern : patternsList){
			patternChance.put(pattern, 1+(patternChance.containsKey(pattern) ? patternChance.get(pattern) : 0));
		}
		patternChance = sortByValue(patternChance);
		String key = new String();
		for(Map.Entry<String, Integer> entry : patternChance.entrySet()){
			System.out.println(entry.getKey() + " (" + Math.round(((double)(entry.getValue())/databaseSize)*100) + "%)");
			key = entry.getKey();
		}
		return key;
	}
	
	static Map sortByValue(Map map) {
	     List list = new LinkedList(map.entrySet());
	     Collections.sort(list, new Comparator() {
	          public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	              .compareTo(((Map.Entry) (o2)).getValue());
	          }
	     });

	    Map result = new LinkedHashMap();
	    for (Iterator it = list.iterator(); it.hasNext();) {
	        Map.Entry entry = (Map.Entry)it.next();
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	} 
	
	private ArrayList<String> getNextGeneration(ArrayList<String> w) {
		ArrayList<String> newPop = new ArrayList<String>();
		String child1 = new String();
		String child2 = new String();
		int crossoverPoint;
		int temp, temp2;
		while(w.size() > 0){
			temp = random.nextInt(w.size());
			temp2 = random.nextInt(w.size());
			while(temp == temp2){
				temp2 = random.nextInt(w.size());
			}
			crossoverPoint = random.nextInt(getCurrentPopulation().get(0).length());
			child1 = crossover(w.get(temp), w.get(temp2), crossoverPoint);
			child1 = mutate(child1);
			child2 = crossover(w.get(temp2), w.get(temp), crossoverPoint);
			child2 = mutate(child2);
			w.remove(temp);
			if(temp<temp2){
				w.remove(temp2-1);
			} else{
				w.remove(temp2);
			}
			newPop.add(child1);
			newPop.add(child2);
		}
		return newPop;
	}

	private String mutate(String child) {
		int mutation = random.nextInt(100);
		if(mutation < getMutation()){
			char c[] = child.toCharArray();
			int pos = random.nextInt(getCurrentPopulation().get(0).length());
			int mut= random.nextInt(2);
			c[pos] = Integer.toString(mut).charAt(0);

			return new String(c);
		}
		return child;
	}

	private String crossover(String parent1, String parent2, int crossoverPoint) {
		String parent1DNA1 = new String();
		String parent2DNA2 = new String();
		parent1DNA1 = parent1.substring(0, crossoverPoint);
		parent2DNA2 = parent2.substring(crossoverPoint, getCurrentPopulation().get(0).length());
		return parent1DNA1+parent2DNA2;
	}

	private ArrayList<String> getTournamentWinners() {
		int challenge;
		ArrayList<String> w = new ArrayList<String>();
		for (int i = 0; i<getPopulationSize();){
			challenge = random.nextInt(getPopulationSize());
			if(challenge != i){
				if(fitnessForPopulation.get(i) > fitnessForPopulation.get(challenge) ){
					w.add(getCurrentPopulation().get(challenge));
				} else {
					w.add(getCurrentPopulation().get(i));
				}
				i++;
			}
		}
		return w;
	}

	public ArrayList<String> getDnaDatabase() {
		return dnaDatabase;
	}

	public void setDnaDatabase(ArrayList<String> dnaDatabase) {
		this.dnaDatabase = dnaDatabase;
	}

	public int getGenerations() {
		return generations;
	}

	public void setGenerations(int generations) {
		this.generations = generations;
	}

	public ArrayList<String> getCurrentPopulation() {
		return currentPopulation;
	}

	public void setCurrentPopulation(ArrayList<String> currentPopulation) {
		this.currentPopulation = currentPopulation;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int population) {
		this.populationSize = population;
	}

	public int getMutation() {
		return mutation;
	}

	public void setMutation(int mutation) {
		this.mutation = mutation;
	}

	public ArrayList<Integer> getFitnessForPopulation() {
		return fitnessForPopulation;
	}

	public void setFitnessForPopulation(ArrayList<Integer> fitnessForPopulation) {
		this.fitnessForPopulation = fitnessForPopulation;
	}

	public int getBestFitnessScore() {
		return bestFitnessScore;
	}

	public void setBestFitnessScore(int bestFitnessScore) {
		this.bestFitnessScore = bestFitnessScore;
	}

	public String getBestFitnessString() {
		return bestFitnessString;
	}

	public void setBestFitnessString(String bestFitnessString) {
		this.bestFitnessString = bestFitnessString;
	}

	public int getDnaPieceMotifMaxLocation() {
		return dnaPieceMotifMaxLocation;
	}

	public void setDnaPieceMotifMaxLocation(int dnaPieceMotifMaxLocation) {
		this.dnaPieceMotifMaxLocation = dnaPieceMotifMaxLocation;
	}

	public int getDnaPieceMotifMaxLength() {
		return dnaPieceMotifMaxLength;
	}

	public void setDnaPieceMotifMaxLength(int dnaPieceMotifMaxLength) {
		this.dnaPieceMotifMaxLength = dnaPieceMotifMaxLength;
	}

	public int getMinimumPatternLength() {
		return minimumPatternLength;
	}

	public void setMinimumPatternLength(int minimumPatternLength) {
		this.minimumPatternLength = minimumPatternLength;
	}
}
