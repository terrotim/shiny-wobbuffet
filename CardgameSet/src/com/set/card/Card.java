package com.set.card;

public class Card {
	
	private String color;
	private String number;
	private String shade;
	private String shape;
	private boolean isSelected;
	
	public Card(){
		setIsSelected(false);
	}
	
	public Card(String color, String number, String shade, String shape){
		this.color = color;
		this.number = number;
		this.shade = shade;
		this.shape = shape;
		setIsSelected(false);
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getShade() {
		return shade;
	}
	
	public void setShade(String shade) {
		this.shade = shade;
	}
	
	public String getShape() {
		return shape;
	}
	
	public void setShape(String shape) {
		this.shape = shape;
	}
	
	public boolean getIsSelected() {
		return isSelected;
	}
	
	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
