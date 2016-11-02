package com.dnastring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ExpectationMaximization {
	private ArrayList<String> dnaDatabase = new ArrayList<String>();
	private int minimumPatternLength;
	private int dnaStringLength;
	private double[][] positionWeightMatrix;
	private double[][] oldPositionWeightMatrix;
	private double[][] motifLocationMatrix;
	private int iterations;
	private int ALPHABET_SIZE = 4;
	private HashMap<Character,Integer> alphabetMap = new HashMap<Character, Integer>();
	

	public ExpectationMaximization(ArrayList<String> dnaDatabase,
			int minimumPatternLength, int dnaStringLength, int generations) {
		this.setDnaDatabase(dnaDatabase);
		this.setMinimumPatternLength(minimumPatternLength);
		this.setDnaStringLength(dnaStringLength);
		this.setIterations(generations);
		this.setPositionWeightMatrix(new double[ALPHABET_SIZE][getMinimumPatternLength()+1]);
		this.setMotifLocationMatrix(new double[dnaDatabase.size()][(dnaStringLength-minimumPatternLength)+1]);
		alphabetMap.put('A', 0);
		alphabetMap.put('C', 1);
		alphabetMap.put('T', 2);
		alphabetMap.put('G', 3);
		initializePositionWeightMatrix();
	}


	private void initializePositionWeightMatrix() {
		int j;
		int rowNum;
		int numSubstrings;
		String substring = new String();
		String dnaString = new String();
		
		numSubstrings = 0;
		for(int i = 0; i<getDnaDatabase().size();i++){
			j = 0;
			dnaString = getDnaDatabase().get(i);
			while(j + getMinimumPatternLength() < getDnaStringLength()){
				substring = dnaString.substring(j, j+getMinimumPatternLength());
				for (int k = 0; k<getMinimumPatternLength(); k++){
					rowNum = alphabetMap.get(substring.charAt(k));
					positionWeightMatrix[rowNum][k+1]++;
					positionWeightMatrix[rowNum][0]++;
				}
				j++;
				numSubstrings++;
			}
		}
		for (int i = 0; i<positionWeightMatrix.length; i++){
			for(int k = 0; k<positionWeightMatrix[i].length; k++){
				positionWeightMatrix[i][k] = positionWeightMatrix[i][k]/numSubstrings;	
				if(k == 0){
					positionWeightMatrix[i][k] = positionWeightMatrix[i][k]/minimumPatternLength;
				}
			}
		}
	}
	

	public String discoverPattern() {
		double deltaP = 1000;
		double error = 0.05;

		while(deltaP>error){
		setMotifLocationMatrix(calculateEstep(getPositionWeightMatrix()));
		setPositionWeightMatrix(calculateMstep(getMotifLocationMatrix()));
		deltaP = getDeltaP(getPositionWeightMatrix(), getOldPositionWeightMatrix());
		}
		double[][] pwm = getPositionWeightMatrix();
		String alphabet = new String();
		String pattern = new String();
		alphabet = "ACTG";
		for (int j = 1; j<pwm[0].length; j++){
			double tempProb = 0;
			char tempChar = ' ';
			for(int i = 0; i<pwm.length; i++){
				if(pwm[i][j] > tempProb){
					tempProb = pwm[i][j];
					tempChar = alphabet.charAt(i);
				}
			}
			pattern = pattern + tempChar;
		}
		return pattern;
	}

	private double getDeltaP(double[][] p,
			double[][] p2) {
		double total=0;
		for (int i = 0; i<p.length; i++){
			for (int j=0; j<p[i].length; j++){
				total = total + (Math.abs(p[i][j] - p2[i][j]));
			}
		}
		return total;
	}


	private double[][] calculateEstep(double[][] p) {
		double[][] z = new double[dnaDatabase.size()][(dnaStringLength-minimumPatternLength)+1];
		
		for (int i = 0; i<z.length; i++){ //for each sequence in dnadatabase
			for(int j = 0; j<z[i].length; j++){ //for each position a motif can start in sequence
				double prob = 1;
				int motifPos = 1;
				for (int k = 0; k<getDnaStringLength(); k++){ //for every spot in a dnasequence
					if(k>=j && k<j+minimumPatternLength){
						prob = prob * p[alphabetMap.get(dnaDatabase.get(i).charAt(j))][motifPos];
						motifPos++;
					} else {
						prob = prob * p[alphabetMap.get(dnaDatabase.get(i).charAt(j))][0];
					}
				}
				z[i][j] = prob;
			}
		}
		z = normalize(z);
		return z;
	}

	private double[][] normalize(double[][] z) {
		double total, normFactor;
		for (int i = 0; i<z.length; i++){
			total = 0;
			for(int j = 0; j<z[i].length; j++){
				total = total + z[i][j];
			}
			normFactor = 1/total;
			for(int j = 0; j<z[i].length;j++){
				z[i][j] = z[i][j]*normFactor;
			}
		}
		return z;
	}


	private double[][] calculateMstep(double[][] z) {
		double[][] p = new double[ALPHABET_SIZE][getMinimumPatternLength()+1];
		ArrayList<String> dnaList = new ArrayList<String>();
		String dnaString = new String();
		dnaList = this.getDnaDatabase();
		double denom;
		char tempChar;
		int numSubstrings;
		
		for(int c = 0; c < p.length; c++){
			for(int k = 1; k < p[c].length; k++){
				denom = 0;
				numSubstrings = 0;
				for (int i = 0; i<z.length;i++){
					for(int j = 0; j<z[i].length; j++){
						dnaString = dnaList.get(i);
						tempChar = dnaString.charAt(j+(k-1));
						if (c == alphabetMap.get(tempChar)){
							p[c][k] = p[c][k] + z[i][j];
							p[c][0]++;
						}
						denom = denom + z[i][j];
						numSubstrings++;
					}
				}
				p[c][k] = p[c][k]/denom;
				p[c][0] = p[c][0]/numSubstrings;
			}
		}
		setOldPositionWeightMatrix(getPositionWeightMatrix());
		return p;
	}
	
	public ArrayList<String> getDnaDatabase() {
		return dnaDatabase;
	}

	public void setDnaDatabase(ArrayList<String> dnaDatabase) {
		this.dnaDatabase = dnaDatabase;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public int getMinimumPatternLength() {
		return minimumPatternLength;
	}

	public void setMinimumPatternLength(int minimumPatternLength) {
		this.minimumPatternLength = minimumPatternLength;
	}

	public double[][] getPositionWeightMatrix() {
		return positionWeightMatrix;
	}

	public void setPositionWeightMatrix(double[][] positionWeightMatrix) {
		this.positionWeightMatrix = positionWeightMatrix;
	}

	public int getDnaStringLength() {
		return dnaStringLength;
	}

	public void setDnaStringLength(int dnaStringLength) {
		this.dnaStringLength = dnaStringLength;
	}

	public double[][] getMotifLocationMatrix() {
		return motifLocationMatrix;
	}

	public void setMotifLocationMatrix(double[][] motifLocationMatrix) {
		this.motifLocationMatrix = motifLocationMatrix;
	}

	public double[][] getOldPositionWeightMatrix() {
		return oldPositionWeightMatrix;
	}

	public void setOldPositionWeightMatrix(double[][] oldPositionWeightMatrix) {
		this.oldPositionWeightMatrix = oldPositionWeightMatrix;
	}
}
