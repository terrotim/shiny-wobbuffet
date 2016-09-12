package com.set.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.set.card.CardSet;

public class Validator {
	
	private int NUM_CARDS_IN_SET = 3;
	private ArrayList<String> colors;
	private ArrayList<String> numbers;
	private ArrayList<String> shades;
	private ArrayList<String> shapes;
	private ArrayList<Integer> uniquecount;

	public Validator(){
		
	}
	
	public boolean validate(CardSet set){
		colors = new ArrayList<String>();
		numbers = new ArrayList<String>();
		shades = new ArrayList<String>();
		shapes = new ArrayList<String>();
		uniquecount = new ArrayList<Integer>();
		set.setCardsInSet(NUM_CARDS_IN_SET);
		for(int i = 0; i<set.getCardsInSet(); i++){
			colors.add(set.getCards().get(i).getColor());
			numbers.add(set.getCards().get(i).getNumber());
			shades.add(set.getCards().get(i).getShade());
			shapes.add(set.getCards().get(i).getShape());
		}
		Set<String> uniquecolors = new HashSet<String>(colors);
		for (String key : uniquecolors) {
			uniquecount.add(Collections.frequency(colors, key));
		}
		Set<String> uniquenumbers = new HashSet<String>(numbers);
		for (String key : uniquenumbers) {
			uniquecount.add(Collections.frequency(numbers, key));
		}
		Set<String> uniqueshades = new HashSet<String>(shades);
		for (String key : uniqueshades) {
			uniquecount.add(Collections.frequency(shades, key));
		}
		Set<String> uniqueshapes = new HashSet<String>(shapes);
		for (String key : uniqueshapes) {
			uniquecount.add(Collections.frequency(shapes, key));
		}
		if(uniquecount.contains(2)){
			return false;
		}
		else{
			return true;
		}
	}

	public ArrayList<String> getColors() {
		return colors;
	}

	public void setColors(ArrayList<String> colors) {
		this.colors = colors;
	}

	public ArrayList<String> getNumbers() {
		return numbers;
	}

	public void setNumbers(ArrayList<String> numbers) {
		this.numbers = numbers;
	}

	public ArrayList<String> getShades() {
		return shades;
	}

	public void setShades(ArrayList<String> shades) {
		this.shades = shades;
	}

	public ArrayList<String> getShapes() {
		return shapes;
	}

	public void setShapes(ArrayList<String> shapes) {
		this.shapes = shapes;
	}

	public ArrayList<Integer> getUniquecount() {
		return uniquecount;
	}

	public void setUniquecount(ArrayList<Integer> uniquecount) {
		this.uniquecount = uniquecount;
	}
}
