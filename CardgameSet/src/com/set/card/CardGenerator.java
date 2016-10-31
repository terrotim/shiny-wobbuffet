package com.set.card;

import java.util.HashMap;
import java.util.Random;


public class CardGenerator {
	
	private int randnumcolor;
	private int randnumber;
	private int randnumshade;
	private int randnumshape;
	private HashMap<Integer, String> colormap = new HashMap<Integer, String>();
	private HashMap<Integer, String> shademap = new HashMap<Integer, String>();
	private HashMap<Integer, String> shapemap = new HashMap<Integer, String>();
	private Random randomGenerator = new Random();
	
	public CardGenerator(){
		colormap.put(0, "red");
		colormap.put(1, "green");
		colormap.put(2, "purple");
		shademap.put(0, "solid");
		shademap.put(1, "striped");
		shademap.put(2, "clear");
		shapemap.put(0, "oval");
		shapemap.put(1, "diamond");
		shapemap.put(2, "wave");
	}
	
	public Card generateCard(){
		randnumcolor = randomGenerator.nextInt(3);
		randnumber = randomGenerator.nextInt(3)+1;
		randnumshade = randomGenerator.nextInt(3);
		randnumshape= randomGenerator.nextInt(3);
		return new Card(colormap.get(randnumcolor), Integer.toString(randnumber), shademap.get(randnumshade),shapemap.get(randnumshape));
	}
}
