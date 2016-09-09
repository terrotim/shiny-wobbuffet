package fireemblemfates.objects;

import java.util.HashSet;

public class FirstGenCharacter {
	double[] growthRates = new double[8];
	int[] statModifiers = new int[7];
	int[][] pairupStats = new int[4][7];
	String[] classSet = new String[5];
	String[] fullClassSet = new String[15];
	HashSet<String> skillSet = new HashSet<String>();
	HashSet<String> supportSet = new HashSet<String>();
	String name = new String();
	
	
	
	public FirstGenCharacter(){
		this.setGrowthRates(new double[] {0,0,0,0,0,0,0,0});
		this.setStatModifiers(new int[] {0,0,0,0,0,0,0});
		this.setPairupStats(new int[][] {{0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0}});
	}
	
	public FirstGenCharacter(String name, double[] growthRates, int[] statModifiers,
			int[][] pairupStats, String[] classSet, HashSet<String> supportSet) {
		super();
		this.setGrowthRates(growthRates);
		this.setStatModifiers(statModifiers);
		this.setPairupStats(pairupStats);
		this.setClassSet(classSet);
		this.setSupportSet(supportSet);
		this.setName(name);
	}
	
	public double[] getGrowthRates() {
		return growthRates;
	}



	public void setGrowthRates(double[] growthRates) {
		this.growthRates = growthRates;
	}



	public int[] getStatModifiers() {
		return statModifiers;
	}



	public void setStatModifiers(int[] statModifiers) {
		this.statModifiers = statModifiers;
	}



	public int[][] getPairupStats() {
		return pairupStats;
	}



	public void setPairupStats(int[][] pairupStats) {
		this.pairupStats = pairupStats;
	}



	public HashSet<String> getSkillSet() {
		return skillSet;
	}



	public void setSkillSet(HashSet<String> skillSet) {
		this.skillSet = skillSet;
	}



	public String[] getClassSet() {
		return classSet;
	}



	public void setClassSet(String[] classSet) {
		this.classSet = classSet;
	}



	public String[] getFullClassSet() {
		return fullClassSet;
	}

	public void setFullClassSet(String[] classSet) {
		this.fullClassSet = classSet;
	}

	public HashSet<String> getSupportSet() {
		return supportSet;
	}



	public void setSupportSet(HashSet<String> supportSet) {
		this.supportSet = supportSet;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}
	
}
