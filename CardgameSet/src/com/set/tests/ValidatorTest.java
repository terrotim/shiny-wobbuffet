package com.set.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.set.card.Card;
import com.set.card.CardSet;
import com.set.logic.Validator;

public class ValidatorTest {
	
	private CardSet cardset;
	private Validator validator;

	@Before
	public void setUp(){
		cardset = new CardSet();
		validator = new Validator();
	}
	
	@Test
	public void testValidateTrue1() {
		cardset.addCardtoSet(new Card("red", "1", "solid", "oval"));
		cardset.addCardtoSet(new Card("red", "1", "solid", "diamond"));
		cardset.addCardtoSet(new Card("red", "1", "solid", "wave"));
		cardset.setIsSet(validator.validate(cardset));
		assertTrue(cardset.getIsSet() == true);
	}
	
	@Test
	public void testValidateFalse1() {
		cardset.addCardtoSet(new Card("red", "1", "solid", "oval"));
		cardset.addCardtoSet(new Card("green", "1", "solid", "diamond"));
		cardset.addCardtoSet(new Card("red", "1", "solid", "wave"));
		cardset.setIsSet(validator.validate(cardset));
		assertTrue(cardset.getIsSet() == false);
	}
	
	@Test
	public void testValidateTrue2() {
		cardset.addCardtoSet(new Card("red", "1", "solid", "oval"));
		cardset.addCardtoSet(new Card("green", "2", "striped", "diamond"));
		cardset.addCardtoSet(new Card("purple", "3", "clear", "wave"));
		cardset.setIsSet(validator.validate(cardset));
		assertTrue(cardset.getIsSet() == true);
	}
	
	@Test
	public void testValidateFalse2() {
		cardset.addCardtoSet(new Card("red", "1", "solid", "oval"));
		cardset.addCardtoSet(new Card("green", "2", "striped", "diamond"));
		cardset.addCardtoSet(new Card("purple", "3", "clear", "diamond"));
		cardset.setIsSet(validator.validate(cardset));
		assertTrue(cardset.getIsSet() == false);
	}
}
