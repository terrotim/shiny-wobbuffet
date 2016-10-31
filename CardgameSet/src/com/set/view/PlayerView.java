package com.set.view;

import java.util.ArrayList;
import javax.swing.JFrame;

import com.set.card.Card;
import com.set.card.CardDrawer;


public class PlayerView extends JFrame{
	
	private static final long serialVersionUID = 5397118599706231775L;
	
	private CardDrawer carddrawer;
	
	public PlayerView(int start){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(720, 720);
		setResizable(false);
		carddrawer = new CardDrawer();
	    getContentPane().add(carddrawer);
		setVisible(true);
	}
	
	public void displayCards(ArrayList<Card> cardsinplay){
		carddrawer.drawCard(cardsinplay);
	}

	public String makeTitle(int validsets, int score, int seconds) {
		return Integer.toString(validsets) + " possible sets || Score: " + score + " || Time: " + seconds;
	}
}
