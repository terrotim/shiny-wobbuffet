package com.monkey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MonkeyProblem {
	private TileGenerator generator = new TileGenerator();
	private Random randomGenerator = new Random();
	private HashMap<Character, Integer> letterMap = new HashMap<Character, Integer>();
	private ArrayList<Tile> monkeyTiles;
	private ArrayList<String> population;
	private ArrayList<Integer> fitnessPopulation;
	private ArrayList<String> winners = new ArrayList<String>();
	private int genMax;
	private double genAvg;
	private int populationSize = 100;
	private int numTiles = 25;
	private String baseSeed = "abcdefghijklmnopqrstuvwxy";
	private String bestSolution = new String();
	private String bestGenSolution = new String();
	private int bestFitness=0;
	private SimulationView simulation;
	
	
	public MonkeyProblem(){
		simulation = new SimulationView();

		setMonkeyTiles(generator.initializeTiles());
		simulation.setMonkeyTiles(monkeyTiles);
		setPopulation(generateRandomPopulation(populationSize));
		setLetterMap();
		simulation.setLetterMap(letterMap);
		int gen = 0;
		while(true){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while(simulation.getPauseSimulation() == false || simulation.getProgressGen() > 0){
				gen++;
				fitnessPopulation = new ArrayList<Integer>();
				double tempTotal = 0;
				genMax = 0;
				for(int i = 0; i<populationSize; i++){
					int fitness = getFitness(population.get(i));
					if(fitness>bestFitness){
						bestFitness = fitness;
						setBestSolution(population.get(i));
						simulation.setBestSolution(population.get(i));
					}
					if(fitness>genMax){
						genMax = fitness;
					}
					fitnessPopulation.add(fitness);
					tempTotal = tempTotal+fitness;
				}
				genAvg = tempTotal/populationSize;
				winners = getTournamentWinners();
				setPopulation(getNextGen(winners));
				simulation.setTitle("Best Fitness: " + bestFitness + " " + simulation.getBestSolution() + " || Current Gen: " + gen + " || Current Gen Average Fitness: " + genAvg  + 
						" || Current Gen Best: " + genMax);
				if(simulation.getProgressGen() > 0){
					simulation.setProgressGen(simulation.getProgressGen()-1);
				}
			}
			simulation.setTitle("Best Fitness: " + bestFitness + " " + simulation.getBestSolution() + " || Current Gen: " + gen + " || Current Gen Average Fitness: " + genAvg  + 
					" || Current Gen Best: " + genMax   + " || Simulation Paused");
		}
		
	}


	private ArrayList<String> getNextGen(ArrayList<String> w) {
		ArrayList<String> newPop = new ArrayList<String>();
		String child1 = new String();
		String child2 = new String();
		int crossoverPoint1, crossoverPoint2;
		int temp, temp2;
		while(w.size() > 0){
			temp = randomGenerator.nextInt(w.size());
			temp2 = randomGenerator.nextInt(w.size());
			while(temp == temp2){
				temp2 = randomGenerator.nextInt(w.size());
			}
			crossoverPoint1 = randomGenerator.nextInt(numTiles);
			crossoverPoint2 = randomGenerator.nextInt(numTiles)+1;
			while(crossoverPoint1 == crossoverPoint2){
			crossoverPoint2 = randomGenerator.nextInt(numTiles);
			}
			if(crossoverPoint1 > crossoverPoint2){
				int tempInt = crossoverPoint2;
				crossoverPoint2 = crossoverPoint1;
				crossoverPoint1 = tempInt;
			}
			child1 = crossover(w.get(temp), w.get(temp2), crossoverPoint1, crossoverPoint2);
			child1 = chanceMutation(child1);
			child2 = crossover(w.get(temp2), w.get(temp), crossoverPoint1, crossoverPoint2);
			child2 = chanceMutation(child2);
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


	private String chanceMutation(String child) {
		int mutation = randomGenerator.nextInt(100);
		if(mutation < 5){
			char c[] = child.toCharArray();
			int rand1 = randomGenerator.nextInt(numTiles);
			int rand2 = randomGenerator.nextInt(numTiles);
			char temp = c[rand1];
			c[rand1] = c[rand2];
			c[rand2] = temp;
			return new String(c);
		}
		return child;
	}


	private String crossover(String string, String string2, int crossoverPoint1, int crossoverPoint2) {
		String substring = new String();
		StringBuilder newString = new StringBuilder("                         ");
		char c;
		substring = string.substring(crossoverPoint1, crossoverPoint2);
		for(int i = crossoverPoint1; i<crossoverPoint2; i++){
			newString.setCharAt(i, substring.charAt(i - crossoverPoint1));
		}
	
		if(crossoverPoint2 >= string.length()){
			crossoverPoint2 = 0;
		}
		int i = crossoverPoint2;
		int j = crossoverPoint2;
		do {
			c = string2.charAt(j);
			if(substring.indexOf(c) == -1){
				newString.setCharAt(i, c);
				i++;
			}
			j++;
			if(i>=string.length()){
				i = 0;
			}
			if(j>=string.length()){
				j = 0;
			}
		} while (j!=crossoverPoint2);
		return newString.toString();
	}


	private ArrayList<String> getTournamentWinners() {
		int challenge;
		ArrayList<String> w = new ArrayList<String>();
		for (int i = 0; i<population.size();){
			challenge = randomGenerator.nextInt(population.size());
			if(challenge != i){
				if(fitnessPopulation.get(i) < fitnessPopulation.get(challenge) ){
					w.add(population.get(challenge));
				} else {
					w.add(population.get(i));
				}
				i++;
			}
		}
		return w;
	}


	private Integer getFitness(String string) {
		int fitness = 0;
		Tile[][] tileLayout = simulation.getTileLayout(string);
		for(int i = 0; i<numTiles/5; i++){
			for(int j = 0; j<numTiles/5; j++){
				if(i+1<numTiles/5){
					if((tileLayout[i][j].getSouthPattern().getColor().equals(tileLayout[i+1][j].getNorthPattern().getColor()))&&
					(tileLayout[i][j].getSouthPattern().getOrientation()!=(tileLayout[i+1][j].getNorthPattern().getOrientation()))){
						fitness++;
					}
				}
				if(j+1<numTiles/5){
					if((tileLayout[i][j].getEastPattern().getColor().equals(tileLayout[i][j+1].getWestPattern().getColor()))&&
					(tileLayout[i][j].getEastPattern().getOrientation()!=(tileLayout[i][j+1].getWestPattern().getOrientation()))){
						fitness++;
					}
				}
			}
		}
		return fitness;
	}

	private ArrayList<String> generateRandomPopulation(int p) {
		ArrayList<String> pop = new ArrayList<String>();
		char c[];
		for(int i = 0; i<p; i++){
			c = baseSeed.toCharArray();
			for(int j = 0; j<numTiles; j++){
				int rand = randomGenerator.nextInt(numTiles);
				char temp = c[j];
				c[j] = c[rand];
				c[rand] = temp;
			}
			pop.add(new String(c));
		}
		return pop;
	}


	public ArrayList<Tile> getMonkeyTiles() {
		return monkeyTiles;
	}


	public void setMonkeyTiles(ArrayList<Tile> monkeyTiles) {
		this.monkeyTiles = monkeyTiles;
	}


	public ArrayList<String> getPopulation() {
		return population;
	}


	public void setPopulation(ArrayList<String> population) {
		this.population = population;
	}

	public void setLetterMap() {
		letterMap.put('a', 0);
		letterMap.put('b', 1);
		letterMap.put('c', 2);
		letterMap.put('d', 3);
		letterMap.put('e', 4);
		letterMap.put('f', 5);
		letterMap.put('g', 6);
		letterMap.put('h', 7);
		letterMap.put('i', 8);
		letterMap.put('j', 9);
		letterMap.put('k', 10);
		letterMap.put('l', 11);
		letterMap.put('m', 12);
		letterMap.put('n', 13);
		letterMap.put('o', 14);
		letterMap.put('p', 15);
		letterMap.put('q', 16);
		letterMap.put('r', 17);
		letterMap.put('s', 18);
		letterMap.put('t', 19);
		letterMap.put('u', 20);
		letterMap.put('v', 21);
		letterMap.put('w', 22);
		letterMap.put('x', 23);
		letterMap.put('y', 24);
	}


	public String getBestSolution() {
		return bestSolution;
	}


	public void setBestSolution(String bestSolution) {
		this.bestSolution = bestSolution;
	}


	public String getBestGenSolution() {
		return bestGenSolution;
	}


	public void setBestGenSolution(String bestGenSolution) {
		this.bestGenSolution = bestGenSolution;
	}

}
