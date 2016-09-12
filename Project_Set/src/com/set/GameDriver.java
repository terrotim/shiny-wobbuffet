package com.set;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import com.set.card.Card;
import com.set.card.CardGenerator;
import com.set.card.CardSet;
import com.set.inputs.KeyPressProcessor;
import com.set.inputs.MouseClickProcessor;
import com.set.logic.CopyFinder;
import com.set.logic.Validator;
import com.set.view.PlayerView;

public class GameDriver implements Observer, ActionListener{
	private ArrayList<Card> cardsinplay; //The 12 cards in play
	private ArrayList<CardSet>  validsets; //The list of all valid sets
	private CardSet cardsinset_debug; //The 3 cards chosen when determining valid sets
	private CardSet cardsinset; //The 3 cards chosen by player

	private KeyPressProcessor keypressprocessor = new KeyPressProcessor();
	private MouseClickProcessor mouseclickprocessor = new MouseClickProcessor();
	private Timer timer;
	
	private PlayerView playerview;
	private Validator validator = new Validator();
	private ArrayList<Integer> cardposition;
	private CopyFinder finder = new CopyFinder();
	private CardGenerator generator = new CardGenerator();
	private int restart;
	private int score;
	private int seconds;
	private int NUM_CARDS_IN_PLAY;
	
	public GameDriver(){
		NUM_CARDS_IN_PLAY = 12;
		seconds = 10000;
		score = 0;
		playerview = new PlayerView(seconds);
		keypressprocessor.addObserver(this);
		mouseclickprocessor.addObserver(this);
		playerview.addKeyListener(keypressprocessor);
		playerview.addMouseListener(mouseclickprocessor);
		setup();
	    timer = new Timer(1,this);
		timer.start();
	}
	
	private void setup(){
		cardsinplay = new ArrayList<Card>();
		cardsinset = new CardSet();
		cardposition = new ArrayList<Integer>();
		for(int i = 0; i < NUM_CARDS_IN_PLAY; i++)
		{
			Card card = generator.generateCard();
			while(finder.getCopyExists(cardsinplay, card) == true){
				card = generator.generateCard();
			}
			cardsinplay.add(card);
		}	
		playerview.displayCards(cardsinplay);
		validsets = determineValidSets(cardsinplay);
		if(validsets.size()==0){
			setup();
		}
		else{
			playerview.setTitle(playerview.makeTitle(validsets.size(), score, getSeconds()));
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		cardsinplay.get((Integer) arg).setIsSelected(!cardsinplay.get((Integer)arg).getIsSelected());
		Card card = cardsinplay.get((Integer) arg);
		if(!cardsinset.getCards().contains(card)){
			cardposition.add((Integer) arg);
			cardsinset.addCardtoSet(cardsinplay.get((Integer) arg));
		}
		else{
			cardsinset.getCards().remove(card);
			cardposition.remove((Integer) arg);
		}
		playerview.displayCards(cardsinplay);
		if(cardsinset.getCards().size() == 3){
			if(validator.validate(cardsinset) == true){
				score = score + 30000 + getSeconds();
				setSeconds(getSeconds()+(1200/validsets.size()));
				for(int i=0;i<3;i++){
					card = generator.generateCard();
					while(finder.getCopyExists(cardsinplay, card) == true){
						card = generator.generateCard();
					}
					cardsinplay.set(cardposition.get(i), card);	
				}
				validsets = determineValidSets(cardsinplay);
				if(validsets.size()==0){
					setup();
				}
				else{
					playerview.setTitle(playerview.makeTitle(validsets.size(), score, getSeconds()));
				}
			}
			else{
				for(int i=0;i<cardsinplay.size();i++){
					cardsinplay.get(i).setIsSelected(false);
				}
				score = score - 10000;
				playerview.makeTitle(validsets.size(), score, getSeconds());
			}
			cardsinset = new CardSet();
			cardposition = new ArrayList<Integer>();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		seconds--;
		setSeconds(seconds);
		playerview.setTitle(playerview.makeTitle(validsets.size(), score, getSeconds()));
		if (seconds <=0){
			timer.stop();
			restart = JOptionPane.showConfirmDialog(playerview, "Final Score: " + score + "\nPlay again?", "Game Over", JOptionPane.YES_NO_OPTION);
			if(restart == JOptionPane.YES_OPTION){
				seconds = 10000;
				score = 0;
				setup();
				timer.start();
			}
			else{
			playerview.setEnabled(false);
			System.exit(0);
			}
		}
	}
	
	public ArrayList<CardSet> determineValidSets(ArrayList<Card> cardsinplay){
		validsets = new ArrayList<CardSet>();
		for(int i = 0; i < cardsinplay.size()-2; i++)
		{
			for(int j = i+1; j<cardsinplay.size()-1;j++)
			{
				for(int k = j+1; k<cardsinplay.size();k++)
				{
					cardsinset_debug = new CardSet();
					cardsinset_debug.addCardtoSet(cardsinplay.get(i));
					cardsinset_debug.addCardtoSet(cardsinplay.get(j));
					cardsinset_debug.addCardtoSet(cardsinplay.get(k));
					cardsinset_debug.setIsSet(validator.validate(cardsinset_debug));
					if(cardsinset_debug.getIsSet() == true){
						validsets.add(cardsinset_debug);
					}
				}
			}
		}
		return validsets;
	}

	public ArrayList<Card> getCardsinplay() {
		return cardsinplay;
	}

	public void setCardsinplay(ArrayList<Card> cardsinplay) {
		this.cardsinplay = cardsinplay;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
}
