package com.monkey;

public class MonkeyPattern {
	private int orientation;
	private String color;
	
	public MonkeyPattern(int o, String c){
		orientation = o;
		color = c;
	}
	public int getOrientation() {
		return orientation;
	}
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
