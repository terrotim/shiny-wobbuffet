package com.monkey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TileGenerator {

	private int randomColor, randomOrientation;
	private HashMap<Integer, String> colorMap = new HashMap<Integer, String>();
	private Random randomGenerator = new Random();
	
	public TileGenerator(){
		colorMap.put(0, "red");
		colorMap.put(1, "blue");
		colorMap.put(2, "yellow");
		colorMap.put(3, "green");
		colorMap.put(4, "black");
	}
	
	public ArrayList<Tile> initializeTiles() {
		int redTop=10, redBottom=10; 
		int blueTop=10, blueBottom=10; 
		int yellowTop=10, yellowBottom=10;
		int greenTop=10, greenBottom=10;
		int blackTop=10, blackBottom=10;
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		ArrayList<MonkeyPattern> patterns = new ArrayList<MonkeyPattern>();
		
		for(int i = 0; i < 25; i++){
			for(int j = 0; j<4;){
				randomColor = randomGenerator.nextInt(5);
				randomOrientation = randomGenerator.nextInt(2);
				if(randomColor == 0 && randomOrientation == 0){
					if(redTop>0){
						redTop--;
						patterns.add(new MonkeyPattern(randomOrientation,colorMap.get(randomColor)));
						j++;
					} 
				} else if(randomColor == 0 && randomOrientation == 1){
					if(redBottom>0){
						redBottom--;
						patterns.add(new MonkeyPattern(randomOrientation,colorMap.get(randomColor)));
						j++;
					} 
				} else if(randomColor == 1 && randomOrientation == 0){
					if(blueTop>0){
						blueTop--;
						patterns.add(new MonkeyPattern(randomOrientation,colorMap.get(randomColor)));
						j++;
					} 
				} else if(randomColor == 1 && randomOrientation == 1){
					if(blueBottom>0){
						blueBottom--;
						patterns.add(new MonkeyPattern(randomOrientation,colorMap.get(randomColor)));
						j++;
					} 
				} else if(randomColor == 2 && randomOrientation == 0){
					if(yellowTop>0){
						yellowTop--;
						patterns.add(new MonkeyPattern(randomOrientation,colorMap.get(randomColor)));
						j++;
					}
				} else if(randomColor == 2 && randomOrientation == 1){
					if(yellowBottom>0){
						yellowBottom--;
						patterns.add(new MonkeyPattern(randomOrientation,colorMap.get(randomColor)));
						j++;
					} 
				} else if(randomColor == 3 && randomOrientation == 0){
					if(greenTop>0){
						greenTop--;
						patterns.add(new MonkeyPattern(randomOrientation,colorMap.get(randomColor)));
						j++;
					} 
				} else if(randomColor == 3 && randomOrientation == 1){
					if(greenBottom>0){
						greenBottom--;
						patterns.add(new MonkeyPattern(randomOrientation,colorMap.get(randomColor)));
						j++;
					} 
				} else if(randomColor == 4 && randomOrientation == 0){
					if(blackTop>0){
						blackTop--;
						patterns.add(new MonkeyPattern(randomOrientation,colorMap.get(randomColor)));
						j++;
					} 
				} else if(randomColor == 4 && randomOrientation == 1){
					if(blackBottom>0){
						blackBottom--;
						patterns.add(new MonkeyPattern(randomOrientation,colorMap.get(randomColor)));
						j++;
					} 
				} 
			}
			tiles.add(new Tile(patterns.get(0), patterns.get(1), patterns.get(2), patterns.get(3)));
			patterns.clear();
		}
		return tiles;
	}
	
	
}
