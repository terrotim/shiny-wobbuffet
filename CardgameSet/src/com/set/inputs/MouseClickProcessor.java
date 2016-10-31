package com.set.inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;

public class MouseClickProcessor extends Observable implements MouseListener{
	
	private int Xmouse, Ymouse, cardnum;
	
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	private boolean determineCardIsClicked(int xmouse, int ymouse) {
		if((xmouse>=20 && xmouse<=170) || (xmouse>=190 && xmouse<=340) || (xmouse>=360 && xmouse<=510) || (xmouse>=530 && xmouse<=680)){
			if((ymouse>=20 && ymouse<=220) || (ymouse>=240 && ymouse<=440) || (ymouse>=460 && ymouse<=660)){
				return true;
			}
		}
		return false;
	}

	private int determineCardRow(int ymouse) {
		if(ymouse>=20 && ymouse<=220){
			return 0;
		}
		else if(ymouse>=240 && ymouse<=440){
			return 1;
		}
		else if(ymouse>=460 && ymouse<=660){
			return 2;
		}
		return 0;
	}

	private int determineCardColumn(int xmouse) {
		if(xmouse>=20 && xmouse<=170){
			return 0;
		}
		else if(xmouse>=190 && xmouse<=340){
			return 1;
		}
		else if(xmouse>=360 && xmouse<=510){
			return 2;
		}
		else if(xmouse>=530 && xmouse<680){
			return 3;
		}
		return 0;
	}

	public int determineCardNum(int xmouse, int ymouse){
		return (ymouse*4)+xmouse;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		setXmouse(e.getX()-4);
		setYmouse(e.getY()-30);
		if(determineCardIsClicked(getXmouse(), getYmouse())==true){
			cardnum = determineCardNum(determineCardColumn(Xmouse), determineCardRow(Ymouse));
			setChanged();
			notifyObservers(cardnum);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	public int getXmouse() {
		return Xmouse;
	}

	public void setXmouse(int xmouse) {
		Xmouse = xmouse;
	}

	public int getYmouse() {
		return Ymouse;
	}

	public void setYmouse(int ymouse) {
		Ymouse = ymouse;
	}

	public int getCardnum() {
		return cardnum;
	}

	public void setCardnum(int cardnum) {
		this.cardnum = cardnum;
	}
}
