package com.set.inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

public class KeyPressProcessor extends Observable implements KeyListener{

	private int cardnum;
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Q) {
        	setCardnum(0);
            setChanged();
            notifyObservers(getCardnum());
        }
        else if (e.getKeyCode() == KeyEvent.VK_W) {
        	setCardnum(1);
            setChanged();
            notifyObservers(getCardnum());
        }
        else if (e.getKeyCode() == KeyEvent.VK_E) {
        	setCardnum(2);
            setChanged();
            notifyObservers(getCardnum());
        }
        else if (e.getKeyCode() == KeyEvent.VK_R) {
        	setCardnum(3);
            setChanged();
            notifyObservers(getCardnum());
        }
        else if (e.getKeyCode() == KeyEvent.VK_A) {
        	setCardnum(4);
            setChanged();
            notifyObservers(getCardnum());
        }
        else if (e.getKeyCode() == KeyEvent.VK_S) {
        	setCardnum(5);
            setChanged();
            notifyObservers(getCardnum());
        }
        else if (e.getKeyCode() == KeyEvent.VK_D) {
        	setCardnum(6);
            setChanged();
            notifyObservers(getCardnum());
        }
        else if (e.getKeyCode() == KeyEvent.VK_F) {
        	setCardnum(7);
            setChanged();
            notifyObservers(getCardnum());
        }
        else if (e.getKeyCode() == KeyEvent.VK_Z) {
        	setCardnum(8);
            setChanged();
            notifyObservers(getCardnum());
        }
        else if (e.getKeyCode() == KeyEvent.VK_X) {
        	setCardnum(9);
            setChanged();
            notifyObservers(getCardnum());
        }
        else if (e.getKeyCode() == KeyEvent.VK_C) {
        	setCardnum(10);
            setChanged();
            notifyObservers(getCardnum());
        }
        else if (e.getKeyCode() == KeyEvent.VK_V) {
        	setCardnum(11);
            setChanged();
            notifyObservers(getCardnum());
        }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	public int getCardnum() {
		return cardnum;
	}

	public void setCardnum(int cardnum) {
		this.cardnum = cardnum;
	}
}
