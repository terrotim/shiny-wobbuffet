package com.monkey;

public class Tile {
	private MonkeyPattern northPattern;
	private MonkeyPattern westPattern;
	private MonkeyPattern eastPattern;
	private MonkeyPattern southPattern;
	
	public Tile(MonkeyPattern north, MonkeyPattern west, MonkeyPattern south, MonkeyPattern east){
		northPattern = north;
		westPattern = west;
		southPattern = south;
		eastPattern = east;
	}
	
	public MonkeyPattern getNorthPattern() {
		return northPattern;
	}
	public void setNorthPattern(MonkeyPattern northPattern) {
		this.northPattern = northPattern;
	}
	public MonkeyPattern getWestPattern() {
		return westPattern;
	}
	public void setWestPattern(MonkeyPattern westPattern) {
		this.westPattern = westPattern;
	}
	public MonkeyPattern getEastPattern() {
		return eastPattern;
	}
	public void setEastPattern(MonkeyPattern eastPattern) {
		this.eastPattern = eastPattern;
	}
	public MonkeyPattern getSouthPattern() {
		return southPattern;
	}
	public void setSouthPattern(MonkeyPattern southPattern) {
		this.southPattern = southPattern;
	}
}
