package com.monkey;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SimulationView extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private TileDrawer tiledrawer;
	private JButton showDraw;
	private JButton pauseSimul;
	private JButton oneGen;
	private JPanel buttonpnl;
	private boolean pauseSimulation;
	private String bestSolution;
	private HashMap<Character, Integer> letterMap = new HashMap<Character, Integer>();
	private ArrayList<Tile> monkeyTiles = new ArrayList<Tile>();
	private int progressGen = 0;

	public SimulationView(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000,1000);
		setResizable(false);
		buttonpnl = new JPanel();
		tiledrawer = new TileDrawer();
		showDraw = new JButton("Show Best");
		showDraw.setBounds(50, 15, 100, 50);
		showDraw.setActionCommand("Show");
		showDraw.addActionListener(this);
		pauseSimul = new JButton("Pause Simulation");
		pauseSimul.setBounds(175, 15, 150, 50);
		pauseSimul.setActionCommand("Pause");
		pauseSimul.addActionListener(this);
		oneGen = new JButton("Progress 1 Generation");
		oneGen.setBounds(225,15,175,50);
		oneGen.setActionCommand("Move one");
		oneGen.addActionListener(this);
		buttonpnl.add(showDraw);
		buttonpnl.add(pauseSimul);
		buttonpnl.add(oneGen);
		buttonpnl.setBounds(0, 0, WIDTH, 75);
		getContentPane().add(buttonpnl, BorderLayout.PAGE_START);
		getContentPane().add(tiledrawer);
		setVisible(true);
		pauseSimulation = true;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand().equals("Show")){
				displayTiles(getTileLayout(bestSolution));
		} else if (event.getActionCommand().equals("Pause")){
			if(pauseSimulation == true){
				setPauseSimulation(false);
			} else {
				setPauseSimulation(true);
			}
		} else if (event.getActionCommand().equals("Move one")){
			if(pauseSimulation == true){
				setProgressGen(1);
			}
		}
	}

	public Tile[][] getTileLayout(String string) {
		Tile[][] tempLayout = new Tile[5][5];
		for (int i = 0; i<string.length(); i++){
			int currentTileNum=letterMap.get(string.charAt(i));
			tempLayout[i/5][i%5] = monkeyTiles.get(currentTileNum);
		}
		return tempLayout;
	}

	public boolean getPauseSimulation() {
		return pauseSimulation;
	}

	public void setPauseSimulation(boolean pauseSimulation) {
		this.pauseSimulation = pauseSimulation;
	}

	public void displayTiles(Tile[][] tiles) {
		tiledrawer.drawTiles(tiles);
	}

	public void setBestSolution(String string) {
		bestSolution = string;
	}
	

	public String getBestSolution() {
		return bestSolution;
	}

	public void setLetterMap(HashMap<Character, Integer> lm) {
		letterMap = lm;
	}

	public void setMonkeyTiles(ArrayList<Tile> mt) {
		monkeyTiles = mt;
	}

	public int getProgressGen() {
		return progressGen;
	}

	public void setProgressGen(int progressGen) {
		this.progressGen = progressGen;
	}



}
