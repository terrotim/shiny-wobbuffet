package com.set.card;

import java.util.ArrayList;


public class CardSet {
	
	private ArrayList<Card> cards = new ArrayList<Card>();
	private int cardsinset;
	private boolean isSet;

	public CardSet(){
	
	}
	
	public int getCardsInSet() {
		return cardsinset;
	}
	
	public void setCardsInSet(int cardsinset) {
		this.cardsinset = cardsinset;
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}

	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}

	public boolean getIsSet() {
		return isSet;
	}

	public void setIsSet(boolean isSet) {
		this.isSet = isSet;
	}

	public void addCardtoSet(Card card){
		cards.add(card);
	}
	
	public void removeAllfromSet(){
		cards.clear();
	}
}
