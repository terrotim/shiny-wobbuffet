package com.set.card;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;


public class CardDrawer extends JPanel{
	
	private static final long serialVersionUID = 7350565892827257878L;
	private static final int PREF_W = 150;
	private static final int PREF_H = 200;
	
	private List<Rectangle> cards = new ArrayList<Rectangle>();
	private ArrayList<Card> cardsinplay = new ArrayList<Card>();
	
	public void drawCard(ArrayList<Card> cardsinplay) {
		cards.clear();
		this.cardsinplay = cardsinplay;
		for (int i = 0; i<cardsinplay.size();i++){
			drawRectangle(i);
		}
		repaint();
	}
	
	private void drawRectangle(int i){
		int x = 20 + 170*(i%4);
		int y = 20 + 220*(i/4);
		Rectangle rect = new Rectangle(x, y, 150, 200);
	    cards.add(rect);
	}

	@Override
	   public Dimension getPreferredSize() {
	      return new Dimension(PREF_W, PREF_H);
	   }

	@Override
	   protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	      Graphics2D g2 = (Graphics2D) g;
	      int i=0;
	      for (Rectangle rect : cards) {
	    	 boolean isSelected = cardsinplay.get(i).getIsSelected();
	    	 if(isSelected == true){
	    		 g2.setColor(Color.MAGENTA);
	    	 }
	    	 else{
	    		 g2.setColor(Color.BLACK);
	    	 }
	         g2.draw(rect);
	         drawDesign(g, cardsinplay.get(i), rect);
	         i++;
	      }
	   }
	
	private void drawDesign(Graphics g, Card card, Rectangle rect){
		int num = getShapeNumber(card);
        for(int i=0;i<num;i++){
        	g.setColor(getShapeColor(card));
	        if(num==3){
	        	drawShape(g, card,(int) (rect.getMinX())+35, (int) (rect.getMinY()+10+(65*i)), 80, 50);
	        }
	        else if(num==2){
	        	drawShape(g, card,(int) (rect.getMinX())+35, (int) (rect.getMinY()+40+(70*i)), 80, 50);
	        }
	        else{
	        	drawShape(g, card,(int) (rect.getMinX())+35, (int) (rect.getMinY()+75), 80, 50);
	        }
        }
	}
		
	private Color getShapeColor(Card card){
		float alpha = getShapeShade(card);
		if(card.getColor().equals("red")){
			return new Color(1,0,0,alpha);
		}
		else if(card.getColor().equals("green")){
			return new Color(0,1,0,alpha);
		}
		else if(card.getColor().equals("purple")){
			return new Color(0.5f,0,1,alpha);
		}
		return null;
	}
	
	private int getShapeNumber(Card card){
		return Integer.parseInt(card.getNumber());
	}
	
	private void drawShape(Graphics g, Card card, int x, int y, int width, int height){
		Graphics2D g2 = (Graphics2D) g;
		if(card.getShape().equals("oval")){
			g.fillOval(x,y,width,height);
			g2.setColor(new Color(g.getColor().getRed(), g.getColor().getGreen(), g.getColor().getBlue()));
			g.drawOval(x, y, width, height);
		}
		else if(card.getShape().equals("diamond")){
			int[] xPoints = {x, (x+width/2), x+width, (x+width/2)};
			int[] yPoints = {(y+height/2), y, (y+height/2), y+height};
			g.fillPolygon(xPoints, yPoints, 4);
			g2.setColor(new Color(g.getColor().getRed(), g.getColor().getGreen(), g.getColor().getBlue()));
			g.drawPolygon(xPoints, yPoints, 4);
		}
		else if(card.getShape().equals("wave")){
			g.fillRect(x, y, width, height);
			g2.setColor(new Color(g.getColor().getRed(), g.getColor().getGreen(), g.getColor().getBlue()));
			g.drawRect(x, y, width, height);
		}

	}
	
	private float getShapeShade(Card card){
		if(card.getShade().equals("solid")){
	        return 1;
		}
		else if(card.getShade().equals("striped")){
			return .2f;
		}
		else if(card.getShade().equals("clear")){
			return 0;
		}
		return 0;
	}
}
