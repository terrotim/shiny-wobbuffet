package com.monkey;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class TileDrawer extends JPanel{

	private static final long serialVersionUID = 1L;
	private List<Rectangle> tilesDrawn = new ArrayList<Rectangle>();
	private Tile[][] tiles = new Tile[5][5];

	public void drawTiles(Tile[][] t) {
		tiles = t;
		tilesDrawn.clear();
		for (int i = 0; i<tiles.length; i++){
			for (int j = 0; j < tiles[i].length; j++){
				drawTile(i, j, tiles[i][j]);
			}
		}
		repaint();
	}

	private void drawTile(int i, int j, Tile tile) {
		// TODO Auto-generated method stub
		Rectangle rect = new Rectangle(175*i+50,j*175+25,175, 175);
		tilesDrawn.add(rect);
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		for (Rectangle rect : tilesDrawn) {
			g2.draw(rect);
		}
		for(int i = 0; i<5; i++){
			for(int j = 0; j< 5; j++){
				drawPatterns(getTiles()[i][j], g, i, j);
			}
		}
		
	}

	private void drawPatterns(Tile tile, Graphics g, int i, int j) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(getPatternColor(tile.getNorthPattern().getColor()));
		drawHeadOrBody(tile.getNorthPattern().getOrientation(), 0, g, i, j);

		g2.setColor(getPatternColor(tile.getEastPattern().getColor()));
		drawHeadOrBody(tile.getEastPattern().getOrientation(), 1, g, i, j);
		
		g2.setColor(getPatternColor(tile.getSouthPattern().getColor()));
		drawHeadOrBody(tile.getSouthPattern().getOrientation(), 2, g, i, j);
		
		g2.setColor(getPatternColor(tile.getWestPattern().getColor()));
		drawHeadOrBody(tile.getWestPattern().getOrientation(), 3, g, i, j);
		
	}

	private void drawHeadOrBody(int orientation, int location, Graphics g, int i, int j) {
		if(orientation == 0){
			if(location == 0){
				g.fillOval(175*j+112,i*175+25, 50, 50);
			} else if (location == 1){
				g.fillOval(175*j+175,i*175+87, 50, 50);
			} else if (location == 2){
				g.fillOval(175*j+112,i*175+150, 50, 50);
			} else {
				g.fillOval(175*j+50,i*175+87, 50, 50);
			}
		} else {
			if(location == 0){
				g.fillRect(175*j+112,i*175+25, 50, 50);
			} else if (location == 1){
				g.fillRect(175*j+175,i*175+87, 50, 50);
			} else if (location == 2){
				g.fillRect(175*j+112,i*175+150, 50, 50);
			} else {
				g.fillRect(175*j+50,i*175+87, 50, 50);
			}
		}
	}

	private Color getPatternColor(String color) {
		if(color.equals("red")){
			return Color.RED;
		} else if (color.equals("blue")){
			return Color.BLUE;
		} else if (color.equals("yellow")){
			return Color.YELLOW;
		} else if (color.equals("green")){
			return Color.GREEN;
		} else if (color.equals("black")){
			return Color.BLACK;
		}
		return null;
	}
}
