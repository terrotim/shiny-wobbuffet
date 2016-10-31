package com.set.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.set.GameDriver;
import com.set.card.Card;
import com.set.card.CardSet;

public class GameDriverTest {

	private ArrayList<Card> cards_in_play;
	private ArrayList<CardSet> sets;
	private GameDriver gamedriver;
	
	@Before
	public void setup(){
		gamedriver = new GameDriver();
		cards_in_play = new ArrayList<Card>(); //The 12 cards in play
		sets = new ArrayList<CardSet>(); //The list of possible sets
	}

	@Test
	public void testGameDriverSetup(){
		assertTrue(gamedriver.getCardsinplay().size() == 12);
	}
	
	@Test
	public void testDetermineValidSets1() {
		cards_in_play.add(new Card("red", "2", "clear", "diamond"));
		cards_in_play.add(new Card("green", "1", "solid", "oval"));
		cards_in_play.add(new Card("red", "1", "solid", "wave"));
		cards_in_play.add(new Card("purple", "1", "clear", "oval"));
		cards_in_play.add(new Card("red", "3", "striped", "oval"));
		cards_in_play.add(new Card("red", "2", "striped", "oval"));
		cards_in_play.add(new Card("purple", "2", "solid", "diamond"));
		cards_in_play.add(new Card("green", "2", "striped", "diamond"));
		cards_in_play.add(new Card("red", "3", "clear", "diamond"));
		cards_in_play.add(new Card("green", "2", "solid", "oval"));
		cards_in_play.add(new Card("red", "2", "solid", "wave"));
		cards_in_play.add(new Card("green", "3", "striped", "wave"));
		sets = gamedriver.determineValidSets(cards_in_play);
		assertTrue(sets.size() == 6);
	}

}
