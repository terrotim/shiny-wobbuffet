package com.set.logic;

import java.util.ArrayList;

import com.set.card.Card;

public class CopyFinder {

	public boolean getCopyExists(ArrayList<Card> cardsinplay, Card card) {
		for(int i = 0; i<cardsinplay.size();i++){
			if(cardsinplay.get(i).getColor().equals(card.getColor())&&cardsinplay.get(i).getNumber().equals(card.getNumber())&&cardsinplay.get(i).getShade().equals(card.getShade())&&cardsinplay.get(i).getShape().equals(card.getShape())){
				return true;
			}
		}
		return false;
	}
}
