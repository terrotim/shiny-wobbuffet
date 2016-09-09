package fireemblemfates.objects;

import java.util.HashSet;

public class UnitClass {
	double[] growthRates = new double[8];
	int[] maximumStats = new int[9];
	int[] pairupStats = new int[8];
	HashSet<String> skillSet = new HashSet<String>();
	HashSet<String> promotions = new HashSet<String>();
	String className = new String();
	String parallelClass = new String();
	
	public UnitClass(){
		this.setGrowthRates(new double[] {0,0,0,0,0,0,0,0});
		this.setMaximumStats(new int[] {0,0,0,0,0,0,0,0,0});
		this.setPairupStats(new int[] {0,0,0,0,0,0,0,0});
	}
	

	public UnitClass(String className, double[] growthRates, int[] maximumStats,
			int[] pairupStats, HashSet<String> promotions, HashSet<String> skillSet, String parallelClass) {
		super();
		this.setGrowthRates(growthRates);
		this.setMaximumStats(maximumStats);
		this.setPairupStats(pairupStats);
		this.setPromotions(promotions);
		this.setSkillSet(skillSet);
		this.setClassName(className);
		this.setParallelClass(parallelClass);
	}


	public double[] getGrowthRates() {
		return growthRates;
	}

	public void setGrowthRates(double[] growthRates) {
		this.growthRates = growthRates;
	}

	public int[] getMaximumStats() {
		return maximumStats;
	}

	public void setMaximumStats(int[] maximumStats) {
		this.maximumStats = maximumStats;
	}

	public int[] getPairupStats() {
		return pairupStats;
	}

	public void setPairupStats(int[] pairupStats) {
		this.pairupStats = pairupStats;
	}

	public HashSet<String> getSkillSet() {
		return skillSet;
	}

	public void setSkillSet(HashSet<String> skillSet) {
		this.skillSet = skillSet;
	}
	
	public HashSet<String> getPromotions() {
		return promotions;
	}


	public void setPromotions(HashSet<String> promotions) {
		this.promotions = promotions;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}


	public String getParallelClass() {
		return parallelClass;
	}


	public void setParallelClass(String parallelClass) {
		this.parallelClass = parallelClass;
	}
}
